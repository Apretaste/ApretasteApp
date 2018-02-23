package apretaste.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.example.apretaste.R;
import com.google.gson.Gson;

import apretaste.Comunication.Comunication;
import apretaste.Helper.EmailAddressValidator;
import apretaste.Helper.PrefsManager;
import apretaste.Comunication.http.HttpInfo;
import apretaste.Comunication.http.Httplistener;
import apretaste.Comunication.http.ServiceHtpp;
import apretaste.Comunication.http.SimpleRequest;


public class LoginHttp extends AppCompatActivity implements Httplistener {

    EditText etMail;
     HttpInfo httpInfo;
     Gson gson;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_http);

        gson= new Gson();
        etMail = (EditText) findViewById(R.id.etMail);


        findViewById(R.id.btn_b_http).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginHttp.this,StartActivity.class));
                finish();
            }
        });

        findViewById(R.id.btn_n_http).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EmailAddressValidator.isValidAddress(etMail.getText().toString())){

                    String url =  new Comunication().domain+"start?email="+etMail.getText().toString();
                  // String url =  "http://192.168.137.1/android/start.php?email="+etMail.getText().toString();
                    Log.e("url",url);
                        new SimpleRequest(LoginHttp.this,url,"Cargando",LoginHttp.this).execute();
                    email = etMail.getText().toString();

                }else{
                    etMail.setError("Email incorrecto");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();

        finish();

    }

    @Override
    public void onErrorHttp(VolleyError error) {

    }

    @Override
    public void onResponseSimpleHttp(String response) {
        Log.e("Login-Http",response);
        httpInfo = gson.fromJson(response,HttpInfo.class);
        if (httpInfo.code.equals("ok")){
            startActivity(new Intent(LoginHttp.this,CodeVerificationActivity.class));
            new PrefsManager().saveData("email",LoginHttp.this,email);
            finish();


        }
    }

    @Override
    public void onResponseArrivedHttp(String service, String command, String response, ServiceHtpp serviceHtpp) {

    }


}
