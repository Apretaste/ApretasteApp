package apretaste.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apretaste.R;
import com.google.gson.Gson;

import apretaste.Helper.DbHelper;
import apretaste.Helper.PrefsManager;
import apretaste.Comunication.email.Mailer;
import apretaste.Comunication.email.Mailerlistener;
import apretaste.Helper.NetworkHelper;
import apretaste.Helper.AlertHelper;
import apretaste.Helper.EmailAddressValidator;
import apretaste.Helper.PrefsWatcher;
import apretaste.ProfileInfo;

import java.util.Random;

public class LoginActivity extends AppCompatActivity implements Mailerlistener {


    public static final String RESP = "resp";
    public static final String OK = "Ok";
    public static final String ERROR = "Error";
    public static final String OPCIONES_DE_SEGURIDAD = "Opciones de seguridad";
    public static final String SIN_SEGURIDAD = "Sin seguridad";
    public static final String SSL = "SSL";
    public static final String EMAIL_NO_VÁLIDO = "Email no válido.";
    public static final String ESTAMOS_CARGANDO_SU_PERFIL_ESTE_PROCESO_DEBE_TOMAR_VARIOS_MINUTOS_POR_FAVOR_SEA_PACIENTE_Y_NO_CIERRE_LA_APLICACION = "Estamos cargando su perfil. Este proceso debe tomar varios minutos. Por favor, sea paciente y no cierre la aplicación.";
    public static final String PERFIL_STATUS = "perfil status";
    public static final String MAILBOX = "mailbox";
    public static final String SMTP_SSL = "smtp_ssl";
    public static final String SMTP_PORT = "smtp_port";
    public static final String SMTP_NAUTA_CU = "smtp.nauta.cu";
    public static final String SMTP_SERVER = "smtp_server";
    public static final String IMAP_SSL = "imap_ssl";
    public static final String IMAP_PORT = "imap_port";
    public static final String IMAP_NAUTA_CU = "imap.nauta.cu";
    public static final String IMAP_SERVER = "imap_server";
    public static final String PASS = "pass";
    public static final String USER = "user";

    int countClick  = 0;
    Context context;
    NetworkHelper networkHelper = new NetworkHelper();
    AlertHelper alertHelper = new AlertHelper();

    DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///Comprueba que si ya hay datos de un usuario , si hay entra directo al main
           if(PreferenceManager.getDefaultSharedPreferences(this).getString(RESP,null) !=null) {
            startActivity(new Intent(LoginActivity.this, DrawerActivity.class));//e inicia la activity principal
           finish();
            return; }


        db=DbHelper.getSingleton(this);
        //Carga el layout de esta activity
        setContentView(R.layout.activity_login);

