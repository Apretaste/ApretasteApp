package com.example.apretaste;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.apretaste.Settings.SettingsActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecentActivity extends AppCompatActivity {

    HistoryManager hm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hm=HistoryManager.getSingleton();
        setContentView(R.layout.activity_recent);
        if(hm.entries.size()>0) {
            ((ViewSwitcher) findViewById(R.id.notifSwitcher)).setDisplayedChild(1);
            RecyclerView rv = (RecyclerView) findViewById(R.id.recentsRV);
            LinearLayoutManager lm=new LinearLayoutManager(this);
            lm.setStackFromEnd(true);
            lm.setReverseLayout(true);
            rv.setLayoutManager(lm);
            RvAdapter adapter = new RvAdapter();
            rv.setAdapter(adapter);
        }
    }

    private class RvAdapter extends RecyclerView.Adapter
    {

        public static final String DD_MM_YYYY_HH_MMAA = "dd/MM/yyyy hh:mmaa";

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_item, parent, false);

            RecentHolder vh = new RecentHolder(v);
            vh.type =(TextView)v.findViewById(R.id.entry_type);
            vh.date=(TextView)v.findViewById(R.id.entry_date);
            vh.title= (TextView) v.findViewById(R.id.entry_title);
            vh.delBtn=  v.findViewById(R.id.delBtn);
            return vh;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

            String timeStamp = new SimpleDateFormat(DD_MM_YYYY_HH_MMAA).format(hm.entries.get(position).date);

            ((RecentHolder)holder).date.setText(timeStamp.toLowerCase());
            String mystring=hm.entries.get(position).service.split(" ")[0].toLowerCase();
            ((RecentHolder)holder).type.setText(mystring.substring(0,1).toUpperCase() + mystring.substring(1));
            ((RecentHolder)holder).title.setText(hm.entries.get(position).command);
            ((RecentHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HistoryManager.getSingleton().setCurrentPage(hm.entries.get(holder.getAdapterPosition()));
                    finish();
                }
            });
            ((RecentHolder)holder).delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new File(hm.entries.get(holder.getAdapterPosition()).path).delete();
                    hm.entries.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return hm.entries.size();
        }
        private class RecentHolder extends RecyclerView.ViewHolder
        {
            TextView type, date, title;
            View delBtn;
            RecentHolder(View itemView) {
                super(itemView);
            }
        }
    }

}
