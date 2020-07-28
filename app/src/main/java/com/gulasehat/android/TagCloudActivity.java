package com.gulasehat.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.ActionBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gulasehat.android.adapter.TagViewAdapter;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.CategoryChip;
import com.gulasehat.android.model.Tag;
import com.gulasehat.android.model.TagArgs;
import com.gulasehat.android.model.TagData;
import com.gulasehat.android.service.CommonService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.Common;
import com.gulasehat.android.util.Resource;
import com.gulasehat.android.widget.Alert;
import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;
import com.plumillonforge.android.chipview.OnChipClickListener;

import java.util.ArrayList;

import butterknife.BindView;

public class TagCloudActivity extends BaseActivity {


    @BindView(R.id.tagContainer)
    protected ScrollView tagContainer;
    @BindView(R.id.tags)
    protected ChipView tags;
    @BindView(R.id.noContentContainer)
    protected LinearLayout noContentContainer;
    private TagArgs args;
    private TagData tagData;
    private boolean isShown = false;


    private int filterIndex = -1;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Analytics.logScreen("Tag Cloud");

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.nav_tag_cloud);

        }


        args = new TagArgs();

        CommonService.getTagClouds(args, onTagCloudFetchedListener);

    }


    private void initializeTagCloud() {

        if (tagData.getTags().size() > 0) {

            if(tagData.getTotalDataCountStatus()){
                actionBar.setTitle(getString(R.string.nav_tag_cloud) + " (" + tagData.getTotalData() + ")");
            }else{
                actionBar.setTitle(getString(R.string.nav_tag_cloud));
            }

            noContentContainer.setVisibility(View.GONE);
            tagContainer.setVisibility(View.VISIBLE);

            tags.setOnChipClickListener(new OnChipClickListener() {
                @Override
                public void onChipClick(Chip chip) {
                    Intent intent = new Intent(TagCloudActivity.this, PostsActivity.class);
                    intent.putExtra("title", ((CategoryChip) chip).getText());
                    intent.putExtra("tag", ((CategoryChip) chip).getSlug());
                    startActivity(intent);
                }
            });

            ArrayList<Chip> tagChips = new ArrayList<>();

            for (Tag tag :
                    tagData.getTags()) {
                tagChips.add(new CategoryChip(tag.getSlug(), tag.getName(), tag.getCount()));
            }
            tags.setAdapter(new TagViewAdapter(this, R.layout.layout_tag));
            tags.setChipList(tagChips);
        }else{
            tagContainer.setVisibility(View.GONE);
            noContentContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_filter, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_filter:
                actionFilter();
                break;
        }

        return true;

    }

    private void actionFilter() {

        if(tagData == null || tagData.getTags().size() == 0){
            Alert.make(TagCloudActivity.this, R.string.not_filter, Alert.ALERT_TYPE_WARNING);
            return;
        }

        if(isShown){
            return;
        }

        isShown = true;

        new MaterialDialog.Builder(this)
                .title(R.string.choose_filter)
                .itemsColorRes(R.color.text)
                .items(R.array.filter_tag_texts)
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

        String key = getResources().getStringArray(R.array.filter_tag_keys)[which];


        args.setSortType(key);
        CommonService.getTagClouds(args, onTagCloudFetchedListener);

    }

    private CommonService.OnTagCloudFetchedListener onTagCloudFetchedListener = new CommonService.OnTagCloudFetchedListener() {
        @Override
        public void onSuccess(TagData data) {
            tagData = data;
            initializeTagCloud();

            filterIndex = Common.findFilterIndex(data.getSortType(), R.array.filter_tag_keys);
        }

        @Override
        public void onFailed(ApiResponse response) {
            Alert.make(TagCloudActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_tag_cloud;
    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }
}
