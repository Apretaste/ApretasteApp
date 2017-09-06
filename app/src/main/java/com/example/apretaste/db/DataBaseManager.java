package com.example.apretaste.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Path;
import android.util.Log;
import android.widget.Switch;

import static android.provider.BaseColumns._ID;

/**
 * Created by Javier on 24/03/2017.
 */

public class DataBaseManager {

    public static final String TABLE_NAME="cache";

    public static final String CN_ID="_id";
    public static final String CN_PETICION="peticion";
    public static final String CN_CACHE = "cache";
    public static final String CN_PATH="path";


    private SQLiteDatabase db;
    private DbHelper helper;


    public static final  String CREATE_TABLE = "create table " +TABLE_NAME+ " ("
            +CN_ID + " integer primary key autoincrement,"
            +CN_PETICION + " text not null,"
            +CN_CACHE + " text not null,"
            +CN_PATH + " text not null);";

    public DataBaseManager(Context context) {

         helper = new DbHelper(context);
         db = helper.getWritableDatabase();
        Log.i("created","The database is created");

    }

    public ContentValues generarContentValues(String peticion,String cache , String path)
    {
        ContentValues valores = new ContentValues();
        valores.put(CN_PETICION,peticion);
        valores.put(CN_CACHE,cache);
        valores.put(CN_PATH,path);
        return valores;
    }

    public void insertar(String peticion,String cache , String path){
        db.insert(TABLE_NAME,null,generarContentValues(peticion,cache, path));
        //db.execSQL("INSERT INTO "+TABLE_NAME+" VALUES (null,'"+texto+"','"+comando+"' "+texto_reply+")");
    }

    public void eliminar(String _id){
        db.delete(TABLE_NAME,CN_ID+"=?",new String[]{_id});
    }



    public Cursor getCache(String peticion){
        String[] col = new String[]{CN_ID,CN_PETICION,CN_CACHE,CN_PATH};
        return  db.query(TABLE_NAME,col,CN_PETICION + "=?",new String[]{peticion},null,null,null);
    }

    public Cursor getData(String peticion){

       // Cursor res =  db.rawQuery( "select * from cache where petition="+peticion+"", null );
        String[] col = new String[]{CN_ID,CN_PETICION,CN_CACHE,CN_PATH};
        return  db.query(TABLE_NAME,col,CN_PETICION + "=?",new String[]{peticion},null,null,null);
    }

    public String Read(){
        String resultado = "";
        String[] col = {"_ID","peticion","cache","path"};
        Cursor c = db.query("cache",col,null,null,null,null,null,null);
        int id,pet,cache,path;
        id = c.getColumnIndex(_ID);
        pet = c.getColumnIndex("peticion");
        cache = c.getColumnIndex("cache");
        path = c.getColumnIndex("path");
        c.moveToLast();
        resultado = c.getString(id)+" "+c.getString(pet)+" "+c.getString(cache)+" "+c.getString(path)+"\n";
      //  resultado = c.getString(pet);
        return resultado;
    }

    public String getAll(String peticion,String campo){
        String resultado = "";
        String[] col = {"_ID","peticion","cache","path"};


        Cursor c = db.query("cache",col,"peticion" + "=?",new String[]{peticion},null,null,null);
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




    public void deleteAll(){
        db.execSQL("DELETE FROM cache;");
    }



}









