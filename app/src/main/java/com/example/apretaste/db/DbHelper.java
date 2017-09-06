package com.example.apretaste.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cjam on 25/10/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "apretaste.db";
    private static final int    DB_SHEME_VERSION = 1;
    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_SHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     db.execSQL(DataBaseManager.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
