package com.gulasehat.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.request.RequestOptions;
import com.gulasehat.android.adapter.NavigationMenuSection;
import com.gulasehat.android.adapter.ViewPagerAdapter;
import com.gulasehat.android.event.OnArchiveBackEvent;
import com.gulasehat.android.event.OnArchiveChangeEvent;
import com.gulasehat.android.event.OnCategoryBackEvent;
import com.gulasehat.android.event.OnCategoryChangeEvent;
import com.gulasehat.android.event.OnCategoryFlushEvent;
import com.gulasehat.android.event.OnClearAllFavoritesEvent;
import com.gulasehat.android.event.OnFavoriteFiltersEvent;
import com.gulasehat.android.event.ScrollTopEvent;
import com.gulasehat.android.event.filter.ArchiveFiltersEvent;
import com.gulasehat.android.event.filter.AuthorFiltersEvent;
import com.gulasehat.android.event.filter.CategoryFiltersEvent;
import com.gulasehat.android.event.filter.PageFiltersEvent;
import com.gulasehat.android.event.filter.PostFiltersEvent;
import com.gulasehat.android.fragment.ArchiveFragment;
import com.gulasehat.android.fragment.AuthorFragment;
import com.gulasehat.android.fragment.CategoryFragment;
import com.gulasehat.android.fragment.FavoriteFragment;
import com.gulasehat.android.fragment.PageFragment;
import com.gulasehat.android.fragment.PostFragment;
import com.gulasehat.android.model.AppMenu;
import com.gulasehat.android.model.AppMenuItem;
import com.gulasehat.android.model.AppTab;
import com.gulasehat.android.model.AuthUser;
import com.gulasehat.android.service.CommonService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.AuthUtil;
import com.gulasehat.android.util.Constant;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.util.Settings;
import com.gulasehat.android.util.Unit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static com.gulasehat.android.model.AppActionBar.IMAGE_POSITION_CENTER;
import static com.gulasehat.android.model.AppActionBar.IMAGE_POSITION_SIDE;

public class MainActivity extends BaseActivity implements NavigationMenuSection.OnMenuItemClick {

