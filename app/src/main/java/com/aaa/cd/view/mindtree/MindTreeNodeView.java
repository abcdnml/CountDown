package com.aaa.cd.view.mindtree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.annotation.Nullable;



public class MindTreeNodeView extends EditText {
    private static final String TAG = MindTreeNodeView.class.getSimpleName();

    private static final int NODE_WIDTH = 160;
    private static final int NODE_HEIGHT = 40;
    private static final int NODE_BORDER_COLOR = Color.BLACK;
    private static final int NODE_BORDER_WIDTH = 4;
    private Paint borderPaint;
    private MindTreeNode mindTree;
    private TreeNodeStyle treeNodeStyle;
    RectF rectF;
    private float density;

    public MindTreeNodeView(Context context) {
        super(context);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        density = dm.density;
        int dpi = dm.densityDpi;
        setGravity(Gravity.CENTER);
        setBackground(null);
    }

    public MindTreeNodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        rectF = new RectF();

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(NODE_BORDER_WIDTH);
        borderPaint.setColor(NODE_BORDER_COLOR);


        setBackgroundColor(Color.BLUE);
    }

    public void setNode(MindTreeNode mindTree) {
        this.mindTree = mindTree;
        this.mindTree.setView(this);
        setText(mindTree.getText());
    }

    public MindTreeNode getNode() {
        return mindTree;
    }

    public void setNodeStyle(TreeNodeStyle style) {
        if (style == null) {
            treeNodeStyle = TreeNodeStyle.DEFAULT;
            return;
        }
        treeNodeStyle = style;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw" + getText());
        canvas.drawColor(Color.GREEN);
        super.onDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean result = super.dispatchTouchEvent(event);
        Log.i(TAG, "child dispatchTouchEvent action: " + event.getAction() + " result: " + result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        Log.i(TAG, "child onTouchEvent action: " + event.getAction() + " result: " + result);
        return result;
    }


    public void reflectSetTextSize(float size) {
/*        try {
            Class cls= TextView.class;
            Method  method=cls.getMethod("setTextSizeInternal" , int.class,float.class,boolean.class);
            method.setAccessible(true);
            method.invoke(this, TypedValue.COMPLEX_UNIT_SP, size,false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }*/
    }

}
