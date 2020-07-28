package com.gulasehat.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gulasehat.android.adapter.NotificationRecyclerAdapter;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Notification;
import com.gulasehat.android.service.NotificationService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.widget.Alert;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;

public class NotificationsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, NotificationRecyclerAdapter.OnNotificationClickListener{

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.noContentContainer)
    protected LinearLayout noContentContainer;

    private NotificationRecyclerAdapter notificationRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Analytics.logScreen("Notifications");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.nav_notifications);
        }

        setupNotifications();


    }

    private void setupNotifications() {
        notificationRecyclerAdapter = new NotificationRecyclerAdapter(this);
        notificationRecyclerAdapter.setOnNotificationClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources()));
        recyclerView.setAdapter(notificationRecyclerAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setRefreshing(true);

        NotificationService.getNotifications(onNotificationFetchedListener);
    }

    private NotificationService.OnNotificationFetchedListener onNotificationFetchedListener = new NotificationService.OnNotificationFetchedListener() {
        @Override
        public void onSuccess(ArrayList<Notification> notifications) {
            swipeRefreshLayout.setRefreshing(false);
            notificationRecyclerAdapter.setNotifications(notifications);

            if(notifications.size() == 0){
                noContentContainer.setVisibility(View.VISIBLE);
            }else{
                noContentContainer.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailed(ApiResponse response) {
            Alert.make(NotificationsActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_notifications;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    @Override
    public void onRefresh() {
        NotificationService.getNotifications(onNotificationFetchedListener);
    }
/*
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
*/
    @Override
    public void onClicked(Notification notification) {
        Gson gson = new Gson();
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("data", gson.toJson(notification));
        startActivity(intent);
    }
}
