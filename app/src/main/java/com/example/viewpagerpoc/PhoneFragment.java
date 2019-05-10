package com.example.viewpagerpoc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PhoneFragment extends BaseFragment {
    Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone, container, false);
        mContext = getActivity();
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        Button btnContacts = view.findViewById(R.id.btnContacts);
        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoneAFragmentReplaceCallbacks.updateFragment(ZoneAFragmentsEnum.BLUETOOTH_PHONE_CONTACTS_FRAGMENT);
            }
        });

        Button btnRecents = view.findViewById(R.id.btnRecents);
        btnRecents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoneAFragmentReplaceCallbacks.updateFragment(ZoneAFragmentsEnum.PHONE_RECENTS_FRAGMENT);
            }
        });

        TextView txtMenu = view.findViewById(R.id.txtMenu);
        txtMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoneAFragmentReplaceCallbacks.updateFragment(ZoneAFragmentsEnum.PHONE_MENU_FRAGMENT);
            }
        });


    }
}
