package com.zmx.tryservice.ui.main;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();

    private LiveData<String> mText = Transformations.map(mIndex, input -> "Hello world from section: " + input);

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }

    private MutableLiveData<String> mText2 = new MutableLiveData<>();

    public void setText2(String text) {
        mText2.setValue(text);
    }

    public LiveData<String> getText2() {
        return mText2;
    }
}