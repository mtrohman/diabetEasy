package com.gulasehat.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gulasehat.android.model.AppSettings;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.Settings;

import butterknife.BindView;
import butterknife.OnClick;

public class StartActivity extends BaseActivity {

    @BindView(R.id.logo)
    protected ImageView logo;
    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.content)
    protected TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Analytics.logScreen("Start");

        AppSettings settings = Settings.getAppSettings();
        if (settings != null) {
            //Picasso.get().load(settings.getAppStartPage().getLogo()).into(logo);
            GlideApp.with(this).load(settings.getAppStartPage().getLogo()).into(logo);
            title.setText(settings.getAppStartPage().getTitle());
            content.setText(settings.getAppStartPage().getContent());
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_start;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    @OnClick(R.id.login)
    public void loginClick() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.register)
    public void registerClick() {
        startActivity(new Intent(this, RegisterActivity.class));
    }


    @Override
    public void onBackPressed() {

        exitApp();

    }
}
