package com.zmx.myfullscreen.observer;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

public class MainObserver implements LifecycleEventObserver {

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                System.out.println("MainObserver onCreate");
                break;
            case ON_START:
                System.out.println("MainObserver onStart");
                break;
            case ON_RESUME:
                System.out.println("MainObserver onResume");
                break;
            case ON_PAUSE:
                System.out.println("MainObserver onPause");
                break;
            case ON_STOP:
                System.out.println("MainObserver onStop");
                break;
            case ON_DESTROY:
                System.out.println("MainObserver onDestroy");
                break;
            default:
                break;
        }
    }
}
