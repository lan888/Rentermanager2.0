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
public class ItemFragment2 extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private myDatabaseHelper dbHelper;
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
        dbHelper = myDatabaseHelper.getInstance(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select room from house", null);
            while (cursor.moveToNext()) {
                String s = cursor.getString(cursor.getColumnIndex("room"));
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
                switch (view.getId()){
                    case R.id.content:
                        String roomInfo = mDatas.get(getAdapterPosition()).toString();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor cursor = db.rawQuery("select * from house where room=?",new String[]{roomInfo});
                        while (cursor.moveToNext()) {
                            pi = cursor.getString(cursor.getColumnIndex("room"));
                            po = cursor.getString(cursor.getColumnIndex("type"));
                            pp = cursor.getString(cursor.getColumnIndex("area"));
                            pa = cursor.getString(cursor.getColumnIndex("status"));
                        }
                        Toast.makeText(getActivity(),"房间号为："+pi+"\n户型："+po+"\n面积为："+pp+"㎡"+"\n是否已出租："+pa,Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.id:
                        String roomInfo1 = mDatas.get(getAdapterPosition()).toString();
                        SQLiteDatabase db1 = dbHelper.getWritableDatabase();
                        Cursor cursor1 = db1.rawQuery("select * from house where room=?",new String[]{roomInfo1});
                        while (cursor1.moveToNext()) {
                            pi = cursor1.getString(cursor1.getColumnIndex("room"));
                            po = cursor1.getString(cursor1.getColumnIndex("type"));
                            pp = cursor1.getString(cursor1.getColumnIndex("area"));
                            pa = cursor1.getString(cursor1.getColumnIndex("status"));
                        }
                        Toast.makeText(getActivity(),"房间号为："+pi+"\n户型："+po+"\n面积为："+pp+"㎡"+"\n是否已出租："+pa,Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        }
    }



}
