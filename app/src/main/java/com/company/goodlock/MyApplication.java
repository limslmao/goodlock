package com.company.goodlock;
import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import com.company.goodlock.view.MyFloatView;

public class MyApplication extends Application {

    /**
     * 創建全局變數
     * 註意在AndroidManifest.xml中的Application節點添加android:name=".MyApplication"屬性
     *
     */
    private WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();
    private WindowManager.LayoutParams Params=new WindowManager.LayoutParams();
    private WindowManager.LayoutParams addParams=new WindowManager.LayoutParams();
    private WindowManager.LayoutParams pswParams=new WindowManager.LayoutParams();
    private WindowManager.LayoutParams fingerParams=new WindowManager.LayoutParams();
    private WindowManager wm;
    private WindowManager wmc;
    private MyFloatView myFV;
    public WindowManager getWm(){
        return wm;
    }
    public WindowManager getWmc(){
        return wmc;
    }
    public WindowManager.LayoutParams getMywmParams(){
        return wmParams;
    }
    public WindowManager.LayoutParams getMyParams(){
        return Params;
    }
    public WindowManager.LayoutParams getfingerParams(){
        return fingerParams;
    }
    public WindowManager.LayoutParams getAddParams(){return addParams;}
    public WindowManager.LayoutParams getpswParams(){return pswParams;}
    public MyFloatView getMyFV(){
        return myFV;
    }
    public FingerprintManagerCompat manager;
    public FingerprintManagerCompat getManager(){return manager;}
    public void initialization(MyFloatView FV,WindowManager windowManager){
        //wm初始化
        wm = windowManager;
        wmc = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //wmParams初始化
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }   //設置window type
        wmParams.format= PixelFormat.RGBA_8888;   //設置圖片格式，效果為背景透明
        //設置Window flag
        wmParams.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity= Gravity.LEFT|Gravity.TOP;   //調整懸浮視窗至左上角
        //以屏幕左上角為原點，設置x、y初始值
        wmParams.x=0;
        wmParams.y=0;
        //設置懸浮視窗長寬數據
        wmParams.width=200;
        wmParams.height=200;
        //Params初始化
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            Params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        Params.format= PixelFormat.RGBA_8888;
        Params.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        Params.gravity=Gravity.CENTER|Gravity.CENTER;
        Params.x=0;
        Params.y=0;
        //浮動視窗List
        Params.width=(int)(wm.getDefaultDisplay().getWidth()*0.8);
        Params.height=(int)(wm.getDefaultDisplay().getHeight()*0.6);
        //addParams初始化
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            addParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            addParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        addParams.format= PixelFormat.RGBA_8888;
        addParams.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        addParams.gravity=Gravity.CENTER|Gravity.CENTER;
        addParams.x=0;
        addParams.y=0;
        //浮動視窗自訂名稱
        addParams.width=(int)(wm.getDefaultDisplay().getWidth()*0.6);
        addParams.height= (int)(wm.getDefaultDisplay().getHeight()*0.4);
        //pswParams初始化
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            pswParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            pswParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        pswParams.format= PixelFormat.RGBA_8888;
        pswParams.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        pswParams.gravity=Gravity.CENTER|Gravity.CENTER;
        pswParams.x=0;
        pswParams.y=0;
        //fingerParams初始設定
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            fingerParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            fingerParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        fingerParams.format= PixelFormat.RGBA_8888;
        fingerParams.flags= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        fingerParams.gravity=Gravity.CENTER|Gravity.CENTER;
        fingerParams.x=0;
        fingerParams.y=0;
        //浮動視窗List
        fingerParams.width=(int)(wm.getDefaultDisplay().getWidth()*0.8);
        fingerParams.height=(int)(wm.getDefaultDisplay().getHeight()*0.6);
        //浮動視窗密碼設定
        pswParams.width=(int)(wm.getDefaultDisplay().getWidth()*0.8);
        pswParams.height=(int)(wm.getDefaultDisplay().getHeight()*0.6);
        //myFV初始化
        myFV=FV;
        myFV.setImageResource(R.drawable.tv);

    }

    public String imei;
    public String code;
    public boolean init;

    //修改 變數値
    public void setimei(String imei){
        this.imei = imei;
    }
    public void setcode(String code){
        this.code = code;
    }
    public void setinit(boolean init){
        this.init = init;
    }


    //取得 變數值
    public String getimei(){
        return imei;
    }
    public String getCode(){
        return code;
    }
    public boolean getinit(){
        return init;
    }

    private int QQ;         //不同actitivity共用變數
    private int sec;

    //修改 變數値
    public void setQQ(int QQ){
        this.QQ = QQ;
    }
    public void setsec(int sec){
        this.sec = sec;
    }

    //取得 變數值
    public int getQQ(){
        return QQ;
    }
    public int getsec(){
        return sec;
    }

    public boolean onepoint;

    public void setOnepoint(boolean onepoint){
        this.onepoint = onepoint;
    }

    public boolean getonepoint(){
        return onepoint;
    }

    public boolean noenter;

    public void setnoenter(boolean noenter){
        this.noenter = noenter;
    }

    public boolean getnoenter(){
        return noenter;
    }

    public int floatfinger;

    public void setfloatfinger(int floatfinger){
        this.floatfinger = floatfinger;
    }

    public int getfloatfinger(){
        return floatfinger;
    }
}