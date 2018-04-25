package com.aaa.cd.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aaa.cd.model.LogItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaa on 2016/9/4.
 */
public class LogDao {
    private static SQLiteDatabase db;

    private static final String SQL_GET_LOG_BY_TIME = "select * from log where time >= ? and time <= ? and user_id=? order by time DESC";
    private static final String SQL_ADD_LOG = "insert into log(title,content,remark,duration,time,user_id) values(?,?,?,?,?,?)";
    private static final String SQL_UPDATE_LOG = "update log set title=?,content=?,remark=?,duration=?,time=?,user_id=? where id=?";
    private static final String SQL_DELETE_LOG_BY_ID = "delete from log where id=?";
    private static final String SQL_GET_LAST_LOG = "select * from log where time<? and user_id=? order by id DESC limit 0,1";
    private static final String SQL_GET_FORMER_LOG = "select * from log where time<? and user_id=? order by id DESC limit 0,20";
    private static final String SQL_GET_NEXT_LOG = "select * from log where time>? and user_id=? order by id DESC limit 0,20";
    public static void addLog(LogItem item) {
        db = DBHelper.getInstance().getWritableDatabase();

        String[] params = new String[6];
        params[0] = item.getTitle();
        params[1] = item.getContent();
        params[2] = item.getRemark();
        params[3] = item.getDuration() + "";
        params[4] = item.getTime() + "";
        params[5] = item.getUserId() + "";

        db.execSQL(SQL_ADD_LOG, params);

        db.close();
    }

    public static void updateLog(LogItem item) {
        db = DBHelper.getInstance().getWritableDatabase();

        String[] params = new String[7];
        params[0] = item.getTitle();
        params[1] = item.getContent();
        params[2] = item.getRemark();
        params[3] = item.getDuration() + "";
        params[4] = item.getTime() + "";
        params[5] = item.getUserId() + "";
        params[6] = item.getId() + "";

        db.execSQL(SQL_UPDATE_LOG, params);

        db.close();
    }

    public static void deleteLog(LogItem item) {
        db = DBHelper.getInstance().getWritableDatabase();

        String[] params = new String[1];
        params[0] = item.getId() + "";

        db.execSQL(SQL_DELETE_LOG_BY_ID, params);

        db.close();
    }

    public static List<LogItem> getLogByTime(long begin, long end,int userId) {
        db = DBHelper.getInstance().getReadableDatabase();

        String[] params = new String[3];
        params[0] = begin + "";
        params[1] = end + "";
        params[2] = userId + "";

        Cursor c = db.rawQuery(SQL_GET_LOG_BY_TIME, params);

        int index_id = c.getColumnIndex("id");
        int index_title = c.getColumnIndex("title");
        int index_content = c.getColumnIndex("content");
        int index_remark = c.getColumnIndex("remark");
        int index_duration = c.getColumnIndex("duration");
        int index_time = c.getColumnIndex("time");
        int index_user_id = c.getColumnIndex("user_id");

        List<LogItem> lli = new ArrayList<>();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                LogItem li = new LogItem();
                li.setId(c.getInt(index_id));
                li.setTitle(c.getString(index_title));
                li.setContent(c.getString(index_content));
                li.setRemark(c.getString(index_remark));
                li.setDuration(c.getLong(index_duration));
                li.setTime(c.getLong(index_time));
                li.setUserId(c.getInt(index_user_id));
                lli.add(li);
            }
        }

        c.close();
        db.close();

        return lli;
    }

    /**
     * 获取传入时间之前的上一条log
     * @param time
     * @return
     */
    public static LogItem getLastLog(long time,int userId) {
        db = DBHelper.getInstance().getReadableDatabase();

        String[] params = new String[2];
        params[0] = time + "";
        params[1] = userId + "";


        Cursor c = db.rawQuery(SQL_GET_LAST_LOG, params);
        int index_id = c.getColumnIndex("id");
        int index_title = c.getColumnIndex("title");
        int index_content = c.getColumnIndex("content");
        int index_remark = c.getColumnIndex("remark");
        int index_duration = c.getColumnIndex("duration");
        int index_time = c.getColumnIndex("time");
        int index_user_id = c.getColumnIndex("user_id");

        LogItem li = null;
        if (c.moveToFirst()) {
            li = new LogItem();
            li.setId(c.getInt(index_id));
            li.setTitle(c.getString(index_title));
            li.setContent(c.getString(index_content));
            li.setRemark(c.getString(index_remark));
            li.setDuration(c.getLong(index_duration));
            li.setTime(c.getLong(index_time));
            li.setUserId(c.getInt(index_user_id));
        }

        c.close();
        db.close();

        return li;
    }

    /**
     * 获取传入时间之前的几条log
     * @param time 当前时间 毫秒
     * @return List<LogItem>
     */
    public static List<LogItem> getFormerLog(long time,int userId) {
        db = DBHelper.getInstance().getReadableDatabase();

        String[] params = new String[2];
        params[0] = time + "";
        params[1] = userId + "";


        Cursor c = db.rawQuery(SQL_GET_FORMER_LOG, params);
        int index_id = c.getColumnIndex("id");
        int index_title = c.getColumnIndex("title");
        int index_content = c.getColumnIndex("content");
        int index_remark = c.getColumnIndex("remark");
        int index_duration = c.getColumnIndex("duration");
        int index_time = c.getColumnIndex("time");
        int index_user_id = c.getColumnIndex("user_id");

        List<LogItem> lli=new ArrayList<>();
        while (c.moveToNext()) {
            LogItem li = new LogItem();
            li.setId(c.getInt(index_id));
            li.setTitle(c.getString(index_title));
            li.setContent(c.getString(index_content));
            li.setRemark(c.getString(index_remark));
            li.setDuration(c.getLong(index_duration));
            li.setTime(c.getLong(index_time));
            li.setUserId(c.getInt(index_user_id));
            lli.add(li);
        }

        c.close();
        db.close();

        return lli;
    }
    /**
     * 获取传入时间之后的几条log
     * @param time 当前时间 毫秒
     * @return List<LogItem>
     */
    public static List<LogItem> getNextLog(long time,int userId) {
        db = DBHelper.getInstance().getReadableDatabase();

        String[] params = new String[2];
        params[0] = time + "";
        params[1] = userId + "";


        Cursor c = db.rawQuery(SQL_GET_NEXT_LOG, params);
        int index_id = c.getColumnIndex("id");
        int index_title = c.getColumnIndex("title");
        int index_content = c.getColumnIndex("content");
        int index_remark = c.getColumnIndex("remark");
        int index_duration = c.getColumnIndex("duration");
        int index_time = c.getColumnIndex("time");
        int index_user_id = c.getColumnIndex("user_id");

        List<LogItem> lli=new ArrayList<>();
        while (c.moveToNext()) {
            LogItem li = new LogItem();
            li.setId(c.getInt(index_id));
            li.setTitle(c.getString(index_title));
            li.setContent(c.getString(index_content));
            li.setRemark(c.getString(index_remark));
            li.setDuration(c.getLong(index_duration));
            li.setTime(c.getLong(index_time));
            li.setUserId(c.getInt(index_user_id));
            lli.add(li);
        }

        c.close();
        db.close();

        return lli;
    }

}
