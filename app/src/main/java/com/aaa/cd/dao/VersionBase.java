package com.aaa.cd.dao;

import android.database.sqlite.SQLiteDatabase;

public class VersionBase extends VersionUpdate
{
    private static final String CREATE_LOG_TABLE = "create table log (" + "  id INTEGER PRIMARY KEY  AUTOINCREMENT DEFAULT 0, " + "  title TEXT, " + "  content TEXT, " + "  remark TEXT, " + "  duration INTEGER, " + "  user_id INTEGER, " + "  time INTEGER);";
    private static final String CREATE_COUNT_DOWN_TABLE = "create table count_down(" + "  id INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0," + "  name TEXT, " + "  descrption TEXT," + "  time INTEGER);";

    private static final String CREATE_DOCUMENT_TABLE = "create table document(" + "  id INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0," + "  name TEXT, " + "  parent INTEGER," + "  content TEXT," + "  type INTEGER," + "   user_id INTEGER," + "  modify_time INTEGER," + "  create_time INTEGER);";
    private static final String CREATE_USER_TABLE = "create table user(" + "  id INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0," + "  username VARCHAR(32)," + "  password VARCHAR(32)," + "  nickname VARCHAR(32)," + "  motto TEXT," + "  head_icon BLOB," + "  birthday INTEGER," + "  deathday INTEGER," + "  create_time INTEGER);";

    public VersionBase(int versionCode, String description)
    {
        super(versionCode, description);
    }

    @Override
    public void update(SQLiteDatabase sqLiteDatabase)
    {
        //SELECT count(*) FROM sqlite_master WHERE type='table' AND name='hourplan';
        sqLiteDatabase.execSQL(CREATE_LOG_TABLE);
        sqLiteDatabase.execSQL(CREATE_COUNT_DOWN_TABLE);
        sqLiteDatabase.execSQL(CREATE_DOCUMENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        createVisitor(sqLiteDatabase);
    }

    public void createVisitor(SQLiteDatabase db)
    {
        Object[] params = new Object[8];
        params[0] = "visitor";
        params[1] = "visitor";
        params[2] = "游客";
        params[3] = "赶快登录吧";
        params[4] = null;
        params[5] = 0 + "";
        params[6] = 0 + "";
        params[7] = 0 + "";
        String SQL_ADD_USER = "insert into user(username,password,nickname,motto,head_icon,birthday,deathday,create_time) values(?,?,?,?,?,?,?,?)";
        db.execSQL(SQL_ADD_USER, params);
    }
}
