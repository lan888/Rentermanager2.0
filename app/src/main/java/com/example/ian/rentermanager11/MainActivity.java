package com.example.ian.rentermanager11;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText name;//用户名
    private EditText password;//用户密码
    private Button login;//登录按钮
    private TextView register;//注册
    private TextView forgetNum;//忘记密码
    private myDatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = myDatabaseHelper.getInstance(this);

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
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("select password from admin where name=?", new String[]{nameInfo});
                String pi =null;
                if (cursor.moveToNext()){
                    pi = cursor.getString(cursor.getColumnIndex("password"));
                }
                if (passwordInfo.equals(pi)){
                    admin_activity.actionStart(MainActivity.this,nameInfo);
                    cursor.close();
                }else {
                    Toast.makeText(MainActivity.this,"用户名或者密码错误",Toast.LENGTH_SHORT).show();
                }
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

                final EditText code =  textEntryView.findViewById(R.id.admin_register_info);
                final EditText name = textEntryView.findViewById(R.id.admin_register_name);
                final EditText firstPassword =  textEntryView.findViewById(R.id.admin_register_first_password);
                final EditText secondPassword = textEntryView.findViewById(R.id.admin_register_second_password);

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String codeInfo = code.getText().toString();

                        if (codeInfo.equals("10086")) {
                            String nameInfo = name.getText().toString();
                            String firstPasswordInfo = firstPassword.getText().toString();
                            String secondPasswordInfo = secondPassword.getText().toString();
                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                            if (firstPasswordInfo.matches("[0-9]{6}")) {

                                if (firstPasswordInfo.equals(secondPasswordInfo)) {
                                    Cursor cursor = db.rawQuery("select name from admin where name =?", new String[]{nameInfo});

                                    if (cursor.moveToNext()) {
                                        Toast.makeText(MainActivity.this, "该用户已存在", Toast.LENGTH_SHORT).show();
                                    } else {
                                        db.execSQL("insert into admin(name,password)values(?,?)", new String[]{nameInfo, firstPasswordInfo});
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "两次密码不相同", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "密码为6位纯数字", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "注册码错误", Toast.LENGTH_SHORT).show();
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

