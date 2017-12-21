package com.zk.powerrecyclerview.sample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;

import com.zk.powerrecyclerview.R;
import com.zk.powerrecyclerview.adapter.CommonRecyclerAdapter;
import com.zk.powerrecyclerview.adapter.ViewHolder;
import com.zk.powerrecyclerview.widget.WrapRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WrapRecyclerView wrapRecyclerView;
    private ArrayList<String> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 100; i++) {
            data.add(i+"测试");
        }

        wrapRecyclerView=findViewById(R.id.wrv_simple);

        wrapRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        wrapRecyclerView.setAdapter(new CommonRecyclerAdapter<String>(MainActivity.this,data,R.layout.item) {
            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.tv,s);
            }
        });
        TextView textView=new TextView(this);
        textView.setText("头部");
        wrapRecyclerView.addHeaderView(textView);
        wrapRecyclerView.addFooterView(textView);



    }


}
