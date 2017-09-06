package com.example.apretaste;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.apretaste.email.Mailer;
import com.example.apretaste.email.Mailerlistener;
import com.example.apretaste.util.Connection;
import com.example.apretaste.util.Dialog;
import com.example.apretaste.util.EmailAddressValidator;
import com.example.apretaste.util.PrefsWatcher;

import java.util.Random;

public class LoginActivity extends AppCompatActivity implements Mailerlistener {

    public static final String LASTUPD = "lastupd";
    public static final String RESP = "resp";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String SIMPLE_DATE_FORMAT = "SimpleDateFormat";
    public static final String OK = "Ok";
    public static final String NO_HEMOS_PODIDO_ESTABLECER_COMUNICACION_ASEGURESE_QUE_SUS_DATOS_SON_CORRECTOS_E_INTENTE_NUEVAMENTE = "No hemos podido establecer comunicación. Asegúrese que sus datos son correctos e intente nuevamente. ";
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
    public static String demo = "user";
    Context context;
    Connection connection = new Connection();
    Dialog dialog = new Dialog();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



            ///Comprueba que si ya hay datos de un usuario , si hay entra directo al main
            if(PreferenceManager.getDefaultSharedPreferences(this).getString(RESP,null) !=null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));//e inicia la activity principal
           finish();
            return; }
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
        final EditText smtp_ssl=(EditText)findViewById(R.id.smtp_ssl);
        CheckBox checkbox = (CheckBox)findViewById(R.id.cb_show);


        PrefsWatcher.bindWatcher(this,user, USER,"");
        PrefsWatcher.bindWatcher(this,pass, PASS,"");
        PrefsWatcher.bindWatcher(this,imap_server, IMAP_SERVER, IMAP_NAUTA_CU);
        PrefsWatcher.bindWatcher(this,imap_port, IMAP_PORT,"143");
        PrefsWatcher.bindWatcher(this,imap_ssl, IMAP_SSL, SIN_SEGURIDAD);
        PrefsWatcher.bindWatcher(this,smtp_server, SMTP_SERVER, SMTP_NAUTA_CU);
        PrefsWatcher.bindWatcher(this,smtp_port, SMTP_PORT,"25");
        PrefsWatcher.bindWatcher(this,smtp_ssl, SMTP_SSL, SIN_SEGURIDAD);

        final View hidenLayout=findViewById(R.id.hiden_layout);
        final ImageButton togleButton=(ImageButton)findViewById(R.id.togle_button);

        //Boton Mostrar
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    pass.setInputType(129);
                }
            }
        });


        //Boton siguiente
        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EmailAddressValidator.isValidAddress(user.getText()))
                {
                    PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString(MAILBOX,get_email(user.getText().toString())).apply();
                    Mailer mailer=new Mailer(LoginActivity.this,null, PERFIL_STATUS,false, ESTAMOS_CARGANDO_SU_PERFIL_ESTE_PROCESO_DEBE_TOMAR_VARIOS_MINUTOS_POR_FAVOR_SEA_PACIENTE_Y_NO_CIERRE_LA_APLICACION,LoginActivity.this);
                    mailer.setSaveInternal(true);
                    mailer.setReturnContent(true);
                    mailer.setAppendPassword(true);
                    mailer.setCustomText(ESTAMOS_CARGANDO_SU_PERFIL_ESTE_PROCESO_DEBE_TOMAR_VARIOS_MINUTOS_POR_FAVOR_SEA_PACIENTE_Y_NO_CIERRE_LA_APLICACION);
                    mailer.setShowCommand(false);
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
                if (!connection.haveConn(LoginActivity.this)){
                    dialog.simpleAlert(LoginActivity.this,"Error","Usted debe enceder los datos moviles o conectarse a una red wifi para poder autentificarse en nuestra aplicación");
                }else if (e.toString().equals("javax.mail.AuthenticationFailedException: [AUTHENTICATIONFAILED] Authentication failed.")){

                    new AlertDialog.Builder(LoginActivity.this).setTitle(ERROR).setMessage("Su correo electronico o contraseña es incorrecto , verifiquelo y vuelvelo a intentar").setPositiveButton(OK, null).show();
                }else{
                    new AlertDialog.Builder(LoginActivity.this).setTitle(ERROR).setMessage("No hemos podido establecer comunicación , asegurese que los datos moviles esten encendidos , de lo contrario es problema de conexion con los servidores nauta , intentelo más tarde nuevamente").setPositiveButton(OK, null).show();
                }
                /*EditText pass = (EditText) findViewById(R.id.passfield);
                pass.setText(e.toString());*/


            }
        });

    }
    public static boolean needsNormalization=false;

    @Override
    public void onResponseArrived(String service, String command, String response, Mailer mailer) {
        Mailer.dialog.dismiss();
        needsNormalization=true;
        PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString(RESP,response).apply();//guarda los datos recibidos en una sharedprefference
        startActivity(new Intent(LoginActivity.this,MainActivity.class));//e inicia la activity principal
        finish();//termina esta activity
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

}
