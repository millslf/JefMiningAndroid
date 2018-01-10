package com.jef.jefmining;

import android.app.Application;
import android.content.Intent;

public class JEFApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, JEFService.class));
    }
}