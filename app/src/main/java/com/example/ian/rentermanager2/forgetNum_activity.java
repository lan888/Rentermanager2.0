package com.example.ian.rentermanager2;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.ian.rentermanager2.R.id.pass;

/**
 * Created by Ian on 2017/8/3 0003.
 */

public class forgetNum_activity extends AppCompatActivity {

    private EditText name;
    private EditText code;
    private Button find;
    private myDatabaseHelper dbHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgetnum_layout);

        dbHelper=myDatabaseHelper.getInstance(this);
        name = (EditText) findViewById(R.id.admin_register_name1);
        code = (EditText) findViewById(R.id.admin_register_info1);
        find = (Button) findViewById(R.id.find);


        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameInfo = name.getText().toString();
                String codeInfo = code.getText().toString();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("select name from admin where name=? ",new String[]{nameInfo});
                Cursor cursor1 = db.rawQuery("select password from admin where name=?",new String[]{nameInfo});
                if (cursor.moveToNext()){
                    if (codeInfo.equals("10086")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(forgetNum_activity.this);
                        LayoutInflater factory = LayoutInflater.from(forgetNum_activity.this);
                         View textEntryView = factory.inflate(R.layout.password,null);
                        builder.setView(textEntryView);
                         TextView password1 =  textEntryView.findViewById(pass);

                        if (cursor1.moveToNext()){

                           String pi= cursor1.getString(cursor1.getColumnIndex("password"));
                            password1.setText(pi);


                        }


                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.create().show();
                    }else {
                        Toast.makeText(forgetNum_activity.this,"注册码不正确",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(forgetNum_activity.this,"管理员不存在",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