    @BindView(R.id.logo)
    protected ImageView logo;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.viewpager)
    protected ViewPager viewpager;
    @BindView(R.id.drawerLayout)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.tabLayout)
    protected TabLayout tabLayout;
    @BindView(R.id.profileContainer)
    protected LinearLayout profileContainer;
    @BindView(R.id.notLoggedInContainer)
    protected LinearLayout notLoggedInContainer;
    @BindView(R.id.loggedInContainer)
    protected LinearLayout loggedInContainer;
    @BindView(R.id.profilePicture)
    protected ImageView profilePicture;
    @BindView(R.id.profileName)
    protected TextView profileName;
    @BindView(R.id.profileUserName)
    protected TextView profileUserName;
    @BindView(R.id.loginBtn)
    protected Button loginBtn;
    @BindView(R.id.registerBtn)
    protected Button registerBtn;
    @BindView(R.id.menu)
    protected RecyclerView menuRecyclerView;

    //@BindView(R.id.adView)
    //protected AdView adView;

    private boolean subCategoryEnable = false;
    private boolean monthsOfTheYear = false;

    private ActionBar actionBar;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String token = Objects.requireNonNull(task.getResult()).getToken();
                            CommonService.sendPushToken(token);
                        }
                    }
                });

        Analytics.logScreen("Home");

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            invalidateActionBar();

            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.icon_back_white);
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        setupViewPager(viewpager);
        setupNavigationMenu();


    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, R.layout.layout_tab);

        ArrayList<String> tabKeys = new ArrayList<>();
        tabKeys.add(Constant.APP_TAB_HOME);
        pagerAdapter.addFragment(new PostFragment(), R.drawable.icon_tab_home, R.string.title_tab_home);

        for (AppTab tab :
                settings.getAppTabs()) {


            switch (tab.getItemKey()) {
                case Constant.APP_TAB_CATEGORIES:
                    tabKeys.add(Constant.APP_TAB_CATEGORIES);
                    pagerAdapter.addFragment(new CategoryFragment(), R.drawable.icon_tab_category, R.string.title_tab_categories);
                    break;
                case Constant.APP_TAB_FAVORITES:
                    tabKeys.add(Constant.APP_TAB_FAVORITES);
                    pagerAdapter.addFragment(new FavoriteFragment(), R.drawable.icon_tab_favorite, R.string.title_tab_favorites);
                    break;
                case Constant.APP_TAB_PAGES:
                    tabKeys.add(Constant.APP_TAB_PAGES);
                    pagerAdapter.addFragment(new PageFragment(), R.drawable.icon_tab_page, R.string.title_tab_pages);
                    break;
                case Constant.APP_TAB_AUTHORS:
                    tabKeys.add(Constant.APP_TAB_AUTHORS);
                    pagerAdapter.addFragment(new AuthorFragment(), R.drawable.icon_tab_author, R.string.title_tab_authors);
                    break;
                case Constant.APP_TAB_ARCHIVE:
                    tabKeys.add(Constant.APP_TAB_ARCHIVE);
                    pagerAdapter.addFragment(new ArchiveFragment(), R.drawable.icon_tab_archive, R.string.title_tab_archive);
                    break;
            }
        }


        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        if (tabLayout.getTabCount() > 3) {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);

            if (tab != null) {
                tab.setTag(tabKeys.get(i));
                View view = pagerAdapter.getTabView(i);
                if (i > 0) {
                    view.setAlpha(0.5f);
                }
                tab.setCustomView(view);
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    EventBus.getDefault().post(new ScrollTopEvent());
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (subCategoryEnable) {
                    EventBus.getDefault().post(new OnCategoryFlushEvent());
                }

                if (monthsOfTheYear) {
                    EventBus.getDefault().post(new OnArchiveBackEvent());
                }

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

                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    private void setupNavigationMenu() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int statusBarHeightResID = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (statusBarHeightResID > 0) {
                int statusBarHeight = getResources().getDimensionPixelSize(statusBarHeightResID);
                profileContainer.setPadding(0, statusBarHeight, 0, 0);
            }
        }

        SectionedRecyclerViewAdapter navigationRecyclerAdapter = new SectionedRecyclerViewAdapter();


        for (AppMenu menu :
                settings.getAppMenus()) {


            ArrayList<AppMenuItem> menuItems = new ArrayList<>();


            for (AppMenuItem item :
                    menu.getAppMenuItems()) {
                Log.d("burvanli", "item: " + item.getItemKey());

                if (item.getItemKey().equals("nav_update_profile") || item.getItemKey().equals("nav_logout")) {
                    if (AuthUtil.isLoggedIn()) {
                        menuItems.add(item);
                    }
                } else {
                    menuItems.add(item);
                }
            }

            Log.d("burvanli", "cat: " + menu.getCatKey());
            String title = getString(Resource.getStringResID(menu.getCatKey()));
            NavigationMenuSection section = new NavigationMenuSection(title, menuItems);
            section.setOnMenuItemClick(this);
            navigationRecyclerAdapter.addSection(section);

        }

        menuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuRecyclerView.setAdapter(navigationRecyclerAdapter);


        if (AuthUtil.isLoggedIn()) {

            notLoggedInContainer.setVisibility(View.GONE);
            loggedInContainer.setVisibility(View.VISIBLE);

            // Bilgileri doldur

            AuthUser user = AuthUtil.getUser();

            if (user != null) {

                Log.d("burakss", user.getUserPicture());

                //Picasso.get().load(user.getUserPicture()).fit().transform(new CircleTransform()).into(profilePicture);
                GlideApp.with(getApplicationContext()).load(user.getUserPicture()).fitCenter().apply(RequestOptions.circleCropTransform()).into(profilePicture);

                profileName.setText(user.getUserFullName());
                profileUserName.setText(user.getUserName());
            }

        } else {

            loggedInContainer.setVisibility(View.GONE);
            notLoggedInContainer.setVisibility(View.VISIBLE);

            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });

            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                }
            });
        }

    }

    private void actionShare() {

        Analytics.logEvent("Share App");

        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, "I suggest you download " + getString(R.string.app_name) + " Android app. You can download it via this link: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        share.setType("text/plain");
        startActivity(share);
    }

    private void actionRate() {

        Analytics.logEvent("Rate App");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (getCurrentTabKey().equals(Constant.APP_TAB_FAVORITES)) {
            menu.findItem(R.id.action_search).setVisible(false);
            if (AuthUtil.isLoggedIn()) {
                menu.findItem(R.id.action_clear).setVisible(true);
            } else {
                menu.findItem(R.id.action_filter).setVisible(false);
            }
        } else if (getCurrentTabKey().equals(Constant.APP_TAB_CATEGORIES) || getCurrentTabKey().equals(Constant.APP_TAB_ARCHIVE)) {
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.action_clear).setVisible(false);
        } else {
            menu.findItem(R.id.action_clear).setVisible(false);
            menu.findItem(R.id.action_search).setVisible(true);
        }

        return true;
    }

    public String getCurrentTabKey() {
        return (String) Objects.requireNonNull(tabLayout.getTabAt(tabLayout.getSelectedTabPosition())).getTag();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }


                break;
            case R.id.action_filter:


                switch (getCurrentTabKey()) {
                    case Constant.APP_TAB_HOME:
                        Analytics.logEvent("Home Filter Dialog");
                        EventBus.getDefault().post(new PostFiltersEvent());
                        break;
                    case Constant.APP_TAB_CATEGORIES:
                        Analytics.logEvent("Category Filter Dialog");
                        EventBus.getDefault().post(new CategoryFiltersEvent());
                        break;
                    case Constant.APP_TAB_FAVORITES:
                        Analytics.logEvent("Favorite Filter Dialog");
                        EventBus.getDefault().post(new OnFavoriteFiltersEvent());
                        break;
                    case Constant.APP_TAB_AUTHORS:
                        Analytics.logEvent("Author Filter Dialog");
                        EventBus.getDefault().post(new AuthorFiltersEvent());
                        break;
                    case Constant.APP_TAB_PAGES:
                        Analytics.logEvent("Page Filter Dialog");
                        EventBus.getDefault().post(new PageFiltersEvent());
                        break;
                    case Constant.APP_TAB_ARCHIVE:
                        Analytics.logEvent("Archive Filter Dialog");
                        EventBus.getDefault().post(new ArchiveFiltersEvent());
                        break;
                }
                break;
            case R.id.action_clear:
                Analytics.logEvent("Clear All Favorites");
                EventBus.getDefault().post(new OnClearAllFavoritesEvent());
                break;
            case R.id.action_search:
                Intent i = new Intent(this, SearchActivity.class);

                switch (getCurrentTabKey()) {
                    case Constant.APP_TAB_HOME:
                        i.putExtra("post", true);
                        break;
                    case Constant.APP_TAB_PAGES:
                        i.putExtra("page", true);
                        break;
                    case Constant.APP_TAB_AUTHORS:
                        i.putExtra("author", true);
                        break;
                }

                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategoryChangeEvent(OnCategoryChangeEvent event) {
        if (event != null) {
            if (event.getCategory().hasSubCategory()) {

                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                mDrawerToggle.setDrawerIndicatorEnabled(false);

                mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

                logo.setVisibility(View.GONE);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setTitle(event.getCategory().getName());


                subCategoryEnable = true;
            } else {
                subCategoryEnable = false;
            }
        }
    }

    @Override
    protected void onPause() {
        drawerLayout.closeDrawer(GravityCompat.START, false);
        super.onPause();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategoryFlushEvent(OnCategoryFlushEvent event) {
        if (subCategoryEnable) {
            invalidateActionBar();

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.setToolbarNavigationClickListener(null);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            subCategoryEnable = false;
        }

    }

    private void invalidateActionBar() {
        if (settings.getAppActionBar().getStatus().equals("title")) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(settings.getAppActionBar().getTitle());
        } else {
            actionBar.setDisplayShowTitleEnabled(false);
            logo.setVisibility(View.VISIBLE);

            int margin = 18;

            if(settings.getAppActionBar().getImageWidth() == settings.getAppActionBar().getImageHeight()
            || settings.getAppActionBar().getImageWidth() < (settings.getAppActionBar().getImageHeight() * 1.5)){
                Log.d("burakist123", "şartlar sağlanıyor");
                margin = 10;
            }else{
                Log.d("burakist123", "şartları sağlamıyor");
                double ratio = (float)settings.getAppActionBar().getImageWidth() / (float)settings.getAppActionBar().getImageHeight();
                Log.d("burakist123", "width: " + settings.getAppActionBar().getImageWidth());
                Log.d("burakist123", "height: " + settings.getAppActionBar().getImageHeight());
                Log.d("burakist123", "ratio: " + ratio);

                if(ratio > 4){
                    margin = 22;
                }
            }

            Toolbar.LayoutParams params = (Toolbar.LayoutParams) logo.getLayoutParams();
            params.setMargins(params.leftMargin, Unit.dp2px(this, margin), params.rightMargin, Unit.dp2px(this, margin));


            switch (settings.getAppActionBar().getImagePos()) {
                case IMAGE_POSITION_CENTER:
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    break;
                case IMAGE_POSITION_SIDE:
                    params.gravity = Gravity.START;
                    break;
                default:
                    params.gravity = Gravity.CENTER_HORIZONTAL;
            }

            logo.setLayoutParams(params);
            GlideApp.with(this).load(settings.getAppActionBar().getImage()).into(logo);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onArchiveChangeEvent(OnArchiveChangeEvent event) {

        if (event != null) {

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.setDrawerIndicatorEnabled(false);

            mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            logo.setVisibility(View.GONE);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(String.valueOf(event.getYear().getYear()));

            monthsOfTheYear = true;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onArchiveBackEvent(OnArchiveBackEvent event) {

        invalidateActionBar();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mDrawerToggle.setToolbarNavigationClickListener(null);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        monthsOfTheYear = false;

    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        if (monthsOfTheYear) {
            EventBus.getDefault().post(new OnArchiveBackEvent());
            return;
        }

        if (subCategoryEnable) {
            EventBus.getDefault().post(new OnCategoryBackEvent());
            return;
        }

        if (!getCurrentTabKey().equals(Constant.APP_TAB_HOME)) {
            viewpager.setCurrentItem(0);
            return;
        }

        exitApp();
    }


    @OnClick(R.id.profilePicture)
    public void onProfilePictureClick(View view) {

        Analytics.logEvent("Navigation Profile Picture Click");
        Intent intent = new Intent(new Intent(this, EditProfileActivity.class));
        intent.putExtra("photo", true);
        startActivity(intent);
    }

    @Override
    public void onMenuItemClick(AppMenuItem menu) {

        switch (menu.getItemKey()) {
            case Constant.NAV_NOTIFICATION:
                startActivity(new Intent(this, NotificationsActivity.class));
                break;
            case Constant.NAV_UPDATE_PROFILE:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
            case Constant.NAV_LOGOUT:
                AuthUtil.logout();
                drawerLayout.closeDrawer(GravityCompat.START, false);
                if (Settings.getAppSettings().getAppMembershipStatus()) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    recreate();
                }

                break;
            case Constant.NAV_SEARCH:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case Constant.NAV_PAGES:
                startActivity(new Intent(this, PagesActivity.class));
                break;
            case Constant.NAV_AUTHORS:
                startActivity(new Intent(this, AuthorsActivity.class));
                break;
            case Constant.NAV_ARCHIVE:
                startActivity(new Intent(this, ArchiveActivity.class));
                break;
            case Constant.NAV_TAG_CLOUD:
                startActivity(new Intent(this, TagCloudActivity.class));
                break;
            case Constant.NAV_SOCIAL_ACCOUNTS:
                startActivity(new Intent(this, SocialAccountsActivity.class));
                break;
            case Constant.NAV_CONTACT:
                startActivity(new Intent(this, ContactActivity.class));
                break;
            case Constant.NAV_SHARE_THIS_APP:
                actionShare();
                break;
            case Constant.NAV_RATE_THIS_APP:
                actionRate();
                break;
            case Constant.NAV_ABOUT_US:
                startActivity(new Intent(MainActivity.this, PostActivity.class).putExtra("id", settings.getAppAboutUsPageID()));
                break;
            case Constant.NAV_FAQ:
                startActivity(new Intent(this, FaqActivity.class));
                break;
            case Constant.NAV_SETTINGS:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case Constant.NAV_CATEGORIES:
                startActivity(new Intent(this, CategoriesActivity.class));
                break;
            case Constant.NAV_FAVORITES:
                startActivity(new Intent(this, FavoritesActivity.class));
                break;


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupNavigationMenu();
    }
}
