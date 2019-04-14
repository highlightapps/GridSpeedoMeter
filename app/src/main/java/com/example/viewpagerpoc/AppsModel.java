package com.example.viewpagerpoc;

public class AppsModel {

    public AppsModel(Object appName, Object appIcon, Object isAppLaunch, Object packageId, Object activityName){
        this.appName = (String) appName;
        this.appIcon = (Integer) appIcon;
        this.isAppLaunch = (Boolean) isAppLaunch;
        this.packageId = (String) packageId;
        this.activityName = (Class) activityName;
    }

    private String appName;

    private Integer appIcon;

    private Boolean isAppLaunch;

    private String packageId;

    private Class activityName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Integer appIcon) {
        this.appIcon = appIcon;
    }

    public Boolean getAppLaunch() {
        return isAppLaunch;
    }

    public void setAppLaunch(Boolean appLaunch) {
        isAppLaunch = appLaunch;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public Class getActivityName() {
        return activityName;
    }

    public void setActivityName(Class activityName) {
        this.activityName = activityName;
    }
}
