package com.zk.powerrecyclerview.splitline;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Created by zhangke
 * @filename DividerGridItemDecoration
 * @date on 2017\12\5 0005 19:29
 * @email 206357792@qq.com
 * @describe RecyclerView的GridLayout布局的样式
 */

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration{

    private Drawable mDivider;
    private int[] attrs=new int[]{
            android.R.attr.listDivider
    };

    public DividerGridItemDecoration(Context context) {
        TypedArray a=context.obtainStyledAttributes(attrs);
        mDivider=a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c,parent);
        drawHorizontal(c,parent);
    }

    /**
     * 绘制水平的分割线
     * @param c
     * @param parent
     */
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount=parent.getChildCount();
        for (int i = 0; i <childCount ; i++) {
            View child=parent.getChildAt(i);
            RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) child.getLayoutParams();
            int left=child.getLeft()-params.leftMargin;
            int right=child.getRight()+params.rightMargin;
            int top=child.getBottom()+params.bottomMargin;
            int bottom=top+mDivider.getIntrinsicHeight();
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 绘制垂直间的间隔线
     * @param c
     * @param parent
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        int childCount=parent.getChildCount();
        for (int i = 0; i <childCount ; i++) {
            View child=parent.getChildAt(i);
            RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) child.getLayoutParams();
            int left=child.getRight()+params.rightMargin;
            int right=left+mDivider.getIntrinsicWidth();
            int top=child.getTop()+params.topMargin;
            int bottom=child.getBottom()+params.bottomMargin;
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int right=mDivider.getIntrinsicWidth();
        int bottom=mDivider.getIntrinsicHeight();

        int position=((RecyclerView.LayoutParams)view.getLayoutParams()).getViewLayoutPosition();

        if (isLastColumn(position,parent)){
            right=0;
        }
        if (isLastRow(position,parent)){
            bottom=0;
        }
        outRect.set(0,0,right,bottom);
    }

    /**
     * 是否是最后一列
     * @param position
     * @param parent
     * @return
     */
    private boolean isLastColumn(int position, RecyclerView parent) {
        int spanCount=getSpanCount(parent);
        if ((position+1)%spanCount==0){
            return true;
        }else {
            return false;
        }
    }



    /**
     * 是否是最后一行
     * @param position
     * @param parent
     * @return
     */
    private boolean isLastRow(int position, RecyclerView parent) {

        int spanCount=getSpanCount(parent);

        int childCount=parent.getAdapter().getItemCount();

        int rowNumber=childCount%spanCount==0?childCount/spanCount: (childCount / spanCount) + 1;
        if (position>((rowNumber-1)*spanCount-1)){
            return true;
        }
        return false;
    }

    /**
     * 获取一行有多少列
     * @param parent
     * @return
     */
    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager=parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
//            获取一行的列数
            GridLayoutManager gridLayoutManager= (GridLayoutManager) layoutManager;
            int spanCount=gridLayoutManager.getSpanCount();
            return spanCount;
        }
        return 1;
    }
}
