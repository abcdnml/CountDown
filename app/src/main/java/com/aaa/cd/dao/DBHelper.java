package com.aaa.cd.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aaa.cd.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by aaa on 2016/9/3.
 */
public class DBHelper extends SQLiteOpenHelper
{
    public static final String TAG = "DBHelper";


    /*catalogue 为json格式  每个节点名字就是key的名字 如果其下有文件夹则继续在下面创建key Iterator<String> it = object.keys();*/

    private static final int DB_CURRENT_VERSION = 2;
    private static final int DB_BASE_VERSION = 0;
    private static final String DB_NAME = "CountDown.db";
    private static Context context;

    private static DBHelper helper;

    public static void init(Context ctx)
    {
        context = ctx.getApplicationContext();
    }

    public static DBHelper getInstance()
    {
        if (helper == null)
        {
            helper = new DBHelper();
        }
        return helper;
    }

    private DBHelper()
    {
        super(context, DB_NAME, null, DB_CURRENT_VERSION);
        Log.i(TAG, "DBHelper");
        initVersions();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        LogUtil.i("onCreate");
        VersionUpdate update = lvu.get(getVersion(DB_BASE_VERSION));
        update.update(sqLiteDatabase);
        update(sqLiteDatabase, DB_BASE_VERSION, DB_CURRENT_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        LogUtil.i("onUpgrade");
        update(sqLiteDatabase, oldVersion, newVersion);
    }


    public void update(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        LogUtil.i("db update oldVersion : " + oldVersion + " newVersion:  " + newVersion);
        if (oldVersion < newVersion)
        {
            int position = getVersion(oldVersion);
            LogUtil.i("db update oldVersion : " + lvu.get(position).versionCode + " :" + lvu.get(position).description);
            if (position >= 0 && position + 1 < lvu.size())
            {
                VersionUpdate version = lvu.get(position + 1);
                LogUtil.i("db update newVersion : " + version.versionCode + " :" + version.description);
                version.update(sqLiteDatabase);
                update(sqLiteDatabase, lvu.get(position + 1).versionCode, newVersion);
            }
        }
    }


    List<VersionUpdate> lvu;

    private void initVersions()
    {
        LogUtil.i("db initVersions");
        VersionUpdate base = new VersionBase(DB_BASE_VERSION, "添加日志 用户 文档 三个表 添加游客账号");
        VersionUpdate version1 = new Version1(1, "添加小时计划表");
        VersionUpdate version2 = new VersionTmp(DB_CURRENT_VERSION, "小时计划表 添加 开始任务的");


        lvu = new ArrayList<>();
        lvu.add(base);
        lvu.add(version1);
        lvu.add(version2);
        Collections.sort(lvu, versionUpdateComparator);


    }

    /**
     * 通过版本号 获取其在更新列表中的位置
     * 如果这个版本号不存在 那么找到比它小的版本号 返回
     * 如果没有比它小的 那么返回0
     * 如果没有比它大的 返回最大position
     *
     * @param version
     * @return
     */
    public int getVersion(int version)
    {
        if (lvu.size() <= 0)
        {
            return -1;
        }
        for (int i = 0; i < lvu.size(); i++)
        {
            if (lvu.get(i).versionCode == version)
            {
                return i;
            }
        }
        if (version < lvu.get(0).versionCode)
        {
            //如果version比第一个版本小 返回0
            return 0;
        }
        if (version > lvu.get(lvu.size() - 1).versionCode)
        {
            //如果version比最后版本大 返回最新版本
            return lvu.size() - 1;
        }

        for (int i = 0; i < lvu.size()-1; i++)
        {
            if (version > lvu.get(i).versionCode && version < lvu.get(i + 1).versionCode)
            {
                return i;
            }
        }
        return -1;
    }

    private Comparator<VersionUpdate> versionUpdateComparator = new Comparator<VersionUpdate>()
    {
        @Override
        public int compare(VersionUpdate o1, VersionUpdate o2)
        {
            if (o1.versionCode > o2.versionCode)
            {
                return 1;
            } else if (o1.versionCode < o2.versionCode)
            {
                return -1;
            } else
            {
                return 0;
            }
        }
    };


}
