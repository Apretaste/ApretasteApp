package com.example.apretaste.util;

import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


/**
 * Created by CJAM on 27/7/2017.
 */

public class PrefsManager {
    public  void show_value(final Context ctx , EditText editText,final String prefName,String defaultValue){
        editText.setText(PreferenceManager.getDefaultSharedPreferences(ctx).getString(prefName,defaultValue));

    }

    public  void change_value(final  Context ctx , final String prefName , EditText editText){
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(prefName,editText.getText().toString()).apply();

    }

    public String getData(String key,Context ctx){
        return  PreferenceManager.getDefaultSharedPreferences(ctx).getString(key,"");
    }

    public void saveData(String key, Context ctx, String value){
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(key,value).apply();

    }





}
