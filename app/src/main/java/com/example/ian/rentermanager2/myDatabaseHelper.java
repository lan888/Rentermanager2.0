package com.example.ian.rentermanager2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ian on 2017/8/2 0002.
 */

public class myDatabaseHelper extends SQLiteOpenHelper {
    private static myDatabaseHelper instance;
    public static final String CREATE_ADMIN="create table admin(id integer primary key autoincrement,name text,password text,time text)";//创建管理员表
    public static final String CREATE_RENTER = "create table renter(id integer primary key,name text,room text,sex text,company text,phone text,idcard text,member text,time text)";//创建租户信息表
    public static final String CREATE_HOUSE = "create table house(id integer primary key,room text,status text,type text,area text,time text)";
    public static final String CREATE_BILL = "create table bill(id integer primary key,room text,bill text,waterBill text,electricityBill text,time text,month text,total text)";

    private myDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADMIN);
        db.execSQL(CREATE_RENTER);
        db.execSQL(CREATE_HOUSE);
        db.execSQL(CREATE_BILL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static myDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new myDatabaseHelper(context, "RenterManagement.db", null, 4);
        }
        return instance;

    }
}
