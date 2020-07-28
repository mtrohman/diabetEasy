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
import com.gulasehat.android.ProfileActivity;
import com.gulasehat.android.R;
import com.gulasehat.android.adapter.AuthorRecyclerAdapter;
import com.gulasehat.android.adapter.FlinkDividerItemDecoration;
import com.gulasehat.android.event.OnFetchedAuthorData;
import com.gulasehat.android.event.filter.AuthorFiltersEvent;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Author;
import com.gulasehat.android.model.AuthorArgs;
import com.gulasehat.android.model.AuthorData;
import com.gulasehat.android.service.AuthorService;
import com.gulasehat.android.util.Common;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.widget.Alert;
import com.rockerhieu.rvadapter.endless.EndlessRecyclerViewAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthorFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, AuthorRecyclerAdapter.OnAuthorClickListener, EndlessRecyclerViewAdapter.RequestToLoadMoreListener  {

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.noContentContainer)
    protected LinearLayout noContentContainer;


    private EndlessRecyclerViewAdapter endlessAdapter;
    private AuthorRecyclerAdapter authorRecyclerAdapter;
    private AuthorArgs args = new AuthorArgs();
    private int totalPostCount = 0;

    private AuthorData authorData;

    private int filterIndex = -1;
    private boolean isShown = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_author, container, false);

        ButterKnife.bind(this, view);

        setupPosts();

        return view;
    }


    private AuthorService.OnAuthorFetchedListener onAuthorFetchedListener = new AuthorService.OnAuthorFetchedListener() {
        @Override
        public void onSuccess(AuthorData authorData) {
            swipeRefreshLayout.setRefreshing(false);
            prepareAuthors(authorData);
        }

        @Override
        public void onFailed(ApiResponse response) {
            swipeRefreshLayout.setRefreshing(false);
            Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
        }
    };

    private void setupPosts() {

        authorRecyclerAdapter = new AuthorRecyclerAdapter(getContext());
        authorRecyclerAdapter.setOnAuthorClickListener(this);

        endlessAdapter = new EndlessRecyclerViewAdapter(authorRecyclerAdapter, this);
        endlessAdapter.setPendingViewId(R.layout.item_loading);

        recyclerView.setAdapter(endlessAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        AuthorService.getAuthors(onAuthorFetchedListener);

    }

    private void prepareAuthors(AuthorData data){

        if(getContext() == null){
            return;
        }

        new OnFetchedAuthorData(data);

        authorData = data;



        if(args.getPage() > 1){
            addAuthors(data.getAuthors());
            return;
        }

        if(recyclerView == null){
            return;
        }

        totalPostCount = data.getTotalData();

        filterIndex = Common.findFilterIndex(data.getSortType(), R.array.filter_author_keys);

        if(recyclerView.getItemDecorationCount() > 0){
            recyclerView.removeItemDecorationAt(0);
        }

        if(data.getLayout() == AuthorRecyclerAdapter.TYPE_VIEW_LIST){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getResources()));
        }else{
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.addItemDecoration(new FlinkDividerItemDecoration(getContext()));

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(endlessAdapter.getItemViewType(position) == EndlessRecyclerViewAdapter.TYPE_PENDING){
                        return 2;
                    }
                    return 1;
                }
            });
        }

        authorRecyclerAdapter.setViewType(data.getLayout());
        authorRecyclerAdapter.setPostCountStatus(data.getPostCountStatus());
        addAuthors(data.getAuthors());


        if(data.getTotalData() == 0){
            noContentContainer.setVisibility(View.VISIBLE);
        }else{
            noContentContainer.setVisibility(View.GONE);
        }
    }

    private void addAuthors(ArrayList<Author> data){

        if(args.getPage() > 1){
            authorRecyclerAdapter.addAuthors(data);
        }else{
            authorRecyclerAdapter.setAuthors(data);
        }

        endlessAdapter.onDataReady(true);

        int totalPage = (int) Math.ceil((double) totalPostCount / authorData.getLimit());
        int curPage = (int) Math.ceil((double) authorRecyclerAdapter.getItemCount() / authorData.getLimit());

        if(curPage == totalPage){
            endlessAdapter.onDataReady(false);
        }else{
            endlessAdapter.onDataReady(true);
        }

    }

    private void actionFilter(){

        if(authorRecyclerAdapter.getItemCount() == 0){
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
                .items(R.array.filter_author_texts)
                .itemsCallbackSingleChoice(filterIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        filterChange(which);
                        return true;
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShown = false;
                    }
                })
                .iconRes(R.drawable.icon_filter_black)
                .widgetColorRes(Resource.getColorPrimary())
                .show();
    }




    private void filterChange(int which){

        String key = getResources().getStringArray(R.array.filter_author_keys)[which];

        swipeRefreshLayout.setRefreshing(true);
        args.setPage(1);
        args.setSortType(key);
        AuthorService.getAuthors(onAuthorFetchedListener, args);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAuthorFiltersEvent(AuthorFiltersEvent event) {
        actionFilter();
    }

    @Override
    public void onRefresh() {
        args.setPage(1);
        AuthorService.getAuthors(onAuthorFetchedListener, args);
    }

    @Override
    public void onClicked(Author author) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("author", author.getAuthorID());
        startActivity(intent);
    }

    @Override
    public void onLoadMoreRequested() {
        if(totalPostCount > 0){

            int page = (int) Math.ceil((double) authorRecyclerAdapter.getItemCount() / authorData.getLimit());

            args.setPage(page+1);
            AuthorService.getAuthors(onAuthorFetchedListener, args);

        }
    }
}
