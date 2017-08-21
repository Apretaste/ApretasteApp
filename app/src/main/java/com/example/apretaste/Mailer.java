package com.example.apretaste;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.smtp.SMTPMessage;
import com.sun.mail.smtp.SMTPTransport;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 * Created  by Raymond Arteaga on 13/07/2017.
 */ //clase que gestiona el envio de la peticion y la espera de la respuesta en segundo plano
public class Mailer extends AsyncTask<Void, String, Void> implements MessageCountListener {

    public static final String TXT = "txt";
    public static final String HTML = "html";
    public static final String HTML1 = ".html";
    public static final String HTML2 = "HTML_";
    public static final String YYYY_M_MDD_H_HMMSS = "yyyyMMdd_HHmmss";
    public static final String PROFILE_PNG = "profile.png";
    public static final String UTF_8 = "UTF-8";
    public static final String TXT1 = ".txt";
    public static final String ZIP = ".zip";
    public static final String APR = "apr";
    public static final String MESSAGES_ADDED_END = "messages added end";
    public static final String IDLE = "idle";
    public static final String ERROR_AL_RECIBIR_RESPUESTA_DEL_MENSAJE = "Error al recibir respuesta del mensaje ";
    public static final String ESPERANDO_RESPUESTA = "Esperando respuesta";
    public static final String ERROR_EN_IDLE = "Error en IDLE ";
    public static final String IGNORANDO = "Ignorando ";
    public static final String MESSAGES_GET_SUBJECT_RETDIF = "messages getSubject retdif";
    public static final String CARGANDO = "Cargando...";
    public static final String PROCESANDO_RESPUESTA = "Procesando respuesta ";
    public static final String MESSAGES_GET_SUBJECT_RETEQUAL = "messages getSubject retequal";
    public static final String MESSAGES_GET_SUBJECT = "messages getSubject";
    public static final String MESSAGES_ADDED_SYNC = "messages added sync";
    public static final String SEND_IDLE_COMMAND_IDLE_RETURNED = "sendIdleCommand idle returned";
    public static final String ESPERANDO_RESPUESTA_IDLE = "Esperando respuesta IDLE";
    public static final String SEND_IDLE_COMMAND = "sendIdleCommand";
    public static final String ENVIADO = "Enviado";
    public static final String APP_MAILGUN_APRETASTE_COM = "app@mailgun.apretaste.com";
    public static final String MAILBOX = "mailbox";
    public static final String CONECTANDO = "Conectando";
    public static final String ENVIANDO_MENSAJE = "Enviando mensaje";
    public static final String APPLICAION_ZIP = "applicaion/zip";
    public static final String COMPRIMIENDO_DATOS = "Comprimiendo datos";
    public static final String MIXED = "mixed";
    public static final String BIT = "7bit";
    public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
    public static final String PLAIN = "plain";
    public static final String MENSAJE_DE_PRUEBA = "mensaje de prueba";
    public static final String CONECTANDO_SMTP = "Conectando SMTP";
    public static final String SMTP_PORT = "smtp.port";
    public static final String SMTP_NAUTA_CU = "smtp.nauta.cu";
    public static final String SMTP_SERVER = "smtp_server";
    public static final String SMTP = "smtp";
    public static final String SMTPS = "smtps";
    public static final String CREANDO_SESION_SMTP = "Creando sesion SMTP";
    public static final String SIN_SEGURIDAD = "Sin seguridad";
    public static final String SMTP_SSL = "smtp_ssl";
    public static final String INBOX = "INBOX";
    public static final String ABRIENDO_INBOX_IMAP = "Abriendo INBOX IMAP";
    public static final String CONECTANDO_IMAP = "Conectando IMAP";
    public static final String IMAP_PORT = "imap_port";
    public static final String IMAP_NAUTA_CU = "imap.nauta.cu";
    public static final String IMAP_SERVER = "imap_server";
    public static final String IMAP = "imap";
    public static final String IMAPS = "imaps";
    public static final String CREANDO_SESION_IMAP = "Creando sesión IMAP";
    public static final String IMAP_SSL = "imap_ssl";
    public static final String PASS = "pass";
    public static final String USER = "user";
    public static final String ACEPTAR = "Aceptar";
    public static final String SE_HA_CANCELADO_LA_PETICION = "Se ha cancelado la petición";
    public static final String E_GET_MESSAGE = "e.getMessage()";
    public static final String DIB = "DIB";
    public static final String MAILER = "Mailer";
    public static final String CANCELLED = "cancelled";
    public static final String MAILER1 = "mailer";
    public static final String CANCELLING = "cancelling";
    Context context;
    private IMAPStore store;
    SMTPTransport transport;
    private IMAPFolder inbox;
    private boolean finished=false;
    private boolean finished2=false;
    private boolean arrived=false;

