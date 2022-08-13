package com.zmx.tryfragment.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.javafaker.Faker;

import java.util.List;

public class MainViewModel extends ViewModel {

    private MutableLiveData<List<String>> stringLiveData;

    List<String> originalList;


    public MainViewModel() {
        stringLiveData = new MutableLiveData<>();

        initList();
    }



    public MutableLiveData<List<String>> getList() {
        return stringLiveData;
    }


    public void addItemToList() {
        // List.of("Hello", "World")

//        stringLiveData.getValue().add("Hello World!");
        originalList.add("Hello World!");

    }

    public void initList() {
        populateList();

        stringLiveData.setValue(originalList);
    }

    private void populateList() {

        originalList = new java.util.ArrayList<>();
        Faker faker = new Faker();
        originalList.add(faker.name().name());
        originalList.add(faker.name().name());
        originalList.add(faker.address().city());

    }
}