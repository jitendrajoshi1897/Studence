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
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.acpl.access_computech_fm220_sdk.FM220_Scanner_Interface;
import com.acpl.access_computech_fm220_sdk.acpl_FM220_SDK;
import com.acpl.access_computech_fm220_sdk.fm220_Capture_Result;
import com.acpl.access_computech_fm220_sdk.fm220_Init_Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Student_verifcation extends AppCompatActivity implements FM220_Scanner_Interface {

    private static final String ACTION_USB_PERMISSION = "com.example.shubh.myapplication.USB_PERMISSION";
    public acpl_FM220_SDK FM220SDK;
    private static final String Telecom_Device_Key = "0x8225";
    private byte[] t1;
    UsbManager manager;
    PendingIntent mpendingintent;
    UsbDevice usb_Dev;
    ImageView Fingerprint;
    TextView studentdata,status,devicestatus;
    Button next;
    private static Cursor database;
    SQLiteManager sqLiteManager;
    private String temp;
    Intent refresh;
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
                    devicestatus.setText("FM220 disconnected");
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
                                    devicestatus.setText("FM220 ready. "+res.getSerialNo());
                                    //EnableCapture();
                                    Log.e("message", "Device is ready"+res.getSerialNo());
                                }
                                else {
                                    devicestatus.setText("Error :-"+res.getError());
                                    //DisableCapture();
                                    Log.e("message", "Device is not ready"+ res.getError());
                                }
                            }
                        }
                    } else {
                        devicestatus.setText("User Blocked USB connection");
                        devicestatus.setText("FM220 ready");
                        //DisableCapture();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_verifcation);
        Fingerprint =(ImageView)findViewById(R.id.imagedata);
        status = (TextView)findViewById(R.id.status);
        devicestatus =(TextView)findViewById(R.id.device) ;
        studentdata =(TextView)findViewById(R.id.data);
        next = (Button)findViewById(R.id.next);
        sqLiteManager = new SQLiteManager(this);
        temp = getIntent().getStringExtra("DATA");
        database = sqLiteManager.getstudent(temp);
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
                    devicestatus.setText("FM220 requesting permission");
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
                        devicestatus.setText("FM220 ready. "+res.getSerialNo());
                        //EnableCapture();
                        Log.e("message", "Device is ready"+res.getSerialNo());

                    }
                    else {
                        devicestatus.setText("Error :-"+res.getError());
                        //DisableCapture();
                        Log.e("message", "Device is not ready"+ res.getError());
                    }
                }
                break;
            }
        }
        FM220SDK.CaptureFM220(2,true,true);
    }
    public void verification() {
        Log.e("databse", "verification on progress ");
        while (database.moveToNext()) {
           String name = database.getString(1);
            String roll = database.getString(2);
            String cla = database.getString(3);
            byte[] templete = database.getBlob(4);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat day = new SimpleDateFormat("EEEE");
            String Day = day.format(c.getTime());
            SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
            String Date = date.format(c.getTime());
            SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
            String Time = time.format(c.getTime());
            Log.e("databse", " " + database.getCount());
            if(fingerprintmatch(t1,templete))
            {
                status.setText("Student Present");
                //studentdata.setText(""+ name+" "+roll+" "+" "+cla);
                studentdata.setText(""+ name+" "+roll+" "+" "+cla+" "+ Day+" "+ Date+" "+ Time);
                sqLiteManager.insertpresentstudent(Date,Day,Time,name,roll,cla);
                return;
            }
        }
        status.setText("Student Not Registered");
        return;
    }
    public boolean fingerprintmatch(byte[] t1,byte[] t2)
    {
        if (t1 != null && t2 != null) {
            if (FM220SDK.MatchFM220(t1, t2)) {
                //testcode.setText("Finger matched");
                t1 = null;
                t2 = null;
                return true;
            } else {
                //testcode.setText("Finger not matched");
                return false;
            }
            //FunctionBase64();
        } else {
            //testcode.setText("Pl capture first");
            return false;
        }
    }
    @Override
    public void ScannerProgressFM220(final boolean DisplayImage,final Bitmap ScanImage,final boolean DisplayText,final String statusMessage) {
        Student_verifcation.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (DisplayText) {
                    devicestatus.setText(statusMessage);
                    devicestatus.invalidate();

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
        Student_verifcation.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (FM220SDK.FM220Initialized())
                    //EnableCapture();
                    if (result.getResult()) {
                        Fingerprint.setImageBitmap(result.getScanImage());
                        byte [] isotem  = result.getISO_Template();   // ISO TEMPLET of FingerPrint.....
//                    isotem is byte value of fingerprints
                        t1 = result.getISO_Template();
                        devicestatus.setText("Success NFIQ:"+Integer.toString(result.getNFIQ())+"  SrNo:"+result.getSerialNo());

                        if(database!=null)
                        {
                            verification();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    refresh = new Intent(getApplicationContext(),Student_verifcation.class);
                                    refresh.putExtra("DATA",temp);
                                    startActivity(refresh);
                                    finish();
                                }
                            }, 5000);

                        }
                    } else {
                        Fingerprint.setImageBitmap(null);
                        devicestatus.setText(result.getError());
                    }
                Fingerprint.invalidate();
                devicestatus.invalidate();
            }
        });
    }

    @Override
    public void ScanMatchFM220(fm220_Capture_Result fm220_capture_result) {

    }
}
