package com.aaa.cd.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaa.cd.R;
import com.aaa.cd.view.ClockSurfaceView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCountDownFragment extends Fragment {

    ClockSurfaceView csv;
    public MainCountDownFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_main_count_down, container, false);
        csv=(ClockSurfaceView)view.findViewById(R.id.csv_clock);
        return view;
    }

}
