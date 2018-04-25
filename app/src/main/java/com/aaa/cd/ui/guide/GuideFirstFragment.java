package com.aaa.cd.ui.guide;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aaa.cd.R;
import com.aaa.cd.view.AnimTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFirstFragment extends Fragment {


    public GuideFirstFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_guide_first, container, false);
        AnimTextView atv_life = (AnimTextView) view.findViewById(R.id.atv_life);
        atv_life.setOrientation(LinearLayout.VERTICAL);
        atv_life.setText(getString(R.string.life_length_question), R.anim.anim_alpha_show, 300);
        return view;
    }

}
