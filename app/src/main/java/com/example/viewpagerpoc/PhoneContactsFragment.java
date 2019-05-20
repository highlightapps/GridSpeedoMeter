package com.example.viewpagerpoc;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vcard.VCardEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import bluetooth.client.pbap.BluetoothPbapClient;

import static bluetooth.client.pbap.BluetoothPbapClient.EVENT_PULL_PHONE_BOOK_DONE;
import static bluetooth.client.pbap.BluetoothPbapClient.EVENT_PULL_PHONE_BOOK_ERROR;
import static bluetooth.client.pbap.BluetoothPbapClient.EVENT_SESSION_CONNECTED;
import static bluetooth.client.pbap.BluetoothPbapClient.EVENT_SESSION_DISCONNECTED;

public class PhoneContactsFragment extends BaseFragment implements AdapterOnItemClickListener{

    public static BluetoothPbapClient sPbapClient = null;
    boolean action_app_disconnect = false;
    BluetoothDevice device = null;
    boolean retrieve_phone_contact = false;

    ArrayList<VCardEntry> contacts = new ArrayList<>();

    boolean isSortByFirstName = true;

    //Views
    RecyclerView recyclerView;
    TextView txtBluetoothDevice;
    PhoneFragmentContactsAdapter phoneFragmentContactsAdapter;
    String[] contactsSortType = {"First Name", "Last Name"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        checkBluetoothIsEnabled();
        initViews(view);
        initLogic();
        return view;
    }

    private void initViews(View view) {

        initSpinner(view);

        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        phoneFragmentContactsAdapter = new PhoneFragmentContactsAdapter(null, this);
        recyclerView.setAdapter(phoneFragmentContactsAdapter);
        txtBluetoothDevice = view.findViewById(R.id.txtBluetoothDevice);

        TextView txtSearchList = view.findViewById(R.id.txtSearchList);
        txtSearchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contacts.isEmpty()){
                    Toast.makeText(getActivity(), "No contacts available", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("contactList", contacts);
                zoneAFragmentReplaceCallbacks.updateFragment(ZoneAFragmentsEnum.PHONE_CONTACTS_SEARCH_FRAGMENT, bundle);
            }
        });
    }

    private void initSpinner(View view) {
        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.phone_contacts_sort_spinner_item, contactsSortType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                isSortByFirstName = (position == 0);
                sortContactsByNamePreference();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    private void sortContactsByNamePreference() {
        Comparator<VCardEntry> compareByName = null;

        if(isSortByFirstName){
            //First name sorter
            compareByName = Comparator.nullsLast((o1, o2) -> o1.getNameData().getGiven().compareTo(o2.getNameData().getGiven()));
        }
        else {
            //Last name sorter
            compareByName = Comparator.nullsLast((o1, o2) -> o1.getNameData().getFamily().compareTo(o2.getNameData().getFamily()));
        }
        Collections.sort(contacts, compareByName);
        phoneFragmentContactsAdapter.setContacts(contacts);
    }

    private void initLogic() {
        requestAllPermissions();
        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice btDevice : pairedDevices) {
                if(btDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    String deviceName = btDevice.getName();
                    Toast.makeText(getContext(), "Device is connected to the " + deviceName, Toast.LENGTH_SHORT).show();
                    device = btDevice;
                    break;
                }
            }
        }

        String deviceName = null;
        if(device != null) {
            deviceName = device.getName();
            connectToPhoneBook(device);
        }
        else {
            deviceName = "No BT Device";
            Toast.makeText(getContext(), "Device is not connected with any bluetooth device, Please connect it the bluetooth device.", Toast.LENGTH_SHORT).show();
        }

        txtBluetoothDevice.setText("Contacts  (" + deviceName + ")");

    }

    @Override
    public void onItemClickListener(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("displayName", contacts.get(position).getDisplayName());
        zoneAFragmentReplaceCallbacks.updateFragment(ZoneAFragmentsEnum.PHONE_CONTACTS_DETAIL_FRAGMENT, bundle);
    }

    private class BluetoothServiceHandler extends Handler {

        private BluetoothServiceHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_PULL_PHONE_BOOK_DONE:
                    Toast.makeText(getContext(), "Contacts retrieved successfully..", Toast.LENGTH_SHORT).show();
                    break;
                case EVENT_PULL_PHONE_BOOK_ERROR:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Contacts retrieving failed, connection issue..", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case EVENT_SESSION_CONNECTED:
                    if (sPbapClient == null || sPbapClient.getState() != BluetoothPbapClient.ConnectionState.CONNECTED) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Unable to establish connection with the device..", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    sPbapClient.pullPhoneBook(BluetoothPbapClient.PB_PATH);
                    Toast.makeText(getContext(), "Retrieving contacts, please wait..", Toast.LENGTH_SHORT).show();
                    return;
                case EVENT_SESSION_DISCONNECTED:
                    if (action_app_disconnect) {
                        action_app_disconnect = false;
                        return;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "disconnected the session from the device..", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                default:
                    return;
            }
            if (!retrieve_phone_contact) {
                retrieve_phone_contact = true;
                if (msg.obj != null) {
                    contacts.addAll((ArrayList) msg.obj);
                }
                if (contacts != null && !contacts.isEmpty()) {
                    phoneFragmentContactsAdapter.setContacts(contacts);
                }
                action_app_disconnect = true;
                disconnectFromPhoneBook();
            }
        }
    }

    public void connectToPhoneBook(BluetoothDevice device) {
        sPbapClient = null;
        sPbapClient = new BluetoothPbapClient(device, new BluetoothServiceHandler());
        sPbapClient.connect();
    }

    public void disconnectFromPhoneBook() {
        if (sPbapClient != null) {
            sPbapClient.disconnect();
        }
    }

    private void checkBluetoothIsEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled())
        {
            Toast.makeText(getActivity(), "Please turn on the bluetooth.", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint({"NewApi"})
    protected void requestAllPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int read_contact = ContextCompat.checkSelfPermission(getActivity(), "android.permission.READ_CONTACTS");
            int write_contact = ContextCompat.checkSelfPermission(getActivity(), "android.permission.WRITE_CONTACTS");
            int access_location = ContextCompat.checkSelfPermission(getActivity(), "android.permission.ACCESS_FINE_LOCATION");
            if (read_contact != 0 || write_contact != 0 || access_location != 0) {
                requestPermissions(new String[]{"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.ACCESS_FINE_LOCATION"}, 1);
            }
        }
    }
}
