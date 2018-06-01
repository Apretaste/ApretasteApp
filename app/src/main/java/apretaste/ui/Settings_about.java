package apretaste.ui;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;


import com.example.apretaste.R;


public class Settings_about extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_about);
        WebView wv;
        wv = (WebView) findViewById(R.id.about_text);
        wv.getSettings().setDisplayZoomControls(true);
        wv.loadUrl("file:///android_asset/index.html");   // now it will not fail here
        // wv.loadDataWithBaseURL("about:apretaste",getResources().getString(R.string.about_text,MainActivity.pro.username),"text/html", Xml.Encoding.UTF_8.name(),null);



    }


}
