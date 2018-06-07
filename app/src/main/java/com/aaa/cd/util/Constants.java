package com.aaa.cd.util;

/**
 * Created by aaa on 2016/9/3.
 */
public class Constants
{

    //日志界面Intent_key value
    public static final String INTENT_KEY_LOG_MODE = "intent_key_log_mode";
    public static final String INTENT_KEY_LOG_CURRENT = "intent_key_log_current";
    public static final int INTENT_VALUE_LOG_MODE_ADD = 1;
    public static final int INTENT_VALUE_LOG_MODE_EDIT = 0;

    //文章界面Intent_key
    public static final String INTENT_KEY_ARTICLE_ID = "intent_key_article_id";
    public static final String INTENT_KEY_ARTICLE_PARENT = "intent_key_article_parent";
    public static final String INTENT_KEY_ARTICLE_USER = "intent_key_article_user";
    public static final String INTENT_KEY_ARTICLE_TYPE = "intent_key_article_type";
    public static final String INTENT_KEY_ARTICLE_TITLE = "intent_key_article_title";
    public static final String INTENT_KEY_ARTICLE_CONTENT = "intent_key_article_content";
    public static final String INTENT_KEY_SEARCH_ARTICLE_STRING = "intent_key_search_article_string";

    //小时计划界面
    public static final String INTENT_KEY_HOURPLAN_MODE="hour_plan_mode";
    public static final String INTENT_KEY_HOURPLAN_ID="hour_plan_id";
    public static final int INTENT_VALUE_HOURPLAN_MODE_ADD =1;
    public static final int INTENT_VALUE_HOURPLAN_MODE_EDIT =0;



    public static final int REQUEST_CODE_LOG = 5;
    public static final int REQUEST_CODE_ARTICLE = 3;
    public static final int REQUEST_CODE_ARTICLE_IMPORT = 31;
    public static final int REQUEST_CODE_ARTICLE_EXPORT = 32;
    public static final int REQUEST_CODE_SEARCH_RESULT = 33;
    public static final int REQUEST_CODE_HOURPLAN_RESULT = 51;

    public static final String SP_KEY_BORN_DATE = "born_date";
    public static final String SP_KEY_DEATH_DATE = "death_date";


    public static final String SP_KEY_USER_ID = "user_id";
}
