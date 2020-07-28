package com.gulasehat.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gulasehat.android.R;
import com.gulasehat.android.util.Resource;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemeRecyclerAdapter extends RecyclerView.Adapter<ThemeRecyclerAdapter.ThemeViewHolder> {

    private Context context;
    private List<String> themes;
    private ThemeViewHolder themeViewHolder;


    private OnThemeClickListener onThemeClickListener;

    public ThemeRecyclerAdapter(Context context, List<String> themes) {
        this.context = context;
        this.themes = themes;
    }

    @NonNull
    @Override
    public ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ThemeViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ThemeViewHolder holder, final int position) {

        final String theme = themes.get(position);

        holder.theme.setBackgroundResource(Resource.getDrawableResID("theme_" + theme.toLowerCase(Locale.ENGLISH)));
        holder.theme.setImageResource(0);

        if(Resource.getAppTheme().equals(theme)){
            holder.theme.setImageResource(R.drawable.icon_tick_light);
            themeViewHolder = holder;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onThemeClickListener != null){
                    onThemeClickListener.onClicked(theme);

                    themeViewHolder = holder;
                    notifyDataSetChanged();
                }
            }
        });


    }

    // Layouta burada karar vereceğiz
    @Override
    public int getItemViewType(int position) {

        return R.layout.item_theme;
    }

    @Override
    public int getItemCount() {
        return themes.size();
    }


    static class ThemeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.theme)
        protected ImageView theme;


        ThemeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnThemeClickListener(OnThemeClickListener onThemeClickListener) {
        this.onThemeClickListener = onThemeClickListener;
    }

    public interface OnThemeClickListener{
        void onClicked(String theme);
    }

}
