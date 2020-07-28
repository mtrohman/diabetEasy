package com.gulasehat.android.util;

import com.gulasehat.android.model.AppSettings;
import com.google.gson.Gson;

public class Settings {

    private static AppSettings settings;

    public static void setAppSettings(AppSettings appSettings){
        Gson gson = new Gson();
        Preferences.setString(Preferences.APP_SETTINGS_DATA, gson.toJson(appSettings));
        settings = appSettings;
    }

    public static AppSettings getAppSettings(){

        if(settings == null){
            Gson gson = new Gson();
            settings = gson.fromJson(Preferences.getString(Preferences.APP_SETTINGS_DATA, ""), AppSettings.class);
        }

        return settings;
    }
}
