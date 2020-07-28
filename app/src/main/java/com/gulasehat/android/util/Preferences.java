package com.gulasehat.android.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

public class Preferences {


    public static final String LAST_CLOSED_ACTIVITY = "last_closed_activity";
    public static final String COUNTRY_CODE = "country_code";
    private static SharedPreferences mSharedPreferences;
    public static final String REGISTER_ID = "register_id";
    public static final String THEME = "theme";
    public static final String THEME_DEFAULT = "theme_default";
    public static final String LANGUAGE_DEFAULT = "language_default";
    public static final String DEVICE_ID = "device_id";

    public static final String SETTINGS_NOTIFY_STATUS = "settings_notify_status";
    public static final String SETTINGS_NOTIFY_SOUND = "settings_notify_sound";
    public static final String SETTINGS_NOTIFY_VIBRATE = "settings_notify_vibrate";
    public static final String SETTINGS_APP_LANG = "settings_app_lang";
    public static final String CURRENT_APP_LANG_CODE = "current_app_lang_code";
    public static final String CURRENT_APP_LANG_NAME = "current_app_lang_name";


    public static final String APP_SETTINGS_DATA = "app_settings_data";

    public static final String SETTINGS_PROFILE_EMAIL = "settings_profile_email";
    public static final String SETTINGS_PROFILE_WEBSITE = "settings_profile_website";
    public static final String SETTINGS_PROFILE_GENDER = "settings_profile_gender";
    public static final String SETTINGS_PROFILE_JOB = "settings_profile_job";
    public static final String SETTINGS_PROFILE_BIO = "settings_profile_bio";

    public static final String MEMBERSHIP = "membership";

    public static final String SEARCH_QUERY = "search_query";

    public static final String TOKEN = "token";
    public static final String IS_LOGGED_IN = "is_logged_in";
    public static final String LOGGED_IN_USER = "logged_in_user";

    public static final String LAST_LATITUDE = "last_latitude";
    public static final String LAST_LONGITUDE = "last_longitude";




    public static void init(Context context){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static boolean getBoolean(String key, boolean defaultValue){
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public static void setBoolean(String key, boolean newValue){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, newValue);
        editor.apply();
    }

    public static int getInt(String key, int defaultValue){
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public static void setInt(String key, int newValue){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, newValue);
        editor.apply();
    }

    public static String getString(String key, String defaultvalue){
        return mSharedPreferences.getString(key, defaultvalue);
    }

    public static void setString(String key, String newValue){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, newValue);
        editor.apply();
    }

    public static long getLong(String key, long defaultValue){
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public static void setLong(String key, long newValue){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key, newValue);
        editor.apply();
    }

    public static Map<String, ?> getAll(){
        return mSharedPreferences.getAll();
    }

    public static void remove(String key){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void clearAll(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}