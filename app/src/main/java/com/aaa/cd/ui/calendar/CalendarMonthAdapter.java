package com.aaa.cd.ui.calendar;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.model.ItemClickListener;
import com.aaa.cd.util.LogUtil;

import java.util.Calendar;

public class CalendarMonthAdapter extends RecyclerView.Adapter<CalendarMonthAdapter.CalendarViewHolder>
{
    private long startTime;
    private long endTime;
    private int currentMonth;
    private LayoutInflater inflater;
    private ItemClickListener listener;

    public CalendarMonthAdapter(Context context, long startTime, long endTime, ItemClickListener listener)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        setOnItemClickListener(listener);
        this.inflater = LayoutInflater.from(context);
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(System.currentTimeMillis());
        currentMonth = current.get(Calendar.MONTH);

    }

    public void setOnItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.rv_item_calendar_month, parent, false);
        CalendarViewHolder holder = new CalendarViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder holder, final int position)
    {
        LogUtil.i("calendar position" + position);
        if (position == currentMonth)
        {
            holder.ll_month.setBackgroundColor(Color.parseColor("#777777"));
            holder.tv_item.setTextColor(Color.WHITE);

        }else{
            holder.ll_month.setBackgroundColor(Color.WHITE);
            holder.tv_item.setTextColor(Color.DKGRAY);
        }
        ViewCompat.setTransitionName(holder.ll_month,  "month_"+String.valueOf(position));
        holder.ll_month.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (listener != null)
                {
                    listener.onItemClick(position, v);
                }
            }
        });
        holder.tv_item.setText(position + 1 + "");
    }

    @Override
    public int getItemCount()
    {
        return 12;
    }


    static class CalendarViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout ll_month;
        TextView tv_item;

        public CalendarViewHolder(View itemView)
        {
            super(itemView);
            ll_month = (LinearLayout) itemView.findViewById(R.id.rv_item_ll_calendar_month);
            tv_item = (TextView) itemView.findViewById(R.id.rv_item_tv_month);
        }
    }
}
