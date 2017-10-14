package com.example.ian.rentermanager2;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.SparseBooleanArray;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    String s1 = null;
    String s6 = null;
    String s7 = null;
    String sss = null;
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
                Intent intent = new Intent(bill_activity.this,new_admin_activity.class);
                startActivity(intent);
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
            sss = cursor.getString(cursor.getColumnIndex("month"));
            data.add(new Bill(s,ss,sss));
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
                        if (numInfo.equals("")){
                            Toast.makeText(bill_activity.this,"房间号不能为空",Toast.LENGTH_SHORT).show();
                        }else {

                            String billInfo = bill.getText().toString();
                            String waterBillInfo = waterBill.getText().toString();
                            String electricityBillInfo = electricityBill.getText().toString();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            int month = Calendar.getInstance().get(Calendar.MONTH)+1;
                            String m =month+"月份";
                            String t = format.format(new Date());
                            double to = Double.parseDouble(billInfo) + Double.parseDouble(waterBillInfo) + Double.parseDouble(electricityBillInfo);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            Cursor cursor = db.rawQuery("select * from bill where room=?", new String[]{numInfo});
                            if (cursor.moveToNext()) {
                                s1 = cursor.getString(cursor.getColumnIndex("room"));

                            }

                            if (numInfo.matches("[0-9]{3}")) {
                                if (billInfo.matches("(([1-9][0-9]*)|(([0]\\.\\d{0,2}|[1-9][0-9]*\\.\\d{0,2})))")) {
                                    if (waterBillInfo.matches("(([1-9][0-9]*)|(([0]\\.\\d{0,2}|[1-9][0-9]*\\.\\d{0,2})))")) {
                                        if (electricityBillInfo.matches("(([1-9][0-9]*)|(([0]\\.\\d{0,2}|[1-9][0-9]*\\.\\d{0,2})))")) {
                                            db.execSQL("insert into bill(room,bill,waterBill,electricityBill,month,time,total)values(?,?,?,?,?,?,?)",
                                                    new String[]{numInfo, billInfo, waterBillInfo, electricityBillInfo,m, t, String.valueOf(to)});
                                        } else {
                                            Toast.makeText(bill_activity.this, "电费数额只能带两位小数", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(bill_activity.this, "水费数额只能带两位小数", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(bill_activity.this, "房租数额只能带两位小数", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(bill_activity.this, "房号为3位纯数字", Toast.LENGTH_SHORT).show();

                            }
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
                            db.execSQL("delete from bill " );
                            db.close();
                            data.clear();
                            Adapter.notifyDataSetChanged();
                        }
                    }
                });
                builder1.create().show();

                break;
            case R.id.settings:
                break;

        }
        return true;
    }

    public class MyAdapter extends SectionedRecyclerViewAdapter<HeaderHolder,MyAdapter.DescHolder,RecyclerView.ViewHolder>{
        public ArrayList<Bill> billList;

        private LayoutInflater mInflater;
        private SparseBooleanArray mBooleanMap;

        public MyAdapter(ArrayList<Bill> billList) {
            this.billList = billList;
            mInflater = LayoutInflater.from(getBaseContext());
            mBooleanMap = new SparseBooleanArray();
        }

        @Override
        protected int getSectionCount() {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ArrayList<String>months = new ArrayList<String>();
            Cursor cursor = db.rawQuery("select distinct month from bill", null);
            while (cursor.moveToNext()){
                s6 = cursor.getString(cursor.getColumnIndex("month"));
                months.add(s6);
            }



            return BillUtils.isEmpty(billList) ? 0 : months.size();
        }
        @Override
        protected int getItemCountForSection(int section) {
            int count = billList.size();
            if (count >= 0 && !mBooleanMap.get(section)) {
                count = 0;
            }
            return BillUtils.isEmpty(billList) ? 0 : count;
        }

        //是否有footer布局
        @Override
        protected boolean hasFooterInSection(int section) {
            return false;
        }

        @Override
        protected HeaderHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
            return new HeaderHolder(mInflater.inflate(R.layout.bill_title_item, parent, false));
        }

        @Override
        protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        protected DescHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
            return new DescHolder(mInflater.inflate(R.layout.item_renter,parent, false));
        }

        @Override
        protected void onBindSectionHeaderViewHolder(final HeaderHolder holder, final int section) {
            holder.openView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isOpen = mBooleanMap.get(section);
                    String text = isOpen ? "展开" : "关闭";
                    mBooleanMap.put(section, !isOpen);
                    holder.openView.setText(text);
                    notifyDataSetChanged();
                }
            });
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ArrayList<String>months = new ArrayList<String>();
            Cursor cursor = db.rawQuery("select distinct month from bill", null);
            while (cursor.moveToNext()){
                s6 = cursor.getString(cursor.getColumnIndex("month"));
                months.add(s6);
            }
            holder.titleView.setText(months.get(section));
            holder.openView.setText(mBooleanMap.get(section) ? "关闭" : "展开");

        }


        @Override
        protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

        }

        @Override
        protected void onBindItemViewHolder(DescHolder holder, int section, int position) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ArrayList<String>months = new ArrayList<String>();
            ArrayList<String>rooms = new ArrayList<String>();
            Map<String,ArrayList> bills= new HashMap<>();
            bills.put(s6,rooms);
            Cursor cursor = db.rawQuery("select distinct month from bill", null);
            while (cursor.moveToNext()){
                s6 = cursor.getString(cursor.getColumnIndex("month"));
                months.add(s6);
            }
            Cursor cursor1 = db.rawQuery("select * from bill where month=?", new String[]{s6});
            while (cursor1.moveToNext()){
                s7 = cursor1.getString(cursor1.getColumnIndex("room"));
                rooms.add(s7);
            }
            String key = (String)bills.keySet().iterator().next();
            holder.mTv.setText((String)bills.get(key).get(section));

        }

        class DescHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView mTv;
            Button mDel;


            public DescHolder(View itemView){
                super(itemView);
                mTv = itemView.findViewById(R.id.text_view1);
                mTv.setOnClickListener(this);
                mDel= itemView.findViewById(R.id.del_button);
                mDel.setOnClickListener(this);
            }

            @Override
            public void onClick(View view ) {
                switch (view.getId()){
                    case R.id.text_view1:
                        int clickPosition =Adapter.getItemPosition(getAdapterPosition());
                        String nameInfo = billList.get(clickPosition).getRoom().toString();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor cursor = db.rawQuery("select * from bill where room=?",new String[]{nameInfo});
                        while (cursor.moveToNext()) {
                            ss = cursor.getString(cursor.getColumnIndex("room"));
                            s2 = cursor.getString(cursor.getColumnIndex("time"));
                            s3 = cursor.getString(cursor.getColumnIndex("total"));
                            s4 = cursor.getString(cursor.getColumnIndex("waterBill"));
                            s5 = cursor.getString(cursor.getColumnIndex("electricityBill"));


                        }
                        Toast.makeText(bill_activity.this,"房间号为："+ss+"\n时间："+s2+"\n上月水费金额为："+s4+"\n上月电费金额为："+s5+"\n上月房租总金额为："+s3,Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.del_button:
                        int clickPosition1 = Adapter.getItemPosition(getAdapterPosition());
                        String deleteText =  billList.get(clickPosition1).getRoom().toString();
                        SQLiteDatabase db1 = dbHelper.getReadableDatabase();
                        Cursor cursor1 = db1.rawQuery("select * from bill where room=?",new String[]{deleteText});
                        while (cursor1.moveToNext()){
                            mName = cursor1.getString(cursor1.getColumnIndex("room"));
                            if (deleteText.equals(mName)){
                                deleteData();
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
    public void deleteData(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from bill where room='"+ mName+"'" );
    }

}

