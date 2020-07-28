package com.gulasehat.android.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gulasehat.android.BaseFragment;
import com.gulasehat.android.DividerItemDecoration;
import com.gulasehat.android.PostActivity;
import com.gulasehat.android.R;
import com.gulasehat.android.adapter.PostRecyclerAdapter;
import com.gulasehat.android.event.OnFavoriteChanged;
import com.gulasehat.android.event.OnSearchResultEvent;
import com.gulasehat.android.event.OnSearchingEvent;
import com.gulasehat.android.event.filter.PostFiltersEvent;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Post;
import com.gulasehat.android.model.SearchArgs;
import com.gulasehat.android.model.SearchResult;
import com.gulasehat.android.service.CommonService;
import com.gulasehat.android.util.Common;
import com.gulasehat.android.util.FavoriteManager;
import com.gulasehat.android.util.Preferences;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.widget.Alert;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchPostFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,
        PostRecyclerAdapter.OnPostClickListener,
        PostRecyclerAdapter.OnFavoriteClick {

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.noContentContainer)
    protected LinearLayout noContentContainer;
    @BindView(R.id.homeContainer)
    protected LinearLayout homeContainer;
    @BindView(R.id.loadingContainer)
    protected LinearLayout loadingContainer;

    private int filterIndex = -1;
    private boolean isShown = false;

    private PostRecyclerAdapter postRecyclerAdapter;
    private SearchArgs args = new SearchArgs();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_post, container, false);

        ButterKnife.bind(this, view);

        setupPosts();

        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchingEvent(OnSearchingEvent event){
        hideAllContainers();
        loadingContainer.setVisibility(View.VISIBLE);

    }

    private void hideAllContainers(){
        loadingContainer.setVisibility(View.GONE);
        homeContainer.setVisibility(View.GONE);
        noContentContainer.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
    }

    private void setupPosts() {

        postRecyclerAdapter = new PostRecyclerAdapter(getContext(), false);
        postRecyclerAdapter.setOnPostClickListener(this);
        postRecyclerAdapter.setOnFavoriteClick(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources()));
        recyclerView.setAdapter(postRecyclerAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);


        args.setType("post");

    }


    private void actionFilter() {

        if (postRecyclerAdapter.getItemCount() == 0) {
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
                .items(R.array.filter_search_post_texts)
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

    private void filterChange(int which) {

        String key = getResources().getStringArray(R.array.filter_search_post_keys)[which];

        swipeRefreshLayout.setRefreshing(true);

        args.setSortType(key);

        CommonService.search(args, onSearchResultListener);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostFiltersEvent(PostFiltersEvent event) {
        actionFilter();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchResultEvent(OnSearchResultEvent event) {

        swipeRefreshLayout.setRefreshing(false);

        if (event != null) {


            if(event.getResult().getPostData().getPage() > 1){
                postRecyclerAdapter.addPosts(event.getResult().getPostData().getPosts());
            }else{
                postRecyclerAdapter.setPosts(event.getResult().getPostData().getPosts());
            }

            args.setQuery(Preferences.getString(Preferences.SEARCH_QUERY, ""));
            args.setSortType(event.getResult().getPostData().getSortType());

            filterIndex = Common.findFilterIndex(event.getResult().getPostData().getSortType(), R.array.filter_search_post_keys);

            if (event.getResult().getPostData().getPostCount() > 0) {
                showList();
            } else {
                hideList();
            }
        }
    }


    private void showList() {
        homeContainer.setVisibility(View.GONE);
        noContentContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    private void hideList() {
        homeContainer.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.GONE);
        noContentContainer.setVisibility(View.VISIBLE);
    }

    private CommonService.OnSearchResultListener onSearchResultListener = new CommonService.OnSearchResultListener() {
        @Override
        public void onSuccess(SearchResult result) {
            swipeRefreshLayout.setRefreshing(false);

            Log.d("burkist", "p: " + result.getPostData().getPage());

            if(result.getPostData().getPage() > 1){
                postRecyclerAdapter.addPosts(result.getPostData().getPosts());
            }else{
                postRecyclerAdapter.setPosts(result.getPostData().getPosts());
            }

            filterIndex = Common.findFilterIndex(result.getPostData().getSortType(), R.array.filter_search_post_keys);

            args.setSortType(result.getPostData().getSortType());

            if (result.getPostData().getPostCount() > 0) {
                showList();
            } else {
                hideList();
            }
        }

        @Override
        public void onFailed(ApiResponse response) {
            swipeRefreshLayout.setRefreshing(false);
            Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
        }
    };

    @Override
    public void onRefresh() {
        args.setPage(1);
        CommonService.search(args, onSearchResultListener);
    }

    @Override
    public void onClicked(Post post) {
        Intent intent = new Intent(getContext(), PostActivity.class);
        intent.putExtra("id", post.getId());
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(int postId, boolean fav) {
        FavoriteManager.handle(getContext(), postId, fav);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onFavoriteChanged(OnFavoriteChanged event){
        postRecyclerAdapter.setFavorite(event.getPostId(), event.isFav());
    }
}