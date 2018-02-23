package apretaste.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apretaste.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import apretaste.Helper.DbHelper;
import apretaste.activity.DrawerActivity;

public class ServiceDetails extends AppCompatActivity {
    DbHelper db;
    String fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        db = DbHelper.getSingleton(this);

        fav = db.getServiceById(DrawerActivity.idservice, "fav");

        findViewById(R.id.btn_favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fav.equals("0")) {

                    db.favorite(DrawerActivity.idservice, true);

                    ((ImageView) findViewById(R.id.star)).setImageResource(R.drawable.star_selected);

                    fav = "1";
                    ((TextView) findViewById(R.id.tvFavText)).setText("Quitar de favorito");
                } else {
                    db.favorite(DrawerActivity.idservice, false);
                    ((ImageView) findViewById(R.id.star))

                            .setImageResource(R.drawable.star_unselected);


                    fav = "0";
                    ((TextView) findViewById(R.id.tvFavText)).setText("Hacer Favorito");
                }


            }
        });
        if (db.getServiceById(DrawerActivity.idservice, "icon") != null && !db.getServiceById(DrawerActivity.idservice, "icon").equals("")) {
            try {
                File file = new File(getFilesDir(), db.getServiceById(DrawerActivity.idservice, "icon"));
                Drawable dr = new BitmapDrawable(getResources(), BitmapFactory.decodeFile(file.getPath()));
                ((ImageView) findViewById(R.id.service_image)).setImageDrawable(dr);
            } catch (Exception e) {

            }

        } else
            ((ImageView) findViewById(R.id.service_image)).setImageResource(R.drawable.noicon);
        ((TextView) findViewById(R.id.service_name)).setText(db.getServiceById(DrawerActivity.idservice, "name"));
        ((TextView) findViewById(R.id.service_description)).setText(db.getServiceById(DrawerActivity.idservice, "des"));
        ((TextView) findViewById(R.id.service_category)).setText(db.getServiceById(DrawerActivity.idservice, "category"));


        if (db.getServiceById(DrawerActivity.idservice, "fav").equals("1")) {
            ((ImageView) findViewById(R.id.star)).setImageResource(R.drawable.star_selected);
            ((TextView) findViewById(R.id.tvFavText)).setText("Quitar de favorito");


        }

        try {
            String timeStamp = db.getServiceById(DrawerActivity.idservice, "updated").substring(0, 10);

            Date dateTime = new SimpleDateFormat("yyyy-MM-dd").parse(timeStamp);
            timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(dateTime);
            ((TextView) findViewById(R.id.service_version)).setText(timeStamp);
        } catch (Exception ignored) {
            ((TextView) findViewById(R.id.service_version)).setText("-");

        }
        ((TextView) findViewById(R.id.service_creator)).setText(db.getServiceById(DrawerActivity.idservice, "creator"));


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ServiceDetails.this, DrawerActivity.class));
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}