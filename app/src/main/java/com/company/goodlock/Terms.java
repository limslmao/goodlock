package com.company.goodlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class Terms extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        load();
    }
    public void load(){
        WindowManager wm=((MyApplication)getApplication()).getWm();
        ScrollView scrollView = (ScrollView)findViewById(R.id.ScrollView);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)scrollView.getLayoutParams();
        params.width = (int)(wm.getDefaultDisplay().getWidth()*0.9);
        params.height = (int)(wm.getDefaultDisplay().getHeight()*0.7);
        scrollView.setLayoutParams(params);
        final CheckBox agreeCheck = findViewById(R.id.checkAgree);
        Button agree = findViewById(R.id.agree);
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(agreeCheck.isChecked()){
                    Intent intent = new Intent(Terms.this,UseTeaching.class);
                    startActivity(intent);
                    Terms.this.finish();
                }else{
                    Toast.makeText(Terms.this,R.string.plz,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
