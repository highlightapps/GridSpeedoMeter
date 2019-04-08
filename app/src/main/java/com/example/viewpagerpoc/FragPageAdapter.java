package com.example.viewpagerpoc;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.viewpagerpoc.views.CompassView;
import com.example.viewpagerpoc.views.SteeringView;
import com.example.viewpagerpoc.views.TyrePressureView;

public class FragPageAdapter extends PagerAdapter {

    Context mContext;

    WidgetColumn widgetColumn;
    SteeringView steeringView;
    TextView textTop;
    CompassView compassView;;


    public FragPageAdapter(Context context, WidgetColumn widgetColumn) {
        this.mContext = context;
        this.widgetColumn = widgetColumn;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ViewGroup layout = null;
        switch (widgetColumn) {
            case FIRST_COLUMN:
                layout = getColumnOneFragment(collection, position);
                break;
            case SECOND_COLUMN:
                layout = getColumnTwoFragment(collection, position);
                break;
            case THIRD_COLUMN:
                layout = getColumnThreeFragment(collection, position);
                break;
        }
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    private ViewGroup getColumnOneFragment(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (position) {
            case 0: // row 1
            {
                ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.steering_widget_fragment_layout, collection, false);
                int DEFAULT_ANGLE = 90;

                steeringView = (SteeringView) layout.findViewById(R.id.steeringView);
                steeringView.setSteeringAngle(0);

                SeekBar seekBar = (SeekBar) layout.findViewById(R.id.seekBar);
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
                return layout;
            }
            case 1: // row 2
            {
                ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_row_two_column_one, collection, false);
                return layout;
            }
            case 2: // row 3
            {
                ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_row_three_column_one, collection, false);
                return layout;
            }
        }
        return null;
    }

    private ViewGroup getColumnTwoFragment(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (position) {
            case 0: // row 1
            {
                ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.tyre_pressure_widget_fragment, collection, false);
                int DEFAULT_PRESSURE = 35;

                final TyrePressureView tyrePressureView = (TyrePressureView) layout.findViewById(R.id.tyrePressureView);
                tyrePressureView.updateTyrePressures(DEFAULT_PRESSURE, DEFAULT_PRESSURE, DEFAULT_PRESSURE, DEFAULT_PRESSURE);

                SeekBar seekBar = (SeekBar) layout.findViewById(R.id.seekBar);
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
                return layout;
            }
            case 1: // row 2
            {
                ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_row_two_column_two, collection, false);
                return layout;
            }
            case 2: // row 3
            {
                ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_row_three_column_two, collection, false);
                return layout;
            }
        }
        return null;
    }

    private ViewGroup getColumnThreeFragment(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (position) {
            case 0: // row 1
            {
                ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.compass_widget_fragment_layout, collection, false);
                textTop = (TextView) layout.findViewById(R.id.textTop);
                compassView = (CompassView) layout.findViewById(R.id.compass);
                return layout;
            }
            case 1: // row 2
            {
                ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_row_two_column_three, collection, false);
                return layout;
            }
            case 2: // row 3
            {
                ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_row_three_column_three, collection, false);
                return layout;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public void updateAngle(float angle) {
        steeringView.setSteeringAngle(angle-90);
    }

    public void updateCompassText(String str){
        if(textTop != null)
            textTop.setText(str);
    }


    public void updateCompass(){
        if(compassView != null){
            compassView.postInvalidate();
        }
    }
}
