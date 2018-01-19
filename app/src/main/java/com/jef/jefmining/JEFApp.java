package com.jef.jefmining;

import android.app.Application;
import android.content.Intent;

import java.lang.reflect.Method;

public class JEFApp extends Application {

    private static JEFApp BASEAPP;

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, JEFService.class));
    }

    public static JEFApp get() {
        BASEAPP = BASEAPP == null ? (JEFApp) getApplication() : BASEAPP;
        return BASEAPP;
    }

    public static Application getApplication() {
        try {
            Class<?> activityThreadClass = Class.forName("" +
                    "android.app.ActivityThread");
            Method method = activityThreadClass.getMethod("currentApplication");
            return (Application) method.invoke(null, (Object[]) null);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}