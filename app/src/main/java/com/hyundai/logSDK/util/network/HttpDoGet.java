package com.hyundai.logSDK.util.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class HttpDoGet extends Thread{

    final String TAG = "LOG_SDK";

    public String CALL_SEVER_URL = "http://yorsild.dothome.co.kr/call.php";
    public int TIME_OUT = 3000;
    HttpHelper.HttpListener receiver;
    Handler handler;

    Map<String, String> paramMap;

    void setParams(Map<String, String> paramMap){
        this.paramMap = paramMap;
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

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            resCode = -1;
        } catch (IOException e) {
            e.printStackTrace();
            resCode = -2;
        } finally {

            if(conn != null){
                conn.disconnect();
            }

            // 결과값 Message에 담아서 반납
            Message msg = handler.obtainMessage();

            JSONObject responseJSON = null;
            try {
                Log.d(TAG, res);
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
