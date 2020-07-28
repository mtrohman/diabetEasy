package com.gulasehat.android.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gulasehat.android.R;
import com.gulasehat.android.model.CategoryChip;
import com.plumillonforge.android.chipview.ChipViewAdapter;

public class TagViewAdapter extends ChipViewAdapter {

    private int layoutRes;

    public TagViewAdapter(Context context, int layoutRes) {
        super(context);
        this.layoutRes = layoutRes;
    }

    @Override
    public int getLayoutRes(int position) {
        return layoutRes;
    }

    @Override
    public int getBackgroundColor(int position) {
        return 0;
    }

    @Override
    public int getBackgroundColorSelected(int position) {
        return 0;
    }

    @Override
    public int getBackgroundRes(int position) {
        return 0;
    }

    @Override
    public void onLayout(View view, int position) {
        CategoryChip tag = (CategoryChip) getChip(position);

        ((TextView) view.findViewById(R.id.name)).setText(tag.getText());
        ((TextView) view.findViewById(R.id.count)).setText(String.valueOf(tag.getCount()));

    }
}