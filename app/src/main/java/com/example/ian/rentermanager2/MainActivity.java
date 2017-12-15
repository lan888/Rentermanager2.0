package com.example.ian.rentermanager2;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ian.rentermanager2.entity.MyUser;
import com.example.ian.rentermanager2.permission.PermissionListener;
import com.example.ian.rentermanager2.permission.PermissionManager;
import com.example.ian.rentermanager2.utils.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MainActivity extends AppCompatActivity {
    private EditText name;//用户名
    private EditText password;//用户密码
    private Button login;//登录按钮
    private TextView register;//注册
    private TextView forgetNum;//忘记密码
    private RoundImageView mImageView;//头像

    PermissionManager helper;


    MyUser mUser = new MyUser();
    private Bitmap mBitmap;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    private String mFilePath;
    private String mFileName;
    private String img_url1;
    private BmobFile uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this,"26a7be21ef92ba03984d461048b40981");


        FileUtils.init();

        mFilePath = FileUtils.getFileDir() + File.separator;
        name = (EditText) findViewById(R.id.admin_login_activity_name_input);
        password = (EditText) findViewById(R.id.admin_login_activity_password_input);
        login = (Button) findViewById(R.id.admin_login_activity_login);
        register = (TextView) findViewById(R.id.admin_login_activity_register);
        forgetNum = (TextView) findViewById(R.id.admin_login_activity_forgetNum);
        mImageView = (RoundImageView) findViewById(R.id.admin_pic);


        Bmob.getServerTime(new QueryListener<Long>() {
            @Override
            public void done(Long aLong, BmobException e) {
                if(e==null){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String times = formatter.format(new Date(aLong * 1000L));
                    Log.i("bmob","当前服务器时间为:" + times);
                }else{
                    Log.i("bmob","获取服务器时间失败:" + e.getMessage());
                }
            }
        });
        helper = PermissionManager.with(MainActivity.this)
                //添加权限请求码
                .addRequestCode(MainActivity.TAKE_PICTURE)
                //设置权限，可以添加多个权限
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                //设置权限监听器
                .setPermissionsListener(new PermissionListener() {

                    @Override
                    public void onGranted() {
                        //当权限被授予时调用
                      //  Toast.makeText(MainActivity.this, "Camera Permission granted",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDenied() {
                        //用户拒绝该权限时调用
                        Toast.makeText(MainActivity.this, "Camera Permission denied",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onShowRationale(String[] permissions) {
                        //当用户拒绝某权限时并点击`不再提醒`的按钮时，下次应用再请求该权限时，需要给出合适的响应（比如,给个展示对话框来解释应用为什么需要该权限）
                        Snackbar.make(login, "需要相机权限去拍照", Snackbar.LENGTH_INDEFINITE)
                                .setAction("ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //必须调用该`setIsPositive(true)`方法
                                        helper.setIsPositive(true);
                                        helper.request();

                                    }
                                }).show();
                    }
                })
                //请求权限
                .request();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameInfo = name.getText().toString();
                String passwordInfo = password.getText().toString();
                //从数据库获取密码并判断是否相同
                mUser.setUsername(nameInfo);
                mUser.setPassword(passwordInfo);
                mUser.login(new SaveListener<MyUser>() {

                   @Override
                   public void done(MyUser s, BmobException e) {
                       if(e==null){
                           Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                            MyUser user = BmobUser.getCurrentUser(MyUser.class);
                           NewAdminActivity.actionStart(MainActivity.this,user.getUsername());
                           //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                       }else{
                           Log.e("fail","失败"+e);
                           Toast.makeText(MainActivity.this,"用户名或者密码错误",Toast.LENGTH_SHORT).show();
                       }
                   }
               });


                }

        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View textEntryView = factory.inflate(R.layout.register,null);
                builder.setTitle("管理员注册");
                builder.setView(textEntryView);

                final ImageView img = textEntryView.findViewById(R.id.admin_pic_reg);
                final EditText phone =  textEntryView.findViewById(R.id.admin_register_phone);
                final EditText name = textEntryView.findViewById(R.id.admin_register_name);
                final EditText firstPassword =  textEntryView.findViewById(R.id.admin_register_first_password);
                final EditText secondPassword = textEntryView.findViewById(R.id.admin_register_second_password);
                final EditText mail = textEntryView.findViewById(R.id.admin_register_mail);

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showChoosePicDialog();

                    }
                });
                Toast.makeText(MainActivity.this, "请点击头像框设置头像" , Toast.LENGTH_SHORT).show();

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String phoneInfo = phone.getText().toString();
                        String nameInfo = name.getText().toString();
                        String firstPasswordInfo = firstPassword.getText().toString();
                        String secondPasswordInfo = secondPassword.getText().toString();
                        String mailInfo = mail.getText().toString();
                        if (phoneInfo.matches("[1][358]\\d{9}")) {
                            if (!nameInfo.equals("")) {
                                if (firstPasswordInfo.matches("[0-9]{6}")) {
                                    if (firstPasswordInfo.equals(secondPasswordInfo)) {
                                        if (!mailInfo.equals("")) {
                                            if (uri != null){
                                                mUser.setUsername(nameInfo);
                                                mUser.setPassword(secondPasswordInfo);
                                                mUser.setEmail(mailInfo);
                                                mUser.setMobilePhoneNumber(phoneInfo);
                                                mUser.setImage(uri);
                                                mUser.signUp(new SaveListener<BmobUser>() {
                                                    @Override
                                                    public void done(BmobUser bmobUser, BmobException e) {
                                                        if(e==null){
                                                            Toast.makeText(MainActivity.this, "注册成功:" +bmobUser.toString(), Toast.LENGTH_SHORT).show();

                                                        }else{
                                                            Log.e("fail","注册失败"+e);
                                                            Toast.makeText(MainActivity.this, "注册失败"+e , Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                            }else {
                                                Toast.makeText(MainActivity.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                        else {
                                            Toast.makeText(MainActivity.this, "电子邮箱不能为空", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "两次密码不相同", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "密码为6位纯数字", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(MainActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "手机号码格式错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.create().show();
            }
        });

        forgetNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,forgetNum_activity.class);
                startActivity(intent);


            }
        });
    }
    /**
     * 显示修改图片的对话框
     */
    protected void showChoosePicDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("添加图片");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        Toast.makeText(MainActivity.this,"设置头像成功",Toast.LENGTH_SHORT).show();
                        break;
                    case TAKE_PICTURE: // 拍照
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            File path = new File(mFilePath);
                            if (!path.exists()) {
                                path.mkdirs();
                            }
                            mFileName = System.currentTimeMillis() + ".jpg";
                            File file = new File(path, mFileName);
                            if (file.exists()) {
                                file.delete();
                            }
                            tempUri = FileUtils.getUriForFile(MainActivity.this,file);
                            FileUtils.startActionFile(MainActivity.this,file,"image/*");
                            FileUtils.startActionCapture(MainActivity.this,file,TAKE_PICTURE);

                        } else {
                            Log.e("main","sdcard not exists");
                        }
                        break;
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PICTURE:
                cutImage(tempUri);
                    Cursor cursor1= getContentResolver().query(tempUri,null,null,null,null);
                    Log.e("uri","uri:"+tempUri+"\n"+cursor1);
                    Toast.makeText(MainActivity.this,"uri:"+tempUri,Toast.LENGTH_SHORT).show();
                    if (cursor1!=null&&cursor1.moveToFirst()){
                        int index1=cursor1.getColumnIndex("_display_name");
                        img_url1="/storage/emulated/0/ian/files/"+cursor1.getString(index1);
                        upload(img_url1);
                        Log.e("uri1","url:"+img_url1);
                    }else {
                         img_url1 = tempUri.getPath();
                        upload(img_url1);
                    }

                //将图片URI转换成存储路径
              //  CursorLoader cursorLoader = new CursorLoader(this,tempUri,null,null,null,null);

                break;
            case CHOOSE_PICTURE:
                cutImage(data.getData());
                if (data.getData()!=null){
                    Cursor cursor= getContentResolver().query(data.getData(),null,null,null,null);
                    Log.e("bd_uri","uri:"+data.getData()+"\n"+cursor);
                    Toast.makeText(MainActivity.this,"uri:"+data.getData(),Toast.LENGTH_SHORT).show();
                    if (cursor!=null&&cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex("_data");
                        String img_url = cursor.getString(index);
                        upload(img_url);
                    }
                }


                break;
            case CROP_SMALL_PICTURE:
                if (data != null){
                    setImageToView(data);
                    Toast.makeText(MainActivity.this,"设置头像成功",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    protected  void cutImage(Uri uri){
        if (uri == null){
            Log.i("al","The uri is not exist.");
        }

        Intent intent = new Intent("com.android.camera.action.CROP");

        Log.e("uri!!!","Url:"+uri);
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
//        Uri uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivityForResult(intent, CROP_SMALL_PICTURE);

    }

    /**
     * 保存裁剪之后的图片数据
     */



    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(mBitmap);
            mImageView.setImageDrawable(drawable);

            //这里图片是方形的，可以用一个工具类处理成圆形（很多头像都是圆形，这种工具类网上很多不再详述）

        }
    }
    private void upload (String imgpath){
        final BmobFile file = new BmobFile(new File(imgpath));
        file.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    uri = file;
                    Log.e("success!!!!","上传成功"+file.getFileUrl());
                }else {
                    Log.e("fail!!!!!","失败："+e.getMessage());
                }
            }
        });
    }
}

