package com.company.goodlock;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.company.goodlock.view.LockPatternView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

public class Login extends AppCompatActivity {
    private LockPatternView mLockPatternView;
    //For Password Process
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
    private String UserSpecialWords[] = {};
    private String UserSpecialWordsCondiction="";

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
    //連起來
    Intent intent;

    /**
     * 是否是第一次输入密码
     */
    public MyApplication gv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gv = (MyApplication) getApplicationContext();
        gv.setinit(false);
        setContentView(R.layout.activity_login);
        GetItemName();
        initViews();
        initEvents();
    }
    //延遲執行
    private TimerTask task = new TimerTask(){
        public void run(){
            //execute the task
            myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
            myClip = ClipData.newPlainText("text", " ");
            myClipboard.setPrimaryClip(myClip);
            System.out.println("清除剪貼簿");
        }
    };

    //ShowPassword
    private void ShowPassword(){
        System.out.println("您的密碼是 : " + Password);
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        myClip = ClipData.newPlainText("text", Password);
        myClipboard.setPrimaryClip(myClip);

        try{
            Uri uri = Uri.parse(getSharedPreferences(getitemname+"Url", MODE_PRIVATE)
                    .getString(getitemname+"Url", ""));
            intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), R.string.link_error, Toast.LENGTH_SHORT).show();
            System.out.println(ex);
        }

        Timer timer = new Timer();
        timer.schedule(task,40000);

    }

    public void deletelock(View view){
        if(gv.getCode() != null && gv.getonepoint() == false  && gv.getnoenter() == false) {
            SwitchCondition();  //OK
            ShowInformation();  //OK
            CreatePassword();
            ShowPassword();
        }else{
            Toast.makeText(getApplicationContext(), R.string.drawtwopoints , Toast.LENGTH_SHORT).show();
        }
        Login.this.finish();
    }

    //取得item名稱以便後續讀檔
    private void GetItemName(){
        Bundle bundle = getIntent().getExtras();
        getitemname = bundle.getString("PointName");
    }
    //以下為圖形的部分
    private void initViews() {
        mLockPatternView = (LockPatternView) findViewById(R.id.lockPatternView);
    }
    private void initEvents() {
        mLockPatternView.setLockListener(new LockPatternView.OnLockListener() {
            @Override
            public void getStringPassword(String password) {
                gv.setcode(password);
                Log.d("Point2","點的數字："+gv.getCode());
                SaveUserTrans = gv.getCode();
            }

            @Override
            public boolean isPassword() {
                return false;
            }
        });
    }

    //判定Switch啟動狀態  打勾
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
        System.out.println("您的ID為 : "+SaveUserID);
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
        Base58result = Base58.encode(SHA256result.getBytes());
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
    public static String ToBigintegerMUL(String UT,String UNAME,String UID,String UASCIINUM,String UIMEI){
        String result = "";
        BigInteger n1 = new BigInteger(UT);
        BigInteger n1p = new BigInteger(UID);
        BigInteger n2 = new BigInteger(UASCIINUM);
        BigInteger n3 = new BigInteger(UIMEI);
        BigInteger n1p2 = new BigInteger(UNAME);
        n1 = (n1.add(n1p));
        result = ( ( (n1.multiply(n2)).multiply(n3) ).multiply(n1p2) ).toString() ;

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