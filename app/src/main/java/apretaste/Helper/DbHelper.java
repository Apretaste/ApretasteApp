package apretaste.Helper;


import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;


import apretaste.Notifications;
import apretaste.ProfileInfo;
import apretaste.Services;

import static android.provider.BaseColumns._ID;

import static apretaste.Helper.DbHelper.BasicTable.CN_BASIC_CREDIT;
import static apretaste.Helper.DbHelper.BasicTable.CN_BASIC_TIMESTAMP;
import static apretaste.Helper.DbHelper.BasicTable.CN_BASIC_USERNAME;
import static apretaste.Helper.DbHelper.CacheTable.CN_CACHE_CACHE;

import static apretaste.Helper.DbHelper.CacheTable.CN_CACHE_PATH;
import static apretaste.Helper.DbHelper.CacheTable.CN_CACHE_PETICION;
import static apretaste.Helper.DbHelper.CacheTable.TABLE_CACHE;

import static apretaste.Helper.DbHelper.NotificationsTable.CN_NOTIFICATIONS_ID;
import static apretaste.Helper.DbHelper.NotificationsTable.CN_NOTIFICATIONS_LINK;
import static apretaste.Helper.DbHelper.NotificationsTable.CN_NOTIFICATIONS_READ;
import static apretaste.Helper.DbHelper.NotificationsTable.CN_NOTIFICATIONS_RECEIVED;
import static apretaste.Helper.DbHelper.NotificationsTable.CN_NOTIFICATIONS_SERVICE;
import static apretaste.Helper.DbHelper.NotificationsTable.CN_NOTIFICATIONS_TEXT;
import static apretaste.Helper.DbHelper.NotificationsTable.TABLE_NOTIFICATIONS;
import static apretaste.Helper.DbHelper.ServicesTable.CN_SERVICES_CATEGORY;
import static apretaste.Helper.DbHelper.ServicesTable.CN_SERVICES_CREATOR;
import static apretaste.Helper.DbHelper.ServicesTable.CN_SERVICES_DESCRIPTION;
import static apretaste.Helper.DbHelper.ServicesTable.CN_SERVICES_FAV;
import static apretaste.Helper.DbHelper.ServicesTable.CN_SERVICES_ICON;
import static apretaste.Helper.DbHelper.ServicesTable.CN_SERVICES_ID;
import static apretaste.Helper.DbHelper.ServicesTable.CN_SERVICES_NAME;
import static apretaste.Helper.DbHelper.ServicesTable.CN_SERVICES_UPDATED;
import static apretaste.Helper.DbHelper.ServicesTable.CN_SERVICES_USED;
import static apretaste.Helper.DbHelper.ServicesTable.TABLE_SERVICES;


