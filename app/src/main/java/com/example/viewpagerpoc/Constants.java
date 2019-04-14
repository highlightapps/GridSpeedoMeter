package com.example.viewpagerpoc;

import android.content.Intent;

import java.util.ArrayList;

public class Constants {

    private static Object[][] appsData = {
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},


            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class},
            {"Gmail Application", R.drawable.gmail_icon, Boolean.TRUE, "com.google.android.gm", null},
            {"Steering Activity", R.drawable.steering, Boolean.FALSE, null, SteeringActivity.class}
    };


    public static ArrayList<AppsModel> getAppsData() {
        ArrayList<AppsModel> appsModelArrayList = new ArrayList<>();
        for (Object[] appData : appsData) {
            AppsModel appsModel = new AppsModel(appData[0], appData[1], appData[2], appData[3], appData[4]);
            appsModelArrayList.add(appsModel);
        }
        return appsModelArrayList;
    }

}
