package com.gulasehat.android;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.gulasehat.android.adapter.ViewPagerAdapter;
import com.gulasehat.android.event.OnProfileSettingsFetchedEvent;
import com.gulasehat.android.fragment.settings.SettingsAppFragment;
import com.gulasehat.android.fragment.settings.SettingsProfileFragment;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.ProfileSettings;
import com.gulasehat.android.service.AuthService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.Preferences;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.viewpager)
    protected ViewPager viewpager;
    @BindView(R.id.tabLayout)
    protected TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Analytics.logScreen("Settings");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.nav_settings);
        }

        setupViewPager(viewpager);


        initialize();

    }

    private void initialize() {

        if(Preferences.getBoolean(Preferences.IS_LOGGED_IN, false)){
            showProgress();


            AuthService.getProfileSettings(new AuthService.OnProfileSettingsFetchedListener() {
                @Override
                public void onSuccess(ProfileSettings settings) {

                    Preferences.setBoolean(Preferences.SETTINGS_PROFILE_EMAIL, settings.isEmail());
                    Preferences.setBoolean(Preferences.SETTINGS_PROFILE_WEBSITE, settings.isWebsite());
                    Preferences.setBoolean(Preferences.SETTINGS_PROFILE_GENDER, settings.isGender());
                    Preferences.setBoolean(Preferences.SETTINGS_PROFILE_JOB, settings.isJob());
                    Preferences.setBoolean(Preferences.SETTINGS_PROFILE_BIO, settings.isBio());

                    EventBus.getDefault().post(new OnProfileSettingsFetchedEvent());

                    hideProgress();
                }

                @Override
                public void onFailed(ApiResponse response) {
                    hideProgress();
                }
            });
        }

    }

    private void setupViewPager(ViewPager viewpager) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, R.layout.layout_tab);

        pagerAdapter.addFragment(new SettingsAppFragment(), R.drawable.icon_tab_settings_app_icon, R.string.app);
        pagerAdapter.addFragment(new SettingsProfileFragment(), R.drawable.icon_tab_settings_profile_icon, R.string.profile);

        viewpager.setOffscreenPageLimit(2);
        viewpager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewpager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View view = pagerAdapter.getTabView(i);
                if (i > 0) {
                    view.setAlpha(0.5f);
                }
                tab.setCustomView(view);
            }
        }

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if (tab != null && tab.getCustomView() != null) {
                        tab.getCustomView().setAlpha(0.5f);
                    }
                }
                TabLayout.Tab selectedTab = tabLayout.getTabAt(position);
                if (selectedTab != null && selectedTab.getCustomView() != null) {
                    selectedTab.getCustomView().setAlpha(1f);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    private void saveSettings(){

        ProfileSettings settings = new ProfileSettings();

        settings.setEmail(Preferences.getBoolean(Preferences.SETTINGS_PROFILE_EMAIL, true));
        settings.setWebsite(Preferences.getBoolean(Preferences.SETTINGS_PROFILE_WEBSITE, true));
        settings.setGender(Preferences.getBoolean(Preferences.SETTINGS_PROFILE_GENDER, true));
        settings.setJob(Preferences.getBoolean(Preferences.SETTINGS_PROFILE_JOB, true));
        settings.setBio(Preferences.getBoolean(Preferences.SETTINGS_PROFILE_BIO, true));

        AuthService.updateProfileSettings(settings, new AuthService.OnProfileSettingsUpdateListener() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                Log.d("burax", "ayarlar kaydedildi");
            }

            @Override
            public void onFailed(ApiResponse apiResponse) {
                Log.d("burax", "ayar kaydetmede hata!");
            }
        });

    }

    @Override
    protected void onPause() {
        saveSettings();
        super.onPause();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    @Override
    public void onBackPressed() {

        if(viewpager.getCurrentItem() > 0){
            viewpager.setCurrentItem(0);
            return;
        }

        super.onBackPressed();

    }
}
