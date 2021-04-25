package com.aaa.cd.ui.main;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;
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
import com.aaa.cd.util.LogUtil;
import com.aaa.cd.util.SPUtil;
import com.aaa.cd.view.ClockView;
import com.aaa.lib.view.timepicker.WheelDatePicker;
import com.aaa.lib.view.timepicker.listener.OnDatetimeSetListener;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCountDownFragment extends MainBaseFragment {

    private ClockView cv_clock;
    private ListView lv_count_down;
    private CheckBox cb_life_death;
    private float touchX, touchY;
    private View death;
    boolean liveOrDeath = true;
    private static final double YEAR_DAY = 365.242199074d;
    private static final long DAY_TIME = 86400000;


    private RelativeLayout rl_content;
    private RelativeLayout rl_setContent;
    private TextView tv_age;
    private TextView tv_set_age;
    private long bornDate;
    private long deathDate;
    private DecelerateInterpolator interpolator;

    public MainCountDownFragment() {
        // Required empty public constructor
        bornDate = (long) SPUtil.get(Constants.SP_KEY_BORN_DATE, -1L);
        deathDate = (long) SPUtil.get(Constants.SP_KEY_DEATH_DATE, -1L);
        interpolator = new DecelerateInterpolator();
    }

    @Override
    public void initTitle(View view) {
        TextView tv_title_content = (TextView) view.findViewById(R.id.tv_title_content);
        tv_title_content.setText(R.string.menu_count_down);
        ImageView iv_left = (ImageView) view.findViewById(R.id.iv_title_left);
        iv_left.setVisibility(View.VISIBLE);
        iv_left.setImageResource(R.mipmap.menu);
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainCallback.openMenu(true);
            }
        });

        ImageView iv_right = (ImageView) view.findViewById(R.id.iv_title_right);
        iv_right.setImageResource(R.drawable.selector_setting);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    public void initView(View view) {
        rl_content = (RelativeLayout) view.findViewById(R.id.rl_coundown_content);
        rl_setContent = (RelativeLayout) view.findViewById(R.id.rl_coundown_set_content);
        tv_age = (TextView) view.findViewById(R.id.tv_age);
        tv_set_age = (TextView) view.findViewById(R.id.tv_set_age);

        cv_clock = (ClockView) view.findViewById(R.id.cv_clock);
        cv_clock.setMute(true);
        cv_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_clock.setMute(!cv_clock.isMute());
            }
        });
        lv_count_down = (ListView) view.findViewById(R.id.lv_count_down);
        death = view.findViewById(R.id.ll_death);

        cb_life_death = (CheckBox) view.findViewById(R.id.cb_life_death);
        cb_life_death.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                liveOrDeath = !isChecked;
                if (isChecked) {
                    setLastAge();
                    animShow();
                } else {
                    setCurrentAge();
                    animHide();
                }
            }
        });
        cb_life_death.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    Rect rect = new Rect();
                    getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                    touchX = event.getRawX();
                    touchY = event.getRawY() - rect.top;
                    LogUtil.i("cb_life_death touch x : " + touchX + "   y : " + touchY);
                }
                return false;
            }
        });
        setCurrentAge();
    }

    public void setCurrentAge() {
        if (bornDate == -1) {
            rl_content.setVisibility(View.GONE);
        } else {
            rl_content.setVisibility(View.VISIBLE);
            hideDeathAndShowLive();
        }
    }

    public void setLastAge() {
        if (deathDate == -1) {
            rl_content.setVisibility(View.GONE);
        } else {
            rl_content.setVisibility(View.VISIBLE);
            hideLiveAndShowDeath();
        }
    }

    public String getLiveAge() {
        long live = System.currentTimeMillis() - bornDate;
        if (bornDate == -1) {
            return "";
        } else {
            if (deathDate == -1) {
                int age = (int) (live / YEAR_DAY * DAY_TIME);
                int day = (int) (live % (YEAR_DAY * DAY_TIME) / DAY_TIME);
                StringBuilder str = new StringBuilder("你在这世界已经存在" + age + "年" + day + "天了,");
                return str.toString();
            } else {
                long life = deathDate - bornDate;
                double percent = live * 1d / life;
                int hour = (int) (percent * DAY_TIME) / (3600 * 1000);
                int min = (int) (percent * DAY_TIME) % (3600 * 1000) / (60 * 1000);
                int age = (int) (live / (YEAR_DAY * DAY_TIME));
                int day = (int) (live % (YEAR_DAY * DAY_TIME) / DAY_TIME);
                LogUtil.i("live : " + live + "  age : " + age + "  day : " + day);

                StringBuilder str = new StringBuilder("你在这世界已经存在" + age + "年" + day + "天了");
                str.append("\n");
                str.append("生命进度条已经走到了" + String.format("%.2f", percent * 100) + "%");
                str.append("\n");
                if (live < DAY_TIME * YEAR_DAY * 7) {

                    str.append("你还小 尽情的去玩 去触摸这世界把");
                    return str.toString();
                }
                str.append("相当于一天中的" + hour + "点" + min + "分");
                str.append("\n");
                if (live < DAY_TIME * YEAR_DAY * 25 || percent < 0.25) {
                    str.append("十五志于学");
                    str.append("\n");
                    str.append("努力学习吧 吸收一切能使你强大的东西");
                    str.append("\n");
                    str.append("养成好的学习习惯 生活习惯 思维习惯 这会让你在后面的路上更加顺畅,每个人的区别 其实就从这时的习惯开始");
                    return str.toString();
                }
                if (live < DAY_TIME * YEAR_DAY * 32 || percent < 0.32) {
                    str.append("三十而立 立身 立家 立业");
                    str.append("\n");
                    str.append("属于你自己的生活才刚刚开始 加油 ! ");
                    str.append("\n");
                    str.append("这是一个关键时期 你开始接触生活 生活可能使你沉沦 也能让你变成你想要的模样  你需要自律 控制欲望 ");
                    return str.toString();
                }

                if (live < DAY_TIME * YEAR_DAY * 55 || percent < 0.55) {
                    str.append("四十不惑  认清世界的样子 自己的样子 知道想做什么 可以做什么 应该做什么 ");
                    str.append("\n");
                    str.append("这时你人生的黄金时刻 你一生所有的成就 就在此刻建立 放开双手 大展宏图吧");
                    return str.toString();
                }


                if (live < DAY_TIME * YEAR_DAY * 65 || percent < 0.65) {
                    str.append("五十知天命 改努力的已经努力过了 一辈子就差不多是这样了 接受现实吧 做些 力所能及的事");
                    str.append("\n");
                    str.append("如果你觉得 你一无所成 也将一无所成 那么 不如学着去好好生活吧 爱自己 爱家人 和这个世界");
                    return str.toString();
                }
                if (live < DAY_TIME * YEAR_DAY * 100 || percent <= 1) {
                    str.append("六十耳顺 七十从心所欲 不逾矩");
                    return str.toString();
                }

                str.append("要么你已经逆天了 ,要么时间设置错了 ");
                return str.toString();

            }
        }
    }

    public String getLeftString() {
        long left = deathDate - System.currentTimeMillis();
        if (deathDate == -1) {
            return "";
        } else {
            if (bornDate == -1) {
                int age = (int) (left / YEAR_DAY * DAY_TIME);
                int day = (int) (left % (YEAR_DAY * DAY_TIME) / DAY_TIME);
                StringBuilder str = new StringBuilder("你的生命还剩" + age + "年" + day + "天,");
                return str.toString();
            } else {
                long life = deathDate - bornDate;
                double percent = left * 1d / life;
                int hour = (int) (percent * DAY_TIME) / (3600 * 1000);
                int min = (int) (percent * DAY_TIME) % (3600 * 1000) / (60 * 1000);
                int age = (int) (left / (YEAR_DAY * DAY_TIME));
                int day = (int) (left % (YEAR_DAY * DAY_TIME) / DAY_TIME);
                StringBuilder str = new StringBuilder("你的生命还剩" + age + "年" + day + "天");
                str.append("\n");
                str.append("生命进度条还剩" + String.format("%.2f", percent * 100) + "%");
                str.append("\n");
                str.append("无论怎样 请你记住: 你还有时间 你还可以努力 还可以追求自己想要的");
                return str.toString();
            }
        }
    }

    public void hideLiveAndShowDeath() {
        AlphaAnimation mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(2000);
        mHideAnimation.setFillAfter(true);
        mHideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tv_age.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv_age.setVisibility(View.GONE);

                tv_age.setText(getLeftString());
                AlphaAnimation mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
                mShowAnimation.setDuration(3000);
                mShowAnimation.setFillAfter(true);
                mShowAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        tv_age.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tv_age.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                tv_age.startAnimation(mShowAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tv_age.startAnimation(mHideAnimation);
    }

    public void hideDeathAndShowLive() {
        AlphaAnimation mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(2000);
        mHideAnimation.setFillAfter(true);
        mHideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tv_age.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv_age.setVisibility(View.GONE);

                tv_age.setText(getLiveAge());
                AlphaAnimation mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
                mShowAnimation.setDuration(3000);
                mShowAnimation.setFillAfter(true);
                mShowAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        tv_age.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tv_age.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                tv_age.startAnimation(mShowAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tv_age.startAnimation(mHideAnimation);
    }

    /**
     * View渐隐动画效果
     */
    public void setHideAnimation(final View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }
        AlphaAnimation mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        mHideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(mHideAnimation);
    }

    /**
     * View渐现动画效果
     */
    public void setShowAnimation(final View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }
        AlphaAnimation mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);
        mShowAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(mShowAnimation);
    }

    private void animShow() {
        // 从 View 的中心开始
        int cx = (death.getLeft() + death.getRight()) / 2;
        int cy = 0;

        cx = (int) touchX;
        cy = (int) touchY;

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

    private void animHide() {
        int cx = (death.getLeft() + death.getRight()) / 2;
        int cy = death.getBottom();


        cx = (int) touchX;
        cy = (int) touchY;

        int initialRadius = (int) Math.sqrt(death.getHeight() * death.getHeight() + death.getWidth() * death.getWidth());
        // 自定义颜色
        ((MainActivity) getActivity()).setStatusBarColor(Color.parseColor("#7777FF"));
        // 半径 从 viewWidth -> 0
        Animator anim = ViewAnimationUtils.createCircularReveal(death, cx, cy, initialRadius, 0);
        anim.setDuration(1000);
        anim.setInterpolator(interpolator);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                death.setVisibility(View.INVISIBLE);
            }
        });
        anim.start();
    }

    public void showDatePickerDialog() {
        Date date = null;
        if (liveOrDeath) {
            date = new Date(bornDate);
        } else {
            date = new Date(deathDate);
        }
        String title = getString(liveOrDeath ? R.string.born_date : R.string.death_date);
        WheelDatePicker dpu = new WheelDatePicker(getActivity());
        dpu.datePicker(date, title, new OnDatetimeSetListener() {
            // 点击了确定才会执行此回调
            @Override
            public void onDatetimeSet(String timestamp, Date date) {
                if (date != null) {
                    if (liveOrDeath) {
                        bornDate = date.getTime();
                        setCurrentAge();
                        SPUtil.put(Constants.SP_KEY_BORN_DATE, bornDate);
                        rl_content.setVisibility(View.VISIBLE);
                    } else {
                        deathDate = date.getTime();
                        setLastAge();
                        SPUtil.put(Constants.SP_KEY_DEATH_DATE, deathDate);
                        rl_content.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_count_down;
    }
}

