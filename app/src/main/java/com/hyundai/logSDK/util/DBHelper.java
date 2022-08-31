package com.hyundai.logSDK.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHelper extends DBConfig {

    private SQLiteDatabase db;
    private DatabaseHelper helper;
    private Context context;

    private String logId;

    public DBHelper(Context context){
        this.context = context;
    }

    public DBHelper open() throws SQLException {
        helper = new DatabaseHelper(context, DATABASE_NAME, null, _DATABASE_VERSION);
        db = helper.getWritableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    public void create(){
        helper.onCreate(db);
    }

    public long insert(String tag, String contents){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        ContentValues values = new ContentValues();
        values.put(LOG_ID, logId);
        values.put(LOG_DT, dateFormat.format(date));
        values.put(TAG, tag);
        values.put(MSG, contents);
        return db.insert(TABLE_NAME, null, values);
    }

    public Cursor selectColumns(){
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_QUERY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public void setLogId(String logId){
        this.logId = logId;
    }

    public String getLogId(){
        return logId;
    }
}
