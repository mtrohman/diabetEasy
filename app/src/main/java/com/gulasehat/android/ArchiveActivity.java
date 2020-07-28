package com.gulasehat.android;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;

import com.gulasehat.android.event.OnArchiveBackEvent;
import com.gulasehat.android.event.filter.ArchiveFiltersEvent;
import com.gulasehat.android.util.Analytics;

import org.greenrobot.eventbus.EventBus;

public class ArchiveActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Analytics.logScreen("Archive");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.nav_archive);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_filter:
                EventBus.getDefault().post(new ArchiveFiltersEvent());
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new OnArchiveBackEvent());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_archive;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

}