package com.aaa.cd.ui.main;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.ui.mindmap.MindMapActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainPlanFragment extends MainBaseFragment {


    public MainPlanFragment() {
    }

    @Override
    public void initTitle(View view) {
        TextView tv_title_content = (TextView) view.findViewById(R.id.tv_title_content);
        tv_title_content.setText(R.string.menu_plan);
        ImageView iv_left = (ImageView) view.findViewById(R.id.iv_title_left);
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
    public void initView(View view) {
        View hello = view.findViewById(R.id.tv_hello);
        hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MindMapActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_plan;
    }


}
