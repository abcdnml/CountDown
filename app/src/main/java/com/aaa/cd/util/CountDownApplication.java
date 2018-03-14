package com.aaa.cd.util;

import android.app.Application;

import com.aaa.cd.dao.DBHelper;

/**
 * Created by aaa on 2016/9/4.
 */
public class CountDownApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DBHelper.init(this);
        SPUtil.init(this);
    }
}
