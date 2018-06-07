package com.aaa.cd.ui.review;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.aaa.cd.R;

public class ReviewListActivity extends AppCompatActivity
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
