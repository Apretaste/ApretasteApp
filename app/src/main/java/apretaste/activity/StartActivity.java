package apretaste.activity;

import android.content.Context;
import android.content.Intent;

import java.net.Proxy;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.apretaste.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import apretaste.Comunication.Comunication;
import apretaste.Comunication.http.Httplistener;
import apretaste.Comunication.http.MultipartHttp;
//import ca.psiphon.PsiphonTunnel;

import static apretaste.activity.LoginActivity.RESP;


public class StartActivity extends AppCompatActivity   {

    public static AtomicInteger mLocalHttpProxyPort;
   // private PsiphonTunnel mPsiphonTunnel;
    public  static boolean connectProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


      /*  mLocalHttpProxyPort = new AtomicInteger(0);
        mPsiphonTunnel = PsiphonTunnel.newPsiphonTunnel(StartActivity.this);*/
        ///Comprueba que si ya hay datos de un usuario , si hay entra directo al main
        if(PreferenceManager.getDefaultSharedPreferences(this).getString(RESP,null) !=null) {
            startActivity(new Intent(StartActivity.this, DrawerActivity.class));//e inicia la activity principal
            //finish();
            return; }



        findViewById(R.id.btn_wd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,LoginHttp.class));

              //  finish();


            }
        });

        findViewById(R.id.btn_na).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,LoginActivity.class));

               // finish();

            }
        });
    }
/*
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // NOTE: for demonstration purposes, this sample app
        // restarts Psiphon in onPause/onResume. Since it may take some
        // time to connect, it's generally recommended to keep
        // Psiphon running, so start/stop in onCreate/onDestroy or
        // even consider running a background Service.


        try {
            mPsiphonTunnel.startTunneling("");
        } catch (PsiphonTunnel.Exception e) {
            logMessage("failed to start Psiphon");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    Log.e("rompio","se esta rompiendo");
    }
    @Override
    protected void onPause() {
        super.onPause();

        // NOTE: stop() can block for a few seconds, so it's generally
        // recommended to run PsiphonTunnel.start()/stop() in a background
        // thread and signal the thread appropriately.

        mPsiphonTunnel.stop();
    }

    private void setHttpProxyPort(int port) {

        // NOTE: here we record the Psiphon proxy port for subsequent
        // use in tunneling app traffic. In this sample app, we will
        // use WebViewProxySettings.setLocalProxy to tunnel a WebView
        // through Psiphon. By default, the local proxy port is selected
        // dynamically, so it's important to record and use the correct
        // port number.

        mLocalHttpProxyPort.set(port);
    }



    private void logMessage(final String message) {

        Log.e("Tunel",message);
    }

    //----------------------------------------------------------------------------------------------
    // PsiphonTunnel.TunneledApp implementation
    //
    // NOTE: these are callbacks from the Psiphon Library
    //----------------------------------------------------------------------------------------------

    @Override
    public String getAppName() {
        return "TunneledWebView Sample";
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Object getVpnService() {
        return null;
    }

    @Override
    public Object newVpnServiceBuilder() {
        return null;
    }

    @Override
    public String getPsiphonConfig() {
        try {
            JSONObject config = new JSONObject(
                    readInputStreamToString(
                            getResources().openRawResource(R.raw.psiphon_config)));

            return config.toString();

        } catch (IOException e) {
            logMessage("error loading Psiphon config: " + e.getMessage());
        } catch (JSONException e) {
            logMessage("error loading Psiphon config: " + e.getMessage());
        }
        return "";
    }

    @Override
    public void onDiagnosticMessage(String message) {
        android.util.Log.i(getString(R.string.app_name), message);
        logMessage(message);
    }

    @Override
    public void onAvailableEgressRegions(List<String> regions) {
        for (String region : regions) {
            logMessage("available egress region: " + region);
        }
    }

    @Override
    public void onSocksProxyPortInUse(int port) {
        logMessage("local SOCKS proxy port in use: " + Integer.toString(port));
    }

    @Override
    public void onHttpProxyPortInUse(int port) {
        logMessage("local HTTP proxy port in use: " + Integer.toString(port));
    }

    @Override
    public void onListeningSocksProxyPort(int port) {
        logMessage("local SOCKS proxy listening on port: " + Integer.toString(port));
    }

    @Override
    public void onListeningHttpProxyPort(int port) {
        logMessage("local HTTP proxy listening on port: " + Integer.toString(port));
        setHttpProxyPort(port);
    }

    @Override
    public void onUpstreamProxyError(String message) {
        logMessage("upstream proxy error: " + message);
    }

    @Override
    public void onConnecting() {
        logMessage("connecting...");
    }

    @Override
    public void onConnected() {
        logMessage("connected");

       connectProxy = true;

        new Comunication().setTunnel(mPsiphonTunnel);

    }

    @Override
    public void onHomepage(String url) {
        logMessage("home page: " + url);
    }

    @Override
    public void onClientUpgradeDownloaded(String filename) {
        logMessage("client upgrade downloaded");
    }

    @Override
    public void onClientIsLatestVersion() {

    }

    @Override
    public void onSplitTunnelRegion(String region) {
        logMessage("split tunnel region: " + region);
    }

    @Override
    public void onUntunneledAddress(String address) {
        logMessage("untunneled address: " + address);
    }

    @Override
    public void onBytesTransferred(long sent, long received) {
        logMessage("bytes sent: " + Long.toString(sent));
        logMessage("bytes received: " + Long.toString(received));
    }

    @Override
    public void onStartedWaitingForNetworkConnectivity() {
        logMessage("waiting for network connectivity...");
    }

    @Override
    public void onClientVerificationRequired(String s, int i, boolean b) {

    }

    @Override
    public void onActiveAuthorizationIDs(List<String> authorizations) {

    }

    @Override
    public void onExiting() {

    }

    @Override
    public void onClientRegion(String region) {
        logMessage("client region: " + region);
    }

    private static String readInputStreamToString(InputStream inputStream) throws IOException {
        return new String(readInputStreamToBytes(inputStream), "UTF-8");
    }

    private static byte[] readInputStreamToBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int readCount;
        byte[] buffer = new byte[16384];
        while ((readCount = inputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, readCount);
        }
        outputStream.flush();
        inputStream.close();
        return outputStream.toByteArray();
    }

    @Override
    public void onErrorHttp(String error) {

    }

    @Override
    public void onResponseSimpleHttp(String response) {
        Log.e("resss",response);
    }

    @Override
    public void onResponseArrivedHttp(String service, String command, String response, MultipartHttp multipartHttp) {

    }
*/
    /*Clase que se llama para llamar recurso via get
    class Connection extends AsyncTask<Void,Void,Void> {

        protected Void doInBackground(Void... params) {
            try {
                sendGet();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }



    }*/
   /* Metodo que envia una simple peticion get
    private void sendGet() throws Exception {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", mLocalHttpProxyPort.get()));
        HttpURLConnection con = (HttpURLConnection) new URL("https://apretaste.com").openConnection(proxy);
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        Log.e("response",response.toString());


    }*/

}
