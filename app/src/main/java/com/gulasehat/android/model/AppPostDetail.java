package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class AppPostDetail {
    @SerializedName("category_status")
    private boolean categoryStatus;
    @SerializedName("tag_status")
    private boolean tagStatus;
    @SerializedName("copy_status")
    private boolean copy = false;


    public boolean getCategoryStatus() {
        return categoryStatus;
    }

    public boolean getTagStatus() {
        return tagStatus;
    }

    public boolean isCopy() {
        return copy;
    }
}
