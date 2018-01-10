package com.jef.jefmining;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class JEFService extends Service {
    public JEFService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
