package com.gulasehat.android.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gulasehat.android.BaseFragment;
import com.gulasehat.android.DividerItemDecoration;
import com.gulasehat.android.LoginActivity;
import com.gulasehat.android.PostActivity;
import com.gulasehat.android.R;
import com.gulasehat.android.adapter.PostRecyclerAdapter;
import com.gulasehat.android.event.OnClearAllFavoritesEvent;
import com.gulasehat.android.event.OnFavoriteChanged;
import com.gulasehat.android.event.OnFavoriteFiltersEvent;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Post;
import com.gulasehat.android.model.PostArgs;
import com.gulasehat.android.model.PostData;
import com.gulasehat.android.service.PostService;
import com.gulasehat.android.util.AuthUtil;
import com.gulasehat.android.util.Common;
import com.gulasehat.android.util.FavoriteManager;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.widget.Alert;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoriteFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        PostRecyclerAdapter.OnPostClickListener,
        PostRecyclerAdapter.OnFavoriteClick{

    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.requireLoginContainer)
    protected LinearLayout requireLoginContainer;
    @BindView(R.id.blankContainer)
    protected LinearLayout blankContainer;
    @BindView(R.id.favoriteIcon)
    protected ImageView favoriteIcon;

    private PostRecyclerAdapter postRecyclerAdapter;
    private PostArgs args = new PostArgs();

    private int filterIndex = -1;
    private boolean isShown = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        ButterKnife.bind(this, view);

        init();



        return view;
    }


    private void init() {

        favoriteIcon.setImageResource(Resource.getDrawableResID("ic_favorite_" + Resource.getAppTheme().toLowerCase(Locale.ENGLISH)));

        setupFavoritePosts();

        invalidateFavorites();

        swipeRefreshLayout.setRefreshing(true);
        PostService.posts(onPostFetchedListener, args);

    }

    private void invalidateFavorites(){

        blankContainer.setVisibility(View.GONE);
        requireLoginContainer.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        if(! AuthUtil.isLoggedIn()){
            requireLoginContainer.setVisibility(View.VISIBLE);
        }else{
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            if(postRecyclerAdapter.getItemCount() > 0){
                recyclerView.setVisibility(View.VISIBLE);
            }else{
                blankContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.loginButton)
    public void onLoginButtonClick(View view){
        startActivity(new Intent(getContext(), LoginActivity.class));
    }


    private void setupFavoritePosts() {

        postRecyclerAdapter = new PostRecyclerAdapter(getContext(), false);
        postRecyclerAdapter.setOnPostClickListener(this);
        postRecyclerAdapter.setOnFavoriteClick(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources()));
        recyclerView.setAdapter(postRecyclerAdapter);

        args.setFavorites(true);



    }

    private PostService.OnPostFetchedListener onPostFetchedListener = new PostService.OnPostFetchedListener() {
        @Override
        public void onSuccess(PostData postData) {

            swipeRefreshLayout.setRefreshing(false);

            filterIndex = Common.findFilterIndex(postData.getSortType(), R.array.filter_favorite_keys);

            postRecyclerAdapter.setPosts(postData.getPosts());
            invalidateFavorites();
        }

        @Override
        public void onFailed(ApiResponse response) {
            swipeRefreshLayout.setRefreshing(false);
            invalidateFavorites();
            Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFavoriteFiltersEvent(OnFavoriteFiltersEvent event) {
        actionFilter();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClearAllFavoritesEvent(OnClearAllFavoritesEvent event) {

        if(postRecyclerAdapter.getItemCount() == 0){
            Alert.make(getContext(), R.string.not_clear_favorites, Alert.ALERT_TYPE_WARNING);
            return;
        }

        if(isShown){
            return;
        }
        isShown = true;

        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .title(R.string.clear_all_favorites)
                .content(R.string.clear_all_favorites_desc)
                .positiveText(R.string.yes)
                .positiveColorAttr(R.attr.colorPrimary)
                .negativeText(R.string.no)
                .negativeColorAttr(R.attr.colorPrimary)
                .icon(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.icon_delete_black)))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        PostService.deleteAllFavorites(new PostService.OnAllFavoriteDeletedListener() {
                            @Override
                            public void onSuccess(ApiResponse response) {

                                for (Integer id :
                                        postRecyclerAdapter.getPostIds()) {
                                    Log.d("buraktest3", "değişti: " + id);
                                    EventBus.getDefault().postSticky(new OnFavoriteChanged(id, false));
                                }

                            }

                            @Override
                            public void onFailed(ApiResponse response) {
                                Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
                            }
                        });
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShown = false;
                    }
                })
                .show();
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
                .items(R.array.filter_favorite_texts)
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

        String key = getResources().getStringArray(R.array.filter_favorite_keys)[which];

        args.setSortType(key);

        PostService.posts(onPostFetchedListener, args);

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
        PostService.posts(onPostFetchedListener, args);
    }

    @Override
    public void onRefresh() {
        PostService.posts(onPostFetchedListener, args);
    }
}