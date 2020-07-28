package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class Year {

    @SerializedName("year")
    private int year;
    @SerializedName("count")
    private int postCount;
    @SerializedName("months")
    private Months months;

    public int getYear() {
        return year;
    }

    public int getPostCount() {
        return postCount;
    }

    public Months getMonths() {
        return months;
    }
}
