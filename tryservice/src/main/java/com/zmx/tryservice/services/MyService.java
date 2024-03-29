package com.zmx.tryservice.services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Random;

public class MyService extends Service {

    public static final int MSG_REGISTER_CLIENT = 1;
    private Looper mServiceLooper;

    private MyServiceHandler mServiceHandler;

    private final IBinder mBinder = new MyServiceBinder();

    Messenger messenger = null;
    Random r;
    int i;

    public MyService() {
        Log.e("MyService", "MyService");
        // log process id
        Log.e("MyService", "process id: " + Process.myPid());
        // log thread id
        Log.e("MyService", "thread id: " + Thread.currentThread().getId());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        r = new Random();
        Log.e("MyService", "onCreate");

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new MyServiceHandler(mServiceLooper);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;//needed for stop.
        msg.setData(intent.getExtras());
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.e("MyService", "onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("MyService", "onUnbind Called.");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("MyService", "onDestroy Called.");
    }

    public void doWork(String message) {
        Log.e("MyService", "doWork:" + message);
        i = 0;

        if (messenger != null) {
//            Message msg = Message.obtain(null, MSG_REGISTER_CLIENT);
//            Bundle bundle = new Bundle();
//            bundle.putString("message", message);
//            msg.setData(bundle);
//            try {
//                messenger.send(msg);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }

            try {
                Message mymsg = Message.obtain();
                mymsg.obj = message + " xxxx ";
//            messenger.send(Message.obtain(null, MSG_REGISTER_CLIENT, message));
                messenger.send(mymsg);

            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }


        sendMessageToActivity(new Location("provider"),"message from service");

        sendDataToActivity();

    }

    public final class MyServiceBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    private final class MyServiceHandler extends Handler {
        public MyServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.i("MyService", " MyServiceHandler handleMessage Called.");
            // log process id
            Log.i("MyService", "process id: " + Process.myPid());
            // log thread id
            Log.i("MyService", "thread id: " + Thread.currentThread().getId());

            Bundle extras = msg.getData();

            int times = 0;

            if (extras != null) {
                times = extras.getInt("times", 0);
                messenger = (Messenger) extras.get("MESSENGER");
            } else {
                times = 10;
            }

            for (i = 0; i < times; i++) {
                synchronized (this) {
                    try {
                        wait(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String info = i + " random " + r.nextInt(100);
                Log.d("intentServer", info);
                if (messenger != null) {
                    Message mymsg = Message.obtain();
                    mymsg.obj = info;
                    try {
                        messenger.send(mymsg);
                    } catch (android.os.RemoteException e1) {
                        Log.w(getClass().getName(), "Exception sending message", e1);
                    }
//                } else {
//                    //no handler, so use notification
//                    makenoti(info);
                }
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
//            stopSelf(msg.arg1);
        }
    }



    private void sendMessageToActivity(Location l, String msg) {
        Intent intent = new Intent("GPSLocationUpdates");
        // You can also include some extra data.
        intent.putExtra("Status", msg);
        Bundle b = new Bundle();
        b.putParcelable("Location", l);
        intent.putExtra("Location", b);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        LocalBroadcastManager.getInstance(MyService.this).sendBroadcast(intent);
    }

    private void sendDataToActivity()
    {
        Intent sendLevel = new Intent();
        sendLevel.setAction("GET_SIGNAL_STRENGTH");
        sendLevel.putExtra( "LEVEL_DATA","Strength_Value");
        sendBroadcast(sendLevel);

    }

    public static class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("MyService", "onServiceConnected Called.");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}