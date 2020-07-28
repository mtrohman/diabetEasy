package com.gulasehat.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gulasehat.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditableProfileItem extends RelativeLayout {

    @BindView(R.id.icon)
    protected ImageView mIcon;
    @BindView(R.id.label)
    protected TextView mLabel;
    @BindView(R.id.value)
    protected TextView mValue;
    @BindView(R.id.body)
    protected LinearLayout body;
    @BindView(R.id.editIcon)
    protected ImageView mEditIcon;

    public EditableProfileItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        inflate(getContext(), R.layout.item_editable_profile_item, this);
        ButterKnife.bind(this);


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EditableProfileItem,
                0, 0
        );

        try {


            Drawable icon = a.getDrawable(R.styleable.EditableProfileItem_epi_icon);
            int color = a.getColor(R.styleable.EditableProfileItem_epi_color, 0);
            String label = a.getString(R.styleable.EditableProfileItem_epi_label);
            String value = a.getString(R.styleable.EditableProfileItem_epi_value);
            boolean editIcon = a.getBoolean(R.styleable.EditableProfileItem_epi_edit_icon, true);

            mIcon.setImageDrawable(icon);
            mLabel.setText(label);

            if(value == null || value.equals("")){
                mValue.setVisibility(GONE);
            }else{
                mValue.setText(value);
            }

            if(! editIcon){
                 mEditIcon.setVisibility(GONE);
                 //body.setPadding((int) Unit.dpToPixels(context, 72f), (int) Unit.dpToPixels(context, 12f), (int) Unit.dpToPixels(context, 12f), (int) Unit.dpToPixels(context, 12f));
            }

            if(color != 0){
                mIcon.getDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                mLabel.setTextColor(color);
            }


        } finally {
            a.recycle();
        }

    }

    public void setText(String text){
        if(text.equals("")){
            mValue.setVisibility(GONE);
        }else{
            mValue.setVisibility(VISIBLE);
            mValue.setText(text);
        }
    }

}
