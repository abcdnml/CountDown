package com.aaa.cd.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.aaa.cd.R;
import com.aaa.cd.model.Callback;

public class DialogUtils
{

    public static void showDeleteDialog(Activity activity ,String content,final Callback callback,final int id)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);
        builder.setMessage(content);
        builder.setNegativeButton(activity.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        }).setPositiveButton(activity.getString(R.string.ensure), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                callback.onCallback(id);
            }
        }).show();
    }
}
