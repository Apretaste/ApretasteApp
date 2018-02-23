package apretaste;

import android.util.Base64;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

/**
 * Created by cjam on 7/10/2017.
 * Clase se utiliza para convertir en json las peticiones
 */

public class ComunicationJson {
    public String command;
    public String timestamp;
    public String token;
    public String appversion;
    public String osversion;

    public void setVersionSo(String versionSo) {
        this.osversion = versionSo;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setToken(String token) {

        this.token = token;
    }

    public void setVersion(String version) {
        this.appversion = version;
    }





}
