package com.example.viewpagerpoc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ZoneAFragment extends BaseFragment implements View.OnClickListener {
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zone_a, container, false);
        mContext = getActivity();
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        Button btnNavigation = (Button) view.findViewById(R.id.btnNavigation);
        Button btnBluetooth = (Button) view.findViewById(R.id.btnBluetooth);

        Button btnRadio = (Button) view.findViewById(R.id.btnRadio);
        Button btnPhone = (Button) view.findViewById(R.id.btnPhone);

        Button btnSettings = (Button) view.findViewById(R.id.btnSettings);
        Button btnUsers = (Button) view.findViewById(R.id.btnUsers);

        btnNavigation.setOnClickListener(this);
        btnBluetooth.setOnClickListener(this);

        btnRadio.setOnClickListener(this);
        btnPhone.setOnClickListener(this);

        btnSettings.setOnClickListener(this);
        btnUsers.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.btnNavigation:
                fragment = new NavigationFragment();
                replaceFragment(fragment);
                break;

            case R.id.btnBluetooth:
                fragment = new BluetoothFragmentOld();
                replaceFragment(fragment);
                break;


            case R.id.btnRadio:
                fragment = new RadioFragment();
                replaceFragment(fragment);
                break;


            case R.id.btnPhone:
                fragment = new PhoneFragment();
                replaceFragment(fragment);
                break;


            case R.id.btnSettings:
                fragment = new SettingsFragment();
                replaceFragment(fragment);
                break;

            case R.id.btnUsers:
                fragment = new UsersFragment();
                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentZoneA, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
