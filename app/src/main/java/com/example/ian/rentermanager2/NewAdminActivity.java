package com.example.ian.rentermanager2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ian.rentermanager2.behavior.AppBarLayoutOverScrollViewBehavior;
import com.example.ian.rentermanager2.entity.Bill;
import com.example.ian.rentermanager2.entity.Renters;
import com.example.ian.rentermanager2.entity.Rooms;
import com.example.ian.rentermanager2.widget.CircleImageView;
import com.example.ian.rentermanager2.widget.NoScrollViewPager;
import com.example.ian.rentermanager2.widget.RoundProgressBar;
import com.example.mylibrary.CommonTabLayout;
import com.example.mylibrary.listener.CustomTabEntity;
import com.example.mylibrary.listener.OnTabSelectListener;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by Ian on 2017/8/24 0024.
 */

public class NewAdminActivity extends AppCompatActivity {

    private ImageView mZoomIv;
    private Toolbar mToolBar;
    private ViewGroup titleContainer;
    private AppBarLayout mAppBarLayout;
    private ViewGroup titleCenterLayout;
    private RoundProgressBar progressBar;
    private ImageView mSettingIv, mMsgIv,mNavIv;
    private TextView mMsgTv,nUser,mMcTv;
    private CircleImageView mAvater;
    private CommonTabLayout mTablayout;
    private NoScrollViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNav;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private List<Fragment> fragments;
    private int lastState = 1;
    private ArrayList<String> mData1 = new ArrayList<String>();
    private ArrayList<String> mData2 = new ArrayList<String>();
    private ArrayList<String> mData3 = new ArrayList<String>();
    private myDatabaseHelper dbHelper;

    public static final String action = "broadcast.action";

    int a;
    int b;
    int c;

