package com.wawa.baselib.utils;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * 作者：create by 张金 on 2021/1/6 15:00
 * 邮箱：564813746@qq.com
 */
public class AppUtils {
    public static String getMetaData(Context context, String key){
        String metaStr = "";
        try {
            ApplicationInfo applicationInfo =
                    context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                metaStr = applicationInfo.metaData.getString(key);// 这里为对应meta-data的name
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return metaStr;
    }
}
