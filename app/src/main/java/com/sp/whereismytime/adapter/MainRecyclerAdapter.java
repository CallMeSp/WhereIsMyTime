package com.sp.whereismytime.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sp.whereismytime.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/22.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MyHolder> {

    private ArrayList<String> usedetails=new ArrayList<>();

    private LayoutInflater inflater;

    private Context context;

    class MyHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.textview_usedetail);
        }
    }

    public MainRecyclerAdapter(Context context,ArrayList<String> details) {
        this.usedetails=details;
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return usedetails.size();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.activity_main_details_item,parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.textView.setText(usedetails.get(position));
    }
}