    public void setAppendPassword(boolean appendPassword) {
        this.appendPassword = appendPassword;
    }

    private boolean appendPassword=false;

    public boolean isShowCommand() {
        return showCommand;
    }

    public Mailer setShowCommand(boolean showCommand) {
        this.showCommand = showCommand;
        return this;
    }

    private boolean showCommand=true;

    public boolean isShowCancelButton() {
        return showCancelButton;
    }

    public void setShowCancelButton(boolean showCancelButton) {
        this.showCancelButton = showCancelButton;
    }

    private boolean showCancelButton=true;

    public boolean isShowStatus() {
        return showStatus;
    }

    public void setShowStatus(boolean showStatus) {
        this.showStatus = showStatus;
    }

    private boolean showStatus=true;

    private static final boolean HIDE_STATUS_DETAILS=true;

    private final Activity activity;

    public static AlertDialog dialog;

    private static Random random;
    static
    {

        random=new Random();
    }
    //el texto de estado actual que aparecera en el dialogo de espera


    private boolean noreply = false;
    private String help = "";

    TextView statusView;

    public Date getResponseTimestamp() {
        return timestamp;
    }

    private Date timestamp;
    private String service;
    private String command;
    private String responseText;  //El texto contenido en el mensaje de respuesta
    private String errorMessage; //El texto del error si ocurre alguno o null de lo contrario
    private String ticket;       //El asunto del correo enviado, debe coincidir con el correo de respuesta

    private Mailerlistener mailerlistener;

    private Bitmap profileBitmap=null;

    public boolean getSaveInternal() {
        return saveInternal;
    }

    public boolean getReturnContent() {
        return returnContent;
    }

    private boolean saveInternal=false;

    public boolean isFastAccess() {
        return isFastAccess;
    }

    public void setFastAccess(boolean fastAccess) {
        isFastAccess = fastAccess;
    }

    public String getCustomText() {
        return customText;
    }

    public Mailer setCustomText(String customText) {
        this.customText = customText;
        return this;
    }

    private boolean isFastAccess=false;
    private boolean returnContent=false;
    private String customText=null;

    //constructor que toma como parametros el usuario y la contrasenha

    public Mailer(Activity parent, @Nullable String service, String command, Boolean noreply, String help, @NonNull Mailerlistener listener) {
        this.activity = parent;
        this.command = command;
        this.service = service;
        this.noreply = noreply;
        this.help = help;
        this.mailerlistener=listener;
    }

    public Mailer setSaveInternal(boolean saveInternal)
    {
        this.saveInternal=saveInternal;
        return this;
    }

    public Mailer setReturnContent(boolean returnContent)
    {
        this.returnContent=returnContent;
        return this;
    }


    Handler handler;
    public void setAttachedbitmap(Bitmap bitmap)
    {
        profileBitmap=bitmap;
    }

