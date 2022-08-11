package com.zmx.tryservice.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zmx.tryservice.databinding.FragmentBlankBinding;
import com.zmx.tryservice.services.MyService;

public class BlankFragment extends Fragment {

    private FragmentBlankBinding binding;
    private BlankViewModel blankViewModel;

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
//            Object path = msg.obj;
//            Toast.makeText(getApplicationContext(), path.toString(), Toast.LENGTH_LONG).show();
//            logger.append(path.toString() + "\n");
            blankViewModel.setText(msg.obj.toString());
            return true;
        }
    });

    public static BlankFragment newInstance() {
        return new BlankFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBlankBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        blankViewModel = new ViewModelProvider(this).get(BlankViewModel.class);

        final TextView show_service_message = binding.showServiceMessage;
        blankViewModel.getText().observe(getViewLifecycleOwner(), show_service_message::setText);

        final Button startServiceBtn = binding.startServiceBtn;
        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent number5 = new Intent(getContext(), MyService.class);
                number5.putExtra("times", 5);
                Messenger messenger = new Messenger(handler);
                number5.putExtra("MESSENGER", messenger);
                getContext().startService(number5);
            }
        });


    }

}