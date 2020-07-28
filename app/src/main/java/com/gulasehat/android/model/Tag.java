package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class Tag {

    @SerializedName("term_id")
    private int termID;
    @SerializedName("name")
    private String name;
    @SerializedName("slug")
    private String slug;
    @SerializedName("count")
    private int count;

    public int getTermID() {
        return termID;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public int getCount() {
        return count;
    }
}
