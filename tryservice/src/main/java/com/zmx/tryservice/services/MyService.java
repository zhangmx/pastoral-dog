package com.zmx.tryservice.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
        Log.e("MyService", "MyService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MyService", "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.e("MyService", "onBind");
        throw new UnsupportedOperationException("Not yet implemented");
    }
}