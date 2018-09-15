package apretaste.Comunication.http;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.apretaste.R;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import apretaste.Comunication.ComunicationJson;
import apretaste.Helper.AlertHelper;
import apretaste.Helper.DialogHelper;
import apretaste.Helper.PrefsManager;
import apretaste.Helper.UtilHelper;
import apretaste.Comunication.email.Mailer;
import apretaste.Comunication.email.Mailerlistener;

/**
 * Created by cjam on 8/2/2018.
 */

public class MultipartHttp extends AsyncTask<Void, String, Void> {


    private String CONECTANDO = "Conectando";
    private String CARGANDO = "Cargando";

    public Date getResponseTimestamp() {
        return timestamp;
    }
    public KProgressHUD dialog;
    private Boolean noreply = false;
    private Activity activity;
    private String help;
    private Date timestamp;
    private Bitmap profileBitmap=null;
    private String service;
    private String command;

    public boolean getReturnContent() {
        return returnContent;
    }
    private int serverResponseCode;
    private boolean returnContent = false;
    Httplistener httplistener;
    private boolean saveInternal = false;
    public String ext;
    public String mincache;
    private Gson gson = new Gson();


    public MultipartHttp setSaveInternal(boolean saveInternal)
    {
        this.saveInternal=saveInternal;
        return this;
    }

    public void setAttachedbitmap(Bitmap bitmap)
    {
        this.profileBitmap=bitmap;
    }

    public MultipartHttp setReturnContent(boolean returnContent)
    {
        this.returnContent=returnContent;
        return this;
    }
    public MultipartHttp(Activity parent, String service, String command, Boolean noreply, String help , Httplistener httplistener) {
        this.activity = parent;
        this.command = command;
        this.service = service;
        this.noreply = noreply;
        this.help = help;
        this.httplistener=httplistener;

    }
    @Override
    protected void onPostExecute(Void aVoid) {
     dialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        dialog = new DialogHelper().DialogRequest(activity);
       activity.runOnUiThread(new Runnable() {
           @Override
           public void run() {
               dialog.show();
           }
       });
    }

    private void setCurrentStatus(final String status, final String simpleStatus) {
       Log.d("Http:Request",status);
    }

    @Override
    protected void onCancelled() {
        new AlertDialog.Builder(activity).setMessage("Se ha cancelado con exito la peticion").setPositiveButton("Aceptar",                 null).show();
    }
    @Override
    protected Void doInBackground(Void... params) {
        try {
            setCurrentStatus("Abriendo conexion http", CONECTANDO);
            SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(activity);
            String urlsaved = pre.getString("domain", "cubaworld.info");
            MultipartUtility multipartUtility = new MultipartUtility("http://"+urlsaved+"/run/app","UTF-8");

            setCurrentStatus("Comprimiendo", CONECTANDO);
            multipartUtility.addFilePart("attachments",Compress(activity,command,profileBitmap,""), new UtilHelper().genString(activity)+".zip");
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);

            multipartUtility.addFormField("token", preferences.getString("token",null));
            setCurrentStatus("Enviado peticion", CONECTANDO);
            String response = multipartUtility.finish();
            if(isCancelled())
            {

                dialog.dismiss();

            }


            HttpInfo httpInfo;
            Log.e("resp-http",response);
            httpInfo = gson.fromJson(response.toString(),HttpInfo.class);



            setCurrentStatus("Descargando", CARGANDO);

           Log.e("link descarga",httpInfo.file);
          if (noreply){
              activity.runOnUiThread(new Runnable() {
                  @Override
                  public void run() {

                      View v=activity.getLayoutInflater().inflate(R.layout.done_dialog_layout,null);
                      new AlertDialog.Builder(activity).setView(v).setPositiveButton("Aceptar",null).show();
                  }
              });
          }
            if (!noreply) {
                downloadFile(httpInfo.file);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;

    }


    public void downloadFile(String fileURL)
                throws IOException {
            URL url = new URL(fileURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = "";
                String disposition = httpConn.getHeaderField("Content-Disposition");
                String contentType = httpConn.getContentType();
                int contentLength = httpConn.getContentLength();

                if (disposition != null) {
                    // extracts file name from header field
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 10,
                                disposition.length() - 1);
                    }
                } else {
                    // extracts file name from URL
                    fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                            fileURL.length());
                }



