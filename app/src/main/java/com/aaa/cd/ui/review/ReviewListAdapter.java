package com.aaa.cd.ui.review;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReviewListAdapter extends RecyclerView.Adapter
{

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    static class ReviewViewHodler extends RecyclerView.ViewHolder
    {
        TextView tv_title;
        public ReviewViewHodler(View itemView)
        {
            super(itemView);
        }
    }

}
