package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TagData {

    @SerializedName("data")
    private ArrayList<Tag> tags = new ArrayList<>();
    @SerializedName("total_data")
    private int totalData;
    @SerializedName("total_data_count_status")
    private boolean totalDataCountStatus;
    @SerializedName("sort_type")
    private String sortType;

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public int getTotalData() {
        return totalData;
    }

    public boolean getTotalDataCountStatus() {
        return totalDataCountStatus;
    }

    public String getSortType() {
        return sortType;
    }
}
