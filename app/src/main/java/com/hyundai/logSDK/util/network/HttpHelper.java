package com.hyundai.logSDK.util.network;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class HttpHelper{

    final String TAG = "LOG_SDK";
    public String CALL_SEVER_URL = "http://yorsild.dothome.co.kr/call.php";
    public int TIME_OUT = 3000;
    private HttpListener receiver;

    public interface HttpListener {
        void onResponse(int resCode, JSONObject res);
    }

    public void setHttpListener(HttpListener receiver){
        this.receiver = receiver;
    }

    // 리스너와 핸들러를 연결한다.
    // Http 응답을 핸들러를 통해 리스너로 전달한다.
    // 핸들러를 통하기 때문에 Main Thread에게 전달 가능하다.
    Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            int resCode = msg.arg1;
            JSONObject resJson = (JSONObject)msg.obj;
            receiver.onResponse(resCode, resJson);
        }
    };

    // Get 파라매터 세팅
    public void doGet(Map<String, String> params){
        Log.d(TAG, "doGet()");

        HttpDoGet httpDoGet = new HttpDoGet();
        httpDoGet.setHandler(handler);
        httpDoGet.setParams(params);
        httpDoGet.start();
    }

    // Post 파라매터 세팅
    public void doPost(JSONObject paramJson){
        Log.d(TAG, "doPost()");

        HttpDoPost httpDoPost = new HttpDoPost();
        httpDoPost.setHandler(handler);
        httpDoPost.setParams(paramJson);
        httpDoPost.start();

    }
}
