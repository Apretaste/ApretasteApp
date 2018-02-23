package apretaste.Helper;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by cjam on 8/21/2017.
 */

public class DialogHelper {



    public void simpleAlert(Context context , String title, String msg){
        new AlertDialog.Builder(context)

                .setMessage(msg)
                .setNegativeButton("OK", null)
                .setCancelable(false)
                .show();
    }


}
