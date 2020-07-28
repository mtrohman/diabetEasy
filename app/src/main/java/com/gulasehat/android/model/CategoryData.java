package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryData {

    @SerializedName("data")
    private ArrayList<Category> categories;
    @SerializedName("parent")
    private Category parent;
    @SerializedName("total_data")
    private int totalData;
    @SerializedName("total_data_count_status")
    private boolean totalDataCountStatus;
    @SerializedName("post_count_status")
    private boolean postCountStatus;
    @SerializedName("sort_type")
    private String sortType;
    @SerializedName("layout")
    private int layout;

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public Category getParent() {
        return parent;
    }

    public int getTotalData() {
        return totalData;
    }

    public boolean getTotalDataCountStatus() {
        return totalDataCountStatus;
    }

    public boolean getPostCountStatus() {
        return postCountStatus;
    }

    public String getSortType() {
        return sortType;
    }

    public int getLayout() {
        return layout;
    }
}
