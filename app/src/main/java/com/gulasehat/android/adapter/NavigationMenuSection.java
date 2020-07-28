package com.gulasehat.android.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gulasehat.android.R;
import com.gulasehat.android.model.AppMenuItem;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.widget.NavigationMenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class NavigationMenuSection extends StatelessSection {

    private String title;
    private List<AppMenuItem> list;
    private OnMenuItemClick onMenuItemClick;

    public NavigationMenuSection(String title, ArrayList<AppMenuItem> list) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.layout_nav_item)
                .headerResourceId(R.layout.layout_nav_section)
                .build());

        this.title = title;
        this.list = list;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder menuHolder = (ItemViewHolder) holder;

        final AppMenuItem menu = list.get(position);

        menuHolder.menu.setText(Resource.getStringResID(menu.getItemKey()));
        menuHolder.menu.setIcon(Resource.getDrawableResID(menu.getItemKey()));



        if(menu.getItemKey().equals("nav_logout")){
            menuHolder.menu.setTextColor(R.color.red);
            menuHolder.menu.setIconColor(R.color.red);
        }else{
            menuHolder.menu.setTextColor(R.color.navTextColor);
            menuHolder.menu.setIconColor(R.color.navTextColor);
        }

        menuHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onMenuItemClick != null){
                    onMenuItemClick.onMenuItemClick(menu);
                }
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.title.setText(title);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        protected TextView title;

        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.menu)
        protected NavigationMenuItem menu;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnMenuItemClick(OnMenuItemClick onMenuItemClick) {
        this.onMenuItemClick = onMenuItemClick;
    }

    public interface OnMenuItemClick{
        void onMenuItemClick(AppMenuItem menu);
    }


}
