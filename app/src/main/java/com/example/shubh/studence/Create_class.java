package com.example.shubh.studence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Create_class extends AppCompatActivity {

    EditText class_name,Dept,No_of_student;
    Button go;
    Intent intent;
    SQLiteManager sqLiteManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        class_name = (EditText)findViewById(R.id.classname);
        Dept = (EditText)findViewById(R.id.classdept);
        No_of_student = (EditText)findViewById(R.id.number);
        sqLiteManager = new SQLiteManager(this);
        go = (Button)findViewById(R.id.go_btn);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   /*String c = class_name.getText().toString();
                    String d = Dept.getText().toString();
                    String n = No_of_student.getText().toString();
                    sqLiteManager.insertclass(c,d,Integer.parseInt(n));*/
                intent = new Intent(getApplicationContext(),Student_Data.class);
                startActivity(intent);

            }
        });
    }
}
