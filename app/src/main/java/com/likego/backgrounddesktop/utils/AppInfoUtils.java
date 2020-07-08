package com.likego.backgrounddesktop.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;
import android.util.Log;

import com.likego.backgrounddesktop.model.AppInfoModel;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: ap01854
 * Date: 2020/7/3
 * Time: 11:42:03
 */
public class AppInfoUtils {

    public static ArrayList<AppInfoModel> getLauncherInfo(Context context){
        ArrayList<AppInfoModel> mAppInfoModels = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);

        for (int i = 0;i < apps.size();i++){
            ResolveInfo info = apps.get(i);
            try {
                AppInfoModel appInfoModel = new AppInfoModel();
                PackageInfo packageinfo = packageManager.getPackageInfo(info.activityInfo.applicationInfo.packageName, 0);
                appInfoModel.setAppName(String.valueOf(info.loadLabel(packageManager)));
                appInfoModel.setVersionCode(String.valueOf(packageinfo.versionCode));
                appInfoModel.setVersionName(String.valueOf(packageinfo.versionName));
                appInfoModel.setPackageName(info.activityInfo.applicationInfo.packageName);
                appInfoModel.setIcon(info.activityInfo.loadIcon(packageManager));
                appInfoModel.setPackageClassName(info.activityInfo.name);
                appInfoModel.setFirstInstallTime(stampToDate(packageinfo.firstInstallTime));
                appInfoModel.setLastUpdateTime(stampToDate(packageinfo.lastUpdateTime));
                mAppInfoModels.add(appInfoModel);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mAppInfoModels;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long s){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(s));
    }

    /**
     * 打开开发者模式界面
     */
    public static void startDevelopmentActivity(Activity activity) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            activity.startActivity(intent);
        } catch (Exception e) {
            try {
                ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.DevelopmentSettings");
                Intent intent = new Intent();
                intent.setComponent(componentName);
                intent.setAction("android.intent.action.View");
                activity.startActivity(intent);
            } catch (Exception e1) {
                try {
                    Intent intent = new Intent("com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS");//部分小米手机采用这种方式跳转
                    activity.startActivity(intent);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public static boolean startUiTest(boolean status){
        try {
            Process process = Runtime.getRuntime().exec("/system/bin/sh");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop debug.layout "+status);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
