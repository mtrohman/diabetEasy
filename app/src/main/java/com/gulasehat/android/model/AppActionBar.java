package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class AppActionBar {

    @SerializedName("status")
    private String status;
    @SerializedName("title")
    private String title;
    @SerializedName("image")
    private String image;
    @SerializedName("image_position")
    private String imagePos;
    @SerializedName("image_width")
    private int imageWidth;
    @SerializedName("image_height")
    private int imageHeight;

    public static final String IMAGE_POSITION_SIDE = "side";
    public static final String IMAGE_POSITION_CENTER = "center";

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getImagePos() {
        return imagePos;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }
}
