package com.zmx.tryservice.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BlankViewModel extends ViewModel {

    private final MutableLiveData<String> mText = new MutableLiveData<>();

    public MutableLiveData<String> getText() {
        return mText;
    }

    public void setText(String msg) {
        mText.setValue(msg);
    }
}