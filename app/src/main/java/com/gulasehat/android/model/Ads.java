package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class Ads {

    public static final String AD_NETWORK_FACEBOOK = "facebook";
    public static final String AD_NETWORK_ADMOB = "admob";


    @SerializedName("ad_bottom_banner")
    private AdBanner adBottomBanner;
    @SerializedName("ad_native")
    private AdNative adNative;
    @SerializedName("ad_interstitial")
    private AdInterstitial adInterstitial;

    public AdBanner getAdBottomBanner() {
        return adBottomBanner;
    }

    public AdNative getAdNative() {
        return adNative;
    }

    public AdInterstitial getAdInterstitial() {
        return adInterstitial;
    }
}
