package com.aaa.cd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;

import com.aaa.cd.R;
import com.aaa.cd.model.MainCallback;
import com.aaa.cd.util.Constants;

public class MainActivity extends AppCompatActivity implements MainCallback {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Fragment[] fragments = new Fragment[7];
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_menu);
        mNavigationView = (NavigationView) findViewById(R.id.nv_main_menu);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                switchContent(item.getItemId());
                mDrawerLayout.closeDrawers();
                return true;
            }
        });



        fragments[0] = new MainCountDownFragment();
        fragments[1] = new MainCalendarFragment();
        fragments[2] = new MainArticleFragment();
        fragments[3] = new MainHourPlanFragment();
        fragments[4] = new MainPlanFragment();
        fragments[5] = new MainLogFragment();
        fragments[6] = new MainDailyReviewFragment();

        fm = getSupportFragmentManager();
        switchContent(0);
    }

    public void switchContent(int id)
    {
        int count=0;
        switch (id){
            case R.id.menu_main_item_count_down:
                count=0;
                break;
            case R.id.menu_main_item_calendar:
                count=1;
                break;
            case R.id.menu_main_item_article:
                count=2;
                break;
            case R.id.menu_main_item_plan:
                count=3;
                break;
            case R.id.menu_main_item_hour_plan:
                count=4;
                break;
            case R.id.menu_main_item_log:
                count=5;
                break;
            case R.id.menu_main_item_review:
                count=6;
                break;
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.ll_main_content, fragments[count]);
        ft.commit();
    }

    @Override
    public void openMenu(boolean flag) {
        if(mDrawerLayout!=null)
        {
            if(flag){
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }else
            {
                mDrawerLayout.closeDrawers();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode== Constants.REQUEST_CODE_LOG)
            {
                fragments[5].onActivityResult(requestCode, resultCode, data);
            }

        }
    }

}
