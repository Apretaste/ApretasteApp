package apretaste.Comunication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.apretaste.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import apretaste.Helper.PrefsManager;
import ca.psiphon.PsiphonTunnel;


public class ServicePsiphon extends Service implements PsiphonTunnel.HostService {

    private static AtomicInteger mLocalHttpProxyPort;
    private PsiphonTunnel mPsiphonTunnel;
    private static Boolean connected = false;


    public static void setConnected() {
        connected = true;
    }

    public static Boolean isConnected() {
        return connected;
    }


    public static int getmLocalHttpProxyPort() {
        return mLocalHttpProxyPort.get();
    }

    public class ServicePsiphonBinder extends Binder {
        public ServicePsiphon getService() {
            return ServicePsiphon.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("msg", "Servicio creado");
        mLocalHttpProxyPort = new AtomicInteger(0);
        mPsiphonTunnel = PsiphonTunnel.newPsiphonTunnel(this);


    }

    @Override
    public int onStartCommand(Intent intencion, int flags, int idArranque) {
        Log.i("msg", "Servicio reiniciado");

        try {
            this.mPsiphonTunnel.startTunneling("");
        } catch (PsiphonTunnel.Exception e) {
            logMessage("failed to start Psiphon");
        }

        //----- Aquí tu codigo--------
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ServicePsiphon", "se cerro la conexion");
        new PrefsManager().saveBoolean(this, "connect-psiphon", false);

    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    //----------------------------------------------------------------------------------------------
    // PsiphonTunnel.TunneledApp implementation
    //
    // NOTE: these are callbacks from the Psiphon Library
    //----------------------------------------------------------------------------------------------

    @Override
    public String getAppName() {
        return "Apretaste";
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
                            this.getResources().openRawResource(R.raw.psiphon_config)));

            return config.toString();

        } catch (IOException e) {
            logMessage("error loading Psiphon config: " + e.getMessage());
        } catch (JSONException e) {
            logMessage("error loading Psiphon config: " + e.getMessage());
        }
        return "";
    }

    private void logMessage(final String status) {

        Log.e("message", status);

    }

    @Override
    public void onDiagnosticMessage(String message) {
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
        mLocalHttpProxyPort.set(port);

        new PrefsManager().saveInt(this, "portProxy", port);
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

        setConnected();

        new PrefsManager().saveBoolean(this, "connect-psiphon", true);


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

}


