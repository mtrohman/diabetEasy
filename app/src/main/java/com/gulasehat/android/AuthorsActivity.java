package com.gulasehat.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;

import com.gulasehat.android.event.OnFetchedAuthorData;
import com.gulasehat.android.event.filter.AuthorFiltersEvent;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.Settings;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AuthorsActivity extends BaseActivity {

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Analytics.logScreen("Authors");

        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.nav_authors);
        }

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFetchedAuthorData(OnFetchedAuthorData event){
        if(actionBar != null){
            if(event.getData().getPostCountStatus()){
                String title = getString(R.string.nav_authors) + " (" + event.getData().getTotalData() + ")";
                actionBar.setTitle(title);
            }else{
                actionBar.setTitle(R.string.nav_authors);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_sub, menu);
        if(!Settings.getAppSettings().getAppSearch().isAuthorListng()){
            menu.findItem(R.id.action_search).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_search:
                Intent i = new Intent(this, SearchActivity.class);
                i.putExtra("author", true);
                startActivity(i);
                break;
            case R.id.action_filter:
                EventBus.getDefault().post(new AuthorFiltersEvent());
                break;
        }

        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_authors;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }
}
