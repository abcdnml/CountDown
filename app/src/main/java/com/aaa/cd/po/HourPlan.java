package com.aaa.cd.po;

public class HourPlan
{
    private int id;
    private String title;
    private String description;
    private int type ;
    private long planTime;
    private long currentTime;
    private long createTime;
    private long executeStartTime;
    private int userId;

    public HourPlan()
    {
        type=1;
        executeStartTime=-1;
    }

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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public long getPlanTime()
    {
        return planTime;
    }

    public void setPlanTime(long planTime)
    {
        this.planTime = planTime;
    }

    public long getCurrentTime()
    {
        return currentTime;
    }

    public void setCurrentTime(long currentTime)
    {
        this.currentTime = currentTime;
    }

    public long getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }


    public long getExecuteStartTime()
    {
        return executeStartTime;
    }

    public void setExecuteStartTime(long executeStartTime)
    {
        this.executeStartTime = executeStartTime;
    }

    @Override
    public String toString()
    {
        return super.toString()+"id: "+id
                +"  title: "+title
                +"  desc: "+description
                +"  type: "+ type
                +"  plan time: "+planTime
                +"  current time : "+currentTime
                +"  create time: "+createTime
                +"  execute start time: "+executeStartTime
                +"  user id: "+userId;
    }
}
