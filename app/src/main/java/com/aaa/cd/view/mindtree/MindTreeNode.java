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
    private transient MindTreeNodeView view;
    private int weight; //用于存储树广度的weight 通过比较 所有子节点 和 本身的高度获得
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

    public MindTreeNodeView getView() {
        return view;
    }

    public void setView(MindTreeNodeView view) {
        this.view = view;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
