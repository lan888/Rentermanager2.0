package com.example.ian.rentermanager2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ian.rentermanager2.entity.Bill;
import com.example.ian.rentermanager2.widget.OnValueChangeListener;
import com.example.ian.rentermanager2.widget.StickyHeaderListView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by Ian on 2017/10/1 0001.
 */

public class bill_activity extends AppCompatActivity {

    final  Bill b1= new Bill() ;
    private StickyHeaderListView mListView;
    private SectionAdapter mAdapter;
    private CheckBox mCheckAll;
    private List<Bill> rows = new ArrayList<>();
    private boolean isOnLoadMore = false;
    private int count = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(bill_activity.this,NewAdminActivity.class);
                startActivity(intent);
            }
        });

        initListView();
        initChechBox();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mListView.setAdapter(mAdapter = new SectionAdapter(bill_activity.this, rows));
            mAdapter.setOnValueChangedListener(new OnValueChangeListener() {
                @Override
                public void onChange(int totalCount, double totalAmount, boolean isCheckAll) {
                    BigDecimal bd = new BigDecimal(totalAmount).setScale(2, RoundingMode.UP);
                    ((TextView) findViewById(R.id.tv_desc)).setText(totalCount + "个房间，共" + bd.doubleValue() + "元");
                    // 防止调用onCheckedChanged
                    mCheckAll.setOnCheckedChangeListener(null);
                    mCheckAll.setChecked(isCheckAll);
                    mCheckAll.setOnCheckedChangeListener(onCheckedChangeListener);
                }
            });
        }
    };

    private void initListView() {
        mListView = (StickyHeaderListView) findViewById(R.id.lv);
        BmobQuery<Bill> query = new BmobQuery<Bill>();
        query.findObjects(new FindListener<Bill>(){
            @Override
            public void done(List<Bill> list, BmobException e) {
                if (e == null){
                    for (Bill bill : list){
                        rows.add(bill);
                    }
                    mHandler.sendEmptyMessage(0);
                }
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public synchronized void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if (!isOnLoadMore) {

                                if(count == 0){
                                    Toast.makeText(bill_activity.this, "没有更多数据啦~", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                count++;

                                isOnLoadMore = true;

                                mAdapter.addData(rows);

                                isOnLoadMore = false;
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initChechBox() {
        mCheckAll = (CheckBox) findViewById(R.id.cb_check_all);
        mCheckAll.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mAdapter.setCheckAll(isChecked);
        }
    };

    public class SectionAdapter extends StickyHeaderAdapter {

        private Context mContext;
        private OnValueChangeListener listener;

        private List<Bill> entityRows;

        private LinkedHashMap<String, List<Bill>> map = new LinkedHashMap<String, List<Bill>>();


        public SectionAdapter(Context context, List<Bill> entityRows) {
            this.mContext = context;
            this.entityRows = entityRows;

            addData(entityRows);
        }

        /**
         * 添加数据，并进行分类
         *
         * @param list
         */
        public void addData(List<Bill> list) {
//            BmobQuery<Bill> query = new BmobQuery<Bill>();
//            query.findObjects(new FindListener<Bill>() {
//                @Override
//                public void done(List<Bill> list, BmobException e) {
//                    if (e == null) {
            for (Bill row : list) {
                String time = row.getCreatedAt();
                String head = time.substring(0,7); // time
                if (map.get(head) == null) {
                    List<Bill> newRows = new ArrayList<>();
                    newRows.add(row);
                    map.put(head, newRows);
                } else {
                    List<Bill> newRows = map.get(head);
                    newRows.add(row);
                }
            }
//                    } else {
//                        Log.i("bmob", "失败" + e.getMessage() + "," + e.getErrorCode());
//                    }
//                }
//            });
            updateValue();
            notifyDataSetChanged();
        }

        public void setCheckAll(boolean checkAll) {
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, List<Bill>> entry = (Map.Entry<String, List<Bill>>) iter.next();
                String key = entry.getKey();
                List<Bill> val = entry.getValue();

                for (Bill row : val) {
                    row.isChecked = checkAll;
                }
            }

            updateValue();

            notifyDataSetChanged();
        }

        public void setOnValueChangedListener(OnValueChangeListener listener) {
            this.listener = listener;
        }

        private void updateValue() {
            if (listener == null)
                return;

            int count = 0;
            double amount = 0;
            boolean isCheckAll = true;

            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, List<Bill>> entry = (Map.Entry<String, List<Bill>>) iter.next();
                String key = entry.getKey();
                List<Bill> val = entry.getValue();

                for (Bill row : val) {
                    if (row.isChecked) {
                        count++;
                        amount += row.getBill();
                    } else {
                        isCheckAll = false;
                    }
                }
            }

            listener.onChange(count, amount, isCheckAll);
        }

        @Override
        public int sectionCounts() {
            return map.keySet().toArray().length;
        }

        @Override
        public int rowCounts(int section) {
            if (section < 0)
                return 0;

            Object[] key = map.keySet().toArray();


            return map.get(key[section]).size();
        }

        @Override
        public View getRowView(int section, int row, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_row, null);

            Object[] keys = map.keySet().toArray();
            String key = (String) keys[section];
            final Bill item = map.get(key).get(row);
            ((TextView) view.findViewById(R.id.time)).setText(item.getCreatedAt());
            ((TextView) view.findViewById(R.id.src)).setText(item.getRoom());
            ((TextView) view.findViewById(R.id.dest)).setText(item.getStatus());
            ((TextView) view.findViewById(R.id.amount)).setText(item.getBill() + "");
            final CheckBox checkBox = ((CheckBox) view.findViewById(R.id.cb));
            Button del = (Button) findViewById(R.id.del);
            Button chk = (Button) findViewById(R.id.check);
            chk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    b1.setStatus("已缴费");
                    b1.update(item.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Log.e("success","更新成功:"+b1.getUpdatedAt());
                            }else{
                                Log.e("succes","更新失败：" + e.getMessage());
                            }
                        }
                    });

                }
            });
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(bill_activity.this);
                    builder.setMessage("确定要删除选定数据吗？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            b1.setObjectId(item.getObjectId());
                            b1.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Log.e("success","删除成功:"+b1.getRoom());
                                    }else{
                                        Log.e("fail","删除失败：" + e.getMessage());
                                    }
                                }
                            });
                            Intent intent = new Intent(bill_activity.this,SuccessDelActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.create().show();

                }
            });
            checkBox.setChecked(item.isChecked);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.isChecked = isChecked;
                    updateValue();
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.setChecked(!checkBox.isChecked());
                }
            });

            return view;
        }

        @Override
        public Object getRowItem(int section, int row) {
            if (section < 0)
                return null;

            Object[] key = map.keySet().toArray();

            return map.get((String) key[section]).get(row);
        }

        @Override
        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_header, null);

            Object[] keys = map.keySet().toArray();

            String key = (String) keys[section];

            ((TextView) view.findViewById(R.id.month)).setText(key.split("-")[1]+"月");

            return view;
        }

        @Override
        public boolean hasSectionHeaderView(int section) {
            return true;
        }
    }

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

                final EditText num = (EditText) textEntryView.findViewById(R.id.num);
                final EditText bill = (EditText) textEntryView.findViewById(R.id.bill);
                final EditText waterBill = (EditText) textEntryView.findViewById(R.id.waterBill);
                final EditText electricityBill = (EditText) textEntryView.findViewById(R.id.electricityBill);


                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String numInfo = num.getText().toString();
                        if (numInfo.equals("")) {
                            Toast.makeText(bill_activity.this, "房间号不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            double billInfo = Double.parseDouble(bill.getText().toString());
                            double waterBillInfo = Double.parseDouble(waterBill.getText().toString());
                            double electricityBillInfo = Double.parseDouble(electricityBill.getText().toString());

                            if (!numInfo.equals("")) {
                                if (numInfo.matches("[0-9]{3}")) {
                                    if (bill.getText().toString().matches("(([1-9][0-9]*)|(([0]\\.\\d{0,2}|[1-9][0-9]*\\.\\d{0,2})))")) {
                                        if (waterBill.getText().toString().matches("(([1-9][0-9]*)|(([0]\\.\\d{0,2}|[1-9][0-9]*\\.\\d{0,2})))")) {
                                            if (electricityBill.getText().toString().matches("(([1-9][0-9]*)|(([0]\\.\\d{0,2}|[1-9][0-9]*\\.\\d{0,2})))")) {
                                                b1.setRoom(numInfo);
                                                b1.setBill(billInfo + waterBillInfo + electricityBillInfo);
                                                b1.setWaterBill(waterBillInfo);
                                                b1.setElectricityBill(electricityBillInfo);
                                                b1.setRoomBill(billInfo);
                                                b1.setStatus("未缴费");
                                                b1.save(new SaveListener<String>() {
                                                    @Override
                                                    public void done(String s, BmobException e) {
                                                        if (e == null) {
                                                            Log.e("success", "添加数据成功，返回objectId为：" + s);
                                                        } else {
                                                            Log.e("fail", "创建数据失败：" + e.getMessage());
                                                        }
                                                    }
                                                });

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
                            } else {
                                Toast.makeText(bill_activity.this, "房号不能为空", Toast.LENGTH_SHORT).show();
                            }
                        }

                        Intent intent = new Intent(bill_activity.this,SuccessAddActivity.class);
                        startActivity(intent);

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

                    }
                });
                builder1.create().show();

                break;
            case R.id.settings:


        }
        return true;
    }

}

