package apretaste.Comunication.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by cjam on 6/2/2018.
 */

public class SimpleRequest extends AsyncTask<Void, String, Void> {

    private String url;
    StringRequest stringRequest;
    Httplistener httplistener;
    private ProgressDialog dialog;
    private  Activity activity;
     String textDialog;


    public SimpleRequest(Activity activity ,String url , String textDialog , Httplistener httplistener){

        this.url = url;
        this.activity = activity;
        this.textDialog = textDialog;
        dialog = new ProgressDialog(activity);
        this.httplistener = httplistener;
    }


    public void SendRequest(){

        stringRequest = new StringRequest(Request.Method.GET, this.url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             httplistener.onResponseSimpleHttp(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                httplistener.onErrorHttp(error);

            }
        });

        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }


    @Override
    protected Void doInBackground(Void... params) {

        SendRequest();
        return null;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(activity, "empezo", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(activity, "Termino", Toast.LENGTH_SHORT).show();
    }
}
