package com.example.viewpagerpoc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class LeftFragment extends Fragment implements View.OnClickListener {
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left, container, false);
        mContext = getActivity();
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        Button btnBluetooth1 = (Button) view.findViewById(R.id.btnBluetooth1);
        Button btnBluetooth2 = (Button) view.findViewById(R.id.btnBluetooth2);

        btnBluetooth1.setOnClickListener(this);
        btnBluetooth2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.btnBluetooth1:
                fragment = new BluetoothFragment();
                replaceFragment(fragment);
                break;

            case R.id.btnBluetooth2:
                fragment = new GmailFragment();
                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLeft, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
