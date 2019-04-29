package com.example.viewpagerpoc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class BluetoothFragment extends BaseFragment {
    Context mContext;
    public static String TAG = BluetoothFragment.class.getName();
    FragmentsReplaceListener fragmentsReplaceListener;
    private BluetoothOptionsFragment bluetoothOptionsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        mContext = getActivity();
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        TextView txtOptions = (TextView) view.findViewById(R.id.txtOptions);
        txtOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapFragment();
            }
        });
       }


    private void swapFragment() {
        fragmentsReplaceListener.replaceFragment(bluetoothOptionsFragment);
    /*    BluetoothFragment bluetoothFragment = new BluetoothFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContent, pmMangerRequestFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/
    }
}
