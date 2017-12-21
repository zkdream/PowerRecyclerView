package com.zk.powerrecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author Created by zhangke
 * @filename CommonRecyclerAdapter
 * @date on 2017\12\7 0007 19:20
 * @email 206357792@qq.com
 * @describe RecyclerView的通用Adapter
 */

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;

//    数据
    private List<T> mData;
//    布局
    private int mLayoutId;

    private MultiTypeSupport mMultiTypeSupport;

    public CommonRecyclerAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mLayoutId = layoutId;
    }

    public CommonRecyclerAdapter(Context context, List<T> data, MultiTypeSupport multiTypeSupport) {
        this(context,data,-1);
        this.mMultiTypeSupport = multiTypeSupport;
    }

    /**
     * 根据当前位置获取不同的viewType
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (mMultiTypeSupport!=null){
            return mMultiTypeSupport.getLayoutId(mData.get(position), position);
        }
        return super.getItemViewType(position);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        多布局的支持
        if (mMultiTypeSupport!=null){
            mLayoutId=viewType;
        }
//        先加载数据
        View itemView=mInflater.inflate(mLayoutId,parent,false);
//        返回ViewHolder
        ViewHolder holder=new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // 设置点击和长按事件
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
//        绑定布局 回传回去
        convert(holder,mData.get(position));
    }

    /**
     * 利用抽象方法回传出去，每个不一样的Adapter去设置
     * @param holder
     * @param t
     */
    public abstract void convert(ViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 设置条目的点击事件和长按事件
     */
    public OnItemClickListener mItemClickListener;
    public OnLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.mItemClickListener=itemClickListener;
    }
    public void setOnLongClickListener(OnLongClickListener onLongClickListener){
        this.mLongClickListener=onLongClickListener;
    }

}
