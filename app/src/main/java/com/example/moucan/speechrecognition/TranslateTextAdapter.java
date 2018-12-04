package com.example.moucan.speechrecognition;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.zip.Inflater;


public class TranslateTextAdapter extends RecyclerView.Adapter<TranslateTextAdapter.MyHolder> {
    List<String> list;
    Context context;
    HashSet<String> mSet=new HashSet<>();
    boolean collected=false;
    public TranslateTextAdapter(Context context,List<String> list){
        this.list=list;
        this.context=context;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_translate_text,null);
        MyHolder holder=new MyHolder(view);
        holder.setIcon();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.imageView.setImageResource(R.mipmap.collection_red);
        myHolder.textView.setText(list.get(i));
        myHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSet.contains(list.get(i))){
                    mSet.add(list.get(i));
                    collected=true;
                    myHolder.setIcon();
                }else {
                    mSet.remove(list.get(i));
                    collected=false;
                    myHolder.setIcon();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.tv_single_translate);
            imageView=(ImageView) itemView.findViewById(R.id.iv_single_collection);
        }
        public void setIcon(){
            if (collected){
                imageView.setImageResource(R.mipmap.collection_all_red);
            }else {
                imageView.setImageResource(R.mipmap.collection_red);
            }
        }
    }
    public ArrayList<String> getList(){
        ArrayList<String> list=new ArrayList<>();
        for (String s:mSet){
            list.add(s);
        }
        return list;
    }
}
