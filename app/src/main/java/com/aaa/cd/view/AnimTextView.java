package com.aaa.cd.view;

/**
 description TODO
 created by aaa
 on 2016/8/6.
 */

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AnimTextView extends LinearLayout
{

    private Context context;
    private static final int MSG_NEXT_WORD=0;
    int animation;
    char[] characters;
    int duration;

    public AnimTextView(Context context)
    {
        super(context);
        this.context = context;
    }

    public AnimTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
    }

    public AnimTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
    }


    public void setText(String text, int animation, int duration)
    {
        if (text != null && !text.isEmpty())
        {
            this.characters = text.toCharArray();

            ll = new LinearLayout(context);
            LinearLayout.LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.height=((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()));
            ll.setGravity(Gravity.CENTER_VERTICAL);
            ll.setLayoutParams(params);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            addView(ll);
        }else{
            this.characters=new char[0];
        }
        this.animation=animation;
        this.duration=duration;

        handler.removeMessages(MSG_NEXT_WORD);
        handler.obtainMessage(MSG_NEXT_WORD,0.0).sendToTarget();
    }

    LinearLayout ll;
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            int i=msg.arg1;

            if(msg.what==MSG_NEXT_WORD && i <characters.length)
            {
                char c = characters[i];

                if (c == '\n')
                {
                    ll = new LinearLayout(context);
                    LinearLayout.LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    params.height=((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics()));
                    ll.setGravity(Gravity.CENTER_VERTICAL);
                    ll.setLayoutParams(params);
                    ll.setOrientation(LinearLayout.HORIZONTAL);
                    addView(ll);

                }else{
                    //遍历传入的字符串的每个字符，生成一个TextView，并设置它的动画
                    TextView t = new TextView(context);
                    t.setText(String.valueOf(c));
                    t.setTextSize(18);
                    ll.addView(t);
                    t.startAnimation(AnimationUtils.loadAnimation(context,animation));
                }

                Message message=new Message();
                message.what=msg.what;
                message.arg1=msg.arg1+1;
                handler.sendMessageDelayed(message,duration);

            }
        }
    };

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        // 计算自定义的ViewGroup中所有子控件的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int childLeft = 0;
        // 遍历所有子视图
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View childView = getChildAt(i);

            // 获取在onMeasure中计算的视图尺寸
            int measureHeight = childView.getMeasuredHeight();
            int measuredWidth = childView.getMeasuredWidth();

            //将他们横向排列
            childView.layout(childLeft, 0, childLeft + measuredWidth, measureHeight);

            childLeft += measuredWidth;
        }
    }

    private int measureWidth(int pWidthMeasureSpec)
    {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode)
        {
            *//**
             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
             * MeasureSpec.AT_MOST。
             *
             *
             * MeasureSpec.EXACTLY是精确尺寸，
             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
             * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
             *
             *
             * MeasureSpec.AT_MOST是最大尺寸，
             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
             *
             *
             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
             * 通过measure方法传入的模式。
             *//*
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int pHeightMeasureSpec)
    {
        int result = 0;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode)
        {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }*/
}
