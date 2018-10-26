package apretaste.Comunication.http;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.apretaste.R;

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
    private TextView statusView;
    private AlertDialog dialog;
    Activity activity;
    Httplistener httplistener;
    boolean requestCheck = false;
    boolean showDialog = true;
    String messageDialog;


    public String getMessageDialog() {
        return messageDialog;
    }

    public void setMessageDialog(String messageDialog) {
        this.messageDialog = messageDialog;
    }

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
            sendGet(this.url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        final View v = activity.getLayoutInflater().inflate(R.layout.wait_dialog_layout, null);
        statusView = ((TextView) v.findViewById(R.id.status));

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(v);

        builder.setCancelable(false);

        builder.setNegativeButton("Cancelar", null);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface d) {

                        final Button b = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                b.setVisibility(View.GONE);
                                cancel(true);
                            }

                        });
                    }
                });
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
    private void sendGet(String url) throws Exception {
        statusView.setText(getMessageDialog());
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
