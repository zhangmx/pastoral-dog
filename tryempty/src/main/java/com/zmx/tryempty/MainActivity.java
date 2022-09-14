package com.zmx.tryempty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zmx.tryempty.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.fragmentContainer.getId(),
                            ItemFragment.newInstance(4))
                    .commitNow();
        }

//        setContentView(R.layout.activity_main);
    }
}