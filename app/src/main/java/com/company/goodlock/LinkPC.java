package com.company.goodlock;

import androidx.appcompat.app.AppCompatActivity;
import android.view.*;

import android.content.Intent;
import android.os.Bundle;

public class LinkPC extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_pc);
    }
    public void Back(View view){
        Intent intent = new Intent(LinkPC.this,MainActivity.class);
        startActivity(intent);
    }
}
