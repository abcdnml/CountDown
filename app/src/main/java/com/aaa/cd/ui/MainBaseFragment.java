package com.aaa.cd.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.model.MainCallback;

import org.w3c.dom.Text;

public abstract class MainBaseFragment extends Fragment {

    protected MainCallback mainCallback;


    public MainBaseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(getLayoutId(),container,false);
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
            throw new RuntimeException(context.toString()+ " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainCallback = null;
    }

    public void initTitle(View view)
    {
    }
    public void initView(View view){

    }
    public abstract int getLayoutId();
}
