package com.gulasehat.android.util;

import android.app.Application;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Analytics {

    private static FirebaseAnalytics mFirebaseAnalytics;

    public static void init(Application application){
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(application);
    }

    private static boolean isAvailable(){
        if(mFirebaseAnalytics == null){
            return false;
        }
        return true;
    }

    public static void logScreen(String screenName){
        String snakeCase = "screen_" + screenName.toLowerCase().replace(" ","_");
        if(isAvailable()) {
            Bundle bundle = new Bundle();
            mFirebaseAnalytics.logEvent(snakeCase , bundle);
        }
    }

    public static void logEvent(String eventName){
        String snakeCase = eventName.toLowerCase().replace(" ", "_");
        if(isAvailable()) {
            Bundle bundle = new Bundle();
            mFirebaseAnalytics.logEvent(snakeCase, bundle);
        }
    }
}
