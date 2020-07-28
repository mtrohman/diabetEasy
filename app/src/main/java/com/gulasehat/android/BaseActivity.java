package com.gulasehat.android;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.gulasehat.android.event.LoginRequired;
import com.gulasehat.android.event.OnFavoriteClicked;
import com.gulasehat.android.model.Ads;
import com.gulasehat.android.model.AppSettings;
import com.gulasehat.android.util.Preferences;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.util.Settings;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public abstract class BaseActivity extends AppCompatActivity {

    private ACProgressFlower progress;

    protected AppSettings settings;

    private boolean isClosable = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        if(Preferences.getString(Preferences.COUNTRY_CODE, "").isEmpty()){
            String countryCode;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                countryCode = LocaleList.getDefault().get(0).getCountry();
                Preferences.setString(Preferences.COUNTRY_CODE, countryCode.toUpperCase());
            } else{
                countryCode = Locale.getDefault().getCountry();
                Preferences.setString(Preferences.COUNTRY_CODE, countryCode.toUpperCase());
            }
        }

        if (!Preferences.getString(Preferences.CURRENT_APP_LANG_CODE, "").equals("")) {
            setLanguage(Preferences.getString(Preferences.CURRENT_APP_LANG_CODE, ""));
        }

        setTheme(Resource.getCurrentThemeStyle(this));
        super.onCreate(savedInstanceState);


        settings = Settings.getAppSettings();


        setContentView(getContentView());
        ButterKnife.bind(this);

        if (Settings.getAppSettings() != null && Settings.getAppSettings().getAds() != null && Settings.getAppSettings().getAds().getAdBottomBanner().isStatus() && Settings.getAppSettings().getAds().getAdBottomBanner().getNetwork().equals(Ads.AD_NETWORK_ADMOB)) {
            MobileAds.initialize(this, Settings.getAppSettings().getAds().getAdBottomBanner().getPublisherId());
        }


        progress = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                //.text(getString(R.string.please_wait))
                .fadeColor(Color.DKGRAY).build();

        progress.setCancelable(false);


        if (canShowInterstitialWhenOpened(getActivityName())) {
            showInterstitial();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (canShowInterstitialWhenClosed()) {
            showInterstitial();
        }
        Preferences.setString(Preferences.LAST_CLOSED_ACTIVITY, "");
    }

    private void showInterstitial() {
        switch (settings.getAds().getAdInterstitial().getNetwork()) {
            case Ads.AD_NETWORK_ADMOB:
                showInterstitialAdmob();
                break;
            case Ads.AD_NETWORK_FACEBOOK:
                showInterstitialFacebook();
                break;
        }
    }

    private void showInterstitialAdmob() {
        if (FlinkApplication.getInterstitialAdAdmob() != null && FlinkApplication.getInterstitialAdAdmob().isLoaded()) {

            final Dialog mDialog = new Dialog(this, R.style.InterstitialDialogTheme);
            mDialog.setContentView(R.layout.interstitial);
            mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {
                    FlinkApplication.getInterstitialAdAdmob().setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            FlinkApplication.getInterstitialAdAdmob().loadAd(new AdRequest.Builder()/*.addTestDevice("33BE2250B43518CCDA7DE426D04EE231")*/.build());
                            dialog.dismiss();
                        }
                    });
                    FlinkApplication.getInterstitialAdAdmob().show();
                }
            });
            mDialog.show();
        }
    }




    private void showInterstitialFacebook() {

        if (FlinkApplication.getInterstitialAdFacebook() != null &&
                FlinkApplication.getInterstitialAdFacebook().isAdLoaded() &&
                !FlinkApplication.getInterstitialAdFacebook().isAdInvalidated()) {

            View layout = View.inflate(this, R.layout.interstitial, null);
            final RelativeLayout rootContainer = layout.findViewById(R.id.rootContainer);
            final MaterialProgressBar progressBar = layout.findViewById(R.id.progress);

            Dialog mDialog = new Dialog(this, R.style.InterstitialDialogTheme);
            mDialog.setContentView(layout);
            mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {

                    FlinkApplication.getInterstitialAdFacebook().setAdListener(new InterstitialAdListener() {
                        @Override
                        public void onInterstitialDisplayed(Ad ad) {
                            rootContainer.setBackgroundResource(android.R.color.transparent);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onInterstitialDismissed(Ad ad) {
                            FlinkApplication.getInterstitialAdFacebook().loadAd();
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(Ad ad, AdError adError) {
                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                        }

                        @Override
                        public void onAdClicked(Ad ad) {

                        }

                        @Override
                        public void onLoggingImpression(Ad ad) {
                        }
                    });
                    FlinkApplication.getInterstitialAdFacebook().show();
                }
            });
            mDialog.show();
        }
    }

    protected void showProgress() {
        if (progress != null) {
            progress.show();
        }
    }

    protected void hideProgress() {
        if (progress != null) {
            progress.dismiss();
            progress.setCancelable(false);
        }
    }

    protected abstract int getContentView();

    protected abstract String getActivityName();

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

    }

    private boolean canShowInterstitialWhenOpened(String targetActivityClassName) {

        if (settings == null || settings.getAds() == null || settings.getAds().getAdInterstitial() == null) {
            return false;
        }

        if (settings.getAds().getAdInterstitial().isStatus()) {
            return targetActivityClassName.equals("CategoriesActivity") && settings.getAds().getAdInterstitial().isOpenedCategoryList() ||
                    targetActivityClassName.equals("ArchiveActivity") && settings.getAds().getAdInterstitial().isOpenedArchivePages() ||
                    targetActivityClassName.equals("AuthorsActivity") && settings.getAds().getAdInterstitial().isOpenedAuthorList() ||
                    targetActivityClassName.equals("ProfileActivity") && settings.getAds().getAdInterstitial().isOpenedAuthorUserProfile() ||
                    targetActivityClassName.equals("PagesActivity") && settings.getAds().getAdInterstitial().isOpenedPageList() ||
                    targetActivityClassName.equals("PostsActivity") && settings.getAds().getAdInterstitial().isOpenedPostList() ||
                    targetActivityClassName.equals("PostActivity") && settings.getAds().getAdInterstitial().isOpenedPostPageDetail() ||
                    targetActivityClassName.equals("TagCloudActivity") && settings.getAds().getAdInterstitial().isOpenedTagCloud();
        }
        return false;

    }

    private boolean canShowInterstitialWhenClosed() {
        if (settings == null || settings.getAds() == null || settings.getAds().getAdInterstitial() == null) {
            return false;
        }

        if (settings.getAds().getAdInterstitial().isStatus()) {
            String lastClosedActivity = Preferences.getString(Preferences.LAST_CLOSED_ACTIVITY, "");
            Log.d("burkist12", "lastClosedActivity: " + lastClosedActivity);
            return lastClosedActivity.equals("CategoriesActivity") && settings.getAds().getAdInterstitial().isClosedCategoryList() ||
                    lastClosedActivity.equals("ArchiveActivity") && settings.getAds().getAdInterstitial().isClosedArchivePages() ||
                    lastClosedActivity.equals("AuthorsActivity") && settings.getAds().getAdInterstitial().isClosedAuthorList() ||
                    lastClosedActivity.equals("ProfileActivity") && settings.getAds().getAdInterstitial().isClosedAuthorUserProfile() ||
                    lastClosedActivity.equals("PagesActivity") && settings.getAds().getAdInterstitial().isClosedPageList() ||
                    lastClosedActivity.equals("PostsActivity") && settings.getAds().getAdInterstitial().isClosedPostList() ||
                    lastClosedActivity.equals("PostActivity") && settings.getAds().getAdInterstitial().isClosedPostPageDetail() ||
                    lastClosedActivity.equals("TagCloudActivity") && settings.getAds().getAdInterstitial().isClosedTagCloud();
        }

        return false;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFavoriteClicked(OnFavoriteClicked event) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginRequired(LoginRequired event) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    protected void showPhoto(View view, String url) {

        Intent intent = new Intent(this, PhotoActivity.class);
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);

        intent.putExtra("left", screenLocation[0]).
                putExtra("top", screenLocation[1]).
                putExtra("width", view.getWidth()).
                putExtra("height", view.getHeight()).
                putExtra("image", url);

        startActivity(intent);

    }



    @Override
    public void startActivity(Intent intent) {

        super.startActivity(intent);

        if (Settings.getAppSettings() == null) {
            return;
        }
        if (intent.hasExtra("image")) {
            return;
        }
        switch (Settings.getAppSettings().getAppScreenAnim()) {
            case "slide_from_left":
                overridePendingTransition(R.anim.slide_in_left, R.anim.no);
                break;
            case "slide_from_right":
                overridePendingTransition(R.anim.slide_in_right, R.anim.no);
                break;
            case "slide_from_bottom":
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.no);
                break;
            case "slide_from_top":
                overridePendingTransition(R.anim.slide_in_top, R.anim.no);
                break;
            case "fade":
                overridePendingTransition(R.anim.fade_enter, R.anim.no);
                break;
            case "shrink":
                overridePendingTransition(R.anim.shrink_enter, R.anim.shrink_exit);
                break;
            case "in_out_from_right":
                overridePendingTransition(R.anim.in_out_enter_from_right, R.anim.in_out_exit_from_right);
                break;
            case "in_out_from_left":
                overridePendingTransition(R.anim.in_out_enter_from_left, R.anim.in_out_exit_from_left);
                break;
        }

    }

    @Override
    public void onBackPressed() {

        Log.d("burkist12", "onbackpressed: " + getActivityName());
        Preferences.setString(Preferences.LAST_CLOSED_ACTIVITY, getActivityName());

        super.onBackPressed();

        if (Settings.getAppSettings() == null) {
            return;
        }
        switch (Settings.getAppSettings().getAppScreenAnim()) {
            case "slide_from_left":
                overridePendingTransition(R.anim.no, R.anim.slide_out_left);
                break;
            case "slide_from_right":
                overridePendingTransition(R.anim.no, R.anim.slide_out_right);
                break;
            case "slide_from_bottom":
                overridePendingTransition(R.anim.no, R.anim.slide_out_bottom);
                break;
            case "slide_from_top":
                overridePendingTransition(R.anim.no, R.anim.slide_out_top);
                break;
            case "fade":
                overridePendingTransition(R.anim.no, R.anim.fade_exit);
                break;
            case "shrink":
                overridePendingTransition(R.anim.shrink_enter, R.anim.shrink_exit);
                break;
            case "in_out_from_right":
                overridePendingTransition(R.anim.in_out_enter_from_left, R.anim.in_out_exit_from_left);
                break;
            case "in_out_from_left":
                overridePendingTransition(R.anim.in_out_enter_from_right, R.anim.in_out_exit_from_right);
                break;
        }

    }

    @Override
    public void finish() {

        super.finish();

        if (Settings.getAppSettings() == null) {
            return;
        }
        switch (Settings.getAppSettings().getAppScreenAnim()) {
            case "slide_from_left":
                overridePendingTransition(R.anim.no, R.anim.slide_out_left);
                break;
            case "slide_from_right":
                overridePendingTransition(R.anim.no, R.anim.slide_out_right);
                break;
            case "slide_from_bottom":
                overridePendingTransition(R.anim.no, R.anim.slide_out_bottom);
                break;
            case "slide_from_top":
                overridePendingTransition(R.anim.no, R.anim.slide_out_top);
                break;
            case "fade":
                overridePendingTransition(R.anim.no, R.anim.fade_exit);
                break;
            case "shrink":
                overridePendingTransition(R.anim.shrink_enter, R.anim.shrink_exit);
                break;
            case "in_out_from_right":
                overridePendingTransition(R.anim.in_out_enter_from_left, R.anim.in_out_exit_from_left);
                break;
            case "in_out_from_left":
                overridePendingTransition(R.anim.in_out_enter_from_right, R.anim.in_out_exit_from_right);
                break;
        }


    }

    public void setLanguage(String languageCode) {
        try {
            Locale locale = new Locale(languageCode);
            Locale.setDefault(locale);
            Resources res = getBaseContext().getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.setLocale(locale);
            res.updateConfiguration(conf, dm);
            Resource.init(this);

        } catch (Exception ignored) {
        }

        if (getIntent().hasExtra("LANG_CHANGED")) {
            getIntent().removeExtra("LANG_CHANGED");
            recreate();
        }
    }

    public void exitApp() {

        if (!isClosable) {

            isClosable = true;

            Toast.makeText(this, R.string.close_text, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isClosable = false;
                }
            }, 3500);

            return;
        }


        finishAffinity();
    }


}
