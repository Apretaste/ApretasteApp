package com.example.apretaste;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.apretaste.Settings.SettingsActivity;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements HistoryManager.HistoryListener, Mailerlistener{
    public static final String PRESIONE_NUEVAMENTE_PARA_SALIR = "Presione nuevamente para salir";
    public static final String BOTON_ACTULIZAR = "Botón actulizar";
    public static final String HISTORYURL = "historyurl";
    public static final String TEXT_HTML = "text/html";
    public static final String HTML_HTML = "<html></html>";
    public static final String LOCALURL = "localurl";
    public static final String APRETASTE = "apretaste";
    public static final String RESP = "resp";
    public static final String BUSCAR = "Buscar";
    public static final String ACEPTAR = "Aceptar";
    public static final String ABRIR = "Abrir";
    public static final String DETALLES = "Detalles";
    public static final String DATE = "date";
    public static final String PATH = "path";
    public static final String COMMAND = "command";
    public static final String SERVICE = "service";
    public static final String HISTORY = "history";
    public static final String ESTAMOS_BUSCANDO_NUEVO_SERVICIOS_CHATS_NOTIFICACIONES_Y_CAMBIOS_EN_SU_PERFIL_POR_FAVOR_SEA_PACIENTE_Y_NO_CIERRE_LA_APLICACION = "Estamos buscando nuevo servicios, chats, notificaciones y cambios en su perfil. Por favor sea paciente y no cierre la aplicación.";
    public static final String PERFIL_STATUS = "perfil status ";
    public static final String PASS = "pass";
    public static final String USER = "user";
    public static final String APP_MAILGUN_APRETASTE_COM = "app@mailgun.apretaste.com";
    public static final String MAILBOX = "mailbox";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String SIMPLE_DATE_FORMAT = "SimpleDateFormat";
    public static final String OK = "Ok";
    public static final String NO_HEMOS_PODIDO_ESTABLECER_COMUNICACION_ASEGURESE_QUE_SUS_DATOS_SON_CORRECTOS_E_INTENTE_NUEVAMENTE = "No hemos podido establecer comunicación. Asegúrese que sus datos son correctos e intente nuevamente. ";
    public static final String ERROR = "Error";
    public static boolean pressed = false;

    ImageView profilePict;

    WebView wv;
    ViewSwitcher vsw;
    LinearLayout profileLayout;
    public static ProfileInfo pro;
    HistoryManager hm;
    ServicesAdapter adapter;
    String mailbox, user, password;
    TextView notifCount;
    GridView gridview;
    
    FloatingActionButton fabSync, fabNotifications, fabSettings, fabHistory;


    public static boolean needsReload=false, needsreloadServices=false;

    public static ProfileInfo.Services selectedService=null;
    @Override
    public void onHistoryChange(HistoryEntry newUrl) {
        if(newUrl!=null)
        {
            appbar.setTitle(newUrl.service.substring(0,1).toUpperCase()+newUrl.service.substring(1).toLowerCase());
            showHomeButton(true);
            //"file:///android_asset/faq.html"
            wv.loadUrl(newUrl.path);
            vsw.setDisplayedChild(1);
            fabSync.setVisibility(View.GONE);
            return;
        }
        vsw.setDisplayedChild(0);
        fabSync.setVisibility(View.VISIBLE);
//        profileLayout.setVisibility(View.VISIBLE);
        showHomeButton(false);
    }

    @Override
    public void onMailSent() {

    }

    private boolean isShowingHome=false;
    private void showHomeButton(final boolean show)
    {
        isShowingHome=show;
      /*  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            AnimatorSet as=new AnimatorSet();
            if(show)
            {
                as.setDuration(80);
                as.playTogether(
                        ObjectAnimator.ofFloat(profilePict,"scaleX",1,0),
                        ObjectAnimator.ofFloat(profilePict,"scaleY",1,0),
                        ObjectAnimator.ofFloat(fabSettings,"scaleY",1,0),
                        ObjectAnimator.ofFloat(profilePict,"scaleY",1,0),

                );
            }
            else
            {
                as.setDuration(120);
                as.playTogether(
                        ObjectAnimator.ofFloat(profilePict,"scaleY",1,0.65f),
                        ObjectAnimator.ofFloat(profileLayout,"alpha",1,0.4f),
                        ObjectAnimator.ofFloat(profilePict,"scaleX",1,0)
                );
            }
            as.start();
        }
        else*/
        {
            profilePict.setVisibility(show?View.GONE:View.VISIBLE);
            profileLayout.setVisibility(show?View.GONE:View.VISIBLE);
            fabSettings.setVisibility(show?View.GONE:View.VISIBLE);
            if(!show)appbar.setTitle("");
        }
    }

    @Override
    public void onError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(MainActivity.this).setTitle(ERROR).setMessage(NO_HEMOS_PODIDO_ESTABLECER_COMUNICACION_ASEGURESE_QUE_SUS_DATOS_SON_CORRECTOS_E_INTENTE_NUEVAMENTE +e.toString()).setPositiveButton(OK, null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(SettingsActivity.terminating)
        {
            SettingsActivity.terminating=false;
         //   startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "Su sesión ha sido cerrada.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(SettingsActivity.terminating)
            return;
        if(needsReload)
            showProfileInfo(needsreloadServices);
    }

    @Override
    public void onResponseArrived(String service, String command, String response, Mailer mailer) {
        Log.e("pro","rarived");
        if(mailer.getReturnContent())
        {
            Log.e("pro","retcontent");
            ProfileInfo pinfo;
            try
            {
               pinfo =new Gson().fromJson(response,ProfileInfo.class);
                Log.e("pro","pinfo jsoned");
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Ha ocurrido un error en el servidor.", Toast.LENGTH_SHORT).show();
                Log.e("pro","error jsoning pinfo");
                return;
            }
            Log.e("pro","updating");
            pro.update(pinfo);
            Log.e("pro","jsoning");
            String prof=new Gson().toJson( MainActivity.pro);
            Log.e("pro","saving");
            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString(RESP,prof).apply();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("pro","refreshing");
                   // gridview.setAdapter(null);
                    showProfileInfo(true);
                   // gridview.setAdapter(adapter);
                }
            });
            return;
        }

        File file = new File(response);
        final HistoryEntry entry = new HistoryEntry(service, command, file.toURI().toString(), mailer.getResponseTimestamp());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    HistoryManager.getSingleton().addToHistory(entry);
                    HistoryManager.getSingleton().setCurrentPage(entry);
                }
            });
    }

    @SuppressWarnings("unused")
    private class JSI
    {
        public static final String CANCELAR = "Cancelar";
        public static final String INFLATE_PARAMS = "InflateParams";
        public static final String TRUE = "true";
        public static final String DOACTION = "doaction";

        @JavascriptInterface
        public void doaction(final String command, final String type, final String help, final boolean waiting)
        {
            Log.e(DOACTION,type+" "+String.valueOf(waiting)+" "+command+" "+help);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type.equalsIgnoreCase(TRUE))
                    {
                        final View v=getLayoutInflater().inflate(R.layout.dialog_prompt,null);
                        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setView(v).setMessage("Escriba un texto para ejecutar dentro del servicio")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String extra=((EditText)v.findViewById(R.id.command_text)).getText().toString();
                                        new Mailer(MainActivity.this, command.split(" ")[0],command+" "+extra,!waiting,help,MainActivity.this).execute();//ejecuta la tarea de login
                                    }
                                }).setNegativeButton("Cancelar",null).create();
                        alertDialog.show();
                    }
                    else
                    {
                        new Mailer(MainActivity.this, command.split(" ")[0],command,!waiting,help,MainActivity.this).execute();
                    }
                }
            });

        }
    }

    @Override
    protected void onDestroy() {
        if(SettingsActivity.terminating)
        {
            super.onDestroy();
            return;
        }
        HistoryManager.getSingleton().removeListener(this);
        String enties=new Gson().toJson(HistoryManager.getSingleton().entries);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(HISTORY,enties).apply();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(SettingsActivity.terminating)
            return;
        String enties=new Gson().toJson(HistoryManager.getSingleton().entries);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(HISTORY,enties).apply();
    }

    Toolbar appbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_material);

        fabHistory=(FloatingActionButton)findViewById(R.id.ib_recent_main);
        fabNotifications=(FloatingActionButton)findViewById(R.id.ib_notifications_main);
        fabSettings=(FloatingActionButton)findViewById(R.id.ib_settings_main);
        fabSync=(FloatingActionButton)findViewById(R.id.ib_sync_main);
        profilePict=(ImageView)findViewById(R.id.profile_pict);
        appbar=(Toolbar)findViewById(R.id.tb);
        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadhome();
            }
        });
        notifCount=(TextView) findViewById(R.id.notifCunter);
        vsw=(ViewSwitcher)findViewById(R.id.homeSwitcher);
        String pref;
        mailbox=PreferenceManager.getDefaultSharedPreferences(this).getString(MAILBOX, APP_MAILGUN_APRETASTE_COM);
        user=PreferenceManager.getDefaultSharedPreferences(this).getString(USER,"");
        password=PreferenceManager.getDefaultSharedPreferences(this).getString(PASS,"");
        if((pref=PreferenceManager.getDefaultSharedPreferences(this).getString(RESP,null))!=null)
        {
            try
            {
                pro=new Gson().fromJson(pref,ProfileInfo.class);
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Ha ocurrido un error en el servidor.", Toast.LENGTH_SHORT).show();
                PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
                SettingsActivity.terminating=true;
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                return;
            }

        }
        pro.profile.humanizeData();



        showProfileInfo(false);

        findViewById(R.id.ib_sync_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setMessage("Desea refrescar la lista de servicios y notificaciones? Esto puede tomar varios minutos").setPositiveButton("Refrescar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Mailer mailer=new Mailer(MainActivity.this, null, PERFIL_STATUS +pro.timestamp, false, null, MainActivity.this);
                                mailer.setReturnContent(true).setSaveInternal(true);
                               // mailer.setCustomText(ESTAMOS_BUSCANDO_NUEVO_SERVICIOS_CHATS_NOTIFICACIONES_Y_CAMBIOS_EN_SU_PERFIL_POR_FAVOR_SEA_PACIENTE_Y_NO_CIERRE_LA_APLICACION);
                                mailer.setShowCommand(false);
                                //mailer.setShowStatus(false);
                                mailer.execute();
                            }
                        }
                ).setNegativeButton("Cancelar",null).show();

            }
        });
        String history=PreferenceManager.getDefaultSharedPreferences(this).getString(HISTORY,null);
        List<LinkedTreeMap<String,String>> enties=new Gson().fromJson(history,List.class);
        hm=HistoryManager.getSingleton();
        if(enties!=null)
        {
            hm.entries.clear();
            for (Map<String, String> val :
                    enties) {
                hm.entries.add(new HistoryEntry(val.get(SERVICE),val.get(COMMAND),val.get(PATH),new Date(val.get(DATE))));
            }
        }


        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setOnScrollListener(new AbsListView.OnScrollListener() {

            int laststate=0;
            boolean ismaximized=true;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState==SCROLL_STATE_IDLE)
                {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                        AnimatorSet as=new AnimatorSet();
                        as.setDuration(200);
                                as.playTogether(
                                        ObjectAnimator.ofFloat(fabSync,"scaleY",0,1),
                                        ObjectAnimator.ofFloat(fabSync,"scaleX",0,1)
                                );
                        as.start();
                    }
                }
                else
                {
                    if(laststate==SCROLL_STATE_IDLE)
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                        AnimatorSet as=new AnimatorSet();
                        as.setDuration(200);
                            as.playTogether(
                                    ObjectAnimator.ofFloat(fabSync,"scaleY",1,0),
                                    ObjectAnimator.ofFloat(fabSync,"scaleX",1,0));
                        as.start();

                    }
                }
                laststate=scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        adapter=new ServicesAdapter();
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                new AlertDialog.Builder(MainActivity.this).setItems(new CharSequence[]{ABRIR, BUSCAR, DETALLES}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg, int which) {
                        switch (which)
                        {
                            case 0:{
                                if(!pro.services[position].isUsed())
                                {
                                    pro.services[position].setUsed();
                                    adapter.notifyDataSetChanged();
                                    String pro=new Gson().toJson( MainActivity.pro);
                                    PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString(RESP,pro).apply();
                                }

                                new Mailer(MainActivity.this, pro.services[position].name,pro.services[position].name,false,pro.services[position].description,MainActivity.this).execute();//ejecuta la tarea de login
                            }break;
                            case 1:{
                                if(!pro.services[position].isUsed())
                                {
                                    pro.services[position].setUsed();
                                    adapter.notifyDataSetChanged();
                                    String pro=new Gson().toJson( MainActivity.pro);
                                    PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString(RESP,pro).apply();
                                }
                                final View v=getLayoutInflater().inflate(R.layout.dialog_prompt,null);
                                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setView(v).setMessage("Escriba un texto para ejecutar dentro del servicio")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String extra=((EditText)v.findViewById(R.id.command_text)).getText().toString();
                                                new Mailer(MainActivity.this, pro.services[position].name,pro.services[position].name+" "+extra,false,pro.services[position].description,MainActivity.this).execute();//ejecuta la tarea de login
                                            }
                                        }).setNegativeButton("Cancelar",null).show();
                            }break;
                            case 2:{
                                selectedService=pro.services[position];
                                startActivity(new Intent(MainActivity.this,ServiceDetails.class));
                                //new AlertDialog.Builder(MainActivity.this).setMessage(pro.services[position].description).show();
                            }break;
                        }
                    }
                }).show();
                String pro=new Gson().toJson( MainActivity.pro);
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString(RESP,pro).apply();
            }
        });




        wv=(WebView)findViewById(R.id.mainWebView);
        profileLayout=(LinearLayout)findViewById(R.id.profile_text_layout);
        wv.getSettings().setJavaScriptEnabled(true);
        //noinspection SpellCheckingInspection
        wv.addJavascriptInterface(new JSI(), APRETASTE);
        if(HistoryManager.getSingleton().currentUrl==null)
            loadhome();
            HistoryManager.getSingleton().addListener(this);


        profilePict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("trans","transed");
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);

                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this,

                        // Now we provide a list of Pair items which contain the view we can transitioning
                        // from, and the name of the view it is transitioning to, in the launched activity
                        new Pair<View, String>(profilePict,
                                ProfileActivity.VIEW_NAME_HEADER_IMAGE),
                        new Pair<View, String>(findViewById(R.id.profile_name),
                                ProfileActivity.VIEW_NAME_HEADER_TITLE));

                // Now we can start the Activity, providing the activity options as a bundle
                ActivityCompat.startActivity(MainActivity.this, intent, activityOptions.toBundle());



              //  startActivity(new Intent(MainActivity.this , ProfileActivity.class));
            }
        });
        
    }

    @SuppressLint("SetTextI18n")
    private void showProfileInfo(boolean reloadServices) {
        ((TextView)findViewById(R.id.profile_name)).setText("@"+pro.username);
        ((TextView)findViewById(R.id.profile_credit)).setText("§"+String.valueOf(pro.credit));
        if(reloadServices)
            adapter.notifyDataSetChanged();
        if(pro.profile.picture!=null && !pro.profile.picture.isEmpty())
        {
            try {
                File file = new File(getFilesDir(), pro.profile.picture);
               // Drawable dr=new BitmapDrawable(getResources(), BitmapFactory.decodeFile(file.getPath()));
                RoundedBitmapDrawable dr=RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeFile(file.getPath()));
                dr.setCircular(true);
                profilePict.setImageDrawable(dr);
            }
            catch (Exception ignored)
            {

            }
        }
        else
            profilePict.setImageResource(R.drawable.ic_person_black_24dp);
        int nReadCount=0;
        for(ProfileInfo.Notifications not:pro.notifications)
        {
            if(!not.isRead())
                nReadCount++;
        }
        if(nReadCount>0)
        {
            notifCount.setVisibility(View.VISIBLE);
            notifCount.setText(String.valueOf(nReadCount));
        }
        else
            notifCount.setVisibility(View.GONE);
    }

    private void loadhome()
    {
        wv.loadDataWithBaseURL(LOCALURL,
                HTML_HTML
                , TEXT_HTML,"UTF8", HISTORYURL);
        HistoryManager.getSingleton().setCurrentPage(null);
    }



    public void open_activitys(View view) {
        switch (view.getId()){
            case R.id.ib_sync_main:
                Toast.makeText(this, BOTON_ACTULIZAR,Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_recent_main:
                startActivity(new Intent(MainActivity.this , RecentActivity.class));
            break;
            case R.id.ib_profile_main:
                startActivity(new Intent(MainActivity.this , ProfileActivity.class));

                break;
            case R.id.ib_settings_main:
                startActivity(new Intent(MainActivity.this , SettingsActivity.class));

                break;
            case R.id.ib_notifications_main:
                startActivity(new Intent(MainActivity.this , NotificationsActivity.class));

                break;
        }
    }

    @Override
    protected void onStart() {
        pressed = false;
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        if(isShowingHome)
        {
            loadhome();
            return;
        }
        if (!pressed){
            Toast.makeText(getBaseContext(), PRESIONE_NUEVAMENTE_PARA_SALIR,Toast.LENGTH_SHORT).show();
            pressed = true;
        }else {
            super.onBackPressed();

        }
    }

    private class ServicesAdapter extends BaseAdapter {
        public int getCount() {
            return pro.services.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        @SuppressLint("InflateParams")
        public View getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout rl;

                if (convertView == null) {
                    // if it's not recycled, initialize some attributes
                    rl=(RelativeLayout)getLayoutInflater().inflate(R.layout.material_service_item,null);
                    rl.findViewById(R.id.srvice_new).setVisibility(!pro.services[position].isUsed()?View.VISIBLE:View.INVISIBLE);
                    ((TextView)rl.findViewById(R.id.service_name)).setText(pro.services[position].name);
                    if(pro.services[position].icon!=null && !pro.services[position].icon.equals(""))
                    {
                        try {
                            File file = new File(getFilesDir(), pro.services[position].icon);
                            Drawable dr=new BitmapDrawable(getResources(), BitmapFactory.decodeFile(file.getPath()));
                            ((ImageView)rl.findViewById(R.id.service_image)).setImageDrawable(dr);
                        }
                        catch (Exception ignored)
                        {

                        }
                    }
                    else
                        ((ImageView)rl.findViewById(R.id.service_image)).setImageResource(R.drawable.noicon);
                    //  float density = getResources().getDisplayMetrics().density;
                    //  rl.findViewById(R.id.service_image).setLayoutParams(new RelativeLayout.LayoutParams(85*(int)density, 85*(int)density));
                    //  rl.findViewById(R.id.service_image).setLayoutParams(new RelativeLayout.MarginLayoutParams(85*(int)density, 85*(int)density));
                } else {
                    rl = (RelativeLayout) convertView;
                    rl.findViewById(R.id.srvice_new).setVisibility(!pro.services[position].isUsed()?View.VISIBLE:View.INVISIBLE);
                    ((TextView)rl.findViewById(R.id.service_name)).setText(pro.services[position].name);
                    if(pro.services[position].icon!=null && !pro.services[position].icon.equals(""))
                    {
                        try {
                            File file = new File(getFilesDir(), pro.services[position].icon);
                            Drawable dr=new BitmapDrawable(getResources(), BitmapFactory.decodeFile(file.getPath()));
                            ((ImageView)rl.findViewById(R.id.service_image)).setImageDrawable(dr);
                        }
                        catch (Exception ignored)
                        {

                        }

                    }
                    else
                        ((ImageView)rl.findViewById(R.id.service_image)).setImageResource(R.drawable.noicon);
                }

            return rl;
        }

        // references to our images
       /* private Integer[] mThumbIds = {
                R.drawable.sample_2, R.drawable.sample_3,
                R.drawable.sample_4, R.drawable.sample_5,
                R.drawable.sample_6, R.drawable.sample_7,
                R.drawable.sample_0, R.drawable.sample_1,
                R.drawable.sample_2, R.drawable.sample_3,
                R.drawable.sample_4, R.drawable.sample_5,
                R.drawable.sample_6, R.drawable.sample_7,
                R.drawable.sample_0, R.drawable.sample_1,
                R.drawable.sample_2, R.drawable.sample_3,
                R.drawable.sample_4, R.drawable.sample_5,
                R.drawable.sample_6, R.drawable.sample_7
        };*/
    }


    //comprime un txt con el comando dentro de un archivo


}
