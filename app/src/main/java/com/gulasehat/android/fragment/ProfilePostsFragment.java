package com.gulasehat.android.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gulasehat.android.BaseFragment;
import com.gulasehat.android.R;
import com.gulasehat.android.event.OnAuthorFetchedEvent;
import com.gulasehat.android.event.OnPostCategoryChangedEvent;
import com.gulasehat.android.model.Category;
import com.gulasehat.android.model.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfilePostsFragment extends BaseFragment {

    @BindView(R.id.postCount)
    protected TextView postCount;
    @BindView(R.id.categoryText)
    protected TextView categoryText;
    @BindView(R.id.topPanel)
    protected LinearLayout topPanel;

    private List<String> categories = new ArrayList<>();
    private int categoryIndex = 0;
    private boolean isShown = false;


    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_posts, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.filterCategory)
    public void onfilterCategoryClick(View view) {

        if (user == null || user.getUserPostCategories() == null) {
            return;
        }

        if(isShown){
            return;
        }
        isShown = true;


        new MaterialDialog.Builder(Objects.requireNonNull(getContext()))
                .iconRes(R.drawable.icon_filter_black)

                .title(R.string.choose_category)
                .items(categories)
                .itemsCallbackSingleChoice(categoryIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        return true;
                    }
                })
                .neutralText(R.string.cancel)
                .neutralColorAttr(R.attr.colorPrimary)
                .positiveText(R.string.filter_ok)
                .positiveColorAttr(R.attr.colorPrimary)
                .widgetColorAttr(R.attr.colorPrimary)
                .autoDismiss(false)
                .onPositive(categoryFilterCallback)
                .onNeutral(negativeCallback)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isShown = false;
                    }
                })
                .show();

    }


    private MaterialDialog.SingleButtonCallback categoryFilterCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(final @NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            categoryIndex = dialog.getSelectedIndex();
            categoryText.setText(categories.get(dialog.getSelectedIndex()));
            dialog.dismiss();



            if(categoryIndex == 0){
                EventBus.getDefault().post(new OnPostCategoryChangedEvent(0));
                postCount.setText(String.valueOf(user.getUserPostCount()));

            }
            else{
                Category category = user.getUserPostCategories().get(categoryIndex-1);
                EventBus.getDefault().post(new OnPostCategoryChangedEvent(category.getTermID()));
                postCount.setText(String.valueOf(category.getPostCount()));
            }



        }
    };


    private MaterialDialog.SingleButtonCallback negativeCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAuthorFetchedEvent(OnAuthorFetchedEvent event) {
        if (event != null) {
            user = event.getUser();

            categories.add(getString(R.string.all));
            for (Category cat :
                    user.getUserPostCategories()) {
                categories.add(cat.getName());
            }

            prepareUI();
        }
    }

    private void prepareUI() {

        if(user.getUserPostCount() == 0){
            topPanel.setVisibility(View.GONE);

            return;
        }

        postCount.setText(String.valueOf(user.getUserPostCount()));
    }

}
