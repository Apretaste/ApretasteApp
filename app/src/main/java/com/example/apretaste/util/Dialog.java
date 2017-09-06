package com.example.apretaste.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by cjam on 8/21/2017.
 */

public class Dialog {



    public void simpleAlert(Context context , String title, String msg){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setNegativeButton("OK", null)
                .setCancelable(false)
                .show();
    }


}
