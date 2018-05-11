package com.aaa.cd.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.aaa.cd.po.Catalogue;
import com.aaa.cd.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * Created by aaa on 2018/4/2.
 */

public class DocumentDao
{
    private static SQLiteDatabase db;

    private static final String SQL_GET_FILE_BY_ID = "select * from document where id=? and type>0";
    private static final String SQL_GET_FILELIST_BY_PARENT = "select d1.id,d1.name,d1.parent,d1.type,d1.modify_time,d1.create_time,d1.user_id,substr(d1.[content],1,128) as overview,(select count(*) from document where d1.id=parent) as subitem from document as d1 where d1.parent=? and d1.user_id=?";
    private static final String SQL_CREATE_FILE = "insert into document(name,content,parent,type,user_id,modify_time,create_time) values(?,?,?,?,?,strftime('%s','now'),strftime('%s','now'))";
    private static final String SQL_DELETE_FILE_BY_ID = "delete from document where id=?";
    private static final String SQL_DELETE_FOLDER_BY_ID = "with dom as " + "(select * from document where parent=? and user_id=?" + "union all " + "select d1.* from document d1 inner join dom d2 on d1.parent=d2.id )" + "delete from document where id in (select id from dom)";
    private static final String SQL_MODIFY_FILE = "update document set name=?,content=?,parent=?,user_id=?,modify_time=strftime('%s','now') where id=?";
    private static final String SQL_GET_LATEST_ID = "select last_insert_rowid() from document";


    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_PARENT = "parent";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_MODIFY_TIME = "modify_time";
    private static final String COLUMN_CREATE_TIME = "create_time";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_SUBITEM = "subitem";
    private static final String COLUMN_OVERVIEW = "overview";

    public static Catalogue getFileById(int fileId)
    {
        db = DBHelper.getInstance().getWritableDatabase();


        String[] params = new String[1];
        params[0] = fileId + "";

        Cursor c = db.rawQuery(SQL_GET_FILE_BY_ID, params);
        Catalogue catalogue = null;
        if (c.getCount() > 0)
        {
            c.moveToFirst();
            catalogue = new Catalogue();
            catalogue.setId(c.getInt(c.getColumnIndex(COLUMN_ID)));
            catalogue.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
            catalogue.setContent(c.getString(c.getColumnIndex(COLUMN_CONTENT)));
            catalogue.setParent(c.getInt(c.getColumnIndex(COLUMN_PARENT)));
            catalogue.setType(c.getInt(c.getColumnIndex(COLUMN_TYPE)));
            catalogue.setUserId(c.getInt(c.getColumnIndex(COLUMN_USER_ID)));
            catalogue.setModifyTime(c.getLong(c.getColumnIndex(COLUMN_MODIFY_TIME)));
            catalogue.setCreateTime(c.getLong(c.getColumnIndex(COLUMN_CREATE_TIME)));
            catalogue.setSubItem(Catalogue.FILE);
        }
        c.close();
        db.close();
        return catalogue;
    }

    public static List<Catalogue> getFileListByParent(int parentId, int userId)
    {
        db = DBHelper.getInstance().getWritableDatabase();


        String[] params = new String[2];
        params[0] = parentId + "";
        params[1] = userId + "";

        Cursor c = db.rawQuery(SQL_GET_FILELIST_BY_PARENT, params);


        List<Catalogue> lc = new ArrayList<>();
        if (c.getCount() > 0)
        {
            int INDEX_ID = c.getColumnIndex(COLUMN_ID);
            int INDEX_NAME = c.getColumnIndex(COLUMN_NAME);
            int INDEX_CONTENT = c.getColumnIndex(COLUMN_OVERVIEW);
            int INDEX_PARENT = c.getColumnIndex(COLUMN_PARENT);
            int INDEX_TYPE = c.getColumnIndex(COLUMN_TYPE);
            int INDEX_USER_ID = c.getColumnIndex(COLUMN_USER_ID);
            int INDEX_MODIFY_TIME = c.getColumnIndex(COLUMN_MODIFY_TIME);
            int INDEX_CREATE_TIME = c.getColumnIndex(COLUMN_CREATE_TIME);
            int INDEX_SUBITEM = c.getColumnIndex(COLUMN_SUBITEM);
            while (c.moveToNext())
            {
                Catalogue catalogue = new Catalogue();
                catalogue.setId(c.getInt(INDEX_ID));
                catalogue.setName(c.getString(INDEX_NAME));
                catalogue.setContent(c.getString(INDEX_CONTENT));
                catalogue.setParent(c.getInt(INDEX_PARENT));
                catalogue.setType(c.getInt(INDEX_TYPE));
                catalogue.setUserId(c.getInt(INDEX_USER_ID));
                catalogue.setModifyTime(c.getLong(INDEX_MODIFY_TIME));
                catalogue.setCreateTime(c.getLong(INDEX_CREATE_TIME));
                catalogue.setSubItem(c.getInt(INDEX_SUBITEM));
                lc.add(catalogue);
            }
        }
        c.close();
        db.close();
        Log.i("document dao", "get files  size : " + lc.size());
        return lc;
    }

    public static int createFile(Catalogue catalogue)
    {
        return createFile(catalogue.getName(), catalogue.getContent(), catalogue.getParent(), catalogue.getType(), catalogue.getUserId());
    }

    public static int createFile(String name, String content, int parent, int type, int userId)
    {
        LogUtil.i("name : "+name+" content: "+content+"  parent : "+parent+"  type : "+ type+ " userid : "+userId);
        db = DBHelper.getInstance().getWritableDatabase();

        Object[] params = new Object[5];
        params[0] = name;
        params[1] = content;
        params[2] = parent;
        params[3] = (type == Catalogue.FOLDER) ? Catalogue.FOLDER + "" : Catalogue.FILE + "";
        params[4] = userId;

        db.execSQL(SQL_CREATE_FILE, params);

        Cursor cursor = db.rawQuery(SQL_GET_LATEST_ID, null);
        int strid = -1;
        if (cursor.moveToFirst())
        {
            strid = cursor.getInt(0);
        }
        cursor.close();

        db.close();

        return strid;
    }


    public static void deleteFileById(int id)
    {
        db = DBHelper.getInstance().getWritableDatabase();

        String[] params = new String[1];
        params[0] = id + "";
        db.execSQL(SQL_DELETE_FILE_BY_ID, params);
        db.close();
    }

    public static void deleteFolderById(int id, int user_id)
    {
        db = DBHelper.getInstance().getWritableDatabase();

        String[] params = new String[2];
        params[0] = id + "";
        params[1] = user_id + "";
        db.execSQL(SQL_DELETE_FOLDER_BY_ID, params);
        db.close();
    }

    public static void modifyFile(Catalogue catalogue)
    {
        LogUtil.i(catalogue.toString());
        modifyFile(catalogue.getId(), catalogue.getName(), catalogue.getContent(), catalogue.getParent(), catalogue.getUserId());
    }

    public static void modifyFile(int id, String name, String content, int parent, int userId)
    {

        db = DBHelper.getInstance().getWritableDatabase();
        String[] params = new String[5];
        params[0] = name;
        params[1] = content;
        params[2] = parent + "";
        params[3] = userId + "";
        params[4] = id + "";
        db.execSQL(SQL_MODIFY_FILE, params);
        db.close();
    }

}
