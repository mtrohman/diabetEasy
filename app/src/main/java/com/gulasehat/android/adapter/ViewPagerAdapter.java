package com.gulasehat.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gulasehat.android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<Integer> mFragmentIconList = new ArrayList<>();
    private final List<Integer> mFragmentTitleList = new ArrayList<>();
    private final Context context;
    private int layoutResource;

    @BindView(R.id.icon)
    protected ImageView icon;
    @BindView(R.id.title)
    protected TextView title;

    public ViewPagerAdapter(FragmentManager manager, Context context, int layoutResource) {
        super(manager);
        this.context = context;
        this.layoutResource = layoutResource;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, int icon, int title) {
        mFragmentList.add(fragment);
        mFragmentIconList.add(icon);
        mFragmentTitleList.add(title);

    }

    public View getTabView(int position) {

        View v = LayoutInflater.from(context).inflate(layoutResource, null);

        ButterKnife.bind(this, v);

        title.setText(context.getString(mFragmentTitleList.get(position)));
        icon.setImageResource(mFragmentIconList.get(position));


        return v;
    }

}