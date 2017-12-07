package com.example.ian.rentermanager2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ian.rentermanager2.entity.Renters;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class ItemFragment1 extends Fragment {

    // TODO: Customize parameters
    private MyItemRecyclerViewAdapter mAdapter;
    private ArrayList<String> mDatas = new ArrayList<String>();

    String s = null;
    String ss = null;
    String s2 = null;
    String s3 = null;
    String s4 = null;
    String s5 = null;
    String s6 = null;
    String s7 = null;

    public ItemFragment1() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View view = inflater.inflate(R.layout.fragment_item_list1, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {

            BmobQuery<Renters> query = new BmobQuery<Renters>();
            query.findObjects(new FindListener<Renters>(){
                @Override
                public void done(List<Renters> list, BmobException e) {
                    if (e == null) {
                        for (Renters r : list) {
                            s = r.getName();
                            mDatas.add(s);

                        }
                        RecyclerView recyclerView = (RecyclerView) view;
                        mAdapter = new MyItemRecyclerViewAdapter(mDatas);
                        recyclerView.setAdapter(mAdapter);

                    }else {
                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }

            });
        }


        return view;
    }


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
            holder.mId.setText(String.valueOf(position + 1));
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

                String nameInfo = mDatas.get(getAdapterPosition());
                BmobQuery query = new BmobQuery<Renters>();
                query.addWhereEqualTo("name",nameInfo);
                query.setLimit(1);
                query.findObjects(new FindListener<Renters>() {
                    @Override
                    public void done(List<Renters> list, BmobException e) {
                        if(e==null){
                            Log.e("success","查询成功:"+list.size()+"条数据");
                            for (  Renters r  : list){
                                ss=r.getName();
                                s2=r.getRoom();
                                s3=r.getSex();
                                s4=r.getCompany();
                                s5=r.getPhone();
                                s6=r.getIdcard();
                                s7=r.getMember();
                            }
                            Toast.makeText(getActivity(),"租户姓名："+ss+"\n租户房间号："+s2+"\n租户性别："
                                            +s3+"\n租户工作单位："+s4+"\n手机号码："+ s5+"\n租户身份证号："+s6+"\n租户人数："+s7,
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }

                });

            }
        }


    }
    }





