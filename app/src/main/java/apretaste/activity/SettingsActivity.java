package apretaste.activity;

import android.content.Context;
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
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import apretaste.Comunication.Comunication;
import apretaste.Comunication.http.Httplistener;
import apretaste.Comunication.http.MultipartHttp;
import apretaste.Helper.FileHelper;
import apretaste.Helper.PrefsManager;
import apretaste.HistoryManager;


import com.example.apretaste.R;

import apretaste.Helper.DbHelper;
import apretaste.Comunication.email.Mailer;
import apretaste.Comunication.email.Mailerlistener;
import apretaste.Helper.NetworkHelper;
import apretaste.Helper.AlertHelper;

import java.io.File;



public class SettingsActivity extends AppCompatActivity implements Mailerlistener, Httplistener {
    public static final String CERRAR_SESSION = "Cerrar Sessión";
    public static final String CANCELAR = "Cancelar";
    public static final String ACEPTAR = "Aceptar";
    private SharedPreferences preferences;
    TextView state_img;
    Comunication comunication = new Comunication();
    NetworkHelper networkHelper = new NetworkHelper();
    AlertHelper dialog = new AlertHelper();
    public static final String SI_TERMINA_SU_SESION_NO_RECIBIRA_MAS_NOTIFICACIONES_Y_OTROS_USUARIOS_NO_PODRAN_CONTACTARLE = "Si termina su sesión no recibira más notificaciones, y otros usuarios no podrán contactarle.";

