package com.example.ian.rentermanager2;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
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

import com.example.ian.rentermanager2.entity.Renters;
import com.example.ian.rentermanager2.entity.Rooms;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Ian on 2017/8/17 0017.
 */

public class renter_activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<String> data = new ArrayList<String>();
    private int lastVisibleItem;
    private MyAdapter Adapter;
    private List<Renters> rawQuery;

    Renters r1 = new Renters();
    Rooms r2 = new Rooms();
    String s = null;
    String ss = null;
    String s2 = null;
    String s3 = null;
    String s4 = null;
    String s5 = null;
    String s6 = null;
    String s7 = null;
    String mName ;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renter_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(renter_activity.this,NewAdminActivity.class);
                startActivity(intent);
            }
        });



        initData();
        initView();


    }


    private void initView() {
        final LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
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
        BmobQuery<Renters> query = new BmobQuery<Renters>();
        query.findObjects(new FindListener<Renters>(){
            @Override
            public void done(List<Renters> list, BmobException e) {
                if (e == null) {
                    for (Renters r : list) {
                        s = r.getName();
                        data.add(s);
                    }
                    rawQuery=list;
                    Adapter = new MyAdapter(data);
                    mRecyclerView.setAdapter(Adapter);
                }else {
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

    }

   // public void onItemClick(View view){

   // }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        setTitle("租户信息");
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
                        final String renter_nameInfo = renter_name.getText().toString();
                        final String renter_roomInfo = renter_room.getText().toString();
                        final String renter_sexInfo = (String)renter_sex.getSelectedItem();
                        final String renter_companyInfo = renter_company.getText().toString();
                        final String renter_phoneInfo = renter_phone.getText().toString();
                        final String renter_idCardInfo = renter_idCard.getText().toString();
                        final String renter_memberInfo = renter_member.getText().toString();


                        if (!renter_nameInfo.equals("")){
                            if (renter_roomInfo.matches("[0-9]{3}")){
                                if (!renter_companyInfo.equals("")){
                                    if (renter_phoneInfo.matches("[1][358]\\d{9}")){
                                        if (renter_idCardInfo.matches("(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)")){
                                            if (renter_memberInfo.matches("[0-9]")){
                                                String bql = "select name from Renters where name = ?";
                                                new BmobQuery<Renters>().doSQLQuery(bql, new SQLQueryListener<Renters>() {
                                                    @Override
                                                    public void done(BmobQueryResult bmobQueryResult, BmobException e) {
                                                        if (e==null){
                                                            List<Renters> list = (List<Renters>) bmobQueryResult.getResults();
                                                            if (list!=null&&list.size()>0){
                                                                for (Renters r : list){
                                                                    mName = r.getName();

                                                                }
                                                                Log.e("name","查询成功"+mName);
                                                            }
                                                        }else {
                                                            Log.e("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                                                        }
                                                        if (renter_nameInfo.equals(mName)){
                                                            Toast.makeText(renter_activity.this, "租户重复，请重新填写", Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            r1.setName(renter_nameInfo);
                                                            r1.setRoom(renter_roomInfo);
                                                            r1.setSex(renter_sexInfo);
                                                            r1.setPhone(renter_phoneInfo);
                                                            r1.setMember(renter_memberInfo);
                                                            r1.setIdcard(renter_idCardInfo);
                                                            r1.setCompany(renter_companyInfo);
                                                            r1.save(new SaveListener<String>() {
                                                                @Override
                                                                public void done(String s, BmobException e) {
                                                                    if(e==null){

                                                                        Log.e("success","添加数据成功，返回objectId为："+s) ;
                                                                    }else{
                                                                        Log.e("fail", "创建数据失败：" + e.getMessage());
                                                                    }
                                                                }
                                                            });

                                                            String bql = "select getObjectId from Rooms where room = ?";
                                                            new BmobQuery<Rooms>().doSQLQuery(bql, new SQLQueryListener<Rooms>() {
                                                                        @Override
                                                                        public void done(BmobQueryResult bmobQueryResult, BmobException e) {
                                                                            if (e == null) {
                                                                                List<Rooms> list = (List<Rooms>) bmobQueryResult.getResults();
                                                                                if (list != null && list.size() > 0) {
                                                                                    for (Rooms r : list) {
                                                                                        id = r.getObjectId();

                                                                                    }
                                                                                    r2.setStatus("已出租");
                                                                                    r2.update(id,new UpdateListener() {
                                                                                        @Override
                                                                                        public void done(BmobException e) {
                                                                                            if(e==null){
                                                                                                Log.e("success","更新成功:"+r2.getUpdatedAt());
                                                                                            }else{
                                                                                                Log.e("succes","更新失败：" + e.getMessage());
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                    Log.e("name", "查询成功" + id);
                                                                                }
                                                                            } else {
                                                                                Log.e("smile", "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                                                                            }
                                                                        }
                                                                        },renter_roomInfo);


                                                            Toast.makeText(renter_activity.this, "已添加租户信息，请下滑刷新一下", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }

                                                },renter_nameInfo);


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
                             deleteData(rawQuery);
                            data.clear();
                            Adapter.notifyDataSetChanged();

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
                                    Toast.makeText(renter_activity.this,"租户姓名："+ss+"\n租户房间号："+s2+"\n租户性别："
                                                    +s3+"\n租户工作单位："+s4+"\n手机号码："+ s5+"\n租户身份证号："+s6+"\n租户人数："+s7,
                                            Toast.LENGTH_SHORT).show();
                                }else {
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }

                        });



                        break;
                    case R.id.del_button:
                        int clickPosition1 = mRecyclerView.getChildAdapterPosition(itemView);
                        String deleteText = rawQuery.get(clickPosition1).getObjectId().toString();
                        r1.setObjectId(deleteText);
                        r1.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Log.e("success","删除成功:"+r1.getRoom());
                                }else{
                                    Log.e("fail","删除失败：" + e.getMessage());
                                }
                            }
                        });
                        data.remove(clickPosition1);
                        Adapter.notifyDataSetChanged();
                        break;
                }

            }
        }
    }
    public void deleteData(List<Renters> list){
        for (Renters r :list){
             String id=r.getObjectId();
            r1.setObjectId(id);
            r1.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        Log.e("success","删除成功:");
                    }else{
                        Log.e("fail","删除失败：" + e.getMessage());
                    }
                }
            });

        }
        }


}
