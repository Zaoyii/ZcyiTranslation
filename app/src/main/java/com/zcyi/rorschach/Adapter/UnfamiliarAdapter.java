package com.zcyi.rorschach.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zcyi.rorschach.MyEntity.Words;
import com.zcyi.rorschach.PagerTranslationInfo.TranslationInfoActivity;
import com.zcyi.rorschach.R;
import com.zcyi.rorschach.Util.Constant;
import com.zcyi.rorschach.Util.UtilMethod;
import com.zcyi.rorschach.databinding.ItemUnfamiliarBinding;
import com.zcyi.rorschach.myFragment.TranslationVM;

import java.util.ArrayList;

public class UnfamiliarAdapter extends RecyclerView.Adapter<UnfamiliarAdapter.UnfamiliarHolder> {
    ArrayList<Words> list;
    Activity activity;
    ItemUnfamiliarBinding binding;
    TranslationVM translationVM;

    public UnfamiliarAdapter(Activity context, ArrayList<Words> list, TranslationVM vm) {
        this.list = list;
        this.activity = context;
        this.translationVM = vm;
        System.out.println("-=-=----=-vm-=-==-=-=-" + translationVM);
    }

    @NonNull
    @Override
    public UnfamiliarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.item_unfamiliar, parent, false);
        return new UnfamiliarHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull UnfamiliarHolder holder, int position) {

        binding.words.setText(list.get(position).getQuery());
        binding.to.setText(list.get(position).getTranslation());
        binding.translation.setText(list.get(position).getExplains().replace("\n", " "));
        Intent intent = new Intent(activity, TranslationInfoActivity.class);
        intent.putExtra("words", UtilMethod.ObjectToString(list.get(position)));
        binding.unfamiliarLin.setOnClickListener(v -> activity.startActivity(intent));
        Log.e(Constant.TAG, "im position:" + position+"and im "+list.get(position).getQuery());
    }

    public ArrayList<Words> getList() {
        return list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class UnfamiliarHolder extends RecyclerView.ViewHolder {
        public UnfamiliarHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
