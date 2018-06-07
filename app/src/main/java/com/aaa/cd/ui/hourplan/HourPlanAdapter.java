package com.aaa.cd.ui.hourplan;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.dao.HourPlanDao;
import com.aaa.cd.model.ItemClickListener;
import com.aaa.cd.po.HourPlan;
import com.aaa.cd.util.Constants;
import com.aaa.cd.util.Utils;

import java.util.List;

public class HourPlanAdapter extends RecyclerView.Adapter<HourPlanAdapter.ViewHolder>
{

    public static final int VIEWTYPE_FINISH=0;
    public static final int VIEWTYPE_START=1;
    public static final int VIEWTYPE_STOP=2;
    private Activity context;
    private List<HourPlan> lhp;
    private LayoutInflater inflater;
    private ItemClickListener clickListener = new ItemClickListener()
    {
        @Override
        public void onItemClick(int position, View view)
        {


        }

        @Override
        public void onItemLongClick(int position, View view)
        {

        }
    };

    public HourPlanAdapter(Activity context, List<HourPlan> lhp)
    {
        this.context = context;
        this.lhp = lhp;
        inflater = LayoutInflater.from(context);
    }

    public void update(List<HourPlan> lhp)
    {
        this.lhp = lhp;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.rv_item_hour_plan, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        HourPlan hp = lhp.get(position);
        holder.tv_title.setText(hp.getTitle());
        holder.tv_startTime.setText("任务已进行: " + Utils.getDuration(System.currentTimeMillis() - hp.getCreateTime()));
        holder.tv_desc.setText(hp.getDescription());
        holder.tv_progress.setText(hp.getCurrentTime() / 1000 / 3600 + "/" + hp.getPlanTime() / 1000 / 3600);
        if (hp.getCurrentTime() >= hp.getPlanTime())
        {
            holder.iv_status.setImageResource(R.drawable.selector_finish);
            holder.iv_status.setClickable(false);
        } else
        {
            holder.iv_status.setClickable(true);
            if (hp.getExecuteStartTime()>0)
            {
                holder.iv_status.setImageResource(R.drawable.selector_pause);
            } else
            {
                holder.iv_status.setImageResource(R.drawable.selector_play);
            }
        }
        holder.iv_status.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HourPlan hp=lhp.get(position);

                if(hp.getExecuteStartTime()>0)
                {
                    long duration=System.currentTimeMillis()-hp.getExecuteStartTime();
                    duration=duration*10000;
                    hp.setExecuteStartTime(-1);
                    hp.setCurrentTime(hp.getCurrentTime()+duration);
                }else{
                    hp.setExecuteStartTime(System.currentTimeMillis());
                }
                HourPlanDao.updatePlan(hp);
                notifyDataSetChanged();
            }
        });
        holder.cl_item.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(context,HourPlanEditActivity.class);
                intent.putExtra(Constants.INTENT_KEY_HOURPLAN_MODE,Constants.INTENT_VALUE_HOURPLAN_MODE_EDIT);
                intent.putExtra(Constants.INTENT_KEY_HOURPLAN_ID,lhp.get(position).getId());
                context.startActivityForResult(intent, Constants.REQUEST_CODE_HOURPLAN_RESULT);
            }
        });

        holder.progress.setMax((int) hp.getPlanTime() / 1000 / 60);
        holder.progress.setProgress((int) hp.getCurrentTime() / 1000 / 60);
    }
    @Override
    public int getItemViewType(int position) {
        HourPlan hp=lhp.get(position);
        if (hp.getCurrentTime() >= hp.getPlanTime())
        {
           return VIEWTYPE_FINISH;
        } else
        {
            if (hp.getExecuteStartTime()>0)
            {
                return VIEWTYPE_STOP;
            } else
            {
                return VIEWTYPE_START;
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return lhp.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout cl_item;
        TextView tv_title;
        TextView tv_startTime;
        TextView tv_desc;
        TextView tv_progress;
        ImageView iv_status;
        ProgressBar progress;

        public ViewHolder(View itemView)
        {
            super(itemView);
            cl_item=(ConstraintLayout)itemView.findViewById(R.id.rv_item_hour_plan);
            tv_title = (TextView) itemView.findViewById(R.id.rv_item_hour_plan_tv_title);
            tv_desc = (TextView) itemView.findViewById(R.id.rv_item_hour_plan_tv_desc);
            tv_startTime = (TextView) itemView.findViewById(R.id.rv_item_hour_plan_tv_start_time);
            tv_progress = (TextView) itemView.findViewById(R.id.rv_item_hour_plan_tv_progress);
            iv_status = (ImageView) itemView.findViewById(R.id.rv_item_hour_plan_iv_status);
            iv_status.setVisibility(View.GONE);
            progress = (ProgressBar) itemView.findViewById(R.id.rv_item_hour_plan_progress);
        }
    }
}
