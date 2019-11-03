package com.mimi.mimigroup.app;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import com.orhanobut.hawk.Hawk;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //MT-API-19
        MultiDex.install(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        Hawk.init(getApplicationContext()).build();
    }

}