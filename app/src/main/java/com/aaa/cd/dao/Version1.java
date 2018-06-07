package com.aaa.cd.dao;

import android.database.sqlite.SQLiteDatabase;

public class Version1 extends VersionUpdate
{
    private static final String CREATE_HOURPLAN_TABLE = "create table hourplan ("
            + "  id INTEGER PRIMARY KEY  AUTOINCREMENT DEFAULT 0, "
            + "  title TEXT, "
            + "  description TEXT, "
            + "  plan_time INTEGER, "
            + "  type INTEGER,"
            + "  executing BOOLEAN,"
            + "  current_time  INTEGER,"
            + "  create_time INTEGER, "
            + "  user_id INTEGER "
            + "  );";

    public Version1(int versionCode, String description)
    {
        super(versionCode, description);
    }

    @Override
    public void update(SQLiteDatabase database)
    {
        database.execSQL(CREATE_HOURPLAN_TABLE);
    }
}
