package com.gulasehat.android.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gulasehat.android.BaseFragment;
import com.gulasehat.android.DividerItemDecoration;
import com.gulasehat.android.PostsActivity;
import com.gulasehat.android.R;
import com.gulasehat.android.adapter.CategoryRecyclerAdapter;
import com.gulasehat.android.adapter.FlinkDividerItemDecoration;
import com.gulasehat.android.event.OnCategoryBackEvent;
import com.gulasehat.android.event.OnCategoryChangeEvent;
import com.gulasehat.android.event.OnCategoryFlushEvent;
import com.gulasehat.android.event.OnFetchedCategoryData;
import com.gulasehat.android.event.filter.CategoryFiltersEvent;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Category;
import com.gulasehat.android.model.CategoryArgs;
import com.gulasehat.android.model.CategoryData;
import com.gulasehat.android.service.CategoryService;
import com.gulasehat.android.util.Common;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.widget.Alert;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, CategoryRecyclerAdapter.OnCategoryClickListener{

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rootContainer)
    protected RelativeLayout rootContainer;
    @BindView(R.id.noContentContainer)
    protected LinearLayout noContentContainer;

    private CategoryRecyclerAdapter categoryRecyclerAdapter;

    private CategoryData categoryData;
    private Category rootCategory;

    private CategoryArgs args;
    private ArrayList<Category> stack;

    private int filterIndex = -1;
    private boolean isShown = false;

    public CategoryFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        ButterKnife.bind(this, view);

        stack = new ArrayList<>();
        args = new CategoryArgs();

        setupCategories();


        return view;
    }

    private void setupCategories() {

        categoryRecyclerAdapter = new CategoryRecyclerAdapter(getContext());
        categoryRecyclerAdapter.setOnCategoryClickListener(this);

        recyclerView.setAdapter(categoryRecyclerAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        CategoryService.getCategories(args, onCategoryFetchedListener);

    }

    private void actionFilter(){

        if(categoryRecyclerAdapter.getItemCount() == 0){
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
                .items(R.array.filter_category_texts)
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

        String key = getResources().getStringArray(R.array.filter_category_keys)[which];

        swipeRefreshLayout.setRefreshing(true);
        args.setSortType(key);
        CategoryService.getCategories(args, onCategoryFetchedListener);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategoryFiltersEvent(CategoryFiltersEvent event) {
        actionFilter();
    }


    private CategoryService.OnCategoryFetchedListener onCategoryFetchedListener = new CategoryService.OnCategoryFetchedListener() {
        @Override
        public void onSuccess(CategoryData categoryData1) {
            swipeRefreshLayout.setRefreshing(false);
            prepareCategories(categoryData1);
            EventBus.getDefault().post(new OnFetchedCategoryData(categoryData1));
        }

        @Override
        public void onFailed(ApiResponse response) {
            swipeRefreshLayout.setRefreshing(false);
            Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
        }
    };

    private void prepareCategories(CategoryData data) {

        if(getContext() == null){
            return;
        }
        filterIndex = Common.findFilterIndex(data.getSortType(), R.array.filter_category_keys);



        categoryData = data;

        if(recyclerView.getItemDecorationCount() > 0){
            recyclerView.removeItemDecorationAt(0);
        }

        if(data.getLayout() == CategoryRecyclerAdapter.TYPE_VIEW_LIST_WITH_ICON || data.getLayout() == CategoryRecyclerAdapter.TYPE_VIEW_LIST_WITHOUT_ICON){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getResources()));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//            recyclerView.addItemDecoration(new GridDividerItemDecoration(getContext()));
              recyclerView.addItemDecoration(new FlinkDividerItemDecoration(getContext()));
        }

        categoryRecyclerAdapter.setViewType(categoryData.getLayout());
        categoryRecyclerAdapter.setPostCountStatus(data.getPostCountStatus());


        categoryRecyclerAdapter.setCategories(data.getCategories());

        if(categoryData.getTotalData() == 0){
            noContentContainer.setVisibility(View.VISIBLE);
        }else{
            noContentContainer.setVisibility(View.GONE);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategoryFlushEvent(OnCategoryFlushEvent event) {
        stack.clear();
        args = new CategoryArgs();
        swipeRefreshLayout.setRefreshing(true);
        CategoryService.getCategories(args, onCategoryFetchedListener);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCategoryBackEvent(OnCategoryBackEvent event) {

        Log.d("burakvanli", "back, stack size : " + stack.size());

        if(stack.size() > 1){
            Category parent = stack.get(stack.size()-2);

            stack.remove(stack.size()-1);
            args.setCategoryId(parent.getTermID());
            swipeRefreshLayout.setRefreshing(true);
            CategoryService.getCategories(args, onCategoryFetchedListener);
            EventBus.getDefault().post(new OnCategoryChangeEvent(parent));
        }else{
            EventBus.getDefault().post(new OnCategoryFlushEvent());
        }
    }

    @Override
    public void onRefresh() {
        CategoryService.getCategories(args, onCategoryFetchedListener);
    }

    @Override
    public void onClick(Category category) {
        actionClickCategory(category);
    }

    private void actionClickCategory(Category category){

        if(category.hasSubCategory()){
            stack.add(category);
            EventBus.getDefault().post(new OnCategoryChangeEvent(category));
            swipeRefreshLayout.setRefreshing(true);
            args.setCategoryId(category.getTermID());
            CategoryService.getCategories(args, onCategoryFetchedListener);

        }
        else{
            String title;
            if(category.isAllContents()){
                title = stack.get(stack.size()-1).getName();
            }else{
                title = category.getName();
            }
            Intent intent = new Intent(getActivity(), PostsActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("cat_id", category.getTermID());
            startActivity(intent);
        }

    }
}