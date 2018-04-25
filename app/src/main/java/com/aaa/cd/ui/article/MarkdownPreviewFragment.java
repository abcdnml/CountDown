/*
 * Copyright 2016. SHENQINCI(沈钦赐)<dev@qinc.me>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aaa.cd.ui.article;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.util.LogUtil;

/**
 * 编辑预览界面
 * Created by 沈钦赐 on 16/1/21.
 */
public class MarkdownPreviewFragment extends Fragment
{
    private static final String TAG = "MarkdownPreviewFragment";
    protected MarkdownPreviewView mMarkdownPreviewView;
    protected TextView tv_title;
    String title;
    String content;

    public MarkdownPreviewFragment()
    {
    }

    public static MarkdownPreviewFragment getInstance(String title, String content)
    {
        MarkdownPreviewFragment editorFragment = new MarkdownPreviewFragment();
        editorFragment.title = title;
        editorFragment.content = content;
        return editorFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_markdown, container, false);

        mMarkdownPreviewView = (MarkdownPreviewView) view.findViewById(R.id.markdownView);
        tv_title = (TextView) view.findViewById(R.id.title);
        tv_title.setText(title);
        mMarkdownPreviewView.setOnLoadingFinishListener(new MarkdownPreviewView.OnLoadingFinishListener()
        {
            @Override
            public void onLoadingFinish()
            {
                Log.i(TAG, "onCreateView : onLoadingFinish");
                mMarkdownPreviewView.parseMarkdown(content, true);
            }
        });
        return view;
    }


    public void refreshContent(String title,String content)
    {
        this.title=title;
        this.content=content;
        tv_title.setText(title);
        mMarkdownPreviewView.parseMarkdown(content, true);
    }
}
