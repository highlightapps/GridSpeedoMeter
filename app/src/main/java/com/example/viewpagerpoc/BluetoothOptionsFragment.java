package com.example.viewpagerpoc;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class BluetoothOptionsFragment extends BaseFragment {

    public static final String TAG = BluetoothOptionsFragment.class.getName();

    Button btnBluetoothOff, btnBluetoothOn;

    BluetoothAdapter bluetoothAdapter = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth_options, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btnBluetoothOn = (Button) view.findViewById(R.id.txtBluetoothOn);
        btnBluetoothOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBluetooth();
            }
        });
        btnBluetoothOff = (Button) view.findViewById(R.id.txtBluetoothOff);
        btnBluetoothOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offBluetooth();
            }
        });
    }



    private void onBluetooth() {
        if(!bluetoothAdapter.isEnabled())
        {
            Toast.makeText(getActivity(), "Bluetooth is enabled", Toast.LENGTH_LONG).show();
            bluetoothAdapter.enable();
            Log.i("Log", "Bluetooth is Enabled");
        } else {
            Toast.makeText(getActivity(), "Bluetooth is already enabled", Toast.LENGTH_LONG).show();
        }
        zoneAFragmentReplaceCallbacks.goBackToPreviousFragment();
    }
    private void offBluetooth() {
        if(bluetoothAdapter.isEnabled())
        {
            Toast.makeText(getActivity(), "Bluetooth is turned off", Toast.LENGTH_LONG).show();
            bluetoothAdapter.disable();
        }
        else {
            Toast.makeText(getActivity(), "Bluetooth is already enabled", Toast.LENGTH_LONG).show();
        }
        zoneAFragmentReplaceCallbacks.goBackToPreviousFragment();
    }
}
