package com.gulasehat.android;

import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.gulasehat.android.event.OnFavoriteClicked;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseFragment extends Fragment {

    @Override
    public void startActivity(Intent intent) {
        if(getActivity() != null){
            getActivity().startActivity(intent);
        }else{
            super.startActivity(intent);
        }
    }

    protected void showPhoto(View view, String url){

        if(getContext() == null){
           return;
        }

        Intent intent = new Intent(getContext(), PhotoActivity.class);
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);

        intent.putExtra("left", screenLocation[0]).
                putExtra("top", screenLocation[1]).
                putExtra("width", view.getWidth()).
                putExtra("height", view.getHeight()).
                putExtra("image", url);

        startActivity(intent);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private int i = 1;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFavoriteClicked(OnFavoriteClicked event){
    }
}
