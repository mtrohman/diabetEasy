package com.gulasehat.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gulasehat.android.BaseFragment;
import com.gulasehat.android.R;
import com.gulasehat.android.event.OnAuthorFetchedEvent;
import com.gulasehat.android.model.User;
import com.gulasehat.android.widget.EditableProfileItem;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileDetailsFragment extends BaseFragment {

    @BindView(R.id.emailContainer)
    protected LinearLayout emailContainer;
    @BindView(R.id.authorEmail)
    protected EditableProfileItem authorEmail;

    @BindView(R.id.websiteContainer)
    protected LinearLayout websiteContainer;
    @BindView(R.id.authorWebsite)
    protected EditableProfileItem authorWebsite;

    @BindView(R.id.genderContainer)
    protected LinearLayout genderContainer;
    @BindView(R.id.authorGender)
    protected EditableProfileItem authorGender;

    @BindView(R.id.jobContainer)
    protected LinearLayout jobContainer;
    @BindView(R.id.authorJob)
    protected EditableProfileItem authorJob;

    @BindView(R.id.bioContainer)
    protected LinearLayout bioContainer;
    @BindView(R.id.authorBio)
    protected EditableProfileItem authorBio;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_details, container, false);

        ButterKnife.bind(this, view);


        return view;
    }

    private void prepareUI(User user) {

        if(user.hasEmail()){
            emailContainer.setVisibility(View.VISIBLE);
        }
        if(user.hasWebsite()){
            websiteContainer.setVisibility(View.VISIBLE);
        }
        if(user.hasGender()){
            genderContainer.setVisibility(View.VISIBLE);
        }
        if(user.hasJob()){
            jobContainer.setVisibility(View.VISIBLE);
        }
        if(user.hasBio()){
            bioContainer.setVisibility(View.VISIBLE);
        }


        if(user.getUserEmail().equals("")){
            authorEmail.setText(getString(R.string.unspecified));
        }else{
            authorEmail.setText(user.getUserEmail());
        }

        if(user.getUserWebsite().equals("")){
            authorWebsite.setText(getString(R.string.unspecified));
        }else{
            authorWebsite.setText(user.getUserWebsite());
        }

        if(user.getUserGender().equals("female")){
            authorGender.setText(getString(R.string.female));
        }

        if(user.getUserGender().equals("male")){
            authorGender.setText(getString(R.string.male));
        }

        if (user.getUserGender().equals("")){
            authorGender.setText(getString(R.string.unspecified));
        }

        if(user.getUserJob().equals("")){
            authorJob.setText(getString(R.string.unspecified));
        }else{
            authorJob.setText(user.getUserJob());
        }

        if(user.getUserBiographicalInfo().equals("")){
            authorBio.setText(getString(R.string.unspecified));
        }else{
            authorBio.setText(user.getUserBiographicalInfo());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAuthorFetchedEvent(OnAuthorFetchedEvent event) {
        if (event != null) {
            prepareUI(event.getUser());
        }
    }

}
