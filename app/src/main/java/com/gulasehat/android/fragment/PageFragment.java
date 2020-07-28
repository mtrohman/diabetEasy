package com.gulasehat.android.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gulasehat.android.BaseFragment;
import com.gulasehat.android.DividerItemDecoration;
import com.gulasehat.android.PostActivity;
import com.gulasehat.android.R;
import com.gulasehat.android.adapter.PostRecyclerAdapter;
import com.gulasehat.android.event.OnFetchedPageData;
import com.gulasehat.android.event.filter.PageFiltersEvent;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Post;
import com.gulasehat.android.model.PostArgs;
import com.gulasehat.android.model.PostData;
import com.gulasehat.android.service.PostService;
import com.gulasehat.android.util.Common;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.widget.Alert;
import com.rockerhieu.rvadapter.endless.EndlessRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, PostRecyclerAdapter.OnPostClickListener, EndlessRecyclerViewAdapter.RequestToLoadMoreListener  {

    @BindView(R.id.noContentContainer)
    protected LinearLayout noContentContainer;
    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;

    private EndlessRecyclerViewAdapter endlessAdapter;
    private PostRecyclerAdapter postRecyclerAdapter;
    private int totalPostCount = 0;
    private int limit = 0;
    private PostArgs args = new PostArgs();

    private int filterIndex = -1;
    private boolean isShown = false;
    private PostData postData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page, container, false);

        ButterKnife.bind(this, view);

        setupPages();

        return view;
    }

    private PostService.OnPostFetchedListener onPostFetchedListener = new PostService.OnPostFetchedListener() {
        @Override
        public void onSuccess(PostData postData) {
            swipeRefreshLayout.setRefreshing(false);
            PageFragment.this.postData = postData;
            preparePages();
        }

        @Override
        public void onFailed(ApiResponse response) {
            swipeRefreshLayout.setRefreshing(false);
            Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
        }
    };

    private void setupPages() {

        args.setSortType("custom_sorting");
        args.setPostType("page");

        postRecyclerAdapter = new PostRecyclerAdapter(getContext(), false);
        postRecyclerAdapter.setOnPostClickListener(this);
        endlessAdapter = new EndlessRecyclerViewAdapter(postRecyclerAdapter, this);
        endlessAdapter.setPendingViewId(R.layout.item_loading);

        recyclerView.setAdapter(endlessAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        PostService.posts(onPostFetchedListener, args);

    }

    private void preparePages(){

        if(args.getPage() > 1){
            addPages(postData.getPosts());
            return;
        }

        EventBus.getDefault().post(new OnFetchedPageData(postData));

        totalPostCount = postData.getPostCount();
        limit = postData.getLimit();



        filterIndex = Common.findFilterIndex(postData.getSortType(), R.array.filter_page_keys);

        if(recyclerView.getItemDecorationCount() > 0){
            recyclerView.removeItemDecorationAt(0);
        }

        if(postData.getLayout() == PostRecyclerAdapter.TYPE_VIEW_POST_WITH_IMAGE || postData.getLayout() == PostRecyclerAdapter.TYPE_VIEW_POST_WITHOUT_IMAGE){
            recyclerView.addItemDecoration(new DividerItemDecoration(getResources()));
        }

        if(postData.getLayout() == PostRecyclerAdapter.TYPE_VIEW_POST_SMALL_BOX){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
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
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        postRecyclerAdapter.setViewType(postData.getLayout());
        addPages(postData.getPosts());


        if(postData.getPostCount() == 0){
            noContentContainer.setVisibility(View.VISIBLE);
        }else{
            noContentContainer.setVisibility(View.GONE);
        }
    }

    private void addPages(ArrayList<Post> data){


        if(args.getPage() > 1){
            postRecyclerAdapter.addPosts(data);
        }else{
            postRecyclerAdapter.setPosts(data);
        }

        endlessAdapter.onDataReady(true);

        int totalPage = (int) Math.ceil((double) totalPostCount / limit);
        int curPage = (int) Math.ceil((double) postRecyclerAdapter.getItemCountExcludeAds() / limit);


        if(postData.getPage() == totalPage){
            endlessAdapter.onDataReady(false);
        }else{
            endlessAdapter.onDataReady(true);
        }

    }

    private void actionFilter(){

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
                .items(R.array.filter_page_texts)
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



    private void filterChange(int which){

        String key = getResources().getStringArray(R.array.filter_page_keys)[which];

        swipeRefreshLayout.setRefreshing(true);
        args.setSortType(key);
        PostService.posts(onPostFetchedListener, args);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPageFiltersEvent(PageFiltersEvent event) {
        actionFilter();
    }

    @Override
    public void onRefresh() {
        args.setPage(1);
        PostService.posts(onPostFetchedListener, args);
    }

    @Override
    public void onClicked(Post post) {
        Intent i = new Intent(getActivity(), PostActivity.class);
        i.putExtra("id", post.getId());
        startActivity(i);
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
}
