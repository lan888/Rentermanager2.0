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

import com.example.ian.rentermanager2.entity.Rooms;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class ItemFragment2 extends Fragment {

    // TODO: Customize parameters
    private MyItemRecyclerViewAdapter mAdapter;
    private ArrayList<String> mDatas = new ArrayList<String>();
    String pi = null;
    String po =null;
    String pp =null;
    String pa = null;
    public ItemFragment2() {

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
           BmobQuery<Rooms> query = new BmobQuery<Rooms>();
           query.findObjects(new FindListener<Rooms>(){
               @Override
               public void done(List<Rooms> list, BmobException e) {
                   if (e == null) {
                       for (Rooms r : list) {
                           String s = r.getRoom();
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
                String roomInfo = mDatas.get(getAdapterPosition());
                BmobQuery query = new BmobQuery<Rooms>();
                query.addWhereEqualTo("room",roomInfo);
                query.setLimit(1);
                query.findObjects(new FindListener<Rooms>() {
                    @Override
                    public void done(List<Rooms> list, BmobException e) {
                        if(e==null){
                            Log.e("success","查询成功:"+list.size()+"条数据");
                            for (  Rooms r  : list){
                                pi = r.getRoom();
                                po = r.getType();
                                pa = r.getStatus();
                                pp = r.getArea();
                            }
                            Toast.makeText(getActivity(),"房间号为："+pi+"\n户型："+po+"\n面积为："+
                                    pp+"㎡"+"\n是否已出租："+pa,Toast.LENGTH_SHORT).show();
                        }else {
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }

                });


            }
        }
    }



}
