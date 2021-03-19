package com.aaa.cd.view.mindtree;

import android.view.View;

import java.util.List;

/**
 * {
 * <p>
 * "text":"aaa",
 * "desc":"aaa",
 * "id":0,
 * "subNode":[
 * <p>
 * ]
 * }
 */
public class MindTreeNode {
    private int nodeId;
    private String text;
    private String desc;
    private View view;
    private List<MindTreeNode> subNode;

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<MindTreeNode> getSubNode() {
        return subNode;
    }

    public void setSubNode(List<MindTreeNode> subNode) {
        this.subNode = subNode;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
