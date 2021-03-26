package com.aaa.cd.view.mindtree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.aaa.cd.ui.mindmap.TouchHandler;
import com.google.gson.Gson;

import java.util.List;

public class MindTreeLayout extends ViewGroup {
    private static final String TAG = MindTreeLayout.class.getSimpleName();
    private MindTreeNode mMindTree;
    private float density;
    private Direction direction;
    private static int maxDeep;    //存储最大树深度
    private TouchHandler touchHandler;
    private float max_zoom = 2f;
    private float min_zoom = 0.25f;
    private float[] mMatrixValue;
    private Matrix mMatrix;


    public MindTreeLayout(Context context) {
        super(context);
    }

    public MindTreeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMatrix = new Matrix();
        mMatrixValue = new float[9];
        touchHandler = new TouchHandler(context, this);
        initScreenInfo();
    }

    public MindTreeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MindTreeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initScreenInfo() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        density = dm.density;
        int dpi = dm.densityDpi;
    }


    public void setTranslateBy(float transX, float transY) {
        mMatrix.postTranslate(transX, transY);
        requestLayout();
    }

    /**
     * 缩放
     *
     * @param scale   缩放比例
     * @param centerX 缩放中心点x
     * @param centerY 缩放中心点y
     */
    public void setScaleBy(float scale, float centerX, float centerY) {
        float currentZoom = getCurrentZoom();
        float targetZoom = currentZoom * scale;
        if (targetZoom > max_zoom) {
            scale = max_zoom / currentZoom;
        } else if (targetZoom < min_zoom) {
            scale = min_zoom / currentZoom;
        }
        mMatrix.postScale(scale, scale, centerX, centerY);
        requestLayout();
        Log.i(TAG, "scale : " + scale);

    }

    public float getCurrentZoom(){
        mMatrix.getValues(mMatrixValue);
        return mMatrixValue[Matrix.MSCALE_X];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasure ");
        int measureWidth = measureWidth(0, widthMeasureSpec);
        int measureHeight = measureHeight(0, heightMeasureSpec);
        // 计算自定义的ViewGroup中所有子控件的大小
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);

            int widthSpec = 0;
            int heightSpec = 0;
            LayoutParams params = v.getLayoutParams();
            if (params.width > 0) {
                widthSpec = MeasureSpec.makeMeasureSpec(params.width,
                        MeasureSpec.EXACTLY);
            } else if (params.width == -1) {
                widthSpec = MeasureSpec.makeMeasureSpec(measureWidth,
                        MeasureSpec.EXACTLY);
            } else if (params.width == -2) {
                widthSpec = MeasureSpec.makeMeasureSpec(measureWidth,
                        MeasureSpec.AT_MOST);
            }

            if (params.height > 0) {
                heightSpec = MeasureSpec.makeMeasureSpec(params.height,
                        MeasureSpec.EXACTLY);
            } else if (params.height == -1) {
                heightSpec = MeasureSpec.makeMeasureSpec(measureHeight,
                        MeasureSpec.EXACTLY);
            } else if (params.height == -2) {
                heightSpec = MeasureSpec.makeMeasureSpec(measureWidth,
                        MeasureSpec.AT_MOST);
            }
            v.measure(widthSpec, heightSpec);

        }
        // 设置自定义的控件MyLayout的大小
        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measureWidth(int size, int pWidthMeasureSpec) {
        int result = size;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int size, int pHeightMeasureSpec) {
        int result = size;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //1.计算出所有节点的总大小
        //2.根据总大小分配每一层的节点位置
        Log.i(TAG, "layoutChild container: " + " l: " + l + " t: " + t + " r: " + r + " b: " + b);
        if (mMindTree == null) {
            return;
        }
        //这里暂时获取向右的宽和高  实际上需要考虑方向不同
        int totalHeight = calculateNodeHeight(mMindTree);

        maxDeep = 0;
        calculateNodeWidth(mMindTree, 0);
        int totalWidth = maxDeep;
        Log.i(TAG, "layoutChild container: width " + maxDeep + " height: " + totalHeight);
        layoutChild(mMindTree, 0, 0, totalWidth, totalHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw ");
    }

/*    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.i(TAG, "dispatchDraw ");
        canvas.save();
        canvas.concat(mMatrix);
        super.dispatchDraw(canvas);
        canvas.restore();
    }*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG,"parent dispatchTouchEvent action: "+ event.getAction());
        boolean result=super.dispatchTouchEvent(event);
        Log.i(TAG,"parent dispatchTouchEvent result: "+result);
        return result;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"parent onTouchEvent action: "+ event.getAction() );
        boolean result=touchHandler.onTouchEvent(event);
        Log.i(TAG,"parent onTouchEvent result: "+ result );
        return result;
    }
    private RectF tempRect=new RectF();
    /**
     * @param node
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public void layoutChild(MindTreeNode node, int l, int t, int r, int b) {
        MindTreeNodeView view = node.getView();
        int w = view.getMeasuredWidth();
        int h = view.getMeasuredHeight();
        Log.i(TAG, "layoutChild : Measured width :" + w + " height: " + h);
        Log.i(TAG, "layoutChild : " + node.getText() + " l: " + l + " t: " + t + " r: " + r + " b: " + b);
        //TODO
        if (direction == Direction.RIGHT) {

        }
        int childLeft = l;
        int childTop = (t + b) / 2 - h / 2;
        int childRight = childLeft + w;
        int childBottom = childTop + h;
//        view.layout(childLeft, childTop, childRight, childBottom);

        tempRect.left=childLeft;
        tempRect.top=childTop;
        tempRect.right=childRight;
        tempRect.bottom=childBottom;

        mMatrix.mapRect(tempRect);
        view.layout((int)tempRect.left, (int)tempRect.top, (int)tempRect.right, (int)tempRect.bottom);
        mMatrix.getValues(mMatrixValue);
//        view.setScaleX(mMatrixValue[0]);
//        view.setScaleText(mMatrixValue[0]);
//        view.setScaleY(mMatrixValue[0]);


        Log.i(TAG, "layoutChild : " + node.getText() + " l: " + childLeft + " t: " + childTop + " r: " + childRight + " b: " + childBottom);

        List<MindTreeNode> subNodes = node.getSubNode();
        if (subNodes == null || subNodes.size() == 0) {
            //没有子控件
            return;
        }
        int tempChildTop = 0;
        for (int i = 0, size = subNodes.size(); i < size; i++) {
            MindTreeNode subNode = subNodes.get(i);
            Log.i(TAG, "layoutChild i : " + i + " : " + node.getText() + " l: " + childRight + " t: " + (t + tempChildTop) + " r: " + r + " b: " + (t + tempChildTop + subNode.getWeight()));
            layoutChild(subNode, childRight, t + tempChildTop, r, t + tempChildTop + subNode.getWeight());
            tempChildTop += subNode.getWeight();
        }
    }

    /**
     * 测量控件大小
     * 1. 判断树的扩展方向
     */
    public void calculateNodeSize(MindTreeNode node) {
        if (direction == Direction.RIGHT) {
//            calculateNodeHeight(node);
//            calculateNodeWidth(node);
        }
    }

    /**
     * 计算扩展方向的大小  即树的广度
     * 测量控树的广度
     * 1. 判断有无子节点
     * 有 -  比较 所有子节点相加 和 当前节点 的大小（有可能当前节点字特长。。。） 取大值
     * 无 -  当前节点大小
     *
     * @param node
     * @return
     */
    public static int calculateNodeHeight(MindTreeNode node) {
        MindTreeNodeView view = node.getView();
        if (view == null) {
            return 0;
        }

        int w = view.getMeasuredWidth();
        int h = view.getMeasuredHeight();


        List<MindTreeNode> subNodes = node.getSubNode();
        //没有子节点
        if (subNodes == null || subNodes.size() == 0) {
            node.setWeight(h);
            return h;
        }

        int sumSubNodeHeight = 0;
        for (int i = 0, l = subNodes.size(); i < l; i++) {
            Log.i(TAG, "calculateNodeHeight" + subNodes.get(i).getText());
            sumSubNodeHeight += calculateNodeHeight(subNodes.get(i));
        }

        if (sumSubNodeHeight > h) {
            node.setWeight(sumSubNodeHeight);
            return sumSubNodeHeight;
        } else {
            node.setWeight(h);
            return h;
        }
    }


    /**
     * 计算非扩展方向的大小 树的深度  应该放到Direction中 做静态方法
     * 测量控树的深度
     * 广度遍历 一直存最大值就行
     *
     * @param node
     * @return
     */
    public static void calculateNodeWidth(MindTreeNode node, int currentDeep) {
        MindTreeNodeView view = node.getView();
        if (view == null) {
            return;
        }

        int w = view.getMeasuredWidth();
        int h = view.getMeasuredHeight();

        List<MindTreeNode> subNodes = node.getSubNode();
        currentDeep += w;
        //没有子节点
        if (subNodes == null || subNodes.size() == 0) {
            if (currentDeep > maxDeep) {
                maxDeep = currentDeep;
            }
        }

        for (int i = 0, l = subNodes.size(); i < l; i++) {
            calculateNodeWidth(subNodes.get(i), currentDeep);
        }
    }

    /**
     * 设置脑树
     *
     * @param json 树的json
     */
    public void setTree(String json) {
        Gson gson = new Gson();
        MindTreeNode mindTree = gson.fromJson(json, MindTreeNode.class);
        mMindTree = mindTree;
        if (getChildCount() > 0) {
            removeAllViews();
        }
        addSubNodeView(mindTree);
        invalidate();
    }

    public void setTree(MindTreeNode treeNode) {
        mMindTree = treeNode;
        if (getChildCount() > 0) {
            removeAllViews();
        }
        addSubNodeView(treeNode);
    }

    /**
     * 根据树结构添加子控件
     *
     * @param mindTree
     */
    private void addSubNodeView(MindTreeNode mindTree) {
        if (mindTree == null) {
            return;
        }

        MindTreeNodeView mindTreeNodeView = new MindTreeNodeView(getContext());
        mindTreeNodeView.setNode(mindTree);
        this.addView(mindTreeNodeView);

        if (mindTree.getSubNode() != null && mindTree.getSubNode().size() > 0) {
            for (MindTreeNode subNode : mindTree.getSubNode()) {
                addSubNodeView(subNode);
            }
        }

        Log.i(TAG, "add sub node view:  " + getChildCount());
    }

    enum Direction {
        LEFT(0), RIGHT(1), TOP(2), BOTTOM(3), ALL(4), LEFT_RIGHT(5), UP_DOWN(6);
        private int value;

        private Direction(int value) {
            this.value = value;
        }
    }
}
