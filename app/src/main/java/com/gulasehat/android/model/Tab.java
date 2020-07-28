package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class Tab {

    @SerializedName("item_id")
    private int itemID;
    @SerializedName("item_key")
    private String itemKey;

    public int getItemID() {
        return itemID;
    }

    public String getItemKey() {
        return itemKey;
    }
}
