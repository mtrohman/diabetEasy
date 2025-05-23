package com.gulasehat.android.widget;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.LruCache;

import androidx.appcompat.widget.AppCompatTextView;

public class TextMaterial extends AppCompatTextView {

    private final static String NAME = "MATERIAL";
    private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(12);

    public TextMaterial(Context context) {
        super(context);
        init();

    }

    public TextMaterial(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {

        Typeface typeface = sTypefaceCache.get(NAME);

        if (typeface == null) {

            typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/material.ttf");
            sTypefaceCache.put(NAME, typeface);

        }

        setTypeface(typeface);

    }

}
