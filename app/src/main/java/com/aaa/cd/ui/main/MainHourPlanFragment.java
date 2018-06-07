package com.aaa.cd.ui.main;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.dao.HourPlanDao;
import com.aaa.cd.model.Callback;
import com.aaa.cd.po.HourPlan;
import com.aaa.cd.ui.hourplan.HourPlanAdapter;
import com.aaa.cd.ui.hourplan.HourPlanEditActivity;
import com.aaa.cd.util.Constants;
import com.aaa.cd.util.CountDownApplication;
import com.aaa.cd.util.DialogUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.List;

import static com.aaa.cd.ui.hourplan.HourPlanAdapter.VIEWTYPE_FINISH;
import static com.aaa.cd.ui.hourplan.HourPlanAdapter.VIEWTYPE_START;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainHourPlanFragment extends MainBaseFragment
{

    SwipeMenuRecyclerView rv_plan;
    FloatingActionButton fab_add;
    HourPlanAdapter adapter;
    List<HourPlan> lhp;

    public MainHourPlanFragment()
    {
    }

    @Override
    public void initTitle(View view)
    {
        TextView tv_title_content = (TextView) view.findViewById(R.id.tv_title_content);
        tv_title_content.setText(R.string.menu_hour_plan);
        ImageView iv_left = (ImageView) view.findViewById(R.id.iv_title_left);
        iv_left.setVisibility(View.VISIBLE);
        iv_left.setImageResource(R.mipmap.menu);
        iv_left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mainCallback.openMenu(true);
            }
        });
    }

    @Override
    public void initView(View view)
    {
        rv_plan = (SwipeMenuRecyclerView) view.findViewById(R.id.rv_hour_plan);
        rv_plan.requestFocus();
        rv_plan.setSwipeMenuCreator(swipeMenuCreator);
        rv_plan.setSwipeMenuItemClickListener(mMenuItemClickListener);
        rv_plan.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(getActivity(), R.color.divider_color)));


        lhp = HourPlanDao.getAllPlan(CountDownApplication.getApplication().getUser().getId());
        if (lhp.size() == 0)
        {
            HourPlan plan = new HourPlan();
            plan.setTitle("看书");
            plan.setDescription("书中自有万物在 书中长存天下道 读书可以怡情 养性");
            plan.setType(1);
            plan.setExecuteStartTime(-1);
            plan.setPlanTime(1000L * 3600 * 1000);
            plan.setCreateTime(System.currentTimeMillis());
            plan.setCurrentTime(0);
            plan.setUserId(CountDownApplication.getApplication().getUser().getId());
            HourPlanDao.addHourPlan(plan);
            lhp = HourPlanDao.getAllPlan(CountDownApplication.getApplication().getUser().getId());
        }

        adapter = new HourPlanAdapter(getActivity(), lhp);
        rv_plan.setAdapter(adapter);
        rv_plan.setLayoutManager(new LinearLayoutManager(getActivity()));


        fab_add = (FloatingActionButton) view.findViewById(R.id.fab_add_hour_plan);
        fab_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), HourPlanEditActivity.class);
                intent.putExtra(Constants.INTENT_KEY_HOURPLAN_MODE, Constants.INTENT_VALUE_HOURPLAN_MODE_ADD);
                startActivityForResult(intent, Constants.REQUEST_CODE_HOURPLAN_RESULT);
            }
        });
    }

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator()
    {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType)
        {
            int width = getResources().getDimensionPixelSize(R.dimen.dimen_64);

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;ViewGroup.LayoutParams.MATCH_PARENT
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            {
                int drawableId = R.drawable.selector_play;
                if (viewType == VIEWTYPE_FINISH)
                {
                    drawableId = R.drawable.selector_finish;
                } else if (viewType == VIEWTYPE_START)
                {
                    drawableId = R.drawable.selector_play;
                } else
                {
                    drawableId = R.drawable.selector_pause;
                }
                SwipeMenuItem addItem = new SwipeMenuItem(getActivity()).setBackground(R.drawable.selector_blue).setImage(drawableId).setWidth(width).setHeight(height);
                swipeLeftMenu.addMenuItem(addItem); // 添加菜单到左侧。
            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity()).setBackground(R.drawable.selector_red).setImage(R.drawable.selector_delete).setTextColor(Color.WHITE).setWidth(width).setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

            }
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener()
    {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge)
        {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION)
            {
                if (menuPosition == 0)
                {
                    DialogUtils.showDeleteDialog(getActivity(), getString(R.string.dialog_delete_plan), deleteCallback, lhp.get(adapterPosition).getId());
                }
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION)
            {
                if (menuPosition == 0)
                {
                    HourPlan hp=lhp.get(adapterPosition);
                    if (hp.getCurrentTime() >= hp.getPlanTime())
                    {
                        //如果已完成
                        return;
                    } else
                    {
                        if(hp.getExecuteStartTime()>0)
                        {
                            long duration=System.currentTimeMillis()-hp.getExecuteStartTime();
                            duration=duration*10000;
                            hp.setExecuteStartTime(-1);
                            hp.setCurrentTime(hp.getCurrentTime()+duration);
                        }else{
                            hp.setExecuteStartTime(System.currentTimeMillis());
                        }
                        HourPlanDao.updatePlan(hp);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    Callback deleteCallback = new Callback()
    {
        @Override
        public void onCallback(int id)
        {
            deleteFile(id);
        }
    };


    public void deleteFile(int id)
    {
        HourPlanDao.deleteHourPlanById(id);
        refresh();
    }

    public void refresh()
    {
        lhp = HourPlanDao.getAllPlan(CountDownApplication.getApplication().getUser().getId());
        adapter.update(lhp);
    }

    @Override
    public int getLayoutId()
    {
        return R.layout.fragment_main_hour_plan;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == Constants.REQUEST_CODE_HOURPLAN_RESULT)
            {
                refresh();
            }
        }
    }
}
