package com.aaa.cd.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by aaa on 2016/9/3.
 */
public class LogItem implements Parcelable
{
    private int id;
    private String title;
    private String content;
    private String remark;
    private long duration;
    private long time;
    private int userId;
    private boolean isChecked;


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getRemark()
    {
        if(TextUtils.isEmpty(remark)){
            return "";
        }
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public long getDuration()
    {
        return duration;
    }

    public void setDuration(long duration)
    {
        this.duration = duration;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }


    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean checked)
    {
        isChecked = checked;
    }

    @Override
    public String toString()
    {
        return "LogItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", duration=" + duration +
                ", time=" + time +
                ", userId=" + userId +
                '}';
    }

    public LogItem()
    {
    }

    private LogItem(Parcel in)
    {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        remark = in.readString();
        duration = in.readLong();
        time = in.readLong();
        userId = in.readInt();
        isChecked = in.readByte() == 1;
    }

    public static final Parcelable.Creator<LogItem> CREATOR = new Parcelable.Creator<LogItem>()
    {
        public LogItem createFromParcel(Parcel in)
        {
            return new LogItem(in);
        }

        public LogItem[] newArray(int size)
        {
            return new LogItem[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flag)
    {
        out.writeInt(id);
        out.writeString(title);
        out.writeString(content);
        out.writeString(remark);
        out.writeLong(duration);
        out.writeLong(time);
        out.writeInt(userId);
        out.writeByte((byte) (isChecked ? 1 : 0));
    }
}
