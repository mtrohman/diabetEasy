package com.gulasehat.android.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gulasehat.android.FlinkApplication;
import com.gulasehat.android.R;
import com.gulasehat.android.util.Resource;

public class Alert {

    public static final int ALERT_TYPE_SUCCESS = 10;
    public static final int ALERT_TYPE_WARNING = 20;

    public static void make(Context context, String key, int alertType) {
        int resourceId = Resource.getStringResID(key);
        String message;

        if(resourceId != 0){
            message = context.getString(resourceId);
        }else{
            message = key;
        }
        build(context, message, alertType, null);
    }

    public static void make(Context context, @StringRes int resource, int alertType){
        build(context, context.getString(resource), alertType, null);
    }

    public static void make(Context context, String key, int alertType, OnButtonClickListener onButtonClickListener){
        build(context, context.getString(Resource.getStringResID(key)), alertType, onButtonClickListener);
    }
    public static void make(Context context, @StringRes int resource, int alertType, OnButtonClickListener onButtonClickListener){
        build(context, context.getString(resource), alertType, onButtonClickListener);
    }

    private static void build(Context context, String message, int alertType, @Nullable final OnButtonClickListener onButtonClickListener){



        if(FlinkApplication.isIsDialogShown()){
            Log.d("burkitest", "diyalog açık");

            return;
        }


        FlinkApplication.setDialogShown(true);

        Log.d("burkitest", "isShown: " + FlinkApplication.isIsDialogShown());


        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        FlinkApplication.setDialogShown(true);
                    }
                })
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        FlinkApplication.setDialogShown(false);
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        FlinkApplication.setDialogShown(false);
                    }
                })
                .customView(R.layout.layout_dialog, false).build();

        ImageView icon = dialog.getCustomView().findViewById(R.id.icon);
        TextView text = dialog.getCustomView().findViewById(R.id.text);
        Button btn = (Button) dialog.getCustomView().findViewById(R.id.ok);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(onButtonClickListener != null){
                    onButtonClickListener.onClick();
                }
            }
        });

        text.setText(message);

        switch (alertType) {
            case ALERT_TYPE_SUCCESS:
                icon.setImageResource(R.drawable.ic_success);
                btn.setBackgroundResource(R.drawable.button_success);
                break;
            case ALERT_TYPE_WARNING:
            default:
                icon.setImageResource(R.drawable.ic_warning);
                btn.setBackgroundResource(R.drawable.button_warning);
        }

        if(! message.isEmpty()){
            dialog.show();
        }

    }

    public interface OnButtonClickListener{
        void onClick();
    }

}
