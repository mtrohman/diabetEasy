package com.gulasehat.android.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.InputDeviceCompat;

import com.gulasehat.android.R;

public class VideoView {
    private static VideoView videoView;
    private AlertDialog dialog;
    private FrameLayout videoLayout;

    public static VideoView getInstance() {
        if (videoView == null) {
            videoView = new VideoView();
        }
        return videoView;
    }

    public void show(Activity activity) {
        dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.DialogTheme);
        View inflate = LayoutInflater.from(activity).inflate(R.layout.layout_video_view, null);
        builder.setView(inflate);
        builder.setCancelable(true);
        this.videoLayout = inflate.findViewById(R.id.videoView);
        this.dialog = builder.create();
        this.dialog.show();
        LayoutParams layoutParams = new LayoutParams();
        Window window = this.dialog.getWindow();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -1;
        window.setAttributes(layoutParams);
        this.dialog.getWindow().setFlags(1024, 1024);
        View decorView = window.getDecorView();
        decorView.setBackgroundResource(R.color.black);
        decorView.setSystemUiVisibility(InputDeviceCompat.SOURCE_TOUCHSCREEN);
    }

    public void dismiss() {
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
    }

    public void setVideoLayout(View view) {
        if (this.videoLayout != null) {
            this.videoLayout.addView(view);
        }
    }


}
