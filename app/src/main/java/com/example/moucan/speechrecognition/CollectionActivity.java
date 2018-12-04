package com.example.moucan.speechrecognition;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectionActivity extends AppCompatActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_collection)
    RecyclerView rvCollection;

    CollectionAdapter adapter;
    private HashSet<String> mSet;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        initData();
        initView();
    }
    @OnClick({R.id.iv_back})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                overridePendingTransition(R.anim.left_in,R.anim.right_out);
                break;
        }
    }
    private void initView(){
        tvTitle.setText("收藏");
        rvCollection.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter=new CollectionAdapter(this,list);
        rvCollection.setAdapter(adapter);

    }
    private void initData(){
        list=new ArrayList<>();
        Intent intent=getIntent();
        list=intent.getStringArrayListExtra("collection");
    }
    public static void actionStart(Activity activity,List<String> list){
        Intent intent=new Intent(activity,CollectionActivity.class);
        intent.putStringArrayListExtra("collection", (ArrayList<String>) list);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.right_in,R.anim.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
    }
}
