package com.gulasehat.android.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;
import com.gulasehat.android.GlideApp;
import com.gulasehat.android.R;
import com.gulasehat.android.model.Post;
import com.gulasehat.android.util.DateUtil;
import com.gulasehat.android.util.Preferences;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.util.Settings;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class PostRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Post> posts = new ArrayList<>();
    private HashMap<Integer, Integer> postPositions = new HashMap<>();
    private int lastPostPosition = 0;
    private int adCount = 0;
    private int lastAdmobAdPosition = -1;


    private OnPostClickListener onPostClickListener;
    private OnFavoriteClick onFavoriteClick;
    private boolean showFeatured = true;

    private static final int TYPE_VIEW_POST_FEATURE = 0;
    public static final int TYPE_VIEW_POST_WITH_IMAGE = 10;
    private static final int TYPE_VIEW_POST_WITH_IMAGE_ADS = 11;
    public static final int TYPE_VIEW_POST_WITHOUT_IMAGE = 20;
    private static final int TYPE_VIEW_POST_WITHOUT_IMAGE_ADS = 21;
    public static final int TYPE_VIEW_POST_BIG_BOX = 30;
    private static final int TYPE_VIEW_POST_BIG_BOX_ADS = 31;
    public static final int TYPE_VIEW_POST_SMALL_BOX = 40;
    private static final int TYPE_VIEW_POST_SMALL_BOX_ADS = 41;

    private int viewType = TYPE_VIEW_POST_WITH_IMAGE;

    private int AD_DISPLAY_FREQUENCY = 6;


    private NativeAdsManager adsManager;
    private AdLoader adsManagerAdmob;
    private List<UnifiedNativeAd> unifiedNativeAds = new ArrayList<>();


    public PostRecyclerAdapter(Context context, NativeAdsManager adsManager, int adFrequency) {
        this.context = context;
        this.adsManager = adsManager;
        AD_DISPLAY_FREQUENCY = adFrequency + 1;

    }

    public PostRecyclerAdapter(Context context, List<UnifiedNativeAd> unifiedNativeAds, int adFrequency) {
        this.context = context;
        this.unifiedNativeAds = unifiedNativeAds;
        AD_DISPLAY_FREQUENCY = adFrequency + 1;
        Log.d("burakadmob", "admob olarak instance edildi");
        Log.d("burakadmob", "size : " + unifiedNativeAds.size());
    }

    public PostRecyclerAdapter(Context context, boolean showFeatured) {
        this.context = context;
        this.showFeatured = showFeatured;
    }

    public PostRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setViewType(int viewType) {
        if (this.viewType != viewType) {
            this.viewType = viewType;
        }
    }

    private boolean isAdEnabled() {
        if (adsManager != null || unifiedNativeAds.size() > 0) {
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layout = 0;

        if (viewType == TYPE_VIEW_POST_FEATURE) {
            layout = R.layout.item_post_featured;
        } else if (viewType == TYPE_VIEW_POST_WITH_IMAGE) {
            layout = R.layout.item_post_with_image;
        } else if (viewType == TYPE_VIEW_POST_WITH_IMAGE_ADS) {
            if (adsManager != null) {
                return new AdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_with_image_ads, parent, false));
            } else {
                return new AdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_with_image_ads_admob, parent, false));
            }
        } else if (viewType == TYPE_VIEW_POST_WITHOUT_IMAGE) {
            layout = R.layout.item_post_without_image;
        } else if (viewType == TYPE_VIEW_POST_WITHOUT_IMAGE_ADS) {
            if (adsManager != null) {
                return new AdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_without_image_ads, parent, false));
            } else {
                return new AdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_without_image_ads_admob, parent, false));
            }
        } else if (viewType == TYPE_VIEW_POST_BIG_BOX) {
            layout = R.layout.item_post_big_box;
        } else if (viewType == TYPE_VIEW_POST_BIG_BOX_ADS) {
            if (adsManager != null) {
                return new AdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_big_box_ads, parent, false));
            } else {
                return new AdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_big_box_ads_admob, parent, false));
            }
        } else if (viewType == TYPE_VIEW_POST_SMALL_BOX) {
            layout = R.layout.item_post_small_box;
        } else if (viewType == TYPE_VIEW_POST_SMALL_BOX_ADS) {
            if (adsManager != null) {
                return new AdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_small_box_ads, parent, false));
            } else {
                return new AdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_small_box_ads_admob, parent, false));
            }
        }

        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            prepareFavoriteBtn((PostViewHolder) holder, position);
        }

        super.onBindViewHolder(holder, position, payloads);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (isAd(position)) {
            Log.d("burakadmob", "isAd true");

            if (adsManager != null) {
                Log.d("burakadmob", "facebook ad aktif");

                bindAdViewHolderFacebook(holder, position);
            } else if (unifiedNativeAds.size() > 0) {
                Log.d("burakadmob", "admob ad aktif");

                bindAdViewHolderAdmob(holder, position);
            } else {
                Log.d("burakadmob", "hiçbiri");

            }
        } else {
            bindPostViewHolder(holder, position);
        }
    }

    private void bindAdViewHolderAdmob(RecyclerView.ViewHolder holder, int position) {

        if (unifiedNativeAds == null || unifiedNativeAds.size() == 0) {
            Log.d("burakadmob", "Yüklenmemiş reklamlar daha");
            return;
        }

        AdViewHolder adHolder = (AdViewHolder) holder;

        if (adHolder.unifiedNativeAdView == null) {
            Log.d("burakadmob", "unifiedNativeAdView null");
            return;
        }

        if(lastAdmobAdPosition < unifiedNativeAds.size()-1){
            lastAdmobAdPosition++;
        }else if(lastAdmobAdPosition == unifiedNativeAds.size()-1){
            lastAdmobAdPosition = 0;
        }

        UnifiedNativeAd ad = unifiedNativeAds.get(lastAdmobAdPosition);

        adHolder.adProgress.setVisibility(View.GONE);
        adHolder.adContainer.setVisibility(View.VISIBLE);

        if(adHolder.adImage != null){
            if(ad.getIcon() != null){
                GlideApp.with(adHolder.adImage).load(ad.getIcon().getUri()).into(adHolder.adImage);
                adHolder.unifiedNativeAdView.setIconView(adHolder.adImage);
            }
        }

        if(adHolder.adMediaViewAdmob != null){
            if(ad.getMediaContent() != null){
                adHolder.adMediaViewAdmob.setMediaContent(ad.getMediaContent());
                adHolder.unifiedNativeAdView.setMediaView(adHolder.adMediaViewAdmob);
            }
        }


        if(adHolder.adCallToAction != null){
            if(ad.getCallToAction() != null && !ad.getCallToAction().isEmpty()){
                adHolder.adCallToAction.setVisibility(View.VISIBLE);
                adHolder.adCallToAction.setText(ad.getCallToAction());
                adHolder.unifiedNativeAdView.setCallToActionView(adHolder.adCallToAction);
            }else{
                adHolder.adCallToAction.setVisibility(View.GONE);
            }
        }


        adHolder.adTitle.setText(ad.getHeadline());
        adHolder.unifiedNativeAdView.setHeadlineView(adHolder.adTitle);

        adHolder.adBody.setText(ad.getBody());
        adHolder.unifiedNativeAdView.setBodyView(adHolder.adBody);

        adHolder.unifiedNativeAdView.setNativeAd(ad);

        Log.d("burakadmob", "admob native ad bind edildi");


    }

    private void bindAdViewHolderFacebook(RecyclerView.ViewHolder holder, int position) {

        if (!adsManager.isLoaded()) {
            Log.d("burkist1", "Yüklenmemiş reklamlar");
            return;
        }

        AdViewHolder adHolder = (AdViewHolder) holder;

        adHolder.adProgress.setVisibility(View.GONE);
        adHolder.adContainer.setVisibility(View.VISIBLE);

        NativeAd ad;

        ad = adsManager.nextNativeAd();

        adHolder.adChoicesContainer.removeAllViews();

        if (ad != null) {

            adHolder.adTitle.setText(ad.getAdvertiserName());
            if (adHolder.adTitleVertical != null) {
                adHolder.adTitleVertical.setText(ad.getAdvertiserName());
            }
            adHolder.adBody.setText(ad.getAdBodyText());


            AdIconView adIconView = adHolder.adIconView;

            if (ad.getAdBodyText().equals("")) {

                if (getItemViewType(position) == TYPE_VIEW_POST_SMALL_BOX_ADS) {
                    adIconView = adHolder.adIconViewVertical;
                    if (adHolder.headerHorizontal != null && adHolder.headerVertical != null) {
                        adHolder.headerHorizontal.setVisibility(View.GONE);
                        adHolder.headerVertical.setVisibility(View.VISIBLE);
                    }
                }

                adHolder.adBody.setVisibility(View.GONE);
            } else {
                adHolder.adBody.setVisibility(View.VISIBLE);
            }

            //adHolder.tvAdSocialContext.setText(ad.getAdSocialContext());
            //adHolder.tvAdSponsoredLabel.setText(ad.getSponsoredTranslation());
            if (adHolder.adCallToAction != null) {
                adHolder.adCallToAction.setText(ad.getAdCallToAction());
                adHolder.adCallToAction.setVisibility(
                        ad.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            }

            AdChoicesView adChoicesView = new AdChoicesView(context,
                    ad, true);
            adHolder.adChoicesContainer.addView(adChoicesView, 0);

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(adHolder.adIconView);
            if (adHolder.adMediaView != null) {
                Log.d("buraktest", "mediaview null değil");
                clickableViews.add(adHolder.adMediaView);
            } else {
                Log.d("buraktest", "mediaview null");
            }

            if (adHolder.adCallToAction != null) {
                clickableViews.add(adHolder.adCallToAction);
            }

            ad.registerViewForInteraction(
                    adHolder.itemView,
                    adHolder.adMediaView,
                    adIconView,
                    clickableViews);

        }

    }

    private void bindPostViewHolder(RecyclerView.ViewHolder holder, final int position) {

        PostViewHolder postHolder = (PostViewHolder) holder;


        final Post post = posts.get(getPostIndex(position));
        postPositions.put(post.getId(), position);

        postHolder.postTitle.setText(post.getPostTitle());

        if (postHolder.postUserPhoto != null) {
//            Picasso.get().load(post.getAuthor().getAuthorThumb()).transform(new CircleTransform()).into(postHolder.postUserPhoto);
            GlideApp.with(context).load(post.getAuthor().getAuthorThumb()).apply(RequestOptions.circleCropTransform()).into(postHolder.postUserPhoto);
        }

        if (postHolder.postUserFullName != null) {
            postHolder.postUserFullName.setText(post.getAuthor().getAuthorFullName());
        }

        if (postHolder.postUserName != null) {
            postHolder.postUserName.setText(post.getAuthor().getAuthorName());
        }

        if (postHolder.postPicture != null) {
            GlideApp.with(context)
                    .load(post.getFeaturedImage())
                    .thumbnail(GlideApp.with(context)
                            .load(Settings.getAppSettings().getDefaultPostImage())
                            .fitCenter()
                            .centerCrop())
                    .fitCenter()
                    .centerCrop()
                    .into(postHolder.postPicture);
            /*
            Picasso.get()
                    .load(post.getFeaturedImage())
                    .fit()
                    .centerCrop()
                    .into(postHolder.postPicture);
            */
        }


        if (post.isProtected()) {
            postHolder.postLockIcon.setVisibility(View.VISIBLE);

            if (getItemViewType(position) == TYPE_VIEW_POST_WITH_IMAGE || getItemViewType(position) == TYPE_VIEW_POST_WITHOUT_IMAGE) {

                if (!showFeatured || position > 0) {
                    Drawable lock = postHolder.postLockIcon.getDrawable().mutate();
                    lock.setColorFilter(ContextCompat.getColor(context, Resource.getColorPrimary()), PorterDuff.Mode.SRC_ATOP);
                }
            }
            if (getItemViewType(position) == TYPE_VIEW_POST_BIG_BOX || getItemViewType(position) == TYPE_VIEW_POST_WITHOUT_IMAGE) {
                //if (position == 0) {
                Drawable lock = postHolder.postLockIcon.getDrawable().mutate();
                lock.setColorFilter(ContextCompat.getColor(context, Resource.getColorPrimary()), PorterDuff.Mode.SRC_ATOP);
                //}
            }
        } else {
            postHolder.postLockIcon.setVisibility(View.GONE);
        }

        if (postHolder.postDate != null) {
            if (Settings.getAppSettings().getAppDate().getPostStatus()) {
                postHolder.postDate.setText(DateUtil.getFormattedDate(context, post.getPostDate()));
            } else {
                postHolder.postDate.setVisibility(View.GONE);
            }
        }

        if (postHolder.postCommentCount != null) {

            if (Settings.getAppSettings().getAppComment().isStatus() && post.isCommentAvailable()) {
                postHolder.postCommentCount.setVisibility(View.VISIBLE);
                postHolder.postCommentCount.setText(String.valueOf(post.getCommentCount()));
            } else {
                postHolder.postCommentCount.setVisibility(View.GONE);
            }
        }

        prepareFavoriteBtn(postHolder, position);

        postHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPostClickListener != null) {
                    onPostClickListener.onClicked(post);
                }
            }
        });


    }

    private int getPostIndex(int position) {
        if (!isAdEnabled()) {
            return position;
        }
        if (position < AD_DISPLAY_FREQUENCY) {
            return position;
        }
        return position - (position / AD_DISPLAY_FREQUENCY);
    }

    public void setShowFeatured(boolean showFeatured) {
        this.showFeatured = showFeatured;
    }

    private void prepareFavoriteBtn(final PostViewHolder postHolder, final int position) {

        final int pos = getPostIndex(position);

        final Post post = posts.get(pos);

        if (post.getPostType().equals(Post.POST)) {

            if (getItemViewType(position) == TYPE_VIEW_POST_WITHOUT_IMAGE) {
                if (postHolder.favoriteBtn != null) {
                    if (post.isFavorite()) {
                        postHolder.favoriteBtn.setImageResource(R.drawable.ic_favorite_full);
                    } else {
                        postHolder.favoriteBtn.setImageResource(R.drawable.ic_favorite_empty);
                    }
                }
            } else if (getItemViewType(position) == TYPE_VIEW_POST_BIG_BOX) {
                if (postHolder.favoriteNormalBtn != null) {
                    postHolder.favoriteNormalBtn.setText(String.valueOf(post.getFavoriteCount()));
                    if (post.isFavorite()) {
                        postHolder.favoriteNormalBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_full, 0, 0);
                    } else {
                        postHolder.favoriteNormalBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_empty, 0, 0);
                    }
                }
            } else {
                if (postHolder.favoriteBtn != null) {
                    if (post.isFavorite()) {
                        postHolder.favoriteBtn.setImageResource(R.drawable.ic_fav_active);
                    } else {
                        postHolder.favoriteBtn.setImageResource(R.drawable.ic_fav_normal);
                    }
                }
            }
        } else {
            if (postHolder.favoriteBtn != null) {
                postHolder.favoriteBtn.setVisibility(View.GONE);
            }
            if (postHolder.favoriteNormalBtn != null) {
                postHolder.favoriteNormalBtn.setVisibility(View.GONE);
            }
        }

        if (postHolder.favoriteBtn != null) {
            postHolder.favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onFavoriteClick != null) {

                        boolean fav = !posts.get(pos).isFavorite();

                        Log.d("buraktest1", "post fav: " + posts.get(pos).isFavorite());
                        Log.d("buraktest1", "post yeni fav: " + fav);

                        Log.d("buraktest2", postPositions.toString());

                        if (Preferences.getBoolean(Preferences.IS_LOGGED_IN, false)) {
                            posts.get(pos).setFavorite(fav);
                            prepareFavoriteBtn(postHolder, position);
                        }

                        onFavoriteClick.onFavoriteClick(post.getId(), fav);

                    }
                }
            });
        }

        if (postHolder.favoriteNormalBtn != null) {
            postHolder.favoriteNormalBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onFavoriteClick != null) {

                        boolean fav = !posts.get(pos).isFavorite();


                        if (Preferences.getBoolean(Preferences.IS_LOGGED_IN, false)) {

                            Log.d("buraktest1", "oturum açılmış");


                            posts.get(pos).setFavorite(fav);

                            if (fav) {
                                posts.get(pos).setFavoriteCount(posts.get(pos).getFavoriteCount() + 1);
                            } else {
                                posts.get(pos).setFavoriteCount(posts.get(pos).getFavoriteCount() - 1);
                            }

                            prepareFavoriteBtn(postHolder, position);
                        }

                        onFavoriteClick.onFavoriteClick(post.getId(), fav);

                    }
                }
            });
        }

        if (post.getPostType().equals(Post.POST)) {
            postHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    boolean fav = !posts.get(position).isFavorite();

                    if (Preferences.getBoolean(Preferences.IS_LOGGED_IN, false)) {
                        posts.get(position).setFavorite(fav);
                        prepareFavoriteBtn(postHolder, position);
                    }

                    onFavoriteClick.onFavoriteClick(post.getId(), fav);

                    return true;
                }
            });
        }


    }


    public ArrayList<Integer> getPostIds() {
        ArrayList<Integer> postIds = new ArrayList<>();
        for (Post post :
                posts) {
            postIds.add(post.getId());
        }
        return postIds;
    }

    public void setFavorite(int postId, boolean fav) {
        if (postPositions.get(postId) != null) {
            int pos = postPositions.get(postId);
            posts.get(getPostIndex(pos)).setFavorite(fav);
            notifyItemChanged(pos, new ArrayList<>());
        }
    }

    private boolean isAd(int position) {
        int index = position + 1;
        if (isAdEnabled() && (index % AD_DISPLAY_FREQUENCY == 0)) {
            return true;
        }
        return false;
    }

    @Override
    public int getItemViewType(int position) {

        if (this.viewType == TYPE_VIEW_POST_WITH_IMAGE && showFeatured && position == 0) {
            return TYPE_VIEW_POST_FEATURE;
        } else if (isAd(position)) {

            switch (this.viewType) {
                case TYPE_VIEW_POST_BIG_BOX:
                    return TYPE_VIEW_POST_BIG_BOX_ADS;
                case TYPE_VIEW_POST_SMALL_BOX:
                    return TYPE_VIEW_POST_SMALL_BOX_ADS;
                case TYPE_VIEW_POST_WITHOUT_IMAGE:
                    return TYPE_VIEW_POST_WITHOUT_IMAGE_ADS;
                case TYPE_VIEW_POST_WITH_IMAGE:
                default:
                    return TYPE_VIEW_POST_WITH_IMAGE_ADS;
            }
        } else {
            return this.viewType;
        }

    }

    @Override
    public int getItemCount() {
        return posts.size();// + adItems.size();
    }

    public int getItemCountExcludeAds() {
        return posts.size() - adCount;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
        //fillPostPositions(posts);
        notifyDataSetChanged();
        adsCalculate();
    }

    private void fillPostPositions(ArrayList<Post> posts) {
        for (Post post :
                posts) {
            postPositions.put(post.getId(), lastPostPosition);
            lastPostPosition++;
        }
    }

    public void addPosts(ArrayList<Post> posts) {
        this.posts.addAll(posts);
        notifyItemRangeInserted(getItemCount(), posts.size());
        adsCalculate();

    }

    private void adsCalculate() {
        if (isAdEnabled()) {
            adCount = (int) Math.ceil((double) posts.size() / AD_DISPLAY_FREQUENCY);
        }
    }


    public void setAdsManager(NativeAdsManager adsManager, int frequency) {
        this.adsManager = adsManager;
        this.AD_DISPLAY_FREQUENCY = frequency;
        notifyDataSetChanged();
    }

    public void disableAds() {

        adsManager = null;
        unifiedNativeAds.clear();
        adCount = 0;
        notifyDataSetChanged();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.postUserPhoto)
        ImageView postUserPhoto;
        @Nullable
        @BindView(R.id.postUserFullName)
        TextView postUserFullName;
        @Nullable
        @BindView(R.id.postUserName)
        TextView postUserName;
        @Nullable
        @BindView(R.id.postPicture)
        ImageView postPicture;
        @Nullable
        @BindView(R.id.favoriteBtn)
        ImageButton favoriteBtn;
        @BindView(R.id.postTitle)
        TextView postTitle;
        @Nullable
        @BindView(R.id.postCommentCount)
        Button postCommentCount;
        @Nullable
        @BindView(R.id.postDate)
        TextView postDate;
        @BindView(R.id.postLockIcon)
        ImageView postLockIcon;
        @Nullable
        @BindView(R.id.favoriteNormalBtn)
        Button favoriteNormalBtn;

        PostViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adContainer)
        RelativeLayout adContainer;
        @Nullable
        @BindView(R.id.native_ad_icon)
        AdIconView adIconView;
        @Nullable
        @BindView(R.id.ad_img_admob)
        ImageView adImage;
        @Nullable
        @BindView(R.id.native_ad_icon_vertical)
        AdIconView adIconViewVertical;
        @Nullable
        @BindView(R.id.ad_choices_container)
        LinearLayout adChoicesContainer;
        @BindView(R.id.native_ad_title)
        TextView adTitle;
        @Nullable
        @BindView(R.id.native_ad_title_vertical)
        TextView adTitleVertical;
        @BindView(R.id.native_ad_body)
        TextView adBody;
        @Nullable
        @BindView(R.id.native_ad_media)
        MediaView adMediaView;
        @Nullable
        @BindView(R.id.native_ad_media_admob)
        com.google.android.gms.ads.formats.MediaView adMediaViewAdmob;
        @Nullable
        @BindView(R.id.native_ad_call_to_action)
        Button adCallToAction;
        @Nullable
        @BindView(R.id.header_horizontal)
        RelativeLayout headerHorizontal;
        @Nullable
        @BindView(R.id.header_vertical)
        RelativeLayout headerVertical;
        @BindView(R.id.adProgress)
        MaterialProgressBar adProgress;
        @Nullable
        @BindView(R.id.unifiedNativeAdView)
        UnifiedNativeAdView unifiedNativeAdView;

        AdViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnPostClickListener(OnPostClickListener onPostClickListener) {
        this.onPostClickListener = onPostClickListener;
    }

    public void setOnFavoriteClick(OnFavoriteClick onFavoriteClick) {
        this.onFavoriteClick = onFavoriteClick;
    }

    public interface OnPostClickListener {
        void onClicked(Post post);
    }

    public interface OnFavoriteClick {
        void onFavoriteClick(int postId, boolean fav);
    }

}
