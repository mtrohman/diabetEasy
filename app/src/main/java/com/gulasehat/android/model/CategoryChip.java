package com.gulasehat.android.model;

import com.plumillonforge.android.chipview.Chip;

public class CategoryChip implements Chip {
    private int id;
    private String slug;
    private String name;
    private int count;

    public CategoryChip(int id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public CategoryChip(String slug, String name, int count) {
        this.slug = slug;
        this.name = name;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    @Override
    public String getText() {
        return name;
    }

    public int getCount() {
        return count;
    }


}