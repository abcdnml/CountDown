package com.aaa.cd.util;

import android.app.Application;

import com.aaa.cd.dao.DBHelper;
import com.aaa.cd.po.User;

/**
 * Created by aaa on 2016/9/4.
 */
public class CountDownApplication extends Application {

    User user;
    private static CountDownApplication application;
    public static CountDownApplication getApplication()
    {
        return application;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        DBHelper.init(this);
        SPUtil.init(this);
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
