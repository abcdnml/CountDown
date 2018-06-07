package com.aaa.cd.model;

import android.view.View;

/**
 * Description
 * Created by aaa on 2018/4/20.
 */

public interface ItemClickListener
{
    void onItemClick(int position,View view);
    void onItemLongClick(int position,View view);
}
