package com.aaa.cd.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aaa.cd.po.User;

/**
 * Description
 * Created by aaa on 2018/4/2.
 */

public class UserDao
{
    private static final String SQL_GET_CATALOGUE_BY_ID="select catalogue from user where id=?";
    private static final String SQL_GET_USER_INFO="select * from user where username=? and password=?";
    private static final String SQL_GET_USER_INFO_BY_ID="select * from user where id=?";
    private static final String SQL_ADD_USER="insert into user(username,password,nickname,motto,head_icon,birthday,deathday,create_time) values(?,?,?,?,?,?,?,?)";

    private static final String COLUMN_ID="id";
    private static final String COLUMN_USERNAME="username";
    private static final String COLUMN_PASSWORD="password";
    private static final String COLUMN_NICKNAME="nickname";
    private static final String COLUMN_MOTTO="motto";
    private static final String COLUMN_CATALOGUE="catalogue";
    private static final String COLUMN_HEADICON="head_icon";
    private static final String COLUMN_BIRTHDAY="birthday";
    private static final String COLUMN_DEATHDAY="deathday";
    private static final String COLUMN_CARETETIME="create_time";

    static SQLiteDatabase db;
    public static  int getCatalogueByUID(int uid)
    {
        db = DBHelper.getInstance().getWritableDatabase();

        int catalogueID=0;

        String[] params = new String[1];
        params[0] = uid+"";

        Cursor c = db.rawQuery(SQL_GET_CATALOGUE_BY_ID, params);
        if(c.getCount()>0)
        {
            catalogueID=c.getInt(c.getColumnIndex(COLUMN_CATALOGUE));

        }
        c.close();
        db.close();
        return catalogueID;
    }

    public static User login(String username, String password)
    {
        db = DBHelper.getInstance().getWritableDatabase();

        User user=null;

        String[] params = new String[2];
        params[0] = username;
        params[1] = password;

        Cursor c = db.rawQuery(SQL_GET_USER_INFO, params);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            user=new User();
            user.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
            user.setUsername(c.getString(c.getColumnIndex(COLUMN_USERNAME)));
            user.setPassword(c.getString(c.getColumnIndex(COLUMN_PASSWORD)));
            user.setNickname(c.getString(c.getColumnIndex(COLUMN_NICKNAME)));
            user.setMotto(c.getString(c.getColumnIndex(COLUMN_MOTTO)));
            user.setHeadIcon(c.getBlob(c.getColumnIndex(COLUMN_HEADICON)));
            user.setBirthday(c.getLong(c.getColumnIndex(COLUMN_BIRTHDAY)));
            user.setDeathday(c.getLong(c.getColumnIndex(COLUMN_DEATHDAY)));
            user.setCreateTime(c.getLong(c.getColumnIndex(COLUMN_CARETETIME)));
        }
        c.close();
        db.close();
        return user;
    }
    public static User getUserInfoByUID(int id)
    {
        db = DBHelper.getInstance().getWritableDatabase();

        User user=null;

        String[] params = new String[1];
        params[0] = id+"";

        Cursor c = db.rawQuery(SQL_GET_USER_INFO_BY_ID, params);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            user=new User();
            user.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
            user.setUsername(c.getString(c.getColumnIndex(COLUMN_USERNAME)));
            user.setPassword(c.getString(c.getColumnIndex(COLUMN_PASSWORD)));
            user.setNickname(c.getString(c.getColumnIndex(COLUMN_NICKNAME)));
            user.setMotto(c.getString(c.getColumnIndex(COLUMN_MOTTO)));
            user.setHeadIcon(c.getBlob(c.getColumnIndex(COLUMN_HEADICON)));
            user.setBirthday(c.getLong(c.getColumnIndex(COLUMN_BIRTHDAY)));
            user.setDeathday(c.getLong(c.getColumnIndex(COLUMN_DEATHDAY)));
            user.setCreateTime(c.getLong(c.getColumnIndex(COLUMN_CARETETIME)));
        }
        c.close();
        db.close();
        return user;
    }

    public static void addUser(String username,String password,String nickname,String motto,byte[] headicon,long birthday,long deathday,long createTime)
    {
        db = DBHelper.getInstance().getWritableDatabase();

        Object[] params = new Object[8];
        params[0] = username;
        params[1] = password;
        params[2] = nickname;
        params[3] = motto;
        params[4] = headicon;
        params[5] = birthday+"";
        params[6] = deathday+"";
        params[7] = createTime+"";

        db.execSQL(SQL_ADD_USER, params);
        db.close();
    }
}
