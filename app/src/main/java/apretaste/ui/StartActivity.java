package apretaste.ui;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.apretaste.R;
import com.facebook.stetho.Stetho;


import apretaste.Helper.PrefsManager;

import static apretaste.ui.LoginActivity.RESP;


public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Stetho.initializeWithDefaults(this);

        ///Comprueba que si ya hay datos de un usuario , si hay entra directo al main
        if (new PrefsManager().getBoolean(StartActivity.this,"login")) {
            startActivity(new Intent(StartActivity.this, DrawerActivity.class));//e inicia la activity principal
            finish();
            return;
        }


        findViewById(R.id.btn_wd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, LoginHttp.class));

                finish();


            }
        });

        findViewById(R.id.btn_na).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));

                finish();

            }
        });
    }

}
