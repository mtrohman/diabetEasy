package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class AppPageDetail {

    @SerializedName("copy_status")
    private boolean copy = false;

    public boolean isCopy() {
        return copy;
    }
}
