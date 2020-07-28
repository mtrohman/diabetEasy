package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class Report {

    @SerializedName("app_id")
    private String appId;
    @SerializedName("app_name")
    private String appName;
    @SerializedName("app_level")
    private int apiLevel;
    @SerializedName("api_url")
    private String apiUrl;
    @SerializedName("error_log")
    private String errorLog;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("model")
    private String model;
    @SerializedName("app_version_name")
    private String appVersionName;
    @SerializedName("app_version_code")
    private int appVersionCode;
    @SerializedName("app_user_language")
    private String appUserLanguage;
    @SerializedName("app_language")
    private String appLanguage;


    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setApiLevel(int apiLevel) {
        this.apiLevel = apiLevel;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public void setAppUserLanguage(String appUserLanguage) {
        this.appUserLanguage = appUserLanguage;
    }

    public void setAppLanguage(String appLanguage) {
        this.appLanguage = appLanguage;
    }
}
