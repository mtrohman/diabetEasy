package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Menu {
    @SerializedName("cat_id")
    private int catID;
    @SerializedName("cat_key")
    private String catKey;
    @SerializedName("items")
    private ArrayList<MenuItem> items;

    public int getCatID() {
        return catID;
    }

    public String getCatKey() {
        return catKey;
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }
}
