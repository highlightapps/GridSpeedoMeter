package com.example.viewpagerpoc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragPageAdapter extends FragmentPagerAdapter {

    SteeringWidgetFragment steeringWidgetFragment;

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
            case 0: // row 1
                steeringWidgetFragment = new SteeringWidgetFragment();
                return steeringWidgetFragment;
            case 1: // row 2
                return new FragmentRowTwoColumnOne();
            case 2: // row 3
                return new FragmentRowThreeColumnOne();
        }
        return null;
    }

    private Fragment getColumnTwoFragment(int position) {
        switch (position) {
            case 0: // row 1
                return new TyrePressureWidgetFragment();
            case 1: // row 2
                return new FragmentRowTwoColumnTwo();
            case 2: // row 3
                return new FragmentRowThreeColumnTwo();
        }
        return null;
    }

    private Fragment getColumnThreeFragment(int position) {
        switch (position) {
            case 0: // row 1
                return new CompassWidgetFragment();
            case 1: // row 2
                return new FragmentRowTwoColumnThree();
            case 2: // row 3
                return new FragmentRowThreeColumnThree();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }


    public void updateAngle(float angle){
        steeringWidgetFragment.updateAngle(angle);
    }
}
