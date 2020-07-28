package com.gulasehat.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.gulasehat.android.model.Notification;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.Resource;
import com.google.gson.Gson;

import butterknife.BindView;

public class NotificationActivity extends BaseActivity {

    @BindView(R.id.iconContainer)
    protected LinearLayout iconContainer;
    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.content)
    protected TextView content;
    @BindView(R.id.linkButton)
    protected Button linkButton;
    @BindView(R.id.date)
    protected TextView date;

    private Notification notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Gson gson = new Gson();

        Intent intent = getIntent();

        Analytics.logScreen("Notification");

        notification = gson.fromJson(intent.getStringExtra("data"), Notification.class);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(notification.getTitle());
        }


        prepareUI();


    }

    private void prepareUI() {
        Resource.colorizeByTheme(iconContainer.getBackground());
        title.setText(notification.getTitle());
        content.setText(notification.getContent());
        date.setText(notification.getTimeFormatted());

        if(! notification.getLink().equals("")){
            linkButton.setVisibility(View.VISIBLE);
            linkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Analytics.logEvent("Notification Link Click");
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(notification.getLink()));
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_notification;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }
}
