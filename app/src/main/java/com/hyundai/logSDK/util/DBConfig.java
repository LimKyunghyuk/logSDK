package com.hyundai.logSDK.util;

public class DBConfig {

    public final String DATABASE_NAME = "LOG.db";
    public final String TABLE_NAME = "TB_LOG";

    public final int _DATABASE_VERSION = 3;

    public final String LOG_ID = "LOG_ID";
    public final String LOG_DT = "LOG_DT";
    public final String TAG = "TAG";
    public final String MSG = "MSG";
    public final String RESULT = "RESULT";



    public String CREATE_QUERY = "create table if not exists " + TABLE_NAME + "("
            + LOG_ID + " integer primary key autoincrement, "
            + LOG_DT + " date not null, "
            + TAG + " text not null, "
            + MSG + " text, "
            + RESULT + " BOOLEAN CHECK (" + RESULT + " IN (0, 1)) "
            + ");";
}
