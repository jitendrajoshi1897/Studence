package com.example.shubh.studence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    TextView textView;
    EditText username,pass;
    Intent intent;
    Button login;
    SQLiteManager sqLiteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        username = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        sqLiteManager = new SQLiteManager(this);
        textView=(TextView) findViewById(R.id.register);
        login =(Button)findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String u =username.getText().toString();
                String p =pass.getText().toString();

                Cursor data = sqLiteManager.getuser(u,p);

                if(data.getCount()>0)
                {
                    Log.e("database","Successfull");
                    intent = new Intent(getApplicationContext(), Authenticate.class);
                    startActivity(intent);
                    finish();
                }*/


                intent = new Intent(getApplicationContext(), Authenticate.class);
                startActivity(intent);

            }
        });

        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                intent =new Intent(getApplicationContext(),Registration.class);
                startActivity(intent);
                return false;
            }
        });
    }
    }
