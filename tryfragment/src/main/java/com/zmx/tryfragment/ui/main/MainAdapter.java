package com.zmx.tryfragment.ui.main;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmx.tryfragment.databinding.ItemManBinding;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {


    private List<String> mList;

    public MainAdapter(List<String> value) {
        mList = value;
//        setHasStableIds(true);
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(ItemManBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String selected = mList.get(position);

        holder.textView.setText(selected);
        holder.textView2.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return this.mList.size();
    }

//    @SuppressLint("NotifyDataSetChanged")
//    public void updateUserList(final List<String> userArrayList) {
//        this.mList.clear();
//        this.mList = userArrayList;
//        notifyDataSetChanged();
//    }

    static class MainViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public TextView textView2;

        public MainViewHolder(ItemManBinding binding) {
            super(binding.getRoot());

            textView = binding.textView;
            textView2 = binding.textView2;
        }
    }



}
