package apretaste.Comunication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import apretaste.Comunication.email.Mailer;
import apretaste.Comunication.email.Mailerlistener;
import apretaste.Comunication.http.Httplistener;
import apretaste.Comunication.http.ServiceHtpp;
import apretaste.Helper.NetworkHelper;
import apretaste.Helper.PrefsManager;
import apretaste.activity.Settings_nauta;


/**
 * Created by cjam on 13/2/2018.
 */

public class Comunication {
    public String domain = "http://apretaste.mobi/api/";
    private boolean returnContent = false;
    private boolean saveInternal = false;
    Activity activity;
    NetworkHelper networkHelper = new NetworkHelper();
    public void setReturnContent(boolean returnContent) {
        this.returnContent = returnContent;
    }

    public boolean byInternet = true;
    public void setSaveInternal(boolean saveInternal) {
        this.saveInternal = saveInternal;
    }


    public void execute(final Activity activity, String service, String command, Boolean noreply, String help , Httplistener htpplistener , Mailerlistener mailerListener){
        this.activity = activity;

        if (networkHelper.haveConn(this.activity)) {

                if (!new PrefsManager().getData("type_conn", activity).equals("")) {
                    switch (new PrefsManager().getData("type_conn", activity)) {

                        case "auto":{

                        }
                        case "email": {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
                            String pass=preferences.getString("pass","");

                            if (!pass.equals("") && new PrefsManager()
                                    .getData("email_configured",activity).equals("")){

                            Mailer mailer = new Mailer(activity, service, command, false, help, mailerListener, false);
                            mailer.setAppendPassword(true);
                            if (returnContent) {
                                mailer.setReturnContent(true);
                            }
                            mailer.execute();}
                            else
                            {
                                new AlertDialog.Builder(activity)
                                        .setMessage("Usted esta tratando de comunicarse con el servidor atraves de correo electronico pero aun no tenemos su contraseña")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                activity.startActivity(new Intent(activity,Settings_nauta.class));


                                            }

                                        }).show();

                            }
                            break;
                        }

                        case "internet": {
                            ServiceHtpp serviceHtpp = new ServiceHtpp(activity, service, command, noreply, help, htpplistener);
                            if (returnContent) {
                                serviceHtpp.setReturnContent(true);
                            }
                            serviceHtpp.execute();
                            break;
                        }

                    }
                }
            } else {
                Toast.makeText(activity, "Usted no esta conectado a ninguna red", Toast.LENGTH_SHORT).show();
            }

    }

    public  boolean CheckInternet(){
        return  true;
    }


}
