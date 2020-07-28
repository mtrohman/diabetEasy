package com.gulasehat.android.util;

import android.content.Context;

import com.gulasehat.android.model.AppSettings;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DateUtil {

    public static String getMonthName(int month){
        try{
            return Resource.getString(Resource.getStringResID("month_" + month));
        }catch (Exception e){
            return "";
        }
    }

    public static String getFormattedDate(Context context, long millis){

        AppSettings settings = Settings.getAppSettings();

        if(settings == null){
            return "";
        }

        try{
            if(Objects.requireNonNull(settings).getAppDate().getType().equals(Constant.DATE_TIME_ELAPSED)){

                PrettyTime p = new PrettyTime(Locale.getDefault());
                return p.format(new Date(millis));

            }else{

                Date date = new Date(millis);
                DateFormat formatter = new SimpleDateFormat(context.getString(Resource.getStringResID(settings.getAppDate().getType())), Locale.getDefault());
                return formatter.format(date);

            }
        }catch (Exception e){
            return "";
        }





    }

}
