package com.aaa.util.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.aaa.cd.R;

/**
 * Created by aaa on 2016/9/17.
 */
public class CheckView extends View implements View.OnClickListener{

    OnCheckChangeListener checkChangeListener;
    boolean isChecked=false;
    Drawable checkedDrawable;
    Drawable unCheckDrawable;
    public CheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setClickable(true);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.CheckView);
        checkedDrawable =ta.getDrawable(R.styleable.CheckView_checkedSrc);
        unCheckDrawable= ta.getDrawable(R.styleable.CheckView_unCheckSrc);
        isChecked=ta.getBoolean(R.styleable.CheckView_isCheck,false);
        ta.recycle();

        setCheck(isChecked);
        setOnClickListener(this);
    }
    public void setCheck(boolean isChecked)
    {
        if(checkChangeListener!=null&&!checkChangeListener.onCheckChange(isChecked)){
            //如果设置Listener并且 onCheckChange内执行不成功 不改变改变图片

        }else{
            //如果没有设置Listener  或者onCheckChange内执行成功 改变改变图片
            this.isChecked=isChecked;
            updateImage();
        }
    }

    @Override
    public void onClick(View view) {
        if(checkChangeListener!=null&&!checkChangeListener.onCheckChange(!isChecked)){
            //如果设置Listener并且 onCheckChange内执行不成功 不改变改变图片

        }else{
            //如果没有设置Listener  或者onCheckChange内执行成功 改变改变图片
            isChecked=!isChecked;
            updateImage();
        }
    }


    public void updateImage()
    {
        if(isChecked){
            if(checkedDrawable!=null){
                setBackground(checkedDrawable);
            }
        }else
        {
            if(unCheckDrawable!=null){
                setBackground(unCheckDrawable);
            }
        }
    }


    public void setOnCheckChangeListener(OnCheckChangeListener listener){
        this.checkChangeListener=listener;
    }
    public static interface  OnCheckChangeListener{
        public boolean onCheckChange(boolean isCheck);
    }
}
