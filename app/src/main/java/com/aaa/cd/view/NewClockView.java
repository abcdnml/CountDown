package com.aaa.cd.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.aaa.cd.R;
import com.aaa.cd.util.DensityUtil;
import com.aaa.cd.util.LogUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class NewClockView extends View
{

    private SoundPool sp;
    private AudioManager mAudioManager;
    private int soundId;

    private float mRadius;
    private int padding;
    volatile long time;
    private Paint bgPaint;
    private Paint circlePaint;
    private Paint bigDiverderPaint;
    private Paint diverderPaint;
    private Paint textPaint;
    private Paint hourPaint;
    private Paint minutePaint;
    private Paint secondPaint;

    Timer timer;

    public NewClockView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.clock);// TypedArray是一个数组容器
        mRadius = types.getDimension(R.styleable.clock_radiu, 100f);// 防止在XML文件里没有定义，就加上了默认值30
        types.recycle();
        padding = DensityUtil.dp2px(context, 20);
        Log.i("aaaaaaaaaaa ", "aaaaaaaaaaa mRadius 1: " + mRadius);

        initPaint();
        initVoice(context);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        timer = new Timer();
        timer.schedule(new SecendsTimerTask(), 0, 8640);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        timer.cancel();
        timer = null;
    }

    public void initVoice(Context context)
    {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 8, 0);
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
        try
        {
            soundId = sp.load(context.getAssets().openFd("clock.wav"), 1);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void initPaint()
    {
        bgPaint = new Paint();
        bgPaint.setColor(Color.TRANSPARENT);
        bgPaint.setStyle(Style.FILL);
        bgPaint.setAntiAlias(true);

        circlePaint = new Paint();
        circlePaint.setColor(Color.DKGRAY);
        circlePaint.setStrokeJoin(Paint.Join.ROUND);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);
        circlePaint.setStrokeWidth(5);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.STROKE);

        bigDiverderPaint = new Paint();
        bigDiverderPaint.setColor(Color.DKGRAY);
        bigDiverderPaint.setStrokeJoin(Paint.Join.ROUND);
        bigDiverderPaint.setStrokeCap(Paint.Cap.ROUND);
        bigDiverderPaint.setStrokeWidth(5);

        diverderPaint = new Paint();
        diverderPaint.setColor(Color.DKGRAY);
        diverderPaint.setStrokeWidth(3);
        diverderPaint.setStrokeJoin(Paint.Join.ROUND);
        diverderPaint.setStrokeCap(Paint.Cap.ROUND);
        diverderPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(mRadius / 7);
        textPaint.setStrokeJoin(Paint.Join.ROUND);
        textPaint.setStrokeCap(Paint.Cap.ROUND);
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setAntiAlias(true);

        hourPaint = new Paint();
        hourPaint.setColor(Color.GRAY);
        hourPaint.setStrokeWidth(8);
        hourPaint.setStrokeJoin(Paint.Join.ROUND);
        hourPaint.setStrokeCap(Paint.Cap.ROUND);
        hourPaint.setStyle(Style.FILL);
        hourPaint.setAntiAlias(true);

        minutePaint = new Paint();
        minutePaint.setColor(Color.GRAY);
        minutePaint.setStrokeWidth(6);
        minutePaint.setStrokeJoin(Paint.Join.ROUND);
        minutePaint.setStrokeCap(Paint.Cap.ROUND);
        minutePaint.setStyle(Style.FILL);
        minutePaint.setAntiAlias(true);

        secondPaint = new Paint();
        secondPaint.setColor(Color.GRAY);
        secondPaint.setStrokeWidth(4);
        secondPaint.setStrokeJoin(Paint.Join.ROUND);
        secondPaint.setStrokeCap(Paint.Cap.ROUND);
        secondPaint.setStyle(Style.FILL);
        secondPaint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY)
        {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST)
        {
            //Can't be bigger than...
            width = (int) mRadius * 2;
        } else
        {
            //Be whatever you want
            width = (int) mRadius * 2;
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST)
        {
            height = (int) mRadius * 2;
        } else
        {
            height = (int) mRadius * 2;
        }

        width = height = (width > height ? width : height) + padding;


        setMeasuredDimension(width, height);
    }

    /**
     * 绘制罗盘 2015-2-3
     */
    private void drawCompass(Canvas canvas)
    {
//        canvas.drawRect(new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight()), bgPaint);
        canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2); // 平移罗盘
