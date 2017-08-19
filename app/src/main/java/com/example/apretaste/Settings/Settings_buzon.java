package com.example.apretaste.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apretaste.LoginActivity;
import com.example.apretaste.MainActivity;
import com.example.apretaste.NotificationsActivity;
import com.example.apretaste.PrefsManager;
import com.example.apretaste.PrefsWatcher;
import com.example.apretaste.R;
import com.example.apretaste.RecentActivity;

public class Settings_buzon extends AppCompatActivity {

    EditText et_buzon;

    Context context;
    PrefsManager prefsManager = new PrefsManager();

    SettingsActivity settingsActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_buzon);
        et_buzon = (EditText) findViewById(R.id.et_buzon);
        // PrefsWatcher.bindWatcher(this,et_buzon,"mailbox","app@mailgun.apretaste.com");

        prefsManager.show_value(this , et_buzon,"mailbox","app@mailgun.apretaste.com");

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_buzon.getText().toString().isEmpty()){
                    Toast.makeText(getBaseContext(),"Rellene el campo del buzón ",Toast.LENGTH_SHORT).show();

                }else{
                    prefsManager.change_value(Settings_buzon.this ,"mailbox" , et_buzon);
                    Toast.makeText(getBaseContext(),"Se han guardado los cambios",Toast.LENGTH_SHORT).show();

                }

            }
        });





    }

}
