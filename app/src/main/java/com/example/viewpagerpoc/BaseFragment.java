package com.example.viewpagerpoc;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    protected ZoneAFragmentReplaceCallbacks zoneAFragmentReplaceCallbacks;

    public void setZoneAFragmentReplaceCallbacks(ZoneAFragmentReplaceCallbacks zoneAFragmentReplaceCallbacks) {
        this.zoneAFragmentReplaceCallbacks = zoneAFragmentReplaceCallbacks;
    }
}
