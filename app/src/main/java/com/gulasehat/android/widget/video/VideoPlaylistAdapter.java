package com.gulasehat.android.widget.video;

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


public class VideoPlaylistAdapter extends RecyclerView.Adapter<VideoPlaylistAdapter.VideoViewHolder> {

    private Context context;
    private ArrayList<Video> videos = new ArrayList<>();
    private int activeVideoIndex = 0;

    private OnVideoClickListener onVideoClickListener;

    public VideoPlaylistAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public VideoPlaylistAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoPlaylistAdapter.VideoViewHolder holder, final int position) {

        final Video video = videos.get(position);

        if(!video.getTitle().equals("")){
            holder.title.setText(video.getTitle());
        }else{
            holder.title.setText("-");
        }
        if(video.getLength() > 0){
            holder.length.setText(Unit.secondsFormatted(video.getLength()));
        }else{
            holder.length.setText("-:-");
        }

        holder.title.setTextColor(ContextCompat.getColor(context, R.color.text));
        holder.title.setTypeface(null, Typeface.NORMAL);
        holder.icon.setBackgroundResource(R.drawable.white_oval_with_stroke);
        holder.icon.setImageResource(R.drawable.video_icon);
        holder.itemView.setBackgroundResource(0);
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.playlist_item_color));
        if(position == videos.size() - 1){
            holder.itemView.setBackgroundResource(R.drawable.playlist_last_item);
        }

        if(activeVideoIndex == position){
            holder.title.setTextColor(ContextCompat.getColor(context, Resource.getColorPrimary()));
            holder.title.setTypeface(null, Typeface.BOLD);
            holder.icon.setBackgroundResource(R.drawable.white_oval_with_colored_stroke);
            holder.icon.setImageResource(R.drawable.video_icon_colored);
            holder.itemView.setBackgroundResource(0);
            holder.itemView.setBackgroundColor(Color.WHITE);
            if(position == videos.size() - 1){
                holder.itemView.setBackgroundResource(R.drawable.playlist_last_item_active);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVideoClickListener != null) {
                    onVideoClickListener.onClick(video, position);
                    setActiveVideoIndex(position);
                }
            }
        });


    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    @Override
    public int getItemViewType(int position) {

        return R.layout.item_video;
    }

    @Override
    public int getItemCount() {

        return videos.size();
    }

    public void setActiveVideoIndex(int activeVideoIndex) {
        this.activeVideoIndex = activeVideoIndex;
        notifyDataSetChanged();
    }

    public int getActiveVideoIndex() {
        return this.activeVideoIndex;
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.length)
        TextView length;

        VideoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnVideoClickListener(OnVideoClickListener onVideoClickListener) {
        this.onVideoClickListener = onVideoClickListener;
    }

    public interface OnVideoClickListener {
        void onClick(Video video, int index);
    }




}
