package com.example.viewpagerpoc;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothSearchFragment extends BaseFragment {

    Context mContext;
    ListView listViewDetected;
    TextView txtSearching;
    ArrayAdapter<String> detectedAdapter;
    BluetoothDevice bdDevice;
    BluetoothAdapter bluetoothAdapter = null;
    ArrayList<BluetoothDevice> arrayListBluetoothDevices = null;
    ListItemClicked listItemClicked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth_search, container, false);
        mContext = getActivity();
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        txtSearching = (TextView) view.findViewById(R.id.txtSearching);
        listViewDetected = (ListView) view.findViewById(R.id.listViewDetected);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        arrayListBluetoothDevices = new ArrayList<BluetoothDevice>();
        //detectedAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_single_choice);


        detectedAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                return view;
            }
        };

        listViewDetected.setAdapter(detectedAdapter);
        listItemClicked = new ListItemClicked();
        detectedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        listViewDetected.setOnItemClickListener(listItemClicked);
    }

    @Override
    public void onResume() {
        super.onResume();
        startSearching();
    }

    public boolean createBond(BluetoothDevice btDevice) throws Exception {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
                if (pairedDevice.size() > 0) {
                    for (BluetoothDevice bluetoothDevice : pairedDevice) {
                        if (!arrayListBluetoothDevices.contains(bluetoothDevice)) {
                            detectedAdapter.add(bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress());
                            arrayListBluetoothDevices.add(bluetoothDevice);
                            detectedAdapter.notifyDataSetChanged();
                        }
                    }
                }
                if (arrayListBluetoothDevices.size() < 1) { // this checks if the size of bluetooth device is 0,then add the
                    detectedAdapter.add(device.getName() + "\n" + device.getAddress());
                    arrayListBluetoothDevices.add(device);
                    detectedAdapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < arrayListBluetoothDevices.size(); i++) {
                        if (!arrayListBluetoothDevices.contains(device)) {
                            detectedAdapter.add(device.getName() + "\n" + device.getAddress());
                            arrayListBluetoothDevices.add(device);
                            detectedAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    };

    private void startSearching() {
        Log.i("Log", "in the start searching method");
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(myReceiver, intentFilter);
        bluetoothAdapter.startDiscovery();
    }

    class ListItemClicked implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            bdDevice = arrayListBluetoothDevices.get(position);
            try {
                boolean isBonded = createBond(bdDevice);
                if (isBonded) {
                    Toast.makeText(mContext, "Device connected successfully", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    zoneAFragmentReplaceCallbacks.updateFragment(ZoneAFragmentsEnum.BLUETOOTH_PHONE_CONNECT_FRAGMENT, bundle);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
