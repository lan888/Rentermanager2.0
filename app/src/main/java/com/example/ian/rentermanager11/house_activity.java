package com.example.ian.rentermanager11;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.xw.repo.refresh.PullListView;
import com.xw.repo.refresh.PullToRefreshLayout;
import com.xw.repo.refresh.ResourceConfig;

import java.util.ArrayList;
import java.util.List;

import static com.example.ian.rentermanager11.R.id.pullToRefreshLayout;
import static com.example.ian.rentermanager11.R.layout.house;

/**
 * Created by Ian on 2017/8/12 0012.
 */

public class house_activity extends AppCompatActivity implements
        PullToRefreshLayout.OnRefreshListener{
    private PullToRefreshLayout mRefreshLayout;
    private PullListView mPullListView;

    private List<String> mStrings;
    private ListAdapter mAdapter;

    private myDatabaseHelper dbHelper;
    String pi = null;
    String po =null;
    String pp =null;
    String pa = null;
    String mRoom = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        dbHelper = myDatabaseHelper.getInstance(this);

        mRefreshLayout = (PullToRefreshLayout) findViewById(pullToRefreshLayout);
        mPullListView = (PullListView) findViewById(R.id.pullListView);
        mStrings = new ArrayList<>();

        mRefreshLayout.setOnRefreshListener(this);

        mAdapter = new com.example.ian.rentermanager11.ListAdapter(this,mStrings);
        mPullListView.setAdapter(mAdapter);
        updateListData();

        mPullListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mPullListView.getCount()!=1){
                    String roomInfo =mStrings.get(position).toString();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor = db.rawQuery("select * from house where room=?",new String[]{roomInfo});
                    while (cursor.moveToNext()) {
                        pi = cursor.getString(cursor.getColumnIndex("room"));
                        po = cursor.getString(cursor.getColumnIndex("type"));
                        pp = cursor.getString(cursor.getColumnIndex("area"));
                        pa = cursor.getString(cursor.getColumnIndex("status"));
                    }
                    Toast.makeText(house_activity.this,"房间号为："+pi+"\n户型："+po+"\n面积为："+pp+"㎡"+"\n是否已出租："+pa,Toast.LENGTH_SHORT).show();
                }
            }
        });
        mAdapter.setOnItemDeleteListener(new ListAdapter.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int position) {
                String deleteText = mStrings.get(position).toString();
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from house where room=?",new String[]{deleteText});
                while (cursor.moveToNext()){
                     mRoom = cursor.getString(cursor.getColumnIndex("room"));
                    if (deleteText.equals(mRoom)){
                        deleteData();
                        break;
                    }
                }
                mStrings.remove(position);
                mAdapter.notifyDataSetChanged();

            }
        });

    }
    public void Data(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select room from house",null);
        while (cursor.moveToNext()) {
            pi = cursor.getString(cursor.getColumnIndex("room"));
            mStrings.add(pi);
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.refreshFinish(true);
                if (mStrings != null) {
                    mStrings.clear();
                }
                updateListData();


            }
        },2000);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.loadMoreFinish(true);
                if (mStrings != null) {
                    mStrings.clear();
                }
                updateListData();
            }
        },2000);
    }

    private void updateListData(){
        if (mAdapter == null){
            mAdapter = new com.example.ian.rentermanager11.ListAdapter(this,mStrings);
            mPullListView.setAdapter(mAdapter);
        }else {
            Data();
            mAdapter.updateListView(mStrings);
            }


        }

    public void deleteData(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL("delete from house where room="+ mRoom );
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        setTitle("房源列表");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                AlertDialog.Builder builder = new AlertDialog.Builder(house_activity.this);
                LayoutInflater factory = LayoutInflater.from(house_activity.this);
                final View textEntryView = factory.inflate(house,null);
                builder.setTitle("添加房源");
                builder.setView(textEntryView);

                final EditText house_num =  textEntryView.findViewById(R.id.house_num);
                final EditText house_type = textEntryView.findViewById(R.id.house_type);
                final EditText house_area =  textEntryView.findViewById(R.id.house_area);

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String house_numInfo = house_num.getText().toString();
                        String house_typeInfo = house_type.getText().toString();
                        String house_areaInfo = house_area.getText().toString();
                        String house_status = "否";
                        String p = null;
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                            Cursor cursor = db.rawQuery("select * from house where room=?",new String[]{house_numInfo});
                            if (cursor.moveToNext()) {
                                p = cursor.getString(cursor.getColumnIndex("room"));

                        }
                        if (house_numInfo.matches("[0-9]{3}")){
                            if (house_areaInfo.matches("[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0{4,5}")){
                                if (house_typeInfo.matches("[1-9]{4}")){//[\u4e00-\u9fa5]{4}
                                    if (house_numInfo.equals(p)){
                                        Toast.makeText(house_activity.this,"房源重复，请重写",Toast.LENGTH_SHORT).show();
                                    }else {
                                        db.execSQL("insert into house(room,type,area,status)values(?,?,?,?)",
                                                new String[]{house_numInfo, house_typeInfo,house_areaInfo,house_status});
                                        Toast.makeText(house_activity.this,"已添加房源，请下滑刷新一下",Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(house_activity.this, "户型应输入4位汉字，例如一房一厅", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(house_activity.this, "房间面积应填写4到5位浮点数，例如100.00", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(house_activity.this, "房号为3位纯数字", Toast.LENGTH_SHORT).show();
                        }



                    }

                });
                builder.create().show();

                break;
            case R.id.delete:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(house_activity.this);
                builder1.setMessage("你确定要清除所有房源数据吗？");

                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mStrings != null){
                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                            db.execSQL("delete from house " );
                            db.close();
                            mStrings.clear();
                            mAdapter.updateListView(mStrings);
                        }
                    }
                });
                builder1.create().show();

                break;
            case R.id.settings:
                ResourceConfig resourceConfig = new ResourceConfig() {
                    @Override
                    public int[] configImageResIds() {
                        return new int[]{R.mipmap.ic_arrow, R.mipmap.ic_ok,
                                R.mipmap.ic_failed, R.mipmap.ic_ok, R.mipmap.ic_failed};
                    }

                    @Override
                    public int[] configTextResIds() {
                        return new int[]{R.string.pull_to_refresh, R.string.release_to_refresh, R.string.refreshing,
                                R.string.refresh_succeeded, R.string.refresh_failed, R.string.pull_up_to_load,
                                R.string.release_to_load, R.string.loading, R.string.load_succeeded,
                                R.string.load_failed};
                    }
                };
                mRefreshLayout.setShowRefreshResultEnable(true);
                mRefreshLayout.setResourceConfig(resourceConfig);
                break;
            default:
        }
        return true;
    }
}
