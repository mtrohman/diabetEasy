package com.gulasehat.android.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;


public class TextAwesome extends AppCompatTextView {

    private final static String NAME = "FONTAWESOME";
    public final static String FA_REGULAR = "far";
    public final static String FA_SOLID = "fas";
    public final static String FA_BRANDS = "fab";

    private String style = FA_SOLID;

    public TextAwesome(Context context) {
        super(context);
        init();

    }

    public TextAwesome(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setStyle(String style){
        this.style = style;
        init();
    }

    public void init() {

        //Typeface typeface = sTypefaceCache.get(NAME);

        String path = "";

        switch (style){
            case FA_SOLID: path = "fonts/fa-solid-900.ttf";
                break;
            case FA_BRANDS: path = "fonts/fa-brands-400.ttf";
                break;
        }

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), path);

        /*
        if (typeface == null) {


            sTypefaceCache.put(NAME, typeface);

        }

       */

        setTypeface(typeface);


    }

}
