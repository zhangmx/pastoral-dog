package com.zmx.tryfragment.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmx.tryfragment.R;
import com.zmx.tryfragment.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    FragmentMainBinding binding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("MainFragment", "onCreateView");
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        Log.e("MainFragment", "onViewCreated");

        binding = FragmentMainBinding.bind(view);

        binding.message.setText("Hello World!");

        MainAdapter mainAdapter = new MainAdapter(mViewModel.getList().getValue());

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(llm);
        binding.recyclerView.setAdapter(mainAdapter);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("MainFragment", "onClick");
                mViewModel.addItemToList();

//                mainAdapter.updateUserList(mViewModel.getList().getValue());
            //  refresh all data  lower performance
//                mainAdapter.notifyDataSetChanged();
//                https://stackoverflow.com/questions/68602157/it-will-always-be-more-efficient-to-use-more-specific-change-events-if-you-can
                mainAdapter.notifyItemInserted(mViewModel.getList().getValue().size() - 1);
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

        Log.e("MainFragment", "onActivityCreated");
    }
}