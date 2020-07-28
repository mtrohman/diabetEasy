package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class Sound {
    @SerializedName("sound_id")
    private int id;
    @SerializedName("sound_title")
    private String title;
    @SerializedName("sound_url")
    private String url;
    @SerializedName("sound_image")
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
