package apretaste.Comunication.http;




/**
 * Created by cjam on 9/02/2018.
 */
public interface Httplistener {

    void onErrorHttp(String error);

    void onResponseSimpleHttp(String response);

    void onResponseArrivedHttp(String service, String command, String response,ServiceHtpp serviceHtpp);


}
