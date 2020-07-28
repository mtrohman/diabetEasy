package com.gulasehat.android.widget.video;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gulasehat.android.DividerItemDecoration;
import com.gulasehat.android.GlideApp;
import com.gulasehat.android.R;
import com.gulasehat.android.util.Unit;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class VideoPlaylist extends LinearLayout implements VideoPlaylistAdapter.OnVideoClickListener{

    @BindView(R.id.playlist)
    protected RecyclerView playlist;
    @BindView(R.id.playOrPause)
    protected RelativeLayout playOrPause;
    @BindView(R.id.playOrPauseIcon)
    protected ImageView playOrPauseIcon;
    @BindView(R.id.cover)
    protected ImageView cover;
    @BindView(R.id.loading)
    protected MaterialProgressBar loading;
    @BindView(R.id.playlist_controller_bg)
    protected LinearLayout playlistControllerBg;
    @BindView(R.id.exo_duration)
    protected TextView duration;
    @BindView(R.id.exo_progress)
    DefaultTimeBar timeBar;

    @BindView(R.id.playerView)
    PlayerView playerView;

    private ConcatenatingMediaSource concatenatedSource;
    private DefaultDataSourceFactory dataSourceFactory;
    private SimpleExoPlayer player;

    private Context context;
    private VideoPlaylistAdapter adapter;

    private ArrayList<Video> videos = new ArrayList<>();
    private int activeIndex;
    private OnBeforePlayingListener onBeforePlayingListener;
    private OnPlayerActionListener onPlayerActionListener;
    private boolean isFirstPlay = true;

    public VideoPlaylist(Context context) {
        super(context);
        this.context = context;
        initialize();

    }

    public VideoPlaylist(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize();
    }

    public VideoPlaylist(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initialize();
    }

    private void initialize() {
        inflate(getContext(), R.layout.video_playlist, this);
        ButterKnife.bind(this);

        concatenatedSource = new ConcatenatingMediaSource();
        player = ExoPlayerFactory.newSimpleInstance(getContext());
        dataSourceFactory = new DefaultDataSourceFactory(getContext(), "Flink");


    }

    public void addVideo(Video video){

        if(videos.size() == 0){
            activeIndex = 0;
        }

        MediaSource mediaSource =
                new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(video.getSource()));

        concatenatedSource.addMediaSource(mediaSource);
        videos.add(video);
    }

    public int getOuterHeight(){
        int headerHeight = 250;
        int playlistHeight;
        if(canPlaylistHide()){
            playlistHeight = 0;
        }else{
            playlistHeight = videos.size() * 46;
        }

        return headerHeight + playlistHeight;
    }

    private boolean canPlaylistHide(){
        return videos.size() <= 1;
    }

    public void build() {

        if(context == null){
            return;
        }

        adapter = new VideoPlaylistAdapter(context);
        adapter.setVideos(videos);
        adapter.setOnVideoClickListener(this);
        playlist.setHasFixedSize(true);
        playlist.setNestedScrollingEnabled(false);
        playlist.setLayoutManager(new LinearLayoutManager(context));
        playlist.addItemDecoration(new DividerItemDecoration(context.getResources()));
        playlist.setAdapter(adapter);




        if(canPlaylistHide()){
            playlist.setVisibility(GONE);
            playlistControllerBg.setBackgroundResource(R.drawable.playlist_bar_bg_rounded);
        }

        playerView.setPlayer(player);




        player.addListener(new Player.EventListener() {
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                activeIndex = player.getCurrentWindowIndex();
                if(adapter.getActiveVideoIndex() != activeIndex){
                    adapter.setActiveVideoIndex(activeIndex);
                }
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {


                if(!playWhenReady){
                    playOrPauseIcon.setVisibility(VISIBLE);
                    playOrPauseIcon.setImageResource(R.drawable.icon_playlist_big_play);
                }else{
                    playOrPauseIcon.setVisibility(GONE);
                    playOrPauseIcon.setImageResource(R.drawable.icon_playlist_big_pause);
                }

                switch (playbackState){
                    case Player.STATE_BUFFERING:
                        playOrPauseIcon.setVisibility(GONE);
                        loading.setVisibility(VISIBLE);
                        Log.d("burakist2", "STATE_BUFFERING");
                        break;
                    case Player.STATE_ENDED:
                        Log.d("burakist2", "STATE_ENDED");
                        activeIndex = 0;
                        player.seekTo(activeIndex,0);
                        player.setPlayWhenReady(true);
                        break;
                    case Player.STATE_IDLE:
                        Log.d("burakist2", "STATE_IDLE");
                        break;
                    case Player.STATE_READY:
                        Log.d("burakist2", "STATE_READY");
                        loading.setVisibility(GONE);
                        cover.setVisibility(GONE);
                        break;
                }
            }

        });

        if(videos.size() > 0){
            Video video = videos.get(0);
            GlideApp.with(context).load(video.getSource()).fitCenter().centerCrop().into(cover);
            if(video.getLength() > 0){
                duration.setText(Unit.secondsFormatted(video.getLength()));
            }else{
                duration.setText("-:-");
            }
        }


        prepareUI();


    }

    @Override
    public void onClick(Video video, int index) {

        if(isFirstPlay){
            player.prepare(concatenatedSource);
            isFirstPlay = false;
        }

        if(!player.getPlayWhenReady()){
            if(onBeforePlayingListener != null){
                onBeforePlayingListener.onBeforePlaying(this);
            }
        }

        activeIndex = index;

        player.seekTo(activeIndex, 0);
        player.setPlayWhenReady(true);

        prepareUI();
    }

    private void prepareUI(){

        Video video = videos.get(activeIndex);

        if(!video.getImg().equals("")){
                    GlideApp.with(context).load(video.getImg()).fitCenter().centerCrop().into(cover);
        }

    }


    @OnClick(R.id.playOrPause)
    public void onPlayOrPauseClick(View view){

        if(isFirstPlay){
            player.prepare(concatenatedSource);
            isFirstPlay = false;
        }

        if(!player.getPlayWhenReady()){
            if(onBeforePlayingListener != null){
                onBeforePlayingListener.onBeforePlaying(this);
            }
        }

        player.setPlayWhenReady(!player.getPlayWhenReady());
    }

    @OnClick(R.id.fullscreen)
    public void onFullScreen(){

        String source = videos.get(activeIndex).getSource();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(source));
        intent.setDataAndType(Uri.parse(source), "video/mp4");
        context.startActivity(intent);

    }

    public void destroy(){
        Log.d("burkist3", "Video Playlist Release");
        player.release();
    }

    public void pause(){
        Log.d("burkist3", "Video Playlist onPause");
        player.setPlayWhenReady(false);
    }

    public void setOnBeforePlayingListener(OnBeforePlayingListener onBeforePlayingListener) {
        this.onBeforePlayingListener = onBeforePlayingListener;
    }

    public void setOnPlayerActionListener(OnPlayerActionListener onPlayerActionListener) {
        this.onPlayerActionListener = onPlayerActionListener;
    }

    public interface OnBeforePlayingListener{
        void onBeforePlaying(VideoPlaylist sp);
    }

    public interface OnPlayerActionListener{
        void onPlay();
        void onPause();
        void onStop();
    }
}