                // opens input stream from the HTTP connection
                InputStream inputStream = httpConn.getInputStream();

                    try {
                      String resp=  Decompress(inputStream);

                        httplistener.onResponseArrivedHttp(service,command,resp,MultipartHttp
                        .this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                Log.e("file-donwload","Llego hata aki en talla");
            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            }
            httpConn.disconnect();
        }


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
                    if(filename.endsWith("ext"))
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

                if(filename.endsWith(Mailer.HTML) || filename.endsWith(Mailer.TXT))
                {
                    timestamp = new Date();
                    String timeStamp = new SimpleDateFormat(Mailer.YYYY_M_MDD_H_HMMSS).format(timestamp);
                    filename = Mailer.HTML2 + timeStamp + Mailer.HTML1;
                }

                if(filename.endsWith("ext") )
                {
                    Log.e("ext", "llego el extra");

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int count;
                    while ((count = zis.read(buffer)) != -1) {
                        baos.write(buffer, 0, count);
                    }
                    bytes = baos.toByteArray();
                    this.ext =new String(bytes);
                    Log.e("ext", ext);
                    continue;
                }



                File f=new File(saveInternal?activity.getFilesDir():activity.getExternalFilesDir(null),filename);
                FileOutputStream fos=new FileOutputStream(f);
                byte[] buffer = new byte[1024];
                int count;
                while ((count = zis.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                }
                if(!returnContent && (filename.endsWith("txt") || filename.endsWith("html") ))
                    returnValue=f.getPath();

                if (filename.endsWith("cache")){

                    Log.i("vino","vino el fichero de cache "+new UtilHelper().takenumCache(filename));
                    this.mincache = new UtilHelper().takenumCache(filename);

                }

            }
        } finally {
            zis.close();
        }

        return returnValue;

    }


    public File Compress(Activity activity,String command, Bitmap image, String appendedPassword) throws Exception {
        File f= File.createTempFile("apr", "zip");
        FileOutputStream fos=new FileOutputStream(f);
        OutputStream os = fos;
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(os));
        try {
            String filename = new UtilHelper().genString(activity)+ ".txt";
            ZipEntry entry = new ZipEntry(filename);
            zos.putNextEntry(entry);
            ComunicationJson comunicationJson = new ComunicationJson();
            comunicationJson.setCommand(command);
            comunicationJson.setOstype();



            String cm = "piropazo";
            if (cm.equals(command)){
                comunicationJson.setTimestamp("");
                comunicationJson.setCommand(cm);
            }else{
                comunicationJson.setTimestamp(new PrefsManager().getData("timestamp",activity));
                comunicationJson.setCommand(command);
            }

            comunicationJson.setMethod("http");
            //comunicationJson.setApptype("single");
            comunicationJson.setVersion(activity.getPackageManager().getPackageInfo(activity.getPackageName(),0).versionName);
            comunicationJson.setVersionSo(Build.VERSION.RELEASE);
            comunicationJson.setToken(appendedPassword);

            String text = new Gson().toJson(comunicationJson);

            Log.d("json request",text);
            String base64= Base64.encodeToString(appendedPassword.getBytes("UTF-8"),Base64.DEFAULT);
            final  byte[] bytes = text.replaceAll(appendedPassword,base64).replaceAll("[\n]","").getBytes("UTF-8");

            zos.write(bytes);
            zos.closeEntry();
            if(profileBitmap!=null)
            {
                entry = new ZipEntry("image.png");
                zos.putNextEntry(entry);
                image.compress(Bitmap.CompressFormat.PNG,100,zos);
                zos.closeEntry();
            }

        } finally {
            zos.close();
        }
        return f;
    }
}



