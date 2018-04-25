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

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.aaa.cd.R;
import com.aaa.cd.util.Constants;
import com.aaa.cd.util.LogUtil;
import com.aaa.cd.util.SystemUtils;
import com.aaa.cd.view.TabIconView;

import java.io.File;


/**
 * markdown编辑界面
 * Created by aaa on 2018/01/13.
 */
public class MarkdownEditorFragment extends Fragment implements View.OnClickListener
{
    private final int SYSTEM_GALLERY = 1;
    protected EditText tv_title;
    protected EditText tv_content;

    private PerformEditable mPerformEditable;
    private PerformEdit mPerformEdit;
    private PerformEdit mPerformNameEdit;
    private TabIconView mTabIconView;
    private MarkdownActivity.TextChangeListener textChangeListener;
    private String title;
    private String content;

    public MarkdownEditorFragment()
    {
    }

    public static MarkdownEditorFragment getInstance(String title,String content)
    {
        MarkdownEditorFragment editorFragment = new MarkdownEditorFragment();
        editorFragment.title=title;
        editorFragment.content=content;
        return editorFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_editor, container, false);
        tv_title = (EditText) view.findViewById(R.id.tv_markdown_title);
        tv_title.setText(title);
        tv_content = (EditText) view.findViewById(R.id.tv_markdown_content);
        tv_content.setText(content);
        initPerformEdit();
        initTab(view);
        return view;
    }


    public void initPerformEdit()
    {

        mPerformEditable = new PerformEditable(tv_content);

        //撤销和恢复初始化
        mPerformNameEdit = new PerformEdit(tv_title)
        {
            @Override
            protected void onTextChanged(Editable s)
            {
                //标题改变
                LogUtil.d("title text changed");
                if (textChangeListener != null)
                {
                    textChangeListener.onTitleChange(s);
                }
            }
        };
        mPerformEdit = new PerformEdit(tv_content)
        {
            @Override
            protected void onTextChanged(Editable s)
            {
                //内容改变
                LogUtil.d("content text changed");
                if (textChangeListener != null)
                {
                    textChangeListener.onContentChange(s);
                }
            }
        };
    }

    public void setTextChangeListener(MarkdownActivity.TextChangeListener listener)
    {
        textChangeListener = listener;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

    }

    private void shareMenu()
    {


        switch ((int) 0)
        {
            case 0://复制
                shareCopyText();
                break;
            case 1://分享文本
                shareText();
                break;
            case 2://分享md文件
                shareMD();
                break;
        }

    }

    private void shareCopyText()
    {
        SystemUtils.copyToClipBoard(getActivity(), tv_content.getText().toString());
    }

    private void shareText()
    {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, tv_content.getText().toString());
        shareIntent.setType("text/plain");


    }

    private void shareMD()
    {

//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mPresenter.getMDFile()));
//        shareIntent.setType("*/*");

//        startActivity(Intent.createChooser(share,"Share Image"));
      /*  BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
        builder.setIntent(getActivity(), shareIntent);
        BottomSheet bottomSheet = builder.create();
        bottomSheet.show();*/
    }


    public PerformEdit getPerformEdit()
    {
        return mPerformEdit;
    }

    private void initTab(View view)
    {
        mTabIconView = (TabIconView) view.findViewById(R.id.tabIconView);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_list_bulleted, R.id.id_shortcut_list_bulleted, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_list_numbers, R.id.id_shortcut_format_numbers, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_insert_link, R.id.id_shortcut_insert_link, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_insert_photo, R.id.id_shortcut_insert_photo, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_console, R.id.id_shortcut_console, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_bold, R.id.id_shortcut_format_bold, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_italic, R.id.id_shortcut_format_italic, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_header_1, R.id.id_shortcut_format_header_1, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_header_2, R.id.id_shortcut_format_header_2, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_header_3, R.id.id_shortcut_format_header_3, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_quote, R.id.id_shortcut_format_quote, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_xml, R.id.id_shortcut_xml, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_minus, R.id.id_shortcut_minus, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_strikethrough, R.id.id_shortcut_format_strikethrough, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_grid, R.id.id_shortcut_grid, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_header_4, R.id.id_shortcut_format_header_4, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_header_5, R.id.id_shortcut_format_header_5, this);
        mTabIconView.addTab(R.mipmap.ic_shortcut_format_header_6, R.id.id_shortcut_format_header_6, this);
    }

    @Override
    public void onClick(View v)
    {
        if (R.id.id_shortcut_insert_photo == v.getId())
        {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);// Pick an item fromthe
            intent.setType("image/*");// 从所有图片中进行选择
            startActivityForResult(intent, SYSTEM_GALLERY);
            return;
        } else if (R.id.id_shortcut_insert_link == v.getId())
        {
            //插入链接
            insertLink();
            return;
        } else if (R.id.id_shortcut_grid == v.getId())
        {
            //插入表格
            insertTable();
            return;
        } else
        {
            //点击事件分发
            mPerformEditable.onClick(v);
        }
    }

    /**
     * 插入表格
     */
    private void insertTable()
    {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_input_table_view, null);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("插入表格")
                .setView(rootView)
                .show();

        final TextInputLayout rowNumberHint = (TextInputLayout) rootView.findViewById(R.id.rowNumberHint);
        final TextInputLayout columnNumberHint = (TextInputLayout) rootView.findViewById(R.id.columnNumberHint);
        final EditText rowNumber = (EditText) rootView.findViewById(R.id.rowNumber);
        final EditText columnNumber = (EditText) rootView.findViewById(R.id.columnNumber);


        rootView.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String rowNumberStr = rowNumber.getText().toString().trim();
                String columnNumberStr = columnNumber.getText().toString().trim();

                if (TextUtils.isEmpty(rowNumberStr))
                {
                    rowNumberHint.setError("不能为空");
                    return;
                }
                if (TextUtils.isEmpty(columnNumberStr))
                {
                    columnNumberHint.setError("不能为空");
                    return;
                }


                if (rowNumberHint.isErrorEnabled())
                    rowNumberHint.setErrorEnabled(false);
                if (columnNumberHint.isErrorEnabled())
                    columnNumberHint.setErrorEnabled(false);

                mPerformEditable.perform(R.id.id_shortcut_grid, Integer.parseInt(rowNumberStr), Integer.parseInt(columnNumberStr));
                dialog.dismiss();
            }
        });

        rootView.findViewById(R.id.cancel)
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });

        dialog.show();
    }

    /**
     * 插入链接
     */
    private void insertLink()
    {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_input_link_view, null);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.DialogTheme)
                .setTitle("插入链接")
                .setView(rootView)
                .show();

        final TextInputLayout titleHint = (TextInputLayout) rootView.findViewById(R.id.inputNameHint);
        final TextInputLayout linkHint = (TextInputLayout) rootView.findViewById(R.id.inputHint);
        final EditText title = (EditText) rootView.findViewById(R.id.name);
        final EditText link = (EditText) rootView.findViewById(R.id.text);


        rootView.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String titleStr = title.getText().toString().trim();
                String linkStr = link.getText().toString().trim();

                if (TextUtils.isEmpty(titleStr))
                {
                    titleHint.setError("不能为空");
                    return;
                }
                if (TextUtils.isEmpty(linkStr))
                {
                    linkHint.setError("不能为空");
                    return;
                }

                if (titleHint.isErrorEnabled())
                    titleHint.setErrorEnabled(false);
                if (linkHint.isErrorEnabled())
                    linkHint.setErrorEnabled(false);

                mPerformEditable.perform(R.id.id_shortcut_insert_link, titleStr, linkStr);
                dialog.dismiss();
            }
        });

        rootView.findViewById(R.id.cancel)
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });

        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK && requestCode == SYSTEM_GALLERY)
        {
            Uri uri = data.getData();
            String[] pojo = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().managedQuery(uri, pojo, null, null, null);
            if (cursor != null)
            {
//                    ContentResolver cr = this.getContentResolver();
                int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(colunm_index);
                //以上代码获取图片路径
                Uri.fromFile(new File(path));//Uri.decode(imageUri.toString())
                mPerformEditable.perform(R.id.id_shortcut_insert_photo, Uri.fromFile(new File(path)));
            } else
            {
                Toast.makeText(getActivity(), "图片处理失败", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
