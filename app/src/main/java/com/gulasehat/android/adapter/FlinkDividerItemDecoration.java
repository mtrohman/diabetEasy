package com.gulasehat.android.adapter;

import android.content.Context;
import android.util.Log;

import com.gulasehat.android.util.Util;
import com.yanyusong.y_divideritemdecoration.Y_Divider;
import com.yanyusong.y_divideritemdecoration.Y_DividerBuilder;
import com.yanyusong.y_divideritemdecoration.Y_DividerItemDecoration;

public class FlinkDividerItemDecoration extends Y_DividerItemDecoration {


    public FlinkDividerItemDecoration(Context context) {
        super(context);
    }

    @Override
    public Y_Divider getDivider(int itemPosition) {
        Y_Divider divider = null;
        switch (itemPosition % 2) {
            case 0:
                if(Util.isRTL()){
                    Log.d("burki", "sol rtl");
                    divider = new Y_DividerBuilder()
                            .setLeftSideLine(true, 0xffc0c2cd, 0.5f, 0, 0)
                            .setBottomSideLine(true, 0xffc0c2cd, 0.5f, 0, 0)
                            .create();
                }else{
                    Log.d("burki", "Sol normal");

                    divider = new Y_DividerBuilder()
                            //.setLeftSideLine(true,0xffc0c2cd, 1,0,0)
                            .setRightSideLine(true, 0xffc0c2cd, 0.5f, 0, 0)
                            .setBottomSideLine(true, 0xffc0c2cd, 0.5f, 0, 0)
                            .create();
                }

                break;
            case 1:
                if(Util.isRTL()){
                    divider = new Y_DividerBuilder()
                            //.setLeftSideLine(true, 0xffc0c2cd, 1, 0, 0)
                            .setBottomSideLine(true, 0xffc0c2cd, 0.5f, 0, 0)
                            .create();
                }else{
                    Log.d("burki", "Sağ normal");

                    divider = new Y_DividerBuilder()
                            //.setRightSideLine(true, 0xffc0c2cd, 1, 1, 1)
                            .setBottomSideLine(true, 0xffc0c2cd, 0.5f, 0, 0)
                            .create();
                }

                break;
            default:
                break;
        }
        return divider;
    }
}