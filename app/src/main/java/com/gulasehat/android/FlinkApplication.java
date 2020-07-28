package com.gulasehat.android;

import android.app.Application;
import android.content.Context;

import com.gulasehat.android.model.Ads;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.Common;
import com.gulasehat.android.util.Preferences;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.util.Settings;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

public class FlinkApplication extends Application {

    private static ArrayList<UnifiedNativeAd> nativeAdsAdmob = new ArrayList<>();
    private static Context context;
    private static boolean isDialogShown = false;

    private static InterstitialAd interstitialAdAdmob;
    private static com.facebook.ads.InterstitialAd interstitialAdFacebook;

    private static boolean processedNativeAd = false;
    private static boolean processedInterstitialAd = false;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        CaocConfig.Builder.create()
                .showErrorDetails(true) //default: true
                .showRestartButton(true) //default: true
                .logErrorOnRestart(true) //default: true
                .errorActivity(CrashActivity.class) //default: null (default error activity)
                .eventListener(new CrashReportEventListener()) //default: null
                .apply();

        Preferences.init(getApplicationContext());
        Analytics.init(this);
        Resource.init(this);
        Common.init(this);

        initializeInterstitialAds();
        initializeNativeAds();

    }

    public static  void initializeNativeAds(){

/*        if(Preferences.getInt("asd",2) % 2 == 0){
            Preferences.setInt("asd", Preferences.getInt("asd", 2)+1);
            return;
        }

        Preferences.setInt("asd", Preferences.getInt("asd", 2)+1);*/

        if(Settings.getAppSettings() != null &&
                Settings.getAppSettings().getAds() != null &&
                Settings.getAppSettings().getAds().getAdNative() != null &&
                Settings.getAppSettings().getAds().getAdNative().isStatus() &&
                Settings.getAppSettings().getAds().getAdNative().getNetwork().equals(Ads.AD_NETWORK_ADMOB) &&
                !Settings.getAppSettings().getAds().getAdNative().getAdUnitId().isEmpty() &&
                !processedNativeAd){

            processedNativeAd = true;

            final AdLoader adsManagerAdmob = new AdLoader.Builder(context, Settings.getAppSettings().getAds().getAdNative().getAdUnitId())
                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            nativeAdsAdmob.add(unifiedNativeAd);
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            .build())
                    .build();

            adsManagerAdmob.loadAds(new AdRequest.Builder().build(),3);

        }
    }

    public static void initializeInterstitialAds() {

        if (Settings.getAppSettings() != null
                && Settings.getAppSettings().getAds() != null
                && Settings.getAppSettings().getAds().getAdInterstitial() != null
                && Settings.getAppSettings().getAds().getAdInterstitial().isStatus()
                && !processedInterstitialAd) {

            processedInterstitialAd = true;

            if (Settings.getAppSettings().getAds().getAdInterstitial().getNetwork().equals(Ads.AD_NETWORK_ADMOB)) {
                interstitialAdAdmob = new InterstitialAd(getContext());
                interstitialAdAdmob.setAdUnitId(Settings.getAppSettings().getAds().getAdInterstitial().getAdUnitId());
                interstitialAdAdmob.loadAd(new AdRequest.Builder()/*.addTestDevice("33BE2250B43518CCDA7DE426D04EE231")*/.build());
            } else if (Settings.getAppSettings().getAds().getAdInterstitial().getNetwork().equals(Ads.AD_NETWORK_FACEBOOK)) {
                interstitialAdFacebook = new com.facebook.ads.InterstitialAd(getContext(), Settings.getAppSettings().getAds().getAdInterstitial().getAdPlacementId());
                interstitialAdFacebook.loadAd();
            }
        }
    }

    public static ArrayList<UnifiedNativeAd> getNativeAdsAdmob() {
        return nativeAdsAdmob;
    }

    public static InterstitialAd getInterstitialAdAdmob(){
        return interstitialAdAdmob;
    }

    public static com.facebook.ads.InterstitialAd getInterstitialAdFacebook(){
        return interstitialAdFacebook;
    }

    private static class CrashReportEventListener implements CustomActivityOnCrash.EventListener {
        @Override
        public void onLaunchErrorActivity() {

        }

        @Override
        public void onRestartAppFromErrorActivity() {
        }

        @Override
        public void onCloseAppFromErrorActivity() {
        }
    }



    public static void setDialogShown(boolean show){
        isDialogShown = show;
    }

    public static boolean isIsDialogShown(){
        return isDialogShown;
    }

    public static Context getContext(){
        return context;
    }





    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}