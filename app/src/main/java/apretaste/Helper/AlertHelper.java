package apretaste.Helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;

import com.example.apretaste.R;

import apretaste.activity.DrawerActivity;
import apretaste.activity.NotificationsActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by cjam on 8/21/2017.
 */

public class AlertHelper {



    public void simpleAlert(Context context , String title, String msg){
        new AlertDialog.Builder(context)

                .setMessage(msg)
                .setNegativeButton("OK", null)
                .setCancelable(false)
                .show();
    }

    public void newNotification(Context context,int when,String service, String text){
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotifyMgr =(NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        int icono = R.mipmap.ic_launcher_new;
        Intent intent = new Intent(context, NotificationsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,intent, 0);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)

                .setSmallIcon(getNotificationIcon())
                .setContentTitle(service)
                .setContentText(text)
                .setVibrate(new long[] {100, 250, 100, 500})
                .setSound(uri)
                .setLights(Color.BLUE, 3000, 3000)
                .setAutoCancel(true);


        mNotifyMgr.notify((int) when, mBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher_new : R.mipmap.ic_launcher_new;
    }

}
