package com.aaa.cd.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by aaa on 2016/9/3.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DBHelper";
    private static final String CREATE_LOG_TABLE = "create table log ("
            + "  id INTEGER PRIMARY KEY  AUTOINCREMENT DEFAULT 0, "
            + "  title TEXT, "
            + "  content TEXT, "
            + "  remark TEXT, "
            + "  duration INTEGER, "
            + "  user_id INTEGER, "
            + "  time INTEGER);";
    private static final String CREATE_COUNT_DOWN_TABLE = "create table count_down("
            + "  id INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0,"
            + "  name TEXT, "
            + "  descrption TEXT,"
            + "  time INTEGER);";

    private static final String CREATE_DOCUMENT_TABLE = "create table document("
            + "  id INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0,"
            + "  name TEXT, "
            + "  parent INTEGER,"
            + "  content TEXT,"
            + "  type INTEGER,"
            + "   user_id INTEGER,"
            + "  modify_time INTEGER,"
            + "  create_time INTEGER);";
    private static final String CREATE_USER_TABLE = "create table user("
            + "  id INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 0,"
            + "  username VARCHAR(32),"
            + "  password VARCHAR(32),"
            + "  nickname VARCHAR(32),"
            + "  motto TEXT,"
            + "  head_icon BLOB,"
            + "  birthday INTEGER,"
            + "  deathday INTEGER,"
            + "  create_time INTEGER);";


    private static final int VERSION_1 = 1;
    private static final int VERSION_2 = 2;
    private static final int VERSION_3 = 3;
    private static final int VERSION_4 = 4;

    /*catalogue 为json格式  每个节点名字就是key的名字 如果其下有文件夹则继续在下面创建key Iterator<String> it = object.keys();*/

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "CountDown.db";
    static Context context;

    private static DBHelper helper;

    public static void init(Context ctx) {
        context = ctx.getApplicationContext();
    }

    public static DBHelper getInstance() {
        if (helper == null) {
            helper = new DBHelper();
        }
        return helper;
    }

    private DBHelper() {
        super(context, DB_NAME, null, DB_VERSION);
        Log.i(TAG, "DBHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "onCreate");
        sqLiteDatabase.execSQL(CREATE_LOG_TABLE);
        sqLiteDatabase.execSQL(CREATE_COUNT_DOWN_TABLE);
        sqLiteDatabase.execSQL(CREATE_DOCUMENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        createVisitor(sqLiteDatabase);

        onUpgrade(sqLiteDatabase, 1, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade");
        for (int i = oldVersion; i < newVersion; i++) {
            switch (i) {
                case VERSION_1:
                    break;
                case VERSION_2:
                    break;
                case VERSION_3:
                    break;
                case VERSION_4:
                    break;
                default:
                    break;
            }
        }
    }

    public void createVisitor(SQLiteDatabase db) {
        Object[] params = new Object[8];
        params[0] = "visitor";
        params[1] = "visitor";
        params[2] = "游客";
        params[3] = "赶快登陆吧";
        params[4] = null;
        params[5] = 0 + "";
        params[6] = 0 + "";
        params[7] = 0 + "";
        String SQL_ADD_USER = "insert into user(username,password,nickname,motto,head_icon,birthday,deathday,create_time) values(?,?,?,?,?,?,?,?)";
        db.execSQL(SQL_ADD_USER, params);
    }
}