    ViewSwitcher vss;
    Switch toggle;
    WebView wvs;
    public static boolean terminating = false;
    public boolean task = false;
     TextView type_conn;

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_list);

        preferences = getSharedPreferences("pref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();



         state_img = (TextView) findViewById(R.id.state_img);
       //  type_conn = (TextView) findViewById(R.id.tvTypeConn);

        if (!new PrefsManager().getData("type_img",SettingsActivity.this).equals("")){
           switch (new PrefsManager().getData("type_img",SettingsActivity.this)){
               case "ORIGINAL":{
                   state_img.setText(" (original)");
                   break;
               }

               case "REDUCIDA":{
                   state_img.setText(" (reducida)");
                   break;
               }

               case "SIN_IMAGEN":{
                   state_img.setText(" (sin imágenes)");
                   break;
               }

           }
        }else{
            state_img.setText("");
        }


       /* if (!new PrefsManager().getData("type_conn",SettingsActivity.this).equals("")){
            switch (new PrefsManager().getData("type_conn",SettingsActivity.this)){
                case "auto":{
                    type_conn.setText(" (automática)");
                    break;
                }

                case "email":{
                    type_conn.setText(" (correo electrónico)");
                    break;
                }

                case "internet":{
                    type_conn.setText(" (internet)");
                    break;
                }

            }
        }else{
            state_img.setText("");
        }*/

        final Switch sw = (Switch) findViewById(R.id.sw_mailer);
        /*Coje el valor actual del switch*/
        final boolean active = preferences.getBoolean("active", true);
        final boolean done = preferences.getBoolean("done", true);
      /*Metodo que detecta el estado del swithc*/
        sw.setChecked(active);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

            /*Guarda el estado del switch en prefernce(en este caso true pq esta en on el switch*/
                    if (networkHelper.haveConn(SettingsActivity.this)) {
                        editor.putBoolean("active", true);
                        editor.apply();
                    }


                    /*Accion que se realiza cuando se pone en on */

                 /*   Mailer mailer = new Mailer(SettingsActivity.this, null, "SUSCRIPCION LISTA ENTRAR", true, "Se ha guardado sus datos", SettingsActivity.this, false)
                            .setCustomText("Estamos guardando los cambios. Sea paciente y no cierre la aplicacion")
                            .setShowCommand(true);
                    mailer.setAppendPassword(true);
                    mailer.execute();*/

                    comunication.execute(SettingsActivity.this, null, "SUSCRIPCION LISTA ENTRAR", true, "Se ha guardado sus datos", SettingsActivity.this, SettingsActivity.this);



                } else {

/*Guarda el estado del switch en prefernce(en este caso false pq esta en on el switch*/
                    if (networkHelper.haveConn(SettingsActivity.this)) {
                        editor.putBoolean("active", false);
                        editor.apply();
                    }

                    /*Accion que se realiza cuando se pone en of */
                 /*   Mailer mailer = new Mailer(SettingsActivity.this, null, "SUSCRIPCION LISTA SALIR", true, "Se ha guardado sus datos", SettingsActivity.this, false)
                            .setCustomText("Estamos guardando los cambios. Sea paciente y no cierre la aplicacion")
                            .setShowCommand(true);
                    mailer.setAppendPassword(true);

                    mailer.execute();*/

                    comunication.execute(SettingsActivity.this, null, "SUSCRIPCION LISTA SALIR", true, "Se ha guardado sus datos", SettingsActivity.this, SettingsActivity.this);




                }


            }

        });
    }


    public void open_activitys(View view) {
        switch (view.getId()) {

           /* case R.id.ll_config_nauta:
                startActivity(new Intent(SettingsActivity.this, Settings_nauta.class));
                break;*/
            case R.id.ll_buzon:
                startActivity(new Intent(SettingsActivity.this, Settings_buzon.class));
                break;

            case R.id.ll_cerrar_sesion: {
                new AlertDialog.Builder(this).setMessage(SI_TERMINA_SU_SESION_NO_RECIBIRA_MAS_NOTIFICACIONES_Y_OTROS_USUARIOS_NO_PODRAN_CONTACTARLE).setPositiveButton(ACEPTAR, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       /* Mailer mailer = new Mailer(SettingsActivity.this, null, "SUSCRIPCION EXCLUYEME", true, "Sus Datos privados han sido borrados", SettingsActivity.this, true);
                        mailer.setAppendPassword(true);
                        mailer.execute();*/
                        comunication.setNoMessage(true);
                        comunication.execute(SettingsActivity.this, null, "SUSCRIPCION EXCLUYEME", true, "Sus Datos privados han sido borrados", SettingsActivity.this, SettingsActivity.this);

                        task = true;

                    }
                }).setNegativeButton(CANCELAR, null).show();
                break;

            }

            case R.id.ll_cache: {
                new AlertDialog.Builder(this)
                        .setMessage("Guardamos el resultado de algunos servicios que usted ya pidio para que no tenga que volver a descargarlos y ahorrarle saldo. ¿Desea eliminar esta data?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new DbHelper(SettingsActivity.this).deleteAllTable("cache");
                                Toast.makeText(getBaseContext(), "Data Eliminada", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancelar", null).show();

                break;

            }
            /*
            case R.id.ll_type_conn:
            {
                String[] options = {"Automática","Correo electrónico","Internet"};
                new AlertDialog.Builder(SettingsActivity.this)
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:{
                                    //    new  PrefsManager().saveData("type_conn",SettingsActivity.this,"auto");
                                      //  type_conn.setText(" (automática)");
                                        Toast.makeText(SettingsActivity.this, "Esta Funcionalidad aun no esta permitida", Toast.LENGTH_SHORT).show();
                                        break;
                                    }

                                    case 1:{
                                        new  PrefsManager().saveData("type_conn",SettingsActivity.this,"email");
                                        type_conn.setText(" (correo Electrónico)");
                                        break;
                                    }

                                    case 2:{
                                        new  PrefsManager().saveData("type_conn",SettingsActivity.this,"internet");
                                        type_conn.setText(" (internet)");
                                        break;
                                    }
                                }
                            }
                        }).show();
                break;
            }*/

            case R.id.ll_calidad_img:
            {
                final SharedPreferences.Editor editor = preferences.edit();

                final boolean active = preferences.getBoolean("active", true);
                new AlertDialog.Builder(SettingsActivity.this)

                        .setItems(new CharSequence[]{"Original", "Reducida", "Sin imagenes"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:{
                                      /*  Mailer mailer = new Mailer(SettingsActivity.this, null, "PERFIL IMAGEN ORIGINAL", true, "Preferencias de imagenes", SettingsActivity.this, false);
                                        mailer.setAppendPassword(true);
                                        mailer.setCustomText("Cambiando preferencia de imagenes a original");
                                        mailer.setShowCommand(true);
                                        mailer.execute();*/

                                        comunication.execute(SettingsActivity.this, null, "PERFIL IMAGEN ORIGINAL", true, "Preferencias de imagenes", SettingsActivity.this, SettingsActivity.this);

                                        new  PrefsManager().saveData("type_img",SettingsActivity.this,"ORIGINAL");

                                        state_img.setText(" (original)");
                                        break;
                                    }

                                    case 1:{
                                      /*  Mailer mailer = new Mailer(SettingsActivity.this, null, "PERFIL IMAGEN REDUCIDA", true, "Preferencias de imagenes", SettingsActivity.this, false);
                                        mailer.setAppendPassword(true);
                                        mailer.setCustomText("Cambiando preferencia de imagenes a reducida");
                                        mailer.setShowCommand(true);
                                        mailer.execute();*/
                                        comunication.execute(SettingsActivity.this, null, "PERFIL IMAGEN ORIGINAL", true, "Preferencias de imagenes", SettingsActivity.this, SettingsActivity.this);


                                        new  PrefsManager().saveData("type_img",SettingsActivity.this,"REDUCIDA");

                                        state_img.setText(" (reducida)");
                                        break;
                                    }

                                    case 2:{

                                      /*  Mailer mailer = new Mailer(SettingsActivity.this, null, "PERFIL IMAGEN SIN_IMAGEN", true, "Preferencias de imagenes", SettingsActivity.this, false);
                                        mailer.setAppendPassword(true);
                                        mailer.setCustomText("Cambiando preferencia de imagenes a sin imagenes");
                                        mailer.setShowCommand(true);
                                        mailer.execute();*/

                                        comunication.execute(SettingsActivity.this, null, "PERFIL IMAGEN ORIGINAL", true, "Preferencias de imagenes", SettingsActivity.this, SettingsActivity.this);

                                        new  PrefsManager().saveData("type_img",SettingsActivity.this,"SIN_IMAGEN");

                                        state_img.setText(" (sin imágenes)");
                                        break;
                                    }


                                }
                            }
                        }).show();

                break;

            }



        }
    }




    @Override
    public void onMailSent() {

        if (task) {

            ClearAllDates(SettingsActivity.this);
            terminating = true;
            finish();
        }

    }

    @Override
    public void onError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!networkHelper.haveConn(SettingsActivity.this)) {
                    dialog.simpleAlert(SettingsActivity.this, "Error", "Usted debe enceder los datos moviles o conectarse a una red wifi para poder usar nuestra aplicación");
                } else if (e.toString().equals("javax.mail.AuthenticationFailedException: [AUTHENTICATIONFAILED] Authentication failed.")) {

                    new AlertDialog.Builder(SettingsActivity.this).setTitle("Error").setMessage("Su correo electronico o contraseña es incorrecto , verifiquelo y vuelvelo a intentar").setPositiveButton("OK", null).show();
                } else {
                    new AlertDialog.Builder(SettingsActivity.this).setTitle("Error").setMessage("No hemos podido establecer comunicación  , es problema de conexion con los servidores nauta , intentelo más tarde nuevamente").setPositiveButton("OK", null).show();
                }


            }
        });

    }

    @Override
    public void onResponseArrived(String service, String command, String response, Mailer mailer) {

    }


    @Override
    public void onErrorHttp(String error) {

    }

    @Override
    public void onResponseSimpleHttp(String response) {

    }

    @Override
    public void onResponseArrivedHttp(String service, String command, String response, MultipartHttp multipartHttp) {

    }

    public void ClearAllDates(Context context){
        HistoryManager.getSingleton().entries.clear();
        //new DbHelper(SettingsActivity.this).deleteAllTable("cache");
        context.deleteDatabase(DbHelper.DB_NAME);
      //  new DbHelper(SettingsActivity.this).deleteAllTable("services");
       // new DbHelper(SettingsActivity.this).deleteAllTable("notifications");
        FileHelper.delete(getCacheDir(), false);
        FileHelper.delete(getFilesDir(), false);
        FileHelper.delete(getExternalFilesDir(null), false);
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }
}




