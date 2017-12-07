package com.example.ian.rentermanager2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Ian on 2017/8/2 0002.
 */

public class admin_activity extends AppCompatActivity{
    private TextView ntv;
    private Button house_btn;
    private Button renter_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_layout);

        ntv = (TextView)findViewById(R.id.ntv);
        house_btn = (Button) findViewById(R.id.house_btn);
        renter_btn = (Button) findViewById(R.id.renter_btn);

        //final Intent intent = getIntent();
       // String n1 = intent.getStringExtra("i");
       // ntv.setText(n1);

        house_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_activity.this,HouseActivity.class);
                startActivity(intent);

            }
        });
        renter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_activity.this,renter_activity.class);
                startActivity(intent);
            }
        });
    }
  /*  public static void actionStart(Context context,String nameInfo ){
        Intent intent = new Intent(context,admin_activity.class);
        intent.putExtra("i",nameInfo);
        context.startActivity(intent);
    }*/
}
