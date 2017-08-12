package com.example.ian.rentermanager11;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ian on 2017/8/2 0002.
 */

public class myDatabaseHelper extends SQLiteOpenHelper {
    private static myDatabaseHelper instance;
    public static final String CREATE_ADMIN="create table admin(id integer primary key autoincrement,name text,password text)";//创建管理员表
    public static final String CREATE_RENTER = "create table renter(id text primary key,name text,room text,sex text,company text,phone text,idcard text,member text)";//创建租户信息表
    public static final String CREATE_HOUSE = "create table house(id text primary key,room text,status text,type text,area text)";

    private myDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADMIN);
        db.execSQL(CREATE_RENTER);
        db.execSQL(CREATE_HOUSE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static myDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new myDatabaseHelper(context, "RenterManagement.db", null, 3);
        }
        return instance;

    }
}
