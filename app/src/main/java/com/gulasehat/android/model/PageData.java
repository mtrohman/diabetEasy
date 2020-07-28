package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PageData {

    @SerializedName("data")
    private ArrayList<Page> pages;
    @SerializedName("total_data")
    private int totalData = 0;
    @SerializedName("total_data_count_status")
    private boolean totalDataCountStatus;
    @SerializedName("search_status")
    private boolean searchStatus;
    @SerializedName("sort_type")
    private String sortType;
    @SerializedName("layout")
    private String layout;
    @SerializedName("limit")
    private int limit;
    @SerializedName("paged")
    private int page;

    public ArrayList<Page> getPages() {
        return pages;
    }

    public int getTotalData() {
        return totalData;
    }

    public boolean getTotalDataCountStatus() {
        return totalDataCountStatus;
    }

    public boolean isSearchStatus() {
        return searchStatus;
    }

    public String getSortType() {
        return sortType;
    }

    public String getLayout() {
        return layout;
    }

    public int getLimit() {
        return limit;
    }

    public int getPage() {
        return page;
    }
}

