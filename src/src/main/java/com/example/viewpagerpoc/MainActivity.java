package com.example.viewpagerpoc;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager pagerOne = (ViewPager) findViewById(R.id.viewPagerOne);
        ViewPager pagerTwo = (ViewPager) findViewById(R.id.viewPagerTwo);
        ViewPager pagerThree = (ViewPager) findViewById(R.id.viewPagerThree);


        FragmentManager fm = getSupportFragmentManager();

        pagerOne.setAdapter(new FragPageAdapter(fm, WidgetColumn.FIRST_COLUMN));
        pagerTwo.setAdapter(new FragPageAdapter(fm, WidgetColumn.SECOND_COLUMN));
        pagerThree.setAdapter(new FragPageAdapter(fm, WidgetColumn.THIRD_COLUMN));
    }
}
