package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class RegisterAppData {

    @SerializedName("os_id")
    private String osID = "";
    @SerializedName("device_id")
    private String deviceID = "";
    @SerializedName("country_code")
    private String countryCode = "";
    @SerializedName("app_user_language")
    private String appUserLanguage;
    @SerializedName("app_language")
    private String appLanguage;
    @SerializedName("location_lat")
    private double locationLat = 0;
    @SerializedName("location_lng")
    private double locationLng = 0;
    @SerializedName("location_country")
    private String locationCountry = "";
    @SerializedName("location_city")
    private String locationCity = "";
    @SerializedName("location_district")
    private String locationDistrict = "";
    @SerializedName("os")
    private String os = "Android";
    @SerializedName("os_version")
    private String osVersion;
    @SerializedName("operator")
    private String operator = "";
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("model")
    private String model;
    @SerializedName("app_version_name")
    private String appVersionName;
    @SerializedName("app_version_code")
    private int appVersionCode;
    @SerializedName("push_token")
    private String pushToken = "";

    public void setOsID(String osID) {
        this.osID = osID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setAppUserLanguage(String appUserLanguage) {
        this.appUserLanguage = appUserLanguage;
    }

    public void setAppLanguage(String appLanguage) {
        this.appLanguage = appLanguage;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public void setLocationLng(double locationLng) {
        this.locationLng = locationLng;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public void setLocationDistrict(String locationDistrict) {
        this.locationDistrict = locationDistrict;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
