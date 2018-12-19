package apretaste.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.apretaste.R;
import com.google.gson.Gson;

import apretaste.Comunication.ServicePsiphon;
import apretaste.Comunication.http.SimpleHttp;
import apretaste.Helper.DbHelper;
import apretaste.Helper.PrefsManager;
import apretaste.ProfileInfo;
import apretaste.Comunication.http.HttpInfo;
import apretaste.Comunication.http.Httplistener;
import apretaste.Comunication.http.MultipartHttp;


public class CodeVerificationActivity extends AppCompatActivity implements Httplistener {
    Button btn_code;
    EditText et_code;
    ServicePsiphon servicePsiphon;
    boolean mBound = false;
    DbHelper db;
    HttpInfo httpInfo;
    Gson gson;
    int port;

    PrefsManager prefsManager;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);

        prefsManager = new PrefsManager();
        db = DbHelper.getSingleton(this);
        httpInfo = new HttpInfo();
        gson = new Gson();
        btn_code = findViewById(R.id.btn_code);
        et_code = findViewById(R.id.et_code);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        findViewById(R.id.btn_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_code.getText().toString().equals("")) {
                    et_code.setError("Entre el código de verificacion");

                } else if (et_code.getText().toString().length() > 4) {
                    et_code.setError("El código de verificacion solo permite 4 numeros");
                } else {

                    String urlsaved = "apretaste.com";
                    String url = "https://" + urlsaved + "/api/auth?email=" + email + "&pin=" + et_code.getText().toString() + "+&appname=apretaste&platform=android";

                    SimpleHttp simpleHttp = new SimpleHttp(CodeVerificationActivity.this, url, CodeVerificationActivity.this);
                    port = servicePsiphon.getmLocalHttpProxyPort();
                    simpleHttp.execute();


                }
            }
        });


    }


    @Override
    public void onErrorHttp(String error) {

    }

    @Override
    public void onResponseSimpleHttp(String response) {
        Log.e("respuesta-code", response);

        Log.e("port code verification", String.valueOf(servicePsiphon.getmLocalHttpProxyPort()));
        httpInfo = gson.fromJson(response, HttpInfo.class);

        if (httpInfo.code.equals("ok")) {
            new PrefsManager().saveData("token", CodeVerificationActivity.this, httpInfo.token);

            final MultipartHttp multipartHttp = new MultipartHttp(CodeVerificationActivity.this, "status", "status", false, "texto help", CodeVerificationActivity.this);
            multipartHttp.setReturnContent(true);
            multipartHttp.setSaveInternal(true);

            // multipartHttp.setPortProxy(servicePsiphon.getmLocalHttpProxyPort());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    multipartHttp.execute();
                }
            });


        } else {
            Toast.makeText(this, "Codigo de verificacion incorrecto", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(CodeVerificationActivity.this, LoginHttp.class));
        }


    }

    @Override
    public void onResponseArrivedHttp(String service, String command, String response, MultipartHttp multipartHttp) {

        ProfileInfo profileInfo;
        profileInfo = gson.fromJson(response, ProfileInfo.class);

        db.addService(profileInfo.services);

        for (int i = 0; i < profileInfo.notifications.length; i++) {
            int count = new PrefsManager().readInt(CodeVerificationActivity.this, "count_noti");
            new PrefsManager().saveInt(CodeVerificationActivity.this, "count_noti", count + 1);
        }

        prefsManager.SaveSettingsApp(CodeVerificationActivity.this, profileInfo);
        prefsManager.saveBoolean(CodeVerificationActivity.this, "login", true);
        prefsManager.saveData("type_conn", CodeVerificationActivity.this, "internet");

        startActivity(new Intent(CodeVerificationActivity.this, DrawerActivity.class));
        finish();
    }

    @Override
    public void onHttpSent() {

    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ServicePsiphon.ServicePsiphonBinder binder = (ServicePsiphon.ServicePsiphonBinder) service;
            servicePsiphon = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(CodeVerificationActivity.this, ServicePsiphon.class);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

}
