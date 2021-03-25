package com.aaa.cd.ui.mindmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.aaa.cd.R;
import com.aaa.cd.view.mindtree.MindTreeLayout;

public class MindMapActivity extends AppCompatActivity {

    MindTreeLayout mtl_mind;

    String test="{\n" +
            "\t\"text\": \"aaa\",\n" +
            "\t\"desc\": \"aaa\",\n" +
            "\t\"id\": 0,\n" +
            "\t\"subNode\": [{\n" +
            "\t\t\"text\": \"111\",\n" +
            "\t\t\"desc\": \"111\",\n" +
            "\t\t\"id\": 0,\n" +
            "\t\t\"subNode\": [{\n" +
            "\t\t\t\"text\": \"11111111111111111111111111111111111111111111111111111111\",\n" +
            "\t\t\t\"desc\": \"11111111111111111111111111111111111111111111111111111111\",\n" +
            "\t\t\t\"id\": 0,\n" +
            "\t\t\t\"subNode\": []\n" +
            "\t\t}]\n" +
            "\t}, {\n" +
            "\t\t\"text\": \"222\",\n" +
            "\t\t\"desc\": \"222\",\n" +
            "\t\t\"id\": 0,\n" +
            "\t\t\"subNode\": [{\n" +
            "\t\t\t\"text\": \"2221\",\n" +
            "\t\t\t\"desc\": \"2221\",\n" +
            "\t\t\t\"id\": 0,\n" +
            "\t\t\t\"subNode\": []\n" +
            "\t\t}, {\n" +
            "\t\t\t\"text\": \"2222\",\n" +
            "\t\t\t\"desc\": \"2222\",\n" +
            "\t\t\t\"id\": 0,\n" +
            "\t\t\t\"subNode\": []\n" +
            "\t\t}, {\n" +
            "\t\t\t\"text\": \"2223\",\n" +
            "\t\t\t\"desc\": \"2223\",\n" +
            "\t\t\t\"id\": 0,\n" +
            "\t\t\t\"subNode\": []\n" +
            "\t\t}, {\n" +
            "\t\t\t\"text\": \"2224\",\n" +
            "\t\t\t\"desc\": \"2224\",\n" +
            "\t\t\t\"id\": 0,\n" +
            "\t\t\t\"subNode\": []\n" +
            "\t\t}, {\n" +
            "\t\t\t\"text\": \"2225\",\n" +
            "\t\t\t\"desc\": \"2225\",\n" +
            "\t\t\t\"id\": 0,\n" +
            "\t\t\t\"subNode\": []\n" +
            "\t\t}, {\n" +
            "\t\t\t\"text\": \"2226\",\n" +
            "\t\t\t\"desc\": \"2226\",\n" +
            "\t\t\t\"id\": 0,\n" +
            "\t\t\t\"subNode\": []\n" +
            "\t\t}, {\n" +
            "\t\t\t\"text\": \"2227\",\n" +
            "\t\t\t\"desc\": \"2227\",\n" +
            "\t\t\t\"id\": 0,\n" +
            "\t\t\t\"subNode\": [{\n" +
            "\t\t\t\t\"text\": \"22271\",\n" +
            "\t\t\t\t\"desc\": \"22271\",\n" +
            "\t\t\t\t\"id\": 0,\n" +
            "\t\t\t\t\"subNode\": []\n" +
            "\t\t\t},{\n" +
            "\t\t\t\t\"text\": \"22272\",\n" +
            "\t\t\t\t\"desc\": \"22272\",\n" +
            "\t\t\t\t\"id\": 0,\n" +
            "\t\t\t\t\"subNode\": []\n" +
            "\t\t\t},{\n" +
            "\t\t\t\t\"text\": \"22273\",\n" +
            "\t\t\t\t\"desc\": \"22273\",\n" +
            "\t\t\t\t\"id\": 0,\n" +
            "\t\t\t\t\"subNode\": []\n" +
            "\t\t\t}]\n" +
            "\t\t}, {\n" +
            "\t\t\t\"text\": \"2228\",\n" +
            "\t\t\t\"desc\": \"2228\",\n" +
            "\t\t\t\"id\": 0,\n" +
            "\t\t\t\"subNode\": []\n" +
            "\t\t}]\n" +
            "\t}, {\n" +
            "\t\t\"text\": \"333\",\n" +
            "\t\t\"desc\": \"333\",\n" +
            "\t\t\"id\": 0,\n" +
            "\t\t\"subNode\": [{\n" +
            "\t\t\t\"text\": \"3333\",\n" +
            "\t\t\t\"desc\": \"3333\",\n" +
            "\t\t\t\"id\": 0,\n" +
            "\t\t\t\"subNode\": [{\n" +
            "\t\t\t\t\"text\": \"33333\",\n" +
            "\t\t\t\t\"desc\": \"33333\",\n" +
            "\t\t\t\t\"id\": 0,\n" +
            "\t\t\t\t\"subNode\": [{\n" +
            "\t\t\t\t\t\"text\": \"333333\",\n" +
            "\t\t\t\t\t\"desc\": \"333333\",\n" +
            "\t\t\t\t\t\"id\": 0,\n" +
            "\t\t\t\t\t\"subNode\": []\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t}]\n" +
            "\t\t}]\n" +
            "\t}]\n" +
            "}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mind_map);
        mtl_mind=findViewById(R.id.mtl_mind);
        mtl_mind.setTree(test);
    }

    public void scroll(View view) {
        mtl_mind.scrollTo(0,-400);
    }
}
