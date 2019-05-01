package com.example.viewpagerpoc;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothFragment extends BaseFragment {

    public static String TAG = BluetoothFragment.class.getName();
    Context mContext;
    ListView listViewPaired;
    BluetoothAdapter bluetoothAdapter = null;
    ArrayAdapter<String> pairedAdapter;
    ArrayList<BluetoothDevice> arrayListPairedBluetoothDevices;
    ArrayList<String> arrayListpaired;
    ListItemClickedonPaired listItemClickedonPaired;
    BluetoothDevice bdDevice;

    @Override
    public void onStart() {
        super.onStart();
        listViewPaired.setOnItemClickListener(listItemClickedonPaired);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPairedDevices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        listViewPaired = (ListView) view.findViewById(R.id.listViewPaired);
        arrayListpaired = new ArrayList<String>();
        arrayListPairedBluetoothDevices = new ArrayList<BluetoothDevice>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        listItemClickedonPaired = new ListItemClickedonPaired();
        TextView txtOptions = (TextView) view.findViewById(R.id.txtOptions);
        txtOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoneAFragmentReplaceCallbacks.updateFragment(ZoneAFragmentsEnum.BLUETOOTH_OPTIONS_FRAGMENT);
            }
        });

        LinearLayout addNewDeviceLayout = (LinearLayout) view.findViewById(R.id.addNewDeviceLayout);
        addNewDeviceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoneAFragmentReplaceCallbacks.updateFragment(ZoneAFragmentsEnum.BLUETOOTH_SEARCH_FRAGMENT);
            }
        });

        pairedAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, arrayListpaired);
        listViewPaired.setAdapter(pairedAdapter);

    }

    public boolean removeBond(BluetoothDevice btDevice)
            throws Exception {
        Class btClass = Class.forName("android.bluetooth.BluetoothDevice");
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    public boolean createBond(BluetoothDevice btDevice)
            throws Exception {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }


    private void getPairedDevices() {
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
        if (pairedDevice.size() > 0) {
            for (BluetoothDevice device : pairedDevice) {
                if (!arrayListpaired.contains(device)) {
                    arrayListpaired.add(device.getName() + "\n" + device.getAddress());
                    arrayListPairedBluetoothDevices.add(device);
                }
            }
        }
        pairedAdapter.notifyDataSetChanged();
    }

    class ListItemClickedonPaired implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            bdDevice = arrayListPairedBluetoothDevices.get(position);
            try {
                Boolean removeBonding = removeBond(bdDevice);
                if (removeBonding) {
                    arrayListpaired.remove(position);
                    arrayListPairedBluetoothDevices.remove(position);
                    pairedAdapter.notifyDataSetChanged();
                }


                Log.i("Log", "Removed" + removeBonding);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
