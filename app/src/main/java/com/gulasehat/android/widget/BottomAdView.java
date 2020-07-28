package com.gulasehat.android.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.gulasehat.android.BuildConfig;
import com.gulasehat.android.R;
import com.gulasehat.android.model.AdBanner;
import com.gulasehat.android.model.Ads;
import com.gulasehat.android.util.Settings;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class BottomAdView extends FrameLayout {

    private static final int HOME = 1;
    private static final int CATEGORIES = 2;
    private static final int POST_LIST = 3;
    private static final int POST_PAGE_DETAIL = 4;
    private static final int PAGE_LIST = 5;
    private static final int AUTHOR_LIST = 6;
    private static final int ARCHIVE = 7;
    private static final int TAG_CLOUD = 8;
    private static final int SOCIAL_ACCOUNTS = 9;
    private static final int SEARCH = 10;
    private static final int FAQ = 11;
    private static final int NOTIFICATIONS = 12;


    private AdBanner banner;

    public BottomAdView(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public BottomAdView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    public void initialize(Context context, AttributeSet attrs) {

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BottomAdView,
                0, 0
        );

        try {

            int placement = a.getInt(R.styleable.BottomAdView_ad_placement, 1);

            banner = Settings.getAppSettings().getAds().getAdBottomBanner();

            if (banner.isStatus()) {
                switch (placement) {
                    case HOME:
                        if (banner.isHomePage()) {
                            Log.d("burkist", "home page ads initializing");
                            initAds();
                        }
                        break;
                    case CATEGORIES:
                        if (banner.isCategories()) {
                            initAds();
                        }
                        break;
                    case POST_LIST:
                        if (banner.isPostList()) {
                            initAds();
                        }
                        break;
                    case POST_PAGE_DETAIL:
                        if (banner.isPostPageDetail()) {
                            initAds();
                        }
                        break;
                    case PAGE_LIST:
                        if (banner.isPageList()) {
                            initAds();
                        }
                        break;
                    case AUTHOR_LIST:
                        if (banner.isAuthorList()) {
                            initAds();
                        }
                        break;
                    case ARCHIVE:
                        if (banner.isArchive()) {
                            initAds();
                        }
                        break;
                    case TAG_CLOUD:
                        if (banner.isTagCloud()) {
                            initAds();
                        }
                        break;
                    case SOCIAL_ACCOUNTS:
                        if (banner.isSocialAccounts()) {
                            initAds();
                        }
                        break;
                    case SEARCH:
                        if (banner.isSearch()) {
                            initAds();
                        }
                        break;
                    case FAQ:
                        if (banner.isFaq()) {
                            initAds();
                        }
                        break;
                    case NOTIFICATIONS:
                        if (banner.isNotifications()) {
                            initAds();
                        }
                        break;
                }
            }

        } finally {
            a.recycle();
        }


    }

    private void initAds() {
        setBackgroundResource(R.color.ad_bg);
        switch (banner.getNetwork()) {
            case Ads.AD_NETWORK_FACEBOOK:
                Log.d("burkist", "ads type : facebook");
                loadFacebook();
                break;
            case Ads.AD_NETWORK_ADMOB:
                Log.d("burkist", "ads type : admob");
                loadAdMob();
                break;
        }
    }

    private void loadFacebook() {

        if(banner.getAdPlacementId().equals("")){
            return;
        }
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(getContext(), banner.getAdPlacementId(), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        addView(adView);
        adView.loadAd();


    }

    private void loadAdMob() {

        if(banner.getAdUnitId().equals("")){
            return;
        }

        AdView mAdView = new AdView(getContext());
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(banner.getAdUnitId());


        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        if(BuildConfig.DEBUG){
            adRequestBuilder.addTestDevice("A5D0E3D47C18092C4BE988E632C7F62D");
        }
        AdRequest adRequest = adRequestBuilder.build();


        mAdView.loadAd(adRequest);


        addView(mAdView);

    }
}
