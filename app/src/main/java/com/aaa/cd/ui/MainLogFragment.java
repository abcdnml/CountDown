package com.aaa.cd.ui;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.dao.LogDao;
import com.aaa.cd.model.LogItem;
import com.aaa.cd.model.MainCallback;
import com.aaa.cd.util.Constants;
import com.aaa.cd.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainLogFragment extends MainBaseFragment {

    private static final int MSG_UPDATE_LAST_TIME=0;
    LinearLayoutManager layoutManager;

    RecyclerView rv_log;
    TextView tv_start_timing;
    TextView tv_timing;
    FloatingActionButton fab_addLog;
    TextView tv_date;

    LogAdapter logAdapter;
    List<LogItem> lli;
    LogItem lastLog;
    SimpleDateFormat timeSDF =new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateSDF =new SimpleDateFormat("yyyy-MM-dd");
    ValueAnimator va;
    String lastingTime;
    MainCallback mainCallback;

    public MainLogFragment()
    {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainCallback) {
            mainCallback = (MainCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainCallback=null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getTodayLog();
        lastLog=LogDao.getLastLog(System.currentTimeMillis());
        lastingTime=getString(R.string.lasting_time);

        showEmptyOrList();
    }


    @Override
    public void initTitle(View view){
        TextView tv_content=(TextView)view.findViewById(R.id.tv_title_content);
        tv_content.setText(R.string.menu_log);
        ImageView iv_left=(ImageView)view.findViewById(R.id.iv_title_left);
        iv_left.setVisibility(View.VISIBLE);
        iv_left.setImageResource(R.mipmap.menu);
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainCallback.openMenu(true);
            }
        });
    }

    @Override
    public void initView(View view)
    {
        fab_addLog=(FloatingActionButton)view.findViewById(R.id.fab_add_log);
        fab_addLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),LogDetailActivity.class);
                intent.putExtra(Constants.INTENT_KEY_LOG_MODE,Constants.INTENT_VALUE_LOG_MODE_ADD);
                startActivityForResult(intent,Constants.REQUEST_CODE_LOG);
            }
        });

        layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        logAdapter=new LogAdapter();
        rv_log=(RecyclerView)view.findViewById(R.id.rv_log);
        rv_log.setAdapter(logAdapter);
        rv_log.setLayoutManager(layoutManager);
        rv_log.setItemAnimator( new DefaultItemAnimator());
        rv_log.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
                tv_date.setText(dateSDF.format(new Date(lli.get(firstItemPosition).getTime())));
            }
        });

        tv_start_timing =(TextView)view.findViewById(R.id.tv_start_timing);
        tv_start_timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFirstLog();
                getTodayLog();
                lastLog=LogDao.getLastLog(System.currentTimeMillis());
                showEmptyOrList();
            }
        });
        tv_date=(TextView)view.findViewById(R.id.tv_log_date);
        tv_timing=(TextView)view.findViewById(R.id.tv_timing);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_log;
    }

    public void addFirstLog(){
        LogItem item=new LogItem();
        item.setTitle(getString(R.string.start_timing));
        item.setContent(getString(R.string.start_timing_desc));
        item.setDuration(0);
        item.setTime(System.currentTimeMillis());
        LogDao.addLog(item);
    }

    public void showEmptyOrList(){
        if(lastLog==null)
        {
            tv_start_timing.setVisibility(View.VISIBLE);
            rv_log.setVisibility(View.GONE);
            fab_addLog.setVisibility(View.GONE);
            tv_timing.setVisibility(View.GONE);
            tv_date.setVisibility(View.GONE);

            handler.removeMessages(MSG_UPDATE_LAST_TIME);
        }else
        {
            tv_start_timing.setVisibility(View.GONE);
            fab_addLog.setVisibility(View.VISIBLE);
            rv_log.setVisibility(View.VISIBLE);
            tv_timing.setVisibility(View.VISIBLE);
            tv_date.setVisibility(View.VISIBLE);

            handler.removeMessages(MSG_UPDATE_LAST_TIME);
            handler.sendEmptyMessage(MSG_UPDATE_LAST_TIME);
        }

        if(lli.size()>0)
        {
            tv_date.setText(dateSDF.format(new Date(lli.get(0).getTime())));
        }else{
            tv_date.setVisibility(View.GONE);
        }
    }

    public void getTodayLog()
    {
        Calendar c= Calendar.getInstance(Locale.getDefault());
        long end=c.getTimeInMillis();
        c.set(Calendar.DAY_OF_MONTH,-7);
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        long start=c.getTimeInMillis();

        Log.i("aaa","start : "+ start +":  end"+end);

        lli= LogDao.getLogByTime(start,end);
        Log.i("aaa","list size"+lli.size());
        logAdapter.notifyDataSetChanged();
        if(lli.size()>0){
            rv_log.scrollToPosition(0);
        }
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_UPDATE_LAST_TIME:
                    tv_timing.setText(lastingTime+Utils.getLastingTime(System.currentTimeMillis()-lastLog.getTime()));
                    sendEmptyMessageDelayed(MSG_UPDATE_LAST_TIME,1000);
                    break;
            }
        }
    };


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
            holder.tv_duration.setText(Utils.getDuration(li.getDuration()));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.REQUEST_CODE_LOG)
        {
            getTodayLog();
            lastLog=LogDao.getLastLog(System.currentTimeMillis());
            showEmptyOrList();
        }
    }
}
