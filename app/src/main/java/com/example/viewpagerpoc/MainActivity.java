package com.example.viewpagerpoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.util.Stack;

import static com.example.viewpagerpoc.ZoneAFragmentsEnum.ZONE_A_FRAGMENT;


public class MainActivity extends AppCompatActivity implements ZoneAFragmentReplaceCallbacks, View.OnClickListener {

    Stack<ZoneAFragmentsEnum> zoneAFragmentsEnumStack = new Stack<>();
    ZoneAFragmentsEnum currentZoneAFragment = ZONE_A_FRAGMENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        ImageView imgHome = (ImageView) findViewById(R.id.imgHome);
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgHome.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        ZoneAFragment zoneAFragment = new ZoneAFragment();
        zoneAFragment.setZoneAFragmentReplaceCallbacks(this);

        ZoneBFragment zoneBFragment = new ZoneBFragment();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.add(R.id.fragmentZoneA, zoneAFragment, "zone_a_fragment_tag");
        transaction.add(R.id.fragmentZoneB, zoneBFragment, "zone_b_fragment_tag");

        transaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Display confirmation here, finish() activity.
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgHome:
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                break;

            case R.id.imgBack:
                goBackToPreviousFragment();
                break;
        }
    }

    @Override
    public void updateFragment(ZoneAFragmentsEnum zoneAFragmentsEnum) {
        updateFragment(zoneAFragmentsEnum, null);
    }

    @Override
    public void updateFragment(ZoneAFragmentsEnum zoneAFragmentsEnum, Bundle bundle) {
        if (currentZoneAFragment == zoneAFragmentsEnum) return;
        zoneAFragmentsEnumStack.push(currentZoneAFragment);
        currentZoneAFragment = zoneAFragmentsEnum;

        replaceFragment(zoneAFragmentsEnum);
    }



    @Override
    public void goBackToPreviousFragment() {
        if(zoneAFragmentsEnumStack.empty()) {
            finish();
            return;
        }

        ZoneAFragmentsEnum zoneAFragmentsEnum = zoneAFragmentsEnumStack.pop();
        if (currentZoneAFragment == zoneAFragmentsEnum) return;
        currentZoneAFragment = zoneAFragmentsEnum;

        replaceFragment(zoneAFragmentsEnum);
    }

    public void replaceFragment(ZoneAFragmentsEnum zoneAFragmentsEnum) {

        BaseFragment fragment = null;
        switch (zoneAFragmentsEnum) {
            case BLUETOOTH_FRAGMENT:

                fragment = new BluetoothFragment();
                break;

            case BLUETOOTH_OPTIONS_FRAGMENT:
                fragment = new BluetoothOptionsFragment();
                break;

            case BLUETOOTH_SEARCH_FRAGMENT:
                fragment = new BluetoothSearchFragment();
                break;

            case BLUETOOTH_PHONE_CONNECT_FRAGMENT:
                fragment = new BluetoothPhoneConnectFragment();
                break;

            case NAVIGATION_FRAGMENT:
                fragment = new NavigationFragment();
                break;

            case RADIO_FRAGMENT:
                fragment = new RadioFragment();
                break;

            case PHONE_FRAGMENT:
                fragment = new PhoneFragment();
                break;

            case SETTINGS_FRAGMENT:
                fragment = new SettingsFragment();
                break;

            case USER_FRAGMENT:
                fragment = new UsersFragment();
                break;

            case ZONE_A_FRAGMENT:
                fragment = new ZoneAFragment();
                break;
        }

        if(fragment != null) {
            fragment.setZoneAFragmentReplaceCallbacks(this);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentZoneA, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
}
