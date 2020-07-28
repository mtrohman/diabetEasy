package com.gulasehat.android.widget.sound;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.gulasehat.android.DividerItemDecoration;
import com.gulasehat.android.GlideApp;
import com.gulasehat.android.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class SoundPlaylist extends LinearLayout implements SoundPlaylistAdapter.OnSoundClickListener {

    private Context context;

    @BindView(R.id.playlist)
    protected RecyclerView playlist;
    @BindView(R.id.icon)
    protected ImageView icon;
    @BindView(R.id.playlistContainer)
    protected LinearLayout playlistContainer;
    @BindView(R.id.loading)
    protected MaterialProgressBar loading;
    @BindView(R.id.play)
    protected ImageButton play;
    @BindView(R.id.prev)
    protected ImageButton prev;
    @BindView(R.id.next)
    protected ImageButton next;

    private SoundPlaylistAdapter adapter;

    private ArrayList<Sound> sounds = new ArrayList<>();
    private int activeIndex;
    private OnBeforePlayingListener onBeforePlayingListener;
    private OnPlayerActionListener onPlayerActionListener;
    private boolean isFirstPlay = true;


    @BindView(R.id.playerView)
    PlayerView playerView;

    private ConcatenatingMediaSource concatenatedSource;
    private DefaultDataSourceFactory dataSourceFactory;
    private SimpleExoPlayer player;

    public SoundPlaylist(Context context) {
        super(context);
        this.context = context;
        initialize();
    }

    public SoundPlaylist(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize();
    }

    public SoundPlaylist(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initialize();
    }

    private void initialize() {

        inflate(getContext(), R.layout.sound_playlist, this);
        ButterKnife.bind(this);

        concatenatedSource = new ConcatenatingMediaSource();

        player = ExoPlayerFactory.newSimpleInstance(getContext());
        playerView.setControllerAutoShow(true);
        dataSourceFactory = new DefaultDataSourceFactory(getContext(), "Flink");

    }


    public void addSound(Sound sound){

        if(sounds.size() == 0){
            activeIndex = 0;
        }

        MediaSource mediaSource =
                new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(sound.getSource()));

        concatenatedSource.addMediaSource(mediaSource);
        sounds.add(sound);
    }

    public int getOuterHeight(){
        int headerHeight = 146;
        int playlistHeight;
        if(sounds.size() <= 1){
            playlistHeight = 0;
        }else{
            playlistHeight = sounds.size() * 46;
        }
        return headerHeight + playlistHeight;
    }


    public void build() {



        adapter = new SoundPlaylistAdapter(context);
        adapter.setSounds(sounds);
        adapter.setOnSoundClickListener(this);
        playlist.setHasFixedSize(false);
        playlist.setNestedScrollingEnabled(false);
        playlist.setLayoutManager(new LinearLayoutManager(context));
        playlist.addItemDecoration(new DividerItemDecoration(context.getResources()));
        playlist.setAdapter(adapter);

        if(sounds.size() == 1){
            playlistContainer.setVisibility(GONE);
        }

        playerView.setPlayer(player);

        player.addListener(new Player.EventListener() {
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                activeIndex = player.getCurrentWindowIndex();
                if(adapter.getActiveSoundIndex() != activeIndex){
                    adapter.setActiveSoundIndex(activeIndex);
                }
                prepareUI();

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {


                if(!playWhenReady){
                    play.setImageResource(R.drawable.icon_playlist_play);
                }else{
                    play.setImageResource(R.drawable.icon_playlist_pause);
                }

                switch (playbackState){
                    case Player.STATE_BUFFERING:
                        loading.setVisibility(VISIBLE);
                        icon.setVisibility(GONE);
                        Log.d("burakist2", "STATE_BUFFERING");
                        break;
                    case Player.STATE_ENDED:
                        Log.d("burakist2", "STATE_ENDED");
                        break;
                    case Player.STATE_IDLE:
                        Log.d("burakist2", "STATE_IDLE");
                        break;
                    case Player.STATE_READY:
                        Log.d("burakist2", "STATE_READY");
                        prepareUI();
                        break;
                }
            }

        });

        prepareUI();

    }

    @Override
    public void onClick(Sound sound, int position) {
        if(isFirstPlay){
            player.prepare(concatenatedSource);
            isFirstPlay = false;
        }
        if(!player.getPlayWhenReady()){
            if(onBeforePlayingListener != null){
                onBeforePlayingListener.onBeforePlaying(this);
            }
        }

        activeIndex = position;
        player.seekTo(activeIndex, 0);
        player.setPlayWhenReady(true);
    }

    @OnClick(R.id.play)
    public void onPlayClick(){
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

    @OnClick(R.id.prev)
    public void onPrevClick(){
        player.previous();
    }

    @OnClick(R.id.next)
    public void onNextClick(){
        player.next();
    }

    private void prepareUI(){

        Sound sound = sounds.get(activeIndex);

        loading.setVisibility(GONE);

        if(sound.getImage().equals("")){
            icon.setVisibility(VISIBLE);
            icon.setImageResource(R.drawable.icon_playlist_black);
        }else{
            GlideApp.with(context).load(sound.getImage()).fitCenter().centerCrop().apply(RequestOptions.circleCropTransform()).into(icon);
        }
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
        void onBeforePlaying(SoundPlaylist sp);
    }

    public interface OnPlayerActionListener{
        void onPlay();
        void onPause();
        void onStop();
    }
}