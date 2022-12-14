package com.hyundai.logSDK;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.hyundai.logSDK.util.DBHelper;
import com.hyundai.logSDK.util.network.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HLog implements HttpHelper.HttpListener {

    private static final String TAG = "LOG_SDK";

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

        if(null == context){
            throw new RuntimeException("Context is null!");
        }

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

        if(null == instance || null == instance.dbHelper){
            throw new RuntimeException("Context not found.");
        }

        JSONArray jsonArr = new JSONArray();
        Cursor csr = instance.dbHelper.selectColumns();

        if(0 == csr.getCount()) return;

        while(csr.moveToNext()){

            String id = csr.getString(0);
            String dt = csr.getString(1);
            String tag = csr.getString(2);
            String msg = csr.getString(3);

            LogData hLog = new LogData();
            hLog.setLog(id, dt, tag, msg);
            jsonArr.put(hLog.toJSON());
        }

        JSONObject reqJson = new JSONObject();
        try {
            reqJson.put("LOG_LIST", jsonArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "JSON:" + reqJson);
        instance.httpHelper.setHttpListener(instance);
        instance.httpHelper.doPost(reqJson);
    }

    private int save(int DEBUG, String tag, String msg){
        dbHelper.insert(tag, msg);
        show();
        return 0;
    }

    public static void show(){

        if(null == instance || null == instance.dbHelper){
            throw new RuntimeException("Context not found.");
        }

        Cursor csr = instance.dbHelper.selectColumns();

        if(0 == csr.getCount()){
            Log.d(TAG, "No data found.");
            return;
        }

        while(csr.moveToNext()){

            String id = csr.getString(0);
            String dt = csr.getString(1);
            String tag = csr.getString(2);
            String msg = csr.getString(3);

            LogData hlog = new LogData();
            hlog.setLog(id, dt, tag, msg);
            Log.d(TAG, "hlog.show(): " + hlog);
        }
    }

    public static void show(Context context){
        init(context);
        show();
    }

    @Override
    public void onResponse(int resCode, JSONObject res) {
        Log.d(TAG, "onResponse()");
        Log.d(TAG, ">" + resCode + ", "+ res);

        try {
            if(resCode != 200) return;
            if(res != null && res.getInt("RES_CODE") != 200) return;

            JSONArray logList = res.getJSONArray("LOG_LIST");

            for(int i = 0 ; i < logList.length() ; i++){

                JSONObject log = logList.getJSONObject(i);

                if(log.getBoolean("RESULT")){
                    String logId = log.getString("LOG_ID");
                    instance.dbHelper.deleteLog(logId);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
