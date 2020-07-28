package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Months {

    @SerializedName("data")
    private ArrayList<Month> months;
    @SerializedName("sort_type")
    private String sortType;
    @SerializedName("layout")
    private int layout;

    public ArrayList<Month> getMonths() {
        return months;
    }

    public String getSortType() {
        return sortType;
    }

    public int getLayout() {
        return layout;
    }
}
