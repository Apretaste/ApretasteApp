package apretaste.Comunication.http;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import apretaste.Helper.DialogHelper;
import apretaste.Helper.PrefsManager;

/**
 * Created by cjam on 23/2/2018.
 */

public class SimpleHttp extends AsyncTask<Void, Void, Void> {
    String url;
    private TextView statusView;
    private KProgressHUD dialog;
    Activity activity;
    Httplistener httplistener;
    boolean requestCheck = false;
    boolean showDialog = true;

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public SimpleHttp(Activity context, String url, Httplistener httpListener) {
        this.activity = context;
        this.url = url;
        this.httplistener = httpListener;
    }

    protected Void doInBackground(Void... params) {
        try {
            sendGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {

        dialog = new DialogHelper().DialogRequest(activity);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isShowDialog()) {
                    dialog.show();
                }
            }
        });

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        dialog.dismiss();
    }

    /*Metodo que envia una simple peticion get*/
    private void sendGet() throws Exception {

        HttpURLConnection con = null;

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost",new PrefsManager().getInt(activity,"portProxy")));

        con = (HttpURLConnection) new URL(url).openConnection(proxy);
        con.setConnectTimeout(2000);
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        if (requestCheck) {
            httplistener.onResponseSimpleHttp(String.valueOf(responseCode));
        } else {
            httplistener.onResponseSimpleHttp(response.toString());
        }

        }


    public void setRequestCheck(boolean requestCheck) {
        this.requestCheck = requestCheck;
    }
}