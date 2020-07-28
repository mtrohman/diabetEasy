package com.gulasehat.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gulasehat.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomPreference extends RelativeLayout {

    @BindView(R.id.icon)
    protected ImageView mIcon;
    @BindView(R.id.text)
    protected TextView mText;
    @BindView(R.id.value)
    protected TextView mValue;
    @BindView(R.id.arrow)
    protected ImageView mArrow;

    public CustomPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {

        inflate(getContext(), R.layout.custom_pref, this);
        ButterKnife.bind(this);


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomPreference,
                0, 0
        );

        try {


            Drawable icon = a.getDrawable(R.styleable.CustomPreference_cp_icon);
            String text = a.getString(R.styleable.CustomPreference_cp_text);
            String value = a.getString(R.styleable.CustomPreference_cp_value);
            boolean hideArrow = a.getBoolean(R.styleable.CustomPreference_cp_hide_arrow, false);
            int color = a.getColor(R.styleable.CustomPreference_cp_color_tint, 0);

            mIcon.setImageDrawable(icon);

            if(color != 0){
                mIcon.getDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                mText.setTextColor(Color.RED);
                mArrow.setVisibility(GONE);
            }
            mText.setText(text);
            if(value == null || value.equals("")){
                mValue.setVisibility(GONE);
            }else{
                mValue.setText(value);
            }

            Log.d("burakv", "arrow: " + hideArrow);

            if(hideArrow){
                mArrow.setVisibility(GONE);
            }


        } finally {
            a.recycle();
        }
    }

    public void setValue(String val){
        mValue.setText(val);
    }
}
