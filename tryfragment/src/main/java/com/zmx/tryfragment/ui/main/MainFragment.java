package com.zmx.tryfragment.ui.main;

import androidx.lifecycle.Observer;
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

import java.util.ArrayList;
import java.util.List;

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

//        MainAdapter mainAdapter = new MainAdapter(mViewModel.getList().getValue());
        MainAdapter mainAdapter = new MainAdapter(mViewModel.getOriginalList());
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

        ListDataAdapter liveDataAdapter = new ListDataAdapter();
        binding.recyclerView2.setHasFixedSize(true);
        binding.recyclerView2.setAdapter(liveDataAdapter);
//        mViewModel.getList().observe(getViewLifecycleOwner(), newList -> {
//            Log.e("MainFragment", "mViewModel observe");
//            liveDataAdapter.submitList(null);
//            liveDataAdapter.submitList(newList);
////            liveDataAdapter.submitList(newList);
//        });

//        mViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
//            @Override
//            public void onChanged(List<String> strings) {
//                Log.e("MainFragment", "mViewModel observe");
//                liveDataAdapter.submitList(new ArrayList<>(strings));
//            }
//        });

        mViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                Log.e("MainFragment", "mViewModel observe");
                liveDataAdapter.submitList(strings);
                liveDataAdapter.notifyDataSetChanged();
            }
        });
        liveDataAdapter.setOnItemClickListener(new ListDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String s) {
                Log.e("MainFragment", "onItemClick");
//                mViewModel.removeItemFromList(s);
//                liveDataAdapter.notifyDataSetChanged();
            }
        });

//        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
//            @Override
//            public void onChanged(@Nullable List<Note> notes) {
//                Log.e("MainActivity", "Updating list of notes from LiveData in ViewModel");
//                adapter.submitList(notes);
//            }
//        });


        binding.recyclerView3.setAdapter(mainAdapter);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

        Log.e("MainFragment", "onActivityCreated");
    }
}