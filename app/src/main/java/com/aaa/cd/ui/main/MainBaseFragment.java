package com.aaa.cd.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


import com.aaa.cd.model.MainCallback;


public abstract class MainBaseFragment extends Fragment {

    protected MainCallback mainCallback;


    public MainBaseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(getLayoutId(), container, false);
        initTitle(view);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainCallback) {
            mainCallback = (MainCallback) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainCallback = null;
    }

    public void initTitle(View view) {
    }

    public void initView(View view) {

    }

    public abstract int getLayoutId();
}
