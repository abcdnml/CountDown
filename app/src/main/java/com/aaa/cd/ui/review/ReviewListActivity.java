package com.aaa.cd.ui.review;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import com.aaa.cd.R;

public class ReviewListActivity extends Activity
{
    RecyclerView rv_list;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        rv_list=(RecyclerView)findViewById(R.id.rv_review_list);


    }




}