    @Override
    protected void onPreExecute() {
        final View v=activity.getLayoutInflater().inflate(R.layout.wait_dialog_layout,null);

   //     ProgressBar downloadProgressBar = (ProgressBar)v.findViewById(R.id.progressBar);
     //   downloadProgressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FF5EBB47"),android.graphics.PorterDuff.Mode.MULTIPLY);

        statusView=((TextView)v.findViewById(R.id.status));
        if(!showStatus)
            statusView.setVisibility(View.GONE);
        if(customText!=null && !customText.isEmpty())
        {
            ((TextView)v.findViewById(R.id.service)).setVisibility(View.VISIBLE);
            ((TextView)v.findViewById(R.id.service)).setText(customText);
        }
        else
            ((TextView)v.findViewById(R.id.service)).setText(service);

        if(!showCommand)
            v.findViewById(R.id.command).setVisibility(View.GONE);
        else
            ((TextView)v.findViewById(R.id.command)).setText(command);


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(v);

        builder.setCancelable(false);

        builder.setNegativeButton("Cancelar", null);

        dialog=builder.create();
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setVisibility(View.GONE);
                synchronized (idleLock) {
                    if(arrived)
                    {
                        Log.e("mailer","arrived");
                        return;
                    }
                    Log.e(MAILER1, CANCELLING);
                    cancel(true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (inbox != null)
                                {
                                    Log.e("mailer","isOpen()");
                                    inbox.isOpen();
                                }
                            }catch (Exception ignored){
                                Log.e("mailer","isopen exception");
                            }
                        }
                    }).start();
                    Log.e(MAILER1, CANCELLED);
                }
            }
        },300000);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface d) {

                final Button b = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                       b.setVisibility(View.GONE);
                        synchronized (idleLock) {
                            if(arrived)
                                return;
                            Log.e(MAILER1, CANCELLING);
                            cancel(true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (inbox != null)
                                            inbox.isOpen();
                                    }
                                    catch (Exception ignored){}
                                }
                            }).start();
                            Log.e(MAILER1, CANCELLED);
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    //esto se ejecutara en background en un hilo secundario, es la tarea principal
    // de envio y recepcion de datos. Esta separado en una funcion aparte sendMail
    @Override
    protected Void doInBackground(Void[] params) {
        Log.e(MAILER, DIB);
            try {
           sendMail();

            } catch (Exception e) {
           // setCurrentStatus(E_GET_MESSAGE, CONECTANDO);
            mailerlistener.onError(e);
            finished2=true;
            Log.e("mailer","DIB error:"+e.getMessage());
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            });
        }
        Log.e("mailer","removing callbacks & messages");
        handler.removeCallbacksAndMessages(null);
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if(finished2)
            return;
        dialog.dismiss();
        new AlertDialog.Builder(activity).setMessage(SE_HA_CANCELADO_LA_PETICION).setPositiveButton(ACEPTAR,null).show();
    }

    //Esta funcion envia el correo y luego se queda esperando
    private void sendMail() throws MessagingException, UnsupportedEncodingException {





        try
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
            String user=preferences.getString(USER,"");
            String pass=preferences.getString(PASS,"");
            //se conecta a imap antes de de enviar el correo
            //*************************************************
            if (!noreply) {
                Properties properties = new Properties();
                boolean imapSsl=!preferences.getString(IMAP_SSL, SIN_SEGURIDAD).equalsIgnoreCase(SIN_SEGURIDAD);

                Session imapSession = Session.getInstance(properties);

                setCurrentStatus(CREANDO_SESION_IMAP, CONECTANDO);

                try {
                    store = (IMAPStore) imapSession.getStore(new URLName(imapSsl? IMAPS : IMAP,
                            preferences.getString(IMAP_SERVER, IMAP_NAUTA_CU),
                            Integer.valueOf(preferences.getString(IMAP_PORT,"143")),
                            null,
                            user,
                            pass));
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                }
                if(isCancelled())
                {
                    dialog.dismiss();
                    return;
                }
                setCurrentStatus(CONECTANDO_IMAP, CONECTANDO);

                store.connect( preferences.getString(IMAP_SERVER, IMAP_NAUTA_CU),Integer.valueOf(preferences.getString(IMAP_PORT,"143")),user,pass);//se conecta al server imap
                if(isCancelled())
                {
                    dialog.dismiss();
                    return;
                }
                setCurrentStatus(ABRIENDO_INBOX_IMAP, CONECTANDO);

                inbox = (IMAPFolder) store.getFolder(INBOX);// obtiene la carpeta inbox
                if(isCancelled())
                {
                    dialog.dismiss();
                    return;
                }
                inbox.open(Folder.READ_WRITE);//y la abre con permiss de escritura para eliminar el correo una vez leido


                inbox.getNewMessageCount();//obtiene el contador de mensajes nuevos para que la app los cachee
                inbox.addMessageCountListener(this);//agrega un listener que se ejecutara cuando entre un nuevo mensaje
                //***************************************************
            }

            //Se conecta a smtp y envia el correo
            //****************************************************
            Properties props = new Properties();
            boolean smtpSsl=!preferences.getString(SMTP_SSL, SIN_SEGURIDAD).equalsIgnoreCase(SIN_SEGURIDAD);

            setCurrentStatus(CREANDO_SESION_SMTP, CONECTANDO);
            Session sendSession = Session.getInstance(props);
             transport = (SMTPTransport) sendSession.getTransport(new URLName(smtpSsl? SMTPS : SMTP,
                    preferences.getString(SMTP_SERVER, SMTP_NAUTA_CU),
                    Integer.valueOf(preferences.getString("smtp_port","25")),
                    null,
                    user,
                    pass));
            setCurrentStatus(CONECTANDO_SMTP, CONECTANDO);
            if(isCancelled())
            {
                dialog.dismiss();
                return;
            }
            String smp=preferences.getString("smtp_port","25");
            transport.connect( preferences.getString(SMTP_SERVER, SMTP_NAUTA_CU),
                    Integer.valueOf(smp),user,pass);
            if(isCancelled())
            {
                dialog.dismiss();
                return;
            }
            SMTPMessage message = new SMTPMessage(sendSession);

            //le pone el from al mensje saliente, es el mismo q la direccion del correo nauta del usuario
            message.setFrom(new InternetAddress(user, user));

            //se establece el ticket que se va a enviar, luego este se compara con los asuntos de los correos
            //entrantes a ver si coinciden. Por el momento esta estatico pero debe generarse con una lista de palabras
            ticket = genString();

            //le ponemos como asunto el contenido del ticket
            message.setSubject(ticket);

            //Creamos el mensaje que vamos a enviar
            //====================================================

            //parte del cuerpo del mensaje
            MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setText(MENSAJE_DE_PRUEBA, UTF_8, PLAIN);
            messagePart.setDisposition(Part.INLINE);
            messagePart.setHeader(CONTENT_TRANSFER_ENCODING, BIT);
            Multipart multipart = new MimeMultipart(MIXED);
            multipart.addBodyPart(messagePart);//agregamos la parte del texto del mensaje
            setCurrentStatus(COMPRIMIENDO_DATOS, CONECTANDO);
            if(isCancelled())
            {
                dialog.dismiss();
                return;
            }
            //parte del adjunto
            try {
                File file = Compress(command,profileBitmap,appendPassword?pass:null);//obtenemos el archivo adjunto que vamos a enviar(este metodo le pone el texto y todo dentro)
                if(isCancelled())
                {
                    dialog.dismiss();
                    return;
                }
                FileInputStream str = new FileInputStream(file);//abrimos un strea para leer el archivo
                byte[] buffer = new byte[(int) file.length()];//creamos un buffer para almacenar el contenido del archivo
                if (str.read(buffer, 0, (int) file.length()) == -1)
                    throw new Exception("Archivo vacío");//si el archivo esta vacio paso algo y lanzamos un error
                if(isCancelled())
                {
                    dialog.dismiss();
                    return;
                }
                //creamos la parte que contendra el adjunto
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new ByteArrayDataSource(buffer, APPLICAION_ZIP);//creamos la fuente de donde se va a obtener los datos;
                attachmentPart.setDataHandler(new DataHandler(source));//creamos un manejador para la fuente de datos
                attachmentPart.setFileName(genString()+ ZIP);//le ponemos un nombre random al archivo q vamos a enviar
                multipart.addBodyPart(attachmentPart);//agregamos la parte del adjunto al mensaje que vamos a enviar
                attachmentPart.setDisposition(Part.ATTACHMENT);//y decimos q esta parte es un adunto
            } catch (Exception e) {
                e.printStackTrace();//si ocurrio algun error lo lanzamos y cancelamos la tarea
                errorMessage = e.getMessage();
                return;
            }
            if(isCancelled())
            {
                dialog.dismiss();
                return;
            }
            message.setContent(multipart);
            //====================================================================
            if(isCancelled())
            {
                dialog.dismiss();
                return;
            }

            setCurrentStatus(ENVIANDO_MENSAJE, CONECTANDO);//enviamos el mensaje
            String mailbox = preferences.getString(MAILBOX, APP_MAILGUN_APRETASTE_COM);//"rarteaga@nauta.cu";
            if(isCancelled())
            {
                dialog.dismiss();
                return;
            }
            transport.sendMessage(message, new Address[]{new InternetAddress(mailbox)});
            if(isCancelled())
            {
                dialog.dismiss();
                return;
            }
            setCurrentStatus(ENVIADO, ESPERANDO_RESPUESTA);
            mailerlistener.onMailSent();
            if(isCancelled())
            {
                dialog.dismiss();
                return;
            }
            //****************************************************

            //ahor enviamos el comand idle para esperar una respuesta. esto se hace en un ciclo para
            //que siga esperando en caso de que el correo q llega no es el que se espera.
            // Una vez q haya respuesta se rompe el ciclo y se cierra

            /*Verifica si la peticion llevaba respuesta en caso de que no da un mensaje */
            if (noreply) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        View v=activity.getLayoutInflater().inflate(R.layout.done_dialog_layout,null);
                        new AlertDialog.Builder(activity).setView(v).setPositiveButton("Aceptar",null).show();
                    }
                });
                return;
            }
            while (responseText == null && !finished)
            {
                if(isCancelled())
                {
                    dialog.dismiss();
                    return;
                }
                sendIdleCommand();
                if(isCancelled())
                {
                    dialog.dismiss();
                    return;
                }
            }
        }
        finally {
            try {
                transport.close();
            }
            catch (Exception ignore) {}

            try {
                store.close();
            }catch (Exception ignored) {}
        }


    }


    private Object idleLock=new Object();
    private void sendIdleCommand() throws MessagingException {
        Log.e(IDLE, SEND_IDLE_COMMAND);
        setCurrentStatus(ESPERANDO_RESPUESTA_IDLE, ESPERANDO_RESPUESTA);
        if(isCancelled())
        {
            dialog.dismiss();
            return;
        }
        synchronized (idleLock)
        {
            Log.e("mailer","sync1");
        }
        if(!finished)
            inbox.idle();
        if(isCancelled())
        {
            dialog.dismiss();
            return;
        }
        synchronized (idleLock)
        {
//envia el comando idle para usar imap push
            Log.e(IDLE, SEND_IDLE_COMMAND_IDLE_RETURNED);
        }


    }

    /**
     * Se ejecutara cuando llegue la respuesta al correo que se esta esperando
     *
     * @param text  el texto contenido en el archivo random.txt dentro del archivo rantom.zip o hull si hubo algun error
     * @param error el texto del error si hubo alguno o null
     */
    private void onResponseArrived(final String text, final String error) {

        //asigna las variables que luego seran leidas al final de la ejecucion de la tarea
        responseText = text;
        errorMessage = error;

        if (errorMessage == null)//si no ocurrio error
        {
            mailerlistener.onResponseArrived(service,command,text, this);

        } else//si hubo error muestra un mensaje de error en un dialogo nuevo y cierra el dialogo de espera
        {
            mailerlistener.onError(new Exception(error));
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        dialog.dismiss();
    }

    //se utiliza para guardar el estado actual y luego actualizar la ui del dialogo de progreso
    private void setCurrentStatus(final String status, final String simpleStatus) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(HIDE_STATUS_DETAILS)
                    statusView.setText(simpleStatus);
                else
                    statusView.setText(status);
            }
        });
    }

    //esto se va a ejecutar cada vez q llegue un mensaje
    @Override
    public void messagesAdded(MessageCountEvent e) {
        Log.e(IDLE, MESSAGES_ADDED_SYNC);
        synchronized (idleLock)
        {try {
            for (final Message mes : e.getMessages()) {

                    Log.e(IDLE, MESSAGES_GET_SUBJECT);
                    final String su = mes.getSubject();//obtiene el asunto del mensaje entrante

                    if (su.equals(ticket)) {//si el asuto es igual al ticket enviado lo procesa
                        arrived=true;
                        Log.e(IDLE, MESSAGES_GET_SUBJECT_RETEQUAL);
                        setCurrentStatus(PROCESANDO_RESPUESTA + su, CARGANDO);

                        Object content = mes.getContent();//obtiene el contenido del mensaje
                        if (content instanceof Multipart)//si el mensaje tiene arias aprtes
                        {
                            Multipart multipart = (Multipart) content;
                            for (int i = 0; i < multipart.getCount(); i++) {//va parte por parte del multiparte buscando un adjunto
                                BodyPart bodyPart = multipart.getBodyPart(i);
                                if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) ||
                                        bodyPart.getFileName() == null) {//si es un adjunto obtiene el nombre del archivo, si es nulo se ignora
                                    continue;
                                }
                                InputStream is = bodyPart.getInputStream();//obtiene el stream para leer los datos del adjunt desde el server imap
                                String dec = Decompress(is);//y los descomprime
                                mes.setFlag(Flags.Flag.DELETED,true);
                                inbox.close(true);
                                onResponseArrived(dec, null);//llego la respuesta que necesitabamos y terminamos
                                break;
                            }
                        }
                    } else {
                        Log.e(IDLE, MESSAGES_GET_SUBJECT_RETDIF);
                        setCurrentStatus(IGNORANDO + su, ESPERANDO_RESPUESTA);//el mensaje q llego no es el que esperamos
                    }
            }
        } catch (MessagingException | IOException e1) {
            setCurrentStatus(ERROR_EN_IDLE + e1.toString(), ESPERANDO_RESPUESTA);//hubo un error
            onResponseArrived(null, ERROR_AL_RECIBIR_RESPUESTA_DEL_MENSAJE + ticket + ".\r\n" + e1.toString());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
            finished=true;
        }

        Log.e(IDLE, MESSAGES_ADDED_END);
    }

    @Override
    public void messagesRemoved(MessageCountEvent e) {

    }

    File Compress(String command,Bitmap image, String appendedPassword) throws Exception {
        File f= File.createTempFile(APR, ZIP);
        FileOutputStream fos=new FileOutputStream(f);
        OutputStream os = fos;
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(os));
        try {
            String filename = genString()+ TXT1;
            String text=command.concat("\n".concat(activity.getPackageManager().getPackageInfo(activity.getPackageName(),0).versionName));
            if(appendedPassword!=null)
            {
                byte[] bytes = appendedPassword.getBytes("UTF-8");
                String base64=Base64.encodeToString(bytes,Base64.DEFAULT);
                text=text.concat("\n").concat(base64);
            }
            byte[] bytes = text.getBytes(UTF_8);
            ZipEntry entry = new ZipEntry(filename);
            zos.putNextEntry(entry);
            zos.write(bytes);
            zos.closeEntry();
            if(profileBitmap!=null)
            {
                entry = new ZipEntry(PROFILE_PNG);
                zos.putNextEntry(entry);
                image.compress(Bitmap.CompressFormat.PNG,100,zos);
                zos.closeEntry();
            }

        } finally {
            zos.close();
        }
        return f;
    }

    //descomprime un zip y obtiene el comando de dentro del txt que contiene
    String Decompress(InputStream is) throws Exception {
        byte[] bytes=null;
        String returnValue="";
        ZipInputStream zis = new ZipInputStream(is);
        try {
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                String filename = ze.getName();

                if(returnContent)
                {
                    if(filename.endsWith(HTML) || filename.endsWith(TXT))
                    {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int count;
                        while ((count = zis.read(buffer)) != -1) {
                            baos.write(buffer, 0, count);
                        }
                        bytes = baos.toByteArray();
                        returnValue=new String(bytes);
                        continue;
                    }
                }

                    if(filename.endsWith(HTML) || filename.endsWith(TXT))
                    {
                        timestamp = new Date();
                        String timeStamp = new SimpleDateFormat(YYYY_M_MDD_H_HMMSS).format(timestamp);
                        filename = HTML2 + timeStamp + HTML1;
                    }

                    File f=new File(saveInternal?activity.getFilesDir():activity.getExternalFilesDir(null),filename);
                    FileOutputStream fos=new FileOutputStream(f);
                    byte[] buffer = new byte[1024];
                    int count;
                    while ((count = zis.read(buffer)) != -1) {
                        fos.write(buffer, 0, count);
                    }
                    if(!returnContent && (filename.endsWith(HTML) || filename.endsWith(TXT)))
                        returnValue=f.getPath();
                }
        } finally {
            zis.close();
        }
        return returnValue;
    }

    private String genString()
    {
        String[] words = activity.getResources().getStringArray(R.array.words);
        String w1=words[random.nextInt(words.length)];
        String w2=words[random.nextInt(words.length)];
        String w3=words[random.nextInt(words.length)];
        return w1+" "+w2+" "+w3;
    }


}
