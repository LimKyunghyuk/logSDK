package com.hyundai.logSDK.util.network;

import android.os.Handler;
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

public class HttpDoPost extends Thread{

    final String TAG = "LOG_SDK";

    public String CALL_SEVER_URL = "http://yorsild.dothome.co.kr/log.php";
    public int TIME_OUT = 3000;
    HttpHelper.HttpListener receiver;
    Handler handler;

    JSONObject paramJson;

    void setParams(JSONObject paramJson){
        this.paramJson = paramJson;
    }

    void setHandler(Handler handler){
        this.handler = handler;
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

            Log.d(TAG, "input: " + paramJson.toString());

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

            conn.disconnect();

        } catch (MalformedURLException e) {
            resCode = -1;
        } catch (IOException e) {
            resCode = -2;
        } finally {

            if(conn != null){
                conn.disconnect();
            }

            Log.d(TAG, "res : " + res);

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
        }

        Log.d(TAG, "-done-");
    }
}
