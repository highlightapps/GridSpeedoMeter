package com.example.viewpagerpoc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.viewpagerpoc.views.SteeringView;


public class WidgetFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.widget_fragment_main_layout, null);
        ViewPager pagerOne = (ViewPager)view. findViewById(R.id.viewPagerOne);
        ViewPager pagerTwo = (ViewPager) view.findViewById(R.id.viewPagerTwo);
        ViewPager pagerThree = (ViewPager) view.findViewById(R.id.viewPagerThree);


        FragmentManager fm = getActivity().getSupportFragmentManager();

        pagerOne.setAdapter(new FragPageAdapter(fm, WidgetColumn.FIRST_COLUMN));
        pagerTwo.setAdapter(new FragPageAdapter(fm, WidgetColumn.SECOND_COLUMN));
        pagerThree.setAdapter(new FragPageAdapter(fm, WidgetColumn.THIRD_COLUMN));

        return view;
    }
}
