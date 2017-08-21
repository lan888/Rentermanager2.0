package com.example.ian.rentermanager11;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ian on 2017/8/17 0017.
 */

public class renter_activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<String> data = new ArrayList<String>();
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
        Cursor cursor = db.rawQuery("select name from renter", null);
        while (cursor.moveToNext()) {
            s = cursor.getString(cursor.getColumnIndex("name"));
            data.add(s);
        }
    }

   // public void onItemClick(View view){

   // }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        setTitle("租户信息列表");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                AlertDialog.Builder builder = new AlertDialog.Builder(renter_activity.this);
                LayoutInflater factory = LayoutInflater.from(renter_activity.this);
                final View textEntryView = factory.inflate(R.layout.renter,null);
                builder.setTitle("添加租户");
                builder.setView(textEntryView);

                final EditText renter_name = textEntryView.findViewById(R.id.renter_name);
                final EditText renter_room = textEntryView.findViewById(R.id.renter_room);
                final Spinner renter_sex = textEntryView.findViewById(R.id.renter_sex);
                final EditText renter_company = textEntryView.findViewById(R.id.renter_company);
                final EditText renter_phone = textEntryView.findViewById(R.id.renter_phone);
                final EditText renter_idCard = textEntryView.findViewById(R.id.renter_idcard);
                final EditText renter_member = textEntryView.findViewById(R.id.renter_member);

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String renter_nameInfo = renter_name.getText().toString();
                        String renter_roomInfo = renter_room.getText().toString();
                        String renter_sexInfo = (String)renter_sex.getSelectedItem();
                        String renter_companyInfo = renter_company.getText().toString();
                        String renter_phoneInfo = renter_phone.getText().toString();
                        String renter_idCardInfo = renter_idCard.getText().toString();
                        String renter_memberInfo = renter_member.getText().toString();
                        String s1 = null;
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor cursor = db.rawQuery("select * from renter where name=?",new String[]{renter_nameInfo});
                        if (cursor.moveToNext()) {
                            s1 = cursor.getString(cursor.getColumnIndex("name"));

                        }
                        if (!renter_nameInfo.equals("")){
                            if (renter_roomInfo.matches("[0-9]{3}")){
                                if (!renter_companyInfo.equals("")){
                                    if (renter_phoneInfo.matches("[1][358]\\d{9}")){
                                        if (renter_idCardInfo.matches("(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)")){
                                            if (renter_memberInfo.matches("[0-9]")){
                                                if (renter_nameInfo.equals(s1)){
                                                    Toast.makeText(renter_activity.this, "租户重复，请重新填写", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    db.execSQL("insert into renter(name,room,sex,company,phone,idcard,member)values(?,?,?,?,?,?,?)",
                                                            new String[]{renter_nameInfo, renter_roomInfo,renter_sexInfo, renter_companyInfo, renter_phoneInfo,renter_idCardInfo,renter_memberInfo});
                                                    Toast.makeText(renter_activity.this, "已添加租户信息，请下滑刷新一下", Toast.LENGTH_SHORT).show();
                                                    db.execSQL("update house set status='是' where room=?",new String[]{renter_roomInfo});
                                                }
                                            }else {
                                                Toast.makeText(renter_activity.this, "请用阿拉伯数字填写准确租住人数", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(renter_activity.this, "请填写合法的身份证号码", Toast.LENGTH_SHORT).show();
                                        }

                                    }else {
                                        Toast.makeText(renter_activity.this, "请填写合法的手机号码", Toast.LENGTH_SHORT).show();
                                    }

                                }else {
                                    Toast.makeText(renter_activity.this, "租户工作单位不能为空", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(renter_activity.this, "租户房间号为三位纯数字", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(renter_activity.this, "租户姓名不能为空", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                builder.create().show();
                break;
            case R.id.delete:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(renter_activity.this);
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

        private ArrayList<String> mData;

        public MyAdapter(ArrayList<String> data){
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
            holder.mTv.setText(mData.get(position));
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
                        String nameInfo = data.get(clickPosition).toString();
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
                        Toast.makeText(renter_activity.this,"租户姓名："+ss+"\n租户房间号："+s2+"\n租户性别："+s3+"\n租户工作单位："+s4+"\n手机号码："+s5+"\n租户身份证号："+s6+"\n租户人数："+s7,Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.del_button:
                        int clickPosition1 = mRecyclerView.getChildAdapterPosition(itemView);
                        String deleteText = data.get(clickPosition1).toString();
                        SQLiteDatabase db1 = dbHelper.getReadableDatabase();
                        Cursor cursor1 = db1.rawQuery("select * from renter where name=?",new String[]{deleteText});
                        while (cursor1.moveToNext()){
                             mName = cursor1.getString(cursor1.getColumnIndex("name"));
                            if (deleteText.equals(mName)){
                                deleteData();
                                db1.execSQL("update house set status='否' where room=?",new String[]{s2});
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
        db.execSQL("delete from renter where name='"+ mName+"'" );
    }

}
