package com.company.goodlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.company.goodlock.util.PreferenceUtil;
import com.company.goodlock.view.MyFloatView;

public class Loading extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initialization();
                String key = PreferenceUtil.getKey(Loading.this);
                PreferenceUtil.setValue(Loading.this,"");
                Intent intent;
                if (key == "") {
                    Log.d("TAG", "true");
                    intent = new Intent(Loading.this, Terms.class);

                } else {
                    intent = new Intent(Loading.this, MainActivity.class);
                }
                startActivity(intent);
                Loading.this.finish();
            }
        }, 2000);
    }
    private void initialization(){
        MyFloatView myFV = new MyFloatView(getApplicationContext());
        WindowManager wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        ((MyApplication)getApplication()).initialization(myFV,wm);
    }

}
