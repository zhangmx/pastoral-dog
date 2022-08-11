package com.zmx.tryservice.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.zmx.tryservice.services.action.FOO";
    private static final String ACTION_BAZ = "com.zmx.tryservice.services.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.zmx.tryservice.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.zmx.tryservice.services.extra.PARAM2";

    private static final String TAG = "MyIntentService";

    private static final String MESSENGER = "com.zmx.tryservice.services.action.MESSENGER";

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MyIntentService", "onCreate");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2, Messenger messenger) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);

        intent.putExtra(MESSENGER, messenger);

        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2, Messenger messenger) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);

        intent.putExtra(MESSENGER, messenger);

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("MyIntentService", "onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (action != null) {
                Messenger messenger = (Messenger) intent.getExtras().get(MESSENGER);

                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                if (ACTION_FOO.equals(action)) {
                    handleActionFoo(param1, param2, messenger);
                } else if (ACTION_BAZ.equals(action)) {
                    handleActionBaz(param1, param2, messenger);
                }
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2, Messenger messenger) {
        // TODO: Handle action Foo
        Log.e("MyIntentService", "handleActionFoo");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2, Messenger messenger) {
        // TODO: Handle action Baz
        Log.e("MyIntentService", "handleActionBaz");

        // run long time task here,every 2 seconds send a broadcast to update the progress bar
        for (int i = 0; i < 100; i++) {
            try {
                synchronized (this) {
                    wait(2000);  //wait 5 seconds.
                }

//                Thread.sleep(2000);
                Log.e("MyIntentService", "handleActionBaz " + i);

                Message msg = Message.obtain();
                msg.obj = "handleActionBaz " + i;
                msg.what = i;
                messenger.send(msg);

            } catch (InterruptedException | RemoteException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent("com.zmx.tryservice.services.action.UPDATE_PROGRESS");
            intent.putExtra("progress", i);
            sendBroadcast(intent);
        }


//        throw new UnsupportedOperationException("Not yet implemented");
    }


}