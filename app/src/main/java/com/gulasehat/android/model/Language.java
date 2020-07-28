package com.gulasehat.android.model;

public class Language {

    private String code;
    private String name;
    private String flag;
    private boolean selected;

    public Language(String code, String name, String flag, boolean selected) {
        this.code = code;
        this.name = name;
        this.flag = flag;
        this.selected = selected;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
