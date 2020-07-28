package com.gulasehat.android.widget.sound;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gulasehat.android.R;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.util.Unit;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SoundPlaylistAdapter extends RecyclerView.Adapter<SoundPlaylistAdapter.SoundViewHolder> {

    private Context context;
    private ArrayList<Sound> sounds = new ArrayList<>();
    private int activeSoundIndex = 0;

    private OnSoundClickListener onSoundClickListener;

    SoundPlaylistAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SoundPlaylistAdapter.SoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new SoundViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final SoundPlaylistAdapter.SoundViewHolder holder, final int position) {

        final Sound sound = sounds.get(position);

        if(!sound.getTitle().equals("")){
            holder.title.setText(sound.getTitle());
        }else{
            holder.title.setText("-");
        }
        if(sound.getLength() > 0){
            holder.length.setText(Unit.secondsFormatted(sound.getLength()));
        }else{
            holder.length.setText("-:-");
        }

        holder.title.setTextColor(ContextCompat.getColor(context, R.color.text));
        holder.title.setTypeface(null, Typeface.NORMAL);
        holder.icon.setBackgroundResource(R.drawable.white_oval_with_stroke);
        holder.icon.setImageResource(R.drawable.sound_icon);
        holder.itemView.setBackgroundResource(0);
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.playlist_item_color));
        if(position == sounds.size() - 1){
            holder.itemView.setBackgroundResource(R.drawable.playlist_last_item);
        }

        if(activeSoundIndex == position){
            holder.title.setTextColor(ContextCompat.getColor(context, Resource.getColorPrimary()));
            holder.title.setTypeface(null, Typeface.BOLD);
            holder.icon.setBackgroundResource(R.drawable.white_oval_with_colored_stroke);
            holder.icon.setImageResource(R.drawable.sound_icon_colored);
            holder.itemView.setBackgroundResource(0);
            holder.itemView.setBackgroundColor(Color.WHITE);
            if(position == sounds.size() - 1){
                holder.itemView.setBackgroundResource(R.drawable.playlist_last_item_active);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSoundClickListener != null) {
                    onSoundClickListener.onClick(sound, position);
                    setActiveSoundIndex(position);
                }
            }
        });


    }

    public void setSounds(ArrayList<Sound> sounds) {
        this.sounds = sounds;
    }

    @Override
    public int getItemViewType(int position) {

        return R.layout.item_sound;
    }

    @Override
    public int getItemCount() {

        return sounds.size();
    }

    public void setActiveSoundIndex(int activeSoundIndex) {
        this.activeSoundIndex = activeSoundIndex;
        notifyDataSetChanged();
    }

    public int getActiveSoundIndex() {
        return activeSoundIndex;
    }

    static class SoundViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.length)
        TextView length;

        SoundViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnSoundClickListener(OnSoundClickListener onSoundClickListener) {
        this.onSoundClickListener = onSoundClickListener;
    }


    public interface OnSoundClickListener {
        void onClick(Sound sound, int index);
    }




}
