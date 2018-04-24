package com.example.tiwari.studence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    Button attend,shce,analy,pd;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        attend=(Button)findViewById(R.id.attendence);
        shce=(Button)findViewById(R.id.schedule);
        analy=(Button)findViewById(R.id.analysis);
        pd=(Button)findViewById(R.id.pdf);

        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent =new Intent(getApplicationContext(),Attendance.class);
                startActivity(intent);
            }
        });
        shce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent =new Intent(getApplicationContext(),Attendance.class);
                startActivity(intent);
            }
        });
        analy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent =new Intent(getApplicationContext(),Attendance.class);
                startActivity(intent);
            }
        });
        pd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent =new Intent(getApplicationContext(),Attendance.class);
                startActivity(intent);
            }
        });

    }
}
