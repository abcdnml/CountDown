package com.aaa.cd.ui.main;


import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.model.ItemClickListener;
import com.aaa.cd.ui.calendar.CalendarMonthFragment;
import com.aaa.cd.ui.calendar.CalendarYearAdapter;
import com.aaa.cd.ui.calendar.DetailsTransition;
import com.aaa.cd.ui.calendar.ProgressActivity;
import com.aaa.cd.util.Constants;
import com.aaa.cd.util.SPUtil;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCalendarFragment extends MainBaseFragment
{

    RecyclerView rv_calendar;
    private long bornDate;
    private long deathDate;
    LinearLayout ll_title;
    public MainCalendarFragment()
    {
        bornDate = (long) SPUtil.get(Constants.SP_KEY_BORN_DATE, -1L);
        deathDate = (long) SPUtil.get(Constants.SP_KEY_DEATH_DATE, -1L);
    }

    @Override
    public void initTitle(View view)
    {
        TextView tv_title_content = (TextView) view.findViewById(R.id.tv_title_content);
        tv_title_content.setText(R.string.menu_calendar);
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
        ll_title=(LinearLayout)view.findViewById(R.id.ll_calendar_title);

        CalendarYearAdapter calendarAdapter=new CalendarYearAdapter(getActivity(),bornDate,deathDate,listener);
        rv_calendar=(RecyclerView)view.findViewById(R.id.rv_calendar);
        GridLayoutManager glm=new GridLayoutManager(getActivity(),8);
        rv_calendar.setLayoutManager(glm);
        rv_calendar.setAdapter(calendarAdapter);
        rv_calendar.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(getActivity(), R.color.divider_color)));
    }

    ItemClickListener listener=new ItemClickListener()
    {
        @Override
        public void onItemClick(int position,View view)
        {
/*            CalendarMonthFragment yearFragment = CalendarMonthFragment.newInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                yearFragment.setSharedElementEnterTransition(new DetailsTransition());
                yearFragment.setEnterTransition(new Fade());
                setExitTransition(new Fade());
                yearFragment.setSharedElementReturnTransition(new DetailsTransition());
            }

            getActivity().getSupportFragmentManager()
                         .beginTransaction()
                         .addSharedElement(view, "year_item")
                         .addSharedElement(ll_title, "year_title")
//                         .addSharedElement(ll_title, "year_item")
                         .replace(R.id.ll_main_content, yearFragment)
                         .addToBackStack(null)
                         .commit();*/
            ProgressActivity.show(getActivity());
        }

        @Override
        public void onItemLongClick(int position, View view)
        {

        }
    };

    @Override
    public int getLayoutId()
    {
        return R.layout.fragment_main_calendar;
    }

}
