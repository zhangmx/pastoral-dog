package com.zmx.tryservice;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.zmx.tryservice.services.MyIntentService;
import com.zmx.tryservice.services.MyService;

public class ServiceApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // log process id
        Log.e("ServiceApp", "process id: " + Process.myPid());
        // log thread id
        Log.e("ServiceApp", "thread id: " + Thread.currentThread().getId());

        Log.e("ServiceApp", "onCreate");
//        ContextCompat.startForegroundService(this, new Intent(this, MyService.class));
//        ContextCompat.startForegroundService(this, new Intent(this, MyIntentService.class));
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
