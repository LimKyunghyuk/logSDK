package com.hyundai.makeLogSDK;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hyundai.logSDK.HLog;

public class MainActivity extends AppCompatActivity {

    int cnt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        HLog.getInstance().setInit(this);
//        LogManager.getInstance().setContext(this).toast();
//        LogManager.getInstance().setContext(this).bgToast();




        Button btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "show()", Toast.LENGTH_SHORT).show();
                HLog.show();

            }
        });

        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "btn2", Toast.LENGTH_SHORT).show();
                HLog.d(getApplicationContext(), "LOG_TEST", "2>" + cnt++);
            }
        });

        Button btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "btn3", Toast.LENGTH_SHORT).show();
                HLog.d(getApplicationContext(), "LOG_TEST", "3>" + cnt++);
            }
        });

        Button btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "btn4", Toast.LENGTH_SHORT).show();
                HLog.d(getApplicationContext(), "LOG_TEST", "4>" + cnt++);
            }
        });

        Button btn5 = (Button) findViewById(R.id.btn5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "btn5", Toast.LENGTH_SHORT).show();
                HLog.d(getApplicationContext(), "LOG_TEST", "5>" + cnt++);
            }
        });
    }
}