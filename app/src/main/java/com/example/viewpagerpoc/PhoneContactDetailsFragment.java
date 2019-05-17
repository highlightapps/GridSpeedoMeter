package com.example.viewpagerpoc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class PhoneContactDetailsFragment extends BaseFragment {

    String displayName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_contact_details, container, false);
        readDataFromBundle();
        initViews(view);
        return view;
    }

    private void readDataFromBundle(){
        Bundle bundle = getArguments();
        if(bundle != null){
            displayName = bundle.getString("displayName");
        }
    }

    private void initViews(View view) {
        TextView txtDisplayName = view.findViewById(R.id.txtDisplayName);
        txtDisplayName.setText(displayName);
    }

}
