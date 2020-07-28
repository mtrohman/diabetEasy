package com.gulasehat.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.gulasehat.android.R;

public class Resource {

    private static Context context;

    public static void init(Context mContext) {
        context = mContext;
    }

    public static String getString(int resId) {
        return context.getString(resId);
    }

    public static String getString(int resId, String value) {
        return context.getString(resId, value);
    }

    public static String getAppTheme(){
        return Preferences.getString(Preferences.THEME, Preferences.getString(Preferences.THEME_DEFAULT, "Blue"));
    }

    public static void setAppTheme(String theme){
        Preferences.setString(Preferences.THEME, theme);
    }

    public static int getStringResID(String stringName){

        try{
            String packageName = context.getPackageName();
            return context.getResources().getIdentifier(stringName, "string", packageName);
        }catch (Exception e){
            return 0;
        }
    }

    public static int getDrawableResID(String drawableName){

        try{
            String packageName = context.getPackageName();
            return context.getResources().getIdentifier(drawableName, "drawable", packageName);
        }catch (Exception e){
            return 0;
        }
    }

    public static int getColorResID(String colorName){

        try{
            String packageName = context.getPackageName();
            return context.getResources().getIdentifier(colorName, "color", packageName);
        }catch (Exception e){
            return 0;
        }
    }

    public static int getStyleResID(String styleName){

        try{
            String packageName = context.getPackageName();
            return context.getResources().getIdentifier(styleName, "style", packageName);
        }catch (Exception e){
            return 0;
        }
    }

    public static String getColorPrimaryHexCode(){
        return "#"+Integer.toHexString(ContextCompat.getColor(context, getColorPrimary()));
    }

    public static String getColorWhiteHexCode(){
        return "#"+Integer.toHexString(ContextCompat.getColor(context, R.color.white));
    }

    public static int getColorPrimary(){
        return getColorResID(getAppTheme() + "ColorPrimary");
    }

    public static int getColorPrimaryDark(){
        return getColorResID(getAppTheme() + "ColorPrimaryDark");
    }

    public static int getColorAccent(){
        return getColorResID(getAppTheme() + "ColorAccent");
    }

    public static void colorizeByTheme(Drawable drawable){ ;
        drawable.setColorFilter(ContextCompat.getColor(context, getColorPrimary()), PorterDuff.Mode.SRC_ATOP);
    }

    public static Drawable colorizeByTheme(int resourceID){
        Drawable drawable = ContextCompat.getDrawable(context, resourceID);
        if(drawable != null){
            drawable.setColorFilter(ContextCompat.getColor(context, getColorPrimary()), PorterDuff.Mode.DST_ATOP);
        }

        return drawable;
    }

    public static int getCurrentThemeStyle(Activity activity){

        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[] {R.attr.windowActionBar, android.R.attr.windowIsTranslucent});
        boolean hasActionBar = a.getBoolean(0, true);
        boolean isTranslucent = a.getBoolean(1, false);
        a.recycle();

        String styleName;

        if(isTranslucent){
            styleName = "Translucent";
        }else if(hasActionBar){
            styleName = "AppTheme";
        }else{
            styleName = "AppThemeNoActionBar";
        }

        return getStyleResID(getAppTheme() + styleName);
    }

}