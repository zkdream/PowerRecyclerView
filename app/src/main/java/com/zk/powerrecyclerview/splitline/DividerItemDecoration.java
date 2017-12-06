package com.zk.powerrecyclerview.splitline;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Created by zhangke
 * @filename DividerItemDecoration
 * @date on 2017\12\5 0005 19:36
 * @email 206357792@qq.com
 * @describe RecycleView垂直布局的分割线
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration{

    /**
     * getItemOffests可以通过outRect.set(l,t,r,b)设置指定itemview的paddingLeft，paddingTop， paddingRight， paddingBottom
     * onDraw可以通过一系列c.drawXXX()方法在绘制itemView之前绘制我们需要的内容。
     * onDrawOver与onDraw类似，只不过是在绘制itemView之后绘制，具体表现形式，就是绘制的内容在itemview上层。
     *  ItemDecoration会被add到集合中,然后RecyclerView会根据add的顺序依次调用(getItemOffsets->onDraw->onDrawOver)
     */
    private static final int[] ATTRS=new int[]{android.R.attr.listDivider};

    public static final int HORIZONTAL_LIST= LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST=LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private int mOrientation;

    private int mWidth;
    private int mHeight;

    public DividerItemDecoration(Context context,int orientation) {
        TypedArray a=context.obtainStyledAttributes(ATTRS);
        mDivider=a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);

    }
    /**
     * 支持自定义dividerDrawable
     * @param context
     * @param orientation
     * @param dividerDrawable
     */
    public DividerItemDecoration(Context context, int orientation, Drawable dividerDrawable) {
        mDivider = dividerDrawable;
        setOrientation(orientation);
    }

    private void setOrientation(int orientation) {
        if (orientation!=HORIZONTAL_LIST&&orientation!=VERTICAL_LIST){
            throw new IllegalArgumentException("兄弟设置一下Orientation");
        }
        mOrientation=orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation==VERTICAL_LIST){
            drawVertical(c, parent);
        }else {
            drawHorizontal(c, parent);
        }

    }

    /**
     * 支持设置分割线的样式
     * @param drawable
     */
    public void setDrawable(@NonNull Drawable drawable) {
        if(drawable == null) {
            throw new IllegalArgumentException("兄弟你设置的分割线不存在");
        } else {
            this.mDivider = drawable;
        }
    }
    /**
     * 支持手动为无高宽的drawable制定宽度
     * @param width
     */
    public void setWidth(int width) {
        this.mWidth = width;
    }
    /**
     * 支持手动为无高宽的drawable制定高度
     * @param height
     */
    public void setHeight(int height) {
        this.mHeight = height;
    }


    private void drawVertical(Canvas c, RecyclerView parent) {
        int left=parent.getPaddingLeft();
        int right=parent.getWidth()-parent.getPaddingRight();

        int childCount=parent.getChildCount();
        for (int i = 0; i <childCount ; i++) {
            View child=parent.getChildAt(i);
            RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) child.getLayoutParams();
            int top=child.getBottom()+params.bottomMargin;
            int bottom=top+mDivider.getIntrinsicHeight();
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }


    private void drawHorizontal(Canvas c, RecyclerView parent) {
       int top=parent.getPaddingTop();
       int bottom=parent.getHeight()-parent.getPaddingBottom();

       int childCount=parent.getChildCount();

        for (int i = 0; i <childCount ; i++) {
            View child=parent.getChildAt(i);
            RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) child.getLayoutParams();
            int left=child.getRight()+params.rightMargin;
            int right=left+mDivider.getIntrinsicHeight();
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation==VERTICAL_LIST){
            outRect.set(0,0,0,getDividerHeight());
        }else {
            outRect.set(0,0,getDividerWidth(),0);
        }
    }

    private int getDividerWidth() {
        return mWidth > 0 ? mWidth : mDivider.getIntrinsicWidth();
    }
    private int getDividerHeight() {
        return mHeight > 0 ? mHeight : mDivider.getIntrinsicHeight();
    }

}
