package com.gulasehat.android;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.gulasehat.android.model.Notification;
import com.gulasehat.android.service.CommonService;
import com.gulasehat.android.util.Preferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Date;
import java.util.Map;

public class FlinkMessagingService extends FirebaseMessagingService {

    private static final String MANUEL = "0";
    private static final String POST = "1";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        if(! Preferences.getString(Preferences.REGISTER_ID, "").equals("")){
            CommonService.sendPushToken(s);
        }

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> data = remoteMessage.getData();

        if (data == null)
            return;

        String channelId = "Fyncode" + new Date().getTime();


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.drawable.icon_notify_white)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setAutoCancel(true)
                .setContentTitle(data.get("title"))
                .setContentText(data.get("content"));

        if(!Preferences.getBoolean(Preferences.SETTINGS_NOTIFY_SOUND, true)){
            builder.setSound(null);
        }
        if(!Preferences.getBoolean(Preferences.SETTINGS_NOTIFY_VIBRATE, true)){
            builder.setVibrate(null);
        }

        if(data.get("type").equals(POST)){
            Intent postIntent = new Intent(getApplicationContext(), PostActivity.class);
            postIntent.putExtra("id", Integer.valueOf(data.get("id")));
            builder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, postIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
        else if(data.get("type").equals(MANUEL)){

            if(data.get("link").equals("")){

                Gson gson = new Gson();

                Notification notify = new Notification();
                notify.setId(Integer.parseInt(data.get("id")));
                notify.setContent(data.get("content"));
                notify.setLink(data.get("link"));
                notify.setTitle(data.get("title"));
                notify.setTime(Long.parseLong(data.get("time")));

                Intent notifyIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                notifyIntent.putExtra("data", gson.toJson(notify));
                builder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT));

            }else{
                builder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(Intent.ACTION_VIEW, Uri.parse(data.get("link"))).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_UPDATE_CURRENT));
            }
        }

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {

                NotificationChannel channel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);

                if(!Preferences.getBoolean(Preferences.SETTINGS_NOTIFY_VIBRATE, true)){
                    channel.setVibrationPattern(new long[]{ 0 });
                    channel.enableVibration(true);
                }
                else{
                    channel.enableVibration(true);
                }
                if(!Preferences.getBoolean(Preferences.SETTINGS_NOTIFY_SOUND, true)){
                    channel.setSound(null, null);
                }

                mNotificationManager.createNotificationChannel(channel);
            }
            if(!Preferences.getBoolean(Preferences.SETTINGS_NOTIFY_STATUS, true)){
                return;
            }
            mNotificationManager.notify((int) new Date().getTime(), builder.build());
        }


    }
}
