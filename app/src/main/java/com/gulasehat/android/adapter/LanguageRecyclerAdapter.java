package com.gulasehat.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gulasehat.android.GlideApp;
import com.gulasehat.android.R;
import com.gulasehat.android.model.Language;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LanguageRecyclerAdapter extends RecyclerView.Adapter<LanguageRecyclerAdapter.LanguageViewHolder> {

    private Context context;
    private List<Language> languages;
    private int active;


    private OnLanguageClickListener onLanguageClickListener;

    public LanguageRecyclerAdapter(Context context, ArrayList<Language> languages) {
        this.context = context;
        this.languages = languages;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new LanguageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final LanguageViewHolder holder, final int position) {

        final Language language = languages.get(position);

//        Picasso.get().load(language.getFlag()).fit().into(holder.flag);

        GlideApp.with(context).load(language.getFlag()).fitCenter().into(holder.flag);

        holder.language.setText(language.getName());

        holder.selection.setBackgroundResource(R.drawable.radio_bg);
        holder.selection.setImageResource(0);



        if(language.isSelected()){
            holder.selection.setBackgroundResource(R.drawable.radio_bg_selected);
            holder.selection.setImageResource(R.drawable.icon_tick_white);
            active = position;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onLanguageClickListener != null){
                    onLanguageClickListener.onClicked(language);


                }
                languages.get(active).setSelected(false);
                languages.get(position).setSelected(true);
                active = position;

                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return languages.size();
    }

    static class LanguageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.flag)
        ImageView flag;
        @BindView(R.id.language)
        TextView language;
        @BindView(R.id.selection)
        ImageView selection;

        LanguageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnLanguageClickListener(OnLanguageClickListener onLanguageClickListener) {
        this.onLanguageClickListener = onLanguageClickListener;
    }

    public interface OnLanguageClickListener{
        void onClicked(Language language);
    }

    public Language getActiveLanguage(){
        if(languages != null && languages.get(active) != null){
            return languages.get(active);
        }
        return null;
    }

}
