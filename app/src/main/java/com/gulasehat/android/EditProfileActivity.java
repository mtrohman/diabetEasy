package com.gulasehat.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cocosw.bottomsheet.BottomSheet;
import com.gulasehat.android.adapter.ViewPagerAdapter;
import com.gulasehat.android.event.ProfileUpdatedEvent;
import com.gulasehat.android.fragment.EditDetailsFragment;
import com.gulasehat.android.fragment.EditProfileFragment;
import com.gulasehat.android.model.ApiResponse;
import com.gulasehat.android.model.AuthUser;
import com.gulasehat.android.model.MyUser;
import com.gulasehat.android.service.AuthService;
import com.gulasehat.android.util.Analytics;
import com.gulasehat.android.util.AuthUtil;
import com.gulasehat.android.util.Permission;
import com.gulasehat.android.util.PhotoUtil;
import com.gulasehat.android.widget.Alert;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.skyfishjy.library.RippleBackground;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class EditProfileActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    protected ViewPager viewPager;
    @BindView(R.id.tabLayout)
    protected TabLayout tabLayout;
    @BindView(R.id.appBarLayout)
    protected AppBarLayout appBarLayout;
    @BindView(R.id.appBarTitle)
    protected TextView appBarTitle;

    @BindView(R.id.authorAvatar)
    protected ImageView authorAvatar;
    @BindView(R.id.authorName)
    protected TextView authorName;
    @BindView(R.id.authorUserName)
    protected TextView authorUserName;
    @BindView(R.id.changeAvatar)
    protected RelativeLayout changeAvatar;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.innerContainer)
    protected RippleBackground innerContainer;

    private ViewPagerAdapter viewPagerAdapter;

    private final int MULTIPLE_PERMISSIONS = 100;


    private PhotoUtil photoUtil;

    private ActionBar actionBar;
    private AuthUser authUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Analytics.logScreen("Edit Profile");

        setSupportActionBar(toolbar);


        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        showProgress();
        AuthService.getUserInfo(new AuthService.OnMyUserInfoFetchedListener() {
            @Override
            public void onSuccess(MyUser user) {
                hideProgress();
                AuthUtil.setUser(user);
                authUser = AuthUtil.getUser();
                init();
                prepareUI();

                if (getIntent().hasExtra("photo")) {
                    showPhotoBottomSheet();
                }
            }

            @Override
            public void onFailed(ApiResponse apiResponse) {
                hideProgress();
                Alert.make(EditProfileActivity.this, R.string.error, Alert.ALERT_TYPE_WARNING);
            }
        });


        innerContainer.startRippleAnimation();

        photoUtil = new PhotoUtil(this, new PhotoUtil.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                Log.d("buraks", outputFile.getAbsolutePath());
                Log.d("buraks", outputUri.toString());

                //uploadAvatar(outputUri);
                startCropping(outputUri);

            }
        }, false);



    }

    private void init() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, R.layout.layout_tab_light);

        viewPagerAdapter.addFragment(new EditProfileFragment(), R.drawable.icon_profile_white, R.string.profile);
        viewPagerAdapter.addFragment(new EditDetailsFragment(), R.drawable.icon_public_white, R.string.detail);

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View view = viewPagerAdapter.getTabView(i);
                if (i > 0) {
                    view.setAlpha(0.5f);
                }
                tab.setCustomView(view);
            }
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if (tab != null && tab.getCustomView() != null) {
                        tab.getCustomView().setAlpha(0.5f);
                    }
                }
                TabLayout.Tab selectedTab = tabLayout.getTabAt(position);
                if (selectedTab != null && selectedTab.getCustomView() != null) {
                    selectedTab.getCustomView().setAlpha(1f);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = Math.abs(appBarLayout.getY()) / (float) maxScroll;

                if (verticalOffset == 0) {
                    actionBar.setDisplayShowTitleEnabled(false);

                } else {
                    actionBar.setDisplayShowTitleEnabled(true);
                }

                float alpha = 1 - percentage;

                innerContainer.setAlpha(alpha);
                appBarTitle.setAlpha(percentage);

            }
        });

    }

    private void prepareUI() {

        if (authUser == null) {
            return;
        }

        authorName.setText(authUser.getUserFullName());
        authorUserName.setText(authUser.getUserName());

        if (authUser.getUserThumb() != null && !authUser.getUserThumb().equals("")) {

            GlideApp.with(this)
                    .load(authUser.getUserThumb())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter()
                    .apply(RequestOptions.circleCropTransform())
                    .into(authorAvatar);
        }

    }


    private void showPhotoBottomSheet() {
        int bottomSheetStyle;
        int bottomSheetMenu;

        if (!authUser.hasPicture()) {
            bottomSheetStyle = R.style.PhotoBottomSheetDouble;
            bottomSheetMenu = R.menu.menu_change_avatar_double;
        } else {
            bottomSheetStyle = R.style.PhotoBottomSheet;
            bottomSheetMenu = R.menu.menu_change_avatar;
        }

        new BottomSheet.Builder(EditProfileActivity.this, bottomSheetStyle).grid().sheet(bottomSheetMenu).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    switch (which) {
                        case R.id.take_photo:
                            if(checkPermissionGranted()){
                                photoUtil.takePhoto();
                            }
                            break;
                        case R.id.choose_photo:
                            if(checkPermissionGranted()){
                                photoUtil.selectPhoto();
                            }
                            break;
                        case R.id.remove_photo:
                            removePhoto();
                            break;
                    }

            }
        }).show();
    }

    private boolean checkPermissionGranted() {


        if (!Permission.hasPermission(this, Permission.READ_STORAGE)
                || !Permission.hasPermission(this, Permission.WRITE_STORAGE)
                || !Permission.hasPermission(this, Permission.CAMERA)
        ) {
            Permission.requestPermissions(this, new String[]{Permission.CAMERA, Permission.READ_STORAGE, Permission.WRITE_STORAGE}, MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }


    private void removePhoto() {

        Analytics.logEvent("Edit Profile Remove Photo");
        showProgress();
        AuthService.removeAvatar(new AuthService.OnAvatarRemovedListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                hideProgress();

                Log.d("burkist", response.getData().get("gravatar_status"));

                authUser.setUserPicture(response.getData().get("user_picture"));
                authUser.setUserThumb(response.getData().get("user_thumb"));
                authUser.setHasPicture(Boolean.parseBoolean(response.getData().get("user_has_picture")));

                AuthUtil.setUser(authUser);

                if(Boolean.parseBoolean(response.getData().get("gravatar_status"))){
                    showGravatarAlert();
                    return;
                }

                reloadImage();

            }

            @Override
            public void onFailed(ApiResponse response) {
                hideProgress();
                Alert.make(EditProfileActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
            }
        });

    }

    private void reloadImage(){
        GlideApp.with(this).load(authUser.getUserThumb()).apply(RequestOptions.circleCropTransform()).into(authorAvatar);
    }

    private void showGravatarAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle(R.string.warning);
        builder.setMessage(Html.fromHtml(getString(R.string.remove_photo_gravatar_question)));
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reloadImage();
            }
        });
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AuthService.updateGravatarStatus(true, new AuthService.OnAvatarRemovedListener() {
                    @Override
                    public void onSuccess(ApiResponse response) {
                        authUser.setUserPicture(response.getData().get("user_picture"));
                        authUser.setUserThumb(response.getData().get("user_thumb"));
                        authUser.setHasPicture(Boolean.parseBoolean(response.getData().get("user_has_picture")));
                        AuthUtil.setUser(authUser);
                        reloadImage();
                    }

                    @Override
                    public void onFailed(ApiResponse response) {

                    }
                });
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uploadAvatar(result.getUri());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Alert.make(this, R.string.error, Alert.ALERT_TYPE_WARNING, new Alert.OnButtonClickListener() {
                    @Override
                    public void onClick() {
                        finish();
                    }
                });
            }
        }


        photoUtil.attachToActivityForResult(requestCode, resultCode, data);

    }

    private void uploadAvatar(final Uri uri) {

        showProgress();



        AuthService.uploadAvatar(uri, new AuthService.OnAvatarUploadListener() {
            @Override
            public void onSuccess(final ApiResponse response) {

                hideProgress();

                if (response.getData() != null && response.getData().get("user_picture") != null && response.getData().get("user_thumb") != null) {
                    authUser.setUserThumb(response.getData().get("user_thumb"));
                    authUser.setUserPicture(response.getData().get("user_picture"));
                    authUser.setHasPicture(true);
                    AuthUtil.setUser(authUser);
                    reloadImage();
                }

                Alert.make(EditProfileActivity.this, response.getMessage(), Alert.ALERT_TYPE_SUCCESS, new Alert.OnButtonClickListener() {
                    @Override
                    public void onClick() {

                    }
                });


            }

            @Override
            public void onFailed(ApiResponse response) {
                hideProgress();

                Alert.make(EditProfileActivity.this, response.getMessage(), Alert.ALERT_TYPE_WARNING);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MULTIPLE_PERMISSIONS) {

            if (grantResults.length == 0) {
                Alert.make(this, R.string.permission_required, Alert.ALERT_TYPE_WARNING);
                return;
            }

            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Alert.make(this, R.string.permission_required, Alert.ALERT_TYPE_WARNING);
                    return;
                }
            }

            showPhotoBottomSheet();

        }

    }

    @OnClick(R.id.changeAvatar)
    public void onClick(View view) {

        showPhotoBottomSheet();
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_edit_profile;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    protected void startCropping(Uri uri) {

        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAllowFlipping(false)
                .setCropMenuCropButtonTitle(getString(R.string.ok))
                .setFixAspectRatio(true)
                .setActivityTitle(getString(R.string.crop_image))
                .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                .setOutputCompressQuality(75)
                .setRequestedSize(1024, 1024)
                .start(this);
    }

    @OnClick(R.id.authorAvatar)
    public void onAvatarClick(View view) {

        Analytics.logEvent("Edit Profile Avatar Click");

        if (authUser != null && !authUser.getUserPicture().equals("")) {

            try {
                Log.d("burki", authUser.getUserThumb());
                showPhoto(authorAvatar, authUser.getUserPicture());

            } catch (Exception e) {
                Alert.make(EditProfileActivity.this, R.string.error, Alert.ALERT_TYPE_WARNING);
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProfileUpdated(ProfileUpdatedEvent event){
        authUser = event.getUser();
        prepareUI();
    }

    @Override
    public void onBackPressed() {

        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(0);
            return;
        }

        super.onBackPressed();

    }

    @Override
    protected String getActivityName() {
        return getClass().getSimpleName();
    }
}
