package com.aaa.cd.view.mindtree;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;

import com.aaa.cd.R;

public class MindTreeNodeView extends View {
    private static final int NODE_WIDTH = 160;
    private static final int NODE_HEIGHT = 40;
    private static final int NODE_BORDER_COLOR = Color.BLACK;
    private static final int NODE_BORDER_WIDTH = 4;
    private static final boolean NODE_BORDER_ROUND = false;
    private static final int NODE_BG_COLOR = Color.WHITE;
    private static final int NODE_TEXT_COLOR = Color.BLACK;
    private Paint borderPaint;
    private MindTreeNode mindTree;
    private TreeNodeStyle treeNodeStyle;
    private int orientation = 0;
    RectF rectF;

    public MindTreeNodeView(Context context) {
        super(context);
    }

    public MindTreeNodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        rectF = new RectF();

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(NODE_BORDER_WIDTH);
        borderPaint.setColor(NODE_BORDER_COLOR);


        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        float density = dm.density;
        int dpi = dm.densityDpi;
    }

    public void setNode(MindTreeNode mindTree) {
        this.mindTree = mindTree;
        this.mindTree.setView(this);
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
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        scrollTo(-width / 2, -height / 2);
        if (mindTree == null) {
            return;
        }
        rectF.set(-NODE_WIDTH / 2, -NODE_HEIGHT / 2, NODE_WIDTH / 2, NODE_HEIGHT / 2);
        canvas.drawRect(rectF, borderPaint);
    }

    private void drawNodeTree(Canvas canvas, MindTreeNode mindTree) {


        //画子树
        if (mindTree.getSubNode() != null) {
            for (MindTreeNode subNode : mindTree.getSubNode()) {
                drawNodeTree(canvas, subNode);
            }
        }

    }

    //绘制当前节点内容
    private void drawNode(Canvas canvas, MindTreeNode node) {
        //计算节点大小， 有最大长度和最小长度
        canvas.drawRect();

    }
}
