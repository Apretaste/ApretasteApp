package com.example.apretaste;

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

        public void haveConn(final Context context , String msg){
            ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo i = conManager.getActiveNetworkInfo();
            if ((i == null)||(!i.isConnected())||(!i.isAvailable())) {
                new AlertDialog.Builder(context)
                        .setTitle("Error de Conexion")
                        .setMessage(msg)
                        /*.setPositiveButton("Ir Ajustes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                context.startActivity(new Intent(Settings.ACTION_SETTINGS));
                            }
                        })*/
                        .setNegativeButton("OK", null)
                        .setCancelable(false)
                        .show();
            }
        }
    }

