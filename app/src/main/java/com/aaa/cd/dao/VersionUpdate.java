package com.aaa.cd.dao;

import android.database.sqlite.SQLiteDatabase;

import com.aaa.cd.util.LogUtil;

public class VersionUpdate
{
    int versionCode;
    String description;

    public VersionUpdate(int versionCode, String description){
        LogUtil.i("new version: "+versionCode+"  : "+ description);
        this.versionCode=versionCode;
        this.description=description;
    }

    public void update(SQLiteDatabase database)
    {
        LogUtil.i("executing  : super");
    }
}
