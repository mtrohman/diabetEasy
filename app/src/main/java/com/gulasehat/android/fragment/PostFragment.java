package com.gulasehat.android.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdsManager;
import com.gulasehat.android.BaseFragment;
import com.gulasehat.android.DividerItemDecoration;
import com.gulasehat.android.FlinkApplication;
import com.gulasehat.android.PostActivity;
import com.gulasehat.android.R;
import com.gulasehat.android.adapter.PostRecyclerAdapter;
import com.gulasehat.android.event.OnFavoriteChanged;
import com.gulasehat.android.event.OnFetchedPostData;
import com.gulasehat.android.event.OnPostCategoryChangedEvent;
import com.gulasehat.android.event.ScrollTopEvent;
import com.gulasehat.android.event.filter.PostFiltersEvent;
import com.gulasehat.android.model.Ads;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Post;
import com.gulasehat.android.model.PostArgs;
import com.gulasehat.android.model.PostData;
import com.gulasehat.android.service.PostService;
import com.gulasehat.android.util.Common;
import com.gulasehat.android.util.FavoriteManager;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.util.Settings;
import com.gulasehat.android.widget.Alert;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.rockerhieu.rvadapter.endless.EndlessRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        PostRecyclerAdapter.OnPostClickListener,
        PostRecyclerAdapter.OnFavoriteClick,
        EndlessRecyclerViewAdapter.RequestToLoadMoreListener,
        NativeAdsManager.Listener{

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.noContentContainer)
    protected LinearLayout noContentContainer;

    private PostRecyclerAdapter postRecyclerAdapter;
    private EndlessRecyclerViewAdapter endlessAdapter;
    private PostArgs args = new PostArgs();

    private int filterIndex = -1;
    private boolean isShown = false;

    private int totalPostCount = 0;
    private int limit = 0;
    private boolean showFeature = true;

    private PostData postData;

    private NativeAdsManager adsManager;

    private ArrayList<UnifiedNativeAd> ads = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post, container, false);

        ButterKnife.bind(this, view);

        setupPosts();
        PostService.posts(onPostFetchedListener, args);


        return view;
    }


    private void setupPosts() {

        if (getActivity() != null) {

            if(Settings.getAppSettings().getAds().getAdNative().isStatus() &&
                    Settings.getAppSettings().getAds().getAdNative().getNetwork().equals(Ads.AD_NETWORK_FACEBOOK) &&
                    !Settings.getAppSettings().getAds().getAdNative().getAdPlacementId().isEmpty()){



                adsManager = new NativeAdsManager(getActivity(), Settings.getAppSettings().getAds().getAdNative().getAdPlacementId(), 5);

                adsManager.loadAds();
                adsManager.setListener(this);


                postRecyclerAdapter = new PostRecyclerAdapter(getActivity(), adsManager, Settings.getAppSettings().getAds().getAdNative().getFrequency());


            }else if(Settings.getAppSettings().getAds().getAdNative().isStatus() &&
            Settings.getAppSettings().getAds().getAdNative().getNetwork().equals(Ads.AD_NETWORK_ADMOB) &&
            !Settings.getAppSettings().getAds().getAdNative().getAdUnitId().isEmpty()){
                postRecyclerAdapter = new PostRecyclerAdapter(getActivity(), FlinkApplication.getNativeAdsAdmob(), Settings.getAppSettings().getAds().getAdNative().getFrequency());
            }else{

                postRecyclerAdapter = new PostRecyclerAdapter(getActivity());
            }


            Intent intent = getActivity().getIntent();
            if (intent.hasExtra("cat_id")) {
                args.setCategory(intent.getIntExtra("cat_id", 0));
                showFeature = false;
            }
            if (intent.hasExtra("year")) {
                args.setYear(intent.getIntExtra("year", 0));
                showFeature = false;
            }
            if (intent.hasExtra("month")) {
                args.setMonth(intent.getIntExtra("month", 0));
                showFeature = false;
            }
            if (intent.hasExtra("tag")) {
                args.setTag(intent.getStringExtra("tag"));
                showFeature = false;
            }
            if (intent.hasExtra("author")) {
                args.setAuthor(intent.getIntExtra("author", 0));
                showFeature = false;
            }

        }

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);



    }
    public void preparePosts(){

        if(getActivity() == null){
            return;
        }

        EventBus.getDefault().post(new OnFetchedPostData(postData));


        Log.d("burkist", "filter: " + postData.getSortType());
        filterIndex = Common.findFilterIndex(postData.getSortType(), R.array.filter_post_keys);

        if(args.getPage() > 1){
            addPosts(postData.getPosts());
            return;
        }

        totalPostCount = postData.getPostCount();
        limit = postData.getLimit();

        postRecyclerAdapter.setShowFeatured(showFeature);
        postRecyclerAdapter.setOnPostClickListener(this);
        postRecyclerAdapter.setOnFavoriteClick(this);
        postRecyclerAdapter.setViewType(postData.getLayout());

        endlessAdapter = new EndlessRecyclerViewAdapter(postRecyclerAdapter, this);


        if(postData.getLayout() == PostRecyclerAdapter.TYPE_VIEW_POST_SMALL_BOX){
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);


            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(endlessAdapter.getItemViewType(position) == EndlessRecyclerViewAdapter.TYPE_PENDING){
                        return 2;
                    }
                    return 1;
                }
            });


        }else{
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        if(recyclerView.getItemDecorationCount() > 0){
            recyclerView.removeItemDecorationAt(0);
        }

        if(postData.getLayout() == PostRecyclerAdapter.TYPE_VIEW_POST_WITH_IMAGE || postData.getLayout() == PostRecyclerAdapter.TYPE_VIEW_POST_WITHOUT_IMAGE){
            recyclerView.addItemDecoration(new DividerItemDecoration(getResources()));
        }


        endlessAdapter.setPendingViewId(R.layout.item_loading);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(endlessAdapter);




        if (postData.getPostCount() == 0) {
            noContentContainer.setVisibility(View.VISIBLE);
        } else {
            noContentContainer.setVisibility(View.GONE);
        }

        addPosts(postData.getPosts());

    }



    private void actionFilter() {

        if(postRecyclerAdapter.getItemCount() == 0){
            Alert.make(getContext(), R.string.not_filter, Alert.ALERT_TYPE_WARNING);
            return;
        }

        if(isShown){
            return;
        }
        isShown = true;

        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title(R.string.choose_filter)
                .itemsColorRes(R.color.text)
                .items(R.array.filter_post_texts)
                .itemsCallbackSingleChoice(filterIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        filterChange(which);
                        return true;
                    }
                })
                .iconRes(R.drawable.icon_filter_black)
                .widgetColorRes(Resource.getColorPrimary())
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShown = false;
                    }
                })
                .show();
    }


    PostService.OnPostFetchedListener onPostFetchedListener = new PostService.OnPostFetchedListener() {
        @Override
        public void onSuccess(PostData postData) {
            swipeRefreshLayout.setRefreshing(false);
            PostFragment.this.postData = postData;
            preparePosts();
        }

        @Override
        public void onFailed(ApiResponse response) {
            swipeRefreshLayout.setRefreshing(false);
            Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
        }
    };

    private void addPosts(ArrayList<Post> posts){


        if(args.getPage() > 1){
            postRecyclerAdapter.addPosts(posts);
        }else{
            postRecyclerAdapter.setPosts(posts);
        }

        endlessAdapter.onDataReady(true);

        int totalPage = (int) Math.ceil((double) totalPostCount / limit);
        int curPage = (int) Math.ceil((double) postRecyclerAdapter.getItemCountExcludeAds() / limit);

        Log.d("burkist123", "cur: " + curPage + " - total: " + totalPage);

        if(postData.getPage() == totalPage){
            endlessAdapter.onDataReady(false);
        }else{
            endlessAdapter.onDataReady(true);
        }


    }


    private void filterChange(int which) {

        String key = getResources().getStringArray(R.array.filter_post_keys)[which];

        swipeRefreshLayout.setRefreshing(true);

        Log.d("burkist", "giden: " + key);
        args.setSortType(key);
        args.setPage(1);
        PostService.posts(onPostFetchedListener, args);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostFiltersEvent(PostFiltersEvent event) {
        actionFilter();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostCategoryChangedEvent(OnPostCategoryChangedEvent event) {
        swipeRefreshLayout.setRefreshing(true);
        args.setCategory(event.getCategory());
        PostService.posts(onPostFetchedListener, args);
    }


    @Override
    public void onRefresh() {
        args.setPage(1);
        PostService.posts(onPostFetchedListener, args);
    }

    @Override
    public void onClicked(Post post) {

        Intent intent = new Intent(getContext(), PostActivity.class);
        intent.putExtra("id", post.getId());
        startActivity(intent);

    }




    @Override
    public void onLoadMoreRequested() {

        if(totalPostCount > 0){

            int totalPage = (int) Math.ceil((double) totalPostCount / limit);

            int page = (int) Math.ceil((double) postRecyclerAdapter.getItemCountExcludeAds() / limit);

            args.setPage(page+1);

            PostService.posts(onPostFetchedListener, args);

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScrollTop(ScrollTopEvent event){
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onFavoriteClick(int postId, boolean fav) {
        FavoriteManager.handle(getContext(), postId, fav);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onFavoriteChanged(OnFavoriteChanged event){
        //Log.d("buraktest3", "onFavoriteChanged: " + event.getPostId());
        postRecyclerAdapter.setFavorite(event.getPostId(), event.isFav());
    }

    @Override
    public void onAdsLoaded() {
        if (getActivity() == null) {
            return;
        }

        if(postRecyclerAdapter != null){
            postRecyclerAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onAdError(AdError error) {

        if(postRecyclerAdapter != null){
            postRecyclerAdapter.disableAds();
        }
        Log.d("burkist1", error.getErrorCode() + " - " + error.getErrorMessage());
    }

}