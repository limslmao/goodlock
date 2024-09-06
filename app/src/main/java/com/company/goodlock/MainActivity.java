package com.company.goodlock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button UseTeach,linkPC,Qlogin,set,abuutUs;
    public static final int READ_PHONE_STATE = 9;
    public String imei_code;
    public static MyApplication gv;
    private FingerprintManagerCompat manager;
    private Context mContext = MainActivity.this;
    private CustomDialog.Builder builder;
    public Set setting;
    private CustomDialog mDialog;
    private Switch aSwitch;
    private View layout;
    public SharedPreferences sharedPreferences;
    public static final String data ="DATA";
    public static final String switchField ="SWITCH_STATE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        builder = new CustomDialog.Builder(this);
        manager = FingerprintManagerCompat.from(this);
        setView();
        setting = new Set();
        gv = (MyApplication) getApplicationContext();

        sharedPreferences = getSharedPreferences(data,MODE_PRIVATE);
        confirm();

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this , new String[] {Manifest.permission.READ_PHONE_STATE},READ_PHONE_STATE);
        }else {
            gv.setimei(getIMEI());
        }
        Log.d("IMEI","IMEI碼："+gv.getimei());
    }
    public void setView(){
        UseTeach = (Button)findViewById(R.id.UseTeach);
        linkPC = (Button)findViewById(R.id.linkPC);
        Qlogin = (Button)findViewById(R.id.Qlogin);
        set = (Button)findViewById(R.id.set);
        abuutUs = (Button)findViewById(R.id.aboutUs);
    }

    public String getIMEI() {
        /*int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this , new String[] {Manifest.permission.READ_PHONE_STATE},READ_PHONE_STATE);
        }*/
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                @SuppressLint("MissingPermission") String imei = telephonyManager.getImei();
                Log.d("IMEI2", "API 26以上(含)");
                return imei;
            } else {
                @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
                Log.d("IMEI2","API 26以下");
                return imei;
            }
        }catch(Exception e){
            String imei = "778341234560120";
            return imei;
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case MainActivity.READ_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //取得權限，進行檔案存取
                    gv.setimei(getIMEI());
                    Log.d("IMEI2","跑過這段囉~~");
                    Log.d("IMEI2",gv.getimei());
                    return;
                }
                return;
        }
    }

    public void onclick(View view){
        Intent intent;
        if(view ==abuutUs) {
            Uri uri = Uri.parse("http://goodlock.site/");
            intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }else if(view == set){
            intent = new Intent(MainActivity.this,Set.class);
            startActivity(intent);
        }else if(view == UseTeach){
            intent = new Intent(MainActivity.this,UseTeaching.class);
            startActivity(intent);
        }else if(view == linkPC){
            intent = new Intent(MainActivity.this,LinkPC.class);
            startActivity(intent);
        }else if(view == Qlogin){
            intent = new Intent(MainActivity.this,NormalLoginTest.class);
            startActivity(intent);
        }
    }
    public void test(View view){

    }

    public void confirm(){
        //GlobalVariable gv = (GlobalVariable)getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.activity_set, null);
        aSwitch = (Switch) layout.findViewById(R.id.switchCheck);
        aSwitch.setChecked(sharedPreferences.getBoolean(switchField,false));
        Log.d("句柄","onClick isChecked switchField = is open"+aSwitch.isChecked());
        int n;
        n = setting.onCheckedChanged((Switch) layout.findViewById(R.id.switchCheck),gv.getQQ()) ;
        String use = getString(R.string.UsingTouchID);
        if(n==1 && gv.getsec() == 0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkAndReturnStatus() == status.NOT_FIND_HARDWARE){
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                    builder2.setMessage(R.string.no_fingersystem);
                    builder2.setCancelable(false);
                    builder2.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder2.create().show();
                }else if(checkAndReturnStatus() == status.NEED_ENROLL_FINGERPRINT) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                    builder2.setMessage(R.string.no_fingerprint_enrolled_dialog_message);
                    //builder2.setIcon(android.R.drawable.stat_sys_warning);
                    builder2.setCancelable(false);
                    builder2.setPositiveButton(R.string.cancel_btn_dialog, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });
                    // show this dialog
                    builder2.create().show();
                }else{
                    showSingleButtonDialog(use, null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //mDialog.dismiss();
                            finish();
                            //这里写自定义处理XXX
                        }
                    }, 1);
                }
            } else {
                AlertDialog.Builder builder3 = new AlertDialog.Builder(mContext);
                builder3.setMessage(R.string.no_fingersystem);
                builder3.setCancelable(false);
                builder3.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder3.create().show();
            }
        }
    }

    public enum status {
        NEED_ENROLL_FINGERPRINT,    // 需要註冊指紋
        NOT_FIND_HARDWARE,          // 找不到指紋感應器
        VERSION_NOT_SUPPORT,        // Android 版本過低
        PASS                        // 通過檢測
    }

    private void showSingleButtonDialog(String alertText, String btnText, View.OnClickListener onClickListener,int n) {
        if (n == 1) {
            mDialog = builder.setMessage(alertText)
                    .setSingleButton(btnText, onClickListener)
                    .createSingleButtonDialog();
            mDialog.show();
        } else {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //builder.setContentView(inflater.inflate(R.layout.mdialog2,null));
            final View view1 = inflater.inflate(R.layout.mdialog2, null);
            mDialog = builder.setMessage(alertText)
                    //.setContentView(view1)
                    .setSingleButton(btnText, onClickListener)
                    .createSingleButtonDialog();
            mDialog.show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {
                manager.authenticate(null, 0, null, new MyCallBack(), null);
            }
        }
    }

    /**
     * 是否有註冊過指紋，至少一組
     *
     * @return
     */
    private boolean hasEnrolledFingerprints() {
        return manager.hasEnrolledFingerprints();
    }

    /**
     * 是否有指紋感應器
     *
     * @return
     */
    private boolean isHardwareDetected() {
        return manager.isHardwareDetected();
    }

    public status checkAndReturnStatus() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (!isHardwareDetected()) {
                return status.NOT_FIND_HARDWARE;
            }

            if (!hasEnrolledFingerprints()) {
                return status.NEED_ENROLL_FINGERPRINT;
            }

        } else {
            // 由於 new class 時就需判斷版本，故不可能走到這。
            return status.VERSION_NOT_SUPPORT;
        }

        return status.PASS;
    }

    public class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback {
        private static final String TAG = "MyCallBack";

        // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            Log.d(TAG, "onAuthenticationError: " + errString);
            //Toast.makeText(mContext, "onAuthenticationError: " + errString, Toast.LENGTH_LONG).show();
        }

        // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
        @Override
        public void onAuthenticationFailed() {
            Log.d(TAG, "onAuthenticationFailed: " + getString(R.string.confirm_fail));
            Toast.makeText(mContext, getString(R.string.confirm_fail), Toast.LENGTH_LONG).show();
            //onDialog(2 );
            showSingleButtonDialog(getString(R.string.try_again), null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    //这里写自定义处理XXX
                }
            },2);
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            Log.d(TAG, "onAuthenticationHelp: " + helpString);
            Toast.makeText(mContext, "onAuthenticationHelp: " + helpString, Toast.LENGTH_LONG).show();
        }

        // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult
                                                      result) {
            Log.d(TAG, "onAuthenticationSucceeded: " + getString(R.string.confirm_success));
            Toast.makeText(mContext,  getString(R.string.confirm_success), Toast.LENGTH_LONG).show();
            /*Intent intent = new Intent();
            intent.setClass(MainActivity.this, MainActivity.class);
            startActivity(intent);*/
            //checked = 1;
            //建立共用變數類別
            //GlobalVariable gv = (GlobalVariable)getApplicationContext();
            // 修改 變數值
            gv.setsec(1);
            mDialog.dismiss();
        }
    }

}
