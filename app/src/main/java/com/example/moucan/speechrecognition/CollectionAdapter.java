package com.example.moucan.speechrecognition;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionHolder> {
    Context context;
    List<String> mList;
    public CollectionAdapter(Context context,List<String> mlist){
        this.context=context;
        this.mList=mlist;

    }
    @NonNull
    @Override
    public CollectionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_collection,null);
        CollectionHolder holder=new CollectionHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionHolder collectionHolder, int i) {
        collectionHolder.textView.setText(mList.get(i));
        collectionHolder.itemView.setOnClickListener(v -> {
            //添加复制事件
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CollectionHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public CollectionHolder(@NonNull View itemView) {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.tv_collection);
        }
    }
}
