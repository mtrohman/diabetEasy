package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Comments {

    @SerializedName("post_id")
    private int postID;
    @SerializedName("sort_type")
    private String sortType;
    @SerializedName("total_data")
    private int totalData;
    @SerializedName("limit")
    private int limit;
    @SerializedName("data")
    private ArrayList<Comment> comments;

    public int getPostID() {
        return postID;
    }

    public String getSortType() {
        return sortType;
    }

    public int getTotalData() {
        return totalData;
    }

    public int getLimit() {
        return limit;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }
}
