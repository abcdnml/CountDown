package com.aaa.cd.view.mindtree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.EditText;

import androidx.annotation.Nullable;


public class MindTreeNodeView extends EditText {
    private static final int NODE_WIDTH = 160;
    private static final int NODE_HEIGHT = 40;
    private static final int NODE_BORDER_COLOR = Color.BLACK;
    private static final int NODE_BORDER_WIDTH = 4;
    private Paint borderPaint;
    private MindTreeNode mindTree;
    private TreeNodeStyle treeNodeStyle;
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
        super.onDraw(canvas);

        canvas.drawColor(Color.argb(0, mindTree.getWeight() % 256,mindTree.getWeight() % 256,mindTree.getWeight() % 256));
    }
}
