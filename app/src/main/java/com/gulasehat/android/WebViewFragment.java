package com.gulasehat.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gulasehat.android.event.OnWebViewLoaded;
import com.gulasehat.android.model.Photo;
import com.gulasehat.android.model.Post;
import com.gulasehat.android.model.Sound;
import com.gulasehat.android.model.Video;
import com.gulasehat.android.util.Settings;
import com.gulasehat.android.util.Util;
import com.gulasehat.android.widget.ImageSlider;
import com.gulasehat.android.widget.VideoView;
import com.gulasehat.android.widget.sound.SoundPlaylist;
import com.gulasehat.android.widget.video.VideoPlaylist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewFragment extends BaseFragment implements VideoPlaylist.OnBeforePlayingListener, SoundPlaylist.OnBeforePlayingListener {

    @BindView(R.id.content)
    RelativeLayout content;
    @BindView(R.id.webView)
    protected WebView webView;
    @BindView(R.id.customViewContainer)
    protected FrameLayout customViewContainer;

    private WebChromeClient.CustomViewCallback customViewCallback;
    private View mCustomView;
    private myWebChromeClient mWebChromeClient;
    private myWebViewClient mWebViewClient;

    public VideoView videoView = VideoView.getInstance();
    /* access modifiers changed from: private */
    public WebChromeClient.CustomViewCallback videoViewCallback;


    private Activity activity;
    private Post post;
    private DisplayMetrics metrics;
    private ArrayList<SoundPlaylist> mAllSoundPlaylist = new ArrayList<>();
    private ArrayList<VideoPlaylist> mAllVideoPlaylist = new ArrayList<>();
    private ArrayList<String> mediaOrder = new ArrayList<>();
    private ArrayList<ImageSlider> galleries = new ArrayList<>();
    private int galleryIndex = 0;
    private int soundIndex = 0;
    private int videoIndex = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        metrics = Resources.getSystem().getDisplayMetrics();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareWebView();
    }


    public void setPostData(Activity activity, Post post) {
        this.activity = activity;
        this.post = post;
    }

    class CrmClient extends WebChromeClient {
        @Override
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //advancedWebView.onActivityResult(requestCode, resultCode, intent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void prepareWebView() {
        if (post == null) {
            return;
        }

        String source = post.getPostSourceUrl();
        if (Util.isRTL()) {
            source += "?is_rtl=true";
        }

        webView.getSettings().setJavaScriptEnabled(true);

        this.webView.getSettings().setAllowFileAccess(true);
        this.webView.getSettings().setAppCacheEnabled(true);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.getSettings().setDefaultTextEncodingName("utf-8");
        this.webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        // Kopyalama koruması
        if(post.getPostType().equals(Post.POST) && Settings.getAppSettings().getAppPostDetail() != null && !Settings.getAppSettings().getAppPostDetail().isCopy() ||
                post.getPostType().equals(Post.PAGE) && Settings.getAppSettings().getAppPageDetail() != null && !Settings.getAppSettings().getAppPageDetail().isCopy()){
                this.webView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });
                this.webView.setLongClickable(false);
        }



        webView.loadUrl(source);
        webView.addJavascriptInterface(new WebViewJavaScriptInterface(getContext()), "Android");
        webView.setWebChromeClient(new myWebChromeClient());
        webView.setWebViewClient(new myWebViewClient());

    }

    private void initializePageItems() {



        webView.evaluateJavascript("getMediaOrder();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(final String value) {


                Gson gson = new Gson();
                mediaOrder = gson.fromJson(value, new TypeToken<ArrayList<String>>() {
                }.getType());

                prepareGalleries();
                prepareSoundPlaylists();
                prepareVideoPlaylists();

                prepareOrderMedia();


            }
        });
    }

    private void prepareGalleries() {

        if (getContext() == null) {
            return;
        }

        if (post.getGalleries().size() == 0) {
            return;
        }

        for (final ArrayList<Photo> gallery :
                post.getGalleries()) {

            final ImageSlider slider = new ImageSlider(getContext(), getFragmentManager());
            slider.setOnSlideClickListener(new ImageSlider.OnSlideClickListener() {
                @Override
                public void onClick(String url) {
                    showPhoto(slider, url);
                }
            });

            for (Photo photo :
                    gallery) {
                slider.addImage(photo.getUrl());
            }

            galleries.add(slider);
            slider.build();
        }
    }

    private void prepareSoundPlaylists() {

        if (getContext() == null) {
            return;
        }

        if (post.getSounds().size() == 0) {
            return;
        }

        for (ArrayList<Sound> sounds :
                post.getSounds()) {

            final SoundPlaylist playlist = new SoundPlaylist(getContext());
            playlist.setOnBeforePlayingListener(this);

            for (Sound sound :
                    sounds
            ) {
                playlist.addSound(new com.gulasehat.android.widget.sound.Sound(sound.getId(), sound.getTitle(), sound.getUrl(), sound.getImage(), sound.getLength()));
            }

            playlist.build();

            mAllSoundPlaylist.add(playlist);
        }
    }

    private void prepareVideoPlaylists() {

        if (getContext() == null) {
            return;
        }

        if (post.getVideos().size() == 0) {
            return;
        }

        for (ArrayList<Video> videos : post.getVideos()) {
            final VideoPlaylist playlist = new VideoPlaylist(getContext());
            playlist.setOnBeforePlayingListener(this);


            for (Video video :
                    videos) {
                playlist.addVideo(new com.gulasehat.android.widget.video.Video(video.getTitle(), video.getUrl(), video.getImage(), video.getLength()));
            }


            playlist.build();


            mAllVideoPlaylist.add(playlist);

        }

    }

    private void prepareOrderMedia() {

        if (getContext() == null) {
            return;
        }

        if (mediaOrder.size() > 0) {
            for (String type :
                    mediaOrder) {
                switch (type) {
                    case "gallery":
                        addGallery();
                        break;
                    case "sound-list":
                        addSoundPlaylist();
                        break;
                    case "video-list":
                        addVideoPlaylist();
                        break;
                }
            }
        }
        EventBus.getDefault().post(new OnWebViewLoaded());

    }


    private void addGallery() {

        if (getContext() == null) {
            return;
        }

        if (galleries.size() == 0) {
            return;
        }

        final ImageSlider gallery = galleries.get(galleryIndex);

        webView.evaluateJavascript("setGalleryHeight(" + galleryIndex + ", 250)", null);
        webView.evaluateJavascript("getGalleryOffset(" + galleryIndex + ")", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(final String value) {

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, (int) (metrics.scaledDensity * Float.valueOf(value)), 0, 0);
                gallery.setLayoutParams(lp);
                content.addView(gallery);

            }
        });

        galleryIndex++;
    }


    private void addSoundPlaylist() {

        if (getContext() == null) {
            return;
        }

        if (mAllSoundPlaylist.size() == 0) {
            return;
        }

        final SoundPlaylist soundPlaylist = mAllSoundPlaylist.get(soundIndex);

        webView.evaluateJavascript("setSoundListHeight(" + soundIndex + ", " + soundPlaylist.getOuterHeight() + ")", null);
        webView.evaluateJavascript("getSoundListOffset(" + soundIndex + ")", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(final String value) {

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, (int) (metrics.scaledDensity * Float.valueOf(value)), 0, 0);
                soundPlaylist.setLayoutParams(lp);
                content.addView(soundPlaylist);

            }
        });

        soundIndex++;
    }


    private void addVideoPlaylist() {

        if (getContext() == null) {
            return;
        }

        if (mAllVideoPlaylist.size() == 0) {
            return;
        }

        final VideoPlaylist videoPlaylist = mAllVideoPlaylist.get(videoIndex);


        webView.evaluateJavascript("setVideoListHeight(" + videoIndex + ", " + videoPlaylist.getOuterHeight() + ")", null);
        webView.evaluateJavascript("getVideoListOffset(" + videoIndex + ")", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(final String value) {
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, (int) (metrics.scaledDensity * Float.valueOf(value)), 0, 0);
                videoPlaylist.setLayoutParams(lp);
                content.addView(videoPlaylist);
            }
        });

        videoIndex++;
    }

    public void destroy() {
        if (mAllVideoPlaylist.size() > 0) {
            for (VideoPlaylist playlist :
                    mAllVideoPlaylist) {
                playlist.destroy();
            }
        }
        if (mAllSoundPlaylist.size() > 0) {
            for (SoundPlaylist playlist :
                    mAllSoundPlaylist) {
                playlist.destroy();
            }
        }
    }

    public void pause() {

        if (mAllVideoPlaylist.size() > 0) {
            for (VideoPlaylist playlist : mAllVideoPlaylist) {
                playlist.pause();
            }
        }
        if (mAllSoundPlaylist.size() > 0) {
            for (SoundPlaylist playlist : mAllSoundPlaylist) {
                playlist.pause();
            }
        }
    }

    @Override
    public void onBeforePlaying(SoundPlaylist sp) {
        pause();
    }

    @Override
    public void onBeforePlaying(VideoPlaylist sp) {
        pause();
    }

    class myWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
            super.onShowCustomView(view, customViewCallback);
            videoViewCallback = customViewCallback;
            videoView.show(activity);
            videoView.setVideoLayout(view);
        }

        public void onHideCustomView() {
            super.onHideCustomView();
            videoView.dismiss();
            videoViewCallback.onCustomViewHidden();
        }

/*        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {

            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mCustomView = view;
            customViewCallback = callback;
            EventBus.getDefault().post(new OnFullscreenEnabled(mCustomView));
        }


        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            if (mCustomView == null)
                return;

            EventBus.getDefault().post(new OnFullscreenDisabled(mCustomView));

            mCustomView.setVisibility(View.GONE);
            customViewCallback.onCustomViewHidden();
            mCustomView = null;
        }*/
    }

    class myWebViewClient extends WebViewClient {
        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            } else {
                return false;
            }
        }

        public void onPageFinished(WebView view, String url) {
            initializePageItems();
        }
    }


}
