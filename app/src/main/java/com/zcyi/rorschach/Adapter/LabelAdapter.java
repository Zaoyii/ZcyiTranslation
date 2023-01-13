package com.zcyi.rorschach.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zcyi.rorschach.R;
import com.zcyi.rorschach.databinding.ItemLabelBinding;

import java.util.List;

public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.LabelHolder> {

    ItemLabelBinding binding;
    Context context;
    List<String> list;

    public LabelAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LabelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_label, parent, false);

        return new LabelHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull LabelHolder holder, int position) {
        binding.setMyData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LabelHolder extends RecyclerView.ViewHolder {

        public LabelHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}