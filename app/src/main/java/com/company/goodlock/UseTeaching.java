package com.company.goodlock;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.company.goodlock.util.PreferenceUtil;

public class UseTeaching extends Activity implements OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views,Views;
    private Button bt,bt2;
    //引導圖片資源
    private int[] pics = { R.drawable.step1,R.drawable.step2,R.drawable.step3,R.drawable.step4,R.drawable.step5,R.drawable.step6,
            R.drawable.step7};
    private int[] Pics = {R.drawable.step10,R.drawable.step11,R.drawable.step12,R.drawable.step13};
    //底部小店圖片
    private ImageView[] dots ;
    //記錄當前選中位置
    private int currentIndex;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_teaching);

        views = new ArrayList<View>();
        Views = new ArrayList<View>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //初始化引導圖片列表
        for(int i=0; i<pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            views.add(iv);
        }
        for(int i=0; i<Pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(Pics[i]);
            Views.add(iv);
        }
        vp = (ViewPager) findViewById(R.id.viewpager);
        //初始化Adapter
        vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        //绑定回調
        vp.setOnPageChangeListener(this);
        bt = (Button)findViewById(R.id.skip);
        bt.setVisibility(View.INVISIBLE);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = PreferenceUtil.getKey(UseTeaching.this);
                Intent intent;
                if(key==""){
                    intent = new Intent(UseTeaching.this,Initialsetting.class);
                }else{
                    intent = new Intent(UseTeaching.this,MainActivity.class);
                }
                startActivity(intent);
                UseTeaching.this.finish();
            }
        });
        bt2 = (Button)findViewById(R.id.LoginGuide);

        bt2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bt2.getText().toString().equals(getString(R.string.Login))){
                    vpset();
                    bt2.setText(R.string.Normal);
                }else{
                    vpSet();
                    bt2.setText(R.string.Login);
                }
            }
        });

        //初始化底部小點
        initDots();

    }
    private void vpset(){
        vp = (ViewPager) findViewById(R.id.viewpager);
        //初始化Adapter
        vpAdapter = new ViewPagerAdapter(Views);
        vp.setAdapter(vpAdapter);
        //绑定回調
        vp.setOnPageChangeListener(UseTeaching.this);
        initDots();
    }
    private void vpSet(){
        vp = (ViewPager) findViewById(R.id.viewpager);
        //初始化Adapter
        vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        //绑定回調
        vp.setOnPageChangeListener(UseTeaching.this);
        initDots();
    }
    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[4];
        //循環取得小點圖片
        for (int i = 0; i < 4; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都設为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//設置位置tag，方便取出與當前位置對應
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//設置为白色，即選中狀態
    }

    /**
     *設置當前的引導頁
     */
    private void setCurView(int position)
    {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp.setCurrentItem(position);
    }
    /**
     *這只當前引導小點的選中
     */
    private void setCurDot(int positon)
    {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }
        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = positon;
    }
    //當滑動狀態改變時調用
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }
    //當當前頁面被滑動時調用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }
    //當新的頁面被選中時調用
    @Override
    public void onPageSelected(int arg0) {
        //設置底部小點選中狀態
        String string = bt2.getText().toString();
        if(string.equals(getString(R.string.Login))&&arg0==pics.length-1)
            arg0 = 3;
        else if (string.equals(getString(R.string.Normal))&&arg0==Pics.length-1)
            arg0 = 3;
        else if(arg0>2)
            arg0 = 2;
        setCurDot(arg0);
        bt.setVisibility(View.VISIBLE);
    }
    @Override
    public void onClick(View v) {
        int position = (Integer)v.getTag();
        setCurView(position);
        setCurDot(position);
    }
}