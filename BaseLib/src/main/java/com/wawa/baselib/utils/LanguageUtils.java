package com.wawa.baselib.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;

import com.wawa.baselib.utils.enumpk.LanguageType;

import java.util.Locale;

public class LanguageUtils {
    private static final String TAG = "LanguageUtil";

    /**切换语言关键类
     * @param context
     * @param newLanguage
     */
    @SuppressWarnings("deprecation")
    public static void changeAppLanguage(Context context, String newLanguage) {
        if (newLanguage==null) {
            return;
        }
        if (newLanguage.equalsIgnoreCase("1")){
            newLanguage=LanguageUtils.getCurLanguage();
        }
        //获取想要切换的语言类型
        Locale locale = getLocaleByLanguage(newLanguage);
        setApplicationLanguage(context,locale);
        // updateConfiguration
    }

    public static Locale getLocaleByLanguage(String language) {
        //默认英文
        //获取语言状态，得到语言类型，
        Locale locale = Locale.ENGLISH;
        if (language.equalsIgnoreCase(LanguageType.CHINESE.getLanguage())) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else if (language.equalsIgnoreCase(LanguageType.ENGLISH.getLanguage())) {
            locale = Locale.ENGLISH;
        } else if (language.equalsIgnoreCase(LanguageType.THAILAND.getLanguage())) {
            locale = Locale.TRADITIONAL_CHINESE;
        }else if (language.equalsIgnoreCase(LanguageType.HONGKONG.getLanguage())){
            locale = Locale.TRADITIONAL_CHINESE;
        }
        else if (language.equalsIgnoreCase(LanguageType.JAPAN.getLanguage())) {
            locale = Locale.JAPAN;
        } else if (language.equalsIgnoreCase(LanguageType.SPAIN.getLanguage())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                locale = Locale.forLanguageTag(language);
            }
        }else if (language.equalsIgnoreCase(LanguageType.MALAY.getLanguage())){
            locale=new Locale("ms","");
        }else if (language.equalsIgnoreCase(LanguageType.THAI.getLanguage())){
            locale=new Locale("th","TH");
        }else if (language.equalsIgnoreCase(LanguageType.INDIA.getLanguage())){
            locale=new Locale("inc","");
        }else if (language.equalsIgnoreCase(LanguageType.INNID.getLanguage())){
            locale=new Locale("in","ID");
        }else if (language.equalsIgnoreCase(LanguageType.VN.getLanguage())){
            locale=new Locale("vi","VN");
        }else if (language.equalsIgnoreCase(LanguageType.KH.getLanguage())){
            locale=new Locale("km","KH");
        }
        Log.d(TAG, "getLocaleByLanguage: " + locale.getDisplayName());
        return locale;
    }

    //判断系统版本，给出不同处理方式
    public static Context attachBaseContext(Context context, String language) {
        Log.d(TAG, "attachBaseContext: " + Build.VERSION.SDK_INT);
        return updateResources(context, language);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

        } else {
            return context;
        }*/
    }

    public static void setApplicationLanguage(Context context,Locale locale) {
        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            context.getApplicationContext().createConfigurationContext(config);
            Locale.setDefault(locale);
        }
        resources.updateConfiguration(config, dm);
    }

    //加载资源文件
    private static Context updateResources(Context context, String language) {
        if (language.equalsIgnoreCase("1")){
            language=LanguageUtils.getCurLanguage();
        }

        Locale locale = LanguageUtils.getLocaleByLanguage(language);
       /* Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);*/
        //new luoji
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    public static String getCurLanguage(){
        String lan=Locale.getDefault().getLanguage();
        if (lan.startsWith("zh")){
            return "zh-"+Locale.getDefault().getCountry();
        }else {
            return lan;
        }
    }
}
