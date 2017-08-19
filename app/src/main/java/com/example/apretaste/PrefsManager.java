package com.example.apretaste;

import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.EditText;
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

    public  void change_value_string(final  Context ctx , final String prefName , final String value){
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(prefName,value).apply();

    }
}
