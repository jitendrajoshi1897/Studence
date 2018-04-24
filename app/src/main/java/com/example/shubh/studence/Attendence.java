package com.example.shubh.studence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Attendence extends AppCompatActivity {

    Button create,signin;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);
        create = (Button)findViewById(R.id.createclass);
        signin = (Button)findViewById(R.id.signinclass);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent =new Intent(getApplicationContext(),Create_class.class);
                startActivity(intent);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent =new Intent(getApplicationContext(),SigninClass.class);
                startActivity(intent);
            }
        });
    }
}
