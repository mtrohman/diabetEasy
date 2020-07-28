package com.gulasehat.android.model;

import com.google.gson.annotations.SerializedName;

public class LocationData {

    @SerializedName("lat")
    private double locationLat = 0;
    @SerializedName("lng")
    private double locationLng = 0;
    @SerializedName("country")
    private String locationCountry = "";
    @SerializedName("city")
    private String locationCity = "";
    @SerializedName("district")
    private String locationDistrict = "";

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(double locationLng) {
        this.locationLng = locationLng;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationDistrict() {
        return locationDistrict;
    }

    public void setLocationDistrict(String locationDistrict) {
        this.locationDistrict = locationDistrict;
    }
}
