package com.gulasehat.android;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gulasehat.android.adapter.ThemeRecyclerAdapter;
import com.gulasehat.android.service.CommonService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.Resource;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class ThemesActivity extends BaseActivity implements ThemeRecyclerAdapter.OnThemeClickListener {

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;

    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Analytics.logScreen("Themes");

        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.themes);
        }


        setupThemes();
    }

    private void setupThemes() {

        List<String> themes =  Arrays.asList(getResources().getStringArray(R.array.themes));

        ThemeRecyclerAdapter themeRecyclerAdapter = new ThemeRecyclerAdapter(this, themes);
        themeRecyclerAdapter.setOnThemeClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(themeRecyclerAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_themes;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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
    protected void onPause() {
        CommonService.updateAppTheme(Resource.getAppTheme());
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }

    @Override
    public void onClicked(String theme) {

        Resource.setAppTheme(theme);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(Resource.getColorPrimaryDark()));
        }

        if (actionBar != null) {
            Drawable drawable = new ColorDrawable(ContextCompat.getColor(this, Resource.getColorPrimary()));
            actionBar.setBackgroundDrawable(drawable);
        }
    }
}
