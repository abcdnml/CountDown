package com.aaa.cd.view.mindtree;

import java.util.List;

/**
 *
{

    "text":"aaa",
    "desc":"aaa",
    "id":0,
    "subNode":[

    ]
}
 */
public class MindTreeNode {
    private int nodeId;
    private String text;
    private String desc;
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
}
