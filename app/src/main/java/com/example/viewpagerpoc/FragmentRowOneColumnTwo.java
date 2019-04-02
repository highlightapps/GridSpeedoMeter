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

import com.example.viewpagerpoc.views.TyrePressureView;

public class FragmentRowOneColumnTwo extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_row_one_column_two, null);

        int DEFAULT_PRESSURE = 35;

        final TyrePressureView tyrePressureView = (TyrePressureView) view.findViewById(R.id.tyrePressureView);
        tyrePressureView.updateTyrePressures(DEFAULT_PRESSURE, DEFAULT_PRESSURE, DEFAULT_PRESSURE, DEFAULT_PRESSURE);

        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setMax(40);
        seekBar.setProgress(DEFAULT_PRESSURE);
        seekBar.refreshDrawableState();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                tyrePressureView.updateTyrePressures(progress, progress, progress, progress);
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
