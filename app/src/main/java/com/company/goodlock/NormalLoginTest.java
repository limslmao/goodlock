package com.company.goodlock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class NormalLoginTest extends AppCompatActivity {
    private String condition = "Link";
    private List<ListViewItem> ListViewItemList = new ArrayList<>();
    private String LoadData = "LoadData";
    private String s;
    private String c;    //放UserID用
    private String u = "";    //放UserUrl用
    public String change = "";
    private ListView lvTest;
    private FloatingActionButton fab;
    //Spinner選擇密碼長度
    private Integer Passwordnumber = 8;     //儲存密碼預設為8
    //switch控制用
    private String CheckSwitchCondiction="";
    private String CheckSwitchCondictionArray[] = {"0","0","0","0","0","0","0"};
    //密碼設定控制用
    private String UserSpecialWords[] = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
    private String UserSpecialWordsCondiction="";
    private String UserSpecialWordsSave[] = {"@","#","$","%","^","&","*","_","+","=","-","?","~","!"};
    private String UserSpecialWordsSaveCondiction="";
    private Boolean checkbutton = false;
    public MyApplication gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_login_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gv = (MyApplication) getApplicationContext();

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
        SetListView();
        addnewitem();

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
        SetListView();
    }

    private void addnewitem(){
        fab = findViewById(R.id.fab); //右下角新增按鈕
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //按下去之後做的事情
                condition = "add";
                System.out.println("新增按鈕按下去前的狀態 : "+condition);
                View item = LayoutInflater.from(NormalLoginTest.this).inflate(R.layout.normal_login_test_login, null);
                settingNew(item);
                condition = "Link";
                System.out.println("新增按鈕按下去後的狀態 : "+condition);
            }

        });
    }
    //Set List
    public void SetListView(){

        //初始化ListView
        lvTest = (ListView) findViewById(R.id.lvTest);
        lvTest.setAdapter(new TestListAdapter(this));
        lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent , View view ,int position ,long id){  //點擊item會出現的事件
                ListViewItem listViewItem = (ListViewItem) parent.getItemAtPosition(position);
                change = listViewItem.getName();
                System.out.println("Change : "+change);
                String text = listViewItem.getName();
                System.out.println("狀態 : " + condition);

                if (condition.equals("Link")){  //進行圖形繪製
                    gv.setOnepoint(false);
                    gv.setnoenter(true);
                    Intent intent = new Intent(NormalLoginTest.this,Login.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("PointName",text);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else if (condition.equals("edit")) {
                    View item = LayoutInflater.from(NormalLoginTest.this).inflate(R.layout.normal_login_test_login, null);
                    EditItem(item);
                    Toast.makeText(NormalLoginTest.this,(getString(R.string.change) + text),Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(NormalLoginTest.this, (getString(R.string.delete) + text) ,Toast.LENGTH_SHORT).show();
                    condition = "Link";
                    //refresh
                    refresh();

                }

            }
        });
        lvTest.setAdapter(new TestListAdapter(this));
    }

    //修改資料的呈現
    private void EditItem(View item){
        final EditText name = item.findViewById(R.id.name);
        final EditText id  = item.findViewById(R.id.id);
        final EditText url  = item.findViewById(R.id.url);

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

        final Button set = item.findViewById(R.id.SetButton);
        System.out.println("PasswordLength : "+Passwordnumber);

        //設定密碼設定button事件
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean dotcondiction = false;
                //判定,字元
                for(int i = 0 ; i < name.getText().toString().length() ; i++){
                    if( (name.getText().toString().charAt(i)+"").equals(",") ){
                        dotcondiction = true;
                    }
                }
                //判斷是否有檔案名稱與帳號存在
                if( TextUtils.isEmpty( name.getText().toString() ) || TextUtils.isEmpty( id.getText().toString() ) ){
                    Toast.makeText(getApplicationContext(), R.string.input, Toast.LENGTH_SHORT).show();
                }else if(dotcondiction == true){
                    Toast.makeText(getApplicationContext(), R.string.input_notice, Toast.LENGTH_SHORT).show();
                }else {
                    name.setFocusable(false);
                    id.setFocusable(false);
                    url.setFocusable(false);
                    View item = LayoutInflater.from(NormalLoginTest.this).inflate(R.layout.normal_login_setting, null);
                    PasswordSetting(item , name.getText().toString() );
                }
            }
        });

        new AlertDialog.Builder(NormalLoginTest.this)
                .setTitle(R.string.change_data)
                .setView(item)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { //輸入完OK
                    @Override
                    public void onClick(DialogInterface dialog, int which) {    //確認按鈕
                        Boolean dotcondiction = false;
                        //判定 , 字元
                        for(int i = 0 ; i < name.getText().toString().length() ; i++){
                            if( (name.getText().toString().charAt(i)+"").equals(",") ){
                                dotcondiction = true;
                            }
                        }
                        String namego = name.getText().toString();
                        String idgo = id.getText().toString();
                        String urlgo = url.getText().toString();
                        if( TextUtils.isEmpty(namego) || TextUtils.isEmpty(idgo) ){
                            Toast.makeText(getApplicationContext(), R.string.input_notice2, Toast.LENGTH_SHORT).show();
                        }else if(dotcondiction == true){
                            Toast.makeText(getApplicationContext(), R.string.input_notice, Toast.LENGTH_SHORT).show();
                        }else {    //如果他兩行都有打並且按確定
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
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {     //取消按鈕
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        condition = "Link";
                    }
                })
                .show();
        condition = "Link";
    }

    //新增SettingPassword事件
    private void PasswordSetting(View item, final String name){
        //init spinner
        final Spinner spNumber = item.findViewById(R.id.spNumber);
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
        //spinner end

        final Button SetSpecialWords = item.findViewById(R.id.SetSpecialWords);  //自訂特殊字元
        SetSpecialWords.setEnabled(false);
        SetSpecialWords.setOnClickListener(new View.OnClickListener() {
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
                View item = LayoutInflater.from(NormalLoginTest.this).inflate(R.layout.spectial_words_set_themselves, null);
                SetSpecial(item,name);
            }
        });

        //switch concrol        資料儲存方式讀取    檔案 : "name"SwitchCondiction    讀取型態 : 字串  1 為啟動 0 為關閉
        final Switch switch_button3 = item.findViewById(R.id.switch3);  //特殊字元(預設)      第0位
        final Switch switch_button5 = item.findViewById(R.id.switch5);  //連續字母(數字)      第1位
        final Switch switch_button6 = item.findViewById(R.id.switch6);  //字母大寫      第2位
        final Switch switch_button7 = item.findViewById(R.id.switch7);  //純數字       第3位
        final Switch switch_button8 = item.findViewById(R.id.switch8);  //第一碼為英文    第4位
        final Switch switch_button9 = item.findViewById(R.id.switch9);  //第一碼為數字       第5位
        final Switch switch_button10 = item.findViewById(R.id.switch10);  //特殊字元(自訂)       第6位
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

        new AlertDialog.Builder(NormalLoginTest.this)
                .setTitle(R.string.password_set)
                .setView(item)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { //輸入完OK
                    @Override
                    public void onClick(DialogInterface dialog, int which) {    //確認按鈕
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
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {     //取消按鈕
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    //新增SettingPassword事件結束
    //自訂特殊字元
    private void  SetSpecial(View item,String name){
        final CheckBox checkBox1 = item.findViewById(R.id.checkBox1);
        final CheckBox checkBox2 = item.findViewById(R.id.checkBox2);
        final CheckBox checkBox3 = item.findViewById(R.id.checkBox3);
        final CheckBox checkBox4 = item.findViewById(R.id.checkBox4);
        final CheckBox checkBox5 = item.findViewById(R.id.checkBox5);
        final CheckBox checkBox6 = item.findViewById(R.id.checkBox6);
        final CheckBox checkBox7 = item.findViewById(R.id.checkBox7);
        final CheckBox checkBox8 = item.findViewById(R.id.checkBox8);
        final CheckBox checkBox9 = item.findViewById(R.id.checkBox9);
        final CheckBox checkBox10 = item.findViewById(R.id.checkBox10);
        final CheckBox checkBox11 = item.findViewById(R.id.checkBox11);
        final CheckBox checkBox12 = item.findViewById(R.id.checkBox12);
        final CheckBox checkBox13 = item.findViewById(R.id.checkBox13);
        final CheckBox checkBox14 = item.findViewById(R.id.checkBox14);
        final String SaveName = name;

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

        new AlertDialog.Builder(NormalLoginTest.this)
                .setTitle(R.string.specila_words_set_themselves_1)
                .setView(item)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { //輸入完OK
                    @Override
                    public void onClick(DialogInterface dialog, int which) {    //確認按鈕
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
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {     //取消按鈕
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    //自訂特殊字元結束
    //新增item存取事件
    private void settingNew (View item){    //跳出來讓人可以key資料進去用的menu
        final EditText name = item.findViewById(R.id.name);
        final EditText id  = item.findViewById(R.id.id);
        final EditText url  = item.findViewById(R.id.url);
        final Button set = item.findViewById(R.id.SetButton);
        System.out.println("PasswordLength : "+Passwordnumber);

        //設定密碼設定button事件
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    View item = LayoutInflater.from(NormalLoginTest.this).inflate(R.layout.normal_login_setting, null);
                    PasswordSetting(item , name.getText().toString() );
                }
            }
        });

        new AlertDialog.Builder(NormalLoginTest.this)
                .setTitle(R.string.input_ur_idnurl)
                .setView(item)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { //輸入完OK
                    @Override
                    public void onClick(DialogInterface dialog, int which) {    //確認按鈕
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
                            Toast.makeText(getApplicationContext(), R.string.input, Toast.LENGTH_SHORT).show();
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
                            SharedPreferences SaveItemUrl = getSharedPreferences(text, MODE_PRIVATE);
                            SaveItemUrl.edit()
                                    .putString(text, idgo)
                                    .commit();
                            //加入Item    SaveUrl
                            SharedPreferences SaveItem = getSharedPreferences(text+"Url", MODE_PRIVATE);
                            SaveItem.edit()
                                    .putString(text+"Url", urlgo)
                                    .commit();
                            ListViewItemList.add(0,new ListViewItem(text));
                            Toast.makeText(NormalLoginTest.this, ( getString(R.string.add) + text) , Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < ListViewItemList.size(); i++) {
                                System.out.println("List裡有 : " + ListViewItemList.get(i).getName());
                            }

                            SetListView();

                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {     //取消按鈕
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }
    //新增item存取事件結束

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

}