package com.zk.powerrecyclerview.widget;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.zk.powerrecyclerview.adapter.OnItemClickListener;
import com.zk.powerrecyclerview.adapter.OnLongClickListener;

/**
 * @author Created by zhangke
 * @filename WrapRecyclerAdapter
 * @date on 2017\12\7 0007 19:14
 * @email 206357792@qq.com
 * @describe 可以添加头部和底部的Adapter
 */

public class WrapRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    // 用来存放底部和头部View的集合  比Map要高效一些
    private SparseArray<View> mHeaderViews;
    private SparseArray<View> mFooterViews;

    //  基本头部类型开始的位置 用于ViewType
    private static int BASE_ITEM_TYPE_HEADER=10000000;
    //  基本底部类型开始的位置 用于ViewType
    private static int BASE_ITEM_TYPE_FOOTER=20000000;

    private RecyclerView.Adapter mAdapter;

    public WrapRecyclerAdapter(RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
        mHeaderViews=new SparseArray<>();
        mFooterViews=new SparseArray<>();
    }

    @Override
    public int getItemViewType(int position) {

        if (isHeaderPosition(position)){
//             查看第几个位置的键
            return mHeaderViews.keyAt(position);
        }
        if (isFooterPosition(position)){
            position=position-mHeaderViews.size()-mAdapter.getItemCount();
            return mFooterViews.keyAt(position);
        }
        position=position-mHeaderViews.size();
        return mAdapter.getItemViewType(position);

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
        if (isHeaderViewType(viewType)){
            View headerView=mHeaderViews.get(viewType);
            return createHeaderFooterViewHolder(headerView);
        }
        if (isFooterViewType(viewType)) {
            View footerView=mFooterViews.get(viewType);
            return createHeaderFooterViewHolder(footerView);
        }
        return mAdapter.onCreateViewHolder(parent,viewType);
    }



    private RecyclerView.ViewHolder createHeaderFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {

        };

    }

    /**
     * 是不是头部类型
     * @param viewType
     * @return
     */
    private boolean isHeaderViewType(int viewType) {
        /**
         * 查看键所在位置，由于采用二分法查找键的位置，所以没有的话返回小于0的数值，而不是返回-1，这点要注意，
         * 返回的负数其实是表示它在哪个位置就找不到了，如果你存了5个，查找的键大于5个值的话，返回就是-6：
         */
        int position=mHeaderViews.indexOfKey(viewType);
        return position>=0;
    }

    /**
     * 判断是不是底部类型
     * @param viewType
     * @return
     */
    private boolean isFooterViewType(int viewType) {
        int position = mFooterViews.indexOfKey(viewType);
        return position >= 0;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (isHeaderPosition(position)||isFooterPosition(position)){
            return;
        }
//        计算一下位置
        final int adapterPosition= position - mHeaderViews.size();

        mAdapter.onBindViewHolder(holder,adapterPosition);

//        设置点击和长按事件
        if (mItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.OnItemClick(position);
                }
            });
        }
        if (mLongClickListener!=null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onLongClick(position);
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return mAdapter.getItemCount()+mHeaderViews.size()+mFooterViews.size();
    }

    public RecyclerView.Adapter getAdapter(){
        return mAdapter;
    }

    /**
     * 添加头部
     * @param view
     */
    public void addHeaderView(View view){
        int position=mHeaderViews.indexOfValue(view);
        if (position<0){
            mHeaderViews.put(BASE_ITEM_TYPE_HEADER++,view);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加底部
     * @param view
     */
    public void addFooterView(View view){
        int position=mFooterViews.indexOfValue(view);
        if (position<0){
            mFooterViews.put(BASE_ITEM_TYPE_FOOTER++,view);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除头部
     * @param view
     */
    public void removeHeaderView(View view){
        int index=mHeaderViews.indexOfValue(view);
        if (index<0) return;
        mHeaderViews.removeAt(index);
        notifyDataSetChanged();
    }

    public void removeFooterView(View view){
        int index=mFooterViews.indexOfValue(view);
        if (index<0) return;
        mFooterViews.removeAt(index);
        notifyDataSetChanged();
    }

    public void adjustSpanSize(RecyclerView recycler){
      if (recycler.getLayoutManager() instanceof GridLayoutManager){
          final GridLayoutManager layoutManager= (GridLayoutManager) recycler.getLayoutManager();
          layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
              @Override
              public int getSpanSize(int position) {
                  boolean isHeaderOrFooter=isHeaderPosition(position)||isFooterPosition(position);
                  return isHeaderOrFooter?layoutManager.getSpanCount():1;
              }
          });
      }
    }

    /**
     * 是不是头部
     * @param position
     * @return
     */
    private boolean isHeaderPosition(int position) {
        return position<mHeaderViews.size();
    }

    /**
     * 是不是底部
     * @param position
     * @return
     */
    private boolean isFooterPosition(int position) {
        return position>=(mHeaderViews.size()+mAdapter.getItemCount());
    }

    /**
     * 给条目设置点击和长按事件
     */
    public OnItemClickListener mItemClickListener;
    public OnLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.mItemClickListener=itemClickListener;

    }

    public void setOnLongClickListener(OnLongClickListener longClickListener){
        this.mLongClickListener=longClickListener;
    }
}
