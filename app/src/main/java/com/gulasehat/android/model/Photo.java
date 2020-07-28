package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("image_id")
    protected int id;
    @SerializedName("image_title")
    protected String title;
    @SerializedName("image_alt")
    protected String alt;
    @SerializedName("image_url")
    protected String url;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAlt() {
        return alt;
    }

    public String getUrl() {
        return url;
    }
}
