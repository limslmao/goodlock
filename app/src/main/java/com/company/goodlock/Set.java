package com.company.goodlock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.content.Context;
import android.view.*;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.company.goodlock.util.PreferenceUtil;
import com.company.goodlock.view.MyFloatView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class Set extends AppCompatActivity implements View.OnClickListener {
    private Switch window;
    private Intent intent;
    private WindowManager wm=null;
    private WindowManager.LayoutParams wmParams=null,Params=null;
    private MyFloatView myFV=null;
    private Boolean bool = true,addbool = true;
    private View test,add,lock,passwordSet,Sp,finger;
    private NormalLoginTest normalLogin = new NormalLoginTest();
    private static final String handle = Set.class.getSimpleName();
    public static final String data ="DATA";
    public Switch switch_button2;
    public SharedPreferences sharedPreferences;
    public static final String switchField ="SWITCH_STATE";
    public static final String switchField1 ="SWITCH_STATE1";
    public static MyApplication gv;
    //test 浮動視窗
    private ListView lvTest;
    private String condition = "Link";
    private List<ListViewItem> ListViewItemList = new ArrayList<>();
    private String LoadData = "LoadData";
    private String s;
    private String c;    //放UserID用
    private String u = "";    //放UserUrl用
    public String change = "";
    //Spinner選擇密碼長度
    //switch控制用
    private String CheckSwitchCondiction="";
    private String CheckSwitchCondictionArray[] = {"0","0","0","0","0","0","0"};
    //密碼設定控制用
    private String UserSpecialWords[] = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
    private String UserSpecialWordsCondiction="";
    private String UserSpecialWordsSave[] = {"@","#","$","%","^","&","*","_","+","=","-","?","~","!"};
    private String UserSpecialWordsSaveCondiction="";
    private Boolean checkbutton = false;
    //--------------------------------------------------------------以下為密碼設定參數
    //存放生成的密碼
    private String Password = "";
    //密碼生成使用    Normal
    private String UTSAH256;    //圖形轉碼雜湊
    private String UIDSHA256;   //使用者ID雜湊
    private String ALLPLUS;     //字串串接結果
    private String SHA256result;    //字串串接後SHA256結果
    private String Base58result;    //Base58編碼結果
    private Integer Passwordnumber; //儲存密碼長度

    //密碼生成使用    特殊字元
    private Integer NumberToChange = 0;     //需要更換成特殊字元的個數
    private String Location = "";   //擷取前N個字元為替換字索引值的索引值
    //密碼生成使用    特殊字元(自訂)
    //private String UserSpecialWords[] = {};
    //private String UserSpecialWordsCondiction="";

    //密碼生成使用    數字
    private Integer UIDToNum;       //UserID to number
    private Integer UKeyToNum;      //User金鑰 to number
    private String UTWithout0 = "";     //將轉碼前面的0給清除
    private String UIMEIWithout0 = "";     //將IMEI碼前面的0給清除
    private String MulResult;   //儲存相成後的一串數字

    //判定Switch啟動狀態的宣告
    private String S10Save = "";
    private String S9Save = "";
    private String S8Save = "";
    private String S7Save = "";
    private String S6Save = "";
    private String S5Save = "";
    private String S4Save = "";
    private String ShowCondiction ="";
    //顯示使用者資訊的宣告
    //使用者圖形轉碼
    private String SaveUserTrans;
    //使用者金鑰
    private String LoadUserKey = "";
    //使用者IMEI碼
    private String SaveIMEIReal="";
    private String SaveIMEITest = "778341234560120";
    //使用者ID
    private String SaveUserID;
    //Get item name
    private String getitemname;
    private Integer SaveItemName;
    //剪貼用到的東東
    ClipboardManager myClipboard;
    ClipData myClip;
    //public static final String data ="DATA";
    //public static final String switchField ="SWITCH_STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        window = (Switch)findViewById(R.id.switchFloat);
        myFV= ((MyApplication)getApplication()).getMyFV();
        wm=((MyApplication)getApplication()).getWm();
        gv = (MyApplication) getApplicationContext();
        switch_button2 =(Switch)findViewById(R.id.switchCheck);
        sharedPreferences = getSharedPreferences(data,MODE_PRIVATE);
        switch_button2.setChecked(sharedPreferences.getBoolean(switchField,false));
        window.setChecked(sharedPreferences.getBoolean(switchField1,false));
        action();
        readData();
    }

    public int onCheckedChanged(CompoundButton buttonView, int QQ) {
        switch (buttonView.getId()){
            case R.id.switchCheck:
                if( buttonView.isChecked()){
                    return 1;
                }else{
                    return 0;
                }
            default:
                return 0;
        }
    }

    public void Back(View view){
        intent = new Intent(Set.this,MainActivity.class);
        startActivity(intent);
        Set.this.finish();
    }
    public void ChangeKey(View view){
        intent = new Intent(Set.this,ChangeKey.class);
        startActivity(intent);
    }
    private void createView(){
        myFV.setOnClickListener(this);
        //獲取WindowManager
        //設置LayoutParams(全局變數）相關參數
        wmParams = ((MyApplication)getApplication()).getMywmParams();
        //顯示myFloatView圖像
        wm.addView(myFV, wmParams);
    }

    @Override
    public void onClick(View v) {

        Toast.makeText(this,"click",Toast.LENGTH_LONG).show();
        if(bool){
            final LayoutInflater inflater = LayoutInflater.from(Set.this);
            test = inflater.inflate(R.layout.activity_normal_login_test, null);
            test.setBackgroundResource(R.drawable.bgwhite);
            test.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { //新增按下去之後做的事情
                    Toast.makeText(Set.this, "fab Click! ", Toast.LENGTH_SHORT).show();
                    condition = "add";
                    System.out.println("新增按鈕按下去前的狀態 : " + condition);
                    View item = LayoutInflater.from(Set.this).inflate(R.layout.normal_login_test_login, null);
                    if (addbool) {
                        add = inflater.inflate(R.layout.float_addview, null);
                        wm.addView(add, ((MyApplication) getApplication()).getAddParams());
                        setAddAction();
                        addbool = false;
                    } else {
                        wm.removeView(add);
                        addbool = true;
                    }
                    condition = "Link";
                    System.out.println("新增按鈕按下去後的狀態 : " + condition);
                }

            });
            Toolbar toolbar = test.findViewById(R.id.toolbar);
            toolbar.setBackgroundColor(Color.BLACK);
            setSupportActionBar(toolbar);
            String s2 = "";
            s = getSharedPreferences(LoadData, MODE_PRIVATE)
                    .getString(LoadData, "");
            System.out.println("儲存的東東 : " + s);
            StringTokenizer st = new StringTokenizer(s, ",");
            while (st.hasMoreTokens()) {
                s2 = st.nextToken();
                System.out.println(s2);
                ListViewItemList.add(new ListViewItem(s2));
            }
            condition = "Link";
            Collections.reverse(ListViewItemList);
            //set
            SetListView(test);
            //set

            Params = ((MyApplication) getApplication()).getMyParams();
            wm.addView(test, Params);
            bool = false;
        }else{
            wm.removeView(test);
            ListViewItemList.clear();
            bool = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.normal_login_test_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            condition = "edit";
            System.out.println("Now edit");
            return true;
        }

        if (id == R.id.action_delete) {
            condition = "delete";
            System.out.println("Now delete");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //處理List事件的Adapter
    private class TestListAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;

        public TestListAdapter(Context context){
            layoutInflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount(){
            return ListViewItemList.size();
        }
        @Override
        public Object getItem(int position){
            return ListViewItemList.get(position);
        }
        @Override
        public long getItemId(int position){
            return ListViewItemList.get(position).getId();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if (convertView == null){
                convertView = layoutInflater.inflate(R.layout.listview_item,parent,false);
            }
            ListViewItem listViewItem = ListViewItemList.get(position);
            TextView tvIcon = convertView.findViewById(R.id.tvIcon);
            tvIcon.setText( "" + (ListViewItemList.get(position).getName() ).charAt(0) );

            TextView tvId = (TextView) convertView.findViewById(R.id.tvID);
            tvId.setText( listViewItem.getName() );

            return convertView;
        }
    }

    public void deletelock(View view){
        if(gv.getCode() != null && gv.getonepoint() == false  && gv.getnoenter() == false) {
            SaveUserTrans = gv.getCode();
            SwitchCondition();  //OK
            ShowInformation();  //OK
            CreatePassword();
            ShowPassword();
        }else{
            Toast.makeText(getApplicationContext(), R.string.drawtwopoints , Toast.LENGTH_SHORT).show();
        }
        wm.removeView(lock);
    }

    private void ShowPassword(){
        System.out.println("您的密碼是 : " + Password);
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        myClip = ClipData.newPlainText("text", Password);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
        System.out.println(test());
    }

    //紀錄Url
    public String test(){
        String string = "";
        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(
                Uri.parse("content://browser/bookmarks"), new String[] {
                        "url", "date" }, "date!=?",
                new String[] { "null" }, "date desc");
        String url = null;
        if (cursor != null && cursor.moveToNext()) {

            url = cursor.getString(cursor.getColumnIndex("url"));
        }
        return url;
    }

    //延遲執行
    private TimerTask task = new TimerTask(){
        public void run(){
            //execute the task
            myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            myClip = ClipData.newPlainText("text", " ");
            myClipboard.setPrimaryClip(myClip);
            System.out.println("清除剪貼簿測試");
        }
    };

    //Set List
    public void SetListView(View view){

        //初始化ListView
        lvTest = (ListView) view.findViewById(R.id.lvTest);
        lvTest.setAdapter(new Set.TestListAdapter(this));
        lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent , View view ,int position ,long id){  //點擊item會出現的事件
                ListViewItem listViewItem = (ListViewItem) parent.getItemAtPosition(position);
                change = listViewItem.getName();
                System.out.println("Change : "+change);
                String text = listViewItem.getName();
                getitemname = listViewItem.getName();
                System.out.println("狀態 : " + condition);


                if (condition.equals("Link")){  //進行圖形繪製
                    gv.setinit(false);
                    gv.setOnepoint(false);
                    gv.setnoenter(true);
                    final LayoutInflater inflater = LayoutInflater.from(Set.this);
                    //Window測試------------------------------------------------------------------------------
                    lock = inflater.inflate(R.layout.login, null);
                    lock.setBackgroundResource(R.drawable.bgwhite);
                    wm.addView(lock,Params);
                    //----------------------------------------------------------------------------------------------
                    System.out.println("開始清除剪貼簿");
                    Timer timer = new Timer();
                    timer.schedule(task,40000);
                    System.out.println("清除剪貼簿完成");
                    //------------------------------------------------------------------------------------------------

                }else if (condition.equals("edit")) {
                    View item = LayoutInflater.from(Set.this).inflate(R.layout.normal_login_test_login, null);
                    Toast.makeText(Set.this, ( getString(R.string.change) + text ) , Toast.LENGTH_SHORT).show();
                    if(addbool){
                        add = LayoutInflater.from(Set.this).inflate(R.layout.float_addview,null);
                        wm.addView(add,((MyApplication)getApplication()).getAddParams());
                        setEditAction();
                        addbool = false;
                    }else{
                        wm.removeView(add);
                        addbool = true;
                    }

                }else if ( condition.equals("delete") ){
                    text = listViewItem.getName();

                    //刪除Item    - - - - -
                    s = s + "," + text;     //將資料串在一起
                    String tk;   //暫存每一筆Token
                    String tmp = "";   //暫存刪除後的資料
                    StringTokenizer st = new StringTokenizer(s, ",");

                    while (st.hasMoreTokens()) {
                        tk = st.nextToken();
                        if (tk.equals(text)) {

                        } else {
                            tmp += (","+tk) ;
                            System.out.println("tmp : " + tmp);
                        }
                    }
                    s = tmp;
                    System.out.println("s : " + s);
                    SharedPreferences SaveLoad = getSharedPreferences(LoadData, MODE_PRIVATE);
                    SaveLoad.edit()
                            .putString(LoadData, s)
                            .commit();
                    // - - - - - - - - - - - - - -
                    System.out.println("text : "+text);
                    SharedPreferences DeleteItem = getSharedPreferences(text, MODE_PRIVATE);
                    DeleteItem.edit()
                            .clear()
                            .commit();
                    SharedPreferences DeleteItemUrl = getSharedPreferences(text+"Url", MODE_PRIVATE);
                    DeleteItemUrl.edit()
                            .clear()
                            .commit();
                    SharedPreferences DeleteSwitch = getSharedPreferences(text+"SwitchCondiction", MODE_PRIVATE);
                    DeleteSwitch.edit()
                            .clear()
                            .commit();
                    SharedPreferences DeletePasswordNum = getSharedPreferences(text+"PasswordNumber", MODE_PRIVATE);
                    DeletePasswordNum.edit()
                            .clear()
                            .commit();
                    ListViewItemList.remove( new ListViewItem(text).getName() );
                    Toast.makeText(Set.this, ( getString(R.string.delete) + text ) ,Toast.LENGTH_SHORT).show();
                    condition = "Link";
                    //refresh
                    refresh();

                }

            }
        });
        lvTest.setAdapter(new Set.TestListAdapter(this));
    }

    private void refresh(){
        ListViewItemList.clear();
        String s2 = "";
        s = getSharedPreferences(LoadData, MODE_PRIVATE)
                .getString(LoadData, "");
        System.out.println("儲存的東東 : " + s);
        StringTokenizer st = new StringTokenizer(s, ",");
        while (st.hasMoreTokens()) {
            s2 = st.nextToken();
            System.out.println(s2);
            ListViewItemList.add( new ListViewItem( s2 ) );
        }
        condition = "Link";
        Collections.reverse(ListViewItemList);
        SetListView(test);
    }


    public void action(){
        window.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(window.isChecked()){
                    //如果SwitchButton開啟做點什麼
                    sharedPreferences.edit().putBoolean(switchField1,true).apply();
                    String Value = PreferenceUtil.getValue(Set.this);
                    if (Build.VERSION.SDK_INT >= 23 && Value == "") {
                        if(!Settings.canDrawOverlays(getApplicationContext())) {
                            //啟動Activity讓使用者授權
                            PreferenceUtil.setValue(Set.this,"on");
                            intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                            window.setChecked(false);
                            return;
                        } else {
                            //執行6.0以上繪製程式碼
                        }
                    } else {
                        //執行6.0以下繪製程式碼、
                    }
                    createView();
                    Log.d("句柄","onClick isChecked switchField = is open");
                } else {
                    //如果SwitchButton關閉做某事
                    //儲存
                    sharedPreferences.edit().putBoolean(switchField1,false).apply();
                    wm.removeView(myFV);
                    Log.d("句柄","onClick isClosed switchField = is Closed");
                }
            }
        });
        switch_button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(switch_button2.isChecked()){
                    //如果SwitchButton開啟做點什麼
                    sharedPreferences.edit().putBoolean(switchField,true).apply();
                    Log.d("句柄","onClick isChecked switchField = is open"+switch_button2.isChecked());
                    //check = 1;
                    //GlobalVariable gv = (GlobalVariable)getApplicationContext();
                    gv.setQQ(1);
                } else {
                    //如果SwitchButton關閉做某事
                    //儲存
                    sharedPreferences.edit().putBoolean(switchField,false).apply();
                    Log.d("句柄","onClick isClosed switchField = is Closed"+switch_button2.isChecked());
                    //check = 0;
                    //GlobalVariable gv = (GlobalVariable)getApplicationContext();
                    gv.setQQ(0);
                }
            }
        });
    }
    public void readData(){//讀取
        switch_button2.setChecked(sharedPreferences.getBoolean(switchField,false));
        window.setChecked(sharedPreferences.getBoolean(switchField1,false));
    }

    public void saveData(){//儲存
        sharedPreferences.edit()
                .apply();
    }
    //浮動視窗新增
    public void setAddAction(){//點添加後開啟的View
        final EditText name = add.findViewById(R.id.editName);
        final EditText id  = add.findViewById(R.id.editID);
        final EditText url  = add.findViewById(R.id.editUrl);
        System.out.println("PasswordLength : "+Passwordnumber);
        add.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {//取消鍵
            @Override
            public void onClick(View view) {
                wm.removeView(add);
                addbool = true;
            }
        });
        add.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {//確認鍵
            @Override
            public void onClick(View view) {
                Boolean dotcondiction = false;
                //判定,字元
                for(int i = 0 ; i < name.getText().toString().length() ; i++){
                    if( (name.getText().toString().charAt(i)+"").equals(",") ){
                        dotcondiction = true;
                    }
                }
                //設定儲存
                String namego = name.getText().toString();
                System.out.println("namego : "+namego);
                String idgo = id.getText().toString();
                System.out.println("idgo : "+idgo);
                String urlgo = url.getText().toString();
                System.out.println("urlgo : "+urlgo);
                if( TextUtils.isEmpty(namego) || TextUtils.isEmpty(idgo) ){
                    Toast.makeText(getApplicationContext(), R.string.input_notice2, Toast.LENGTH_SHORT).show();
                }else if(checkbutton == false){
                    Toast.makeText(getApplicationContext(), R.string.password_set2, Toast.LENGTH_SHORT).show();
                }else if(dotcondiction == true){
                    Toast.makeText(getApplicationContext(), R.string.input_notice, Toast.LENGTH_SHORT).show();
                } else {    //如果他兩行都有打並且按確定

                    String text;
                    text = namego;
                    //儲存Item
                    s = s  + (","+text) ;     //將資料串在一起
                    SharedPreferences SaveLoad = getSharedPreferences(LoadData, MODE_PRIVATE);
                    SaveLoad.edit()
                            .putString(LoadData, s)
                            .commit();
                    //加入Item    SaveID
                    SharedPreferences SaveItem = getSharedPreferences(text, MODE_PRIVATE);
                    SaveItem.edit()
                            .putString(text, idgo)
                            .commit();
                    //加入Item    SaveUrl
                    SharedPreferences SaveItemUrl = getSharedPreferences(text+"Url", MODE_PRIVATE);
                    SaveItemUrl.edit()
                            .putString(text+"Url", urlgo)
                            .commit();
                    ListViewItemList.add(0,new ListViewItem(text));
                    Toast.makeText(Set.this, ( getString(R.string.add) + text ), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < ListViewItemList.size(); i++) {
                        System.out.println("List裡有 : " + ListViewItemList.get(i).getName());
                    }
                    SetListView(test);
                }
                wm.removeView(add);
                addbool = true;
            }
        });
        add.findViewById(R.id.pswSet).setOnClickListener(new View.OnClickListener() {//密碼設定
            @Override
            public void onClick(View view) {
                final LayoutInflater inflater = LayoutInflater.from(Set.this);
                Boolean dotcondiction = false;
                checkbutton = true;
                //判定,字元
                for(int i = 0 ; i < name.getText().toString().length() ; i++){
                    if( (name.getText().toString().charAt(i)+"").equals(",") ){
                        dotcondiction = true;
                    }
                }
                //判斷是否有檔案名稱與帳號
                if( TextUtils.isEmpty( name.getText().toString() ) || TextUtils.isEmpty( id.getText().toString() ) ){
                    Toast.makeText(getApplicationContext(), R.string.input, Toast.LENGTH_SHORT).show();
                }else if(dotcondiction == true){
                    Toast.makeText(getApplicationContext(), R.string.input_notice, Toast.LENGTH_SHORT).show();
                }else {
                    name.setFocusable(false);
                    id.setFocusable(false);
                    url.setFocusable(false);
                    passwordSet = inflater.inflate(R.layout.float_pswset, null);
                    passwordSet.setBackgroundResource(R.drawable.bgwhite);
                    setPswAction(name.getText().toString());
                    wm.addView(passwordSet, ((MyApplication) getApplication()).getpswParams());
                }
            }
        });
    }
    //浮動視窗編輯
    public void setEditAction(){//點添加後開啟的View
        final EditText name = add.findViewById(R.id.editName);
        final EditText id  = add.findViewById(R.id.editID);
        final EditText url  = add.findViewById(R.id.editUrl);
        System.out.println("PasswordLength : "+Passwordnumber);
        add.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {//取消鍵
            @Override
            public void onClick(View view) {
                wm.removeView(add);
                addbool = true;
            }
        });

        //讀取儲存的資料   ID
        c = getSharedPreferences(change, MODE_PRIVATE)
                .getString(change, "");
        System.out.println("C : " + c );
        //讀取儲存的資料   Url
        u = getSharedPreferences(change+"Url", MODE_PRIVATE)
                .getString(change+"Url", "");
        System.out.println("U : " + u );
        //將讀取的資料傳進TextView
        name.setText(change);
        id.setText(c);
        url.setText(u);

        final Button set = add.findViewById(R.id.pswSet);
        System.out.println("PasswordLength : "+Passwordnumber);

        //設定密碼設定button事件
        set.setOnClickListener(new View.OnClickListener() {//密碼設定
            @Override
            public void onClick(View view) {
                final LayoutInflater inflater = LayoutInflater.from(Set.this);
                Boolean dotcondiction = false;
                checkbutton = true;
                //判定,字元
                for(int i = 0 ; i < name.getText().toString().length() ; i++){
                    if( (name.getText().toString().charAt(i)+"").equals(",") ){
                        dotcondiction = true;
                    }
                }
                //判斷是否有檔案名稱與帳號
                if( TextUtils.isEmpty( name.getText().toString() ) || TextUtils.isEmpty( id.getText().toString() ) ){
                    Toast.makeText(getApplicationContext(), R.string.input, Toast.LENGTH_SHORT).show();
                }else if(dotcondiction == true){
                    Toast.makeText(getApplicationContext(), R.string.input_notice, Toast.LENGTH_SHORT).show();
                }else {
                    name.setFocusable(false);
                    id.setFocusable(false);
                    url.setFocusable(false);
                    passwordSet = inflater.inflate(R.layout.float_pswset, null);
                    passwordSet.setBackgroundResource(R.drawable.bgwhite);
                    setPswAction(name.getText().toString());
                    wm.addView(passwordSet, ((MyApplication) getApplication()).getpswParams());
                }
            }
        });

        add.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {//確認鍵
            @Override
            public void onClick(View view) {
                Boolean dotcondiction = false;
                //判定,字元
                for(int i = 0 ; i < name.getText().toString().length() ; i++){
                    if( (name.getText().toString().charAt(i)+"").equals(",") ){
                        dotcondiction = true;
                    }
                }
                //設定儲存
                String namego = name.getText().toString();
                System.out.println("namego : "+namego);
                String idgo = id.getText().toString();
                System.out.println("idgo : "+idgo);
                String urlgo = url.getText().toString();
                System.out.println("urlgo : "+urlgo);
                if( TextUtils.isEmpty(namego) || TextUtils.isEmpty(idgo) ){
                    Toast.makeText(getApplicationContext(), R.string.input_notice2, Toast.LENGTH_SHORT).show();
                }else if(dotcondiction == true){
                    Toast.makeText(getApplicationContext(), R.string.input_notice, Toast.LENGTH_SHORT).show();
                } else {    //如果他兩行都有打並且按確定

                    //delete
                    String text;
                    text = change;

                    //刪除Item    - - - - -
                    s = s + "," + text;     //將資料串在一起
                    String tk;   //暫存每一筆Token
                    String tmp = "";   //暫存刪除後的資料
                    StringTokenizer st = new StringTokenizer(s, ",");

                    while (st.hasMoreTokens()) {
                        tk = st.nextToken();
                        if (tk.equals(text)) {

                        } else {
                            tmp += (","+tk) ;
                            System.out.println("tmp : " + tmp);
                        }
                    }
                    s = tmp;
                    System.out.println("s : " + s);
                    SharedPreferences SaveLoad = getSharedPreferences(LoadData, MODE_PRIVATE);
                    SaveLoad.edit()
                            .putString(LoadData, s)
                            .commit();
                    // - - - - - - - - - - - - - -
                    System.out.println("text : "+text);
                    SharedPreferences DeleteItem = getSharedPreferences(text, MODE_PRIVATE);
                    DeleteItem.edit()
                            .clear()
                            .commit();
                    ListViewItemList.remove( new ListViewItem(text).getName() );
                    condition = "Link";
                    //add
                    text = namego;
                    //儲存Item
                    s = s  + (","+text) ;     //將資料串在一起
                    SharedPreferences SaveLoad1 = getSharedPreferences(LoadData, MODE_PRIVATE);
                    SaveLoad1.edit()
                            .putString(LoadData, s)
                            .commit();
                    //加入Item    SaveID
                    SharedPreferences SaveItem = getSharedPreferences(text, MODE_PRIVATE);
                    SaveItem.edit()
                            .putString(text, idgo)
                            .commit();
                    //加入Item    SaveUrl
                    SharedPreferences SaveItemUrl = getSharedPreferences(text+"Url", MODE_PRIVATE);
                    SaveItemUrl.edit()
                            .putString(text+"Url", urlgo)
                            .commit();
                    ListViewItemList.add(new ListViewItem(text));
                    for (int i = 0; i < ListViewItemList.size(); i++) {
                        System.out.println("List裡有 : " + ListViewItemList.get(i).getName());
                    }
                    //refresh
                    refresh();

                }
                wm.removeView(add);
                addbool = true;
            }
        });

    }
    //浮動視窗編輯結束
    //浮動視窗密碼設定
    public void setPswAction(final String name){//點密碼設定後開啟的view
        //init spinner
        final Spinner spNumber = passwordSet.findViewById(R.id.spinner);
        final Integer[] nums = {4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        ArrayAdapter<Integer> adapterspN = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,nums);
        spNumber.setAdapter(adapterspN);
        //spinner 儲存設定

        if(getSharedPreferences(name+"PasswordNumber", MODE_PRIVATE)
                .getString(name+"PasswordNumber","").equals("")||getSharedPreferences(name+"PasswordNumber", MODE_PRIVATE)
                .getString(name+"PasswordNumber","").equals(null)){
            Passwordnumber = 8;
            spNumber.setSelection( (4) ,true);
            SharedPreferences SavePasswordInit = getSharedPreferences(name+"PasswordNumber", MODE_PRIVATE);
            SavePasswordInit.edit()
                    .putString(name+"PasswordNumber", Passwordnumber+"")
                    .commit();
        }else{
            Passwordnumber = Integer.valueOf( getSharedPreferences(name+"PasswordNumber", MODE_PRIVATE)
                    .getString(name+"PasswordNumber","") );
            spNumber.setSelection( (Passwordnumber-4) ,true);
        }

        spNumber.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
                Passwordnumber = nums[pos];
                System.out.println("PasswordLength : "+Passwordnumber);
                SharedPreferences SavePasswordNumber = getSharedPreferences(name+"PasswordNumber", MODE_PRIVATE);
                SavePasswordNumber.edit()
                        .putString(name+"PasswordNumber", Passwordnumber+"")
                        .commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
                Passwordnumber = nums[4];
            }

        });
        System.out.println("PasswordLength : "+Passwordnumber);
        //spinner end--------------------------------------------------------------------------------------------------------------------------------------
        //Button init---------------------------------------------------------------------------------------------------------------------------------------
        final Button SetSpecialWords = passwordSet.findViewById(R.id.SpButton);  //自訂特殊字元
        SetSpecialWords.setEnabled(false);
        SetSpecialWords.setOnClickListener(new View.OnClickListener() {//自訂鍵
            @Override
            public void onClick(View view) {
                try {
                    if(getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                            .getString(name+"UserSpecialWords","").equals("") || getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                            .getString(name+"UserSpecialWords","").equals(null)){
                        SharedPreferences SaveSwitchInit = getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE);
                        SaveSwitchInit.edit()
                                .putString(name+"UserSpecialWords", "00000000000000")
                                .commit();
                    }else{
                        UserSpecialWordsCondiction = getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                                .getString(name+"UserSpecialWords", "");
                        for(int i = 0;i<UserSpecialWords.length;i++){
                            UserSpecialWords[i] = UserSpecialWordsCondiction.charAt(i)+"";
                        }
                    }
                }catch (Exception ex){

                }
                final LayoutInflater inflater = LayoutInflater.from(Set.this);
                Sp = inflater.inflate(R.layout.float_spset, null);
                Sp.setBackgroundResource(R.drawable.bgwhite);
                wm.addView(Sp,((MyApplication)getApplication()).getpswParams());
                setSpAction(name);
            }
        });
        //Button init end----------------------------------------------------------------------------------------------------------------------------------
        //Switch-------------------------------------------------------------------------------------------------------------------------------------------
        //switch concrol        資料儲存方式讀取    檔案 : "name"SwitchCondiction    讀取型態 : 字串  1 為啟動 0 為關閉
        final Switch switch_button3 = passwordSet.findViewById(R.id.switchSpp);  //特殊字元(預設)      第0位
        final Switch switch_button5 = passwordSet.findViewById(R.id.switchCon);  //連續字母(數字)      第1位
        final Switch switch_button6 = passwordSet.findViewById(R.id.switchCapital);  //字母大寫      第2位
        final Switch switch_button7 = passwordSet.findViewById(R.id.switchNum);  //純數字       第3位
        final Switch switch_button8 = passwordSet.findViewById(R.id.switchFen);  //第一碼為英文    第4位
        final Switch switch_button9 = passwordSet.findViewById(R.id.switchFnum);  //第一碼為數字       第5位
        final Switch switch_button10 = passwordSet.findViewById(R.id.switchSpc);  //特殊字元(自訂)       第6位
        //Switch初始化讀取判斷
        try {
            if(getSharedPreferences(name+"SwitchCondiction", MODE_PRIVATE)
                    .getString(name+"SwitchCondiction","").equals("") || getSharedPreferences(name+"SwitchCondiction", MODE_PRIVATE)
                    .getString(name+"SwitchCondiction","").equals(null)){
                SharedPreferences SaveSwitchInit = getSharedPreferences(name+"SwitchCondiction", MODE_PRIVATE);
                SaveSwitchInit.edit()
                        .putString(name+"SwitchCondiction", "0000000")
                        .commit();
            }
        }catch (Exception ex){

        }

        //特殊字元(預設)狀態讀取
        try {
            if ( !(getSharedPreferences(name+"SwitchCondiction", MODE_PRIVATE)
                    .getString(name+"SwitchCondiction", "").charAt(0)+"").equals("1") ){
                CheckSwitchCondictionArray[0] = "0";
            }else {
                CheckSwitchCondictionArray[0] = "1";
            }
        }catch (Exception ex){
            CheckSwitchCondictionArray[0] = "0";
        }
        if (CheckSwitchCondictionArray[0].equals("1")){
            switch_button3.setChecked(true);
        }else{
            switch_button3.setChecked(false);
        }
        //連續字母(數字)狀態讀取
        try {
            if ( !(getSharedPreferences(name+"SwitchCondiction", MODE_PRIVATE)
                    .getString(name+"SwitchCondiction", "").charAt(1)+"").equals("1") ){
                CheckSwitchCondictionArray[1] = "0";
            }else {
                CheckSwitchCondictionArray[1] = "1";
            }
        }catch (Exception ex){
            CheckSwitchCondictionArray[1] = "0";
        }
        if (CheckSwitchCondictionArray[1].equals("1")){
            switch_button5.setChecked(true);
        }else{
            switch_button5.setChecked(false);
        }
        //字母大寫狀態讀取
        try {
            if ( !(getSharedPreferences(name+"SwitchCondiction", MODE_PRIVATE)
                    .getString(name+"SwitchCondiction", "").charAt(2)+"").equals("1") ){
                CheckSwitchCondictionArray[2] = "0";
            }else {
                CheckSwitchCondictionArray[2] = "1";
            }
        }catch (Exception ex){
            CheckSwitchCondictionArray[2] = "0";
        }
        if (CheckSwitchCondictionArray[2].equals("1")){
            switch_button6.setChecked(true);
        }else{
            switch_button6.setChecked(false);
        }
        //純數字狀態讀取
        try {
            if ( !(getSharedPreferences(name+"SwitchCondiction", MODE_PRIVATE)
                    .getString(name+"SwitchCondiction", "").charAt(3)+"").equals("1") ){
                CheckSwitchCondictionArray[3] = "0";
            }else {
                CheckSwitchCondictionArray[3] = "1";
            }
        }catch (Exception ex){
            CheckSwitchCondictionArray[3] = "0";
        }
        if (CheckSwitchCondictionArray[3].equals("1")){
            switch_button7.setChecked(true);
        }else{
            switch_button7.setChecked(false);
        }
        //第一碼為英文狀態讀取
        try {
            if ( !(getSharedPreferences(name+"SwitchCondiction", MODE_PRIVATE)
                    .getString(name+"SwitchCondiction", "").charAt(4)+"").equals("1") ){
                CheckSwitchCondictionArray[4] = "0";
            }else {
                CheckSwitchCondictionArray[4] = "1";
            }
        }catch (Exception ex){
            CheckSwitchCondictionArray[4] = "0";
        }
        if (CheckSwitchCondictionArray[4].equals("1")){
            switch_button8.setChecked(true);
        }else{
            switch_button8.setChecked(false);
        }
        //第一碼為數字狀態讀取
        try {
            if ( !(getSharedPreferences(name+"SwitchCondiction", MODE_PRIVATE)
                    .getString(name+"SwitchCondiction", "").charAt(5)+"").equals("1") ){
                CheckSwitchCondictionArray[5] = "0";
            }else {
                CheckSwitchCondictionArray[5] = "1";
            }
        }catch (Exception ex){
            CheckSwitchCondictionArray[5] = "0";
        }
        if (CheckSwitchCondictionArray[5].equals("1")){
            switch_button9.setChecked(true);
        }else{
            switch_button9.setChecked(false);
        }
        //特殊字元(自訂)狀態讀取
        try {
            if ( !(getSharedPreferences(name+"SwitchCondiction", MODE_PRIVATE)
                    .getString(name+"SwitchCondiction", "").charAt(6)+"").equals("1") ){
                CheckSwitchCondictionArray[6] = "0";
            }else {
                CheckSwitchCondictionArray[6] = "1";
            }
        }catch (Exception ex){
            CheckSwitchCondictionArray[6] = "0";
        }
        if (CheckSwitchCondictionArray[6].equals("1")){
            switch_button10.setChecked(true);
            SetSpecialWords.setEnabled(true);
        }else{
            switch_button10.setChecked(false);
        }
        //顯示Switch狀態
        System.out.print("CheckSwitchCondictionArray : ");
        for(int i = 0;i<CheckSwitchCondictionArray.length;i++){
            System.out.print(CheckSwitchCondictionArray[i]);
        }
        System.out.println();

        //特殊字元(預設)      第0位  連續字母(數字)      第1位  字母大寫      第2位  純數字   第3位
        // 第一碼為英文    第4位  第一碼為數字       第5位  特殊字元(自訂)       第6位
        switch_button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(switch_button3.isChecked()){     //如果SwitchButton開啟做點什麼
                    //儲存Switch狀態為開啟 ( 1 )
                    CheckSwitchCondictionArray[0] = "1";
                    //關閉純數字
                    CheckSwitchCondictionArray[3] = "0";
                    switch_button7.setChecked(false);
                    //關閉特殊字元(自訂)
                    CheckSwitchCondictionArray[6] = "0";
                    switch_button10.setChecked(false);
                    SetSpecialWords.setEnabled(false);

                    Log.d("帥哲","switch特殊字元開啟");
                } else {    //如果SwitchButton關閉做某事
                    //儲存Switch狀態為關閉 ( 0 )
                    CheckSwitchCondictionArray[0] = "0";
                    Log.d("帥哲","switch特殊字元關閉");
                }
            }
        });

        switch_button5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(switch_button5.isChecked()){     //如果SwitchButton開啟做點什麼
                    //儲存Switch狀態為開啟 ( 1 )
                    CheckSwitchCondictionArray[1] = "1";
                    //關閉純數字
                    CheckSwitchCondictionArray[3] = "0";
                    switch_button7.setChecked(false);
                    Log.d("帥哲","switch連續字母(數字)開啟");
                } else {    //如果SwitchButton關閉做某事
                    //儲存Switch狀態為關閉 ( 0 )
                    CheckSwitchCondictionArray[1] = "1";
                    Log.d("帥哲","switch連續字母(數字)關閉");
                }
            }
        });

        switch_button6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(switch_button6.isChecked()){     //如果SwitchButton開啟做點什麼
                    //儲存Switch狀態為開啟 ( 1 )
                    CheckSwitchCondictionArray[2] = "1";
                    //關閉純數字
                    CheckSwitchCondictionArray[3] = "0";
                    switch_button7.setChecked(false);
                    Log.d("帥哲","switch字母大寫開啟");
                } else {    //如果SwitchButton關閉做某事
                    //儲存Switch狀態為關閉 ( 0 )
                    CheckSwitchCondictionArray[2] = "0";
                    Log.d("帥哲","switch字母大寫關閉");
                }
            }
        });

        switch_button7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(switch_button7.isChecked()){     //如果SwitchButton開啟做點什麼
                    //儲存Switch狀態為開啟 ( 1 )
                    CheckSwitchCondictionArray[3] = "1";
                    //關閉其他選項
                    switch_button3.setChecked(false);
                    CheckSwitchCondictionArray[0] = "0";
                    switch_button5.setChecked(false);
                    CheckSwitchCondictionArray[1] = "0";
                    switch_button6.setChecked(false);
                    CheckSwitchCondictionArray[2] = "0";
                    switch_button8.setChecked(false);
                    CheckSwitchCondictionArray[4] = "0";
                    switch_button9.setChecked(false);
                    CheckSwitchCondictionArray[5] = "0";
                    switch_button10.setChecked(false);
                    CheckSwitchCondictionArray[6] = "0";
                   SetSpecialWords.setEnabled(false);
                    Log.d("帥哲","switch純數字開啟");
                } else {    //如果SwitchButton關閉做某事
                    //儲存Switch狀態為關閉 ( 0 )
                    CheckSwitchCondictionArray[3] = "0";
                    Log.d("帥哲","switch純數字關閉");
                }
            }
        });

        switch_button8.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(switch_button8.isChecked()){     //如果SwitchButton開啟做點什麼
                    //儲存Switch狀態為開啟 ( 1 )
                    CheckSwitchCondictionArray[4] = "1";
                    //關閉純數字
                    CheckSwitchCondictionArray[3] = "0";
                    switch_button7.setChecked(false);
                    //關閉第一碼為數字
                    CheckSwitchCondictionArray[5] = "0";
                    switch_button9.setChecked(false);
                    Log.d("帥哲","switch第一碼為英文開啟");
                } else {    //如果SwitchButton關閉做某事
                    //儲存Switch狀態為關閉 ( 0 )
                    CheckSwitchCondictionArray[4] = "0";
                    Log.d("帥哲","switch第一碼為英文關閉");
                }
            }
        });

        switch_button9.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(switch_button9.isChecked()){     //如果SwitchButton開啟做點什麼
                    //儲存Switch狀態為開啟 ( 1 )
                    CheckSwitchCondictionArray[5] = "1";
                    //關閉純數字
                    CheckSwitchCondictionArray[3] = "0";
                    switch_button7.setChecked(false);
                    //關閉第一碼為英文
                    CheckSwitchCondictionArray[4] = "0";
                    switch_button8.setChecked(false);
                    Log.d("帥哲","switch第一碼為數字開啟");
                } else {    //如果SwitchButton關閉做某事
                    //儲存Switch狀態為關閉 ( 0 )
                    CheckSwitchCondictionArray[5] = "0";
                    Log.d("帥哲","switch第一碼為數字關閉");
                }
            }
        });

        switch_button10.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(switch_button10.isChecked()){     //如果SwitchButton開啟做點什麼
                    //將Button變為可用
                    SetSpecialWords.setEnabled(true);
                    //儲存Switch狀態為開啟 ( 1 )
                    CheckSwitchCondictionArray[6] = "1";
                    //關閉純數字
                    CheckSwitchCondictionArray[3] = "0";
                    switch_button7.setChecked(false);
                    //關閉特殊字元(預設)
                    CheckSwitchCondictionArray[0] = "0";
                    switch_button3.setChecked(false);
                } else {    //如果SwitchButton關閉做某事
                    //將Button變為不可用
                    SetSpecialWords.setEnabled(false);
                    //儲存Switch狀態為關閉 ( 0 )
                    CheckSwitchCondictionArray[6] = "0";
                    Log.d("帥哲","switch特殊字元(自訂)關閉");
                }

            }
        });
        //Switch end--------------------------------------------------------------------------------------------------------------------------------------
        passwordSet.findViewById(R.id.Close).setOnClickListener(new View.OnClickListener() {//取消鍵
            @Override
            public void onClick(View view) {
                wm.removeView(passwordSet);
            }
        });
        passwordSet.findViewById(R.id.Next).setOnClickListener(new View.OnClickListener() {//確認鍵
            @Override
            public void onClick(View view) {
                wm.removeView(passwordSet);
                //寫入啟動狀態
                CheckSwitchCondiction = "";
                for(int i = 0;i<CheckSwitchCondictionArray.length;i++){
                    CheckSwitchCondiction += CheckSwitchCondictionArray[i];
                }
                SharedPreferences SaveSwitchCondiction = getSharedPreferences(name+"SwitchCondiction", MODE_PRIVATE);
                SaveSwitchCondiction.edit()
                        .putString(name+"SwitchCondiction", CheckSwitchCondiction)
                        .commit();
            }
        });

    }
    //浮動視窗自訂特殊字元
    public void setSpAction(String name){//點自訂特殊字元後開啟的view
        final CheckBox checkBox1 = Sp.findViewById(R.id.check1);
        final CheckBox checkBox2 = Sp.findViewById(R.id.check2);
        final CheckBox checkBox3 = Sp.findViewById(R.id.check3);
        final CheckBox checkBox4 = Sp.findViewById(R.id.check4);
        final CheckBox checkBox5 = Sp.findViewById(R.id.check5);
        final CheckBox checkBox6 = Sp.findViewById(R.id.check6);
        final CheckBox checkBox7 = Sp.findViewById(R.id.check7);
        final CheckBox checkBox8 = Sp.findViewById(R.id.check8);
        final CheckBox checkBox9 = Sp.findViewById(R.id.check9);
        final CheckBox checkBox10 = Sp.findViewById(R.id.check10);
        final CheckBox checkBox11 = Sp.findViewById(R.id.check11);
        final CheckBox checkBox12 = Sp.findViewById(R.id.check12);
        final CheckBox checkBox13 = Sp.findViewById(R.id.check13);
        final CheckBox checkBox14 = Sp.findViewById(R.id.check14);
        final String SaveName = name;


        //check
        System.out.println((getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "")));
        //checkBox1 狀態確認
        if( (getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "").charAt(0)+"").equals("0") ){
            checkBox1.setChecked(false);
        }else{
            checkBox1.setChecked(true);
        }
        //checkBox2 狀態確認
        if( (getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "").charAt(1)+"").equals("0") ){
            checkBox2.setChecked(false);
        }else{
            checkBox2.setChecked(true);
        }
        //checkBox3 狀態確認
        if( (getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "").charAt(2)+"").equals("0") ){
            checkBox3.setChecked(false);
        }else{
            checkBox3.setChecked(true);
        }
        //checkBox4 狀態確認
        if( (getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "").charAt(3)+"").equals("0") ){
            checkBox4.setChecked(false);
        }else{
            checkBox4.setChecked(true);
        }
        //checkBox5 狀態確認
        if( (getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "").charAt(4)+"").equals("0") ){
            checkBox5.setChecked(false);
        }else{
            checkBox5.setChecked(true);
        }
        //checkBox6 狀態確認
        if( (getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "").charAt(5)+"").equals("0") ){
            checkBox6.setChecked(false);
        }else{
            checkBox6.setChecked(true);
        }
        //checkBox7 狀態確認
        if( (getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "").charAt(6)+"").equals("0") ){
            checkBox7.setChecked(false);
        }else{
            checkBox7.setChecked(true);
        }
        //checkBox8 狀態確認
        if( (getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "").charAt(7)+"").equals("0") ){
            checkBox8.setChecked(false);
        }else{
            checkBox8.setChecked(true);
        }
        //checkBox9 狀態確認
        if( (getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "").charAt(8)+"").equals("0") ){
            checkBox9.setChecked(false);
        }else{
            checkBox9.setChecked(true);
        }
        //checkBox10 狀態確認
        if( (getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "").charAt(9)+"").equals("0") ){
            checkBox10.setChecked(false);
        }else{
            checkBox10.setChecked(true);
        }
        //checkBox11 狀態確認
        if( (getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "").charAt(10)+"").equals("0") ){
            checkBox11.setChecked(false);
        }else{
            checkBox11.setChecked(true);
        }
        //checkBox12 狀態確認
        if( (getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "").charAt(11)+"").equals("0") ){
            checkBox12.setChecked(false);
        }else{
            checkBox12.setChecked(true);
        }
        //checkBox13 狀態確認
        if( (getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "").charAt(12)+"").equals("0") ){
            checkBox13.setChecked(false);
        }else{
            checkBox13.setChecked(true);
        }
        //checkBox14 狀態確認
        if( (getSharedPreferences(name+"UserSpecialWords", MODE_PRIVATE)
                .getString(name+"UserSpecialWords", "").charAt(13)+"").equals("0") ){
            checkBox14.setChecked(false);
        }else{
            checkBox14.setChecked(true);
        }
        // 0為要的1為不要的
        checkBox1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkBox1.isChecked()){
                    UserSpecialWords[0] = "1";
                }else{
                    UserSpecialWords[0] = "0";
                }
            }
        });
        checkBox2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkBox2.isChecked()){
                    UserSpecialWords[1] = "1";
                }else{
                    UserSpecialWords[1] = "0";
                }
            }
        });
        checkBox3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkBox3.isChecked()){
                    UserSpecialWords[2] = "1";
                }else{
                    UserSpecialWords[2] = "0";
                }
            }
        });
        checkBox4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkBox4.isChecked()){
                    UserSpecialWords[3] = "1";
                }else{
                    UserSpecialWords[3] = "0";
                }
            }
        });
        checkBox5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkBox5.isChecked()){
                    UserSpecialWords[4] = "1";
                }else{
                    UserSpecialWords[4] = "0";
                }
            }
        });
        checkBox6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkBox6.isChecked()){
                    UserSpecialWords[5] = "1";
                }else{
                    UserSpecialWords[5] = "0";
                }
            }
        });
        checkBox7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkBox7.isChecked()){
                    UserSpecialWords[6] = "1";
                }else{
                    UserSpecialWords[6] = "0";
                }
            }
        });
        checkBox8.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkBox8.isChecked()){
                    UserSpecialWords[7] = "1";
                }else{
                    UserSpecialWords[7] = "0";
                }
            }
        });
        checkBox9.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkBox9.isChecked()){
                    UserSpecialWords[8] = "1";
                }else{
                    UserSpecialWords[8] = "0";
                }
            }
        });
        checkBox10.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkBox10.isChecked()){
                    UserSpecialWords[9] = "1";
                }else{
                    UserSpecialWords[9] = "0";
                }
            }
        });
        checkBox11.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkBox11.isChecked()){
                    UserSpecialWords[10] = "1";
                }else{
                    UserSpecialWords[10] = "0";
                }
            }
        });
        checkBox12.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkBox12.isChecked()){
                    UserSpecialWords[11] = "1";
                }else{
                    UserSpecialWords[11] = "0";
                }
            }
        });
        checkBox13.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkBox13.isChecked()){
                    UserSpecialWords[12] = "1";
                }else{
                    UserSpecialWords[12] = "0";
                }
            }
        });
        checkBox14.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(checkBox14.isChecked()){
                    UserSpecialWords[13] = "1";
                }else{
                    UserSpecialWords[13] = "0";
                }
            }
        });
        //check end


        Sp.findViewById(R.id.toClose).setOnClickListener(new View.OnClickListener() {//取消鍵
            @Override
            public void onClick(View view) {
                 wm.removeView(Sp);
            }
        });
        Sp.findViewById(R.id.toNext).setOnClickListener(new View.OnClickListener() {//確認鍵
            @Override
            public void onClick(View view) {
                //寫入啟動狀態
                UserSpecialWordsCondiction = "";
                for(int i = 0;i<UserSpecialWords.length;i++){
                    UserSpecialWordsCondiction += UserSpecialWords[i];
                }
                SharedPreferences SaveSpecialWordsCondiction = getSharedPreferences(SaveName+"UserSpecialWords", MODE_PRIVATE);
                SaveSpecialWordsCondiction.edit()
                        .putString(SaveName+"UserSpecialWords", UserSpecialWordsCondiction)
                        .commit();
                //寫入自訂義字元庫
                UserSpecialWordsSaveCondiction = "";
                for(int i = 0;i<UserSpecialWordsSave.length;i++){
                    if (UserSpecialWords[i].equals("0")){
                        UserSpecialWordsSaveCondiction+=UserSpecialWordsSave[i];
                    }
                }
                System.out.println("UserSpecialWordsCondiction : "+UserSpecialWordsSaveCondiction);
                SharedPreferences SaveSpecialWordsSaveCondiction = getSharedPreferences(SaveName+"UserSpecialWordsSave", MODE_PRIVATE);
                SaveSpecialWordsSaveCondiction.edit()
                        .putString(SaveName+"UserSpecialWordsSave", UserSpecialWordsSaveCondiction)
                        .commit();
                wm.removeView(Sp);
            }
        });
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d(handle,"onPause()");
        saveData();
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(handle,"onStop()");
        saveData();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(handle,"onDestroy()");
        saveData();
    }

    //---------------------------------------------------------------以下為密碼設定

    public void SwitchCondition(){

        //檢查特殊字元(預設)開關是否啟動
        try{
            S4Save = ( getSharedPreferences(getitemname+"SwitchCondiction", MODE_PRIVATE)
                    .getString(getitemname+"SwitchCondiction", "") ).charAt(0)+"";
            System.out.println("S4Save : " + S4Save);
        }catch (Exception ex){
            System.out.println(getitemname + " : S4Save :  Crush");
            S4Save = "0";
        }

        if (S4Save.equals("1")){
            ShowCondiction+=" 特殊字元(預設)開啟  ";
        }else{
            ShowCondiction+=" 特殊字元(預設)關閉  ";
        }
        //檢查連續字母開關是否啟動
        try {
            S5Save = ( getSharedPreferences(getitemname+"SwitchCondiction", MODE_PRIVATE)
                    .getString(getitemname+"SwitchCondiction", "") ).charAt(1)+"";
            System.out.println("S5Save : " + S5Save);
        }catch (Exception ex){
            System.out.println(getitemname + " : S5Save :  Crush");
            S5Save = "0";
        }

        if (S5Save.equals("1")){
            ShowCondiction+=" 連續字母(數字)開啟  ";
        }else{
            ShowCondiction+=" 連續字母(數字)關閉  ";
        }
        //檢查字母大寫開關是否啟動
        try{
            S6Save = ( getSharedPreferences(getitemname+"SwitchCondiction", MODE_PRIVATE)
                    .getString(getitemname+"SwitchCondiction", "") ).charAt(2)+"";
            System.out.println("S6Save : " + S6Save);
        }catch (Exception ex){
            System.out.println(getitemname + " : S6Save :  Crush");
            S6Save = "0";
        }

        if (S6Save.equals("1")){
            ShowCondiction+=" 字母大寫開啟  ";
        }else{
            ShowCondiction+=" 字母大寫關閉  ";
        }
        //檢查純數字開關是否啟動
        try{
            S7Save = ( getSharedPreferences(getitemname+"SwitchCondiction", MODE_PRIVATE)
                    .getString(getitemname+"SwitchCondiction", "") ).charAt(3)+"";
            System.out.println("S7Save : " + S7Save);
        }catch (Exception ex){
            System.out.println(getitemname + " : S7Save :  Crush");
            S7Save = "0";
        }

        if (S7Save.equals("1")){
            ShowCondiction+=" 純數字開啟  ";
        }else{
            ShowCondiction+=" 純數字關閉  ";
        }
        //檢查第一碼為英文開關是否啟動
        try{
            S8Save = ( getSharedPreferences(getitemname+"SwitchCondiction", MODE_PRIVATE)
                    .getString(getitemname+"SwitchCondiction", "") ).charAt(4)+"";
            System.out.println("S8Save : " + S8Save);
        }catch (Exception ex){
            System.out.println(getitemname + " : S8Save :  Crush");
            S8Save = "0";
        }

        if (S8Save.equals("1")){
            ShowCondiction+=" 第一碼為英文開啟  ";
        }else{
            ShowCondiction+=" 第一碼為英文關閉  ";
        }
        //檢查第一碼為數字開關是否啟動
        try{
            S9Save = ( getSharedPreferences(getitemname+"SwitchCondiction", MODE_PRIVATE)
                    .getString(getitemname+"SwitchCondiction", "") ).charAt(5)+"";
            System.out.println("S9Save : " + S9Save);
        }catch (Exception ex){
            System.out.println(getitemname + " : S9Save :  Crush");
            S9Save = "0";
        }

        if (S9Save.equals("1")){
            ShowCondiction+=" 第一碼為數字開啟  ";
        }else{
            ShowCondiction+=" 第一碼為數字關閉  ";
        }
        //檢查特殊字元(自訂)開關是否啟動
        try{
            S10Save = ( getSharedPreferences(getitemname+"SwitchCondiction", MODE_PRIVATE)
                    .getString(getitemname+"SwitchCondiction", "") ).charAt(6)+"";
            System.out.println("S10Save : " + S10Save);
        }catch (Exception ex){
            System.out.println(getitemname + " : S10Save :  Crush");
            S10Save = "0";
        }

        if (S10Save.equals("1")){
            //讀取自訂義特殊字元
            UserSpecialWordsCondiction = getSharedPreferences(getitemname+"UserSpecialWordsSave", MODE_PRIVATE)
                    .getString(getitemname+"UserSpecialWordsSave", "");
            System.out.println("UserSpecialWordsCondiction : "+UserSpecialWordsCondiction);
            String User[] = new String[UserSpecialWordsCondiction.length()];
            UserSpecialWords = User;
            for(int i = 0;i<UserSpecialWordsCondiction.length();i++){
                UserSpecialWords[i] = UserSpecialWordsCondiction.charAt(i)+"";
            }
            ShowCondiction+=" 特殊字元(自訂)開啟  ";

        }else{
            ShowCondiction+=" 特殊字元(自訂)關閉  ";
        }

        System.out.println("密碼生成要求 : "+ShowCondiction);
    }

    //判斷switch啟動狀態結束
    public String getIMEI() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
            return imei;
        }catch(Exception e){
            String imei = "778341234560120";
            return imei;
        }
    }
    //顯示使用者資訊
    public void ShowInformation(){
        //使用者圖形轉碼
        System.out.println("您的圖形轉碼為 : " + SaveUserTrans);
        //使用者金鑰
        LoadUserKey = getSharedPreferences("SaveKey", MODE_PRIVATE)
                .getString("SaveUserKey", "");
        System.out.println("您的金鑰為 : " + LoadUserKey);
        //使用者IMEI碼
        SaveIMEITest = getIMEI();
        System.out.println("您的IMEI碼為 : " + SaveIMEITest);
        //使用者ID
        //Get User ID
        SaveUserID = getSharedPreferences(getitemname, MODE_PRIVATE)
                .getString(getitemname, "");
        System.out.println("您的ID為 : "+ SaveUserID);
        PasswordLength();
    }
    public void PasswordLength(){
        Passwordnumber = Integer.valueOf( getSharedPreferences(getitemname+"PasswordNumber", MODE_PRIVATE)
                .getString(getitemname+"PasswordNumber","") );
        System.out.println("您的密碼長度為 : "+Passwordnumber);
    }

    //密碼生成判定
    public void CreatePassword(){

        if(S7Save.equals("1")){   //數字密碼生成

            UTWithout0 = SaveUserTrans.replaceFirst("^0*", "");
            System.out.println("UTWithout0 : " + UTWithout0);
            UKeyToNum = Login.StringToASCIIToInteger(LoadUserKey);
            System.out.println("UKeyToNum : " + UKeyToNum);
            UIDToNum = Login.StringToASCIIToInteger(SaveUserID);
            System.out.println("UIDToNum : " + UIDToNum);
            UIMEIWithout0 = SaveIMEITest.replaceFirst("^0*", "");
            System.out.println("UTWithout0 : " + UTWithout0);
            SaveItemName = Login.StringToASCIIToInteger(getitemname);
            System.out.println(" SaveItemName : " + SaveItemName);
            MulResult = Login.ToBigintegerMUL(UTWithout0 ,(SaveItemName.toString() ),(UIDToNum.toString() ) , (UKeyToNum.toString() ) ,  UIMEIWithout0);
            System.out.println("數字相乘結果 : " + MulResult);
            Password = MulResult.substring(0,Passwordnumber);
            System.out.println("您的密碼 : " + Password);

        }
        else if(S4Save.equals("1")){   //有特殊字元(預設)的密碼生成
            NormalPassword();
            //取位置進行替換
            NumberToChange = Login.ChangeHowMuch(Passwordnumber);
            //擷取字串並回傳數字
            Location = SaveUserTrans.substring(0 , NumberToChange);
            System.out.println("要被改變的位置在轉碼的第" + Location + " 位 : ");
            Password = Login.ChageToSpecial(SaveUserTrans , Password , Location , NumberToChange , Passwordnumber);
            //判斷使否也有開啟字母大小寫篩選
            if (S6Save.equals("1")){
                System.out.println("字母大小寫篩選前的密碼 : " + Password);
                Password = Login.FirstToBigOrSmall(Password);
                System.out.println("字母大小寫篩選後的密碼 : " + Password);
            }
            //判斷使否也有開啟連續字元篩選
            if (S5Save.equals("1")){
                System.out.println("連續字元篩選前的密碼 : " + Password);
                Password = ContinuousWords( Password );
                System.out.println("連續字元篩選後的密碼 : " + Password);
            }
            //判斷是否也有開啟第一碼為英文篩選
            if(S8Save.equals("1")){
                System.out.println("第一碼為英文篩選前的密碼 : " + Password);
                Password = EnglishFirst(Password);
                System.out.println("篩選後的密碼 : " + Password);

            }
            //判斷是否也有開啟第一碼為數字篩選
            if(S9Save.equals("1")){
                System.out.println("第一碼為數字篩選前的密碼 : " + Password);
                Password = NumberFirst(Password);
                System.out.println("篩選後的密碼 : " + Password);

            }
            System.out.println( "密碼含特殊字元結果 : " + Password );

        }
        else if(S10Save.equals("1")){   //有特殊字元(自訂)的密碼生成
            NormalPassword();
            //取位置進行替換
            NumberToChange = Login.ChangeHowMuch(Passwordnumber);
            //擷取字串並回傳數字
            Location = SaveUserTrans.substring(0 , NumberToChange);
            System.out.println("要被改變的位置在轉碼的第" + Location + " 位 : ");
            Password = Login.ChageToSpecialUser(SaveUserTrans , Password , Location , NumberToChange , Passwordnumber,UserSpecialWords);
            //判斷使否也有開啟字母大小寫篩選
            if (S6Save.equals("1")){
                System.out.println("字母大小寫篩選前的密碼 : " + Password);
                Password = Login.FirstToBigOrSmall(Password);
                System.out.println("字母大小寫篩選後的密碼 : " + Password);
            }
            //判斷使否也有開啟連續字元篩選
            if (S5Save.equals("1")){
                System.out.println("連續字元篩選前的密碼 : " + Password);
                Password = ContinuousWords( Password );
                System.out.println("連續字元篩選後的密碼 : " + Password);
            }
            //判斷是否也有開啟第一碼為英文篩選
            if(S8Save.equals("1")){
                System.out.println("第一碼為英文篩選前的密碼 : " + Password);
                Password = EnglishFirst(Password);
                System.out.println("篩選後的密碼 : " + Password);

            }
            //判斷是否也有開啟第一碼為數字篩選
            if(S9Save.equals("1")){
                System.out.println("第一碼為數字篩選前的密碼 : " + Password);
                Password = NumberFirst(Password);
                System.out.println("篩選後的密碼 : " + Password);

            }
            System.out.println( "密碼含特殊字元結果 : " + Password );

        }
        else if(S6Save.equals("1")){   //字母大小寫篩選密碼生成
            NormalPassword();
            System.out.println("字母大小寫篩選前的密碼 : " + Password);
            //判斷是否也有開啟第一碼為英文篩選
            if(S8Save.equals("1")){
                System.out.println("第一碼為英文篩選前的密碼 : " + Password);
                Password = EnglishFirst(Password);
                System.out.println("篩選後的密碼 : " + Password);

            }
            //判斷是否也有開啟第一碼為數字篩選
            if(S9Save.equals("1")){
                System.out.println("第一碼為數字篩選前的密碼 : " + Password);
                Password = NumberFirst(Password);
                System.out.println("篩選後的密碼 : " + Password);

            }
            Password = Login.FirstToBigOrSmall(Password);
            System.out.println("字母大小寫篩選後的密碼 : " + Password);
            if(S5Save.equals("1")){
                Password = ContinuousWords( Password );
                System.out.println("字母大小寫+連續字元篩選後的密碼 : " + Password);
            }
            System.out.println("字母大小寫偵測以 11@FRTCRBAK 為例偵測後的結果 : " + Login.FirstToBigOrSmall("11@FRTCRBAK"));

        }
        else if(S5Save.equals("1")){   //連續字母(數字)篩選密碼生成
            NormalPassword();
            //判斷是否也有開啟第一碼為英文篩選
            if(S8Save.equals("1")){
                System.out.println("第一碼為英文篩選前的密碼 : " + Password);
                Password = EnglishFirst(Password);
                System.out.println("篩選後的密碼 : " + Password);

            }
            //判斷是否也有開啟第一碼為數字篩選
            if(S9Save.equals("1")){
                System.out.println("第一碼為數字篩選前的密碼 : " + Password);
                Password = NumberFirst(Password);
                System.out.println("篩選後的密碼 : " + Password);

            }
            Password = ContinuousWords( Password );
            System.out.println("您的密碼 : " + Password);
            System.out.println("連續字元1234測試 : "+ContinuousWords("1234"));


        }
        else if(S8Save.equals("1")){   //第一碼為英文篩選密碼生成
            NormalPassword();
            System.out.println("您的密碼 : " + Password);
            Password = EnglishFirst(Password);
            System.out.println("篩選後的密碼 : " + Password);

        }
        else if(S9Save.equals("1")){   //第一碼為數字篩選密碼生成
            NormalPassword();
            System.out.println("您的密碼 : " + Password);
            Password = NumberFirst(Password);
            System.out.println("篩選後的密碼 : " + Password);
        }
        else {  //預設密碼生成
            NormalPassword();
            System.out.println("您的密碼 : " + Password);
        }


    }

    //預設密碼生成
    protected String NormalPassword(){

        //將圖形轉碼進行單向雜湊SHA256
        UTSAH256 = Login.shaEncrypt(SaveUserTrans);
        System.out.println("將圖形轉碼進行單向雜湊SHA256 : " + UTSAH256);
        //將使用者ID進行單向雜湊SHA256
        UIDSHA256 = Login.shaEncrypt(SaveUserID);
        System.out.println("將使用者ID進行單向雜湊SHA256 : " + UIDSHA256);
        //SaveItemName
        SaveItemName = StringToASCIIToInteger(getitemname);
        System.out.println("SaveItemName : " + SaveItemName);
        //字串串接 : (順序ㄇ形雜湊+使用者金鑰+使用者ID雜湊+IMEI+自訂名稱轉ASCII的總和)
        ALLPLUS = UTSAH256 + LoadUserKey + UIDSHA256 + SaveIMEITest + SaveItemName;
        System.out.println("字串串接結果 : " + ALLPLUS);
        SHA256result = Login.shaEncrypt(ALLPLUS);
        System.out.println("字串串接SHA256結果 : " + SHA256result);
        Base58result = Login.Base58.encode(SHA256result.getBytes());
        System.out.println("Base58轉碼結果 : " + Base58result);
        Password = Base58result.substring(0, Passwordnumber);
        System.out.println("數字檢測前的密碼 : " + Password);
        Password = FirstToNum(Password);
        System.out.println("數字偵測以 ccdfrtcrbak 為例偵測後的結果 : "+FirstToNum("ccdfrtcrbak"));
        System.out.println("數字檢測後的密碼 : " + Password);

        return Password;
    }

    //特殊字元Method
    //判斷更換的字元數
    public static Integer ChangeHowMuch(Integer i){
        Integer result = 0;

        if( i > 3 && i < 8){
            result = 1;
        }else if( i>7 && i<12){
            result = 2;
        }else if( i > 11 && i<16){
            result = 3;
        }else if( i > 15 && i < 21){
            result = 4;
        }else{

        }
        return result;
    }

    //轉換成特殊字元(預設)
    public static String ChageToSpecial(String UT , String Orignal , String Location , Integer NumberToChange , Integer Passwordnumber){
        Integer UTCount = 0;  //轉碼的index
        Integer PASCount = 0;    //密碼的index
        Integer TOdiv = 0;      //產生密碼位數除以要加入幾個特殊字元
        Integer ToChange = 0;       //計算要換的密碼索引值
        Integer ToChangeCount = 0;  //計算是不是第一次運行
        String ToSpecial = "";      //儲存要換的特殊字元
        Integer Specialindex = 0;   //儲存特殊字元的索引值
        String result = "";
        String[] res = new String[Orignal.length()];  // 01234567
        final String[] spec = {"@","#","$","%","^","&","*","_","+","=","-","?","~","!"};

        TOdiv = Passwordnumber/NumberToChange;
        //將無特殊字元的密碼加入陣列
        res[0] = "";
        for (int i = 0;i<Orignal.length();i++){
            res[i] = "" + Orignal.charAt(i) ;
        }
        //一一找尋要換的索引
        for(int i = 0 ; i < Location.length() ; i++){
            //檢測有沒有超出UT的長度
            if ( ( UTCount + Integer.valueOf( "" + Location.charAt(i) ) ) >= UT.length()){
                UTCount = (UTCount + Integer.valueOf( "" + Location.charAt(i) ) )%UT.length();
            }else{
                UTCount += Integer.valueOf( "" + Location.charAt(i) );
            }
            System.out.println("轉碼的索引 : " + UTCount);
            PASCount = Integer.valueOf(""+UT.charAt(UTCount));
            System.out.println("要換的密碼的索引 : " + PASCount);

            if (ToChangeCount == 0){
                ToChange += (PASCount % TOdiv);
            }else{
                if (PASCount % TOdiv == 0){
                    ToChange += 1;
                }else{
                    ToChange += (PASCount % TOdiv)+1;
                }
            }
            System.out.println("取餘數後的密碼索引 : "+ToChange);
            ToChangeCount++;

            //開始特殊字元轉換
            Specialindex = ((int)(Orignal.charAt(ToChange)))%spec.length;
            System.out.println("Specialindex : "+Specialindex);
            ToSpecial = spec[Specialindex];
            res[ToChange] = ToSpecial;
        }

        for (int i = 0;i<Orignal.length();i++){
            result += res[i];
        }

        return result;
    }

    //轉換成特殊字元(自訂)
    public static String ChageToSpecialUser(String UT , String Orignal , String Location , Integer NumberToChange , Integer Passwordnumber, String[] UserSpecialWords){
        Integer UTCount = 0;  //轉碼的index
        Integer PASCount = 0;    //密碼的index
        Integer TOdiv = 0;      //產生密碼位數除以要加入幾個特殊字元
        Integer ToChange = 0;       //計算要換的密碼索引值
        Integer ToChangeCount = 0;  //計算是不是第一次運行
        String ToSpecial = "";      //儲存要換的特殊字元
        Integer Specialindex = 0;   //儲存特殊字元的索引值
        String result = "";
        String[] res = new String[Orignal.length()];  // 01234567
        final String[] spec = {"@","#","$","%","^","&","*","_","+","=","-","?","~","!"};
        final String[] specUser = UserSpecialWords;

        TOdiv = Passwordnumber/NumberToChange;
        //將無特殊字元的密碼加入陣列
        res[0] = "";
        for (int i = 0;i<Orignal.length();i++){
            res[i] = "" + Orignal.charAt(i) ;
        }
        //一一找尋要換的索引
        for(int i = 0 ; i < Location.length() ; i++){
            //檢測有沒有超出UT的長度
            if ( ( UTCount + Integer.valueOf( "" + Location.charAt(i) ) ) >= UT.length()){
                UTCount = (UTCount + Integer.valueOf( "" + Location.charAt(i) ) )%UT.length();
            }else{
                UTCount += Integer.valueOf( "" + Location.charAt(i) );
            }
            System.out.println("轉碼的索引 : " + UTCount);
            PASCount = Integer.valueOf(""+UT.charAt(UTCount));
            System.out.println("要換的密碼的索引 : " + PASCount);

            if (ToChangeCount == 0){
                ToChange += (PASCount % TOdiv);
            }else{
                if (PASCount % TOdiv == 0){
                    ToChange += 1;
                }else{
                    ToChange += (PASCount % TOdiv)+1;
                }
            }
            System.out.println("取餘數後的密碼索引 : "+ToChange);
            ToChangeCount++;

            //開始特殊字元轉換
            if (UserSpecialWords.length<1){
                Specialindex = ((int)(Orignal.charAt(ToChange)))%spec.length;
                System.out.println("Specialindex : "+Specialindex);
                ToSpecial = spec[Specialindex];
                res[ToChange] = ToSpecial;
            }else{
                Specialindex = ((int)(Orignal.charAt(ToChange)))%specUser.length;
                System.out.println("Specialindex : "+Specialindex);
                ToSpecial = specUser[Specialindex];
                res[ToChange] = ToSpecial;
            }

        }

        for (int i = 0;i<Orignal.length();i++){
            result += res[i];
        }

        return result;
    }

    //連續字元篩選Method
    protected String ContinuousWords(String NeedToChange){
        String res = "";    //儲存最後生成的密碼
        int Length = NeedToChange.length();
        int[] SavePassword = new int[Length];
        String SaveResult[] = new String[Length];
        for (int i = 0;i<Length;i++){
            SavePassword[i] = (int)(NeedToChange.charAt(i));
            SaveResult[i] = ""+(NeedToChange.charAt(i));
        }
        //ASCII與字元對照
        for (int i = 0;i<Length;i++){
            System.out.print(SavePassword[i]+"、");
            System.out.print((char)(SavePassword[i]+1) +"、");
        }
        System.out.println("");
        //判定2、4、6、8、10~18等偶數位
        for (int i = 2 ; i < Length ; i += 2){

            //不能超過密碼長度
            if( i + 1 < Length){

                //若為數字的話
                if ( (SavePassword[i] > 47) && (SavePassword[i] < 58) ){
                    //依序為若3個字元都一樣的時候、若成遞增排序的時候、若成遞減排序的時候
                    if( ( (SavePassword[i-1] ==  SavePassword[i]) && (SavePassword[i] ==  SavePassword[i+1]) ) ||
                            ( (SavePassword[i-1] == SavePassword[i]-1) && (SavePassword[i] ==  SavePassword[i+1]-1) ) ||
                            ( (SavePassword[i-1] == SavePassword[i]+1) && (SavePassword[i] ==  SavePassword[i+1]+1) ) ){

                        SaveResult[i] = "A";

                    }

                }
                //若為英文字母的話
                else if ( ((SavePassword[i] > 64) && (SavePassword[i] < 91)) ||  (SavePassword[i] > 96) && (SavePassword[i] < 123)){
                    //依序為若3個字元都一樣的時候、若成遞增排序的時候、若成遞減排序的時候
                    if( ( (SavePassword[i-1] ==  SavePassword[i]) && (SavePassword[i] ==  SavePassword[i+1]) ) ||
                            ( (SavePassword[i-1] == SavePassword[i]-1) && (SavePassword[i] ==  SavePassword[i+1]-1) ) ||
                            ( (SavePassword[i-1] == SavePassword[i]+1) && (SavePassword[i] ==  SavePassword[i+1]+1) ) ){

                        SaveResult[i] = "1";

                    }
                }

            }
        }
        //將陣列中的字元串接為字串
        for (int i = 0;i<Length;i++){
            res += SaveResult[i];
        }

        return res;
    }

    //數字運算Method
    //String to ASCII   AND   plus all the number to Integer
    public static Integer StringToASCIIToInteger(String s){
        Integer result = 0;
        for(int i = 0; i<s.length();i++){
            result += (int)( s.charAt(i) );
        }
        return result;
    }

    //BigInteger運算
    public static String ToBigintegerMUL(String UT,String UID,String UASCIINUM,String UIMEI){
        String result = "";
        BigInteger n1 = new BigInteger(UT);
        BigInteger n1p = new BigInteger(UID);
        BigInteger n2 = new BigInteger(UASCIINUM);
        BigInteger n3 = new BigInteger(UIMEI);
        n1 = n1.add(n1p);
        result = ( (n1.multiply(n2)).multiply(n3) ).toString() ;

        return result;
    }
    protected String FirstToNum(String s){
        int hasnum = 0;
        for(int i = 0 ; i<s.length() ; i++){
            if ( ((int)( s.charAt(i) ) > 47) && ((int)( s.charAt(i) ) < 58) ){
                hasnum++;
            }
        }
        if (hasnum > 0){
            return s;
        }else{
            return SaveUserTrans.substring(0,1)+s.substring(1,s.length());
        }
    }
    //字母大小寫檢測
    public static String FirstToBigOrSmall(String s){
        String result = "";
        int Big = 0;
        int Small = 0;
        int Bigindex = 0;
        int Smallindex = 0;
        for(int i = 0 ; i<s.length() ; i++){
            if ( ((int)( s.charAt(i) ) > 64) && ((int)( s.charAt(i) ) < 91) ){
                Big++;
                if(Big==1){
                    Bigindex = i;
                }
            }else if( ((int)( s.charAt(i) ) > 96) && ((int)( s.charAt(i) ) < 123) ){
                Small++;
                if (Small == 1){
                    Smallindex = i;
                }
            }
        }
        if (Small == 0){
            result = s.replaceFirst(""+s.charAt(Bigindex),(""+(s.charAt(Bigindex))).toLowerCase() );
        }else if (Big == 0){
            result = s.replaceFirst(""+s.charAt(Smallindex),(""+(s.charAt(Smallindex))).toUpperCase() );
        }else{
            result = s;
        }
        return result;
    }

    //First Char to ASCII  English
    protected String EnglishFirst (String s){
        int cal;
        cal = (int)(s.charAt(0));
        if ( ( (cal > 64) && (cal < 91) ) || ( (cal>96) && (cal < 123) ) ){
            return s;
        }else{
            if( (s.charAt(0)+"").equals("@") ){
                return "A@"+ s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("#") ){
                return "A#"+ s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("$") ){
                return "A$"+ s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("%") ){
                return "A%"+ s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("^") ){
                return "A^"+ s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("&") ){
                return "A&"+ s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("*") ){
                return "A*"+ s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("_") ){
                return "A_"+ s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("+") ){
                return "A+"+ s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("=") ){
                return "A="+ s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("-") ){
                return "A-"+ s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("?") ){
                return "A?"+ s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("~") ){
                return "A~"+ s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("!") ){
                return "A!"+ s.substring(2,s.length());
            }else{
                return "A" + s.substring(1,s.length());
            }
        }
    }

    //First Char to ASCII  Number
    protected String NumberFirst (String s){
        int cal;
        cal = (int)(s.charAt(0));
        if ( (cal > 47) && (cal < 58) ){
            return s;
        }else{
            if( (s.charAt(0)+"").equals("@") ){
                return SaveUserTrans.substring(0,1)+"@"+s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("#") ){
                return SaveUserTrans.substring(0,1)+"#"+s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("$") ){
                return SaveUserTrans.substring(0,1)+"$"+s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("%") ){
                return SaveUserTrans.substring(0,1)+"%"+s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("^") ){
                return SaveUserTrans.substring(0,1)+"^"+s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("&") ){
                return SaveUserTrans.substring(0,1)+"&"+s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("*") ){
                return SaveUserTrans.substring(0,1)+"*"+s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("_") ){
                return SaveUserTrans.substring(0,1)+"_"+s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("+") ){
                return SaveUserTrans.substring(0,1)+"+"+s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("=") ){
                return SaveUserTrans.substring(0,1)+"="+s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("-") ){
                return SaveUserTrans.substring(0,1)+"-"+s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("?") ){
                return SaveUserTrans.substring(0,1)+"?"+s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("~") ){
                return SaveUserTrans.substring(0,1)+"~"+s.substring(2,s.length());
            }else if( (s.charAt(0)+"").equals("!") ){
                return SaveUserTrans.substring(0,1)+"!"+s.substring(2,s.length());
            }else{
                return SaveUserTrans.substring(0,1)+s.substring(1,s.length());
            }
        }
    }

    //  SHA256雜湊開始
    public static String shaEncrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
    //SHA256雜湊結束

    //Base58編碼開始
    public static class Base58 {
        private static final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
                .toCharArray();
        private static final int BASE_58 = ALPHABET.length;
        private static final int BASE_256 = 256;

        private static final int[] INDEXES = new int[128];
        static {
            for (int i = 0; i < INDEXES.length; i++) {
                INDEXES[i] = -1;
            }
            for (int i = 0; i < ALPHABET.length; i++) {
                INDEXES[ALPHABET[i]] = i;
            }
        }

        public static String encode(byte[] input) {
            if (input.length == 0) {
                return "";
            }

            input = copyOfRange(input, 0, input.length);

            int zeroCount = 0;
            while (zeroCount < input.length && input[zeroCount] == 0) {
                ++zeroCount;
            }

            byte[] temp = new byte[input.length * 2];
            int j = temp.length;

            int startAt = zeroCount;
            while (startAt < input.length) {
                byte mod = divmod58(input, startAt);
                if (input[startAt] == 0) {
                    ++startAt;
                }

                temp[--j] = (byte) ALPHABET[mod];
            }

            while (j < temp.length && temp[j] == ALPHABET[0]) {
                ++j;
            }

            while (--zeroCount >= 0) {
                temp[--j] = (byte) ALPHABET[0];
            }

            byte[] output = copyOfRange(temp, j, temp.length);
            return new String(output);
        }

        public static byte[] decode(String input) {
            if (input.length() == 0) {
                return new byte[0];
            }

            byte[] input58 = new byte[input.length()];

            for (int i = 0; i < input.length(); ++i) {
                char c = input.charAt(i);

                int digit58 = -1;
                if (c >= 0 && c < 128) {
                    digit58 = INDEXES[c];
                }
                if (digit58 < 0) {
                    throw new RuntimeException("Not a Base58 input: " + input);
                }

                input58[i] = (byte) digit58;
            }


            int zeroCount = 0;
            while (zeroCount < input58.length && input58[zeroCount] == 0) {
                ++zeroCount;
            }


            byte[] temp = new byte[input.length()];
            int j = temp.length;

            int startAt = zeroCount;
            while (startAt < input58.length) {
                byte mod = divmod256(input58, startAt);
                if (input58[startAt] == 0) {
                    ++startAt;
                }

                temp[--j] = mod;
            }


            while (j < temp.length && temp[j] == 0) {
                ++j;
            }

            return copyOfRange(temp, j - zeroCount, temp.length);
        }

        private static byte divmod58(byte[] number, int startAt) {
            int remainder = 0;
            for (int i = startAt; i < number.length; i++) {
                int digit256 = (int) number[i] & 0xFF;
                int temp = remainder * BASE_256 + digit256;
                number[i] = (byte) (temp / BASE_58);
                remainder = temp % BASE_58;
            }

            return (byte) remainder;
        }

        private static byte divmod256(byte[] number58, int startAt) {
            int remainder = 0;
            for (int i = startAt; i < number58.length; i++) {
                int digit58 = (int) number58[i] & 0xFF;
                int temp = remainder * BASE_58 + digit58;
                number58[i] = (byte) (temp / BASE_256);
                remainder = temp % BASE_256;
            }

            return (byte) remainder;
        }

        private static byte[] copyOfRange(byte[] source, int from, int to) {
            byte[] range = new byte[to - from];
            System.arraycopy(source, from, range, 0, range.length);
            return range;
        }
    }
    //Base58編碼結束

}