        //obtenemos las views que vamos a utilizar para ponerles los eventos
        final EditText user=(EditText)findViewById(R.id.mailfield);
        final EditText pass=(EditText)findViewById(R.id.passfield);
        final EditText imap_server=(EditText)findViewById(R.id.imap_server);
        final EditText smtp_server=(EditText)findViewById(R.id.smtp_server);
        final EditText imap_port=(EditText)findViewById(R.id.imap_port);
        final EditText smtp_port=(EditText)findViewById(R.id.smtp_port);
        final EditText imap_ssl=(EditText)findViewById(R.id.imap_ssl);
        final EditText custom_mailbox = (EditText) findViewById(R.id.mailbox_custom);
        final EditText smtp_ssl=(EditText)findViewById(R.id.smtp_ssl);
        final ImageView logo = (ImageView) findViewById(R.id.logo);



        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countClick+=1;
                if(countClick==3){
                    findViewById(R.id.ll_mailbox_custom).setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "Ahora puede escribir su buzón personal", Toast.LENGTH_SHORT).show();
                }
                Log.e("countClick", String.valueOf(countClick));
            }
        });


        PrefsWatcher.bindWatcher(this,user, USER,"");
        PrefsWatcher.bindWatcher(this,pass, PASS,"");
        PrefsWatcher.bindWatcher(this,imap_server, IMAP_SERVER, "imap.nauta.cu");
        PrefsWatcher.bindWatcher(this,imap_port, IMAP_PORT,"143");
        PrefsWatcher.bindWatcher(this,imap_ssl, IMAP_SSL, SIN_SEGURIDAD);
        PrefsWatcher.bindWatcher(this,smtp_server, SMTP_SERVER, "smtp.nauta.cu");
        PrefsWatcher.bindWatcher(this,smtp_port, SMTP_PORT,"25");
        PrefsWatcher.bindWatcher(this,smtp_ssl, SMTP_SSL, SIN_SEGURIDAD);

        final View hidenLayout=findViewById(R.id.hiden_layout);
        final ImageButton togleButton=(ImageButton)findViewById(R.id.togle_button);

        findViewById(R.id.btn_back_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,StartActivity.class));
                finish();
            }
        });


        //Boton siguiente
        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EmailAddressValidator.isValidAddress(user.getText()))
                {

                 PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString(MAILBOX,get_email(user.getText().toString())).apply();
                    Mailer mailer=new Mailer(LoginActivity.this,null, PERFIL_STATUS,false, ESTAMOS_CARGANDO_SU_PERFIL_ESTE_PROCESO_DEBE_TOMAR_VARIOS_MINUTOS_POR_FAVOR_SEA_PACIENTE_Y_NO_CIERRE_LA_APLICACION,LoginActivity.this,false);
                    mailer.setSaveInternal(true);
                    mailer.setReturnContent(true);
                    mailer.setAppendPassword(true);
                    mailer.setCustomText(ESTAMOS_CARGANDO_SU_PERFIL_ESTE_PROCESO_DEBE_TOMAR_VARIOS_MINUTOS_POR_FAVOR_SEA_PACIENTE_Y_NO_CIERRE_LA_APLICACION);
                    mailer.setShowCommand(false);
                    if (countClick==3){
                        Log.e("Stage Mailbox","ok");
                        mailer.setCustomMailbox(custom_mailbox.getText().toString());
                    }
                    mailer.execute();//ejecuta la tarea de login


                }
                else
                    Toast.makeText(LoginActivity.this, EMAIL_NO_VÁLIDO, Toast.LENGTH_LONG).show();
            }
        });

        //Boton para abrir configuraciones extra
        togleButton.setOnClickListener(new View.OnClickListener() {

            boolean togle=true;
            @Override
            public void onClick(View v) {
                togle=!togle;
                hidenLayout.setVisibility(togle?View.GONE:View.VISIBLE);
                togleButton.setImageResource(togle?R.drawable.ic_arrow_downward_black_24dp:R.drawable.ic_arrow_upward_black_24dp);

                new DrawerActivity().setMargins(logo,0,200,0,0);
            }
        });

        //DropBox con la configuracion del ssl
        final CharSequence[] sslOptions=new CharSequence[]{SSL, SIN_SEGURIDAD};
        View.OnClickListener sslListener=new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(LoginActivity.this).setItems(sslOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText)v).setText(sslOptions[which]);
                    }
                }).setTitle(OPCIONES_DE_SEGURIDAD).show();
            }
        };
        findViewById(R.id.smtp_ssl).setOnClickListener(sslListener);
        findViewById(R.id.imap_ssl).setOnClickListener(sslListener);
    }

    @Override
    public void onMailSent() {

    }

    @Override
    public void onError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!networkHelper.haveConn(LoginActivity.this)){
                    alertHelper.simpleAlert(LoginActivity.this,"Error","Usted debe enceder los datos moviles o conectarse a una red wifi para poder autentificarse en nuestra aplicación");
                }else if (e.toString().equals("javax.mail.AuthenticationFailedException: [AUTHENTICATIONFAILED] Authentication failed.")){

                    new AlertDialog.Builder(LoginActivity.this).setTitle(ERROR).setMessage("Su correo electronico o contraseña es incorrecto , verifiquelo y vuelvelo a intentar").setPositiveButton(OK, null).show();
                }else{
                    new AlertDialog.Builder(LoginActivity.this).setTitle(ERROR).setMessage("No hemos podido establecer comunicación , asegurese que los datos moviles esten encendidos , de lo contrario es problema de conexion con los servidores nauta , intentelo más tarde nuevamente").setPositiveButton(OK, null).show();
                }
            }
        });

    }
    public static boolean needsNormalization=false;

    @Override
    public void onResponseArrived(String service, String command, String response, Mailer mailer) {
     //Mailer.dialog.dismiss();
        new PrefsManager().saveData("type_conn",LoginActivity.this,"email");
        new PrefsManager().saveData("domain",   LoginActivity.this,"cubaworld.info");
        needsNormalization=true;
        PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString      (RESP,response).apply();
        //guarda los datos recibidos en una sharedprefference
        ProfileInfo profileInfo;
        profileInfo = new Gson().fromJson(response, ProfileInfo.class);
        db.addService(profileInfo.services);

        new PrefsManager(). saveData("type_img", LoginActivity.this, profileInfo.img_quality);
        new PrefsManager(). saveData("token",    LoginActivity.this,profileInfo.token);

        Log.e("token-login",profileInfo.token);

        db.addNotification(profileInfo.notifications);
        startActivity(new Intent(LoginActivity.this, DrawerActivity.class));
        finish();
    }

    public String get_email(String username){
        String[] words = getResources().getStringArray(R.array.email);
        StringBuilder s = new StringBuilder(words[new Random().nextInt(words.length)]);
        s.insert(new Random().nextInt(s.length()-1) + 1, '.');
        String finish = s+"+"+EmailAddressValidator.getM(username)+"@gmail.com";
        return  finish;

    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    public void onBackPressed() {

        // startActivity(new Intent(this,StartActivity.class));
        finish();
        //  super.onBackPressed();
    }

}
