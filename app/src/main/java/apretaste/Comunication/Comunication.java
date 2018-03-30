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
import apretaste.Comunication.http.MultipartHttp;
import apretaste.Helper.NetworkHelper;
import apretaste.Helper.PrefsManager;
import apretaste.activity.Settings_nauta;
//import ca.psiphon.PsiphonTunnel;


/**
 * Created by cjam on 13/2/2018.
 */

public class Comunication {
    public String domain = "http://apretaste.com/api/";
    private boolean returnContent = false;
    private boolean saveInternal = false;
    Activity activity;
    private boolean noMessage = false;

    public void setNoMessage(boolean noMessage) {
        this.noMessage = noMessage;
    }


    NetworkHelper networkHelper = new NetworkHelper();

   // public static  PsiphonTunnel tunnel;

    /*public static void setTunnel(PsiphonTunnel tunnel) {
        Comunication.tunnel = tunnel;
    }*/

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

                            Mailer mailer = new Mailer(activity, service, command, noreply, help, mailerListener, this.noMessage);
                                if (returnContent) {
                                mailer.setReturnContent(true);
                            }

                            if (saveInternal){
                                mailer.setSaveInternal(true);
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
                            MultipartHttp multipartHttp = new MultipartHttp(activity, service, command, noreply, help, htpplistener);
                            if (returnContent) {
                                multipartHttp.setReturnContent(true);
                            }
                            multipartHttp.execute();
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
