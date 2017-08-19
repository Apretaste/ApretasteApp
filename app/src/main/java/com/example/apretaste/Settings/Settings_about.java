package com.example.apretaste.Settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apretaste.MainActivity;
import com.example.apretaste.NotificationsActivity;
import com.example.apretaste.ProfileActivity;
import com.example.apretaste.R;
import com.example.apretaste.RecentActivity;

public class Settings_about extends AppCompatActivity {
    ImageButton back_about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_about);
        WebView wv=(WebView)findViewById(R.id.about_text);
        wv.loadDataWithBaseURL("about:apretaste",getResources().getString(R.string.about_text,MainActivity.pro.username),"text/html", Xml.Encoding.UTF_8.name(),null);
    }
}
