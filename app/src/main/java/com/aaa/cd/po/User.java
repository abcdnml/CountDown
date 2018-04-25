package com.aaa.cd.po;

/**
 * Description
 * Created by aaa on 2018/4/2.
 */

public class User
{
    private int id;
    private String username;
    private String nickname;
    private String password;
    private String motto;
    private byte[] headIcon;
    private long createTime;
    private long birthday;
    private long deathday;


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getMotto()
    {
        return motto;
    }

    public void setMotto(String motto)
    {
        this.motto = motto;
    }

    public byte[] getHeadIcon()
    {
        return headIcon;
    }

    public void setHeadIcon(byte[] head)
    {

        this.headIcon = head;
    }

    public long getBirthday()
    {
        return birthday;
    }

    public void setBirthday(long birthday)
    {
        this.birthday = birthday;
    }

    public long getDeathday()
    {
        return deathday;
    }

    public void setDeathday(long deathday)
    {
        this.deathday = deathday;
    }

    public long getCreateTime()
    {
        return createTime;
    }
    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
    }

}
