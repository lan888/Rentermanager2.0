package com.example.ian.rentermanager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {
    private EditText name;//用户名
    private EditText password;//用户密码
    private Button login;//登录按钮
    private TextView register;//注册
    private TextView forgetNum;//忘记密码

    BmobUser user = new BmobUser();
    String path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this,"26a7be21ef92ba03984d461048b40981");


        name = (EditText) findViewById(R.id.admin_login_activity_name_input);
        password = (EditText) findViewById(R.id.admin_login_activity_password_input);
        login = (Button) findViewById(R.id.admin_login_activity_login);
        register = (TextView) findViewById(R.id.admin_login_activity_register);
        forgetNum = (TextView) findViewById(R.id.admin_login_activity_forgetNum);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameInfo = name.getText().toString();
                String passwordInfo = password.getText().toString();
                //从数据库获取密码并判断是否相同
               user.setUsername(nameInfo);
               user.setPassword(passwordInfo);
               user.login(new SaveListener<BmobUser>() {

                   @Override
                   public void done(BmobUser bmobUser, BmobException e) {
                       if(e==null){
                           Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                           BmobUser user = BmobUser.getCurrentUser();
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

                    }
                });

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
                                            user.setUsername(nameInfo);
                                            user.setPassword(secondPasswordInfo);
                                            user.setEmail(mailInfo);
                                            user.setMobilePhoneNumber(phoneInfo);
                                            user.signUp(new SaveListener<BmobUser>() {
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


}