/**
 * Created by cjam on 5/10/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper singleton;
    public static final String DB_NAME = "ap.db";
    private static final int DB_SHEME_VERSION = 1;


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_SHEME_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CacheTable.CREATE_TABLE_CACHE);
        db.execSQL(ServicesTable.CREATE_TABLE_SERVICES);
        db.execSQL(NotificationsTable.CREATE_TABLE_NOTIFICATIONS);
        db.execSQL(HistoryTable.CREATE_TABLE_HISTORY);
        db.execSQL(BasicTable.CREATE_TABLE_BASIC);

	}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*Metodos Generales Reutilizables para todas las tablas*/
    public void deleteAllTable(String table){
        getWritableDatabase().execSQL("DELETE FROM "+table+";");

    }

    public int delBy(String table , String campo , String value ){
       return getWritableDatabase().delete(table,campo+"=?",new String[]{value});
    }



    public static DbHelper getSingleton(Context context)
    {
        if(singleton==null)
            singleton=new DbHelper(context.getApplicationContext());
        return singleton;
    }


    /*Tabla de Cache*/
    static final class CacheTable
    {

        public  static final String TABLE_CACHE="cache";
        public  static final String CN_CACHE_ID="_id";
        public  static final String CN_CACHE_PETICION="peticion";
        public  static final String CN_CACHE_CACHE = "cache";
        public  static final String CN_CACHE_PATH="path";

        private static final String CACHE_TABLE_DROP_QUERY ="DROP TABLE "+ TABLE_CACHE +";";

        public static final  String CREATE_TABLE_CACHE = "create table " +TABLE_CACHE+ " ("
                +CN_CACHE_ID + " integer primary key autoincrement,"
                +CN_CACHE_PETICION + " text not null,"
                +CN_CACHE_CACHE + " text not null,"
                +CN_CACHE_PATH + " text not null);";
    }

    /*Tabla Hisotry*/
    static final class HistoryTable
    {
        public static final String TABLE_HISTORY="history";
        public static final String CN_HISTORY_ID="_id";
        public static final String CN_HISTORY_SERVICE="service";
        public static final String CN_HISTORY_COMMAND = "command";
        public static final String CN_HISTORY_PATH="path";
        public static final String CN_HISTORY_DATE="date";

        private static final String HISTORY_TABLE_DROP_QUERY ="DROP TABLE "+ TABLE_HISTORY +";";



        public static final  String CREATE_TABLE_HISTORY = "create table " +TABLE_HISTORY+ " ("
                +CN_HISTORY_ID + " integer primary key autoincrement,"
                +CN_HISTORY_SERVICE + " text not null,"
                +CN_HISTORY_COMMAND + " text not null,"
                +CN_HISTORY_PATH+ " text not null,"
                +CN_HISTORY_DATE+ " text not null);";
    }


    /*Metodos de Notifications*/

    /*Tabla Notifications*/
    static  final class NotificationsTable{
        public static final String TABLE_NOTIFICATIONS="notifications";

        public static final String CN_NOTIFICATIONS_ID="_id";
        public static final String CN_NOTIFICATIONS_RECEIVED="received";
        public static final String CN_NOTIFICATIONS_SERVICE = "service";
        public static final String CN_NOTIFICATIONS_TEXT="text";
        public static final String CN_NOTIFICATIONS_LINK="link";
        public static final String CN_NOTIFICATIONS_READ="read";



        public static final  String CREATE_TABLE_NOTIFICATIONS = "create table " +TABLE_NOTIFICATIONS+ " ("
                +CN_NOTIFICATIONS_ID + " integer primary key autoincrement,"
                +CN_NOTIFICATIONS_RECEIVED + " text,"
                +CN_NOTIFICATIONS_SERVICE + " text ,"
                +CN_NOTIFICATIONS_TEXT + " text ,"
                +CN_NOTIFICATIONS_LINK + " text ,"
                +CN_NOTIFICATIONS_READ + " int);";

    }

    /*Table Services*/

    static final class BasicTable{
        public static final String TABLE_BASIC="basic";

        public static final String CN_BASIC_ID                = "_id";
        public static final String CN_BASIC_TIMESTAMP        ="timestamp";
        public static final String CN_BASIC_USERNAME         ="username";
        public static final String CN_BASIC_CREDIT          ="credit";

        public static final  String CREATE_TABLE_BASIC = "create table " +TABLE_BASIC+ " ("
                +CN_BASIC_ID + " integer primary key autoincrement,"
                +CN_BASIC_TIMESTAMP + " text ,"
                +CN_BASIC_USERNAME + " text ,"
                +CN_BASIC_CREDIT + " text );";




    }
    public ContentValues generarValueBasic(String timestamp, String username , String credit)
    {
        ContentValues valores = new ContentValues();
        valores.put(CN_BASIC_TIMESTAMP,timestamp);
        valores.put(CN_BASIC_USERNAME,username);
        valores.put(CN_BASIC_CREDIT,credit);
        return valores;
    }


    static final class ServicesTable{
        public static final String TABLE_SERVICES="services";
        public static final String CN_SERVICES_ID            ="_id";
        public static final String CN_SERVICES_NAME          ="service";
        public static final String CN_SERVICES_DESCRIPTION   = "description";
        public static final String CN_SERVICES_CATEGORY      = "category";
        public static final String CN_SERVICES_CREATOR       = "creator";
        public static final String CN_SERVICES_UPDATED       ="updated";
        public static final String CN_SERVICES_ICON          ="icon";
        public static final String CN_SERVICES_USED          ="used";
        public static final String CN_SERVICES_FAV           ="fav";

        private static final String SERVICES_TABLE_DROP_QUERY ="DROP TABLE "+ TABLE_SERVICES +";";




        public static final  String CREATE_TABLE_SERVICES = "create table " +TABLE_SERVICES+ " ("
                +CN_SERVICES_ID + " integer primary key autoincrement,"
                +CN_SERVICES_NAME + " text not null,"
                +CN_SERVICES_DESCRIPTION + " text not null,"
                +CN_SERVICES_CATEGORY + " text not null,"
                +CN_SERVICES_CREATOR + " text not null,"
                +CN_SERVICES_UPDATED + " text not null,"
                +CN_SERVICES_ICON + " text not null,"
                +CN_SERVICES_USED + " int ,"
                +CN_SERVICES_FAV + " int );";



    }




    /*Metodos de Cache*/
    public ContentValues generarValueCache(String peticion, String cache , String path)
    {
        ContentValues valores = new ContentValues();
        valores.put(CN_CACHE_PETICION,peticion);
        valores.put(CN_CACHE_CACHE,cache);
        valores.put(CN_CACHE_PATH,path);
        return valores;
    }

    public ContentValues generarValueCache(String text)
    {
        ContentValues valores = new ContentValues();
        valores.put(CN_NOTIFICATIONS_TEXT,text);

        return valores;
    }


    public long addCache(String peticion, String cache , String path){
       return getWritableDatabase().insert(TABLE_CACHE,null,generarValueCache(peticion,cache, path));

    }




    public String getAllCache(String peticion,String campo){
        String resultado = "";
        String[] col = {"_ID","peticion","cache","path"};


        Cursor c = getReadableDatabase().query("cache",col,"peticion" + "=?",new String[]{peticion},null,null,null);
        int id,pet,cache,path;
        id = c.getColumnIndex(_ID);
        pet = c.getColumnIndex("peticion");
        cache = c.getColumnIndex("cache");
        path = c.getColumnIndex("path");
        c.moveToLast();


        if (c.getCount() > 0){
            if (campo.equals("id")){
                resultado = c.getString(id);
            }else if (campo.equals("peticion")){
                resultado = c.getString(pet);
            }else if (campo.equals("cache")){
                resultado = c.getString(cache);
            }else if (campo.equals("path")){
                resultado = c.getString(path);
            }else if (campo.equals("all")){
                resultado = c.getString(id)+" "+c.getString(pet)+" "+c.getString(cache)+" "+c.getString(path)+"\n";
            }

        }else{
            resultado = "";
        }
        return resultado;
    }




    /*Metodos de Notifications*/


    public void addNotification(ProfileInfo.Notifications[] servicesLists){


        for (int i = 0; i < servicesLists.length; i++) {
            ContentValues valores = new ContentValues();
            valores.put(CN_NOTIFICATIONS_SERVICE,servicesLists[i].service);
            valores.put(CN_NOTIFICATIONS_TEXT,servicesLists[i].text);
            valores.put(CN_NOTIFICATIONS_LINK,servicesLists[i].link);
            valores.put(CN_NOTIFICATIONS_RECEIVED,servicesLists[i].received);
            valores.put(CN_NOTIFICATIONS_READ,0);


            getWritableDatabase().insert(TABLE_NOTIFICATIONS, null, valores);
        }
    }




    /*Metodos de Services*/


    public void setServiceUsed(String idService){
        ContentValues values=new ContentValues();
        values.put(ServicesTable.CN_SERVICES_USED , "1");
        getWritableDatabase().update(TABLE_SERVICES,values , ServicesTable.CN_SERVICES_ID + "=?",new String[]{String.valueOf(idService)});

    }


    public void favorite(String id , boolean status){
        ContentValues values = new ContentValues();
        values.put(ServicesTable.CN_SERVICES_FAV, status ? 1 : 0);
        getWritableDatabase().update(TABLE_SERVICES,values , ServicesTable.CN_SERVICES_ID + "=?",new String[]{String.valueOf(id)});
    }


    public void setRead(String id ){
        ContentValues values = new ContentValues();
        values.put(NotificationsTable.CN_NOTIFICATIONS_READ, 1);
        getWritableDatabase().update(TABLE_NOTIFICATIONS,values ,NotificationsTable.CN_NOTIFICATIONS_ID + "=?",new String[]{String.valueOf(id)});
    }

    public void setUsed(String id ){
        ContentValues values = new ContentValues();
        values.put(ServicesTable.CN_SERVICES_USED, 1);
        getWritableDatabase().update(TABLE_SERVICES,values ,ServicesTable.CN_SERVICES_ID + "=?",new String[]{String.valueOf(id)});
    }


    public int getCountNoRead(){
        String query = "SELECT * FROM " + TABLE_NOTIFICATIONS + " WHERE read = 0";
        Cursor c = getReadableDatabase().rawQuery(query, null);
        return c.getCount();
    }





    public void addService(ProfileInfo.Services[] servicesLists){
        for (int i = 0; i < servicesLists.length; i++) {
            if (!CheckIsDataAlreadyInDborNot(TABLE_SERVICES,ServicesTable.CN_SERVICES_NAME,servicesLists[i].name)){
                ContentValues valores = new ContentValues();
                valores.put(CN_SERVICES_NAME, servicesLists[i].name);
                valores.put(CN_SERVICES_DESCRIPTION, servicesLists[i].description);
                valores.put(CN_SERVICES_CATEGORY, servicesLists[i].category);
                valores.put(CN_SERVICES_CREATOR, servicesLists[i].creator);
                valores.put(CN_SERVICES_UPDATED, servicesLists[i].updated);
                valores.put(CN_SERVICES_ICON, servicesLists[i].icon);
                valores.put(CN_SERVICES_USED, 0);
                valores.put(CN_SERVICES_FAV, 0);
                getWritableDatabase().insert(TABLE_SERVICES, null, valores);
            }else{
                delBy(TABLE_SERVICES,ServicesTable.CN_SERVICES_NAME,servicesLists[i].name);
                addOneService(servicesLists[i].name,servicesLists[i].description,servicesLists[i].category,servicesLists[i].creator,servicesLists[i].updated,servicesLists[i].icon);
            }



        }

    }


    public void addOneService(String name,String description,String category, String creator , String updated , String icon){
        ContentValues valores = new ContentValues();
        valores.put(CN_SERVICES_NAME, name);
        valores.put(CN_SERVICES_DESCRIPTION, description);
        valores.put(CN_SERVICES_CATEGORY, category);
        valores.put(CN_SERVICES_CREATOR, creator);
        valores.put(CN_SERVICES_UPDATED, updated);
        valores.put(CN_SERVICES_ICON, icon);
        valores.put(CN_SERVICES_USED, 0);
        valores.put(CN_SERVICES_FAV, 0);
        getWritableDatabase().insert(TABLE_SERVICES, null, valores);
    }



    public boolean CheckIsDataAlreadyInDborNot(String table , String dbfield , String valuefield){
        String query = "SELECT *" +  " from " + table +
                " where " + dbfield + " = ?;";
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,new String[] { String.valueOf(valuefield) });
        return(cursor.moveToFirst());
    }



    public ArrayList<Services> getAllServices() {
        String query = "SELECT * FROM " + TABLE_SERVICES + " order by fav DESC , service ASC ";

        ArrayList<Services> services = new ArrayList<Services>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery(query, null);

        if (c != null) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndex(CN_SERVICES_ID));
                String name = c.getString(c.getColumnIndex(CN_SERVICES_NAME));
                String icon = c.getString(c.getColumnIndex(CN_SERVICES_ICON));
                String used = c.getString(c.getColumnIndex(CN_SERVICES_USED));
                String  fav = c.getString(c.getColumnIndex(CN_SERVICES_FAV));
                String des = c.getString(c.getColumnIndex(CN_SERVICES_DESCRIPTION));
                Services service = new Services();

                service.setId(id);
                service.setName(name);
                service.setIcon(icon);
                service.setFav(fav);
                service.setUsed(used);
                service.setDescription(des);

                services.add(service);
            }
        }

        return services;

    }




    public ArrayList<Notifications> getAllNotifications() {
        String query = "SELECT * FROM " + TABLE_NOTIFICATIONS + " order by received DESC";

        ArrayList<Notifications> notifications = new ArrayList<Notifications>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery(query, null);

        if (c != null) {
            while (c.moveToNext()) {

                String text = c.getString(c.getColumnIndex(CN_NOTIFICATIONS_TEXT));
                String link = c.getString(c.getColumnIndex(CN_NOTIFICATIONS_LINK));

                String service = c.getString(c.getColumnIndex(CN_NOTIFICATIONS_SERVICE));
                String received = c.getString(c.getColumnIndex(CN_NOTIFICATIONS_RECEIVED));
                String read = c.getString(c.getColumnIndex(CN_NOTIFICATIONS_READ));
                String id = c.getString(c.getColumnIndex(CN_NOTIFICATIONS_ID));

                Notifications notification = new Notifications();

                notification.setText(text);
                notification.setLink(link);
                notification.setRead(read);
                notification.setReceived(received);
                notification.setService(service);
                notification.setId(Integer.parseInt(id));

                notifications.add(notification);
            }
        }

        return notifications;

    }


    public String getServiceById(String idd , String campo)
    {
        String query = "SELECT * FROM " + TABLE_SERVICES+ " WHERE "+ ServicesTable.CN_SERVICES_ID + " = " +idd;
        Services sev = new Services() ;
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery(query, null);
        String resultado = "";
        if (c.getCount() > 0) {

            c.moveToFirst();
            String id = c.getString(c.getColumnIndex(CN_SERVICES_ID));
            String name = c.getString(c.getColumnIndex(CN_SERVICES_NAME));
            String icon = c.getString(c.getColumnIndex(CN_SERVICES_ICON));

            String creator = c.getString(c.getColumnIndex(CN_SERVICES_CREATOR));
            String category = c.getString(c.getColumnIndex(CN_SERVICES_CATEGORY));
            String updated = c.getString(c.getColumnIndex(CN_SERVICES_UPDATED));

            String fav = c.getString(c.getColumnIndex(CN_SERVICES_FAV));
            String des = c.getString(c.getColumnIndex(CN_SERVICES_DESCRIPTION));

            if (campo!=""){
                if (campo.equals("creator")){
                    resultado = creator;
                }else if(campo.equals("category")){
                    resultado = category;
                }else if(campo.equals("updated")){
                    resultado = updated;
                }else if(campo.equals("des")){
                    resultado = des;
                }else if(campo.equals("name")){
                    resultado = name;
                }else if(campo.equals("icon")){
                    resultado = icon;
                }else if(campo.equals("id")){
                    resultado = id;
                }else if(campo.equals("fav")){
                    resultado = fav;
                }
            }



        }
        return resultado;
    }



    public String getIdByName(String name){
       String dbfield = "service";
        String res = "";
        String query = "SELECT *" +  " from services  where " + dbfield + " = ?;";
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery(query,new String[] { String.valueOf(name) });
        if (c.getCount() > 0) {
            c.moveToFirst();
            res = c.getString(c.getColumnIndex(CN_SERVICES_ID));;
        }
        return  res;

    }

}








