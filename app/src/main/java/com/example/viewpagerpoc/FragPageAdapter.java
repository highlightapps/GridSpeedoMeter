package com.example.viewpagerpoc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragPageAdapter extends FragmentPagerAdapter {

    WidgetColumn widgetColumn;

    public FragPageAdapter(FragmentManager fm, WidgetColumn widgetColumn) {
        super(fm);
        this.widgetColumn = widgetColumn;
    }

    @Override
    public Fragment getItem(int position) {

        switch (widgetColumn) {
            case FIRST_COLUMN:
                return getColumnOneFragment(position);
            case SECOND_COLUMN:
                return getColumnTwoFragment(position);
            case THIRD_COLUMN:
                return getColumnThreeFragment(position);
        }
        return null;

    }

    private Fragment getColumnOneFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentRowOneColumnOne();
            case 1:
                return new FragmentRowTwoColumnOne();
            case 2:
                return new FragmentRowThreeColumnOne();
        }
        return null;
    }

    private Fragment getColumnTwoFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentRowOneColumnTwo();
            case 1:
                return new FragmentRowTwoColumnTwo();
            case 2:
                return new FragmentRowThreeColumnTwo();
        }
        return null;
    }

    private Fragment getColumnThreeFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentRowOneColumnThree();
            case 1:
                return new FragmentRowTwoColumnThree();
            case 2:
                return new FragmentRowThreeColumnThree();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
