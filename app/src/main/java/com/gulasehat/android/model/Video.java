package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class Video {
    @SerializedName("video_id")
    private int id;
    @SerializedName("video_title")
    private String title;
    @SerializedName("video_url")
    private String url;
    @SerializedName("video_image")
    private String image;
    @SerializedName("length")
    private int length;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

    public int getLength() {
        return length;
    }
}
