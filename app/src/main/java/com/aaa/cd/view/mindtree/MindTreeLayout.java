package com.aaa.cd.view.mindtree;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.List;

public class MindTreeLayout extends ViewGroup {

    private MindTreeNode mMindTree;
    private float density;
    private Direction direction;
    private float verticalInterval;
    private float horizontalInterval;

    public MindTreeLayout(Context context) {
        super(context);
    }

    public MindTreeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        direction = Direction.RIGHT;
        verticalInterval = density * 20;
        horizontalInterval = density * 20;

    }

    public MindTreeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MindTreeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    private void initScreenInfo(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        density = dm.density;
        int dpi = dm.densityDpi;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mMindTree == null) {
            return;
        }
        MindTreeNodeView view = (MindTreeNodeView) mMindTree.getView();
        int w = view.getMeasuredWidth();
        int h = view.getMeasuredHeight();
        view.layout(0 - w / 2, 0 - h / 2, 0 + w / 2, 0 + h / 2);

        List<MindTreeNode> subNode = mMindTree.getSubNode();
        if (subNode == null) {
            return;
        }

        int subNodeLeft=

        for (int i = 0; i < subNode.size(); i++) {

        }
    }

    public void layoutChild(MindTreeNode node, int l, int t, int r, int b) {
        if (node == null) {
            return;
        }
        View view = node.getView();
        int w = view.getMeasuredWidth();
        int h = view.getMeasuredHeight();


        if (direction == Direction.LEFT) {

        } else if (direction == Direction.RIGHT) {
            int childLeft = (r + l) / 2 - w / 2;
            int childTop = (t + b) / 2 - h / 2;
            int childRight = (r + l) / 2 + w / 2;
            int childBottom = (t + b) / 2 + h / 2;
        } else if (direction == Direction.RIGHT) {

        } else if (direction == Direction.TOP) {

        } else if (direction == Direction.BOTTOM) {

        } else if (direction == Direction.ALL) {

        } else if (direction == Direction.LEFT_RIGHT) {

        } else if (direction == Direction.UP_DOWN) {

        }
        view.layout(childLeft, childTop, childRight, childBottom);

        if (mMindTree.getSubNode() == null || mMindTree.getSubNode().size() == 0) {
            //没有子控件
            return;
        }
        for (int i = 0; i < mMindTree.getSubNode().size(); i++) {
            MindTreeNode subNode = mMindTree.getSubNode().get(i);
            layoutChild(subNode, );
        }
    }

    public Rect getNodeRect() {
        if (direction == Direction.LEFT) {

        } else if (direction == Direction.RIGHT) {
            int childLeft = (r + l) / 2 - w / 2;
            int childTop = (t + b) / 2 - h / 2;
            int childRight = (r + l) / 2 + w / 2;
            int childBottom = (t + b) / 2 + h / 2;
        } else if (direction == Direction.RIGHT) {

        } else if (direction == Direction.TOP) {

        } else if (direction == Direction.BOTTOM) {

        } else if (direction == Direction.ALL) {

        } else if (direction == Direction.LEFT_RIGHT) {

        } else if (direction == Direction.UP_DOWN) {

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
    }

    private void calculatHeight(MindTreeNode mindTreeNode){
        int max=mindTreeNode.getView().getMeasuredHeight();
        List<MindTreeNode> subNode=mindTreeNode.getSubNode();
        for(int i=0;i<subNode.size();i++){

        }

    }

    enum Direction{
        LEFT(0),RIGHT(1),TOP(2),BOTTOM(3),ALL(4),LEFT_RIGHT(5),UP_DOWN(6);
        private int value;
        private Direction(int value){
            this.value=value;
        }
    }
}
