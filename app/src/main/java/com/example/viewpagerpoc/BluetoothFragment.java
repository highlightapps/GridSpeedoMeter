package com.example.viewpagerpoc;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.viewpagerpoc.R;

public class BluetoothFragment extends Fragment {
    Context mContext;
    TextView deviceList;

    StringBuilder blueToothDeviceList = new StringBuilder("");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        mContext = getActivity();
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        deviceList = (TextView) view.findViewById(R.id.deviceList);

    }

    @Override
    public void onResume() {
        super.onResume();
        findListOfAvailableBlueToothDevices();
    }

    @Override
    public void onPause() {
        super.onPause();
        mContext.unregisterReceiver(mReceiver);
    }

    private void findListOfAvailableBlueToothDevices(){
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(mReceiver, filter);
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                Log.i("Device Name: " , "device " + deviceName);
                Log.i("deviceHardwareAddress " , "hard"  + deviceHardwareAddress);
                updateListOnUI(deviceName, deviceHardwareAddress);
            }
        }
    };

    private void updateListOnUI(String deviceName, String deviceHardwareAddress){
        blueToothDeviceList.append(deviceName).append("     ").append(deviceHardwareAddress).append("\n");
        deviceList.setText(blueToothDeviceList.toString());
    }
}