    final  Bill b1= new Bill() ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo1);

        getDatas();



    }
    public void getDatas(){
        BmobQuery<Rooms> query = new BmobQuery<Rooms>();
        query.findObjects(new FindListener<Rooms>(){
            @Override
            public void done(List<Rooms> list, BmobException e) {
                if (e == null) {
                    for (Rooms r : list) {
                        String s = r.getRoom();
                        mData1.add(s);
                    }
                    Log.i("a","a:"+mData1.size());
                    a = mData1.size();

                    BmobQuery<Renters> query = new BmobQuery<Renters>();
                    query.findObjects(new FindListener<Renters>(){
                        @Override
                        public void done(List<Renters> list, BmobException e) {
                            if (e == null) {
                                for (Renters r : list) {
                                    String s = r.getName();
                                    mData2.add(s);
                                }
                                Log.i("b","b:"+mData2.size());
                                b = mData2.size();
                                BmobQuery<Bill> query = new BmobQuery<Bill>();
                                query.findObjects(new FindListener<Bill>(){
                                    @Override
                                    public void done(List<Bill> list, BmobException e) {
                                        if (e == null) {
                                            for (Bill r : list) {
                                                String s = r.getRoom();
                                                mData3.add(s);
                                            }
                                            Log.i("c","c:"+mData3.size());
                                            c = mData3.size();

                                            handler.sendEmptyMessage(0);
                                        }else {
                                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                        }
                                    }
                                });

                            }else {
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }else {
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            findId();
            initListener();
            initTab();
            initStatus();

            final View headerView = mNav.getHeaderView(0);
            nUser = headerView.findViewById(R.id.nav_user);
            final Intent intent = getIntent();
            String s1 = intent.getStringExtra("i");
            if (s1!=null){
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putString("name",s1);
                editor.apply();

            }
            SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
            String s2 = pref.getString("name","");
            nUser.setText(s2);



        }
    };

    public static void actionStart(Context context, String nameInfo ){
        Intent intent = new Intent(context,NewAdminActivity.class);
        intent.putExtra("i",nameInfo);
        context.startActivity(intent);
    }


    private void findId(){
        mZoomIv = (ImageView) findViewById(R.id.uc_zoomiv);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        titleContainer = (ViewGroup) findViewById(R.id.title_layout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        titleCenterLayout = (ViewGroup) findViewById(R.id.title_center_layout);
        progressBar = (RoundProgressBar) findViewById(R.id.uc_progressbar);
        mMsgTv = (TextView)findViewById(R.id.frag_uc_follow_tv);
        mMcTv = (TextView)findViewById(R.id.frag_uc_msg_tv);
        mSettingIv = (ImageView) findViewById(R.id.uc_setting_iv);
        mMsgIv = (ImageView) findViewById(R.id.uc_msg_iv);
        mAvater = (CircleImageView) findViewById(R.id.uc_avater);
        mTablayout = (CommonTabLayout) findViewById(R.id.uc_tablayout);
        mViewPager = (NoScrollViewPager) findViewById(R.id.uc_viewpager);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNav = (NavigationView) findViewById(R.id.nav_view);
        mNavIv=(ImageView) findViewById(R.id.uc_nav);


    }

    private void initTab(){

        fragments = getFragments();
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,getNames());

        mTablayout.setTabData(mTabEntities);
        mViewPager.setAdapter(myFragmentPagerAdapter);
        mToolBar.setNavigationIcon(R.mipmap.ic_menu);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        mMsgTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NewAdminActivity.this,"小样，赶紧交房租！哈哈哈",Toast.LENGTH_SHORT).show();
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                db.execSQL("delete from bill " );
            }
        });
        mMcTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewAdminActivity.this);
                final LayoutInflater factory = LayoutInflater.from(NewAdminActivity.this);
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
                        if (numInfo.equals("")) {
                            Toast.makeText(NewAdminActivity.this, "房间号不能为空", Toast.LENGTH_SHORT).show();
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
                                                b1.setBill(billInfo+waterBillInfo+electricityBillInfo);
                                                b1.setWaterBill(waterBillInfo);
                                                b1.setElectricityBill(electricityBillInfo);
                                                b1.setRoomBill(billInfo);
                                                b1.setStatus("未缴费");
                                                b1.save(new SaveListener<String>() {
                                                    @Override
                                                    public void done(String s, BmobException e) {
                                                        if(e==null){
                                                            Log.e("success","添加数据成功，返回objectId为："+s) ;
                                                        }else{
                                                            Log.e("fail", "创建数据失败：" + e.getMessage());
                                                        }
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(NewAdminActivity.this, "电费数额只能带两位小数", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(NewAdminActivity.this, "水费数额只能带两位小数", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(NewAdminActivity.this, "房租数额只能带两位小数", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(NewAdminActivity.this, "房号为3位纯数字", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(NewAdminActivity.this, "房号不能为空", Toast.LENGTH_SHORT).show();
                            }
                           recreate();


                        }
                    }

                });
                builder.create().show();

            }
        });



    }



    private void initListener(){
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                       HouseActivity.actionStart(NewAdminActivity.this,null);

                        break;
                    case R.id.nav_call:
                        Intent intent1 = new Intent(NewAdminActivity.this,renter_activity.class);
                        startActivityForResult(intent1,1);

                        break;
                    case R.id.nav_friends:
                        Intent intent = new Intent(NewAdminActivity.this,bill_activity.class);
                        startActivity(intent);

                        break;

                }


                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percent = Float.valueOf(Math.abs(verticalOffset)) / Float.valueOf(appBarLayout.getTotalScrollRange());
                if (titleCenterLayout != null && mAvater != null && mSettingIv != null && mMsgIv != null) {
                    titleCenterLayout.setAlpha(percent);
                    StatusBarUtil.setTranslucentForImageView(NewAdminActivity.this, (int) (255f * percent), null);
                    if (percent == 0) {
                        groupChange(1f, 1);
                    } else if (percent == 1) {
                        if (mAvater.getVisibility() != View.GONE) {
                            mAvater.setVisibility(View.GONE);
                        }
                        groupChange(1f, 2);
                    } else {
                        if (mAvater.getVisibility() != View.VISIBLE) {
                            mAvater.setVisibility(View.VISIBLE);
                        }
                        groupChange(percent, 0);
                    }

                }
            }
        });
        AppBarLayoutOverScrollViewBehavior myAppBarLayoutBehavoir = (AppBarLayoutOverScrollViewBehavior)
                ((CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();
        myAppBarLayoutBehavoir.setOnProgressChangeListener(new AppBarLayoutOverScrollViewBehavior.onProgressChangeListener() {
            @Override
            public void onProgressChange(float progress, boolean isRelease) {
                progressBar.setProgress((int)(progress*360));
                if (progress == 1 && !progressBar.isSpinning && isRelease){

                }
                if (mMsgIv != null){
                    if (progress == 0 && !progressBar.isSpinning){
                        mMsgIv.setVisibility(View.VISIBLE);
                    }else if (progress>0 && mSettingIv.getVisibility()== View.VISIBLE){
                        mMsgIv.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        mTablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {


            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTablayout.setCurrentTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void initStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4以下不支持状态栏变色
            //注意了，这里使用了第三方库 StatusBarUtil，目的是改变状态栏的alpha
            StatusBarUtil.setTransparentForImageView(NewAdminActivity.this, null);
            //这里是重设我们的title布局的topMargin，StatusBarUtil提供了重设的方法，但是我们这里有两个布局
            //TODO 关于为什么不把Toolbar和@layout/layout_uc_head_title放到一起，是因为需要Toolbar来占位，防止AppBarLayout折叠时将title顶出视野范围
            int statusBarHeight = getStatusBarHeight(NewAdminActivity.this);
            CollapsingToolbarLayout.LayoutParams lp1 = (CollapsingToolbarLayout.LayoutParams) titleContainer.getLayoutParams();
            lp1.topMargin = statusBarHeight;
            titleContainer.setLayoutParams(lp1);
            CollapsingToolbarLayout.LayoutParams lp2 = (CollapsingToolbarLayout.LayoutParams) mToolBar.getLayoutParams();
            lp2.topMargin = statusBarHeight;
            mToolBar.setLayoutParams(lp2);
        }
    }

    /**
     * @param alpha
     * @param state 0-正在变化 1展开 2 关闭
     */
    public void groupChange(float alpha, int state) {
        lastState = state;

        mSettingIv.setAlpha(alpha);
        mMsgIv.setAlpha(alpha);

        switch (state) {
            case 1://完全展开 显示白色
                mMsgIv.setImageResource(R.drawable.icon_msg);
                mSettingIv.setImageResource(R.drawable.icon_setting);
                mViewPager.setNoScroll(false);
                break;
            case 2://完全关闭 显示黑色
                mMsgIv.setImageResource(R.drawable.icon_msg_black);
                mSettingIv.setImageResource(R.drawable.icon_setting_black);
                mViewPager.setNoScroll(false);
                break;
            case 0://介于两种临界值之间 显示黑色
                if (lastState != 0) {
                    mMsgIv.setImageResource(R.drawable.icon_msg_black);
                    mSettingIv.setImageResource(R.drawable.icon_setting_black);
                }
                mViewPager.setNoScroll(true);
                break;
        }
    }


    /**
     * 获取状态栏高度
     * ！！这个方法来自StatusBarUtil,因为作者将之设为private，所以直接copy出来
     *
     * @param context context
     * @return 状态栏高度
     */
    private int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }




    public int getData3(){
        dbHelper = myDatabaseHelper.getInstance(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select room from bill", null);
        while (cursor.moveToNext()) {
            String s = cursor.getString(cursor.getColumnIndex("room"));
            mData3.add(s);
        }
        return mData3.size();
    }
    public String[] getNames() {
        String[] mName1 = new String[]{"房源"};
        String[] mName2 = new String[]{"租户"};
        String[] mName3 = new String[]{"账单"};

         c = getData3();
        Log.i("b1","b:"+b);
        int i[] = {a,b,c};



        for (String str : mName1) {
            mTabEntities.add(new TabEntity(String.valueOf(i[0]), str));

        }
        for (String str : mName2) {
            mTabEntities.add(new TabEntity(String.valueOf(i[1]), str));

        }
        for (String str : mName3) {
            mTabEntities.add(new TabEntity(String.valueOf(i[2]), str));

        }
        return mName1;
    }


    public List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ItemFragment2());
        fragments.add(new ItemFragment1());
        fragments.add(new ItemFragment3());
        return fragments;
    }


}
