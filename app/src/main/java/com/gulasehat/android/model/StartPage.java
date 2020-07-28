package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class StartPage {

    @SerializedName("logo")
    private String logo;
    @SerializedName("logo_width")
    private int width;
    @SerializedName("logo_height")
    private int height;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;

    public String getLogo() {
        return logo;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
