package com.zmx.tryfragment.ui.main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.zmx.tryfragment.databinding.ItemManBinding;

import java.util.List;

// String to Note Object
public class ListDataAdapter extends ListAdapter<String, ListDataAdapter.MainViewHolder> {
    private OnItemClickListener listener;

//    private final MutableLiveData<List<String>> stringLiveData;

    public ListDataAdapter() {
//        public ListDataAdapter(MutableLiveData<List<String>> stringLiveData) {
        super(DIFF_CALLBACK);
//        this.stringLiveData = stringLiveData;
    }


    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(String oldItem, String newItem) {
            Log.e("ListDataAdapter", "areItemsTheSame: " + oldItem + " " + newItem);
//            Log.e("NoteAdapter", "areItemsTheSame: " + oldItem.getId() + " " + newItem.getId());
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(String oldItem, String newItem) {
            Log.e("ListDataAdapter", "areContentsTheSame: " + oldItem + " " + newItem);
//            Log.e("NoteAdapter", "areContentsTheSame: " + oldItem.getTitle() + " " + newItem.getTitle());
            return oldItem.equals(newItem);
//                    oldItem.getTitle().equals(newItem.getTitle()) &&
//                    oldItem.getDescription().equals(newItem.getDescription()) &&
//                    oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemManBinding itemManBinding = ItemManBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MainViewHolder(itemManBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String currentNote = getItem(position);

        holder.textView.setText(currentNote);
//        holder.itemManBinding.executePendingBindings();

        holder.textView2.setText(String.valueOf(position));
    }

    // https://github.com/LaZyRaifur/Room.ViewModel.LiveData.RecyclerView.MVVM
    public interface OnItemClickListener {
//        void onItemClick(Note note);
        void onItemClick(String note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class MainViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        public final ItemManBinding itemManBinding;

        public TextView textView;
        public TextView textView2;

        public MainViewHolder(ItemManBinding binding) {
            super(binding.getRoot());
            this.itemManBinding = binding;
            textView = binding.textView;
            textView2 = binding.textView2;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }
}
