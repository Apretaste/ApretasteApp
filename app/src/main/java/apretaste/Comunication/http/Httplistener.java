package apretaste.Comunication.http;

import com.android.volley.VolleyError;


/**
 * Created by cjam on 9/02/2018.
 */
public interface Httplistener {

    void onErrorHttp(VolleyError error);

    void onResponseSimpleHttp(String response);

    void onResponseArrivedHttp(String service, String command, String response,ServiceHtpp serviceHtpp);


}
