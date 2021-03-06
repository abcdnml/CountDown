package com.aaa.cd.view;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.aaa.cd.R;

public class ClockSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{

	private SoundPool sp;
	private AudioManager mAudioManager;
	private int soundId;

	private SurfaceHolder mHolder;

	private int mRadius;
	private int mHours;
	private int mMinutes;
	private int mSeconds;

	private Paint bgPaint;
	private Paint circlePaint;
	private Paint bigDiverderPaint;
	private Paint diverderPaint;
	private Paint textPaint;
	private Paint hourPaint;
	private Paint minutePaint;
	private Paint secondPaint;

	Timer timer;

	SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
	
	public ClockSurfaceView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.clock);// TypedArray是一个数组容器
		mRadius = types.getInteger(R.styleable.clock_radiu, 0);// 防止在XML文件里没有定义，就加上了默认值30
		types.recycle();
		if (mRadius == 0)
		{
			mRadius=200;
		}

        initPaint();
        initVoice(context);

		mHolder = this.getHolder();
		mHolder.addCallback(this);
	}
	public void initVoice(Context context){
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,8,0);
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
		bgPaint.setColor(Color.WHITE);
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
		textPaint.setTextSize(mRadius/5);
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

	/**
	 * 绘制罗盘 2015-2-3
	 */
	private void drawCompass(Canvas canvas)
	{
		canvas.drawRect(new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight()), bgPaint);
		canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2); // 平移罗盘
		// canvas.drawCircle(0, 0, mRadius, circlePaint); // 画圆圈


		if (mRadius <= 0)
		{
			mRadius = canvas.getWidth() / 2 - 100;
		}
		// 使用path绘制路径文字
		canvas.save();

		// drawLabel(canvas);
		// canvas.restore();

		drawDividing(canvas);

		Calendar cal = Calendar.getInstance();
		mHours = cal.get(Calendar.HOUR_OF_DAY);
		mMinutes = cal.get(Calendar.MINUTE);
		mSeconds = cal.get(Calendar.SECOND);

		drawHourHand(canvas);
		drawMinuteHand(canvas);
		drawSecondHand(canvas);

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
	    canvas.drawText(sdf.format(new Date(System.currentTimeMillis())), 0,  mRadius/2, textPaint);
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
		int count = 60; // 总刻度数
		canvas.rotate(30, 0f, 0f); // 旋转画纸
		for (int i = 0; i < count; i++)
		{
			if (i % 5 == 0)
			{
				if((i/5+1)>9){
					canvas.drawText(String.valueOf(i / 5 + 1), 0, -y + 15, textPaint);
				}else{
					canvas.drawText(String.valueOf(i / 5 + 1), 0, -y + 15, textPaint);
				}
				
				// canvas.drawLine(0f, -y, 0, -y + 20f, mPaint);
			} else
			{
				canvas.drawLine(0f, -y, 0f, -y + 15f, diverderPaint);
			}

			canvas.rotate(6, 0f, 0f); // 旋转画纸
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
		int angle = mSeconds * 6; // 计算角度
		Log.i("aaaaaaaaa", "aaaaaaaaaaaa mSeconds angle  " + angle);
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
		int angle = mMinutes * 6 + mSeconds / 10; // 计算角度
		canvas.rotate(angle, 0f, 0f);
		canvas.drawLine(0, (mRadius / 9), 0, -(mRadius * 2 / 3), minutePaint);
		canvas.restore();
	}

	/**
	 * 绘制时针 2015-2-4
	 */
	private void drawHourHand(Canvas canvas)
	{
		canvas.save();
		int angle = ((mHours % 12) * 30) + (mMinutes/2); // 计算角度
		canvas.rotate(angle, 0f, 0f);
		canvas.drawLine(0, (mRadius / 10), 0, -(mRadius* 2/ 5), hourPaint);
		canvas.restore();
	}

	class SecendsTimerTask extends TimerTask{
		SurfaceHolder  currentHolder;
		public SecendsTimerTask(SurfaceHolder holder)
		{
			currentHolder=holder;
		}

		@Override
		public void run() {
			Canvas canvas = null;
			synchronized (currentHolder)
			{

				canvas = currentHolder.lockCanvas(null);
				if(canvas!=null)
				{
					canvas.drawColor(Color.BLACK);
					drawCompass(canvas);
				}
				currentHolder.unlockCanvasAndPost(canvas); // 解锁画布，提交画好的图像
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(sp!=null){
					sp.play(soundId, 0.2f, 0.2f, 0, 0, 1);
				}

			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		timer=new Timer();
		timer.scheduleAtFixedRate(new SecendsTimerTask(holder),0,1000);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		mHolder = holder;
		mHolder.addCallback(this);
		timer.cancel();
		timer=new Timer();
		timer.scheduleAtFixedRate(new SecendsTimerTask(mHolder),0,1000);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		timer.cancel();
		timer=null;
	}
}
