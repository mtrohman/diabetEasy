package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("website")
    private String website;
    @SerializedName("gender")
    private String gender;
    @SerializedName("job")
    private String job;
    @SerializedName("biographical_info")
    private String bio;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void clearAll(){
        setName("");
        setEmail("");
        setWebsite("");
        setGender("");
        setJob("");
        setBio("");
    }
}
