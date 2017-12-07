package com.example.ian.rentermanager2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ian.rentermanager2.entity.Renters;
import com.example.ian.rentermanager2.entity.Rooms;
import com.xw.repo.refresh.PullListView;
import com.xw.repo.refresh.PullToRefreshLayout;
import com.xw.repo.refresh.ResourceConfig;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.example.ian.rentermanager2.R.id.pullToRefreshLayout;
import static com.example.ian.rentermanager2.R.layout.house;

/**
 * Created by Ian on 2017/8/12 0012.
 */

public class HouseActivity extends AppCompatActivity implements
        PullToRefreshLayout.OnRefreshListener{
    private PullToRefreshLayout mRefreshLayout;
    private PullListView mPullListView;

    private List<String> mStrings;
    private ListAdapter mAdapter;
    private List<Rooms> rawQuery;

    String pi = null;
    String po =null;
    String pp =null;
    String pa = null;
    String mRoom = null;
    Rooms r2 = new Rooms();

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HouseActivity.this,NewAdminActivity.class);
                startActivity(intent);
            }
        });


        mRefreshLayout = (PullToRefreshLayout) findViewById(pullToRefreshLayout);
        mPullListView = (PullListView) findViewById(R.id.pullListView);
        mStrings = new ArrayList<>();

        mRefreshLayout.setOnRefreshListener(this);

        mAdapter = new com.example.ian.rentermanager2.ListAdapter(this,mStrings);
        mPullListView.setAdapter(mAdapter);
        updateListData();

        mPullListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mAdapter.mDataList.size()>0){
                    String roomInfo =mStrings.get(position).toString();
                    BmobQuery query = new BmobQuery<Rooms>();
                    query.addWhereEqualTo("room",roomInfo);
                    query.setLimit(1);
                    query.findObjects(new FindListener<Rooms>() {
                        @Override
                        public void done(List<Rooms> list, BmobException e) {
                            if (e == null){
                                Log.e("success","查询成功:"+list.size()+"条数据");
                                for (Rooms r : list){
                                    pi = r.getRoom();
                                    po = r.getType();
                                    pp = r.getArea();
                                    pa = r.getStatus();
                                }
                                Toast.makeText(HouseActivity.this,"房间号为："+pi+"\n户型："+po+"\n面积为："+pp+"㎡"+"\n是否已出租："+pa,Toast.LENGTH_SHORT).show();
                            }else {
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }


                    });



                }
            }
        });
        mAdapter.setOnItemDeleteListener(new ListAdapter.onItemDeleteListener() {
            @Override
            public void onDeleteClick(int position) {
                String deleteText = rawQuery.get(position).getObjectId();
                r2.setObjectId(deleteText);
                r2.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("success", "删除成功:" + r2.getRoom());
                        } else {
                            Log.e("fail", "删除失败：" + e.getMessage());
                        }
                    }
                });
                mStrings.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    public static void actionStart(Context context, String nameInfo ){
        Intent intent = new Intent(context,HouseActivity.class);
        intent.putExtra("i",nameInfo);
        context.startActivity(intent);
    }

    public void Data(){
        BmobQuery<Rooms> query = new BmobQuery<Rooms>();
        query.findObjects(new FindListener<Rooms>() {
            @Override
            public void done(List<Rooms> list, BmobException e) {
                if (e==null){
                    for (Rooms r : list){
                        pi = r.getRoom();
                        mStrings.add(pi);
                    }
                    rawQuery = list;
                    mAdapter.updateListView(mStrings);
                }
            }
        });

           ;

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
            mAdapter = new com.example.ian.rentermanager2.ListAdapter(this,mStrings);
            mPullListView.setAdapter(mAdapter);
        }else {
            Data();
            }
        mAdapter.notifyDataSetChanged();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(HouseActivity.this);
                LayoutInflater factory = LayoutInflater.from(HouseActivity.this);
                final View textEntryView = factory.inflate(house,null);
                builder.setTitle("添加房源");
                builder.setView(textEntryView);

                final EditText house_num =  textEntryView.findViewById(R.id.house_num);
                final Spinner house_type = textEntryView.findViewById(R.id.house_type);
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
                        String house_typeInfo = (String)house_type.getSelectedItem();
                        String  house_areaInfo = house_area.getText().toString();
                        String house_status = "否";
                        BmobQuery query = new BmobQuery<Renters>();
                        query.addWhereEqualTo("room",house_numInfo);
                        query.setLimit(1);
                        query.findObjects(new FindListener<Rooms>() {
                            @Override
                            public void done(List<Rooms> list, BmobException e) {
                                if(e==null) {
                                    for (Rooms r : list){
                                       mRoom = r.getRoom();
                                    }
                                    Log.e("success", "查询成功:" + list.size() + "条数据");
                                }else {
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }


                        });

                        if (house_numInfo.matches("[0-9]{3}")){
                            if (house_areaInfo.matches("[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0{4,5}")) {
                                // if (house_typeInfo.matches("[1-9]{4}")){//[\u4e00-\u9fa5]{4}
                                if (house_numInfo.equals(mRoom)) {
                                    Toast.makeText(HouseActivity.this, "房源重复，请重新填写", Toast.LENGTH_SHORT).show();
                                } else {
                                   r2.setRoom(house_numInfo);
                                   r2.setArea(house_areaInfo);
                                   r2.setStatus(house_status);
                                   r2.setType(house_typeInfo);
                                   r2.save(new SaveListener<String>() {
                                       @Override
                                       public void done(String s, BmobException e) {
                                           if(e==null){

                                               Log.e("success","添加数据成功，返回objectId为："+s) ;
                                           }else{
                                               Log.e("fail", "创建数据失败：" + e.getMessage());
                                           }
                                       }
                                   });

                                    Toast.makeText(HouseActivity.this, "已添加房源，请下滑刷新一下", Toast.LENGTH_SHORT).show();
                                //else {
                                  //  Toast.makeText(HouseActivity.this, "户型应输入4位汉字，例如一房一厅", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(HouseActivity.this, "房间面积应填写4到5位浮点数，例如100.00", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(HouseActivity.this, "房号为3位纯数字", Toast.LENGTH_SHORT).show();
                        }



                    }

                });
                builder.create().show();

                break;
            case R.id.delete:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(HouseActivity.this);
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
