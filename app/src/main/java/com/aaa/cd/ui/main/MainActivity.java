package com.aaa.cd.ui.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.dao.UserDao;
import com.aaa.cd.model.MainCallback;
import com.aaa.cd.po.User;
import com.aaa.cd.util.Constants;
import com.aaa.cd.util.CountDownApplication;
import com.aaa.cd.util.SPUtil;
import com.example.youngkaaa.ycircleview.CircleView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class MainActivity extends AppCompatActivity implements MainCallback
{

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Fragment[] fragments = new Fragment[9];
    FragmentManager fm;
    SystemBarTintManager tintManager;
    User user;
    int currentFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            setTranslucentStatus(true);
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        user = getSavedUserId();
        CountDownApplication.getApplication().setUser(user);

        initView();
        setStatusBarColor(Color.parseColor("#7777FF"));
    }

    public User getSavedUserId()
    {
        int id = (int) SPUtil.get(Constants.SP_KEY_USER_ID, -1);
        if (id >= 0)
        {
            return UserDao.getUserInfoByUID(id);
        } else
        {
            //游客ID为0
            return UserDao.login("visitor", "visitor");
        }

    }

    public void setStatusBarColor(int color)
    {
        tintManager.setTintColor(color);
    }

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

    public void initView()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_menu);
        mNavigationView = (NavigationView) findViewById(R.id.nv_main_menu);
        View drawerHead = mNavigationView.inflateHeaderView(R.layout.menu_main_drawer_header);
        mNavigationView.inflateMenu(R.menu.menu_main_drawer_content);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem item)
            {
                item.setChecked(true);
                switchContent(item.getItemId());
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        CircleView cv_head = (CircleView) drawerHead.findViewById(R.id.menu_header_cv_icon);
        TextView tv_name = (TextView) drawerHead.findViewById(R.id.menu_header_tv_name);
        TextView tv_motto = (TextView) drawerHead.findViewById(R.id.menu_header_tv_motto);
        ImageView tv_setting = (ImageView) drawerHead.findViewById(R.id.menu_header_iv_setting);
        tv_setting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                startActivity(intent);
            }
        });
        if (user == null)
        {
            cv_head.setImgSrc(R.mipmap.bigwhite);
            tv_name.setText(R.string.sample_name);
            tv_motto.setText(R.string.sample_signature);
        } else
        {
            cv_head.setImgSrc(R.mipmap.bigwhite);
            tv_name.setText(user.getNickname());
            tv_motto.setText(user.getMotto());
        }

        fragments[0] = new MainCountDownFragment();
        fragments[1] = new MainCalendarFragment();
        fragments[2] = new MainArticleFragment();
        fragments[3] = new MainHourPlanFragment();
        fragments[4] = new MainPlanFragment();
        fragments[5] = new MainLogFragment();
        fragments[6] = new MainDailyReviewFragment();
        fragments[7] = new MainSettingFragment();
        fragments[8] = new MainAboutFragment();

        fm = getSupportFragmentManager();
        switchContent(0);
    }

    public void switchContent(int id)
    {

        int count = 0;
        switch (id)
        {
            case R.id.menu_main_item_count_down:
                count = 0;
                break;
            case R.id.menu_main_item_calendar:
                count = 1;
                break;
            case R.id.menu_main_item_article:
                count = 2;
                break;
            case R.id.menu_main_item_plan:
                count = 3;
                break;
            case R.id.menu_main_item_hour_plan:
                count = 4;
                break;
            case R.id.menu_main_item_log:
                count = 5;
                break;
            case R.id.menu_main_item_review:
                count = 6;
                break;
            case R.id.menu_main_item_setting:
                count = 7;
                break;
            case R.id.menu_main_item_about:
                count = 8;
                break;
        }
        currentFragment = count;
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.ll_main_content, fragments[count]);
        ft.commit();
    }

    @Override
    public void openMenu(boolean flag)
    {
        if (mDrawerLayout != null)
        {
            if (flag)
            {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            } else
            {
                mDrawerLayout.closeDrawers();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == Constants.REQUEST_CODE_LOG)
            {
                fragments[5].onActivityResult(requestCode, resultCode, data);
            }/* else if (requestCode == Constants.REQUEST_CODE_ARTICLE)
            {
                LogUtil.i("onActivityResult");
                fragments[3].onActivityResult(requestCode, resultCode, data);
            }
*/
        }
    }

    @Override
    public void onBackPressed()
    {
//        super.onBackPressed();
        boolean isInterrupt = false;
        if (currentFragment == 2)
        {
            isInterrupt = ((MainArticleFragment) fragments[2]).onBackPressed();
        }
        if (!isInterrupt)
        {
            super.onBackPressed();
        }
    }
}
