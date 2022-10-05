package com.hyundai.logSDK;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hyundai.logSDK.util.DBHelper;
import com.hyundai.logSDK.util.HttpHelper;

import org.json.JSONObject;

public class HLog implements HttpHelper.HttpListener {

    private static String TAG = "LOG_SDK";

    private static HLog instance;
    private static Context context;
    private LogData hlog;

    BackgroundTask backgroundTask;

    final DBHelper dbHelper;
    final HttpHelper httpHelper;

    private HLog(){

        // DB
        dbHelper = new DBHelper(context);
        dbHelper.open();
        dbHelper.create();

        // Network
        httpHelper = new HttpHelper();
        httpHelper.setHttpListener(new HttpHelper.HttpListener() {
            @Override
            public void onResponse(int resCode, JSONObject res) {

            }
        });
    }

    private static void init(Context context){
        if(null == instance){
            instance = new HLog();
        }
    }

    public static int d(Context context, String tag, String msg){

        init(context);

        return instance.save(0, tag, msg);
    }

    public static void send(Context context){

        init(context);

        if(null == instance.dbHelper) return; // 앱을 종료 후 send() 부터 실행하면 null인 현상이 있음

        LogData hlog = new LogData();
        Cursor csr = instance.dbHelper.selectColumns();
        while(csr.moveToNext()){
            String id = csr.getString(0);
            String dt = csr.getString(1);
            String tag = csr.getString(2);
            String contents = csr.getString(3);

            hlog.setLog(id, dt, tag, contents);
            Log.d(TAG, "hlog.show(): " + hlog);
            instance.httpHelper.doPost(null);
        }
    }

    private int save(int DEBUG, String tag, String msg){
        dbHelper.insert(tag, msg);
        show();
        return 0;
    }

    public static HLog getInstance(){
        if(null == instance){
            instance = new HLog();
        }
        return instance;
    }

    public void log(String tag, String text){
        dbHelper.insert(tag, text);
    }

    public void show(){

        if(null == dbHelper) return; // 앱을 종료 후 show 부터 실행하면 null인 현상이 있음

        LogData hlog = new LogData();
        Cursor csr = dbHelper.selectColumns();
        while(csr.moveToNext()){
            String id = csr.getString(0);
            String dt = csr.getString(1);
            String tag = csr.getString(2);
            String contents = csr.getString(3);

            hlog.setLog(id, dt, tag, contents);
            Log.d(TAG, "hlog.show(): " + hlog);
        }
    }



    public void toast(){
        Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
    }



    public void bgToast(){
        backgroundTask.execute();
    }

    @Override
    public void onResponse(int resCode, JSONObject res) {



    }

    //    https://velog.io/@haero_kim/Android-AsyncTask-%EA%B0%80-%EB%96%A0%EB%82%98%EA%B0%84-%EC%9D%B4%EC%9C%A0
    class BackgroundTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... integers) {
            for(int i = 0 ; i<10 ; i++){
                Log.d(TAG, ">" + i);
//                Toast.makeText(context, ">"+i, Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return 1;
        }

    }


}
