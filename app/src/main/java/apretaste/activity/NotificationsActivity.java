package apretaste.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.apretaste.R;

import apretaste.HistoryEntry;
import apretaste.HistoryManager;
import apretaste.email.Mailer;
import apretaste.email.Mailerlistener;
import com.google.gson.Gson;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationsActivity extends AppCompatActivity implements Mailerlistener {

    public static final String RESP = "resp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        if(MainActivity.pro.notifications.length>0)
        {
            ((ViewSwitcher)findViewById(R.id.notifSwitcher)).setDisplayedChild(1);
            RecyclerView rv=(RecyclerView)findViewById(R.id.notifRV);
            rv.setLayoutManager(new LinearLayoutManager(this));
            RvAdapter adapter=new RvAdapter();
            rv.setAdapter(adapter);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.needsReload=true;
        String pro=new Gson().toJson( MainActivity.pro);
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
                new AlertDialog.Builder(NotificationsActivity.this).setTitle("Error").setMessage(MainActivity.NO_HEMOS_PODIDO_ESTABLECER_COMUNICACION_ASEGURESE_QUE_SUS_DATOS_SON_CORRECTOS_E_INTENTE_NUEVAMENTE +e.toString()).setPositiveButton(MainActivity.OK, null).show();
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
                finish();
            }
        });
    }

    private class RvAdapter extends RecyclerView.Adapter
    {

        public static final String DD_MM_YYYY_HH_MMAA = "dd/MM/yyyy hh:mmaa";
        public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notif_item, parent, false);

            RvAdapter.RecentHolder vh = new RvAdapter.RecentHolder(v);
            vh.type =(TextView)v.findViewById(R.id.entry_type);
            vh.date=(TextView)v.findViewById(R.id.entry_date);
            vh.title= (TextView) v.findViewById(R.id.entry_title);
            vh.readBtn = (ImageView) v.findViewById(R.id.read);
            return vh;
            
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            String timeStamp=MainActivity.pro.notifications[position].received;
            try {
                Date dateTime = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).parse(timeStamp);
                timeStamp = new SimpleDateFormat(DD_MM_YYYY_HH_MMAA).format(dateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ((RvAdapter.RecentHolder)holder).date.setText(timeStamp.toLowerCase());
            String mystring=MainActivity.pro.notifications[position].service.split(" ")[0].toLowerCase();
            ((RvAdapter.RecentHolder)holder).type.setText(mystring.substring(0,1).toUpperCase() + mystring.substring(1));

           // ((RvAdapter.RecentHolder)holder).type.setText(MainActivity.pro.notifications[position].service.split(" ")[0].toUpperCase());
            final SpannableStringBuilder sb = new SpannableStringBuilder(MainActivity.pro.notifications[position].text);


            ((RvAdapter.RecentHolder)holder).title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(MainActivity.pro.notifications[holder.getAdapterPosition()].link!=null && !MainActivity.pro.notifications[holder.getAdapterPosition()].link.isEmpty())
                    new Mailer(NotificationsActivity.this,MainActivity.pro.notifications[holder.getAdapterPosition()].link,MainActivity.pro.notifications[holder.getAdapterPosition()].link,false,MainActivity.pro.notifications[holder.getAdapterPosition()].link,NotificationsActivity.this).execute();//ejecuta la tarea de login
                    MainActivity.pro.notifications[holder.getAdapterPosition()].setRead();
                    notifyDataSetChanged();
                }
            });

            if(MainActivity.pro.notifications[position].isRead())
            {
                ((RecentHolder)holder).readBtn.setImageResource(R.drawable.ic_mail_outline_black_24dp);
                ((RvAdapter.RecentHolder)holder).title.setText(sb);
            }
          else
            {
                ((RecentHolder)holder).readBtn.setImageResource(R.drawable.ic_mail_black_24dp);
               final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
               sb.setSpan(bss, 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold
                ((RvAdapter.RecentHolder)holder).title.setText(sb);
            }
            ((RvAdapter.RecentHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   //MainActivity.pro.notifications[holder.getAdapterPosition()].service;
                }
            });
            ((RvAdapter.RecentHolder)holder).readBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   MainActivity.pro.notifications[holder.getAdapterPosition()].toggleRead();
                    notifyItemChanged(holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return MainActivity.pro.notifications.length;
        }
        private class RecentHolder extends RecyclerView.ViewHolder
        {
            TextView type, date, title;
            ImageView readBtn;
            RecentHolder(View itemView) {
                super(itemView);
            }
        }
    }


}
