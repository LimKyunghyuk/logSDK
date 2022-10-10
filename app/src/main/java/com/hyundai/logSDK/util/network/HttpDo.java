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
import java.util.Map;

public class HttpDo extends Thread{

    final String TAG = "LOG_SDK";

    Handler handler;
    String HTTP_URL;
    private int TIME_OUT;
    private final String type;

    HttpDo(String type){
        this.type = type;
    }

    @Override
    public void run() {

        try {
            HttpURLConnection conn = setHttpHeader();
            setHttpBody(conn, setBodyString());
            setResponse(conn);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setURL(String url){
        this.HTTP_URL = url;
    }

    public void setTimeOut(int timeOut){
        this.TIME_OUT = timeOut;
    }

    public void setHttpParams(){

    }

    void setParam(Object param){

    }

    public String setUrlPrams(){

        return "";
    }


    public HttpURLConnection setHttpHeader() throws IOException {

        Log.d(TAG, "URL: " + HTTP_URL + setUrlPrams());
        URL url = new URL(HTTP_URL + setUrlPrams());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(type);
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(TIME_OUT);
        conn.setReadTimeout(TIME_OUT);
        conn.setDoOutput(true);
        conn.setDoInput(true);

        return conn;
    }



    void setHttpBody(HttpURLConnection conn, String body) throws IOException {

        if(body != null){
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = body.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }
        }
    }

    String setBodyString(){

        return "";
    }


    void setResponse(HttpURLConnection conn) throws IOException {
        String res = "";

        // 호출 후 응답 코드 수신
        int resCode = conn.getResponseCode();
        
        // 본문 파싱
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

        // 결과를 메시지에 담아서 콜백
        Message msg = handler.obtainMessage();
        msg.arg1 = resCode;
        msg.obj = getResponse(res);
        handler.sendMessage(msg);
    }

    void setHandler(Handler handler){
        this.handler = handler;
    }

    Object getResponse(String res){

        if(res != null){
            JSONObject responseJSON = null;
            try {
                responseJSON = new JSONObject(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return responseJSON;
        }

        return "";
    }
}
