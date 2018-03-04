package apretaste.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;


import com.example.apretaste.R;

import apretaste.Comunication.Comunication;
import apretaste.Comunication.http.Httplistener;
import apretaste.Comunication.http.MultipartHttp;
import apretaste.Helper.DataHelper;
import apretaste.Helper.DbHelper;
import apretaste.Helper.PrefsManager;
import apretaste.Helper.StringHelper;
import apretaste.HistoryEntry;
import apretaste.HistoryManager;
import apretaste.Comunication.email.Mailer;
import apretaste.Comunication.email.Mailerlistener;
import apretaste.Notifications;
import apretaste.ProfileInfo;

import com.google.gson.Gson;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationsActivity extends AppCompatActivity implements Mailerlistener , Httplistener {

    public static final String RESP = "resp";
    public ArrayList<Notifications> listNoti;//aki
    DbHelper db;
    DataHelper dataHelper = new DataHelper();
    RecyclerView rv;
    private Comunication comunication = new Comunication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar tbn = (Toolbar) findViewById(R.id.tbn);
        tbn.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationsActivity.this, DrawerActivity.class));
                finish();
            }
        });
        tbn.setTitle("Notificaciones");

        findViewById(R.id.del_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NotificationsActivity.this)

                        .setMessage("¿Desea eliminar todas las notificaciones ?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ((ViewSwitcher) findViewById(R.id.notifSwitcher)).setDisplayedChild(0);

                                db.deleteAllTable("notifications");




                            }
                        })
                        .setNegativeButton("No", null)
                        .setCancelable(false)
                        .show();
            }
        });

        db = new DbHelper(this);




        listNoti= db.getAllNotifications();
        if (listNoti.size() > 0){
            ((ViewSwitcher)findViewById(R.id.notifSwitcher)).setDisplayedChild(1);
            rv=(RecyclerView)findViewById(R.id.notifRV);
            rv.setLayoutManager(new LinearLayoutManager(this));
            MyAdapter adapter=new MyAdapter(listNoti);
            rv.setAdapter(adapter);

        }


    }




    @Override
    protected void onPause() {
        super.onPause();
        String pro=new Gson().toJson( DrawerActivity.pro);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(RESP,pro).apply();
    }

    @Override
    public void onMailSent() {

    }

    @Override
    public void onError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(NotificationsActivity.this).setTitle("Error").setMessage(DrawerActivity.NO_HEMOS_PODIDO_ESTABLECER_COMUNICACION_ASEGURESE_QUE_SUS_DATOS_SON_CORRECTOS_E_INTENTE_NUEVAMENTE +e.toString()).setPositiveButton("OK", null).show();
            }
        });
    }

    @Override
    public void onResponseArrived(String service, String command, String response, Mailer mailer) {

        File file = new File(response);
        final HistoryEntry entry = new HistoryEntry(service, command, file.toURI().toString(), mailer.getResponseTimestamp());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HistoryManager.getSingleton().addToHistory(entry);
                HistoryManager.getSingleton().setCurrentPage(entry);
                DrawerActivity.open = true;
                finish();
            }
        });

        if (mailer.mincache !=null) {
            db.addCache(new StringHelper().clearString(command), dataHelper.addMinutes(mailer.mincache), new File(response).toURI().toString());
        }

        if (mailer.ext != null) {

            ProfileInfo pi = new Gson().fromJson( mailer.ext,ProfileInfo.class);

                /*Accion para anadir notifiaciones */
            if (pi.notifications.length > 0){
                db.addNotification(pi.notifications);



            }

            new PrefsManager(). saveData("mailbox", NotificationsActivity.this, pi.mailbox);
            new PrefsManager(). saveData("type_img", NotificationsActivity.this, pi.img_quality);
        }
    }



    @Override
    public void onErrorHttp(String error) {

    }

    @Override
    public void onResponseSimpleHttp(String response) {

    }

    @Override
    public void onResponseArrivedHttp(String service, String command, String response, MultipartHttp multipartHttp) {


        File file = new File(response);
        final HistoryEntry entry = new HistoryEntry(service, command, file.toURI().toString(), multipartHttp.getResponseTimestamp());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HistoryManager.getSingleton().addToHistory(entry);
                HistoryManager.getSingleton().setCurrentPage(entry);
                finish();
            }
        });

        if (multipartHttp.mincache !=null) {
            db.addCache(new StringHelper().clearString(command), dataHelper.addMinutes(multipartHttp.mincache), new File(response).toURI().toString());
        }

        if (multipartHttp.ext != null) {

            ProfileInfo pi = new Gson().fromJson( multipartHttp.ext,ProfileInfo.class);

                /*Accion para anadir notifiaciones */
            if (pi.notifications.length > 0){
                db.addNotification(pi.notifications);



            }

            new PrefsManager(). saveData("mailbox", NotificationsActivity.this, pi.mailbox);
            new PrefsManager(). saveData("type_img", NotificationsActivity.this, pi.img_quality);
        }
    }


    private class MyAdapter extends RecyclerView.Adapter
    {

        ArrayList<Notifications> notiList;//esta





        public  MyAdapter(ArrayList<Notifications> listNoti){
            this.notiList = listNoti;
        }
        public static final String DD_MM_YYYY_HH_MMAA = "dd/MM/yyyy hh:mmaa";
        public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notif_item, parent, false);

            MyAdapter.RecentHolder vh = new MyAdapter.RecentHolder(v);
            vh.type =(TextView)v.findViewById(R.id.entry_type);
            vh.date=(TextView)v.findViewById(R.id.entry_date);
            vh.title= (TextView) v.findViewById(R.id.entry_title);
            vh.dlbtn = (ImageView) v.findViewById(R.id.dlbtn);
            return vh;

        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final Notifications fdb = listNoti.get(holder.getAdapterPosition());//aki
            String timeStamp=fdb.getReceived();
            try {
                Date dateTime = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).parse(timeStamp);
                timeStamp = new SimpleDateFormat(DD_MM_YYYY_HH_MMAA).format(dateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ((MyAdapter.RecentHolder)holder).date.setText(timeStamp.toLowerCase());
            String mystring=fdb.getService().split(" ")[0].toLowerCase();
            ((MyAdapter.RecentHolder)holder).type.setText(mystring.substring(0,1).toUpperCase() + mystring.substring(1));


            final SpannableStringBuilder sb = new SpannableStringBuilder(fdb.getText());


            ((MyAdapter.RecentHolder)holder).title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fdb.getLink()!=null && !fdb.getLink().isEmpty()) {

                        comunication.execute(NotificationsActivity.this, fdb.getLink(), fdb.getLink(), false, fdb.getLink(), NotificationsActivity.this,NotificationsActivity.this);



                    }
                    db.setRead(String.valueOf(fdb.getId()));
                    listNoti.get(holder.getAdapterPosition()).setRead("1");
                    notifyItemChanged(holder.getAdapterPosition());

                   /// notifyDataSetChanged();



                }
            });

            if(fdb.getRead().equals("1"))
            {

                ((MyAdapter.RecentHolder)holder).title.setText(sb);
            }
            else
            {

                final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
                sb.setSpan(bss, 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold
                ((MyAdapter.RecentHolder)holder).title.setText(sb);
            }
            ((MyAdapter.RecentHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ((MyAdapter.RecentHolder)holder).dlbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                  db.delBy("notifications","_id", String.valueOf(fdb.getId()));

                    listNoti.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());



                }
            });
        }


        @Override
        public int getItemCount() {
            return listNoti.size();
        }
        private class RecentHolder extends RecyclerView.ViewHolder
        {
            TextView type, date, title;
            ImageView dlbtn;
            RecentHolder(View itemView) {
                super(itemView);
            }
        }
    }

}