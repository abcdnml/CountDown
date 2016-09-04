package com.aaa.cd.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aaa.cd.model.LogItem;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaa on 2016/9/4.
 */
public class LogDao {
    private static SQLiteDatabase db;

    private static final String SQL_GET_LOG_BY_TIME = "select * from log where time >= ? and time <= ?";
    private static final String SQL_ADD_LOG = "insert into log(title,content,duration,time) values(?,?,?,?)";
    private static final String SQL_UPDATE_LOG = "update log set title=?,content=?,duration=?,time=? where id=?)";
    private static final String SQL_DELETE_LOG_BY_ID = "delete from log where id=?";
    private static final String SQL_GET_LAST_LOG = "select * from log order by id DESC limit 0,1";


    public static void addLog(LogItem item) {
        db = DBHelper.getInstance().getWritableDatabase();

        String[] params = new String[4];
        params[0] = item.getTitle();
        params[1] = item.getContent();
        params[2] = item.getDuration() + "";
        params[3] = item.getTime() + "";



        db.execSQL(SQL_ADD_LOG, params);

        db.close();
    }

    public static void updateLog(LogItem item) {
        db = DBHelper.getInstance().getWritableDatabase();

        String[] params = new String[5];
        params[0] = item.getTitle();
        params[1] = item.getContent();
        params[2] = item.getDuration() + "";
        params[3] = item.getTime() + "";
        params[4] = item.getId() + "";

        db.execSQL(SQL_UPDATE_LOG, params);

        db.close();
    }

    public static void deleteLogById(LogItem item) {
        db = DBHelper.getInstance().getWritableDatabase();

        String[] params = new String[1];
        params[0] = item.getId() + "";

        db.execSQL(SQL_DELETE_LOG_BY_ID, params);

        db.close();
    }

    public static List<LogItem> getLogByTime(long begin, long end) {
        db = DBHelper.getInstance().getReadableDatabase();

        String[] params = new String[2];
        params[0] = begin + "";
        params[1] = end + "";


        Cursor c = db.rawQuery(SQL_GET_LOG_BY_TIME, params);

        int index_id = c.getColumnIndex("id");
        int index_title = c.getColumnIndex("title");
        int index_content = c.getColumnIndex("content");
        int index_duration = c.getColumnIndex("duration");
        int index_time = c.getColumnIndex("time");

        List<LogItem> lli = new ArrayList<>();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                LogItem li = new LogItem();
                li.setId(c.getInt(index_id));
                li.setTitle(c.getString(index_title));
                li.setContent(c.getString(index_content));
                li.setDuration(c.getLong(index_duration));
                li.setTime(c.getLong(index_time));
                lli.add(li);
            }
        }

        c.close();
        db.close();

        return lli;
    }

    public static LogItem getLastLog() {
        db = DBHelper.getInstance().getReadableDatabase();

        Cursor c = db.rawQuery(SQL_GET_LAST_LOG, null);
        int index_id = c.getColumnIndex("id");
        int index_title = c.getColumnIndex("title");
        int index_content = c.getColumnIndex("content");
        int index_duration = c.getColumnIndex("duration");
        int index_time = c.getColumnIndex("time");

        LogItem li = null;
        if (c.moveToFirst()) {
            li = new LogItem();
            li.setId(c.getInt(index_id));
            li.setTitle(c.getString(index_title));
            li.setContent(c.getString(index_content));
            li.setDuration(c.getLong(index_duration));
            li.setTime(c.getLong(index_time));
        }

        c.close();
        db.close();

        return li;
    }

}
