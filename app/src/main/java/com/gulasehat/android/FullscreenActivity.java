package com.gulasehat.android;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.gulasehat.android.event.OnCloseFullScreen;
import com.gulasehat.android.event.OnOpenFullScreen;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class FullscreenActivity extends BaseActivity {

    @BindView(R.id.frameLayout)
    protected FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_fullscreen;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTest(OnOpenFullScreen event){
        Log.d("burakist", "onOpenFullScreen tetiklendi");

        frameLayout.addView(event.getView());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTest2(OnCloseFullScreen event){
        Log.d("burakist", "onCloseFullScreen tetiklendi");

        //frameLayout.removeView(event.getView());
        //finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
