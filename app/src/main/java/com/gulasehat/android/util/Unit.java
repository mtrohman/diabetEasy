package com.gulasehat.android.util;

import android.content.Context;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Unit {

    public static float dpToPixels(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().density;
        return px * scaledDensity;
    }

    public static int dp2px(Context context, float dp){
        /*DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / dp);
        return Math.round(px);*/

        return Math.round(dp*(context.getResources().getDisplayMetrics().xdpi/DisplayMetrics.DENSITY_DEFAULT));
    }

    public static String secondsFormatted(int millis){

        return (new SimpleDateFormat("mm:ss", Locale.getDefault())).format(new Date(millis));

    }

}
