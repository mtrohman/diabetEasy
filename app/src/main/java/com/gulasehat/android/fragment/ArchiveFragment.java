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
import com.gulasehat.android.PostsActivity;
import com.gulasehat.android.R;
import com.gulasehat.android.adapter.ArchiveRecyclerAdapter;
import com.gulasehat.android.adapter.FlinkDividerItemDecoration;
import com.gulasehat.android.event.OnArchiveBackEvent;
import com.gulasehat.android.event.OnArchiveChangeEvent;
import com.gulasehat.android.event.filter.ArchiveFiltersEvent;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.ArchiveData;
import com.gulasehat.android.model.Month;
import com.gulasehat.android.model.Year;
import com.gulasehat.android.service.ArchiveService;
import com.gulasehat.android.util.Common;
import com.gulasehat.android.util.DateUtil;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.widget.Alert;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArchiveFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ArchiveRecyclerAdapter.OnArchiveClickListener {

    @BindView(R.id.noContentContainer)
    protected LinearLayout noContentContainer;
    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;

    private ArchiveRecyclerAdapter archiveRecyclerAdapter;

    private ArchiveData archiveData;
    private boolean isBackAvailable = true;

    private int filterIndex = 0;
    private boolean isShown = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_archive, container, false);

        ButterKnife.bind(this, view);

        setupArchives();

        return view;
    }

    private ArchiveService.OnArchiveFetchedListener onArchiveFetchedListener = new ArchiveService.OnArchiveFetchedListener() {
        @Override
        public void onSuccess(ArchiveData data) {

            swipeRefreshLayout.setRefreshing(false);
            prepareArchive(data);
        }

        @Override
        public void onFailed(ApiResponse response) {
            swipeRefreshLayout.setRefreshing(false);
            Alert.make(getContext(), response.getMessage(), Alert.ALERT_TYPE_WARNING);
        }
    };

    private void setupArchives() {

        archiveRecyclerAdapter = new ArchiveRecyclerAdapter(getContext());
        archiveRecyclerAdapter.setOnArchiveClickListener(this);
        recyclerView.setAdapter(archiveRecyclerAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        ArchiveService.getArchives(onArchiveFetchedListener);

    }

    private void prepareArchive(ArchiveData data){
        archiveData = data;

        if(recyclerView.getItemDecorationCount() > 0){
            recyclerView.removeItemDecorationAt(0);
        }

        if(data.getLayout() == ArchiveRecyclerAdapter.TYPE_VIEW_LIST){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getResources()));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            recyclerView.addItemDecoration(new FlinkDividerItemDecoration(getContext()));
        }

        archiveRecyclerAdapter.setViewType(data.getLayout());
        archiveRecyclerAdapter.setYears(data.getYears());


        if(data.getYears().size() == 0){
            noContentContainer.setVisibility(View.VISIBLE);
        }else{
            noContentContainer.setVisibility(View.GONE);
        }

        isBackAvailable = true;

        //filterIndex = Common.findFilterIndex(data.getSortType(), R.array.filter_archive_keys);
    }

    private void actionFilter(){

        if(archiveRecyclerAdapter.getItemCount() == 0){
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
                .items(R.array.filter_archive_texts)
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

        if(which == 0){
            filterIndex = Common.findFilterIndex("desc", R.array.filter_archive_keys);
        }else{
            filterIndex = Common.findFilterIndex("asc", R.array.filter_archive_keys);
        }

        archiveRecyclerAdapter.reverseContents();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onArchiveBackEvent(OnArchiveBackEvent event) {
        if(isBackAvailable){
            if(getActivity() != null){
                getActivity().finish();
            }
        }
        filterIndex = 0;
        if(archiveData != null){
            swipeRefreshLayout.setEnabled(true);
            prepareArchive(archiveData);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onArchiveFiltersEvent(ArchiveFiltersEvent event) {
        actionFilter();
    }

    @Override
    public void onRefresh() {
        ArchiveService.getArchives(onArchiveFetchedListener);
    }

    @Override
    public void onYearClick(Year year) {
        //archiveRecyclerAdapter.setMonths(year.getMonths().getMonths());
        if(recyclerView.getItemDecorationCount() > 0){
            recyclerView.removeItemDecorationAt(0);
        }

        swipeRefreshLayout.setEnabled(false);

        if(year.getMonths().getLayout() == ArchiveRecyclerAdapter.TYPE_VIEW_LIST){
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getResources()));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            recyclerView.addItemDecoration(new FlinkDividerItemDecoration(getContext()));
        }

        isBackAvailable = false;
        filterIndex = 1;

        EventBus.getDefault().post(new OnArchiveChangeEvent(year));
    }

    @Override
    public void onMonthClick(Month month) {
        String title = DateUtil.getMonthName(month.getMonth()) + " " + month.getYear();


        Intent intent = new Intent(getContext(), PostsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("year", month.getYear());
        intent.putExtra("month", month.getMonth());
        startActivity(intent);
    }
}
