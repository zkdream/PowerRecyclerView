package com.zk.powerrecyclerview.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Created by zhangke
 * @filename RefreshRecyclerView
 * @date on 2017\12\21 0021 10:45
 * @email 206357792@qq.com
 * @describe  下拉刷新的RecyclerView
 */

public class RefreshRecyclerView extends WrapRecyclerView{
//    下拉刷新的辅助类
    private RefreshViewCreator mRefreshCreator;
    // 下拉刷新头部的高度
    private int mRefreshViewHeight=0;
    // 下拉刷新的头部View
    private View mRefreshView;
    // 手指按下的Y位置
    private int mFingerDownY;
    // 手指拖拽的阻力指数
    protected float mDragIndex=0.35f;
    // 当前是否正在拖动
    private boolean mCurrentDrag=false;
    // 当前的状态
    private int mCurrentRefreshStatus;
    // 默认状态
    private int REFRESH_STATUS_NORMAL=0x0011;
    // 下拉刷新状态
    private int REFRESH_STATUS_PULL_DOWN_REFRESH=0x0022;
    // 松开刷新状态
    private int REFRESH_STATUS_LOOSEN_REFRESHING=0x0033;
    // 正在刷新状态
    private int REFRESH_STATUS_REFRESHING=0x0044;

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addRefreshViewCreator(RefreshViewCreator refreshCreator){
        this.mRefreshCreator=refreshCreator;
        addRefreshView();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshView();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置 ,之所以写在dispatchTouchEvent那是因为如果我们处理了条目点击事件，
                // 那么就不会进入onTouchEvent里面，所以只能在这里获取
                mFingerDownY= (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentDrag){
                    restoreRefreshView();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 重置当前刷新的状态
     */
    private void restoreRefreshView() {
        int currentTopMargin=((MarginLayoutParams)mRefreshView.getLayoutParams()).topMargin;
        int finalTopMargin=-mRefreshViewHeight+1;
        if (mCurrentRefreshStatus==REFRESH_STATUS_LOOSEN_REFRESHING){
            finalTopMargin=0;
            mCurrentRefreshStatus=REFRESH_STATUS_REFRESHING;
            if (mRefreshCreator!=null){
                mRefreshCreator.onRefreshing();
            }
            if (mListener!=null){
                mListener.onRefresh();
            }
        }

        int distance=currentTopMargin-finalTopMargin;
//     回弹到当前位置
        ValueAnimator animator= ObjectAnimator.ofFloat(currentTopMargin,finalTopMargin).setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentTopMargin= (float) animation.getAnimatedValue();
                setRefreshViewMarginTop((int) currentTopMargin);
            }
        });
        animator.start();
        mCurrentDrag=false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_MOVE:
                // 如果是在最顶部才处理，否者就不用去处理
                if (canScrollUp()||mCurrentRefreshStatus==REFRESH_STATUS_REFRESHING
                        ||mRefreshView==null||mRefreshCreator==null){
                //如果没有到达最顶端，也就是说还可以向上滚动就什么都不用去处理
                    return super.onTouchEvent(e);
                }
                //解决下拉刷新自动滚动问题
                if (mCurrentDrag){
                    scrollToPosition(0);
                }
                // 获取手指触摸拖拽的距离
                int distanceY= (int) ((e.getRawY()-mFingerDownY)*mDragIndex);
                //  如果是已经到达头部，并且不断的向下拉，那么不断的改变refreshView的marginTop的值
                if (distanceY>0){
                    int marginTop=distanceY-mRefreshViewHeight;
                    setRefreshViewMarginTop(marginTop);
                    updateRefreshStatus(distanceY);
                    mCurrentDrag=true;
                    return false;
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 更新刷新的状态
     * @param distanceY
     */
    private void updateRefreshStatus(int distanceY) {
        if (distanceY<=0){
            mCurrentRefreshStatus=REFRESH_STATUS_NORMAL;
        }else if (distanceY<mRefreshViewHeight){
            mCurrentRefreshStatus=REFRESH_STATUS_PULL_DOWN_REFRESH;
        }else {
            mCurrentRefreshStatus=REFRESH_STATUS_LOOSEN_REFRESHING;
        }
        if (mRefreshCreator!=null){
            mRefreshCreator.onPull(distanceY,mRefreshViewHeight,mCurrentRefreshStatus);
        }
    }

    /**
     * 添加头部的刷新View
     */
    private void addRefreshView() {
        RecyclerView.Adapter adapter=getAdapter();
        if (adapter!=null&&mRefreshCreator!=null){
//            添加头部的刷新View
            View refreshView=mRefreshCreator.getRefreshView(getContext(),this);
            if (refreshView!=null){
                addHeaderView(refreshView);
                this.mRefreshView=refreshView;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mRefreshView!=null&&mRefreshViewHeight<=0){
//            获取头部刷新View的高度
            mRefreshViewHeight=mRefreshView.getMeasuredHeight();

            if (mRefreshViewHeight>0){
                // 隐藏头部刷新的View  marginTop  多留出1px防止无法判断是不是滚动到头部问题
                setRefreshViewMarginTop(-mRefreshViewHeight + 1);
            }
        }
    }

    /**
     * 设置刷新View的marginTop
     * @param marginTop
     */
    private void setRefreshViewMarginTop(int marginTop) {
      MarginLayoutParams params= (MarginLayoutParams) mRefreshView.getLayoutParams();
      if (marginTop<-mRefreshViewHeight+1){
          marginTop=-mRefreshViewHeight+1;
      }
      params.topMargin=marginTop;
      mRefreshView.setLayoutParams(params);
    }

    /**
     * 判断是不是滚动到了最顶部，这个是从SwipeRefreshLayout里面copy过来的源代码
     * @return
     */
    private boolean canScrollUp() {
        if (Build.VERSION.SDK_INT<14){
            // 检查这个View在垂直上面是否可以滑动
            return canScrollVertically(-1)||this.getScrollY() > 0;
        }else {
            return canScrollVertically(-1);
        }

    }

    public void onStopRefresh(){
        mCurrentRefreshStatus=REFRESH_STATUS_NORMAL;
        restoreRefreshView();
        if (mRefreshCreator!=null){
            mRefreshCreator.onStopRefresh();
        }
    }


    private OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener){
        this.mListener=listener;
    }

    public interface OnRefreshListener{
        void onRefresh();
    }
}
