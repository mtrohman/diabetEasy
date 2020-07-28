package com.gulasehat.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.gulasehat.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomSwitch extends RelativeLayout {

    @BindView(R.id.icon)
    protected ImageView mIcon;
    @BindView(R.id.text)
    protected TextView mText;
    @BindView(R.id.sw)
    protected SwitchCompat chechbox;
    private boolean fromUser = false;
    private OnCheckedChangeListener onCheckedChangeListener;

    public CustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    private void initialize(final Context context, AttributeSet attrs) {

        setClickable(true);
        setFocusable(true);

        inflate(getContext(), R.layout.custom_switch, this);
        ButterKnife.bind(this);


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomSwitch,
                0, 0
        );

        try {


            Drawable icon = a.getDrawable(R.styleable.CustomSwitch_cs_icon);
            String text = a.getString(R.styleable.CustomSwitch_cs_text);

            mIcon.setImageDrawable(icon);
            mText.setText(text);

        } finally {
            a.recycle();
        }

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fromUser = true;
                if(chechbox.isChecked()){
                    chechbox.setChecked(false);
                }else{
                    chechbox.setChecked(true);
                }

            }
        });

        chechbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!fromUser){

                }
                if(onCheckedChangeListener != null){
                    onCheckedChangeListener.onChanged(isChecked);
                }
                fromUser = false;
            }
        });
    }

    public boolean getValue(){
        return chechbox.isChecked();
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public void setValue(boolean val){
        fromUser = true;
        chechbox.setChecked(val);
    }

    public interface OnCheckedChangeListener{
        void onChanged(boolean val);
    }

}
