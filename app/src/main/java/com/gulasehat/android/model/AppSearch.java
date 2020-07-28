package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class AppSearch {

    @SerializedName("post_listing")
    private boolean postListing;
    @SerializedName("page_listing")
    private boolean pageListing;
    @SerializedName("author_listing")
    private boolean authorListng;
    @SerializedName("minimum_character")
    private int minimumCharacter = 3;

    public boolean isPostListing() {
        return postListing;
    }

    public boolean isPageListing() {
        return pageListing;
    }

    public boolean isAuthorListng() {
        return authorListng;
    }

    public int getMinimumCharacter() {
        return minimumCharacter;
    }
}
