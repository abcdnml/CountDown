package com.aaa.cd.ui.main;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCalendarFragment extends MainBaseFragment {


    public MainCalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void initTitle(View view){
        TextView tv_title_content=(TextView)view.findViewById(R.id.tv_title_content);
        tv_title_content.setText(R.string.menu_calendar);
        ImageView iv_left=(ImageView)view.findViewById(R.id.iv_title_left);
        iv_left.setVisibility(View.VISIBLE);
        iv_left.setImageResource(R.mipmap.menu);
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainCallback.openMenu(true);
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_calendar;
    }

}
