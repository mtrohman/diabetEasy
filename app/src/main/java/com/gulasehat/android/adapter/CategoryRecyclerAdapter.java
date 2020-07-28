package com.gulasehat.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gulasehat.android.R;
import com.gulasehat.android.model.Category;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.widget.TextAwesome;
import com.gulasehat.android.widget.TextMaterial;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categories = new ArrayList<>();
    private OnCategoryClickListener onCategoryClickListener;

    public static final int TYPE_VIEW_LIST_WITH_ICON = 10;
    public static final int TYPE_VIEW_LIST_WITHOUT_ICON = 11;
    public static final int TYPE_VIEW_GRID_WITH_ICON = 20;
    public static final int TYPE_VIEW_GRID_WITHOUT_ICON = 21;

    private int viewType = TYPE_VIEW_LIST_WITH_ICON;

    private boolean postCountStatus = true;

    public CategoryRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layout;

        switch (viewType) {
            case TYPE_VIEW_GRID_WITH_ICON:
                layout = R.layout.item_category_grid_with_icon;
                break;
            case TYPE_VIEW_GRID_WITHOUT_ICON:
                layout = R.layout.item_category_grid_without_icon;
                break;
            case TYPE_VIEW_LIST_WITHOUT_ICON:
                layout = R.layout.item_category_list_without_icon;
                break;
            case TYPE_VIEW_LIST_WITH_ICON:
            default:
                layout = R.layout.item_category_list_with_icon;

        }

        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final Category category = categories.get(position);



        if(category.isAllContents()){
            holder.name.setText(R.string.all_contents);
        }else{
            holder.name.setText(category.getName());
        }


        if(holder.textAwesome != null && holder.textMaterial != null){
            holder.textAwesome.setVisibility(View.GONE);
            holder.textMaterial.setVisibility(View.GONE);

            if(category.isAllContents()){
                holder.textMaterial.setVisibility(View.VISIBLE);
                holder.textMaterial.setText(category.getIcon().getCategoryIcon());
                holder.textMaterial.setTextColor(ContextCompat.getColor(context, R.color.white));


                if(holder.iconContainer != null){
                    Drawable iconContainerBg = holder.iconContainer.getBackground();
                    iconContainerBg.setColorFilter(ContextCompat.getColor(context, Resource.getColorPrimary()), PorterDuff.Mode.SRC_ATOP);
                }

            }else{
                if (category.getIcon().getCategoryIconSet().equals("font_awesome")) {
                    if(category.getIcon().getStyleTag().equals(TextAwesome.FA_BRANDS)){
                        holder.textAwesome.setStyle(TextAwesome.FA_BRANDS);
                    }else{
                        holder.textAwesome.setStyle(TextAwesome.FA_SOLID);
                    }
                    holder.textAwesome.setVisibility(View.VISIBLE);
                    holder.textAwesome.setText(category.getIcon().getCategoryIcon());
                    holder.textAwesome.setTextColor(Color.parseColor(category.getIcon().getCategoryFontColor()));
                }

                if (category.getIcon().getCategoryIconSet().equals("material")) {
                    holder.textMaterial.setVisibility(View.VISIBLE);
                    holder.textMaterial.setText(category.getIcon().getCategoryIcon());
                    holder.textMaterial.setTextColor(Color.parseColor(category.getIcon().getCategoryFontColor()));

                }


                if(holder.iconContainer != null){
                    Drawable iconContainerBg = holder.iconContainer.getBackground();
                    iconContainerBg.setColorFilter(Color.parseColor(category.getIcon().getCategoryBgColor()), PorterDuff.Mode.SRC_ATOP);
                }
//                holder.name.setTextColor(Color.parseColor("#232140"));
            }
            
        }

        if(postCountStatus){
            holder.countContainer.setVisibility(View.VISIBLE);
            if(holder.iconContainer != null){
                Drawable drawable = holder.countContainer.getBackground();
                if(category.isAllContents()){
                    drawable.setColorFilter(ContextCompat.getColor(context, Resource.getColorPrimary()), PorterDuff.Mode.SRC_ATOP);
                }else{
                    drawable.setColorFilter(Color.parseColor(category.getIcon().getCategoryBgColor()), PorterDuff.Mode.SRC_ATOP);
                }
            }
            holder.count.setText(String.valueOf(category.getPostCount()));
        }else{
            holder.countContainer.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCategoryClickListener != null)
                    onCategoryClickListener.onClick(category);
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public void setViewType(int viewType) {
        if (this.viewType != viewType) {
            this.viewType = viewType;
            //notifyDataSetChanged();
        }
    }

    public void setPostCountStatus(boolean postCountStatus) {
        this.postCountStatus = postCountStatus;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.iconContainer)
        LinearLayout iconContainer;
        @Nullable
        @BindView(R.id.iconFontAwesome)
        TextAwesome textAwesome;
        @Nullable
        @BindView(R.id.iconMaterial)
        TextMaterial textMaterial;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.countContainer)
        LinearLayout countContainer;
        @BindView(R.id.count)
        TextView count;


        CategoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener) {
        this.onCategoryClickListener = onCategoryClickListener;
    }

    public interface OnCategoryClickListener {
        void onClick(Category category);
    }
}
