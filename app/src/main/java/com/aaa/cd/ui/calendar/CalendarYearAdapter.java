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

public class CalendarYearAdapter extends RecyclerView.Adapter<CalendarYearAdapter.CalendarViewHolder>
{
    private long startTime;
    private long endTime;
    private int monthCount;
    private int yearCount;
    private int currentYear;
    private LayoutInflater inflater;
    private ItemClickListener listener;

    public CalendarYearAdapter(Context context, long startTime, long endTime, ItemClickListener listener)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        setOnItemClickListener(listener);
        this.inflater = LayoutInflater.from(context);
        if (startTime > 0 && endTime > 0)
        {
            Calendar current = Calendar.getInstance();
            current.setTimeInMillis(System.currentTimeMillis());

            Calendar from = Calendar.getInstance();
            from.setTimeInMillis(startTime);
            Calendar to = Calendar.getInstance();
            to.setTimeInMillis(endTime);
            int fromYear = from.get(Calendar.YEAR);
            int fromMonth = from.get(Calendar.MONTH);
            int toYear = to.get(Calendar.YEAR);
            int toMonth = to.get(Calendar.MONTH);

            int currentYear = current.get(Calendar.YEAR);

            this.currentYear = currentYear - fromYear;
            yearCount = toYear - fromYear + 1;
            monthCount = toYear * 12 + toMonth - (fromYear * 12 + fromMonth) + 1;
        } else
        {
            monthCount = 120;
        }

    }

    public void setOnItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.rv_item_calendar, parent, false);
        CalendarViewHolder holder = new CalendarViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CalendarViewHolder holder, final int position)
    {
        LogUtil.i("calendar position" + position);
        /*if (position < 12 * 15)
        {
            LogUtil.i("calendar color GREEN" );
            holder.ll_year.setBackgroundColor(Color.GREEN);
        } else if (position < 12 * 30)
        {
            holder.ll_year.setBackgroundColor(Color.BLUE);
        } else if (position < 12 * 40)
        {
            holder.ll_year.setBackgroundColor(Color.YELLOW);
        } else if (position < 12 * 50)
        {
            holder.ll_year.setBackgroundColor(Color.RED);
        } else if (position < 12 * 60)
        {
            holder.ll_year.setBackgroundColor(Color.CYAN);
        } else if (position < 12 * 70)
        {
            holder.ll_year.setBackgroundColor(Color.LTGRAY);
        } else if (position < 12 * 80)
        {
            holder.ll_year.setBackgroundColor(Color.GRAY);
        } else
        {
            holder.ll_year.setBackgroundColor(Color.WHITE);
            //活了这么久 即使什么都没做 也算成功了
        }*/
        long currentTime = System.currentTimeMillis();
        if (currentTime < startTime)
        {
            holder.ll_year.setBackgroundColor(Color.GREEN);
        } else if (currentTime > endTime)
        {
            holder.ll_year.setBackgroundColor(Color.GRAY);
        } else
        {
            if ((position + 1) <= currentYear)
            {
                holder.ll_year.setBackgroundColor(Color.GREEN);
            } else
            {
                holder.ll_year.setBackgroundColor(Color.WHITE);
            }
        }
        holder.ll_year.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (listener != null)
                {
                    listener.onItemClick(position,v);
                }
            }
        });
        ViewCompat.setTransitionName(holder.ll_year, String.valueOf(position) + "_image");
        holder.tv_item.setTextColor(Color.DKGRAY);
        holder.tv_item.setText(position + 1 + "");
    }

    @Override
    public int getItemCount()
    {
        return yearCount;
    }


    static class CalendarViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout ll_year;
        TextView tv_item;

        public CalendarViewHolder(View itemView)
        {
            super(itemView);
            ll_year = (LinearLayout) itemView.findViewById(R.id.rv_item_ll_calendar);
            tv_item = (TextView) itemView.findViewById(R.id.rv_item_tv_id);
        }
    }
}
