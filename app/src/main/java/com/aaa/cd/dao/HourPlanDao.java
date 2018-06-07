package com.aaa.cd.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aaa.cd.po.HourPlan;
import com.aaa.cd.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class HourPlanDao
{
    private static SQLiteDatabase db;


    //    SELECT count(*) FROM sqlite_master WHERE type='table' AND name='tableName';

    private static final String SQL_GET_PLAN_BY_ID = "select * from hourplan where id=?";
    private static final String SQL_GET_ALL_PLAN = "select * from hourplan where user_id=?";
    private static final String SQL_GET_FINISHED_PLAN = "select * from hourplan where user_id=? and current_time>=plan_time";
    private static final String SQL_GET_ONGOING_PLAN = "select * from hourplan where user_id=? and current_time<plan_time";
    private static final String SQL_GET_PLAN_BY_TYPE = "select * from hourplan where type=? and user_id=? ";

    private static final String SQL_ADD_PLAN = "insert into hourplan(title,description,type,execute_start,plan_time,current_time,create_time,user_id) values(?,?,?,?,?,?,?,?)";
    private static final String SQL_DELETE_PLAN_BY_ID = "delete from hourplan where id=?";
    private static final String SQL_UPDATE_PLAN = "update hourplan set title=?,description=?,type=?,execute_start=?,plan_time=?,current_time=?,create_time=?,user_id=? where id=?";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_EXECUTE_START_TIME = "execute_start";
    private static final String COLUMN_PLAN_TIME = "plan_time";
    private static final String COLUMN_CURRENT_TIME = "current_time";
    private static final String COLUMN_CREATE_TIME = "create_time";
    private static final String COLUMN_USER_ID = "user_id";

    public static void addHourPlan(HourPlan plan)
    {

        List<String> paramList=new ArrayList<>();
        paramList.add(plan.getTitle());
        paramList.add(plan.getDescription());
        paramList.add(plan.getType()+ "");
        paramList.add(plan.getExecuteStartTime()+ "");
        paramList.add(plan.getPlanTime()+ "");
        paramList.add(plan.getCurrentTime()+ "");
        paramList.add(plan.getCreateTime()+ "");
        paramList.add(plan.getUserId()+ "");

        String[] params=new String[paramList.size()];
        paramList.toArray(params);

        db = DBHelper.getInstance().getWritableDatabase();
        db.execSQL(SQL_ADD_PLAN, params);

        db.close();
    }

    public static void deleteHourPlanById(int id)
    {

        List<String> paramList=new ArrayList<>();
        paramList.add(id + "");
        String[] params=new String[paramList.size()];
        paramList.toArray(params);

        db = DBHelper.getInstance().getWritableDatabase();
        db.execSQL(SQL_DELETE_PLAN_BY_ID, params);
        db.close();
    }

    public static void updatePlan(HourPlan plan) {

        List<String> paramList=new ArrayList<>();
        paramList.add(plan.getTitle());
        paramList.add(plan.getDescription());
        paramList.add(plan.getType()+ "");
        paramList.add(plan.getExecuteStartTime()+ "");
        paramList.add(plan.getPlanTime()+ "");
        paramList.add(plan.getCurrentTime()+ "");
        paramList.add(plan.getCreateTime()+ "");
        paramList.add(plan.getUserId()+ "");
        paramList.add(plan.getId()+ "");
        String[] params=new String[paramList.size()];
        paramList.toArray(params);

        db = DBHelper.getInstance().getWritableDatabase();
        db.execSQL(SQL_UPDATE_PLAN, params);
        db.close();

    }

    public static HourPlan getPlanById(int id) {

        List<String> paramList=new ArrayList<>();
        paramList.add(id + "");
        String[] params=new String[paramList.size()];
        paramList.toArray(params);

        db = DBHelper.getInstance().getReadableDatabase();

        Cursor c = db.rawQuery(SQL_GET_PLAN_BY_ID, params);
        int index_id = c.getColumnIndex(COLUMN_ID);
        int index_title = c.getColumnIndex(COLUMN_TITLE);
        int index_desc = c.getColumnIndex(COLUMN_DESCRIPTION);
        int index_type = c.getColumnIndex(COLUMN_TYPE);
        int index_execute_start = c.getColumnIndex(COLUMN_EXECUTE_START_TIME);
        int index_plan_time = c.getColumnIndex(COLUMN_PLAN_TIME);
        int index_current_time = c.getColumnIndex(COLUMN_CURRENT_TIME);
        int index_create_time = c.getColumnIndex(COLUMN_CREATE_TIME);
        int index_user_id = c.getColumnIndex(COLUMN_USER_ID);

        HourPlan plan=null;
        if (c.moveToFirst()) {
            plan = new HourPlan();
            plan.setId(c.getInt(index_id));
            plan.setTitle(c.getString(index_title));
            plan.setDescription(c.getString(index_desc));
            plan.setType(c.getInt(index_type));
            plan.setExecuteStartTime(c.getLong(index_execute_start));
            plan.setPlanTime(c.getLong(index_plan_time));
            plan.setCurrentTime(c.getLong(index_current_time));
            plan.setCreateTime(c.getLong(index_create_time));
            plan.setUserId(c.getInt(index_user_id));
        }

        c.close();
        db.close();
        return plan;
    }
    public static List<HourPlan> getPlanByStatus(boolean isFinished,int userId)
    {
        if(isFinished)
        {
            return getFinishedPlan(userId);
        }else{
            return getUnfinishedPlan(userId);
        }
    }
    public static List<HourPlan> getFinishedPlan(int userId) {
        db = DBHelper.getInstance().getReadableDatabase();

        List<String> paramList=new ArrayList<>();
        paramList.add(userId + "");
        String[] params=new String[paramList.size()];
        paramList.toArray(params);

        Cursor c = db.rawQuery(SQL_GET_FINISHED_PLAN, params);
        int index_id = c.getColumnIndex(COLUMN_ID);
        int index_title = c.getColumnIndex(COLUMN_TITLE);
        int index_desc = c.getColumnIndex(COLUMN_DESCRIPTION);
        int index_type = c.getColumnIndex(COLUMN_TYPE);
        int index_execute_start = c.getColumnIndex(COLUMN_EXECUTE_START_TIME);
        int index_plan_time = c.getColumnIndex(COLUMN_PLAN_TIME);
        int index_current_time = c.getColumnIndex(COLUMN_CURRENT_TIME);
        int index_create_time = c.getColumnIndex(COLUMN_CREATE_TIME);
        int index_user_id = c.getColumnIndex(COLUMN_USER_ID);

        List<HourPlan> lhp=new ArrayList<>();
        while (c.moveToNext()) {
            HourPlan plan = new HourPlan();
            plan.setId(c.getInt(index_id));
            plan.setTitle(c.getString(index_title));
            plan.setDescription(c.getString(index_desc));
            plan.setType(c.getInt(index_type));
            plan.setExecuteStartTime(c.getLong(index_execute_start));
            plan.setPlanTime(c.getLong(index_plan_time));
            LogUtil.i("get plan time :  id "+index_plan_time+" ->"+plan.getPlanTime());
            plan.setCurrentTime(c.getLong(index_current_time));
            plan.setCreateTime(c.getLong(index_create_time));
            plan.setUserId(c.getInt(index_user_id));
            lhp.add(plan);
        }

        c.close();
        db.close();

        return lhp;
    }
    public static List<HourPlan> getUnfinishedPlan(int userId) {

        List<String> paramList=new ArrayList<>();
        paramList.add(userId + "");
        String[] params=new String[paramList.size()];
        paramList.toArray(params);

        db = DBHelper.getInstance().getReadableDatabase();
        Cursor c = db.rawQuery(SQL_GET_ONGOING_PLAN, params);
        int index_id = c.getColumnIndex(COLUMN_ID);
        int index_title = c.getColumnIndex(COLUMN_TITLE);
        int index_desc = c.getColumnIndex(COLUMN_DESCRIPTION);
        int index_type = c.getColumnIndex(COLUMN_TYPE);
        int index_execute_start = c.getColumnIndex(COLUMN_EXECUTE_START_TIME);
        int index_plan_time = c.getColumnIndex(COLUMN_PLAN_TIME);
        int index_current_time = c.getColumnIndex(COLUMN_CURRENT_TIME);
        int index_create_time = c.getColumnIndex(COLUMN_CREATE_TIME);
        int index_user_id = c.getColumnIndex(COLUMN_USER_ID);

        List<HourPlan> lhp=new ArrayList<>();
        while (c.moveToNext()) {
            HourPlan plan = new HourPlan();
            plan.setId(c.getInt(index_id));
            plan.setTitle(c.getString(index_title));
            plan.setDescription(c.getString(index_desc));
            plan.setType(c.getInt(index_type));
            plan.setExecuteStartTime(c.getLong(index_execute_start));
            plan.setPlanTime(c.getLong(index_plan_time));
            plan.setCurrentTime(c.getLong(index_current_time));
            plan.setCreateTime(c.getLong(index_create_time));
            plan.setUserId(c.getInt(index_user_id));
            lhp.add(plan);
        }

        c.close();
        db.close();

        return lhp;
    }

    public static List<HourPlan> getAllPlan(int userId) {


        List<String> paramList=new ArrayList<>();
        paramList.add(userId + "");
        String[] params=new String[paramList.size()];
        paramList.toArray(params);

        db = DBHelper.getInstance().getReadableDatabase();
        Cursor c = db.rawQuery(SQL_GET_ALL_PLAN, params);
        int index_id = c.getColumnIndex(COLUMN_ID);
        int index_title = c.getColumnIndex(COLUMN_TITLE);
        int index_desc = c.getColumnIndex(COLUMN_DESCRIPTION);
        int index_type = c.getColumnIndex(COLUMN_TYPE);
        int index_execute_start = c.getColumnIndex(COLUMN_EXECUTE_START_TIME);
        int index_plan_time = c.getColumnIndex(COLUMN_PLAN_TIME);
        int index_current_time = c.getColumnIndex(COLUMN_CURRENT_TIME);
        int index_create_time = c.getColumnIndex(COLUMN_CREATE_TIME);
        int index_user_id = c.getColumnIndex(COLUMN_USER_ID);

        List<HourPlan> lhp=new ArrayList<>();
        while (c.moveToNext()) {
            HourPlan plan = new HourPlan();
            plan.setId(c.getInt(index_id));
            plan.setTitle(c.getString(index_title));
            plan.setDescription(c.getString(index_desc));
            plan.setType(c.getInt(index_type));
            plan.setExecuteStartTime(c.getLong(index_execute_start));
            plan.setPlanTime(c.getLong(index_plan_time));
            plan.setCurrentTime(c.getLong(index_current_time));
            plan.setCreateTime(c.getLong(index_create_time));
            plan.setUserId(c.getInt(index_user_id));
            lhp.add(plan);
        }

        c.close();
        db.close();

        return lhp;
    }
    public static List<HourPlan> getPlanByType(int type,int userId) {

        List<String> paramList=new ArrayList<>();
        paramList.add(type + "");
        paramList.add(userId + "");
        String[] params=new String[paramList.size()];
        paramList.toArray(params);


        db = DBHelper.getInstance().getReadableDatabase();
        Cursor c = db.rawQuery(SQL_GET_PLAN_BY_TYPE, params);
        int index_id = c.getColumnIndex(COLUMN_ID);
        int index_title = c.getColumnIndex(COLUMN_TITLE);
        int index_desc = c.getColumnIndex(COLUMN_DESCRIPTION);
        int index_type = c.getColumnIndex(COLUMN_TYPE);
        int index_execute_start = c.getColumnIndex(COLUMN_EXECUTE_START_TIME);
        int index_plan_time = c.getColumnIndex(COLUMN_PLAN_TIME);
        int index_current_time = c.getColumnIndex(COLUMN_CURRENT_TIME);
        int index_create_time = c.getColumnIndex(COLUMN_CREATE_TIME);
        int index_user_id = c.getColumnIndex(COLUMN_USER_ID);

        List<HourPlan> lhp=new ArrayList<>();
        while (c.moveToNext()) {
            HourPlan plan = new HourPlan();
            plan.setId(c.getInt(index_id));
            plan.setTitle(c.getString(index_title));
            plan.setDescription(c.getString(index_desc));
            plan.setType(c.getInt(index_type));
            plan.setExecuteStartTime(c.getLong(index_execute_start));
            plan.setPlanTime(c.getLong(index_plan_time));
            plan.setCurrentTime(c.getLong(index_current_time));
            plan.setCreateTime(c.getLong(index_create_time));
            plan.setUserId(c.getInt(index_user_id));
            lhp.add(plan);
        }

        c.close();
        db.close();

        return lhp;
    }
}
