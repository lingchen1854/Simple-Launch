package com.likego.backgrounddesktop.model;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;

/**
 * User: ap01854
 * Date: 2020/7/3
 * Time: 11:17:28
 */
public class AppInfoModel {

    private String appName;
    private Drawable icon;
    private String packageName;
    private String versionName;
    private String versionCode;
    private String packageClassName;
    private String firstInstallTime;
    private String lastUpdateTime;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getPackageClassName() {
        return packageClassName;
    }

    public void setPackageClassName(String packageClassName) {
        this.packageClassName = packageClassName;
    }

    public String getFirstInstallTime() {
        return firstInstallTime;
    }

    public void setFirstInstallTime(String firstInstallTime) {
        this.firstInstallTime = firstInstallTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

}
