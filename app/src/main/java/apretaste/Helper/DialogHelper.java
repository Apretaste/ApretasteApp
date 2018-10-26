package apretaste.Helper;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by cjam on 8/21/2017.
 */

public class DialogHelper {


    public void simpleAlert(Context context, String title, String msg) {
        new AlertDialog.Builder(context)

                .setMessage(msg)
                .setNegativeButton("OK", null)
                .setCancelable(false)
                .show();
    }

    public KProgressHUD DialogRequest(Activity activity) {
        KProgressHUD alert;
        alert = new KProgressHUD(activity);
        alert.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        alert.setCancellable(true);
        alert.setAnimationSpeed(2);
        alert.setDimAmount(0.5f);

        return alert;
    }


}
