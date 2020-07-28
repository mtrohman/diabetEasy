package com.gulasehat.android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gulasehat.android.adapter.FlinkDividerItemDecoration;
import com.gulasehat.android.adapter.SocialRecyclerAdapter;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.SocialAccount;
import com.gulasehat.android.model.SocialAccountData;
import com.gulasehat.android.service.CommonService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.widget.Alert;

import butterknife.BindView;

public class SocialAccountsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, SocialRecyclerAdapter.OnSocialAccountClickListener {

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.noContentContainer)
    protected LinearLayout noContentContainer;


    private SocialRecyclerAdapter socialRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Analytics.logScreen("Social Accounts");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.nav_social_accounts);

        }

        setupSocialAccounts();

    }

    private CommonService.OnSocialAccountsFetchedListener onSocialAccountsFetchedListener = new CommonService.OnSocialAccountsFetchedListener() {
        @Override
        public void onSuccess(SocialAccountData socialAccountData) {
            swipeRefreshLayout.setRefreshing(false);
            prepareSocialAccounts(socialAccountData);
        }

        @Override
        public void onFailed(ApiResponse response) {
            swipeRefreshLayout.setRefreshing(false);
            Alert.make(SocialAccountsActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
        }
    };

    private void setupSocialAccounts() {
        socialRecyclerAdapter = new SocialRecyclerAdapter();
        socialRecyclerAdapter.setOnSocialAccountClickListener(this);
        recyclerView.setAdapter(socialRecyclerAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        CommonService.getSocialAccounts(onSocialAccountsFetchedListener);

    }

    private void prepareSocialAccounts(SocialAccountData data){

        if(recyclerView.getItemDecorationCount() > 0){
            recyclerView.removeItemDecorationAt(0);
        }

        if(data.getLayout() == SocialRecyclerAdapter.TYPE_VIEW_LIST){
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecoration(getResources()));
        }else{
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.addItemDecoration(new FlinkDividerItemDecoration(this));
        }

        socialRecyclerAdapter.setViewType(data.getLayout());
        socialRecyclerAdapter.setSocialAccounts(data.getSocialAccounts());


        if(data.getSocialAccounts().size() == 0){
            noContentContainer.setVisibility(View.VISIBLE);
        }else{
            noContentContainer.setVisibility(View.GONE);
        }

    }

    @Override
    public void onRefresh() {
        CommonService.getSocialAccounts(onSocialAccountsFetchedListener);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_social_accounts;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
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
    public void onClicked(SocialAccount socialAccount) {
        if(socialAccount.getUrl() != null && ! socialAccount.getUrl().equals("")){
            try{
                if(socialAccount.getName().toLowerCase().equals("whatsapp")){
                    openWhatsapp(socialAccount.getUrl());
                    return;
                }
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(socialAccount.getUrl()));
                startActivity(i);
            }catch (Exception e){
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
            }

        }
    }




    private void openWhatsapp(String phone){
        PackageManager packageManager = getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone=" + phone;
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }else{
                Alert.make(this, R.string.error, Alert.ALERT_TYPE_WARNING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
