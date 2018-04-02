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

import apretaste.ComunicationJson;
import apretaste.activity.DrawerActivity;

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








    public String takenumCache(String num){
        return  num.substring(0,num.length()-6);

    }



}
