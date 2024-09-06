package com.company.goodlock;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences;


public class ChangeKey extends AppCompatActivity {
    private Button BCancel;
    private Button BSave;
    private TextView TextNotice;
    private EditText Passwordnum;
    private String SaveUserKey = "";
    private String LoadUserKey = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_key);
        findViews();
    }

    public void findViews() {
        Passwordnum = findViewById(R.id.ChangeKey);
        TextNotice = findViewById(R.id.ChangeHint);
        LoadUserKey = Passwordnum.getText().toString();
        BSave = (Button) findViewById(R.id.ChangeOk);

        try {
            LoadUserKey = getSharedPreferences("SaveKey", MODE_PRIVATE)
                    .getString("SaveUserKey", "");
            Passwordnum.setText(LoadUserKey);

        }catch (Exception ex){
            TextNotice.setText(R.string.key_fail);
        }

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
                }else {
                    TextNotice.setTextColor(Color.RED);
                    TextNotice.setText(R.string.change_fail);
                }
            }
        });

        BCancel = (Button) findViewById(R.id.ChangeBack);
        BCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
