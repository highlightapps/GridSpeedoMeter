package com.example.viewpagerpoc;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.vcard.VCardEntry;

import java.util.ArrayList;

public class PhoneContactsSearchFragment extends BaseFragment implements AdapterOnItemClickListener {

    ArrayList<VCardEntry> contacts = new ArrayList<>();

    //Views
    RecyclerView recyclerView;
    TextView txtBluetoothDevice;
    PhoneFragmentContactsAdapter phoneFragmentContactsAdapter;
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_search, container, false);
        mContext = getActivity();
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        phoneFragmentContactsAdapter = new PhoneFragmentContactsAdapter(null, this);
        recyclerView.setAdapter(phoneFragmentContactsAdapter);
    }

    @Override
    public void onItemClickListener(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("displayName", contacts.get(position).getDisplayName());
        zoneAFragmentReplaceCallbacks.updateFragment(ZoneAFragmentsEnum.PHONE_CONTACTS_DETAIL_FRAGMENT, bundle);
    }
}
