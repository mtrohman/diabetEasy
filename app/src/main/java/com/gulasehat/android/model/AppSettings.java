package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AppSettings {

    @SerializedName("register_id")
    private String registerID;
    @SerializedName("app_theme")
    private String appTheme;
    @SerializedName("app_screen_anim")
    private String appScreenAnim;
    @SerializedName("app_comment_rate_status")
    private boolean appCommentRateStatus;
    @SerializedName("app_action_bar")
    private AppActionBar appActionBar;
    @SerializedName("user_profile")
    private AppUserProfile appUserProfile;
    @SerializedName("app_about_us_page")
    private int appAboutUsPageID;
    @SerializedName("app_membership_status")
    private boolean appMembershipStatus;
    @SerializedName("start_page")
    private StartPage appStartPage;
    @SerializedName("comment")
    private AppComment appComment;
    @SerializedName("date")
    private AppDate appDate;
    @SerializedName("app_is_language_override")
    private boolean isLanguageOverride = false;
    @SerializedName("app_default_lang")
    private String appDefaultLang;
    @SerializedName("post_detail")
    private AppPostDetail appPostDetail;
    @SerializedName("page_detail")
    private AppPageDetail appPageDetail;
    @SerializedName("search")
    private AppSearch appSearch;
    @SerializedName("menu")
    private ArrayList<AppMenu> appMenus;
    @SerializedName("tabs")
    private ArrayList<AppTab> appTabs;
    @SerializedName("ads")
    private Ads ads;
    @SerializedName("default_post_image")
    private String defaultPostImage;
    @SerializedName("app_user_location_permission_status")
    private boolean locationPermissionStatus = false;

    public String getRegisterID() {
        return registerID;
    }

    public String getAppTheme() {
        return appTheme;
    }

    public boolean isAppCommentRateStatus() {
        return appCommentRateStatus;
    }

    public AppActionBar getAppActionBar() {
        return appActionBar;
    }

    public AppComment getAppComment() {
        return appComment;
    }

    public AppDate getAppDate() {
        return appDate;
    }

    public AppUserProfile getAppUserProfile() {
        return appUserProfile;
    }

    public int getAppAboutUsPageID() {
        return appAboutUsPageID;
    }

    public ArrayList<AppMenu> getAppMenus() {
        return appMenus;
    }

    public ArrayList<AppTab> getAppTabs() {
        return appTabs;
    }

    public boolean getAppMembershipStatus() {
        return appMembershipStatus;
    }

    public StartPage getAppStartPage() {
        return appStartPage;
    }

    public Ads getAds() {
        return ads;
    }

    public String getAppScreenAnim() {
        return appScreenAnim;
    }

    public boolean isOverrideLanguage() {
        return isLanguageOverride;
    }

    public String getAppDefaultLang() {
        return appDefaultLang;
    }

    public String getDefaultPostImage() {
        return defaultPostImage;
    }

    public AppSearch getAppSearch() {
        return appSearch;
    }

    public AppPostDetail getAppPostDetail() {
        return appPostDetail;
    }

    public AppPageDetail getAppPageDetail() {
        return appPageDetail;
    }

    public boolean getLocationPermissionStatus() {
        return locationPermissionStatus;
    }
}
