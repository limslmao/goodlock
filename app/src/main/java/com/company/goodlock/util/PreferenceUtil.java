package com.company.goodlock.util;
import android.content.Context;
import android.content.SharedPreferences;
public class PreferenceUtil {
    public static String getKey(Context context){
        SharedPreferences sp = context.getSharedPreferences("gesturePassword", Context.MODE_PRIVATE);
        String key = sp.getString("key", "");
        return key;
    }
    public static void setKey(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences("gesturePassword", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("key", key);
        editor.commit();
    }
    public static String getValue(Context context){
        SharedPreferences sp = context.getSharedPreferences("Permission", Context.MODE_PRIVATE);
        String Value = sp.getString("Value", "");
        return Value;
    }
    public static void setValue(Context context, String Value){
        SharedPreferences sp = context.getSharedPreferences("Permission", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Value", Value);
        editor.commit();
    }
}
