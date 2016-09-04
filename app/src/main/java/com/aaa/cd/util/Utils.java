package com.aaa.cd.util;


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

}
