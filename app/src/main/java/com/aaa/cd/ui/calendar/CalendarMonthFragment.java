package com.aaa.cd.ui.calendar;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.cd.R;
import com.aaa.cd.model.ItemClickListener;
import com.aaa.cd.ui.main.MainBaseFragment;
import com.aaa.cd.util.Constants;
import com.aaa.cd.util.SPUtil;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

public class CalendarMonthFragment extends MainBaseFragment
{
    private OnFragmentInteractionListener mListener;
    RecyclerView rv_calendar;
    private long bornDate;
    private long deathDate;

    public CalendarMonthFragment()
    {
        bornDate = (long) SPUtil.get(Constants.SP_KEY_BORN_DATE, -1L);
        deathDate = (long) SPUtil.get(Constants.SP_KEY_DEATH_DATE, -1L);
    }

    public static CalendarMonthFragment newInstance()
    {
        CalendarMonthFragment fragment = new CalendarMonthFragment();
        return fragment;
    }


    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
/*        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
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
        CalendarMonthAdapter calendarAdapter = new CalendarMonthAdapter(getActivity(), bornDate, deathDate, listener);
        rv_calendar = (RecyclerView) view.findViewById(R.id.rv_calendar_month);
        GridLayoutManager glm = new GridLayoutManager(getActivity(), 3);
        rv_calendar.setLayoutManager(glm);
        rv_calendar.setAdapter(calendarAdapter);
        rv_calendar.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(getActivity(), R.color.divider_color)));
    }
    ItemClickListener listener=new ItemClickListener()
    {
        @Override
        public void onItemClick(int position,View view)
        {
            CalendarDateFragment dateFragment = CalendarDateFragment.newInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dateFragment.setSharedElementEnterTransition(new DetailsTransition());
                dateFragment.setEnterTransition(new Fade());
                setExitTransition(new Fade());
                dateFragment.setSharedElementReturnTransition(new DetailsTransition());
            }

            getActivity().getSupportFragmentManager()
                         .beginTransaction()
                         .addSharedElement(view, "month_item")
                         .replace(R.id.ll_main_content, dateFragment)
                         .addToBackStack(null)
                         .commit();
        }

        @Override
        public void onItemLongClick(int position, View view)
        {

        }
    };

    @Override
    public int getLayoutId()
    {
        return R.layout.fragment_calendar_month;
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }
}
