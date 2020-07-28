package com.gulasehat.android.event;

public class OnFilterChangeEvent {

    private String key;

    public OnFilterChangeEvent(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}