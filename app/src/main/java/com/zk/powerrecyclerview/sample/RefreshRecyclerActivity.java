package com.zk.powerrecyclerview.sample;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zk.powerrecyclerview.R;
import com.zk.powerrecyclerview.adapter.CommonRecyclerAdapter;
import com.zk.powerrecyclerview.adapter.ViewHolder;
import com.zk.powerrecyclerview.widget.RefreshRecyclerView;
import com.zk.powerrecyclerview.widget.RefreshViewCreator;
import com.zk.powerrecyclerview.widget.defaultcreator.DefaultRefreshCreator;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.CENTER;

public class RefreshRecyclerActivity extends AppCompatActivity implements RefreshRecyclerView.OnRefreshListener{
    private RefreshRecyclerView mRecyclerView;

    private List<String> mData;

    private HomeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_recycler_view);
        mRecyclerView=findViewById(R.id.rv_refresh);
        mData=new ArrayList<>();

        initData();
        mRecyclerView.addRefreshViewCreator(new DefaultRefreshCreator());
        mRecyclerView.setOnRefreshListener(this);
        // 设置正在获取数据页面和无数据页面
        mRecyclerView.addLoadingView(findViewById(R.id.load_view));
        mRecyclerView.addEmptyView(findViewById(R.id.empty_view));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter = new HomeAdapter(RefreshRecyclerActivity.this, mData);
                mRecyclerView.setAdapter(mAdapter);
                TextView textView=new TextView(RefreshRecyclerActivity.this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setGravity(CENTER);
                textView.setText("就这样玩");
                mRecyclerView.addHeaderView(textView);
            }
        },2000);

    }

    private void initData() {
        for (int i = 'A'; i < 'z'; i++) {
            mData.add("这是啥" + (char) i);
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.onStopRefresh();
            }
        }, 2000);

    }
    class HomeAdapter extends CommonRecyclerAdapter<String>{

        public HomeAdapter(Context context, List<String> data) {
            super(context, data, R.layout.item);
        }

        @Override
        public void convert(ViewHolder holder, String item) {
            holder.setText(R.id.tv, item);
        }
    }
}
