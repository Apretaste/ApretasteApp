package apretaste.ui;

import android.content.Intent;
import android.content.SharedPreferences;
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

    DbHelper db;
     HttpInfo httpInfo;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);

        db=DbHelper.getSingleton(this);
        httpInfo = new HttpInfo();
        gson = new Gson();
        btn_code = (Button) findViewById(R.id.btn_code);
        et_code = (EditText) findViewById(R.id.et_code);

        findViewById(R.id.btn_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_code.getText().toString().equals("")){


                    et_code.setError("Entre el código de verificacion");

                }else if (et_code.getText().toString().length() > 4){
                    et_code.setError("El código de verificacion solo permite 4 numeros");
                }else{
                    String email = new PrefsManager().getData("email",CodeVerificationActivity.this);
                   // String url = new Comunication().domain+"auth?email="+email+"&pin="+et_code.getText().toString()+"+&appname=apretaste&platform=android";

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CodeVerificationActivity.this);
                    String urlsaved = preferences.getString("domain", "cubaworld.info");
                    String url = "http://"+urlsaved+"/api/auth?email="+email+"&pin="+et_code.getText().toString()+"+&appname=apretaste&platform=android";
                    Log.e("url",url);


                    SimpleHttp simpleHttp = new SimpleHttp(CodeVerificationActivity.this,url,CodeVerificationActivity.this);
                    simpleHttp.setMessageDialog("Verificando");
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
        Log.e("respuesta-code",response);

        httpInfo = gson.fromJson(response,HttpInfo.class);

       //Log.e("token",httpInfo.message);
        if (httpInfo.code.equals("ok")){
            new PrefsManager().saveData("token",CodeVerificationActivity.this,httpInfo.token);
            MultipartHttp multipartHttp =   new MultipartHttp(CodeVerificationActivity.this,"perfil status","perfil status",false,"texto help",CodeVerificationActivity.this);
            multipartHttp.setReturnContent(true);
            multipartHttp.setSaveInternal(true);
            multipartHttp.execute();

        }else{
            Toast.makeText(this, "Codigo de verificacion incorrecto", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CodeVerificationActivity.this,LoginHttp.class));
        }


    }

    @Override
    public void onResponseArrivedHttp(String service, String command, String response,MultipartHttp multipartHttp) {

        new PrefsManager().saveData("type_conn",CodeVerificationActivity.this,"internet");
        new PrefsManager().saveData("mailbox",CodeVerificationActivity.this,"alexandergiogustino+ap@gmail.com");
        Log.e("res",response);
        PreferenceManager.getDefaultSharedPreferences(CodeVerificationActivity.this).edit().putString      (LoginActivity.RESP,response).apply();

        ProfileInfo profileInfo;
        profileInfo = gson.fromJson(response, ProfileInfo.class);
        db.addService(profileInfo.services);
        new PrefsManager().saveData("type_img", CodeVerificationActivity.this, profileInfo.img_quality);
        db.addNotification(profileInfo.notifications);
        startActivity(new Intent(CodeVerificationActivity.this, DrawerActivity.class));
        finish();
    }


}
