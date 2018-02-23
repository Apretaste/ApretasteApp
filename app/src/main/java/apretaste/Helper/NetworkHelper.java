package apretaste.Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

/**
 * Created by cjam on 8/20/2017.
 */

public class NetworkHelper {

        public boolean haveConn(final Context context){
            ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo i = conManager.getActiveNetworkInfo();
            if ((i == null)||(!i.isConnected())||(!i.isAvailable())) {
               return false;
            }
            return true;
        }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }


    }

