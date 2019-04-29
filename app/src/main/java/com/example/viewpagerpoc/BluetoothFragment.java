package com.example.viewpagerpoc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BluetoothFragment extends BaseFragment {

    public static String TAG = BluetoothFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

    }

}
