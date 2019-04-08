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

import com.example.viewpagerpoc.views.SteeringView;

public class SteeringWidgetFragment extends Fragment {

    SteeringView steeringView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.steering_widget_fragment_layout, null);

        int DEFAULT_ANGLE = 90;

        steeringView = (SteeringView) view.findViewById(R.id.steeringView);
        steeringView.setSteeringAngle(0);

        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setMax(180);
        seekBar.setProgress(DEFAULT_ANGLE);
        seekBar.refreshDrawableState();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Log.e("","--->" +progress);
                steeringView.setSteeringAngle(progress - 90); //steering angle should be 0 degrees when seekbar is in 90 degrees.
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

    public void updateAngle(float angle){
        steeringView.setSteeringAngle(angle-90);
    }
}
