package com.aaa.cd.ui.main;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.util.Constants;
import com.aaa.cd.util.SPUtil;
import com.aaa.cd.view.ClockView;
import com.aaa.lib.view.timepicker.WheelDatePicker;
import com.aaa.lib.view.timepicker.listener.OnDatetimeSetListener;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCountDownFragment extends MainBaseFragment
{

    ClockView cv_clock;
    ListView lv_count_down;
    CheckBox cb_life_death;
    View death;
    boolean liveOrDeath = true;

    RelativeLayout rl_content;
    RelativeLayout rl_setContent;
    TextView tv_age;
    TextView tv_set_age;
    private long bornDate;
    private long deathDate;
    private DecelerateInterpolator interpolator;

    public MainCountDownFragment()
    {
        // Required empty public constructor
        bornDate = (long) SPUtil.get(Constants.SP_KEY_BORN_DATE, -1L);
        deathDate = (long) SPUtil.get(Constants.SP_KEY_DEATH_DATE, -1L);
        interpolator=new DecelerateInterpolator();
    }

    @Override
    public void initTitle(View view)
    {
        TextView tv_title_content = (TextView) view.findViewById(R.id.tv_title_content);
        tv_title_content.setText(R.string.menu_count_down);
        ImageView iv_left = (ImageView) view.findViewById(R.id.iv_title_left);
        iv_left.setVisibility(View.VISIBLE);
        iv_left.setImageResource(R.mipmap.menu);
        iv_left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mainCallback.openMenu(true);
            }
        });

        ImageView iv_right = (ImageView) view.findViewById(R.id.iv_title_right);
        iv_right.setImageResource(R.drawable.selector_setting);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                if (liveOrDeath)
//                {
//                    tv_set_age.setText(R.string.born_date);
//                } else
//                {
//                    tv_set_age.setText(R.string.death_date);
//                }
//                setHideAnimation(rl_content, 2000);
//                setShowAnimation(rl_setContent, 2000);
                showDatePickerDialog();
            }
        });
    }


    @Override
    public void initView(View view)
    {
        rl_content = (RelativeLayout) view.findViewById(R.id.rl_coundown_content);
        rl_setContent = (RelativeLayout) view.findViewById(R.id.rl_coundown_set_content);
        tv_age = (TextView) view.findViewById(R.id.tv_age);
        tv_set_age = (TextView) view.findViewById(R.id.tv_set_age);
        setCurrentAge();

        cv_clock = (ClockView) view.findViewById(R.id.cv_clock);
        lv_count_down = (ListView) view.findViewById(R.id.lv_count_down);
        death = view.findViewById(R.id.ll_death);

        cb_life_death = (CheckBox) view.findViewById(R.id.cb_life_death);
        cb_life_death.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                liveOrDeath = !isChecked;
                if (isChecked)
                {
                    setLastAge();
                    animShow();
                } else
                {
                    setCurrentAge();
                    animHide();
                }
            }
        });
    }

    public void setCurrentAge()
    {
        if (bornDate == -1)
        {
            rl_content.setVisibility(View.GONE);
        } else
        {
            rl_content.setVisibility(View.VISIBLE);
            long live = System.currentTimeMillis() - bornDate;
            tv_age.setText(String.format(getResources().getString(R.string.live_age), live / (365.2d*24*60*60*1000)));
        }
    }

    public void setLastAge()
    {
        if (deathDate == -1)
        {
            rl_content.setVisibility(View.GONE);
        } else
        {
            rl_content.setVisibility(View.VISIBLE);
            long death = deathDate - System.currentTimeMillis();
            tv_age.setText(String.format(getResources().getString(R.string.left_age), death / (365.2d*24*60*60*1000)));
        }
    }

    /**
     * View渐隐动画效果
     */
    public void setHideAnimation(final View view, int duration)
    {
        if (null == view || duration < 0)
        {
            return;
        }
        AlphaAnimation mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        mHideAnimation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        view.startAnimation(mHideAnimation);
    }

    /**
     * View渐现动画效果
     */
    public void setShowAnimation(final View view, int duration)
    {
        if (null == view || duration < 0)
        {
            return;
        }
        AlphaAnimation mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        mShowAnimation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        view.startAnimation(mShowAnimation);
    }

    private void animShow()
    {

        // 从 View 的中心开始
        int cx = (death.getLeft() + death.getRight()) / 2;
        int cy = 0;

        int finalRadius = (int) Math.sqrt(death.getHeight() * death.getHeight() + death.getWidth() * death.getWidth());
        ((MainActivity) getActivity()).setStatusBarColor(Color.parseColor("#000000"));

        //为此视图创建动画设计(起始半径为零)
        Animator anim = ViewAnimationUtils.createCircularReveal(death, cx, cy, 0, finalRadius);
        // 使视图可见并启动动画
        death.setVisibility(View.VISIBLE);
        anim.setDuration(1000);
        anim.setInterpolator(interpolator);
        anim.start();
    }

    private void animHide()
    {
        int cx = (death.getLeft() + death.getRight()) / 2;
        int cy = death.getBottom();

        int initialRadius = (int) Math.sqrt(death.getHeight() * death.getHeight() + death.getWidth() * death.getWidth());
        // 自定义颜色
        ((MainActivity) getActivity()).setStatusBarColor(Color.parseColor("#7777FF"));
        // 半径 从 viewWidth -> 0
        Animator anim = ViewAnimationUtils.createCircularReveal(death, cx, cy, initialRadius, 0);
        anim.setDuration(1000);
        anim.setInterpolator(interpolator);
        anim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                death.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

    public void showDatePickerDialog()
    {
        WheelDatePicker dpu = new WheelDatePicker(getActivity());
        dpu.datePicker(new Date(),
                getString(liveOrDeath ? R.string.born_date : R.string.death_date),
                new OnDatetimeSetListener()
                {
                    // 点击了确定才会执行此回调
                    @Override
                    public void onDatetimeSet(String timestamp, Date date)
                    {
                        if (date != null)
                        {
                            if (liveOrDeath)
                            {
                                bornDate=date.getTime();
                                setCurrentAge();
                                SPUtil.put(Constants.SP_KEY_BORN_DATE,bornDate);
                                rl_content.setVisibility(View.VISIBLE);
                            } else
                            {
                                deathDate=date.getTime();
                                setLastAge();
                                SPUtil.put(Constants.SP_KEY_DEATH_DATE, deathDate);
                                rl_content.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }

    @Override
    public int getLayoutId()
    {
        return R.layout.fragment_main_count_down;
    }
}

