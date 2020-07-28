package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class ProfileSettings {

    @SerializedName("email_address")
    private boolean email = true;
    @SerializedName("website")
    private boolean website = true;
    @SerializedName("gender")
    private boolean gender = true;
    @SerializedName("job")
    private boolean job = true;
    @SerializedName("biography")
    private boolean bio = true;

    public boolean isEmail() {
        return email;
    }

    public boolean isWebsite() {
        return website;
    }

    public boolean isGender() {
        return gender;
    }

    public boolean isJob() {
        return job;
    }

    public boolean isBio() {
        return bio;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public void setWebsite(boolean website) {
        this.website = website;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setJob(boolean job) {
        this.job = job;
    }

    public void setBio(boolean bio) {
        this.bio = bio;
    }
}
