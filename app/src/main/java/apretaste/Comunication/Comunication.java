package apretaste.Comunication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import apretaste.Comunication.email.Mailer;
import apretaste.Comunication.email.Mailerlistener;
import apretaste.Comunication.http.Httplistener;
import apretaste.Comunication.http.MultipartHttp;
import apretaste.Comunication.http.SimpleHttp;
import apretaste.Helper.NetworkHelper;
import apretaste.Helper.PrefsManager;
import apretaste.ui.Settings_nauta;
//import ca.psiphon.PsiphonTunnel;


/**
 * Created by cjam on 13/2/2018.
 */

public class Comunication implements Httplistener {
    public String domain = "";
    private boolean returnContent = false;
    private boolean saveInternal = false;
    Activity activity;
    private boolean noMessage = false;
    boolean reponseOk;
    private Bitmap bitmap=null;
    public void setAttachedbitmap(Bitmap bitmap)
    {
        this.bitmap=bitmap;
    }


    public void setNoMessage(boolean noMessage) {
        this.noMessage = noMessage;
    }


    NetworkHelper networkHelper = new NetworkHelper();


    public void setReturnContent(boolean returnContent) {
        this.returnContent = returnContent;
    }


    public void setSaveInternal(boolean saveInternal) {
        this.saveInternal = saveInternal;
    }


    public void execute(final Activity activity, final String service, final String command, final Boolean noreply, final String help , final Httplistener htpplistener , final Mailerlistener mailerListener){
        this.activity = activity;

        if (networkHelper.haveConn(this.activity)) {
            if (!new PrefsManager().getData("type_conn", activity).equals("")) {
                    switch (new PrefsManager().getData("type_conn", activity)) {
                        case "auto":{
                            SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(activity);
                            final String url = "http://"+pre.getString("domain", "cubaworld.info")+"/api/check";
                            SimpleHttp checkGoogle = new SimpleHttp(activity,"https://www.google.com/",Comunication.this);
                            checkGoogle.setRequestCheck(true);
                            checkGoogle.execute();
                            Log.e("res", String.valueOf(reponseOk));
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("res", String.valueOf(reponseOk));
                                    if (reponseOk){
                                        SimpleHttp checkDomain = new SimpleHttp(activity,url,Comunication.this);
                                        checkDomain.setRequestCheck(true);
                                        checkDomain.execute();

                                        if (reponseOk){
                                            sendViaHttp(service, command, noreply, help, htpplistener);
                                            reponseOk = false;
                                        }else{
                                            sendViaEmail(service, command, noreply, help, mailerListener);
                                        }

                                    }else{
                                        sendViaEmail(service, command, noreply, help, mailerListener);
                                    }
                                }
                            }, 1000);

                            break;

                        }
                        case "email": {
                            sendViaEmail(service, command, noreply, help, mailerListener);
                            break;
                        }

                        case "internet": {
                            sendViaHttp(service, command, noreply, help, htpplistener);
                            break;
                        }

                    }
                }
            } else {
                Toast.makeText(activity, "Usted no esta conectado a ninguna red", Toast.LENGTH_SHORT).show();
            }

    }





    public void sendViaHttp( String service, String command, Boolean noreply, String help , Httplistener htpplistener){
        MultipartHttp multipartHttp = new MultipartHttp(activity, service, command, noreply, help, htpplistener);
        if (returnContent) {
            multipartHttp.setReturnContent(true);
        }


        if (this.bitmap!=null){
            Log.e("Comunication ","hay img");
            multipartHttp.setAttachedbitmap(this.bitmap);
        }

        multipartHttp.execute();
    }

    public void sendViaEmail( String service, String command, Boolean noreply, String help ,Mailerlistener mailerListener){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String pass=preferences.getString("pass","");
        String user=preferences.getString("user","");
        if (!pass.equals("") && !user.equals("")){

            Mailer mailer = new Mailer(activity, service, command, noreply, help, mailerListener, this.noMessage);
            if (this.bitmap!=null){
                Log.e("Comunication ","hay img");
                mailer.setAttachedbitmap(this.bitmap);
            }
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
                    .setMessage("Usted esta intentando comunicarse con el servidor usando su correo Nauta, pero aun no ha insertado su cuenta.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(new Intent(activity,Settings_nauta.class));


                        }

                    }).show();

        }
    }

    @Override
    public void onErrorHttp(String error) {

    }

    @Override
    public void onResponseSimpleHttp(String response) {
        Log.e("response",response);

        if (response.equals("200")){
            reponseOk = true;
        }
    }

    @Override
    public void onResponseArrivedHttp(String service, String command, String response, MultipartHttp multipartHttp) {

    }
}
