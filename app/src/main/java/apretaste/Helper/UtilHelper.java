package apretaste.Helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.example.apretaste.R;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import apretaste.Comunication.ComunicationJson;
import apretaste.ui.DrawerActivity;

/**
 * Created by cjam on 21/1/2018.
 */

public class UtilHelper {
    private static Random random;
    static
    {

        random=new Random();
    }

    private Bitmap profileBitmap=null;
    private Date timestamp;
    private boolean saveInternal = false;
    private Context activity;

    private boolean returnContent = false;


    public void setReturnContent(boolean returnContent) {
        this.returnContent = returnContent;
    }



    public String genString(Activity activity)
    {
        String[] words = activity.getResources().getStringArray(R.array.words);
        String w1=words[random.nextInt(words.length)];
        String w2=words[random.nextInt(words.length)];
        String w3=words[random.nextInt(words.length)];
        return w1+" "+w2+" "+w3;
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
            Log.e("command",command);
            if (!command.equals("perfil status")) {
                comunicationJson.setTimestamp(DrawerActivity.pro.timestamp);
            }else{
                comunicationJson.setTimestamp("");
                comunicationJson.setCommand("status");
            }
            comunicationJson.setVersion(activity.getPackageManager().getPackageInfo(activity.getPackageName(),0).versionName);
            comunicationJson.setVersionSo(Build.VERSION.RELEASE);
            comunicationJson.setToken(appendedPassword);
            comunicationJson.setMethod("http");

            String text = new Gson().toJson(comunicationJson);
            String base64= Base64.encodeToString(appendedPassword.getBytes("UTF-8"),Base64.DEFAULT);
            final  byte[] bytes = text.replaceAll(appendedPassword,base64).replaceAll("[\n]","").getBytes("UTF-8");

            zos.write(bytes);
            zos.closeEntry();
            if(profileBitmap!=null)
            {
                entry = new ZipEntry("profile.png");
                zos.putNextEntry(entry);
                image.compress(Bitmap.CompressFormat.PNG,100,zos);
                zos.closeEntry();
            }

        } finally {
            zos.close();
        }
        return f;
    }




    public String takenumCache(String num){
        return  num.substring(0,num.length()-6);

    }



}
