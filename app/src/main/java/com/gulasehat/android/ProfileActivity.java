package com.gulasehat.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.request.RequestOptions;
import com.gulasehat.android.adapter.ViewPagerAdapter;
import com.gulasehat.android.event.OnAuthorFetchedEvent;
import com.gulasehat.android.fragment.ProfileDetailsFragment;
import com.gulasehat.android.fragment.ProfilePostsFragment;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.User;
import com.gulasehat.android.service.AuthorService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.widget.Alert;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.skyfishjy.library.RippleBackground;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    protected ViewPager viewPager;
    @BindView(R.id.tabLayout)
    protected TabLayout tabLayout;
    @BindView(R.id.appBarLayout)
    protected AppBarLayout appBarLayout;
    @BindView(R.id.appBarTitle)
    protected TextView appBarTitle;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.innerContainer)
    protected RippleBackground innerContainer;
    @BindView(R.id.authorName)
    protected TextView authorName;
    @BindView(R.id.authorUserName)
    protected TextView authorUserName;
    @BindView(R.id.authorAvatar)
    protected ImageView authorAvatar;

    private ActionBar actionBar;
    private ViewPagerAdapter viewPagerAdapter;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Analytics.logScreen("Profile");

        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        init();

        Intent intent = getIntent();

        AuthorService.getAuthor(intent.getIntExtra("author", 0), new AuthorService.OnAuthorListener() {
            @Override
            public void onSuccess(User user) {
                mUser = user;

                prepareUI();
                EventBus.getDefault().post(new OnAuthorFetchedEvent(user));
            }

            @Override
            public void onFailed(ApiResponse response) {
                Alert.make(ProfileActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
            }
        });

        innerContainer.startRippleAnimation();

    }

    private void prepareUI(){
        appBarTitle.setText(mUser.getUserFullName());
        authorName.setText(mUser.getUserFullName());
        authorUserName.setText(mUser.getUserName());
        if(!mUser.getUserPicture().equals("")){
//            Picasso.get().load(mUser.getUserPicture()).fit().transform(new CircleTransform()).into(authorAvatar);
            GlideApp.with(getApplicationContext()).load(mUser.getUserPicture()).fitCenter().apply(RequestOptions.circleCropTransform()).into(authorAvatar);
        }
    }

    private void init() {

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, R.layout.layout_tab_light);

        viewPagerAdapter.addFragment(new ProfileDetailsFragment(), R.drawable.icon_profile_white, R.string.about);
        viewPagerAdapter.addFragment(new ProfilePostsFragment(), R.drawable.icon_post_white, R.string.posts);

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View view = viewPagerAdapter.getTabView(i);
                if (i > 0) {
                    view.setAlpha(0.5f);
                }
                tab.setCustomView(view);
            }
        }



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                boolean hasScroll = true;

                if(position == 0){
                    appBarLayout.setExpanded(true);
                }

                if(position == 1){
                    appBarLayout.setExpanded(false);
                    hasScroll = false;
                }

                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();

                if(behavior != null){
                    final boolean finalHasScroll = hasScroll;
                    behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                        @Override
                        public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                            return finalHasScroll;
                        }
                    });
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = Math.abs(appBarLayout.getY()) / (float) maxScroll;

                if (verticalOffset == 0) {
                    actionBar.setDisplayShowTitleEnabled(false);

                } else {
                    actionBar.setDisplayShowTitleEnabled(true);
                }

                float alpha = 1 - percentage;

                innerContainer.setAlpha(alpha);
                appBarTitle.setAlpha(percentage);


            }
        });
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

    @OnClick(R.id.authorAvatar)
    public void onAvatarClick(View view){

        if(mUser != null && !mUser.getUserPicture().equals("")){

            try{

                showPhoto(authorAvatar, mUser.getUserPicture());

            }catch (Exception e){
                Alert.make(ProfileActivity.this, R.string.error, Alert.ALERT_TYPE_WARNING);
            }
        }

    }

    @Override
    public void onBackPressed() {

        if(viewPager.getCurrentItem() > 0){
            viewPager.setCurrentItem(0);
            return;
        }

        super.onBackPressed();

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_profile;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }
}
