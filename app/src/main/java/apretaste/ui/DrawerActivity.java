package apretaste.ui;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

import apretaste.Comunication.Comunication;
import apretaste.Comunication.ServicePsiphon;
import apretaste.Helper.DialogHelper;
import apretaste.Helper.InputTypeHelper;
import apretaste.ProfileInfo;

import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;


import com.example.apretaste.R;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import apretaste.Helper.DataHelper;
import apretaste.Helper.DbHelper;
import apretaste.Helper.AlertHelper;
import apretaste.Helper.NetworkHelper;
import apretaste.Helper.PrefsManager;
import apretaste.Helper.StringHelper;
import apretaste.HistoryEntry;
import apretaste.HistoryManager;
import apretaste.Comunication.email.Mailer;
import apretaste.Comunication.email.Mailerlistener;


import apretaste.Services;
import apretaste.Comunication.http.Httplistener;
import apretaste.Comunication.http.MultipartHttp;


public class DrawerActivity extends AppCompatActivity
        implements InputTypeHelper.InputTypeInterface, NavigationView.OnNavigationItemSelectedListener, HistoryManager.HistoryListener, Mailerlistener, Httplistener {
    public static boolean pressed = false;
    public static ProfileInfo pro;
    public static final String PASS = "pass";
    public static final String USER = "user";
    public static boolean needsReload = false;
    public String pref;
    DbHelper dbh;
    FloatingActionButton fabSync;
    public static final String COMMAND = "command";
    public static final String SERVICE = "service";
    public static final String DATE = "date";
    public static final String HISTORY = "history";
    public static final String PATH = "path";
    public static String idservice;
    NetworkHelper networkHelper = new NetworkHelper();
    AlertHelper alertHelper = new AlertHelper();
    DataHelper dataHelper = new DataHelper();
    ImageView profilePict;
    public ServiceAdapter serviceAdapter;
    String mailbox, user, password;
    public static final String MAILBOX = "mailbox";
    private int count;
    public static final String PERFIL_STATUS = "status ";
    public WebView wv;
    ViewSwitcher vsw;

    public static final String NO_HEMOS_PODIDO_ESTABLECER_COMUNICACION_ASEGURESE_QUE_SUS_DATOS_SON_CORRECTOS_E_INTENTE_NUEVAMENTE = "No hemos podido establecer comunicación , asegurese que los datos moviles esten encendidos , de lo contrario es problema de conexion con los servidores nauta , intentelo más tarde nuevamente";
    View hView;
    Toolbar toolbar;
    public static boolean open = false;
    private Toolbar appbar;
    private boolean isShowingHome = false;
    private String PRESIONE_NUEVAMENTE_PARA_SALIR = "Presione nuevamente para salir";
    private HistoryManager hm;
    private SearchView searchView;
    TextView count_noti;
    MenuItem searchItem;
    MenuItem updateItem;
    MenuItem donwloadItem;
    Comunication comunication = new Comunication();
    double latest;
    private GridView gridView;
    EditText etSearchview;
    private int laststate = 0;
    public static final String IMAGE = "image/*";
    private final static int REQUEST_STORAGE_PERMISSION = 1;
    private static final int REQUEST_IMAGE_GET = 0;
    Uri fullPhotoUri = null;
    Bitmap newBitmap = null;
    KProgressHUD dialog;
    ServicePsiphon servicePsiphon;
    boolean mBound = false;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        if (new PrefsManager().getData("type_conn", DrawerActivity.this).equals("internet") && !servicePsiphon.isConnected()) {
            startService(new Intent(this, ServicePsiphon.class));
            dialog = new DialogHelper().DialogRequest(DrawerActivity.this);
            dialog.show();
        }
        final Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
                                  @Override
                                  public void run() {
                                      if (servicePsiphon.isConnected()) {
                                          runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  dialog.dismiss();
                                                  comunication.setConnectedPsiphon();
                                                  t.purge();
                                              }
                                          });

                                      }
                                  }
                              },
                0,

                1000);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbh = DbHelper.getSingleton(this);
        fabSync = (FloatingActionButton) findViewById(R.id.fab);


        fabSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DrawerActivity.this).setMessage("Desea refrescar la lista de servicios y notificaciones? Esto puede tomar varios minutos")
                        .setPositiveButton("Refrescar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        comunication.setSaveInternal(true);
                                        comunication.setReturnContent(true);
                                        comunication.execute(DrawerActivity.this, null, PERFIL_STATUS, false, null,
                                                DrawerActivity.this, DrawerActivity.this);

                                    }
                                }
                        ).setNegativeButton("Cancelar", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        appbar = (Toolbar) findViewById(R.id.tb);
        vsw = (ViewSwitcher) findViewById(R.id.homeSwitcher);
        mailbox = PreferenceManager.getDefaultSharedPreferences(this).getString(MAILBOX, "interweb+ap@gmail.com");
        user = PreferenceManager.getDefaultSharedPreferences(this).getString(USER, "");
        password = PreferenceManager.getDefaultSharedPreferences(this).getString(PASS, "");
        if ((pref = PreferenceManager.getDefaultSharedPreferences(this).getString("resp", null)) != null) {
            try {
                pro = new Gson().fromJson(pref, ProfileInfo.class);

            } catch (Exception e) {
                Toast.makeText(this, "Ha ocurrido un error en el servidor.", Toast.LENGTH_SHORT).show();
                PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();
                SettingsActivity.terminating = true;
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }

        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        hView = navigationView.getHeaderView(0);

        count_noti = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_noti));





        /*Accin para abrir la activity perfil desde el header */
        profilePict = (ImageView) hView.findViewById(R.id.ivProfile);
        profilePict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comunication.execute(DrawerActivity.this, "PERFIL", "PERFIL EDITAR", false, "", DrawerActivity.this, DrawerActivity.this);

            }
        });

        showProfileInfo(hView, false);
        String history = PreferenceManager.getDefaultSharedPreferences(this).getString(HISTORY, null);
        List<LinkedTreeMap<String, String>> enties = new Gson().fromJson(history, List.class);
        hm = HistoryManager.getSingleton();
        if (enties != null) {
            hm.entries.clear();
            for (Map<String, String> val :
                    enties) {
                hm.entries.add(new HistoryEntry(val.get(SERVICE), val.get(COMMAND), val.get(PATH), new Date(val.get(DATE))));
            }
        }


        /*Accion para mostrar los servicios en el gridview*/

        serviceAdapter = new ServiceAdapter(DrawerActivity.this, dbh.getAllServices());
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(serviceAdapter);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean ismaximized = true;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        AnimatorSet as = new AnimatorSet();
                        as.setDuration(200);
                        as.playTogether(
                                ObjectAnimator.ofFloat(fabSync, "scaleY", 0, 1),
                                ObjectAnimator.ofFloat(fabSync, "scaleX", 0, 1)

                        );
                        as.start();
                    }


                } else {
                    if (laststate == SCROLL_STATE_IDLE)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            AnimatorSet as = new AnimatorSet();
                            as.setDuration(200);
                            as.playTogether(
                                    ObjectAnimator.ofFloat(fabSync, "scaleY", 1, 0),
                                    ObjectAnimator.ofFloat(fabSync, "scaleX", 1, 0));
                            as.start();

                        }
                }
                laststate = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(DrawerActivity.this).setItems(new CharSequence[]{"Abrir", "Buscar", "Detalles"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg, int which) {
                        switch (which) {
                            case 0: {
                                if (serviceAdapter.sevList.get(position).getUsed().equals("0")) {
                                    dbh.setUsed(serviceAdapter.sevList.get(position).getId());
                                    serviceAdapter.sevList.get(position).setUsed("1");
                                    serviceAdapter.notifyDataSetChanged();


                                }
                                String peticion = new StringHelper().clearString(serviceAdapter.sevList.get(position).getName());
                                if (!dbh.getAllCache(peticion, "peticion").equals("")) {
                                    try {
                                        if (dataHelper.compareTwoDates(dataHelper.getNowDateTime(), dbh.getAllCache(peticion, "cache"))) {
                                            Log.i("llamar", "abrir el cacheado");//Si la fecha de la db es superior a la actual
                                            final HistoryEntry entry = new HistoryEntry(peticion, null, dbh.getAllCache(peticion, "path"), null);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    HistoryManager.getSingleton().setCurrentPage(entry);
                                                    open = true;


                                                }
                                            });

                                        } else {
                                            Log.i("llamar", "llamar servicios y borra el cache");
                                            dbh.delBy("cache", "_id", dbh.getAllCache(peticion, "id"));

                                            comunication.execute(DrawerActivity.this, serviceAdapter.sevList.get(position).getName(), serviceAdapter.sevList.get(position).getName(), false, serviceAdapter.sevList.get(position).getDescription(), DrawerActivity.this, DrawerActivity.this);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {


                                    comunication.execute(DrawerActivity.this, serviceAdapter.sevList.get(position).getName(), serviceAdapter.sevList.get(position).getName(), false, serviceAdapter.sevList.get(position).getDescription(), DrawerActivity.this, DrawerActivity.this);

                                }
                                break;
                            }

                            case 1: {
                                if (serviceAdapter.sevList.get(position).getUsed().equals("0")) {

                                    serviceAdapter.sevList.get(position).setUsed("1");
                                    dbh.setUsed(serviceAdapter.sevList.get(position).getId());
                                    serviceAdapter.notifyDataSetChanged();

                                }


                                final View v = getLayoutInflater().inflate(R.layout.dialog_prompt, null);
                                final AlertDialog alertDialog = new AlertDialog.Builder(DrawerActivity.this).setView(v).setMessage("Escriba un texto para ejecutar dentro del servicio")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String extra = ((EditText) v.findViewById(R.id.command_text)).getText().toString();


                                                String peticion = new StringHelper().clearString(((serviceAdapter.sevList.get(position).getName() + " " + extra)));

                                                if (!dbh.getAllCache(peticion, "peticion").equals("")) {


                                                    try {
                                                        if (dataHelper.compareTwoDates(dataHelper.getNowDateTime(), dbh.getAllCache(peticion, "cache"))) {
                                                            Log.i("llamar", "abrir el cacheado");//Si la fecha de la db es superior a la actual
                                                            final HistoryEntry entry = new HistoryEntry(peticion, null, dbh.getAllCache(peticion, "path"), null);
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {

                                                                    HistoryManager.getSingleton().setCurrentPage(entry);
                                                                    open = true;

                                                                }
                                                            });

                                                        } else {
                                                            Log.i("llamar", "llamar servicios y borra el cache");
                                                            dbh.delBy("cache", "_id", dbh.getAllCache(peticion, "id"));


                                                            comunication.execute(DrawerActivity.this, serviceAdapter.sevList.get(position).getName(), serviceAdapter.sevList.get(position).getName() + " " + extra, false, serviceAdapter.sevList.get(position).getDescription(), DrawerActivity.this, DrawerActivity.this);

                                                        }
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    Log.i("llamar", "El servicio no esta en cache");
                                                    if (extra.equals("")) {
                                                        Toast.makeText(DrawerActivity.this, "Rellene el campo antes de enviar", Toast.LENGTH_SHORT).show();
                                                    } else {

                                                        comunication.execute(DrawerActivity.this, serviceAdapter.sevList.get(position).getName(), serviceAdapter.sevList.get(position).getName() + " " + extra, false, serviceAdapter.sevList.get(position).getDescription(), DrawerActivity.this, DrawerActivity.this);
                                                    }
                                                }


                                            }
                                        }).setNegativeButton("Cancelar", null).show();
                                break;
                            }

                            case 2: {
                                idservice = serviceAdapter.sevList.get(position).getId();
                                startActivity(new Intent(DrawerActivity.this, ServiceDetails.class));
                                finish();
                                break;
                            }


                        }
                    }
                }).show();
            }
        });


        wv = findViewById(R.id.mainWebView);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setDisplayZoomControls(true);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    WebView wv = (WebView) v;

                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (wv.canGoBack()) {
                                wv.goBack();
                                return true;
                            }
                            break;
                    }
                }

                return false;
            }
        });


        wv.addJavascriptInterface(new JSI(), "apretaste");
        if (HistoryManager.getSingleton().currentUrl == null) {

            HistoryManager.getSingleton().addListener(this);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (SettingsActivity.terminating)
            return;
        String enties = new Gson().toJson(HistoryManager.getSingleton().entries);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(HISTORY, enties).apply();


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (SettingsActivity.terminating)
            return;
        if (needsReload)
            showProfileInfo(hView, true);
    }

    @SuppressLint("SetTextI18n")
    private void showProfileInfo(View view, boolean reloadServices) {

        ((TextView) view.findViewById(R.id.tvUsername)).setText("@" + pro.username);
        ((TextView) view.findViewById(R.id.tvcredit)).setText("§" + String.format("%.2f", pro.credit));
        profilePict = (ImageView) view.findViewById(R.id.ivProfile);
        if (reloadServices) {
            serviceAdapter.notifyDataSetChanged();
        }
        if (pro.profile.picture != null && !pro.profile.picture.isEmpty()) {
            try {
                File file = new File(getFilesDir(), pro.profile.picture);

                RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeFile(file.getPath()));
                dr.setCircular(true);
                profilePict.setImageDrawable(dr);
            } catch (Exception ignored) {

            }
        } else {
            profilePict.setImageResource(R.drawable.ic_person_black_24dp);
        }


    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            if (open) {
                isShowingHome = true;
            } else {
                if (!pressed) {
                    Toast.makeText(getBaseContext(), PRESIONE_NUEVAMENTE_PARA_SALIR, Toast.LENGTH_SHORT).show();
                    pressed = true;
                } else {
                    super.onBackPressed();
                }
            }
            if (isShowingHome) {
                HistoryManager.getSingleton().setCurrentPage(null);
                open = false;
                searchItem.setVisible(true);
                donwloadItem.setVisible(false);
                updateItem.setVisible(false);
                return;
            }


        }


    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ServicePsiphon.ServicePsiphonBinder binder = (ServicePsiphon.ServicePsiphonBinder) service;
            servicePsiphon = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        pressed = false;
        setCountNoti(count_noti, false);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(DrawerActivity.this, ServicePsiphon.class);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            }
        });
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */


    @Override
    protected void onDestroy() {
        if (SettingsActivity.terminating) {
            super.onDestroy();
            return;
        }
        HistoryManager.getSingleton().removeListener(this);
        String enties = new Gson().toJson(HistoryManager.getSingleton().entries);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(HISTORY, enties).apply();
        new PrefsManager().saveBoolean(this, "connect-psiphon", false);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        searchItem = menu.findItem(R.id.action_search);
        updateItem = menu.findItem(R.id.action_update);
        donwloadItem = menu.findItem(R.id.action_download);


        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


        searchView.setQueryHint("Inserte un servicio a buscar");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                serviceAdapter.getFilter().filter(newText);

                return true;
            }
        });

        etSearchview = (EditText) searchView.findViewById(R.id.search_src_text);

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case android.R.id.home: {

                break;
            }

            case R.id.action_download: {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    PrintManager printManager = (PrintManager) DrawerActivity.this
                            .getSystemService(Context.PRINT_SERVICE);

                    PrintDocumentAdapter printAdapter =
                            null;

                    printAdapter = wv.createPrintDocumentAdapter("MyDocument");


                    String jobName = getString(R.string.app_name) +
                            " Print Test";

                    printManager.print(jobName, printAdapter,
                            new PrintAttributes.Builder().build());
                } else {
                    File Directory = new File("/sdcard/Apretaste/");
                    if (!Directory.exists()) {
                        Directory.mkdirs();
                    }
                    String name = "HTML" + wv.getUrl().split("HTML")[1];
                    copyFile(getExternalFilesDir(null) + "/" + name, "/sdcard/Apretaste/");
                    if (copyFile(getExternalFilesDir(null) + "/" + name, "/sdcard/Apretaste/")) {
                        Toast.makeText(this, "Pagina guardada  en /sdcard/Apretaste/" + "HTML" + wv.getUrl().split("HTML")[1], Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }

            case R.id.action_update: {

                String path_picado = wv.getUrl().substring(8);
                String pathfinal = "file:/" + path_picado;
                comunication.execute(DrawerActivity.this, dbh.getHistoryById(pathfinal, "service"), dbh.getHistoryById(pathfinal, "command"), false, null, DrawerActivity.this, DrawerActivity.this);


            }
        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    public static boolean copyFile(String from, String to) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                int end = from.toString().lastIndexOf("/");
                String str1 = from.toString().substring(0, end);
                String str2 = from.toString().substring(end + 1, from.length());
                File source = new File(str1, str2);
                File destination = new File(to, str2);
                if (source.exists()) {
                    FileChannel src = new FileInputStream(source).getChannel();
                    FileChannel dst = new FileOutputStream(destination).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        switch (id) {

            case R.id.nav_home: {
                HistoryManager.getSingleton().setCurrentPage(null);
                open = false;
                searchItem.setVisible(true);
                donwloadItem.setVisible(false);
                updateItem.setVisible(false);
                break;
            }
            case R.id.nav_noti: {
                comunication.execute(DrawerActivity.this, "Notificaciones", "notificaciones", false, "", DrawerActivity.this, DrawerActivity.this);
                setCountNoti(count_noti, true);
                break;
            }

            case R.id.nav_profile: {
                comunication.execute(DrawerActivity.this, "PERFIL", "PERFIL EDITAR", false, "", DrawerActivity.this, DrawerActivity.this);
                break;
            }

            case R.id.nav_retos: {

                comunication.execute(DrawerActivity.this, "retos", "Retos", false, "des", DrawerActivity.this, DrawerActivity.this);
                break;
            }

            case R.id.nav_cupones: {

                comunication.execute(DrawerActivity.this, "cupones", "Cupones", false, "des", DrawerActivity.this, DrawerActivity.this);
                break;
            }

            case R.id.nav_invitar: {

                comunication.execute(DrawerActivity.this, "referir", "referir", false, "des", DrawerActivity.this, DrawerActivity.this);
                break;
            }

            case R.id.nav_recent: {
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            }

            case R.id.nav_settings: {
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            }

            case R.id.nav_about: {
                startActivity(new Intent(this, Settings_about.class));
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMailSent() {

    }

    @Override
    public void onError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String ERROR = "Error";
                String OK = "ok";
                if (!networkHelper.haveConn(DrawerActivity.this)) {
                    alertHelper.simpleAlert(DrawerActivity.this, "Error", "Usted debe enceder los datos moviles o conectarse a una red wifi para poder usar nuestra app");
                } else if (e.toString().equals("javax.mail.AuthenticationFailedException: [AUTHENTICATIONFAILED] Authentication failed.")) {

                    new AlertDialog.Builder(DrawerActivity.this).setTitle(ERROR).setMessage("Su correo electronico o contraseña es incorrecto , verifiquelo y vuelvelo a intentar").setPositiveButton(OK, null).show();
                } else {
                    new AlertDialog.Builder(DrawerActivity.this).setTitle(ERROR).setMessage("No hemos podido establecer comunicación  , es problema de conexion con los servidores nauta , intentelo más tarde nuevamente").setPositiveButton(OK, null).show();
                }


            }
        });

    }

    @Override
    public void onResponseArrived(String service, String command, String response, Mailer mailer) {

        Log.e("pro", "rarived");
        /*Si se manda a actualizar la app*/
        if (mailer.getReturnContent()) {
            comunication.setReturnContent(false);
            Log.e("update-reply", response);
            ProfileInfo piu = new Gson().fromJson(response, ProfileInfo.class);

            updateService(piu);
            workNotifications(piu);

            new PrefsManager().saveData("mailbox", DrawerActivity.this, piu.mailbox);
            new PrefsManager().saveData("type_img", DrawerActivity.this, piu.img_quality);

            new PrefsManager().saveData("token", DrawerActivity.this, piu.token);

            /*Elimina lo servicios que se quitan del servidor*/
            ServiceNoActive(piu.active);


            update_info(response);

        } else {
            open(service, command, response, mailer, null);

            /*Si vino la cache */
            if (mailer.mincache != null) {
                Log.e("num cache drawer", mailer.mincache);
                dbh.addCache(new StringHelper().clearString(command), dataHelper.addMinutes(mailer.mincache), new File(response).toURI().toString());
            }
            /*Metodo cuando venga .ext (siempre viene)*/
            if (mailer.ext != null) {
                Log.i("ext", mailer.ext);
                ProfileInfo pi = new Gson().fromJson(mailer.ext, ProfileInfo.class);

                updateService(pi);
                /*Accion para anadir notifiaciones */
                workNotifications(pi);

                new PrefsManager().saveData("mailbox", DrawerActivity.this, pi.mailbox);
                new PrefsManager().saveData("type_img", DrawerActivity.this, pi.img_quality);
                new PrefsManager().saveData("token", DrawerActivity.this, pi.token);
                Log.e("token-response-mailer", pi.token);

                /*Elimina lo servicios que se quitan del servidor*/
                ServiceNoActive(pi.active);

                update_info(mailer.ext);

            }
        }
    }

    @Override
    public void onResponseArrivedHttp(String service, String command, String response, MultipartHttp multipartHttp) {
        if (multipartHttp.getReturnContent()) {
            comunication.setReturnContent(false);
            ProfileInfo piu = new Gson().fromJson(response, ProfileInfo.class);
            updateService(piu); /*Actualiza o agrega servicios nuevos*/
            workNotifications(piu);//Muestra las notificaciones en caso de que venga.
            new PrefsManager().saveData("mailbox", DrawerActivity.this, piu.mailbox);
            new PrefsManager().saveData("type_img", DrawerActivity.this, piu.img_quality);
            new PrefsManager().saveData("token", DrawerActivity.this, piu.token);
            ServiceNoActive(piu.active);       /*Elimina lo servicios que se quitan del servidor*/
            update_info(response);


        } else {
            open(service, command, response, null, multipartHttp);

            /*Si vino la cache */
            if (multipartHttp.mincache != null) {
                Log.e("num cache drawer", multipartHttp.mincache);
                dbh.addCache(new StringHelper().clearString(command), dataHelper.addMinutes(multipartHttp.mincache), new File(response).toURI().toString());
            }
            /*Metodo cuando venga .ext (siempre viene)*/
            if (multipartHttp.ext != null) {
                Log.i("ext", multipartHttp.ext);
                ProfileInfo pi = new Gson().fromJson(multipartHttp.ext, ProfileInfo.class);

                updateService(pi);
                /*Accion para anadir notifiaciones */
                workNotifications(pi);

                new PrefsManager().saveData("mailbox", DrawerActivity.this, pi.mailbox);
                new PrefsManager().saveData("type_img", DrawerActivity.this, pi.img_quality);
                new PrefsManager().saveData("token", DrawerActivity.this, pi.token);
                Log.e("token-response-mailer", pi.token);

                /*Elimina lo servicios que se quitan del servidor*/
                ServiceNoActive(pi.active);

                update_info(multipartHttp.ext);

            }
        }

    }

    public void updateService(ProfileInfo pi) {
        if (pi.services.length > 0) {
            dbh.addService(pi.services);
            for (int i = 0; i < pi.services.length; i++) {
                Services service = new Services();
                service.setName(pi.services[i].name);
                service.setCreator(pi.services[i].creator);
                service.setDescription(pi.services[i].description);
                service.setUpdated(pi.services[i].updated);
                service.setIcon(pi.services[i].icon);
                service.setUsed("0");
                service.setId(dbh.getIdByName(pi.services[i].name));
                service.setFav("0");
                //Log.e("found", String.valueOf(getItemExist(service.getName())));
                if (!getItemExist(service.getName())) {
                    serviceAdapter.sevList.add(service);
                    Collections.sort(serviceAdapter.sevList, new Comparator<Services>() {
                        @Override
                        public int compare(Services o1, Services o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                }

            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProfileInfo(hView, true);
                }
            });
        }


    }

    public void ServiceNoActive(String[] active) {
        Services[] sev = serviceAdapter.sevList.toArray(new Services[serviceAdapter.sevList.size()]);
        for (int i = 0; i < sev.length; i++) {
            boolean found = false;
            for (int j = 0; j < active.length; j++) {
                if (sev[i].getName().equals(active[j])) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                dbh.delBy("services", "service", sev[i].getName());
                serviceAdapter.sevList.remove(getIndexByname(sev[i].getName()));
                laststate = 3;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        serviceAdapter.notifyDataSetChanged();
                    }
                });


            }
        }
    }

    public int getIndexByname(String sName) {
        for (Services _item : serviceAdapter.sevList) {
            if (_item.getName().equals(sName))
                return serviceAdapter.sevList.indexOf(_item);
        }
        return -1;
    }

    public boolean getItemExist(String sName) {
        boolean exists = false;
        for (Services _item : serviceAdapter.sevList) {

            if (_item.getName().equals(sName))
                exists = true;
        }
        return exists;
    }

    public void open(final String service, final String command, String response, final Mailer mailer, final MultipartHttp multiparthttp) {
        Date time = null;

        if (mailer != null) {
            time = mailer.getResponseTimestamp();
        } else {
            time = multiparthttp.getResponseTimestamp();
        }

        final File file = new File(response);
        final HistoryEntry entry = new HistoryEntry(service, command, file.toURI().toString(), time);
        final Date finalTime = time;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HistoryManager.getSingleton().addToHistory(entry);
                dbh.addHistory(service, command, file.toURI().toString(), String.valueOf(finalTime));
                HistoryManager.getSingleton().setCurrentPage(entry);
                open = true;

            }
        });
    }

    public void workNotifications(ProfileInfo pi) {
        /*Accion para anadir notifiaciones */
        if (pi.notifications.length > 0) {
            dbh.addNotification(pi.notifications);
            for (int i = 0; i < pi.notifications.length; i++) {
                alertHelper.newNotification(DrawerActivity.this, (int) Calendar.getInstance().getTimeInMillis(), pi.notifications[i].service, pi.notifications[i].text);
                int count = new PrefsManager().readInt(DrawerActivity.this, "count_noti");
                new PrefsManager().saveInt(DrawerActivity.this, "count_noti", count + 1);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setCountNoti(count_noti, false);
                }
            });


        }
    }

    public void update_info(String response) {
        Log.i("response", response);

        Log.e("pro", "retcontent");
        ProfileInfo pinfo;
        try {
            pinfo = new Gson().fromJson(response, ProfileInfo.class);

            Log.e("pro", "pinfo jsoned");
        } catch (Exception e) {
            Toast.makeText(this, "Ha ocurrido un error en el servidor.", Toast.LENGTH_SHORT).show();
            Log.e("pro", "error jsoning pinfo");
            return;
        }
        Log.e("pro", "updating");

        pro.update(pinfo);

        Log.e("pro", "jsoning");
        String prof = new Gson().toJson(DrawerActivity.pro);

        Log.e("pro", "saving");

        PreferenceManager.getDefaultSharedPreferences(DrawerActivity.this).edit().putString("resp", prof).apply();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("pro", "refreshing");
                showProfileInfo(hView, true);
            }
        });
        return;
    }
    /*Metodo que abre las web*/

    public void onHistoryChange(HistoryEntry newUrl) {
        if (newUrl != null) {
            toolbar.setTitle(newUrl.service.split(" ")[0].toLowerCase());
            wv.loadUrl(newUrl.path);
            fabSync.setVisibility(View.GONE);

            searchItem.setVisible(false);
            donwloadItem.setVisible(true);
            updateItem.setVisible(true);
            vsw.setDisplayedChild(1);

            return;
        } else {
            vsw.setDisplayedChild(0);
            fabSync.setVisibility(View.VISIBLE);
            toolbar.setTitle("Apretaste");

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SettingsActivity.terminating) {
            SettingsActivity.terminating = false;
            Toast.makeText(this, "Su sesión ha sido cerrada.", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    @Override
    public void onErrorHttp(String error) {

    }

    /*Metodo que gestiona la respuesta de peticiones simple*/
    @Override
    public void onResponseSimpleHttp(String response) {
        Log.e("response drawe", response);
    }


    /*Adaptador de los servicios */
    public class ServiceAdapter extends BaseAdapter implements Filterable {
        Context context;
        ArrayList<Services> sevList;
        ArrayList<Services> orig;

        private LayoutInflater inflater = null;

        public ServiceAdapter(Context context, ArrayList<Services> sevList) {
            this.context = context;
            this.sevList = sevList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {

            return sevList.size();
        }

        @Override
        public Object getItem(int position) {

            return position;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.material_service_item, null);
            if (convertView == null)


                ((ImageView) convertView.findViewById(R.id.service_image)).setImageResource(R.drawable.noicon);
            ImageView im = (ImageView) convertView.findViewById(R.id.iVstart);
            ImageView srvice_new = (ImageView) convertView.findViewById(R.id.srvice_new);

            if (sevList.get(position).getFav().equals("0")) {
                im.setVisibility(View.INVISIBLE);
            }

            if (sevList.get(position).getUsed().equals("1")) {
                srvice_new.setVisibility(View.INVISIBLE);
            }


            ((TextView) convertView.findViewById(R.id.service_name)).setText((sevList.get(position).getName()));
            if (sevList.get(position).getIcon() != null && !sevList.get(position).getIcon().equals("")) {
                try {
                    File file = new File(getFilesDir(), sevList.get(position).getIcon());
                    Drawable dr = new BitmapDrawable(getResources(), BitmapFactory.decodeFile(file.getPath()));
                    ((ImageView) convertView.findViewById(R.id.service_image)).setImageDrawable(dr);
                } catch (Exception ignored) {

                }
            } else
                ((ImageView) convertView.findViewById(R.id.service_image)).setImageResource(R.drawable.noicon);


            return convertView;

        }


        @Override
        public Filter getFilter() {
            return new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<Services> results = new ArrayList<Services>();
                    if (orig == null)
                        orig = sevList;
                    if (constraint != null) {
                        if (orig != null && orig.size() > 0) {
                            for (final Services g : orig) {
                                if (g.getName().toLowerCase()
                                        .contains(constraint.toString()))
                                    results.add(g);
                            }
                        }
                        oReturn.values = results;
                    }
                    return oReturn;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    sevList = (ArrayList<Services>) results.values;
                    notifyDataSetChanged();
                }
            };
        }


    }

    /*Clase que maneja el webview*/
    private class JSI {
        public static final String TRUE = "true";
        public static final String DOACTION = "doaction";

        @JavascriptInterface
        public void doaction(final String command, final String type, final String help, final boolean waiting, final String callback) {
            Log.e(DOACTION, type + " " + String.valueOf(waiting) + " " + command + " " + help + " " + callback.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (type.equalsIgnoreCase(TRUE)) {
                        final View v = getLayoutInflater().inflate(R.layout.various, null);
                        final LinearLayout linearLayout = v.findViewById(R.id.ll_demo);

                        final InputTypeHelper inputTypeHelper = new InputTypeHelper(DrawerActivity.this, linearLayout, help, DrawerActivity.this);
                        inputTypeHelper.showInputs();

                        final AlertDialog alertDialog = alertHelper.AlertView(DrawerActivity.this, v);
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (inputTypeHelper.checkInputRequire(newBitmap)) {
                                            comunication.setAttachedbitmap(newBitmap);
                                            comunication.execute(DrawerActivity.this, command.split(" ")[0], command + " " + inputTypeHelper.getCommand().substring(1), !waiting, help, DrawerActivity.this, DrawerActivity.this);
                                            alertDialog.dismiss();
                                            if (!callback.equals("") || !callback.isEmpty()) {
                                                String js = "javascript:" + callback + "(" + inputTypeHelper.getParamsCallBack() + ")";
                                                wv.loadUrl(js);

                                                Log.i("params callback", inputTypeHelper.getParamsCallBack());
                                            }


                                        } else {
                                            Toast.makeText(DrawerActivity.this, "Por favor rellene todos los campos mandatarios", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                        alertDialog.show();
                    } else {
                        comunication.execute(DrawerActivity.this, command.split(" ")[0], command, !waiting, help, DrawerActivity.this, DrawerActivity.this);

                    }
                }
            });

        }

    }


    private void setCountNoti(TextView notifCount, boolean readed) {
        notifCount.setGravity(Gravity.CENTER_VERTICAL);
        notifCount.setTypeface(null, Typeface.BOLD);
        notifCount.setTextColor(getResources().getColor(R.color.negro));
        int count = new PrefsManager().readInt(DrawerActivity.this, "count_noti");
        if (count > 0 && !readed) {
            notifCount.setVisibility(View.VISIBLE);
            notifCount.setText(String.valueOf(count));
        } else {
            if (readed) {
                new PrefsManager().saveInt(DrawerActivity.this, "count_noti", 0);
            }
            notifCount.setVisibility(View.GONE);
        }
    }


    public void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }


    @Override
    public void onClickInputUpload() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int checkpermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (checkpermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                }, REQUEST_STORAGE_PERMISSION);

            } else
                picPict();
        } else
            picPict();
    }

    @Override
    public void updateLabel(EditText editText, Calendar myCalendar) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    private void picPict() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(IMAGE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                picPict();
            } else {
                Toast.makeText(this, "Permiso no autorizado", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            fullPhotoUri = data.getData();
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.outHeight = 256;
            opts.outWidth = 256;
            try {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(fullPhotoUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String path = cursor.getString(columnIndex);
                cursor.close();


                newBitmap = decodeSampledBitmapFromFile(path, 250, 250);
                RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), newBitmap);
                roundedBitmapDrawable.setCircular(true);
                // profilePict.setImageDrawable(roundedBitmapDrawable);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);


        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);

        int w = bmp.getWidth();
        int h = bmp.getHeight();
        if (w > reqHeight) {
            float ratio = (float) w / reqHeight;
            w = reqWidth;
            h = (int) ((float) h / ratio);
        }
        if (h > reqHeight) {
            float ratio = (float) h / reqWidth;
            w = (int) ((float) w / ratio);
            h = reqHeight;
        }

        return Bitmap.createScaledBitmap(bmp, w, h, true);
    }

}




