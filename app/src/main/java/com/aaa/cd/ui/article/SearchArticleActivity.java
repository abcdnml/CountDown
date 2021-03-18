package com.aaa.cd.ui.article;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.dao.DocumentDao;
import com.aaa.cd.po.Catalogue;
import com.aaa.cd.util.Constants;
import com.aaa.cd.util.CountDownApplication;
import com.aaa.cd.util.EasyTransition;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration;

import java.util.List;

public class SearchArticleActivity extends Activity
{
    EditText et_search;
    private RecyclerView rv_searchResult;
    private CatalogueAdapter catalogueAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_search_article);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#7777FF"));

        initTitle();
        et_search = (EditText) findViewById(R.id.search_article);
        Drawable moveDrawable = ContextCompat.getDrawable(this, R.mipmap.search);
        moveDrawable.setBounds(0, 0, 64, 64);
        et_search.setCompoundDrawables(moveDrawable, null, null, null);
        et_search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                List<Catalogue> searchResult=DocumentDao.getFileByContent(et_search.getText().toString(), CountDownApplication.getApplication().getUser().getId());
                catalogueAdapter.updateList(searchResult);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
        rv_searchResult = (RecyclerView) findViewById(R.id.rv_article_catalogue);
        catalogueAdapter=new CatalogueAdapter(this,DisplayMode.MODE_DETAIL,null);
        rv_searchResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_searchResult.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color)));
        catalogueAdapter.setOnItemClickLitsener(catalogueItemListener);
        rv_searchResult.setAdapter(catalogueAdapter);
        rv_searchResult.requestFocus();



        EasyTransition.enter(this, 500, new DecelerateInterpolator(), new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                et_search.requestFocus();
            }
        });
    }

    CatalogueAdapter.ItemClickListener catalogueItemListener = new CatalogueAdapter.ItemClickListener()
    {
        @Override
        public void onItemClick(Catalogue catalogue)
        {
            Intent intent=new Intent();
            intent.putExtra(Constants.INTENT_KEY_ARTICLE_ID,catalogue.getId());
            intent.putExtra(Constants.INTENT_KEY_ARTICLE_PARENT,catalogue.getParent());
            intent.putExtra(Constants.INTENT_KEY_SEARCH_ARTICLE_STRING,et_search.getText().toString());
            setResult(RESULT_OK,intent);
            EasyTransition.exit(SearchArticleActivity.this, 800, new DecelerateInterpolator());

        }

        @Override
        public void onItemLongClick(Catalogue catalogue)
        {
        }
    };

    @TargetApi(19)
    private void setTranslucentStatus(boolean on)
    {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on)
        {
            winParams.flags |= bits;
        } else
        {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void initTitle()
    {
        TextView tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText(R.string.search_article);
        ImageView iv_left = (ImageView) findViewById(R.id.iv_title_left);
        iv_left.setVisibility(View.VISIBLE);
        iv_left.setImageResource(R.mipmap.back);
        iv_left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_CANCELED);
        EasyTransition.exit(this, 800, new DecelerateInterpolator());
    }
}
