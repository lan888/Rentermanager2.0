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


/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class ItemFragment1 extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private myDatabaseHelper dbHelper;
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
        dbHelper = myDatabaseHelper.getInstance(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select name from renter", null);
        while (cursor.moveToNext()) {
           String s = cursor.getString(cursor.getColumnIndex("name"));
            mDatas.add(s);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list1, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            mAdapter = new MyItemRecyclerViewAdapter(mDatas);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
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

                        String nameInfo = mDatas.get(getAdapterPosition()).toString();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor cursor = db.rawQuery("select * from renter where name=?",new String[]{nameInfo});
                        while (cursor.moveToNext()) {
                            ss = cursor.getString(cursor.getColumnIndex("name"));
                            s2 = cursor.getString(cursor.getColumnIndex("room"));
                            s3 = cursor.getString(cursor.getColumnIndex("sex"));
                            s4 = cursor.getString(cursor.getColumnIndex("company"));
                            s5 = cursor.getString(cursor.getColumnIndex("phone"));
                            s6 = cursor.getString(cursor.getColumnIndex("idcard"));
                            s7 = cursor.getString(cursor.getColumnIndex("member"));

                        }
                        Toast.makeText(getActivity(),"租户姓名："+ss+"\n租户房间号："+s2+"\n租户性别："+s3+"\n租户工作单位："+s4+"\n手机号码："+s5+"\n租户身份证号："+s6+"\n租户人数："+s7,Toast.LENGTH_SHORT).show();



                }


            }
        }





    }





