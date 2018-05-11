package com.aaa.cd.po;

import android.text.TextUtils;

import com.aaa.cd.util.CharacterParser;
import com.aaa.cd.util.LogUtil;

/**
 * Description
 * Created by aaa on 2018/4/2.
 */

public class Catalogue
{

    public static final int FOLDER = 0;
    public static final int FILE = 1;

    private int id;
    private String name;
    private String sortLetter;
    private int type;
    private String content;
    private int size;
    private int parent;
    private int subItem;
    private int userId;
    private long createTime;
    private long modifyTime;

    public Catalogue()
    {
        id = -1;
        setName("");;
        setContent("");
    }


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        LogUtil.i("catalogue set name  : "+ name);
        String pinyin=CharacterParser.getInstance().getSelling(name);
        if(TextUtils.isEmpty(pinyin))
        {
            setSortLetter("#");
        }else{
            setSortLetter(CharacterParser.getInstance().getSelling(name) .substring(0,1));
        }
    }

    public String getSortLetter()
    {
        return sortLetter;
    }

    private void setSortLetter(String sortLetter)
    {
        this.sortLetter = sortLetter;
    }
    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
        if(content==null){
            setSize(0);
        }else{
            setSize(content.length());
        }
    }

    public int getSize()
    {
        return size;
    }

    private void setSize(int size)
    {
        this.size = size;
    }

    public int getParent()
    {
        return parent;
    }

    public void setParent(int parent)
    {
        this.parent = parent;
    }

    public int getSubItem()
    {
        return subItem;
    }

    public void setSubItem(int subItem)
    {
        this.subItem = subItem;
    }

    public long getCreateTime()
    {
        return createTime;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
    }

    public long getModifyTime()
    {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime)
    {
        this.modifyTime = modifyTime;
    }


    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }


    @Override
    public String toString()
    {
        return super.toString() + " id : " + id + " name : " + name + "  sortLetter : " + sortLetter + "  content : " + content + "  size : " + size + "  user id " + userId + "  parent : " + parent+"   type : "+ type;
    }
}
