package apretaste.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.apretaste.R;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Timer;
import java.util.TimerTask;

import apretaste.Comunication.ServicePsiphon;
import apretaste.Comunication.http.SimpleHttp;
import apretaste.Helper.DialogHelper;
import apretaste.Helper.EmailAddressValidator;
import apretaste.Comunication.http.HttpInfo;
import apretaste.Comunication.http.Httplistener;
import apretaste.Comunication.http.MultipartHttp;


public class LoginHttp extends AppCompatActivity implements Httplistener {
    ServicePsiphon servicePsiphon;
    boolean mBound = false;
    EditText etMail;
    HttpInfo httpInfo;
    Gson gson;
    String email;
    ImageButton btn_b_http;
    KProgressHUD dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_http);
        btn_b_http = findViewById(R.id.btn_b_http);


        final Timer t = new Timer();

        dialog = new DialogHelper().DialogRequest(LoginHttp.this);
        dialog.show();
        t.scheduleAtFixedRate(new TimerTask() {
                                  @Override
                                  public void run() {
                                      if (servicePsiphon.isConnected()) {
                                          runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {

                                                  dialog.dismiss();
                                                  t.purge();
                                              }
                                          });

                                      }
                                  }
                              },
                0,

                3000);

        startService(new Intent(this, ServicePsiphon.class));
        gson = new Gson();
        etMail = findViewById(R.id.etMail);


        btn_b_http.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginHttp.this, StartActivity.class));
                finish();

            }
        });

        findViewById(R.id.btn_n_http).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EmailAddressValidator.isValidAddress(etMail.getText().toString())) {


                    String urlsaved = "apretaste.com";
                    String url = "https://" + urlsaved + "/api/start?email=" + etMail.getText().toString();
                    Log.e("url", url);
                    SimpleHttp simpleHttp = new SimpleHttp(LoginHttp.this, url, LoginHttp.this);
                    simpleHttp.execute();

                    email = etMail.getText().toString();


                } else {
                    etMail.setError("Email incorrecto");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onErrorHttp(String error) {

    }

    @Override
    public void onResponseSimpleHttp(String response) {
        Log.e("Login-Http", response);
        httpInfo = gson.fromJson(response, HttpInfo.class);
        if (httpInfo.code.equals("ok")) {

            Intent intent = new Intent(LoginHttp.this, CodeVerificationActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);

            finish();


        }
    }

    @Override
    public void onResponseArrivedHttp(String service, String command, String response, MultipartHttp multipartHttp) {

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
        // Bind to LocalService

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginHttp.this, ServicePsiphon.class);
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
