package apretaste.Comunication.http;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * Created by cjam on 23/2/2018.
 */

public class SimpleHttp extends AsyncTask<Void, Void, Void> {
    String url;
    Activity activity;
    Httplistener httplistener;
    boolean requestCheck = false;



    public SimpleHttp(Activity context , String url, Httplistener httpListener){
        this.activity = context;
        this.url = url;
        this.httplistener = httpListener;
    }

    protected Void doInBackground(Void... params) {
        try {
            sendGet(this.url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /*Metodo que envia una simple peticion get*/
    private void sendGet(String url) throws Exception {
        HttpURLConnection con = null;

        con = (HttpURLConnection) new URL(url).openConnection();
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
        if (requestCheck){
            httplistener.onResponseSimpleHttp(String.valueOf(responseCode));
        }else {
            httplistener.onResponseSimpleHttp(response.toString());
        }



    }

    public void setRequestCheck(boolean requestCheck) {
        this.requestCheck = requestCheck;
    }
}
