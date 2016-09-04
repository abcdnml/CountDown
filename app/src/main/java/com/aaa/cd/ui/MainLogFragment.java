package com.aaa.cd.ui;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.dao.LogDao;
import com.aaa.cd.model.LogItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainLogFragment extends Fragment {

    LinearLayoutManager layoutManager;

    RecyclerView rv_log;
    TextView tv_timing;
    FloatingActionButton fab_addLog;

    LogAdapter logAdapter;
    List<LogItem> lli;
    LogItem lastLog;
    SimpleDateFormat timeSDF =new SimpleDateFormat("HH:mm:ss");

    public MainLogFragment()
    {
        getTodayLog();
        lastLog=LogDao.getLastLog();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_log, container, false);
        layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        logAdapter=new LogAdapter();
        initView(view);

        return view;
    }

    public void initView(View view)
    {
        fab_addLog=(FloatingActionButton)view.findViewById(R.id.fab_add_log);
        fab_addLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogItem item=new LogItem();
                item.setTime(System.currentTimeMillis());
                item.setTitle("");
                item.setContent("");
                item.setDuration(System.currentTimeMillis());
                LogDao.addLog(item);
                logAdapter.notifyDataSetChanged();
            }
        });

        rv_log=(RecyclerView)view.findViewById(R.id.rv_log);
        rv_log.setAdapter(logAdapter);
        rv_log.setItemAnimator( new DefaultItemAnimator());

        tv_timing=(TextView)view.findViewById(R.id.tv_start_timing);

        showEmptyOrList();
    }

    public void showEmptyOrList(){
        if(lli.size()>0)
        {
            tv_timing.setVisibility(View.GONE);
            rv_log.setVisibility(View.VISIBLE);
        }else
        {
            tv_timing.setVisibility(View.VISIBLE);
            rv_log.setVisibility(View.GONE);
        }
    }

    public void getTodayLog()
    {
        Calendar c= Calendar.getInstance(Locale.getDefault());
        long end=c.getTimeInMillis();
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        long start=c.getTimeInMillis();

        lli= LogDao.getLogByTime(start,end);
        logAdapter.notifyDataSetChanged();
    }


    class  LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder>{

        private LayoutInflater inflater;

        public LogAdapter()
        {
            inflater=LayoutInflater. from(getActivity());
        }

        @Override
        public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.rv_item_log,parent, false);
            return new LogViewHolder(view);
        }

        @Override
        public void onBindViewHolder(LogViewHolder holder, int position) {
            LogItem li=lli.get(position);
            holder.tv_time.setText(timeSDF.format(new Date(li.getTime())));
            holder.tv_title.setText(li.getTitle());
            holder.tv_content.setText(li.getContent());
        }

        @Override
        public int getItemCount() {
            return lli.size();
        }

       public class LogViewHolder extends RecyclerView.ViewHolder {
            TextView tv_time;
            TextView tv_title;
            TextView tv_content;
            public LogViewHolder(View view) {
                super(view);
                tv_time=(TextView) view.findViewById(R.id.rv_item_log_time);
                tv_title=(TextView) view.findViewById(R.id.rv_item_log_title);
                tv_content=(TextView) view.findViewById(R.id.rv_item_log_content);
            }

        }
    }

}
