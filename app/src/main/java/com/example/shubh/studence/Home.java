package com.example.shubh.studence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    Button attend,shce,analy,pd;
    Intent intent;
    public static int Flag_home=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        attend=(Button)findViewById(R.id.attendence);
        shce=(Button)findViewById(R.id.schedule);
        analy=(Button)findViewById(R.id.analysis);
        pd=(Button)findViewById(R.id.pdf);

        Flag_home=1;


        attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent =new Intent(getApplicationContext(),Attendence.class);
                startActivity(intent);
            }
        });
        shce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent =new Intent(getApplicationContext(),Attendence.class);
                startActivity(intent);
            }
        });
        analy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent =new Intent(getApplicationContext(),Attendence.class);
                startActivity(intent);
            }
        });
        pd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent =new Intent(getApplicationContext(),Create_pdf.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Home_screen();
                return true;
            case R.id.settings:
                Setting();
                return true;
            case R.id.about:
                About();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Home_screen()
    {
        if(Flag_home!=1)
        {
            intent =new Intent(getApplicationContext(),Home.class);
            startActivity(intent);
        }
    }
    public void Setting()
    {

        intent =new Intent(getApplicationContext(),Attendence.class);
        startActivity(intent);
    }
    public void About()
    {

        intent =new Intent(getApplicationContext(),Attendence.class);
        startActivity(intent);
    }
}
