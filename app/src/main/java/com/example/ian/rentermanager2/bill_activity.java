package com.example.ian.rentermanager2;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ian on 2017/10/1 0001.
 */

public class bill_activity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<Bill> data = new ArrayList<Bill>();
    private int lastVisibleItem;
    private MyAdapter Adapter;

    private myDatabaseHelper dbHelper;
    String s = null;
    String ss = null;
    String s2 = null;
    String s3 = null;
    String s4 = null;
    String s5 = null;
    String s6 = null;
    String s7 = null;
    String mName = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renter_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dbHelper = myDatabaseHelper.getInstance(this);

        initData();
        initView();


    }

    private void initView() {
        final LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.refresh);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Adapter = new MyAdapter(data);
        mRecyclerView.setAdapter(Adapter);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        srl.setColorSchemeColors(Color.BLUE);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        data.clear();
                        initData();
                        Adapter.notifyDataSetChanged();
                        srl.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == Adapter.getItemCount()) {
                    initData();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Adapter.notifyDataSetChanged();
                    }
                }, 1500);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = manager.findLastVisibleItemPosition();
            }
        });


    }

    private void initData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from bill", null);
        while (cursor.moveToNext()) {
            s = cursor.getString(cursor.getColumnIndex("room"));
            ss = cursor.getString(cursor.getColumnIndex("time"));
            data.add(new Bill(s,ss));
        }
    }

    // public void onItemClick(View view){

    // }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        setTitle("账单");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                AlertDialog.Builder builder = new AlertDialog.Builder(bill_activity.this);
                LayoutInflater factory = LayoutInflater.from(bill_activity.this);
                final View textEntryView = factory.inflate(R.layout.money,null);
                builder.setTitle("收款");
                builder.setView(textEntryView);

                final EditText num =  textEntryView.findViewById(R.id.num);
                final EditText bill = textEntryView.findViewById(R.id.bill);
                final EditText waterBill =  textEntryView.findViewById(R.id.waterBill);
                final EditText electricityBill = textEntryView.findViewById(R.id.electricityBill);


                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String numInfo = num.getText().toString();
                        String billInfo = bill.getText().toString();
                        String waterBillInfo = waterBill.getText().toString();
                        String electricityBillInfo = electricityBill.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String t =format.format(new Date());
                        double to = Double.parseDouble(billInfo)+Double.parseDouble(waterBillInfo)+Double.parseDouble(electricityBillInfo);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor cursor = db.rawQuery("select * from bill where room=?",new String[]{numInfo});
                        if (cursor.moveToNext()) {
                            ss = cursor.getString(cursor.getColumnIndex("room"));

                        }
                        if (numInfo.matches("[0-9]{3}")){
                            if (billInfo.matches("(([1-9][0-9]*)|(([0]\\.\\d{0,2}|[1-9][0-9]*\\.\\d{0,2})))")){
                                if (waterBillInfo.matches("(([1-9][0-9]*)|(([0]\\.\\d{0,2}|[1-9][0-9]*\\.\\d{0,2})))")){
                                    if (electricityBillInfo.matches("(([1-9][0-9]*)|(([0]\\.\\d{0,2}|[1-9][0-9]*\\.\\d{0,2})))")){
                                        db.execSQL("insert into bill(room,bill,waterBill,electricityBill,time,total)values(?,?,?,?,?,?)",
                                                new String[]{numInfo, billInfo, waterBillInfo, electricityBillInfo,t,String.valueOf(to)});
                                    }else {
                                        Toast.makeText(bill_activity.this, "电费数额只能带两位小数", Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    Toast.makeText(bill_activity.this, "水费数额只能带两位小数", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(bill_activity.this, "房租数额只能带两位小数", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(bill_activity.this, "房号为3位纯数字", Toast.LENGTH_SHORT).show();

                        }


                    }

                });
                builder.create().show();
                break;
            case R.id.delete:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(bill_activity.this);
                builder1.setMessage("你确定要清除所有租户数据吗？");

                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (data != null){
                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                            db.execSQL("delete from renter " );
                            db.execSQL("update house set status='否'");
                            db.close();
                            data.clear();
                            Adapter.notifyDataSetChanged();
                        }
                    }
                });
                builder1.create().show();

                break;
        }
        return true;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<Bill> mData;

        public MyAdapter(ArrayList<Bill> data){
            this.mData=data;

        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_renter,parent,false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            holder.mTv.setText(mData.get(position).getRoom());
        }

        @Override
        public int getItemCount() {
            return mData.size() ;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView mTv;
            Button mDel;


            public ViewHolder(View itemView){
                super(itemView);
                mTv = itemView.findViewById(R.id.text_view1);
                mTv.setOnClickListener(this);
                mDel= itemView.findViewById(R.id.del_button);
                mDel.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.text_view1:
                        int clickPosition = mRecyclerView.getChildAdapterPosition(itemView);
                        String nameInfo = data.get(clickPosition).getRoom().toString();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor cursor = db.rawQuery("select * from bill where room=?",new String[]{nameInfo});
                        while (cursor.moveToNext()) {
                            ss = cursor.getString(cursor.getColumnIndex("room"));
                            s2 = cursor.getString(cursor.getColumnIndex("time"));
                            s3 = cursor.getString(cursor.getColumnIndex("total"));


                        }
                        Toast.makeText(bill_activity.this,"房间号为："+ss+"\n时间："+s2+"\n该月房租金额为："+s3,Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.del_button:
                        int clickPosition1 = mRecyclerView.getChildAdapterPosition(itemView);
                        String deleteText = data.get(clickPosition1).toString();
                        SQLiteDatabase db1 = dbHelper.getReadableDatabase();
                        Cursor cursor1 = db1.rawQuery("select * from bill where room=?",new String[]{deleteText});
                        while (cursor1.moveToNext()){
                            mName = cursor1.getString(cursor1.getColumnIndex("name"));
                            if (deleteText.equals(mName)){
                               // deleteData();
                                db1.close();
                                break;
                            }
                        }
                        data.remove(clickPosition1);
                        Adapter.notifyDataSetChanged();
                        break;
                }

            }
        }
    }
//    public void deleteData(){
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.execSQL("delete from bill where name='"+ mName+"'" );
//    }

}

