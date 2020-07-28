package com.gulasehat.android;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;

import com.gulasehat.android.event.OnClearAllFavoritesEvent;
import com.gulasehat.android.event.OnFavoriteFiltersEvent;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.AuthUtil;

import org.greenrobot.eventbus.EventBus;

public class FavoritesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.nav_favorites);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);

        if (!AuthUtil.isLoggedIn()) {
            menu.findItem(R.id.action_clear).setVisible(false);
            menu.findItem(R.id.action_filter).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        else if(item.getItemId() == R.id.action_filter){
            Analytics.logEvent("Favorites Filter Dialog");
            EventBus.getDefault().post(new OnFavoriteFiltersEvent());
        }
        else if(item.getItemId() == R.id.action_clear){
            Analytics.logEvent("Clear All Favorites");
            EventBus.getDefault().post(new OnClearAllFavoritesEvent());
        }

        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_favorites;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }
}
