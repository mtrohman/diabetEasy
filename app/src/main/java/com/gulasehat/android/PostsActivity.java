package com.gulasehat.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.gulasehat.android.event.OnFetchedPostData;
import com.gulasehat.android.event.filter.PostFiltersEvent;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.Settings;
import com.gulasehat.android.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class PostsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    protected TextView toolbarTitle;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Analytics.logScreen("Posts");

        Intent intent = getIntent();
        title = Util.getCapsSentences(intent.getStringExtra("title"));
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back_white);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //checkTitleWeight(title);

    }

    private void checkTitleWeight(final String title){
        if(toolbarTitle.getText().equals(title)){
            return;
        }
        toolbarTitle.setVisibility(View.GONE);
        toolbarTitle.post(new Runnable() {
            @Override
            public void run() {

                toolbarTitle.setText(title);
                if(toolbarTitle.getLineCount() > 1){
                    toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }else{
                    toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                }
                toolbarTitle.setVisibility(View.VISIBLE);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFetchedPostData(OnFetchedPostData event){
        String title = this.title;
        if(event.getData().getPostCountStatus()){
            title = this.title + " (" + event.getData().getPostCount() + ")";
        }

        checkTitleWeight(title);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_posts;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_sub, menu);
        if(!Settings.getAppSettings().getAppSearch().isPostListing()){
            menu.findItem(R.id.action_search).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_search:
                Intent i = new Intent(this, SearchActivity.class);
                i.putExtra("post", true);
                startActivity(i);
                return true;
            case R.id.action_filter:
                EventBus.getDefault().post(new PostFiltersEvent());
                return true;
        }



        return super.onOptionsItemSelected(item);
    }

}
