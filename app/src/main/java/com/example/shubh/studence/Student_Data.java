package com.example.shubh.studence;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acpl.access_computech_fm220_sdk.FM220_Scanner_Interface;
import com.acpl.access_computech_fm220_sdk.acpl_FM220_SDK;
import com.acpl.access_computech_fm220_sdk.fm220_Capture_Result;
import com.acpl.access_computech_fm220_sdk.fm220_Init_Result;

import java.util.Arrays;

public class Student_Data extends AppCompatActivity implements AdapterView.OnItemSelectedListener,FM220_Scanner_Interface {


    private static final String ACTION_USB_PERMISSION = "com.example.shubh.myapplication.USB_PERMISSION";
    public acpl_FM220_SDK FM220SDK;
    private static final String Telecom_Device_Key = "0x8225";
    private byte[] t1,t2;
    UsbManager manager;
    PendingIntent mpendingintent;
    UsbDevice usb_Dev;
    EditText sname,rollno;
    Spinner selectclass;
    ImageView Fingerprint;
    TextView testcode;
    Button submitdata;
    String [] classes;
    SQLiteManager sqLiteManager;
    Intent refresh;
    static String temp;
    int k=0;

    private final BroadcastReceiver musbReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                int pid, vid;
                pid = device.getProductId();
                vid = device.getVendorId();

