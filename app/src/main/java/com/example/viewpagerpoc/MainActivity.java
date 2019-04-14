package com.example.viewpagerpoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        //Grid view setup..
        GridView gridView = (GridView) findViewById(R.id.gridView);
        final AppsAdapter appsAdapter = new AppsAdapter(this, Constants.getAppsData());
        gridView.setAdapter(appsAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                AppsModel appsModel = appsAdapter.getItem(position);
                Intent launchIntent = null;
                if(appsModel.getAppLaunch()){
                    //launch app
                    launchIntent = getPackageManager().getLaunchIntentForPackage(appsModel.getPackageId());
                }
                else {
                    //launch activity
                    launchIntent = new Intent(MainActivity.this, appsModel.getActivityName());
                }

                try {
                    startActivity(launchIntent);
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "Something went wrong launching the app/activity", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
