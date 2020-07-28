package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class Month {

    @SerializedName("year")
    private int year;
    @SerializedName("month")
    private int month;
    @SerializedName("count")
    private int postCount;

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getPostCount() {
        return postCount;
    }
}
