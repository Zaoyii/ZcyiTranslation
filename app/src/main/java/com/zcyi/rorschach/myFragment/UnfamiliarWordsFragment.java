package com.zcyi.rorschach.myFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zcyi.rorschach.Adapter.UnfamiliarAdapter;
import com.zcyi.rorschach.MyEntity.Words;
import com.zcyi.rorschach.PagerLogin.LoginActivity;
import com.zcyi.rorschach.R;
import com.zcyi.rorschach.Util.UtilMethod;
import com.zcyi.rorschach.databinding.FragmentUnfamiliarWordsBinding;

@SuppressLint("NotifyDataSetChanged")
public class UnfamiliarWordsFragment extends Fragment {
    FragmentUnfamiliarWordsBinding binding;
    AlertDialog.Builder exitDialog;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    UnfamiliarAdapter adapter;
    TranslationVM translationVM;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_unfamiliar_words, container, false);
        translationVM = new ViewModelProvider(requireActivity(), new SavedStateViewModelFactory(requireActivity().getApplication(), this)).get(TranslationVM.class);
        binding.setVm(translationVM);
        binding.setLifecycleOwner(this);
        init();
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        binding.unfamiliarList.removeAllViews();
        translationVM.selectAllWordsByUserId();
        super.onResume();
    }
    private void init() {
        preferences = UtilMethod.getPreferences(requireActivity().getApplication());
        editor = preferences.edit();

        exitDialog = new AlertDialog.Builder(getContext());
        exitDialog.setTitle("退出登录").setMessage("确定要退出登录吗?").setPositiveButton("确定", (dialog, which) -> {
            editor.putInt("isLogin", 0);
            editor.commit();
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            requireActivity().finish();
        }).setNegativeButton("取消", (dialog, which) -> {
            dialog.cancel();
        });
        binding.exitLogin.setOnClickListener(v -> exitDialog.show());
        adapter = new UnfamiliarAdapter(requireActivity(), translationVM.words, translationVM);
        binding.unfamiliarList.setAdapter(adapter);
        binding.unfamiliarList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        translationVM.words.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Words>>() {

            @Override
            public void onChanged(ObservableList<Words> sender) {
            }

            @Override
            public void onItemRangeChanged(ObservableList<Words> sender, int positionStart, int itemCount) {
            }

            @Override
            public void onItemRangeInserted(ObservableList<Words> sender, int positionStart, int itemCount) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeMoved(ObservableList<Words> sender, int fromPosition, int toPosition, int itemCount) {
            }

            @Override
            public void onItemRangeRemoved(ObservableList<Words> sender, int positionStart, int itemCount) {

            }
        });
    }
}