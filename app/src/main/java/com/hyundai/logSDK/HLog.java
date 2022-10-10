package com.hyundai.logSDK;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hyundai.logSDK.util.DBHelper;
import com.hyundai.logSDK.util.network.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HLog implements HttpHelper.HttpListener {

    private static String TAG = "LOG_SDK";

    private static HLog instance;
    private static Context context;

    final DBHelper dbHelper;
    final HttpHelper httpHelper;

    private HLog(Context context){

        // DB
        dbHelper = new DBHelper(context);
        dbHelper.open();
        dbHelper.create();

        // Network
        httpHelper = new HttpHelper();
    }

    private static void init(Context context){
        if(null == instance){
            instance = new HLog(context);
        }
    }

    public static int d(Context context, String tag, String msg){

        init(context);
        return instance.save(0, tag, msg);
    }

    public static int httpDoGet(Context context, String tag, String msg){

        init(context);
        return instance.httpDoGet(tag, msg);
    }

    public int httpDoGet(String tag, String msg){
        Log.d(TAG, "httpDoGet()");
        Map<String, String> params = new HashMap<>();
        params.put(tag,msg);
        instance.httpHelper.setHttpListener(this);
        instance.httpHelper.doGet(params);
        return 0;
    }

    public static int httpDoPost(Context context, String tag, String msg){

        init(context);
        return instance.httpDoPost(tag, msg);
    }

    public int httpDoPost(String tag, String msg){
        Log.d(TAG, "httpDoPost()");

        JSONObject json = new JSONObject();
        try {
            json.put(tag, msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        instance.httpHelper.setHttpListener(this);
        instance.httpHelper.doPost(json);
        return 0;
    }

    public static void send(Context context){

        init(context);

        if(null == instance || null == instance.dbHelper) return; // 앱을 종료 후 show 부터 실행하면 null인 현상이 있음


        JSONArray jsonArr = new JSONArray();
        Cursor csr = instance.dbHelper.selectColumns();
        while(csr.moveToNext()){
            LogData hLog = new LogData();
            String id = csr.getString(0);
            String dt = csr.getString(1);
            String tag = csr.getString(2);
            String msg = csr.getString(3);
            int result = csr.getInt(4);
            hLog.setLog(id, dt, tag, msg, result);

            jsonArr.put(hLog.toJSON());
        }

        JSONObject reqJson = new JSONObject();
        try {
            reqJson.put("LOG_LIST", jsonArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "JSON:" + reqJson.toString());
        instance.httpHelper.setHttpListener(instance);
        instance.httpHelper.doPost(reqJson);
    }

    private int save(int DEBUG, String tag, String msg){
        dbHelper.insert(tag, msg);
        show();
        return 0;
    }

    public void log(String tag, String text){
        dbHelper.insert(tag, text);
    }

    public static void show(){

        if(null == instance || null == instance.dbHelper) return; // 앱을 종료 후 show 부터 실행하면 null인 현상이 있음

        LogData hlog = new LogData();
        Cursor csr = instance.dbHelper.selectColumns();
        while(csr.moveToNext()){
            String id = csr.getString(0);
            String dt = csr.getString(1);
            String tag = csr.getString(2);
            String msg = csr.getString(3);
            int result = csr.getInt(4);
            hlog.setLog(id, dt, tag, msg, result);
            Log.d(TAG, "hlog.show(): " + hlog.toString());
        }
    }



    public void toast(){
        Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(int resCode, JSONObject res) {
        Log.d(TAG, "onResponse()");
        Log.d(TAG, ">" + resCode + ", "+ res);
    }
}
