package com.gulasehat.android.widget;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gulasehat.android.GlideApp;
import com.gulasehat.android.R;
import com.gulasehat.android.util.Util;
import com.gulasehat.android.widget.ImageSlider.ImageFragment.OnItemClickListener;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageSlider extends RelativeLayout{

    private Context context;
    private ImagePagerAdapter imagePagerAdapter;
    private FragmentManager fragmentManager;

    @BindView(R.id.left)
    protected ImageButton left;
    @BindView(R.id.right)
    protected ImageButton right;
    @BindView(R.id.slider)
    protected ViewPager slider;

    private OnSlideClickListener onSlideClickListener;

    private final float ALPHA = 0.3f;

    public ImageSlider(@NonNull Context context, FragmentManager fragmentManager) {
        super(context);
        this.context = context;
        this.fragmentManager = fragmentManager;
        init();
    }

    public ImageSlider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {


        inflate(getContext(), R.layout.image_slider, this);
        ButterKnife.bind(this);

        slider.setId(View.generateViewId());

        imagePagerAdapter = new ImagePagerAdapter(fragmentManager);




        left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slider.getCurrentItem() > 0){
                    right.setAlpha(1f);
                    int index = slider.getCurrentItem() - 1;
                    slider.setCurrentItem(index);
                    if(slider.getCurrentItem() == 0){
                        v.setAlpha(ALPHA);
                    }
                }else{
                    v.setAlpha(ALPHA);
                }

            }
        });
        right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagePagerAdapter.getCount()-1 > (slider.getCurrentItem())){
                    left.setAlpha(1f);
                    int index = slider.getCurrentItem() + 1;
                    slider.setCurrentItem(index);
                    if(imagePagerAdapter.getCount()-1 == slider.getCurrentItem()){
                        v.setAlpha(ALPHA);
                    }
                }else{
                    v.setAlpha(ALPHA);
                }
            }
        });

        slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position > 0){
                    left.setAlpha(1f);
                }else{
                    left.setAlpha(ALPHA);
                }
                if(position < imagePagerAdapter.getCount()-1){
                    right.setAlpha(1f);
                }else{
                    right.setAlpha(ALPHA);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void addImage(String url){
        ImageFragment image = new ImageFragment();
        image.setImageUrl(url);
        if(onSlideClickListener != null){
            image.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(String url) {
                    onSlideClickListener.onClick(url);
                }
            });
        }

        imagePagerAdapter.addImageFragment(image);
    }

    public void build(){



        slider.setOffscreenPageLimit(imagePagerAdapter.getCount());
        slider.setAdapter(imagePagerAdapter);

        if(Util.isRTL()){
            imagePagerAdapter.enableRTL(true);
            slider.setCurrentItem(imagePagerAdapter.getCount()-1);
        }

    }

    public void setOnSlideClickListener(OnSlideClickListener onSlideClickListener) {
        this.onSlideClickListener = onSlideClickListener;
    }


    public class ImagePagerAdapter extends FragmentPagerAdapter {

        private ArrayList<ImageFragment> images = new ArrayList<>();

        private ImagePagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public void enableRTL(boolean enable){
            if(enable) Collections.reverse(images);
        }

        @Override
        public Fragment getItem(int position) {
            return images.get(position);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        public void addImageFragment(ImageFragment image){
            images.add(image);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    public static class ImageFragment extends Fragment {

        private ImageView image;
        private String url;

        private OnItemClickListener onItemClickListener;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_image, container, false);

            image = view.findViewById(R.id.image);

            image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        onItemClickListener.onClick(url);
                    }
                }
            });

            image.setImageResource(0);

            GlideApp.with(getContext()).load(url).fitCenter().centerCrop().into(image);
//            Picasso.get().load(url).fit().centerCrop().into(image);

            return view;
        }

        public void setImageUrl(String url){
            this.url = url;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public interface OnItemClickListener{
            void onClick(String url);
        }

    }

    public interface OnSlideClickListener{
        void onClick(String url);
    }



}