//         canvas.drawCircle(0, 0, mRadius, circlePaint); // 画圆圈


        if (mRadius <= 0)
        {
            mRadius = canvas.getWidth() / 2 - 100;
        }
        // 使用path绘制路径文字
        canvas.save();

        // drawLabel(canvas);
        // canvas.restore();

        drawDividing(canvas);
        long current=System.currentTimeMillis();
        long zero = current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();
        time = current -zero;
        drawHourHand(canvas);
        drawMinuteHand(canvas);
//        drawSecondHand(canvas);

        canvas.drawCircle(0, 0, 10, diverderPaint);

        drawTime(canvas);
        canvas = null;
    }

    /**
     * 绘制罗盘内侧的标签文本 2015-2-4
     */
    private void drawLabel(Canvas canvas)
    {
        canvas.save();

        canvas.translate(-155, -155);
        Path path = new Path();
        path.addArc(new RectF(0, 0, mRadius + 100, mRadius + 100), -180, 180);
        canvas.drawTextOnPath("http://blog.csdn.net/lemon_tree", path, 35, 0, textPaint);

        canvas.restore();

        path = null;
        canvas = null;
    }

    /**
     * 绘制罗盘内时间
     */
    private void drawTime(Canvas canvas)
    {
        canvas.save();
        canvas.drawText(String.format("%.2f", time/864000f)+"%", 0, mRadius / 2, textPaint);
        canvas.restore();
        canvas = null;
    }

    /**
     * 绘制刻度 2015-2-4
     */
    private void drawDividing(Canvas canvas)
    {
        canvas.save();
        float y = mRadius;
        int count = 100; // 总刻度数
        canvas.rotate(36, 0f, 0f); // 旋转画纸
        for (int i = 0; i < count; i++)
        {
            if (i % 10 == 0)
            {
                canvas.drawText(String.valueOf(i / 10 + 1), 0, -y - 15, textPaint);
                canvas.drawLine(0f, -y, 0f, -y + 45f, bigDiverderPaint);
                // canvas.drawLine(0f, -y, 0, -y + 20f, mPaint);
            } else
            {
                if(i%5==0){
                    canvas.drawLine(0f, -y, 0f, -y + 30f, diverderPaint);
                }else{
                    canvas.drawLine(0f, -y, 0f, -y + 15f, diverderPaint);
                }
            }

            canvas.rotate(3.6f, 0f, 0f); // 旋转画纸
        }
        canvas.restore();
        canvas = null;
    }

    /**
     * 绘制秒针 2015-2-4
     */
    private void drawSecondHand(Canvas canvas)
    {
        canvas.save();
        float angle = (time%8640)*360f/8640; // 计算角度
        LogUtil.i(" senconds angle : "+angle);
        canvas.rotate(angle, 0f, 0f);
        canvas.drawLine(0, (mRadius / 8), 0, -(mRadius * 4 / 5), secondPaint);
        canvas.restore();
    }

    /**
     * 绘制分针 2015-2-4
     */
    private void drawMinuteHand(Canvas canvas)
    {
        canvas.save();
        int angle =(int)(time%864000)/8640*8640*360/864000; // 计算角度
        canvas.rotate(angle, 0f, 0f);
        canvas.drawLine(0, (mRadius / 9), 0, -(mRadius), diverderPaint);
        canvas.restore();
    }

    /**
     * 绘制时针 2015-2-4
     */
    private void drawHourHand(Canvas canvas)
    {
        canvas.save();
        int angle =(int) (time*360/86400000); // 计算角度
        canvas.rotate(angle, 0f, 0f);
        canvas.drawLine(0, (mRadius / 10), 0, -(mRadius * 4 / 5), hourPaint);
        canvas.restore();
    }

    class SecendsTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            if (sp != null)
            {
                sp.play(soundId, 0.2f, 0.2f, 0, 0, 1);
            }
            try
            {
                Thread.sleep(500);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            postInvalidate();


        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
//        canvas.drawColor(Color.BLACK);
        Long start=System.currentTimeMillis();
        drawCompass(canvas);

//        invalidate();
        LogUtil.i("draw duration : "+ (System.currentTimeMillis()-start));
    }
}
