package com.hyundai.logSDK.util;

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

    final String TAG = "CATCH_CALL";
    public String CALL_SEVER_URL = "http://yorsild.dothome.co.kr/call.php";
    public int TIME_OUT = 3000;
    private HttpListener receiver;

    public interface HttpListener {
        void onResponse(int resCode, JSONObject res);
    }

    public void setHttpListener(HttpListener receiver){
        this.receiver = receiver;
    }

    // Call back 핸들러. Http 응답을 Main Thread에게 전달한다.
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

        HttpDoGetService hs = new HttpDoGetService();
        hs.setParams(params);
        hs.start();
    }

    // Post 파라매터 세팅
    public void doPost(JSONObject paramJson){
        Log.d(TAG, "doPost()");

        HttpDoPostService hs = new HttpDoPostService();
        hs.setParams(paramJson);
        hs.start();
    }

    // 비동기 호출
    class HttpDoGetService extends Thread {

        Map<String, String> paramMap;

        void setParams(Map<String, String> paramMap){
            this.paramMap = paramMap;
        }

        @Override
        public void run() {

            HttpURLConnection conn = null;
            int resCode = 0;
            String res = "";
            String urlPrams = "";

            try {

                // GET 파라매터 세팅
                if(paramMap != null){
                    for(String key : paramMap.keySet()){

                        if(urlPrams.isEmpty()){
                            urlPrams += "?";
                        }else{
                            urlPrams += "&";
                        }

                        urlPrams += key;
                        urlPrams += "=";
                        urlPrams += paramMap.get(key);
                    }
                    Log.d(TAG, "Set url params: " + urlPrams);
                }

                // URL GET 호출
                Log.d(TAG, "URL: " + CALL_SEVER_URL + urlPrams);
                URL url = new URL(CALL_SEVER_URL + urlPrams);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(TIME_OUT);
                conn.setReadTimeout(TIME_OUT);
                conn.setDoOutput(true);
                conn.setDoInput(true);

                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    reader.close();
                    res = response.toString();
                }

                Message msg = handler.obtainMessage();

                msg.obj = new JSONObject(res);
                msg.arg1 = resCode;
                handler.sendMessage(msg);

                conn.disconnect();

            } catch (MalformedURLException e) {
                resCode = -1;
            } catch (IOException e) {
                resCode = -2;
            } catch (JSONException e) {
                resCode = -3;
            } finally {

                if(conn != null){
                    conn.disconnect();
                }
            }

            // 결과값 Message에 담아서 반납
            Message msg = handler.obtainMessage();

            JSONObject responseJSON = null;
            try {
                responseJSON = new JSONObject(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            msg.obj = responseJSON;
            msg.arg1 = resCode;
            handler.sendMessage(msg);

            Log.d(TAG, "-done-");
        }
    }

    // 비동기 호출
    class HttpDoPostService extends Thread {

        JSONObject paramJson;

        void setParams(JSONObject paramJson){
            this.paramJson = paramJson;
        }

        @Override
        public void run() {

            HttpURLConnection conn = null;
            int resCode = 0;
            String res = "";
            String urlPrams = "";

            try {

                Log.d(TAG, "URL: " + CALL_SEVER_URL + urlPrams);
                URL url = new URL(CALL_SEVER_URL + urlPrams);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(TIME_OUT);
                conn.setReadTimeout(TIME_OUT);
                conn.setDoOutput(true);
                conn.setDoInput(true);

                paramJson.toString();
                try(OutputStream os = conn.getOutputStream()) {
                    byte[] input = paramJson.toString().getBytes("UTF-8");
                    os.write(input, 0, input.length);
                }

                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    reader.close();
                    res = response.toString();
                }

                Message msg = handler.obtainMessage();

                msg.obj = new JSONObject(res);
                msg.arg1 = resCode;
                handler.sendMessage(msg);

                conn.disconnect();

            } catch (MalformedURLException e) {
                resCode = -1;
            } catch (IOException e) {
                resCode = -2;
            } catch (JSONException e) {
                resCode = -3;
            } finally {

                if(conn != null){
                    conn.disconnect();
                }
            }

            // 결과값 Message에 담아서 반납
            Message msg = handler.obtainMessage();

            JSONObject responseJSON = null;
            try {
                responseJSON = new JSONObject(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            msg.obj = responseJSON;
            msg.arg1 = resCode;
            handler.sendMessage(msg);

            Log.d(TAG, "-done-");
        }
    }
}
