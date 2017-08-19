package com.example.apretaste;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Created by Raymond Arteaga on 27/07/2017.
 */
  class TPT implements Executor {
    private TPT()
    {

    }
    public static final TPT Executor =new TPT();
    public void execute(@NonNull Runnable r) {

        try
        {
            new Thread(r).start();
        }
        catch (Exception e)
        {

        }
    }}
