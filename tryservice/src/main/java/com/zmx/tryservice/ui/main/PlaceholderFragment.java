package com.zmx.tryservice.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.zmx.tryservice.R;
import com.zmx.tryservice.databinding.FragmentMainBinding;
import com.zmx.tryservice.services.MyIntentService;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentMainBinding binding;

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            pageViewModel.setText2(msg.obj.toString());

            return true;
        }
    });

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

        Log.d("PlaceholderFragment", "onCreate");
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {


        Log.d("PlaceholderFragment", "onCreateView");

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.sectionLabel;
        pageViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final TextView textView2 = binding.textView;
        pageViewModel.getText2().observe(getViewLifecycleOwner(), textView2::setText);

        binding.button.setOnClickListener((view)->{
            Messenger messenger = new Messenger(handler);
            MyIntentService.startActionBaz(getContext(), "hello","world", messenger);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("PlaceholderFragment", "onDestroyView");
        binding = null;
    }
}