package com.hyundai.makeLogSDK;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hyundai.logSDK.HLog;
import com.hyundai.logSDK.util.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    int cnt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        HLog.getInstance().setInit(this);
//        LogManager.getInstance().setContext(this).toast();
//        LogManager.getInstance().setContext(this).bgToast();

        ((Button) findViewById(R.id.btn1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "send()", Toast.LENGTH_SHORT).show();
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

        ((Button) findViewById(R.id.btn5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "btn5", Toast.LENGTH_SHORT).show();
                HLog.send(getApplicationContext());
            }
        });

        ((Button) findViewById(R.id.btn6)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Network get()", Toast.LENGTH_SHORT).show();
                HLog.httpDoGet(getApplicationContext(), "LOG_TEST","hello");
            }
        });

        ((Button) findViewById(R.id.btn7)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Network post()", Toast.LENGTH_SHORT).show();
                HLog.httpDoPost(getApplicationContext(), "LOG_TEST","hello");
            }
        });

        ((Button) findViewById(R.id.btn8)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "DB open()", Toast.LENGTH_SHORT).show();

                DBHelper dbHelper = new DBHelper(getApplicationContext());
                dbHelper.open();
                dbHelper.create();
            }
        });
    }
}