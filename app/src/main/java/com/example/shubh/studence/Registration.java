package com.example.shubh.studence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {

    EditText username,password,emailid,dept;
    Button regi;
    Intent intent;
    public SQLiteManager sqLiteManager;
    boolean valid=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
        username = (EditText)findViewById(R.id.user);
        password = (EditText)findViewById(R.id.psss);
        emailid = (EditText)findViewById(R.id.email);
        dept = (EditText)findViewById(R.id.depte);
        regi = (Button)findViewById(R.id.regis);
        sqLiteManager = new SQLiteManager(this);
        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u= username.getText().toString();
                String p = password.getText().toString();
                String e = emailid.getText().toString();
                String d = dept.getText().toString();

                if(DepartmentValidator(d) && PasswordValidator(p) && emailValidator(e) && usernameValidator(u))
                {
                    sqLiteManager.insertuser(u,p,e,d);
                    intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                }

            }
        });

    }

    public boolean DepartmentValidator(String dept)
    {
        if(dept!=null)
        {
            return true;
        }
        Toast.makeText(getApplicationContext(),"Department Can't be Empty",Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean PasswordValidator(String pass)
    {
        int lenght = pass.length();
        if(lenght>=8)
        {
            return true;
        }
        Toast.makeText(getApplicationContext(),"Password is too short",Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean usernameValidator(String user)
    {
        if(user!=null)
        {
            return true;
        }
        Toast.makeText(getApplicationContext(),"Username Can't be Empty",Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
