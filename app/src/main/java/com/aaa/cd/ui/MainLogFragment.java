package com.aaa.cd.ui;


import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.dao.LogDao;
import com.aaa.cd.model.LogItem;
import com.aaa.cd.util.Utils;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
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
    TextView tv_start_timing;
    TextView tv_timing;
    FloatingActionButton fab_addLog;

    LogAdapter logAdapter;
    List<LogItem> lli;
    LogItem lastLog;
    SimpleDateFormat timeSDF =new SimpleDateFormat("HH:mm:ss");
    ValueAnimator va;
    String lastingTime;

    public MainLogFragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastLog=LogDao.getLastLog();
        lastingTime=getString(R.string.lasting_time);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_log, container, false);
        initView(view);
        getTodayLog();
        return view;
    }

    int testId=0;
    public void initView(View view)
    {
        fab_addLog=(FloatingActionButton)view.findViewById(R.id.fab_add_log);
        fab_addLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddLog();
            }
        });

        layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        logAdapter=new LogAdapter();
        rv_log=(RecyclerView)view.findViewById(R.id.rv_log);
        rv_log.setAdapter(logAdapter);
        rv_log.setLayoutManager(layoutManager);
        rv_log.setItemAnimator( new DefaultItemAnimator());

        tv_start_timing =(TextView)view.findViewById(R.id.tv_start_timing);
        tv_start_timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddLog();
            }
        });

        tv_timing=(TextView)view.findViewById(R.id.tv_timing);

        showEmptyOrList();
    }
    public void toAddLog(){
        testId++;
        LogItem item=new LogItem();
        item.setTime(System.currentTimeMillis());
        item.setTitle("test-"+testId);
        item.setContent("大分类及咖啡的记录时代发附件asfjas;结论为featur爱迪生发婆家分我啦而佛吉安慰房间爱疯了efewae辣味监控联网恩爱 哦骂我分卡我付款咯烦了放开那");
        item.setDuration(12345*testId);
        LogDao.addLog(item);

        getTodayLog();

        showEmptyOrList();
    }



    public void showEmptyOrList(){
        if(lastLog==null)
        {
            tv_start_timing.setVisibility(View.VISIBLE);
            rv_log.setVisibility(View.GONE);
            fab_addLog.setVisibility(View.GONE);
            tv_timing.setVisibility(View.GONE);

            if(va!=null&& va.isRunning()){
                va.cancel();
            }

        }else
        {
            tv_start_timing.setVisibility(View.GONE);
            fab_addLog.setVisibility(View.VISIBLE);
            rv_log.setVisibility(View.VISIBLE);
            tv_timing.setVisibility(View.VISIBLE);

            if(va!=null&& va.isRunning()){
                va.cancel();
            }
            if(lastLog!=null)
            {
                va=ValueAnimator.ofInt(0,86400);
                va.setDuration(86400000);
                va.setInterpolator(new LinearInterpolator());
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        tv_timing.setText(lastingTime+Utils.getLastingTime(System.currentTimeMillis()-lastLog.getTime()));
                    }
                });
                va.start();
            }
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

        Log.i("aaa","start : "+ start +":  end"+end);

        lli= LogDao.getLogByTime(start,end);
        Log.i("aaa","list size"+lli.size());
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
            Log.i("viewholder ", "aaaaaaaaaa position : " +position);
            LogItem li=lli.get(position);
            holder.tv_time.setText(timeSDF.format(new Date(li.getTime())));
            holder.tv_title.setText(li.getTitle());
            holder.tv_content.setText(li.getContent());
            holder.tv_duration.setText(Utils.getLastingTime(li.getTime()));
        }

        @Override
        public int getItemCount() {

            Log.i("viewholder ", "aaaaaaaaaaa size : " +lli.size());
            return lli.size();
        }

       public class LogViewHolder extends RecyclerView.ViewHolder {
            TextView tv_time;
            TextView tv_title;
            TextView tv_content;
            TextView tv_duration;
            public LogViewHolder(View view) {
                super(view);
                tv_time=(TextView) view.findViewById(R.id.rv_item_log_time);
                tv_title=(TextView) view.findViewById(R.id.rv_item_log_title);
                tv_content=(TextView) view.findViewById(R.id.rv_item_log_content);
                tv_duration=(TextView)view.findViewById(R.id.rv_item_log_duration);
            }

        }
    }

}
