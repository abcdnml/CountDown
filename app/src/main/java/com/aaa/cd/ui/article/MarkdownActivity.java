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

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.dao.DocumentDao;
import com.aaa.cd.po.Catalogue;
import com.aaa.cd.util.Constants;
import com.aaa.cd.util.LogUtil;
import com.aaa.cd.util.SystemUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;


public class MarkdownActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String TAG = "MarkdownActivity";
    public static final int ID_NOT_EXIST = -1;

    private MarkdownEditorFragment editFragment;
    private MarkdownPreviewFragment previewFragment;


    private String title;
    private String content;

    protected ViewPager mViewPager;
    private Catalogue catalogue;

    private ImageView iv_back, iv_undo, iv_redo, iv_save;
    private TextView tv_title;
    private boolean isSave = true;

    Drawable drawableSave, drawableUnsave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setTintColor(Color.parseColor("#7777FF"));

        Intent intent=getIntent();
        int id = intent.getIntExtra(Constants.INTENT_KEY_ARTICLE_ID, ID_NOT_EXIST);
        Log.i(TAG, "catalog id : " + id);
        if (id == ID_NOT_EXIST)
        {
            int parent=intent.getIntExtra(Constants.INTENT_KEY_ARTICLE_PARENT,-1);
            int user=intent.getIntExtra(Constants.INTENT_KEY_ARTICLE_USER,-1);
            catalogue = new Catalogue();
            catalogue.setParent(parent);
            catalogue.setUserId(user);
            catalogue.setType(Catalogue.FILE);
        } else
        {
            catalogue = DocumentDao.getFileById(id);
            title = catalogue.getName();
            content = catalogue.getContent();
            Log.i(TAG, "catalog title : " + title + "  content : " + content);
        }


        editFragment = MarkdownEditorFragment.getInstance(catalogue.getName(), catalogue.getContent());
        editFragment.setTextChangeListener(textChangeListener);
        previewFragment = MarkdownPreviewFragment.getInstance(catalogue.getName(), catalogue.getContent());

        initTitle();
        initViewPager();

    }

    public void initTitle()
    {
        drawableSave = getResources().getDrawable(R.mipmap.ic_action_save, null);
        drawableUnsave = getResources().getDrawable(R.mipmap.ic_action_unsave, null);
        tv_title = (TextView) findViewById(R.id.tv_markdown_mode);
        iv_back = (ImageView) findViewById(R.id.iv_markdown_title_back);
        iv_back.setOnClickListener(this);
        iv_undo = (ImageView) findViewById(R.id.iv_markdown_title_undo);
        iv_undo.setOnClickListener(this);
        iv_redo = (ImageView) findViewById(R.id.iv_markdown_title_redo);
        iv_redo.setOnClickListener(this);
        iv_save = (ImageView) findViewById(R.id.iv_markdown_title_save);
        iv_save.setOnClickListener(this);
    }

    private void initViewPager()
    {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new EditFragmentAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            @Override
            public void onPageSelected(int position)
            {
                LogUtil.d("onPageSelected position: " + position);

                if (position == 0)
                {
                    tv_title.setText(R.string.markdown_edit);
                    showEditOption();
                } else
                {
                    tv_title.setText(R.string.markdown_preview);
                    hideEditOption();
                    previewFragment.refreshContent(title,content);
                    SystemUtils.closeSoftkeybord();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });
    }

    TextChangeListener textChangeListener = new TextChangeListener()
    {
        @Override
        public void onContentChange(Editable editable)
        {
            if (isSave)
            {
                iv_save.setImageDrawable(drawableUnsave);
                isSave = false;
            }
            content = editable.toString();
        }

        @Override
        public void onTitleChange(Editable editable)
        {
            if (isSave)
            {
                iv_save.setImageDrawable(drawableUnsave);
                isSave = false;
            }
            title = editable.toString();
        }
    };


    @Override
    public void onClick(View v)
    {

        if (R.id.iv_markdown_title_back == v.getId())
        {
            //返回
            onBackPressed();
            return;
        } else if (R.id.iv_markdown_title_redo == v.getId())
        {
            //重做
            editFragment.getPerformEdit().redo();
            return;
        } else if (R.id.iv_markdown_title_save == v.getId())
        {
            //保存
            LogUtil.i("iv_markdown_title_save isSave : " + isSave);
            if (!isSave)
            {
                iv_save.setImageDrawable(drawableSave);
                save();
                isSave = true;
            }
            return;
        } else if (R.id.iv_markdown_title_undo == v.getId())
        {
            //撤销
            editFragment.getPerformEdit().undo();
            return;
        }

    }

    private void showSaveDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setMessage(getString(R.string.dialog_need_save));
        builder.setNegativeButton(getString(R.string.dialog_unsave), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        }).setNeutralButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        }).setPositiveButton(getString(R.string.dialog_save), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                save();
                finish();
            }
        }).show();

    }

    private class EditFragmentAdapter extends FragmentPagerAdapter
    {

        public EditFragmentAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            if (position == 0)
            {
                return editFragment;
            } else
            {
                return previewFragment;
            }
        }

        @Override
        public int getCount()
        {
            return 2;
        }
    }

    @Override
    public void onBackPressed()
    {
        SystemUtils.closeSoftkeybord();
        if (!isSave)
        {
            showSaveDialog();
        } else
        {
            setResult(RESULT_OK);
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        editFragment.onActivityResult(requestCode, resultCode, data);
    }


    public void save()
    {
        if (catalogue.getId() == ID_NOT_EXIST)
        {
            LogUtil.i("create file "+ catalogue);
            catalogue.setContent(content);
            catalogue.setName(title);
            DocumentDao.createFile(catalogue);
        } else
        {
            catalogue.setName(title);
            catalogue.setContent(content);
            DocumentDao.modifyFile(catalogue);
        }
    }

    public void showEditOption()
    {
        iv_redo.setVisibility(View.VISIBLE);
        iv_save.setVisibility(View.VISIBLE);
        iv_undo.setVisibility(View.VISIBLE);
    }

    public void hideEditOption()
    {
        iv_redo.setVisibility(View.GONE);
        iv_save.setVisibility(View.GONE);
        iv_undo.setVisibility(View.GONE);
    }

    interface TextChangeListener
    {
        void onContentChange(Editable editable);

        void onTitleChange(Editable editable);
    }

}
