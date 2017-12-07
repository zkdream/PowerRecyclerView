package com.zk.powerrecyclerview.adapter;

/**
 * @author Created by zhangke
 * @filename MultiTypeSupport
 * @date on 2017\12\7 0007 19:30
 * @email 206357792@qq.com
 * @describe 多布局的支持接口
 */

public interface MultiTypeSupport<T> {
//    根据当前位置返回或者条目返回不同的布局
    public int getLayoutId(T item, int position);
}
