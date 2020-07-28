package com.gulasehat.android;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gulasehat.android.adapter.FaqRecyclerAdapter;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.Question;
import com.gulasehat.android.service.CommonService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.widget.Alert;

import java.util.ArrayList;

import butterknife.BindView;

public class FaqActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.noContentContainer)
    protected LinearLayout noContentContainer;
    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    private FaqRecyclerAdapter faqRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Analytics.logScreen("Faq");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.nav_faq);

        }

        faqRecyclerAdapter = new FaqRecyclerAdapter(this, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(faqRecyclerAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setRefreshing(true);

        CommonService.getQuestions(onQuestionsFetchedListener);
    }

    private CommonService.OnQuestionsFetchedListener onQuestionsFetchedListener = new CommonService.OnQuestionsFetchedListener() {
        @Override
        public void onSuccess(ArrayList<Question> questions) {
            faqRecyclerAdapter.setQuestions(questions);
            swipeRefreshLayout.setRefreshing(false);

            if(questions.size() == 0){
                noContentContainer.setVisibility(View.VISIBLE);
            }else{
                noContentContainer.setVisibility(View.GONE);
            }
        }

        @Override
        public void onFailed(ApiResponse response) {
            swipeRefreshLayout.setRefreshing(false);
            Alert.make(FaqActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_faq;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    @Override
    public void onRefresh() {
        CommonService.getQuestions(onQuestionsFetchedListener);
    }
}
