package com.gulasehat.android;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.gulasehat.android.event.OnCategoryBackEvent;
import com.gulasehat.android.event.OnCategoryChangeEvent;
import com.gulasehat.android.event.OnCategoryFlushEvent;
import com.gulasehat.android.event.OnFetchedCategoryData;
import com.gulasehat.android.event.filter.CategoryFiltersEvent;
import com.gulasehat.android.util.Analytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;


public class CategoriesActivity extends BaseActivity {

    private ActionBar actionBar;
    private boolean subCategoryEnable = false;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    protected TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back_white);
        }
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

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFetchedCategoryData(OnFetchedCategoryData event){
        String title = getString(R.string.nav_categories);
        if(event.getCategoryData().getParent() != null){
            title = event.getCategoryData().getParent().getName();
        }

        if(event.getCategoryData().getTotalDataCountStatus()){
            if(event.getCategoryData().getParent() != null){
                title = event.getCategoryData().getParent().getName();
                if(event.getCategoryData().getTotalDataCountStatus()){
                    title = title + " (" + event.getCategoryData().getTotalData() + ")";
                }
            }
        }
        checkTitleWeight(title);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_categories;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    @Override
    public void onBackPressed() {

        if (subCategoryEnable) {
            EventBus.getDefault().post(new OnCategoryBackEvent());
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        else if(item.getItemId() == R.id.action_filter){
            Analytics.logEvent("Category Filter Dialog");
            EventBus.getDefault().post(new CategoryFiltersEvent());
        }

        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategoryChangeEvent(OnCategoryChangeEvent event) {
        if (event != null) {
            if (event.getCategory().hasSubCategory()) {
                subCategoryEnable = true;
            } else {
                subCategoryEnable = false;
            }
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategoryFlushEvent(OnCategoryFlushEvent event) {
        if (subCategoryEnable) {
            subCategoryEnable = false;
            toolbarTitle.setText(R.string.nav_categories);
        }

    }
}
