package com.example.viewpagerpoc;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.example.viewpagerpoc.steering.SteeringView;

public class FragmentRowOneColumnOne extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_row_one_column_one, null);
        final SteeringView steeringView = (SteeringView) view.findViewById(R.id.steeringView);
        steeringView.setSteeringAngle(90);
        steeringView.postInvalidate();
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setMax(180);
        seekBar.setProgress(90);
        seekBar.refreshDrawableState();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.e("","--->" +i);
                steeringView.setSteeringAngle(i - 90); //steering angle should be 0 degrees when seekbar is in 90 degrees.
                steeringView.postInvalidate();
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
}
