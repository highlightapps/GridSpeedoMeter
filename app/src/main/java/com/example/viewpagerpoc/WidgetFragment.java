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

    FragPageAdapter fragPageAdapterOne;
    FragPageAdapter fragPageAdapterTwo;
    FragPageAdapter fragPageAdapterThree;


    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.widget_fragment_main_layout, null);
        ViewPager pagerOne = (ViewPager)view. findViewById(R.id.viewPagerOne);
        ViewPager pagerTwo = (ViewPager) view.findViewById(R.id.viewPagerTwo);
        ViewPager pagerThree = (ViewPager) view.findViewById(R.id.viewPagerThree);


        FragmentManager fm = getActivity().getSupportFragmentManager();

        fragPageAdapterOne = new FragPageAdapter(fm, WidgetColumn.FIRST_COLUMN);
        fragPageAdapterTwo = new FragPageAdapter(fm, WidgetColumn.SECOND_COLUMN);
        fragPageAdapterThree = new FragPageAdapter(fm, WidgetColumn.THIRD_COLUMN);

        pagerOne.setAdapter(fragPageAdapterOne);
        pagerTwo.setAdapter(fragPageAdapterTwo);
        pagerThree.setAdapter(fragPageAdapterThree);


        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setMax(180);
        seekBar.setProgress(90);
        seekBar.refreshDrawableState();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Log.e("","--->" +progress);
                updateData(progress); //steering angle should be 0 degrees when seekbar is in 90 degrees.
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }

    public void updateData(float angle){
        fragPageAdapterOne.updateAngle(angle);
    }

}
