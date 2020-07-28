package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class AppMenuItem {

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
