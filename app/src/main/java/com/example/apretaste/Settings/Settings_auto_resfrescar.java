package com.example.apretaste.Settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.apretaste.LoginActivity;
import com.example.apretaste.MainActivity;
import com.example.apretaste.NotificationsActivity;
import com.example.apretaste.ProfileActivity;
import com.example.apretaste.R;
import com.example.apretaste.RecentActivity;

public class Settings_auto_resfrescar extends AppCompatActivity {

    public static final String CAMBIOS_REALIZADOS_CORRECTAMENTE = "Cambios Realizados Correctamente";
    public static final String PREF_AUTO_RESFRESCADO = "pref_auto_resfrescado";
    public static final String PREF = "pref";
    public static final String RELLENE_EL_CAMPO = "Rellene el campo";
    public static final String TIEMPO_DE_AUTO_RESFRESCADO = "Tiempo de Auto Resfrescado";
    public static final String CADA_6_HORAS = "Cada 6 horas";
    public static final String CADA_5_HORAS = "Cada 5 horas";
    public static final String CADA_4_HORAS = "Cada 4 horas";
    public static final String CADA_3_HORAS = "Cada 3 horas";
    public static final String CADA_2_HORAS = "Cada 2 horas";
    public static final String CADA_1_HORA = "Cada 1 hora";
    public static final String CADA_30_MINUTOS = "Cada 30 minutos";
    ImageButton back_auto;
    Button btn_save;
    EditText et_time;
    Context context;
    SettingsActivity settingsActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_auto_resfrescar);
        final CharSequence[] timeOptions=new CharSequence[]{CADA_30_MINUTOS, CADA_1_HORA, CADA_2_HORAS, CADA_3_HORAS, CADA_4_HORAS, CADA_5_HORAS, CADA_6_HORAS};
        View.OnClickListener timeListener=new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(Settings_auto_resfrescar.this).setItems(timeOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText)v).setText(timeOptions[which]);
                    }
                }).setTitle(TIEMPO_DE_AUTO_RESFRESCADO).show();
            }


        };
        findViewById(R.id.et_time).setOnClickListener(timeListener);

        et_time = (EditText) findViewById(R.id.et_time);


        settingsActivity = new SettingsActivity();
        final String text = settingsActivity.getPref_value(this , PREF, PREF_AUTO_RESFRESCADO);
            if(!text.equals("")) {
                et_time.setText(text);
            }






    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!et_time.getText().toString().equals("")){
            Toast.makeText(getBaseContext(), RELLENE_EL_CAMPO,Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPreferences = getSharedPreferences(PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PREF_AUTO_RESFRESCADO,et_time.getText().toString());
            editor.apply();
            Toast.makeText(getBaseContext(), CAMBIOS_REALIZADOS_CORRECTAMENTE,Toast.LENGTH_SHORT).show();
        }
    }
}
