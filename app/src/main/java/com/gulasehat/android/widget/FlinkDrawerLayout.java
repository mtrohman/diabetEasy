package com.gulasehat.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;

public class FlinkDrawerLayout extends DrawerLayout {

    public FlinkDrawerLayout(Context context) {
        super(context);
    }

    public FlinkDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlinkDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.EXACTLY);
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.getSize(heightMeasureSpec), View.MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}