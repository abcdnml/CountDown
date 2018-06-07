package com.aaa.cd.ui.hourplan;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

import com.aaa.cd.R;
import com.aaa.cd.util.CountDownApplication;
import com.aaa.cd.util.LogUtil;

public class HourPlanTextWatcher implements TextWatcher
{
    TextInputLayout textInputLayout;

    public static final int TYPE_HOUR = 0;
    public static final int TYPE_MIN = 1;

    private int type;

    public HourPlanTextWatcher(TextInputLayout parent, int type)
    {
        textInputLayout = parent;
        this.type = type;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        Context context = CountDownApplication.getApplication();
        if (type == TYPE_HOUR)
        {
            if (s.length() == 0)
            {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(context.getString(R.string.plan_time_should_not_be_null));
            } else
            {
                try
                {
                    int hour = Integer.parseInt(s.toString());
                    if (hour >= 0 && hour < 1000000)
                    {
                        textInputLayout.setErrorEnabled(false);
                    } else if(hour >=1000000)
                    {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError(CountDownApplication.getApplication().getString(R.string.plan_time_too_long));
                    }else{
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError(CountDownApplication.getApplication().getString(R.string.plan_time_not_correct));
                    }
                } catch (NumberFormatException e)
                {
                    e.printStackTrace();
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("输入的什么鬼东西...");
                }
            }
        } else
        {
            if (s.length() == 0)
            {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(CountDownApplication.getApplication().getString(R.string.plan_time_should_not_be_null));
            } else
            {
                try
                {
                    int min = Integer.parseInt(s.toString());
                    if (min >= 0 && min < 60)
                    {
                        textInputLayout.setErrorEnabled(false);
                    } else if(min>=60)
                    {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError(CountDownApplication.getApplication().getString(R.string.plan_time_min_to_large));
                    }else{
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError(CountDownApplication.getApplication().getString(R.string.plan_time_not_correct));
                    }
                } catch (NumberFormatException e)
                {
                    e.printStackTrace();
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("输入的什么鬼东西...");
                }
            }

        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }
}
