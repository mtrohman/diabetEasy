package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class AuthUser extends User{

    @SerializedName("user_cookie_hash")
    private String userCookieHash;

    public String getUserCookieHash() {
        return userCookieHash;
    }

    public AuthUser(MyUser user) {
        this.setUserID(user.getUserID());
        this.setUserName(user.getUserName());
        this.setUserFullName(user.getUserFullName());
        this.setUserEmail(user.getUserEmail());
        this.setUserBiographicalInfo(user.getUserBiographicalInfo());
        this.setUserGender(user.getUserGender());
        this.setHasPicture(user.hasPicture());
        this.setUserJob(user.getUserJob());
        this.setUserWebsite(user.getUserWebsite());
        this.setUserPicture(user.getUserPicture());
        this.setUserThumb(user.getUserThumb());
    }
}
