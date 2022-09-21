package com.zmx.tryempty.viewPager2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.zmx.tryempty.databinding.ActivityMainBinding;
import com.zmx.tryempty.viewPager2.fragments.FragmentViewPagerActivity;
import com.zmx.tryempty.viewPager2.views.ViewsSliderActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnViewsDemo.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ViewsSliderActivity.class));
        });

        binding.btnFragmentDemo.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, FragmentViewPagerActivity.class));
        });
    }
}