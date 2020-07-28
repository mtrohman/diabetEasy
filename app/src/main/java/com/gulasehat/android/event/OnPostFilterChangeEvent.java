package com.gulasehat.android.event;

public class OnPostFilterChangeEvent {

    private String key;

    public OnPostFilterChangeEvent(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}