package com.gulasehat.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class ApiResponse {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message = "error";
    @Expose
    @SerializedName("data")
    private HashMap<String, String> data = new HashMap<>();

    public ApiResponse() {
    }

    public ApiResponse(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
