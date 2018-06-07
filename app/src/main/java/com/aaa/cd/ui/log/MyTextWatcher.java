package com.aaa.cd.ui.log;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

import com.aaa.cd.R;
import com.aaa.cd.util.CountDownApplication;

public class MyTextWatcher implements TextWatcher
{
    TextInputLayout textInputLayout;
    public MyTextWatcher(TextInputLayout parent){
        textInputLayout=parent;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        if (s.length() != 0)
        {
            textInputLayout.setErrorEnabled(false);
        }else{
            textInputLayout.setError(CountDownApplication.getApplication().getString(R.string.should_not_be_null));
            textInputLayout.setErrorEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }
}
