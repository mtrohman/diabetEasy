package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class AppDate {

    @SerializedName("type")
    private String type;
    @SerializedName("post_status")
    private boolean postStatus;

    public String getType() {
        return type;
    }

    public boolean getPostStatus() {
        return postStatus;
    }
}
