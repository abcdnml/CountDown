package com.aaa.cd.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by aaa on 2016/9/3.
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final String CREATE_LOG_TABLE="create table log ("
            +"  id INTEGER NOT NULL  PRIMARY KEY  AUTOINCREMENT DEFAULT 0, "
            +"  title VARCHAR, "
            +"  content TEXT, "
            +"  duration TEXT, "
            +"  time INTEGER);";
    private static final int DB_VERSION=1;
    private static final String DB_NAME="CountDown.db";
    static Context context;

    private static DBHelper helper;

    public static void init(Context ctx){
        context=ctx.getApplicationContext();
    }
    public static DBHelper getInstance(){
        if(helper==null){
            helper=new DBHelper();
        }
        return helper;
    }

    private DBHelper() {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_LOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
