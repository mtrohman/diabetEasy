package com.gulasehat.android;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gulasehat.android.adapter.PostRecyclerAdapter;
import com.gulasehat.android.adapter.TagViewAdapter;
import com.gulasehat.android.event.OnCloseFullScreen;
import com.gulasehat.android.event.OnFullscreenDisabled;
import com.gulasehat.android.event.OnFullscreenEnabled;
import com.gulasehat.android.event.OnOpenFullScreen;
import com.gulasehat.android.event.OnWebViewLoaded;
import com.gulasehat.android.event.ScrollChangeEvent;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Category;
import com.gulasehat.android.model.CategoryChip;
import com.gulasehat.android.model.Post;
import com.gulasehat.android.model.Tag;
import com.gulasehat.android.service.PostService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.DateUtil;
import com.gulasehat.android.util.FavoriteManager;
import com.gulasehat.android.util.KeyboardUtils;
import com.gulasehat.android.util.Preferences;
import com.gulasehat.android.util.Settings;
import com.gulasehat.android.util.Unit;
import com.gulasehat.android.widget.Alert;
import com.gulasehat.android.widget.BottomAdView;
import com.google.android.material.appbar.AppBarLayout;
import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;
import com.plumillonforge.android.chipview.OnChipClickListener;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class PostActivity extends BaseActivity implements PostRecyclerAdapter.OnFavoriteClick {

    @BindView(R.id.appBarLayout)
    protected AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.toolbarTitle)
    protected TextView toolbarTitle;
    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.categories)
    protected ChipView categories;
    @BindView(R.id.tags)
    protected ChipView tags;
    @BindView(R.id.feature)
    protected ImageView feature;
    @BindView(R.id.commentButton)
    protected Button commentButton;
    @BindView(R.id.favoriteButton)
    protected Button favoriteButton;
    @BindView(R.id.date)
    protected TextView date;
    @BindView(R.id.gradient)
    protected ImageView gradient;
    @BindView(R.id.tagContainer)
    protected LinearLayout tagContainer;
    @BindView(R.id.comments)
    protected RelativeLayout comments;
    @BindView(R.id.commentCount)
    protected TextView commentCount;
    @BindView(R.id.contentContainer)
    protected LinearLayout contentContainer;
    @BindView(R.id.content)
    protected LinearLayout content;
    @BindView(R.id.otherPostsContainer)
    protected LinearLayout otherPostsContainer;
    @BindView(R.id.contentScrollView)
    protected NestedScrollView contentScrollView;
    @BindView(R.id.protection)
    protected LinearLayout protection;
    @BindView(R.id.password)
    protected EditText password;
    @BindView(R.id.posts)
    protected RecyclerView postsRV;
    @BindView(R.id.loadingContainer)
    protected LinearLayout loadingContainer;
    private int scrollPosition = 0;

    @BindView(R.id.bottomAdView)
    protected BottomAdView bottomAdView;


    private int postID;
    private Post post;
    private ActionBar actionBar;
    protected WebViewFragment webViewFragment;

    //private InterstitialAd mInterstitialAd;
    //private com.facebook.ads.InterstitialAd interstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webViewFragment = new WebViewFragment();



        Analytics.logScreen("Post");

        Intent intent = getIntent();
        postID = intent.getIntExtra("id", 0);


        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.bringToFront();

        if(Settings.getAppSettings() != null){
            int delayMillis;
            if(Settings.getAppSettings().getAppScreenAnim().equals("fade")){
                delayMillis = 300;
            }else{
                delayMillis = 0;
            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fetchPost();
                }
            }, delayMillis);
        }else{
            fetchPost();
        }



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

                toolbarTitle.setAlpha(percentage);
                feature.setAlpha(alpha);
                date.setAlpha(alpha);
                commentButton.setAlpha(alpha);
                favoriteButton.setAlpha(alpha);
                gradient.setAlpha(alpha);
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void fetchPost(){

        PostService.post(postID, new PostService.OnPostListener() {
            @Override
            public void onSuccess(Post p) {

                post = p;
                //test();
                prepareUI();
            }

            @Override
            public void onFailed(ApiResponse response) {
                //hideProgress();

                Alert.make(PostActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
            }
        });
    }


    private void prepareUI() {



        setupHeader();

        if(Settings.getAppSettings().getAppComment().isStatus() && post.isCommentAvailable() ){
            comments.setVisibility(View.VISIBLE);
            commentCount.setText(String.valueOf(post.getCommentCount()));
        }else{
            commentButton.setVisibility(View.GONE);
        }

        if(Settings.getAppSettings().getAppPostDetail().getCategoryStatus()){
            categories.setVisibility(View.VISIBLE);
        }else{
            categories.setVisibility(View.GONE);
        }


        appBarLayout.setExpanded(false, false);





        if (post.isProtected()) {
            loadingContainer.setVisibility(View.GONE);
            content.setVisibility(View.GONE);
            protection.setVisibility(View.VISIBLE);

            comments.setVisibility(View.GONE);


            password.requestFocus();
            KeyboardUtils.showKeyboard(this, password);

        } else {

            //initializePageItems();


            appBarLayout.setExpanded(true, false);

            protection.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);

            title.setText(post.getPostTitle());
            initializePageItems();


            if(post.getPostType().equals(Post.POST)){
                favoriteButton.setVisibility(View.VISIBLE);
                if(post.getOtherPosts().getPostCount() > 0){
                    prepareOtherPosts();
                }
            }else{
                favoriteButton.setVisibility(View.GONE);
            }



            feature.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPhoto(feature, post.getFeaturedImage());
                }
            });

        }


    }

    private void prepareOtherPosts() {

        PostRecyclerAdapter adapter = new PostRecyclerAdapter(this, false);
        adapter.setOnFavoriteClick(this);
        adapter.setViewType(post.getOtherPosts().getLayout());
        adapter.setPosts(post.getOtherPosts().getPosts());
        postsRV.setLayoutManager(new LinearLayoutManager(this));
        postsRV.addItemDecoration(new DividerItemDecoration(getResources()));
        postsRV.setHasFixedSize(true);
        postsRV.setAdapter(adapter);

        adapter.setOnPostClickListener(new PostRecyclerAdapter.OnPostClickListener() {
            @Override
            public void onClicked(Post post) {
                Intent intent = new Intent(PostActivity.this, PostActivity.class);
                intent.putExtra("id", post.getId());
                startActivity(intent);
            }
        });

        otherPostsContainer.setVisibility(View.VISIBLE);

    }



    private void initializePageItems(){


        //loadingContainer.setVisibility(View.GONE);

        try{
            webViewFragment.setPostData(this, post);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.postFragment, webViewFragment);
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }


        setupCategories();
        setupTags();


        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if(isOpen){
                            Log.d("burkist", "scroll pos: " + contentScrollView.getScrollY());
                        }else{

                        }
                    }
                });


    }


    private void setupHeader() {

        toolbarTitle.setText(post.getPostTitle());

        toolbarTitle.post(new Runnable() {
            @Override
            public void run() {
                if(toolbarTitle.getLineCount() > 1){
                    toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                }
            }
        });


        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(750);

        feature.startAnimation(fadeIn);

        if (!post.getFeaturedImage().equals("")) {

            GlideApp.with(getApplicationContext()).load(post.getFeaturedImage()).thumbnail(GlideApp.with(getApplicationContext())
                    .load(Settings.getAppSettings().getDefaultPostImage())
                    .fitCenter()
                    .centerCrop()).fitCenter().centerCrop().addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    gradient.setVisibility(View.VISIBLE);
                    return false;
                }
            }).into(feature);
        }


        if (post.isFavorite()) {
            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.ic_fav_active), null, null);
        }

        commentButton.setText(String.valueOf(post.getCommentCount()));
        favoriteButton.setText(String.valueOf(post.getFavoriteCount()));
        if(Settings.getAppSettings().getAppDate().getPostStatus()){
            date.setText(DateUtil.getFormattedDate(this, post.getPostDate()));
        }else{
            date.setVisibility(View.GONE);
        }

    }

    private void setupTags() {

        if (post.getTags().size() > 0 && Settings.getAppSettings().getAppPostDetail().getTagStatus()) {

            tagContainer.setVisibility(View.VISIBLE);

            tags.setOnChipClickListener(new OnChipClickListener() {
                @Override
                public void onChipClick(Chip chip) {
                    Intent intent = new Intent(PostActivity.this, PostsActivity.class);
                    intent.putExtra("title", ((CategoryChip)chip).getText());
                    intent.putExtra("tag", ((CategoryChip)chip).getSlug());
                    startActivity(intent);
                }
            });

            ArrayList<Chip> tags = new ArrayList<>();

            for (Tag tag :
                    post.getTags()) {
                tags.add(new CategoryChip(tag.getSlug(), tag.getName(), tag.getCount()));
            }

            this.tags.setAdapter(new TagViewAdapter(this, R.layout.chip_2));
            this.tags.setChipList(tags);

        }else{
            tagContainer.setVisibility(View.GONE);
        }
    }

    private void setupCategories() {


        if (post.getCategories().size() > 0) {

            categories.setOnChipClickListener(new OnChipClickListener() {
                @Override
                public void onChipClick(Chip chip) {
                    Intent intent = new Intent(PostActivity.this, PostsActivity.class);
                    intent.putExtra("title", ((CategoryChip) chip).getText());
                    intent.putExtra("cat_id", ((CategoryChip) chip).getId());
                    startActivity(intent);
                }
            });

            ArrayList<Chip> cats = new ArrayList<>();

            for (Category cat :
                    post.getCategories()) {
                cats.add(new CategoryChip(cat.getTermID(), cat.getName().toUpperCase(), 0));
            }

            categories.setChipLayoutRes(R.layout.chip_1);
            categories.setChipList(cats);

        }


    }

    @OnClick({R.id.comments, R.id.commentButton})
    public void onCommentsClick(View view) {
        Intent intent = new Intent(this, CommentsActivity.class);
        intent.putExtra("id", postID);
        startActivity(intent);
    }

    @OnClick(R.id.enter)
    public void onPasswordSendClick(View view) {


        String pass = password.getText().toString().trim();

        if (pass.isEmpty()) {
            Alert.make(this, R.string.password_required, Alert.ALERT_TYPE_WARNING);
            return;
        }

        showProgress();

        PostService.checkPassword(postID, pass, new PostService.OnPostListener() {
            @Override
            public void onSuccess(Post p) {

                hideProgress();

                loadingContainer.setVisibility(View.VISIBLE);

                post = p;
                prepareUI();

                //KeyboardUtils.hideKeyboard(PostActivity.this, password);
            }

            @Override
            public void onFailed(ApiResponse response) {
                hideProgress();
                Alert.make(PostActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
            }
        });

    }

    public void actionShare(){

        if(post == null){
            return;
        }

        Analytics.logEvent("Share Post");

        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, post.getPostURL());
        share.setType("text/plain");
        startActivity(share);
    }

    @OnClick(R.id.favoriteButton)
    public void onPostFavoriteClick(View view) {
        if (post != null) {
            if(Preferences.getBoolean(Preferences.IS_LOGGED_IN, false)){

                if (post.isFavorite()) {
                    favoriteButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.ic_fav_normal), null, null);
                    post.setFavorite(false);
                    post.setFavoriteCount(post.getFavoriteCount() - 1);
                    favoriteButton.setText(String.valueOf(post.getFavoriteCount()));
                    FavoriteManager.handle(this, post.getId(), false);
                } else {
                    favoriteButton.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.ic_fav_active), null, null);
                    post.setFavorite(true);
                    post.setFavoriteCount(post.getFavoriteCount() + 1);
                    favoriteButton.setText(String.valueOf(post.getFavoriteCount()));
                    FavoriteManager.handle(this, post.getId(), true);
                }
            }else{
                FavoriteManager.handle(this, post.getId(), !post.isFavorite());
            }
        }
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_post;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.share:
                actionShare();
                break;
        }

        return true;
    }

    @Override
    protected void onPause() {
        scrollPosition = contentScrollView.getScrollY();

        super.onPause();
        if (webViewFragment != null){
            webViewFragment.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        contentScrollView.post(new Runnable() {
            public void run() {
                contentScrollView.scrollTo(0, scrollPosition);
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (webViewFragment != null){
            webViewFragment.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        /*if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }*/
/*
        if(interstitialAd != null && interstitialAd.isAdLoaded()) {
            if(!interstitialAd.isAdInvalidated()) {
                interstitialAd.show();
            }
        }
*/
        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebViewLoaded(OnWebViewLoaded event){



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingContainer.setVisibility(View.GONE);
                contentScrollView.scrollTo(0, 0);
                contentContainer.setPadding(0, 0, 0, bottomAdView.getHeight());
            }
        }, 500);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScrollChange(ScrollChangeEvent event){

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        int position = (int) (metrics.scaledDensity * (float) event.getPosition());
        Log.d("burkist", "pos1: " + position);
        position += title.getHeight();
        Log.d("burkist", "pos2: " + position);

        position += categories.getHeight();
        Log.d("burkist", "pos3: " + position);
        position += Unit.dp2px(this, 34);

        if(event.isSmooth()){
            contentScrollView.smoothScrollTo(0, position);
        }else{
            contentScrollView.scrollTo(0, position);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }



    @Override
    public void onFavoriteClick(int postId, boolean fav) {
        FavoriteManager.handle(this, postId, fav);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (webViewFragment != null) {
            webViewFragment.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFullScreenEnabled(OnFullscreenEnabled event){

        final View view = event.getView();
        Intent i = new Intent(this, FullscreenActivity.class);
        startActivity(i);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new OnOpenFullScreen(view));
                Log.d("burakist", "onOpenFullScreen post edildi");
            }
        },1000);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFullScreenDisabled(OnFullscreenDisabled event){
        Log.d("burakist", "onCloseFullScreen post edildi");

        EventBus.getDefault().post(new OnCloseFullScreen(event.getView()));
    }

}


