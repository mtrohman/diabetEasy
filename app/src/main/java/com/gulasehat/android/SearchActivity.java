package com.gulasehat.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.gulasehat.android.adapter.ViewPagerAdapter;
import com.gulasehat.android.event.OnSearchResultEvent;
import com.gulasehat.android.event.OnSearchingEvent;
import com.gulasehat.android.event.filter.AuthorFiltersEvent;
import com.gulasehat.android.event.filter.PageFiltersEvent;
import com.gulasehat.android.event.filter.PostFiltersEvent;
import com.gulasehat.android.fragment.SearchAuthorFragment;
import com.gulasehat.android.fragment.SearchPageFragment;
import com.gulasehat.android.fragment.SearchPostFragment;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.SearchArgs;
import com.gulasehat.android.model.SearchResult;
import com.gulasehat.android.service.CommonService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.Preferences;
import com.gulasehat.android.util.Settings;
import com.gulasehat.android.widget.Alert;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.viewpager)
    protected ViewPager viewpager;
    @BindView(R.id.tabLayout)
    protected TabLayout tabLayout;
    @BindView(R.id.search)
    protected EditText search;


    private ActionBar actionBar;
    private ViewPagerAdapter pagerAdapter;
    private ArrayList<String> searchHints = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Analytics.logScreen("Search");

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setupViewPager(viewpager);

        setListeners();

        handleIntent();


    }

    private void handleIntent() {
        Intent intent = getIntent();

        if(intent.hasExtra("post")){
            viewpager.setCurrentItem(0);
        }

        if(intent.hasExtra("page")){
            viewpager.setCurrentItem(1);
        }

        if(intent.hasExtra("author")){
            viewpager.setCurrentItem(2);
        }
    }

    private void setupViewPager(ViewPager viewpager) {

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, R.layout.layout_tab);

        if(Settings.getAppSettings().getAppSearch().isPostListing()){
            searchHints.add(getString(R.string.search_on_posts));
            pagerAdapter.addFragment(new SearchPostFragment(), R.drawable.icon_tab_post, R.string.title_tab_posts);
        }
        if(Settings.getAppSettings().getAppSearch().isPageListing()){
            searchHints.add(getString(R.string.search_on_pages));
            pagerAdapter.addFragment(new SearchPageFragment(), R.drawable.icon_tab_page, R.string.title_tab_pages);
        }
        if(Settings.getAppSettings().getAppSearch().isAuthorListng()){
            searchHints.add(getString(R.string.search_authors));
            pagerAdapter.addFragment(new SearchAuthorFragment(), R.drawable.icon_tab_author, R.string.title_tab_authors);
        }

        if(!Settings.getAppSettings().getAppSearch().isPostListing()
        && !Settings.getAppSettings().getAppSearch().isPageListing()
        && !Settings.getAppSettings().getAppSearch().isAuthorListng()){
            searchHints.add(getString(R.string.search_on_posts));
            pagerAdapter.addFragment(new SearchPostFragment(), R.drawable.icon_tab_post, R.string.title_tab_posts);
        }

        search.setHint(searchHints.get(0));


        viewpager.setOffscreenPageLimit(3);
        viewpager.setAdapter(pagerAdapter);


        tabLayout.setupWithViewPager(viewpager);


        if(pagerAdapter.getCount() == 1){
            tabLayout.setVisibility(View.GONE);
        }

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if(tab != null){
                View view = pagerAdapter.getTabView(i);
                if(i > 0){
                    view.setAlpha(0.5f);
                }
                tab.setCustomView(view);
            }
        }

    }

    private void setListeners(){

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    actionSearch();
                    return true;
                }
                return false;
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                search.setHint(searchHints.get(position));


                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if(tab != null && tab.getCustomView() != null){
                        tab.getCustomView().setAlpha(0.5f);
                    }
                }
                TabLayout.Tab selectedTab = tabLayout.getTabAt(position);
                if(selectedTab != null && selectedTab.getCustomView() != null){
                    selectedTab.getCustomView().setAlpha(1f);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void actionSearch() {

        final String q = search.getText().toString().trim();

        if(q.length() < Settings.getAppSettings().getAppSearch().getMinimumCharacter()){
            String message = getString(R.string.invalid_character_limit);
            message = message.replace("3", String.valueOf(Settings.getAppSettings().getAppSearch().getMinimumCharacter()));
            message = message.replace("تین", String.valueOf(Settings.getAppSettings().getAppSearch().getMinimumCharacter()));
            Alert.make(this, message, Alert.ALERT_TYPE_WARNING);
            return;
        }

        SearchArgs args = new SearchArgs();
        args.setPage(1);
        args.setQuery(q);
        args.setType("all");

        EventBus.getDefault().post(new OnSearchingEvent());

        Preferences.setString(Preferences.SEARCH_QUERY, q);

        CommonService.search(args, new CommonService.OnSearchResultListener() {
            @Override
            public void onSuccess(SearchResult result) {
                result.setQuery(q);
                EventBus.getDefault().post(new OnSearchResultEvent(result));
            }

            @Override
            public void onFailed(ApiResponse response) {
                Alert.make(SearchActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sub, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_search:
                actionSearch();
                break;
            case R.id.action_filter:


                switch (tabLayout.getSelectedTabPosition()){
                    case 0:
                        EventBus.getDefault().post(new PostFiltersEvent());
                        break;
                    case 1:
                        EventBus.getDefault().post(new PageFiltersEvent());
                        break;
                    case 2:
                        EventBus.getDefault().post(new AuthorFiltersEvent());
                        break;

                }
                break;
        }

        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_search;
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
