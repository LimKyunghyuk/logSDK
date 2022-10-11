package com.hyundai.logSDK;

import org.json.JSONException;
import org.json.JSONObject;

public class LogData {

    String logId;
    String logDt;
    String tag;
    String msg;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getLogDt() {
        return logDt;
    }

    public void setLogDt(String logDt) {
        this.logDt = logDt;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setLog(String logId, String logDt, String tag, String msg){
        this.logId = logId;
        this.logDt = logDt;
        this.tag = tag;
        this.msg = msg;
    }

    public JSONObject toJSON(){

        JSONObject json = new JSONObject();
        try {
            json.put("LOG_ID", this.logId);
            json.put("LOG_DT", this.logDt);
            json.put("TAG",this.tag);
            json.put("MSG",this.msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public String toString() {
        return "HLog{" +
                "logId='" + logId + '\'' +
                ", logDt='" + logDt + '\'' +
                ", tag='" + tag + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}


