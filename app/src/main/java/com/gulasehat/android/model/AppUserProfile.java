package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class AppUserProfile {

    @SerializedName("name_status")
    private boolean hasName;
    @SerializedName("email_status")
    private boolean hasEmail;
    @SerializedName("website_status")
    private boolean hasWebsite;
    @SerializedName("gender_status")
    private boolean hasGender;
    @SerializedName("job_status")
    private boolean hasJob;
    @SerializedName("biographical_info_status")
    private boolean hasBio;

    public boolean hasName() {
        return hasName;
    }

    public boolean hasEmail() {
        return hasEmail;
    }

    public boolean hasWebsite() {
        return hasWebsite;
    }

    public boolean hasGender() {
        return hasGender;
    }

    public boolean hasJob() {
        return hasJob;
    }

    public boolean hasBio() {
        return hasBio;
    }
}
