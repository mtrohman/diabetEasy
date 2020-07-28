package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class SocialAccount {

    @SerializedName("short_name")
    private String name;
    @SerializedName("social_url")
    private String url;
    @SerializedName("icon")
    private String icon;
    @SerializedName("bg_color")
    private String bgColor;
    @SerializedName("font_color")
    private String fontColor;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getIcon() {
        return icon;
    }

    public String getBgColor() {
        return bgColor;
    }

    public String getFontColor() {
        return fontColor;
    }
}
