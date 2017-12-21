package com.zk.powerrecyclerview.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Created by zhangke
 * @filename LoadViewCreator
 * @date on 2017\12\21 0021 16:06
 * @email 206357792@qq.com
 * @describe 上拉加载更多的辅助类
 */

public abstract class LoadViewCreator {

    /**
     * 获取上拉加载更多的View
     * @param context
     * @param parent
     * @return
     */
    public abstract View getLoadView(Context context, ViewGroup parent);

    /**
     * 正在上拉加载
     * @param currentDragHeight 当前拖动的距离
     * @param loadViewHeight 总的高度
     * @param currentLoadStatus 当前的状态
     */
    public abstract void onPull(int currentDragHeight,int loadViewHeight,int currentLoadStatus);

    /**
     * 正在加载中
     */
    public abstract void onLoading();

    /**
     * 停止加载中
     */
    public abstract void onStopLoad();
    

}
