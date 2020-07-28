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
import com.gulasehat.android.event.OnSearchResultEvent;
import com.gulasehat.android.event.OnSearchingEvent;
import com.gulasehat.android.event.filter.AuthorFiltersEvent;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Author;
import com.gulasehat.android.model.AuthorData;
import com.gulasehat.android.model.SearchArgs;
import com.gulasehat.android.model.SearchResult;
import com.gulasehat.android.service.CommonService;
import com.gulasehat.android.util.Common;
import com.gulasehat.android.util.Preferences;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.widget.Alert;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAuthorFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, AuthorRecyclerAdapter.OnAuthorClickListener{

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

    private AuthorRecyclerAdapter authorRecyclerAdapter;
    private SearchArgs args = new SearchArgs();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_author, container, false);

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

        authorRecyclerAdapter = new AuthorRecyclerAdapter(getContext());
        authorRecyclerAdapter.setOnAuthorClickListener(this);
        recyclerView.setAdapter(authorRecyclerAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);


        args.setType("author");

    }

    private void prepareAuthors(AuthorData data){



        filterIndex = Common.findFilterIndex(data.getSortType(), R.array.filter_search_author_keys);

        if(recyclerView.getItemDecorationCount() > 0){
            recyclerView.removeItemDecorationAt(0);
        }

        if(data.getLayout() == AuthorRecyclerAdapter.TYPE_VIEW_LIST){
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(llm);
            recyclerView.addItemDecoration(new DividerItemDecoration(getResources()));

        }else{
            GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(glm);
            recyclerView.addItemDecoration(new FlinkDividerItemDecoration(getContext()));
        }

        authorRecyclerAdapter.setViewType(data.getLayout());
        authorRecyclerAdapter.setPostCountStatus(data.getPostCountStatus());

        if(data.getPage() > 1){
            authorRecyclerAdapter.addAuthors(data.getAuthors());
        }else{
            authorRecyclerAdapter.setAuthors(data.getAuthors());
        }


        if(data.getAuthors().size() > 0){
            showList();
        }else{
            hideList();
        }

    }


    private void actionFilter(){

        if(authorRecyclerAdapter.getItemCount() == 0){
            Alert.make(getContext(), R.string.not_filter, Alert.ALERT_TYPE_WARNING);
            return;
        }

        if (isShown){
            return;
        }
        isShown = true;

        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title(R.string.choose_filter)
                .itemsColorRes(R.color.text)
                .items(R.array.filter_search_author_texts)
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

        String key = getResources().getStringArray(R.array.filter_search_author_keys)[which];

        swipeRefreshLayout.setRefreshing(true);

        args.setSortType(key);
        args.setPage(1);
        CommonService.search(args, onSearchResultListener);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAuthorFiltersEvent(AuthorFiltersEvent event) {
        actionFilter();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchResultEvent(OnSearchResultEvent event){

        swipeRefreshLayout.setRefreshing(false);

        if(event != null){
            prepareAuthors(event.getResult().getAuthorData());

            args.setQuery(Preferences.getString(Preferences.SEARCH_QUERY, ""));
            args.setSortType(event.getResult().getAuthorData().getSortType());
        }
    }


    private void showList(){
        homeContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.GONE);
        noContentContainer.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    private void hideList(){
        homeContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
        noContentContainer.setVisibility(View.VISIBLE);
    }

    private CommonService.OnSearchResultListener onSearchResultListener = new CommonService.OnSearchResultListener() {
        @Override
        public void onSuccess(SearchResult result) {
            swipeRefreshLayout.setRefreshing(false);
            prepareAuthors(result.getAuthorData());
        }

        @Override
        public void onFailed(ApiResponse response) {
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onRefresh() {
        args.setPage(1);
        CommonService.search(args, onSearchResultListener);
    }

    @Override
    public void onClicked(Author author) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("author", author.getAuthorID());
        startActivity(intent);
    }

}