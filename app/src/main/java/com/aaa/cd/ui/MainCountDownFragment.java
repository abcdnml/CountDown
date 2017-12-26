package com.aaa.cd.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.view.ClockSurfaceView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCountDownFragment extends MainBaseFragment {

    ClockSurfaceView csv;
    ListView lv_count_down;
    public MainCountDownFragment() {
        // Required empty public constructor
    }

    @Override
    public void initView(View view){
        csv=(ClockSurfaceView)view.findViewById(R.id.csv_clock);
        lv_count_down=(ListView) view.findViewById(R.id.lv_count_down);
    }
    @Override
    public void initTitle(View view){
        TextView tv_title_content=(TextView)view.findViewById(R.id.tv_title_content);
        tv_title_content.setText(R.string.menu_count_down);
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
        return R.layout.fragment_main_count_down;
    }

}
