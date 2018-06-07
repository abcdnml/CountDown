package com.aaa.cd.dao;

import android.database.sqlite.SQLiteDatabase;

import com.aaa.cd.util.LogUtil;

public class VersionTmp extends VersionUpdate
{
//    private static final String ADD_COLUMN_EXECUTING = "ALTER TABLE hourplan ADD COLUMN executing BOOLEAN ;";
    private static final String ADD_COLUMN_EXECUTE_START = "ALTER TABLE hourplan ADD COLUMN execute_start INTEGER ;";

    public VersionTmp(int versionCode, String description)
    {
        super(versionCode, description);
    }

    @Override
    public void update(SQLiteDatabase database)
    {
        LogUtil.i("executing  : "+ADD_COLUMN_EXECUTE_START);
        database.execSQL(ADD_COLUMN_EXECUTE_START);
    }
}
