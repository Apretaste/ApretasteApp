package com.example.apretaste;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiceDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);


        if (MainActivity.selectedService.icon != null && !MainActivity.selectedService.icon.equals("")) {
            try {
                File file = new File(getFilesDir(), MainActivity.selectedService.icon);
                Drawable dr = new BitmapDrawable(getResources(), BitmapFactory.decodeFile(file.getPath()));
                ((ImageView) findViewById(R.id.service_image)).setImageDrawable(dr);
            } catch (Exception e) {

            }

        } else
            ((ImageView) findViewById(R.id.service_image)).setImageResource(R.drawable.noicon);
        ((TextView) findViewById(R.id.service_name)).setText(MainActivity.selectedService.name);
        ((TextView) findViewById(R.id.service_description)).setText(MainActivity.selectedService.description);
        ((TextView) findViewById(R.id.service_category)).setText(MainActivity.selectedService.category);
        try
        {
            String timeStamp=MainActivity.selectedService.updated.substring(0, 10);
            Date dateTime = new SimpleDateFormat("yyyy-MM-dd").parse(timeStamp);
            timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(dateTime);
            ((TextView) findViewById(R.id.service_version)).setText(timeStamp);
        }
        catch (Exception ignored)
        {
            ((TextView) findViewById(R.id.service_version)).setText("-");
        }
        ((TextView) findViewById(R.id.service_creator)).setText(MainActivity.selectedService.creator);


    }
}