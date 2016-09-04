package com.aaa.cd.util;


import java.text.DecimalFormat;

/**
 * Created by aaa on 2016/9/4.
 */
public class Utils {
    public static String getDuration(int timeMillis){

        int seconds=timeMillis/1000;
        if(seconds>=86400)
        {
            return seconds/86400+"天"+(seconds%86400)/3600+"小时";
        }else if(seconds>=3600){
            return seconds/3600+"小时"+(seconds%3600)/60+"分";
        }else {
            return seconds/60+"分"+(seconds%60)/60+"秒";
        }
    }

    static String STR_FORMAT1="00";
    static String STR_FORMAT2="000";
    static DecimalFormat df = new DecimalFormat(STR_FORMAT1);
    static DecimalFormat dfmilli = new DecimalFormat(STR_FORMAT2);
    public static String getLastingTime(long timeMillis)
    {
        long min=timeMillis/60000%60;
        long second=timeMillis/1000%60;
        long milli=timeMillis%1000;
        return df.format(min)+":"+df.format(second)+"."+dfmilli.format(milli);
    }

}
