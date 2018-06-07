package com.aaa.cd.ui.calendar;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaa.cd.R;
import com.aaa.cd.ui.main.MainBaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarYearFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarYearFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarYearFragment extends MainBaseFragment
{

    private OnFragmentInteractionListener mListener;

    public CalendarYearFragment()
    {
        // Required empty public constructor
    }

    public static CalendarYearFragment newInstance(String param1, String param2)
    {
        CalendarYearFragment fragment = new CalendarYearFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_calendar_year, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
    public int getLayoutId()
    {
        return R.layout.fragment_calendar_year;
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
