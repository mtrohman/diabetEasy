package com.gulasehat.android.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gulasehat.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationMenuItem extends RelativeLayout {

    private Context context;

    @BindView(R.id.icon)
    protected ImageView icon;
    @BindView(R.id.name)
    protected TextView name;
    @BindView(R.id.badge)
    protected LinearLayout badge;
    @BindView(R.id.count)
    protected TextView count;


    public NavigationMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        inflate(getContext(), R.layout.item_navigation, this);
        ButterKnife.bind(this);
    }
    public void setText(String text){
        this.name.setText(text);
    }

    public void setText(int text){
        this.name.setText(text);
    }

    public void setTextColor(int color){
        this.name.setTextColor(ContextCompat.getColor(context, color));
    }

    public void setIcon(int icon){
        this.icon.setImageResource(icon);
    }

    public void setIconColor(int color){
        this.icon.getDrawable().setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
    }

}
