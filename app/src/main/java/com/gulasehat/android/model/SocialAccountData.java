package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SocialAccountData {

    @SerializedName("data")
    private ArrayList<SocialAccount> socialAccounts;
    @SerializedName("layout")
    private int layout;

    public ArrayList<SocialAccount> getSocialAccounts() {
        return socialAccounts;
    }

    public int getLayout() {
        return layout;
    }
}
