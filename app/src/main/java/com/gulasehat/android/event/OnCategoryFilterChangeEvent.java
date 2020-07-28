package com.gulasehat.android.event;

public class OnCategoryFilterChangeEvent {

    private String key;

    public OnCategoryFilterChangeEvent(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}