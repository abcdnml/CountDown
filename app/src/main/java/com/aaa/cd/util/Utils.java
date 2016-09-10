package com.aaa.cd.util;


import java.text.DecimalFormat;

/**
 * Created by aaa on 2016/9/4.
 */
public class Utils {
    public static String getDuration(long timeMillis){

        long seconds=timeMillis/1000;
        if(seconds>=86400)
        {
            return seconds/86400+"天"+(seconds%86400)/3600+"小时";
        }else if(seconds>=3600){
            return seconds/3600+"小时"+(seconds%3600)/60+"分";
        }else {
            return seconds/60+"分"+(seconds%60)+"秒";
        }
    }

    static String STR_FORMAT1="00";
    static DecimalFormat df = new DecimalFormat(STR_FORMAT1);
    public static String getLastingTime(long timeMillis)
    {
        long hour=timeMillis/3600000;
        long min=timeMillis/60000;
        long second=timeMillis/1000%60;
        return df.format(hour)+":"+df.format(min)+":"+df.format(second);
    }

}
