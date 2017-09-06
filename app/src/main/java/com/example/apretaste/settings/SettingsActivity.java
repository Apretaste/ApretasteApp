package com.example.apretaste.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.apretaste.HistoryManager;
import com.example.apretaste.R;
import com.example.apretaste.db.DataBaseManager;
import com.example.apretaste.email.Mailer;
import com.example.apretaste.email.Mailerlistener;
import com.example.apretaste.util.Connection;
import com.example.apretaste.util.Dialog;

import java.io.File;

public class SettingsActivity extends AppCompatActivity implements Mailerlistener {
    public static final String CERRAR_SESSION = "Cerrar Sessión";
    public static final String CANCELAR = "Cancelar";
    public static final String ACEPTAR = "Aceptar";
    private SharedPreferences preferences;
    Boolean done = false;
    Connection connection = new Connection();
    Dialog dialog = new Dialog();
    public static final String SI_TERMINA_SU_SESION_NO_RECIBIRA_MAS_NOTIFICACIONES_Y_OTROS_USUARIOS_NO_PODRAN_CONTACTARLE = "Si termina su sesión no recibira más notificaciones, y otros usuarios no podrán contactarle.";
    LinearLayout ll_about,ll_config_nauta,ll_buzon,ll_time,ll_cerrar_sesion;

    Switch toggle;
    public static boolean terminating=false;
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_list);
        preferences = getSharedPreferences("pref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();


        final Switch sw = (Switch)findViewById(R.id.sw_mailer);
        /*Coje el valor actual del switch*/
        final boolean active = preferences.getBoolean("active", true);
        final boolean done = preferences.getBoolean("done", true);
      /*Metodo que detecta el estado del swithc*/
        sw.setChecked(active);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

            /*Guarda el estado del switch en prefernce(en este caso true pq esta en on el switch*/
    if (connection.haveConn(SettingsActivity.this)){
        editor.putBoolean("active", true);
        editor.apply();}


                    /*Accion que se realiza cuando se pone en on */

                Mailer mailer = new Mailer(SettingsActivity.this, null, "SUSCRIPCION LISTA ENTRAR", true, "Se ha guardado sus datos", SettingsActivity.this)
                        .setCustomText("Estamos guardando los cambios. Sea paciente y no cierre la aplicacion")
                        .setShowCommand(true);
                    mailer.setAppendPassword(true);


                mailer.execute();

                }else{

/*Guarda el estado del switch en prefernce(en este caso false pq esta en on el switch*/
                    if (connection.haveConn(SettingsActivity.this)){
                       editor.putBoolean("active", false);
                       editor.apply();}

                    /*Accion que se realiza cuando se pone en of */
                       Mailer mailer = new Mailer(SettingsActivity.this,null,"SUSCRIPCION LISTA SALIR",true,"Se ha guardado sus datos",SettingsActivity.this)
                               .setCustomText("Estamos guardando los cambios. Sea paciente y no cierre la aplicacion")
                               .setShowCommand(true);
                    mailer.setAppendPassword(true);


                       mailer.execute();



                    }




                }

        });
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
                       // Mailer mailer = new Mailer(SettingsActivity.this,null,"LISTA EXCLUYEME",true,"Sus Datos privados han sido borrados",SettingsActivity.this);

                       // mailer.setAppendPassword(true);


                      //  mailer.execute();
                        finish();
                    }
                }).setNegativeButton(CANCELAR,null).show();
                break;

            }

            case R.id.ll_cache:
            {
                new AlertDialog.Builder(this)
                        .setMessage("Guardamos el resultado de algunos servicios que usted ya pidio para que no tenga que volverlos a descargarlos y ahorrarle saldo. Desea eliminar esta data ? ")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataBaseManager manager = new DataBaseManager(SettingsActivity.this);
                                manager.deleteAll();
                            }
                        })
                        .setNegativeButton("Cancelar",null).show();

                break;

            }
        }
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

    @Override
    public void onMailSent() {

    }

    @Override
    public void onError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!connection.haveConn(SettingsActivity.this)){
                    dialog.simpleAlert(SettingsActivity.this,"Error","Usted debe enceder los datos moviles o conectarse a una red wifi para poder autentificarse en nuestra aplicación");
                }else if (e.toString().equals("javax.mail.AuthenticationFailedException: [AUTHENTICATIONFAILED] Authentication failed.")){

                    new AlertDialog.Builder(SettingsActivity.this).setTitle("Error").setMessage("Su correo electronico o contraseña es incorrecto , verifiquelo y vuelvelo a intentar").setPositiveButton("OK", null).show();
                }else{
                    new AlertDialog.Builder(SettingsActivity.this).setTitle("Error").setMessage("No hemos podido establecer comunicación , asegurese que los datos moviles esten encendidos , de lo contrario es problema de conexion con los servidores nauta , intentelo más tarde nuevamente").setPositiveButton("OK", null).show();
                }
                /*EditText pass = (EditText) findViewById(R.id.passfield);
                pass.setText(e.toString());*/


            }
        });

    }

    @Override
    public void onResponseArrived(String service, String command, String response, Mailer mailer) {

    }


}



