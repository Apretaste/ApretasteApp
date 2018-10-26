package apretaste.ui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import apretaste.Comunication.email.Mailer;
import apretaste.Comunication.email.Mailerlistener;

import com.example.apretaste.R;

import apretaste.Helper.PrefsManager;

public class Settings_nauta extends AppCompatActivity implements Mailerlistener {
    public static final String SIN_SEGURIDAD = "Sin seguridad";
    public static final String SMTP_SSL = "smtp_ssl";
    public static final String SMTP_PORT = "smtp_port";
    public static final String SMTP_NAUTA_CU = "smtp.nauta.cu";
    public static final String SMTP_SERVER = "smtp_server";
    public static final String IMAP_SSL = "imap_ssl";
    public static final String IMAP_PORT = "imap_port";
    public static final String IMAP_NAUTA_CU = "imap.nauta.cu";
    public static final String IMAP_SERVER = "imap_server";
    public static final String PASS = "pass";
    public static final String USER = "user";
    public static final String OPCIONES_DE_SEGURIDAD = "Opciones de seguridad";
    public static final String SSL = "SSL";
    PrefsManager prefsManager = new PrefsManager();
    EditText et_email, et_password, et_server_smtp, et_security_smtp, et_server_imap, et_security_imap, et_port_smtp, et_port_imap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_nauta);


        et_email = (EditText) findViewById(R.id.et_settings_email);
        if (new PrefsManager().getData("user", Settings_nauta.this).equals("")) {
            prefsManager.change_value(Settings_nauta.this, "user", et_email);
        }
        et_password = (EditText) findViewById(R.id.et_settings_password);
        et_server_imap = (EditText) findViewById(R.id.et_settings_server_imap);
        et_port_imap = (EditText) findViewById(R.id.et_settings_port_imap);
        et_server_smtp = (EditText) findViewById(R.id.et_settings_server_smtp);
        et_port_smtp = (EditText) findViewById(R.id.et_settings_port_smtp);
        et_security_smtp = (EditText) findViewById(R.id.et_settings_security_smtp);
        et_security_imap = (EditText) findViewById(R.id.et_settings_security_imap);

        final CharSequence[] sslOptions = new CharSequence[]{SSL, SIN_SEGURIDAD};
        View.OnClickListener sslListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(Settings_nauta.this).setItems(sslOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((EditText) v).setText(sslOptions[which]);
                    }
                }).setTitle(OPCIONES_DE_SEGURIDAD).show();
            }
        };
        et_security_smtp.setOnClickListener(sslListener);
        et_security_imap.setOnClickListener(sslListener);

        prefsManager.show_value(this, et_email, "user", "");
        prefsManager.show_value(this, et_password, PASS, "");

        prefsManager.show_value(this, et_server_imap, IMAP_SERVER, IMAP_NAUTA_CU);
        prefsManager.show_value(this, et_port_imap, IMAP_PORT, "143");
        prefsManager.show_value(this, et_security_imap, IMAP_SSL, SIN_SEGURIDAD);

        prefsManager.show_value(this, et_server_smtp, SMTP_SERVER, SMTP_NAUTA_CU);
        prefsManager.show_value(this, et_port_smtp, SMTP_PORT, "25");
        prefsManager.show_value(this, et_security_smtp, SMTP_SSL, SIN_SEGURIDAD);

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_email.getText().toString().isEmpty() ||
                        et_password.getText().toString().isEmpty() ||

                        et_server_imap.getText().toString().isEmpty() ||
                        et_port_imap.getText().toString().isEmpty() ||
                        et_security_imap.getText().toString().isEmpty() ||

                        et_server_smtp.getText().toString().isEmpty() ||
                        et_port_smtp.getText().toString().isEmpty() ||
                        et_security_smtp.getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    prefsManager.change_value(Settings_nauta.this, "pass", et_password);
                    prefsManager.change_value(Settings_nauta.this, "user", et_email);
                    //Imap
                    prefsManager.change_value(Settings_nauta.this, "imap_server", et_server_imap);
                    prefsManager.change_value(Settings_nauta.this, "imap_port", et_port_imap);
                    prefsManager.change_value(Settings_nauta.this, "imap_ssl", et_security_imap);

                    //Smtp
                    prefsManager.change_value(Settings_nauta.this, "smtp_server", et_server_smtp);
                    prefsManager.change_value(Settings_nauta.this, "smtp_port", et_port_smtp);
                    prefsManager.change_value(Settings_nauta.this, "smtp_ssl", et_security_smtp);

                    Log.e("user", new PrefsManager().getData("user", Settings_nauta.this));
                    Log.e("pass", new PrefsManager().getData("pass", Settings_nauta.this));
                    Mailer mailer = new Mailer(Settings_nauta.this, null, "PERFIL IMAGEN " + new PrefsManager().getData("type_img", Settings_nauta.this), true, null, Settings_nauta.this, true);
                    mailer.execute();
                }
            }
        });


    }

    @Override
    public void onMailSent() {


        prefsManager.change_value(Settings_nauta.this, "pass", et_password);
        prefsManager.change_value(Settings_nauta.this, "user", et_email);
        //Imap
        prefsManager.change_value(Settings_nauta.this, "imap_server", et_server_imap);
        prefsManager.change_value(Settings_nauta.this, "imap_port", et_port_imap);
        prefsManager.change_value(Settings_nauta.this, "imap_ssl", et_security_imap);

        //Smtp
        prefsManager.change_value(Settings_nauta.this, "smtp_server", et_server_smtp);
        prefsManager.change_value(Settings_nauta.this, "smtp_port", et_port_smtp);
        prefsManager.change_value(Settings_nauta.this, "smtp_ssl", et_security_smtp);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Settings_nauta.this, "Se han guardado los cambios", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onError(Exception e) {

        Log.e("error", e.toString());

        prefsManager.change_value2(Settings_nauta.this, "user", "");
        prefsManager.change_value2(Settings_nauta.this, "pass", "");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Settings_nauta.this, "Hubo un error, compruebe sus datos", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onResponseArrived(String service, String command, String response, Mailer mailer) {

    }
}