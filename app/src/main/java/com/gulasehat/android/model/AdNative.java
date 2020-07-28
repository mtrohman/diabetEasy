package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class AdNative {

    @SerializedName("status")
    private boolean status;
    @SerializedName("network")
    private String network;
    @SerializedName("ad_placement_id")
    private String adPlacementId;
    @SerializedName("ad_unit_id")
    private String adUnitId;
    @SerializedName("ad_frequency")
    private int frequency;

    public boolean isStatus() {
        return status;
    }

    public String getNetwork() {
        return network;
    }

    public String getAdPlacementId() {
        return adPlacementId;
    }

    public String getAdUnitId() {
        return adUnitId;
    }

    public int getFrequency() {
        return frequency;
    }
}
