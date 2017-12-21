package com.zk.powerrecyclerview.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Created by zhangke
 * @filename RefreshViewCreator
 * @date on 2017\12\21 0021 10:47
 * @email 206357792@qq.com
 * @describe 下拉刷新的辅助类为了匹配所有的效果
 */

public abstract class RefreshViewCreator {
    /**
     * 获取下拉刷新的View
     * @param context 上下文
     * @param parent RecyclerView
     * @return
     */
    public abstract View getRefreshView(Context context, ViewGroup parent);

    /**
     *  正在下拉
     * @param currentDragHeight 当前拖动的高度
     * @param refreshViewHeight 总的刷新的高度
     * @param currentRefreshStatus 当前的状态
     */
    public abstract void onPull(int currentDragHeight,int refreshViewHeight,int currentRefreshStatus);

    /**
     * 正在刷新
     */
    public abstract void onRefreshing();

    /**
     * 停止刷新
     */
    public abstract void onStopRefresh();














}
