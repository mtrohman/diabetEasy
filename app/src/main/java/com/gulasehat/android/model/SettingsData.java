package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SettingsData {

    @SerializedName("app_date_type")
    private String appDateType;

    @SerializedName("tabs")
    private ArrayList<CustomTab> tabs;

    @SerializedName("app_about_us_page")
    private Post appAboutPage;

    @SerializedName("app_faq_page")
    private Post appFaqPage;


    public String getAppDateType() {
        return appDateType;
    }

    public ArrayList<CustomTab> getTabs() {
        return tabs;
    }

    public Post getAppAboutPage() {
        return appAboutPage;
    }

    public Post getAppFaqPage() {
        return appFaqPage;
    }

}
