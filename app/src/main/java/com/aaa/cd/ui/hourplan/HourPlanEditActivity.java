package com.aaa.cd.ui.hourplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.dao.HourPlanDao;
import com.aaa.cd.po.HourPlan;
import com.aaa.cd.ui.log.MyTextWatcher;
import com.aaa.cd.util.Constants;
import com.aaa.cd.util.CountDownApplication;
import com.aaa.cd.util.LogUtil;

public class HourPlanEditActivity extends AppCompatActivity
{

    private int MODE = Constants.INTENT_VALUE_HOURPLAN_MODE_ADD;

    private TextInputLayout til_title;
    private TextInputLayout til_desc;
    private EditText et_title;
    private EditText et_desc;
    private TextView tv_planTime;
    private long plan_hour;
    private long plan_min;
    private HourPlan plan;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour_plan_edit);
        Intent intent = getIntent();
        MODE = intent.getIntExtra(Constants.INTENT_KEY_HOURPLAN_MODE, Constants.INTENT_VALUE_HOURPLAN_MODE_ADD);
        initTitle();
        initView();
    }
    public void initTitle()
    {
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText(R.string.menu_hour_plan);
        ImageView iv_left = (ImageView) findViewById(R.id.iv_title_left);
        iv_left.setVisibility(View.VISIBLE);
        iv_left.setImageResource(R.mipmap.back);
        iv_left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
    }
    private void initView()
    {
        til_title = (TextInputLayout) findViewById(R.id.til_hour_plan_title);
        et_title = (EditText) findViewById(R.id.et_hour_plan_title);
        et_title.addTextChangedListener(new MyTextWatcher(til_title));

        til_desc = (TextInputLayout) findViewById(R.id.til_hour_plan_desc);
        et_desc = (EditText) findViewById(R.id.et_hour_plan_desc);
        et_desc.addTextChangedListener(new MyTextWatcher(til_desc));

        if (MODE == Constants.INTENT_VALUE_HOURPLAN_MODE_ADD)
        {
            et_title.setText("");
            et_desc.setText("");
            plan_hour = 1000;
            plan_min = 0;
            plan = new HourPlan();
        } else
        {
            int id = getIntent().getIntExtra(Constants.INTENT_KEY_HOURPLAN_ID, -1);
            plan = HourPlanDao.getPlanById(id);
            plan_hour =  plan.getPlanTime() / 1000 / 60 / 60;
            plan_min =  plan.getPlanTime() / 1000 / 60 % 60;
            et_title.setText(plan.getTitle());
            et_desc.setText(plan.getDescription());
        }
        LogUtil.i(plan.toString());
        tv_planTime = (TextView) findViewById(R.id.tv_plan_time);
        tv_planTime.setText(plan_hour + "小时" + plan_min + "分钟");

        btn_submit = (Button) findViewById(R.id.btn_hour_plan_submit);
        btn_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (til_title.isErrorEnabled() || til_desc.isErrorEnabled())
                {
                    return;
                }

                plan.setTitle(et_title.getText().toString());
                plan.setDescription(et_desc.getText().toString());
                plan.setPlanTime(plan_hour*3600*1000+plan_min*60*1000);
                if(MODE == Constants.INTENT_VALUE_HOURPLAN_MODE_ADD)
                {
                    plan.setType(1);
                    plan.setExecuteStartTime(-1);
                    plan.setCurrentTime(0);
                    plan.setCreateTime(System.currentTimeMillis());
                    plan.setUserId(CountDownApplication.getApplication().getUser().getId());
                    HourPlanDao.addHourPlan(plan);
                }else{
                    HourPlanDao.updatePlan(plan);
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    public void setTarget(View view)
    {
        showSetHourPlanTime(plan_hour, plan_min);
    }

    public void showSetHourPlanTime(long defaultHour, long defaultMin)
    {
        View rootView = LayoutInflater.from(this).inflate(R.layout.dialog_set_hourplan_time, null);
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.DialogTheme).setTitle(R.string.hour_plan_time).setView(rootView).show();

        final TextInputLayout til_hour = (TextInputLayout) rootView.findViewById(R.id.til_hourplan_hour);
        final TextInputLayout til_min = (TextInputLayout) rootView.findViewById(R.id.til_hourplan_min);
        final EditText et_hour = (EditText) rootView.findViewById(R.id.acet_hourplan_hour);
        final EditText et_min = (EditText) rootView.findViewById(R.id.acet_hourplan_min);
        et_hour.addTextChangedListener(new HourPlanTextWatcher(til_hour, HourPlanTextWatcher.TYPE_HOUR));
        et_min.addTextChangedListener(new HourPlanTextWatcher(til_min, HourPlanTextWatcher.TYPE_MIN));
        et_hour.setText("" + defaultHour);
        et_min.setText("" + defaultMin);
        et_hour.requestFocus();
        et_hour.setSelection(et_hour.length());
        et_min.setSelection(et_min.length());




        rootView.findViewById(R.id.tv_hour_plan_ensure).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                LogUtil.i("has error ?  hour:  " + til_hour.isErrorEnabled() + "  min : " + til_min.isErrorEnabled());
                if (til_hour.isErrorEnabled() || til_min.isErrorEnabled())
                {
                    return;
                }
                String hourStr = et_hour.getText().toString().trim();
                String minStr = et_min.getText().toString().trim();
                tv_planTime.setText(hourStr + "小时" + minStr + "分钟");

                plan_hour=Integer.parseInt(hourStr);
                plan_min=Integer.parseInt(minStr);

                dialog.dismiss();
            }
        });

        rootView.findViewById(R.id.tv_hour_plan_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }
}
