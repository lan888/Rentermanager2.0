package com.example.ian.rentermanager2;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class ItemFragment3 extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private myDatabaseHelper dbHelper;
    private MyItemRecyclerViewAdapter mAdapter;
    private List<Bill> mDatas = new ArrayList<Bill>();
    String pi = null;
    String po =null;
    String pp =null;
    String pa = null;
    public ItemFragment3() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = myDatabaseHelper.getInstance(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from bill", null);

            while (cursor.moveToNext()) {

                String room = cursor.getString(cursor.getColumnIndex("room"));
                String time = cursor.getString(cursor.getColumnIndex("time"));

               mDatas.add(new Bill(room,time));

        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list2, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            mAdapter = new MyItemRecyclerViewAdapter(mDatas);
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }
    public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {


        private List<Bill> mData;


        public MyItemRecyclerViewAdapter(List<Bill> data) {
            this.mData = data;

        }

        @Override
        public MyItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_time, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mContent.setText(mData.get(position).getRoom());
            holder.mId.setText(String.valueOf(position+1));
            holder.mTime.setText(mData.get(position).getTime());
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView mId;
            TextView mContent;
            TextView mTime;


            public ViewHolder(View itemView) {
                super(itemView);
                mId = itemView.findViewById(R.id.id);
                mId.setOnClickListener(this);
                mContent = itemView.findViewById(R.id.content);
                mContent.setOnClickListener(this);
                mTime = itemView.findViewById(R.id.time);
                mTime.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                        String roomInfo = mDatas.get(getAdapterPosition()).getRoom().toString();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor cursor = db.rawQuery("select * from bill where room=?",new String[]{roomInfo});
                        while (cursor.moveToNext()) {
                            pi = cursor.getString(cursor.getColumnIndex("room"));
                            po = cursor.getString(cursor.getColumnIndex("time"));
                            pp = cursor.getString(cursor.getColumnIndex("total"));
                           // pa = cursor.getString(cursor.getColumnIndex("status"));
                        }
                        Toast.makeText(getActivity(),"房间号为："+pi+"\n时间："+po+"\n总金额为："+pp,Toast.LENGTH_SHORT).show();


                }

            }
        }


}
