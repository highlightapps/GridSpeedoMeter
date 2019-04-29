package com.example.viewpagerpoc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BluetoothFragment extends BaseFragment {

    public static String TAG = BluetoothFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        TextView txtOptions = (TextView) view.findViewById(R.id.txtOptions);
        txtOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoneAFragmentReplaceCallbacks.updateFragment(ZoneAFragmentsEnum.BLUETOOTH_OPTIONS_FRAGMENT);
            }
        });
    }
}
