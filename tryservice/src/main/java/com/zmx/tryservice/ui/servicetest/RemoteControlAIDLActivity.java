package com.zmx.tryservice.ui.servicetest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.zmx.tryservice.IRemoteService;
import com.zmx.tryservice.R;
import com.zmx.tryservice.servicetest.RemoteServices;

public class RemoteControlAIDLActivity extends AppCompatActivity implements View.OnClickListener {

    private Button fireButton;
    private EditText messageEditText;
    private IRemoteService remoteService;
    private boolean isBound;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, RemoteServices.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_service);

        fireButton = (Button) findViewById(R.id.fire_button);
        messageEditText = (EditText) findViewById(R.id.fired_edit_text);

        fireButton.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    //region View.OnClickListener
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fire_button: {
                if (remoteService != null) {
                    try {
                        String message = messageEditText.getText().toString().trim();
                        remoteService.fireNotification(message);
                    } catch (RemoteException e) {
                        Log.e("RemoteControlAIDL", Log.getStackTraceString(e));
                    }
                }
            }
                break;
        }
    }
    //endregion

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            remoteService = IRemoteService.Stub.asInterface(iBinder);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            remoteService = null;
            isBound = false;
        }
    };
}
