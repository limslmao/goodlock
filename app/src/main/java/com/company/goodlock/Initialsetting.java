package com.company.goodlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.*;
import android.os.Bundle;
import com.company.goodlock.util.PreferenceUtil;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Initialsetting extends AppCompatActivity {
    EditText key;
    PreferenceUtil pUtil;
    private Button BSave;
    private TextView TextNotice;
    private EditText Passwordnum;
    private String SaveUserKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialsetting);
        key = (EditText)findViewById(R.id.cKey);
        pUtil = new PreferenceUtil();
        findViews();
    }

    public void findViews() {
        Passwordnum = findViewById(R.id.cKey);
        TextNotice = findViewById(R.id.textFirstHint);
        BSave = (Button) findViewById(R.id.Ok);

        BSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveUserKey = Passwordnum.getText().toString();
                if(SaveUserKey.length() == 8){
                    SharedPreferences pref = getSharedPreferences("SaveKey", MODE_PRIVATE);
                    pref.edit()
                            .putString("SaveUserKey", SaveUserKey)
                            .commit();
                    TextNotice.setTextColor(Color.GREEN);
                    TextNotice.setText( getString(R.string.change_success) + SaveUserKey);

                    pUtil.setKey(Initialsetting.this,key.getText().toString());
                    Intent intent = new Intent(Initialsetting.this,MainActivity.class);
                    startActivity(intent);
                    Initialsetting.this.finish();
                }else {
                    TextNotice.setTextColor(Color.RED);
                    TextNotice.setText(R.string.change_fail);
                }
            }
        });

    }
}