                if ((pid == 0x8225 || pid == 0x8220)  && (vid == 0x0bca)) {
                    FM220SDK.stopCaptureFM220();
                    FM220SDK.unInitFM220();
                    usb_Dev=null;
                    testcode.setText("FM220 disconnected");
                    // DisableCapture();
                }
            }
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            // call method to set up device communication
                            int pid, vid;
                            pid = device.getProductId();
                            vid = device.getVendorId();
                            if ((pid == 0x8225 || pid == 0x8220)  && (vid == 0x0bca)) {
                                fm220_Init_Result res =  FM220SDK.InitScannerFM220(manager,device,Telecom_Device_Key);
                                if (res.getResult()) {
                                    testcode.setText("FM220 ready. "+res.getSerialNo());
                                    //EnableCapture();
                                    Log.e("message", "Device is ready"+res.getSerialNo());
                                }
                                else {
                                    testcode.setText("Error :-"+res.getError());
                                    //DisableCapture();
                                    Log.e("message", "Device is not ready"+ res.getError());
                                }
                            }
                        }
                    } else {
                        testcode.setText("User Blocked USB connection");
                        testcode.setText("FM220 ready");
                        //DisableCapture();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__data);
        sname =(EditText)findViewById(R.id.studentname);
        rollno = (EditText)findViewById(R.id.studentrollno);
        selectclass =(Spinner)findViewById(R.id.studentclass);
        Fingerprint =(ImageView)findViewById(R.id.studentfingerprint);
        testcode = (TextView)findViewById(R.id.testcode);
        submitdata = (Button) findViewById(R.id.submitdata);
        sqLiteManager = new SQLiteManager(this);
        Cursor cname = sqLiteManager.getclassdata();
        classes =new String[cname.getCount()];
        while(cname.moveToNext()) {
            classes[k] = cname.getString(1);
            k++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Student_Data.this, android.R.layout.simple_list_item_1, classes);
        selectclass.setAdapter(adapter);
        selectclass.setOnItemSelectedListener(this);

        SharedPreferences sp = getSharedPreferences("last_FM220_type", Activity.MODE_PRIVATE);
        boolean olddevicetype = sp.getBoolean("FM220type",true);
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        final Intent ipintent = new Intent(ACTION_USB_PERMISSION);
        if(Build.VERSION.SDK_INT >=16)
        {
            ipintent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        }
        mpendingintent = PendingIntent.getBroadcast(getBaseContext(),1,ipintent,0);

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        registerReceiver(musbReciver,filter);
        UsbDevice device =null;
        Log.e("message", ""+manager.getDeviceList().values());
        for ( UsbDevice mdevice : manager.getDeviceList().values()) {
            int pid, vid;
            pid = mdevice.getProductId();
            vid = mdevice.getVendorId();
            Log.e("message", pid + "    " + vid);

            boolean devtype;
            if ((pid == 0x8225) && (vid == 0x0bca)) {
                FM220SDK = new acpl_FM220_SDK(getApplicationContext(),this,false);
                devtype = true;
                Log.e("message", "  8225  " +devtype);
            }
            else if ((pid == 0x8220) && (vid == 0x0bca)) {
                FM220SDK = new acpl_FM220_SDK(getApplicationContext(),this,false);
                devtype=false;
                Log.e("message", "  8220  " +devtype);
            } else {
                FM220SDK = new acpl_FM220_SDK(getApplicationContext(),this,olddevicetype);
                devtype=olddevicetype;
                Log.e("message", " else   " +devtype);
                Log.e("message", " else   " +olddevicetype);
            }
            if (olddevicetype != devtype) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("FM220type", devtype);
                editor.apply();
                Log.e("message", " devtype!=olddevicetype");
            }
            if ((pid == 0x8225 || pid == 0x8220) && (vid == 0x0bca)) {
                device  = mdevice;
                if (!manager.hasPermission(device)) {
                    testcode.setText("FM220 requesting permission");
                    manager.requestPermission(device, mpendingintent);
                } else {
                    /*Intent intent = this.getIntent();
                    if (intent != null) {
                        if (intent.getAction().equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
                            finishAffinity();
                        }
                    }*/
                    fm220_Init_Result res =  FM220SDK.InitScannerFM220(manager,device,Telecom_Device_Key);
                    if (res.getResult()) {
                        testcode.setText("FM220 ready. "+res.getSerialNo());
                        //EnableCapture();
                        Log.e("message", "Device is ready"+res.getSerialNo());

                    }
                    else {
                        testcode.setText("Error :-"+res.getError());
                        //DisableCapture();
                        Log.e("message", "Device is not ready"+ res.getError());
                    }
                }
                break;
            }
        }
        FM220SDK.CaptureFM220(2,true,true);
        submitdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cls = selectclass.getSelectedItem().toString();
                String roll = rollno.getText().toString();
                String name = sname.getText().toString();

                if(cls!=null && roll!=null && name!=null)
                {
                    if(t1!=null)
                    {
                        sqLiteManager.insertstudent(roll,name,cls,t1);
                        Toast.makeText(getApplicationContext(),"Student Registered",Toast.LENGTH_LONG).show();
                        refresh = new Intent(getApplicationContext(),Student_Data.class);
                        startActivity(refresh);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Give your FingerPrint",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Fill all the fields",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        temp = classes[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void ScannerProgressFM220(final boolean DisplayImage,final Bitmap ScanImage,final boolean DisplayText,final String statusMessage) {
        Student_Data.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (DisplayText) {
                    testcode.setText(statusMessage);
                    testcode.invalidate();

                }
                if (DisplayImage) {
                    Fingerprint.setImageBitmap(ScanImage);
                    Fingerprint.invalidate();
                }
            }
        });
    }

    @Override
    public void ScanCompleteFM220(final fm220_Capture_Result result) {
        Student_Data.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (FM220SDK.FM220Initialized())
                    //EnableCapture();
                    if (result.getResult()) {
                        Fingerprint.setImageBitmap(result.getScanImage());
                        byte [] isotem  = result.getISO_Template();   // ISO TEMPLET of FingerPrint.....
//                    isotem is byte value of fingerprints
                        t1 = result.getISO_Template();
                        testcode.setText("Success NFIQ:"+Integer.toString(result.getNFIQ())+"  SrNo:"+result.getSerialNo());
                    } else {
                        Fingerprint.setImageBitmap(null);
                        testcode.setText(result.getError());
                    }
                Fingerprint.invalidate();
                testcode.invalidate();
            }
        });
    }

    @Override
    public void ScanMatchFM220(fm220_Capture_Result fm220_capture_result) {

    }
}
