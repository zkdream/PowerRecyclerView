package com.zk.powerrecyclerview.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author Created by zhangke
 * @filename WrapRecyclerAdapter
 * @date on 2017\12\7 0007 19:14
 * @email 206357792@qq.com
 * @describe 可以添加头部和底部的RecyclerView
 */

public class WrapRecyclerAdapter extends RecyclerView{


    public WrapRecyclerAdapter(Context context) {
        super(context);
    }

    public WrapRecyclerAdapter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecyclerAdapter(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


}
