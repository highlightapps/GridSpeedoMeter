package com.example.viewpagerpoc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PhoneRecentsFragment extends BaseFragment {

    Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_recents, container, false);
        mContext = getActivity();
        initViews(view);
        return view;
    }

    private void initViews(View view) {

    }
}
