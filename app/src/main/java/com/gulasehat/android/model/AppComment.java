package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class AppComment {
    @SerializedName("status")
    private boolean status;
    @SerializedName("rate_status")
    private boolean rateStatus;

    public boolean isStatus() {
        return status;
    }

    public boolean isRateStatus() {
        return rateStatus;
    }
}
