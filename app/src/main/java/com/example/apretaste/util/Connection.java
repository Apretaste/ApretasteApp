package com.example.apretaste.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by cjam on 8/20/2017.
 */

public class Connection {

        public boolean haveConn(final Context context){
            ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo i = conManager.getActiveNetworkInfo();
            if ((i == null)||(!i.isConnected())||(!i.isAvailable())) {
               return false;
            }
            return true;
        }
    }

