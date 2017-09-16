package com.example.apretaste.Settings;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apretaste.HistoryManager;
import com.example.apretaste.LoginActivity;
import com.example.apretaste.NotificationsActivity;
import com.example.apretaste.R;
import com.example.apretaste.RecentActivity;

import java.io.File;

public class SettingsActivity extends AppCompatActivity {
    public static final String CERRAR_SESSION = "Cerrar Sessión";
    public static final String CANCELAR = "Cancelar";
    public static final String ACEPTAR = "Aceptar";
    public static final String SI_TERMINA_SU_SESION_NO_RECIBIRA_MAS_NOTIFICACIONES_Y_OTROS_USUARIOS_NO_PODRAN_CONTACTARLE = "Si termina su sesión no recibira más notificaciones, y otros usuarios no podrán contactarle.";
    LinearLayout ll_about,ll_config_nauta,ll_buzon,ll_time,ll_cerrar_sesion;
    ImageButton back_list;
   // TextView time;

    public static boolean terminating=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_list);
        /*time = (TextView) findViewById(R.id.tv_time);
        final String text = getPref_value(this , "pref" , "pref_auto_resfrescado").replace("Cada","");
        if(!text.equals("")) {
            time.setText(text);
        }*/
    }

    public void open_activitys(View view) {
        switch (view.getId()){
            case R.id.ll_about:
                startActivity(new Intent(SettingsActivity.this, Settings_about.class ));
                break;
            case R.id.ll_config_nauta:
                startActivity(new Intent(SettingsActivity.this, Settings_nauta.class ));
                break;
            case R.id.ll_buzon:
                startActivity(new Intent(SettingsActivity.this, Settings_buzon.class ));
                break;
            case R.id.ll_cerrar_sesion:
            {
                new AlertDialog.Builder(this).setMessage(SI_TERMINA_SU_SESION_NO_RECIBIRA_MAS_NOTIFICACIONES_Y_OTROS_USUARIOS_NO_PODRAN_CONTACTARLE).setPositiveButton(ACEPTAR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this).edit().clear().apply();
                        terminating=true;
                        HistoryManager.getSingleton().entries.clear();
                        delete(getCacheDir(),false);
                        delete(getFilesDir(),false);
                        delete(getExternalFilesDir(null),false);
                        finish();
                    }
                }).setNegativeButton(CANCELAR,null).show();
            }
        }}

    /*Devuelve el valor de alguna preferencia*/
    public String getPref_value(Context context ,String PREFS_NAME , String PREFS_KEY ){
        String text;
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME , Context.MODE_PRIVATE);
        text = settings.getString(PREFS_KEY,"");
        return text;

    }

    public static void delete(File file, boolean deleteDir)
    {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    delete(f, true);
                }
            }
            if (deleteDir) {
                file.delete();
            }
        } else {
            file.delete();
        }
    }
}



