package com.example.viewpagerpoc.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.viewpagerpoc.R;

public class TyrePressureView extends FrameLayout {

    String tyrePressureMeasure = " psi";

    TextView txtViewTopLeftTyre, txtViewTopRightTyre, txtViewBottomLeftTyre, txtViewBottomRightTyre;

    public TyrePressureView(@NonNull Context context) {
        super(context);
        initView();
    }

    public TyrePressureView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TyrePressureView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public TyrePressureView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.tyre_pressure_layout, null);
        txtViewTopLeftTyre = (TextView) view.findViewById(R.id.txtViewTopLeftTyre);
        txtViewTopRightTyre = (TextView) view.findViewById(R.id.txtViewTopRightTyre);
        txtViewBottomLeftTyre = (TextView) view.findViewById(R.id.txtViewBottomLeftTyre);
        txtViewBottomRightTyre = (TextView) view.findViewById(R.id.txtViewBottomRightTyre);
        addView(view);
    }

    public void updateTyrePressures(int topLeft, int topRight, int bottomLeft, int bottomRight) {
        txtViewTopLeftTyre.setText(topLeft + tyrePressureMeasure);
        txtViewTopRightTyre.setText(topRight + tyrePressureMeasure);
        txtViewBottomLeftTyre.setText(bottomLeft + tyrePressureMeasure);
        txtViewBottomRightTyre.setText(bottomRight + tyrePressureMeasure);
    }


}
