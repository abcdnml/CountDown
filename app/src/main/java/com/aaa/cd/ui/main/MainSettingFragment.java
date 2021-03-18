package com.aaa.cd.ui.main;


import android.os.Bundle;

import com.aaa.cd.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainSettingFragment extends MainBaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public int getLayoutId() {
//        TODO
        return R.xml.preferences;
    }
}