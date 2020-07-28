package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArchiveData {

    @SerializedName("data")
    private ArrayList<Year> years;
    @SerializedName("sort_type")
    private String sortType;
    @SerializedName("layout")
    private int layout;

    public ArrayList<Year> getYears() {
        return years;
    }

    public String getSortType() {
        return sortType;
    }

    public int getLayout() {
        return layout;
    }
}
