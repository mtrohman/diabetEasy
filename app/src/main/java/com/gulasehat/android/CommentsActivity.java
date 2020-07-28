package com.gulasehat.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.gulasehat.android.adapter.ViewPagerAdapter;
import com.gulasehat.android.event.OnCommentFiltersEvent;
import com.gulasehat.android.event.OnCommentSendEvent;
import com.gulasehat.android.event.OnCommentSentEvent;
import com.gulasehat.android.event.OnCommentsRefreshEvent;
import com.gulasehat.android.event.OnNewCommentVisibleEvent;
import com.gulasehat.android.event.SwitchNewCommentTabEvent;
import com.gulasehat.android.util.Analytics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class CommentsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.viewpager)
    protected ViewPager viewpager;
    @BindView(R.id.tabLayout)
    protected TabLayout tabLayout;
    @BindView(R.id.send)
    protected FloatingActionButton send;

    protected CommentsFragment commentsFragment;

    private Animation showFabAnimation;
    private Animation hideFabAnimation;
    private int postID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Analytics.logScreen("Comments");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.comments);
        }

        Intent intent = getIntent();
        postID = intent.getIntExtra("id", 0);

        showFabAnimation = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_show);
        hideFabAnimation = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_hide);

        showFabAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                send.show();
                send.setClickable(true);
                send.setFocusable(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        hideFabAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                send.setClickable(false);
                send.setFocusable(false);
                send.hide();
                animation.cancel();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        setupViewPager();


    }

    private void setupViewPager() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, R.layout.layout_tab);


        Bundle data = new Bundle();
        data.putInt("id", postID);

        commentsFragment = new CommentsFragment();
        commentsFragment.setArguments(data);

        NewCommentFragment newCommentFragment = new NewCommentFragment();
        newCommentFragment.setArguments(data);

        pagerAdapter.addFragment(commentsFragment, R.drawable.icon_tab_comment, R.string.comments);
        pagerAdapter.addFragment(newCommentFragment, R.drawable.icon_tab_new_comment, R.string.write_comment);

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

                switch (position){
                    case 0:
                        send.startAnimation(hideFabAnimation);
                        EventBus.getDefault().post(new OnNewCommentVisibleEvent(false));
                        break;
                    case 1:
                        send.startAnimation(showFabAnimation);
                        EventBus.getDefault().post(new OnNewCommentVisibleEvent(true));
                        break;
                }
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentSentEvent(OnCommentSentEvent event){
        EventBus.getDefault().post(new OnCommentsRefreshEvent());
        viewpager.setCurrentItem(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchNewCommentTabEvent(SwitchNewCommentTabEvent event){
        viewpager.setCurrentItem(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);

        if(tabLayout.getSelectedTabPosition() == 0){
            menu.setGroupVisible(R.id.menu_group_filter, true);
        }else{
            menu.setGroupVisible(R.id.menu_group_filter, false);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_filter:
                EventBus.getDefault().post(new OnCommentFiltersEvent());
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {

        if(viewpager.getCurrentItem() > 0){
            viewpager.setCurrentItem(0);
            return;
        }

        super.onBackPressed();

    }

    @OnClick(R.id.send)
    protected void onSendClick(View view){
        EventBus.getDefault().post(new OnCommentSendEvent());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_comments;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }
}
