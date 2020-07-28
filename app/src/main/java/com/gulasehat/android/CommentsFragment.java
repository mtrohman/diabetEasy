package com.gulasehat.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gulasehat.android.adapter.CommentRecyclerAdapter;
import com.gulasehat.android.event.OnCommentFiltersEvent;
import com.gulasehat.android.event.OnCommentsRefreshEvent;
import com.gulasehat.android.event.SwitchNewCommentTabEvent;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Comment;
import com.gulasehat.android.model.CommentArgs;
import com.gulasehat.android.model.Comments;
import com.gulasehat.android.service.CommentService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.Common;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.util.Settings;
import com.gulasehat.android.widget.Alert;
import com.rockerhieu.rvadapter.endless.EndlessRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentsFragment extends BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,
        CommentRecyclerAdapter.OnCommentRateClickListener,
        CommentRecyclerAdapter.OnCommentAuthorClickListener,
        EndlessRecyclerViewAdapter.RequestToLoadMoreListener{

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.noContentContainer)
    protected LinearLayout noContentContainer;

    private CommentRecyclerAdapter commentRecyclerAdapter;
    private EndlessRecyclerViewAdapter endlessAdapter;
    private CommentArgs commentArgs;

    private int filterIndex = -1;
    private int totalCommentCount = 0;
    private int limit = 5;
    private boolean isShown = false;


    private int postID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            postID = getArguments().getInt("id", 0);
        } else {
            postID = 0;
        }

        commentArgs = new CommentArgs();
        commentArgs.setPostId(postID);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comments, container, false);

        ButterKnife.bind(this, view);

        setupComments();

        return view;
    }

    private void setupComments() {

        commentRecyclerAdapter = new CommentRecyclerAdapter(getContext());
        commentRecyclerAdapter.setOnCommentAuthorClickListener(this);
        commentRecyclerAdapter.setOnCommentRateClickListener(this);

        endlessAdapter = new EndlessRecyclerViewAdapter(commentRecyclerAdapter, this);
        endlessAdapter.setPendingViewId(R.layout.item_loading);
        endlessAdapter.onDataReady(false);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(endlessAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);


        CommentService.getComments(commentArgs, onCommentsFetchedListener);


    }


    CommentService.OnCommentsFetchedListener onCommentsFetchedListener = new CommentService.OnCommentsFetchedListener() {
        @Override
        public void onSuccess(Comments comments) {
            swipeRefreshLayout.setRefreshing(false);

            prepareComments(comments);




        }

        @Override
        public void onFailed(ApiResponse response) {
            Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
        }
    };

    private void addComments(ArrayList<Comment> comments){
        if(commentArgs.getPage() > 1){
            commentRecyclerAdapter.addComments(comments);
        }else{
            commentRecyclerAdapter.setComments(comments);
            recyclerView.scrollToPosition(0);
        }

        int totalPage = (int) Math.ceil((double) totalCommentCount / limit);
        int curPage = (int) Math.ceil((double) commentRecyclerAdapter.getItemCount() / limit);

        if(curPage == totalPage){
            endlessAdapter.onDataReady(false);
        }else{
            endlessAdapter.onDataReady(true);
        }
    }

    private void prepareComments(Comments comments){

        if(getActivity() == null){
            return;
        }

        if(commentArgs.getPage() > 1){
            addComments(comments.getComments());
            return;
        }

        totalCommentCount = comments.getTotalData();
        limit = comments.getLimit();



        Log.d("burakist", "rate status: " + Settings.getAppSettings().getAppComment().isRateStatus());

        if(Settings.getAppSettings().getAppComment().isRateStatus()){
            filterIndex = Common.findFilterIndex(comments.getSortType(), R.array.filter_comment_keys_with_rates);
        }else{
            filterIndex = Common.findFilterIndex(comments.getSortType(), R.array.filter_comment_keys_without_rates);
        }


        if (comments.getComments().size() > 0) {
            noContentContainer.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }else{
            swipeRefreshLayout.setVisibility(View.GONE);
            noContentContainer.setVisibility(View.VISIBLE);
        }

        addComments(comments.getComments());


    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        commentArgs.setPage(1);
        CommentService.getComments(commentArgs, onCommentsFetchedListener);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentsRefreshEvent(OnCommentsRefreshEvent event){
        onRefresh();
    }


    @Override
    public void onCommentAuthorClick(Comment comment) {
        if(comment.getCommentUserID() > 0){
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            intent.putExtra("author", comment.getCommentUserID());
            startActivity(intent);
        }
    }

    @Override
    public void onRateClick(Comment comment, int rateType) {

        Analytics.logEvent("Comment Rate");

        CommentService.rateComment(comment.getCommentID(), rateType, new CommentService.OnRateCompletedListener() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                Toast.makeText(getContext(), Resource.getStringResID(apiResponse.getMessage()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(ApiResponse response) {
                Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentFiltersEvent(OnCommentFiltersEvent event) {
        actionFilter();
    }

    private void actionFilter() {

        if(commentRecyclerAdapter != null && commentRecyclerAdapter.getItemCount() == 0){
            Alert.make(getContext(), R.string.not_filter, Alert.ALERT_TYPE_WARNING);
            return;
        }

        Analytics.logEvent("Comments Filter Dialog");

        int keys;

        if(Settings.getAppSettings().getAppComment().isRateStatus()){
            keys = R.array.filter_comment_texts_with_rates;
        }else{
            keys = R.array.filter_comment_texts_without_rates;
        }

        if(isShown){
            return;
        }

        isShown = true;

        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title(R.string.choose_filter)
                .itemsColorRes(R.color.text)
                .items(keys)
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

    private void filterChange(int which) {

        String key;
        if(Settings.getAppSettings().getAppComment().isRateStatus()){
            key = getResources().getStringArray(R.array.filter_comment_keys_with_rates)[which];
        }else{
            key = getResources().getStringArray(R.array.filter_comment_keys_without_rates)[which];
        }

        swipeRefreshLayout.setRefreshing(true);

        commentArgs.setSortType(key);
        commentArgs.setPage(1);
        CommentService.getComments(commentArgs, onCommentsFetchedListener);

    }


    @OnClick(R.id.newComment)
    public void onNewComment(){
        EventBus.getDefault().post(new SwitchNewCommentTabEvent());
    }

    @Override
    public void onLoadMoreRequested() {
        if(totalCommentCount > 0){

            int page = (int) Math.ceil((double) commentRecyclerAdapter.getItemCount() / limit);

            commentArgs.setPage(page+1);

            CommentService.getComments(commentArgs, onCommentsFetchedListener);

        }
    }
}
