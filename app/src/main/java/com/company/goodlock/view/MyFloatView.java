package com.company.goodlock.view;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

public class MyFloatView extends AppCompatImageView {
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    private float mStartX;
    private float mStartY;
    private OnClickListener mClickListener;
    public FingerprintManagerCompat manager ;

    private WindowManager wm=(WindowManager)getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

    //此wmParams為獲取的全局變數，用以保存懸浮視窗的屬性
    private WindowManager.LayoutParams wmParams = ((com.company.goodlock.MyApplication)getContext().getApplicationContext()).getMywmParams();

    public MyFloatView(Context context) {
        super(context);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //獲取相對屏幕的坐標，即以屏幕左上角為原點
        x = event.getRawX();
        y = event.getRawY() - 25;   //25是系統狀態欄的高度
        Log.i("currP", "currX" + x + "====currY" + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //獲取相對View的坐標，即以此View左上角為原點
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                mStartX = x;
                mStartY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                if(wmParams.x<=(wm.getDefaultDisplay().getWidth())/2)
                    mTouchStartX = x;
                else
                    mTouchStartX = 0-wmParams.x;
                updateViewPosition();
                mTouchStartX = mTouchStartY = 0;
                if (Math.abs(x - mStartX) < 10 && Math.abs(y - mStartY) < 10) {
                    if (mClickListener != null) {
                        mClickListener.onClick(this);
                    }
                }
                break;
        }
        return true;
    }
    @Override
    public void setOnClickListener(OnClickListener l) {
        this.mClickListener = l;
    }
    private void updateViewPosition(){
        //更新浮動視窗位置參數
        wmParams.x=(int)( x-mTouchStartX);
        wmParams.y=(int) (y-mTouchStartY);
        wm.updateViewLayout(this, wmParams);
    }

}
