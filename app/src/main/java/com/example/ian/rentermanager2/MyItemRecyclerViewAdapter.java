package com.example.ian.rentermanager2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {


    private ArrayList<String> mData;


    public MyItemRecyclerViewAdapter(ArrayList<String> data) {
        this.mData = data;

    }

    @Override
    public MyItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mContent.setText(mData.get(position));
        holder.mId.setText(String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mId;
        TextView mContent;


        public ViewHolder(View itemView) {
            super(itemView);
            mId = itemView.findViewById(R.id.id);
            mId.setOnClickListener(this);
            mContent = itemView.findViewById(R.id.content);
            mContent.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
