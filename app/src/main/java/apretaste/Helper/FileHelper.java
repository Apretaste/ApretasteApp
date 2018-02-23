package apretaste.Helper;

import android.content.Context;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



/**
 * Created by cjam on 8/10/2017.
 */

public class FileHelper {

    public void createDirectory(Context context ,String name){
        File mydir = context.getDir( name, Context.MODE_PRIVATE); //Creating an internal dir;
        if(!mydir.exists())
        {
            mydir.mkdirs();
        }

    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }



}
