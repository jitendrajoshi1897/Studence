package com.example.shubh.studence;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SigninClass extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinnerview;
    String [] classes;
    SQLiteManager sqLiteManager;
    Button submit;
    Intent intent;
    private String temp;
    int k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_class);
        submit =(Button)findViewById(R.id.submit);
        spinnerview = (Spinner)findViewById(R.id.select_class);
        sqLiteManager = new SQLiteManager(this);
        Cursor cname = sqLiteManager.getclassdata();
        classes =new String[cname.getCount()];
        while(cname.moveToNext()) {
            classes[k] = cname.getString(1);
            k++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(SigninClass.this, android.R.layout.simple_list_item_1, classes);
        spinnerview.setAdapter(adapter);
        spinnerview.setOnItemSelectedListener(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent =new Intent(getApplicationContext(),Student_verifcation.class);
                intent.putExtra("DATA",spinnerview.getSelectedItem().toString());
                startActivity(intent);
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
}
