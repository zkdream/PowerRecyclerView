package com.zk.powerrecyclerview.widget.defaultcreator;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.zk.powerrecyclerview.R;
import com.zk.powerrecyclerview.widget.RefreshViewCreator;

/**
 * @author Created by zhangke
 * @filename DefaultRefreshCreator
 * @date on 2017\12\21 0021 15:11
 * @email 206357792@qq.com
 * @describe TODO
 */

public class DefaultRefreshCreator extends RefreshViewCreator{
//    加载数据的ImageView
    private View mRefreshIv;

    @Override
    public View getRefreshView(Context context, ViewGroup parent) {
        View refreshView= LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_view,parent,false);
        mRefreshIv=refreshView.findViewById(R.id.refresh_iv);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {
        float rotate = ((float) currentDragHeight) / refreshViewHeight;
        // 不断下拉的过程中旋转图片
        mRefreshIv.setRotation(rotate);
    }

    @Override
    public void onRefreshing() {
        // 刷新的时候不断旋转
        RotateAnimation animation = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(1000);
        mRefreshIv.startAnimation(animation);

    }

    @Override
    public void onStopRefresh() {
        mRefreshIv.setRotation(0);
        mRefreshIv.clearAnimation();

    }
}
