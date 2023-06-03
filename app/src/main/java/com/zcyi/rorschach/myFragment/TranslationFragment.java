package com.zcyi.rorschach.myFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.zcyi.rorschach.Adapter.LabelAdapter;
import com.zcyi.rorschach.R;
import com.zcyi.rorschach.Util.UtilMethod;
import com.zcyi.rorschach.databinding.FragmentTranslationBinding;

@SuppressLint("NotifyDataSetChanged")
public class TranslationFragment extends Fragment {

    FragmentTranslationBinding binding;
    TranslationVM translationVM;

    LabelAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_translation, container, false);
        translationVM = new ViewModelProvider(requireActivity(), new SavedStateViewModelFactory(requireActivity().getApplication(), this)).get(TranslationVM.class);
        binding.setVm(translationVM);
        binding.setLifecycleOwner(this);
        init();
        return binding.getRoot();
    }

    private void init() {

        adapter = new LabelAdapter(getContext(), translationVM.labels);
        binding.mainLabel.setAdapter(adapter);
        binding.mainLabel.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.mainInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //删除输入框的单词清空下方数据
            @Override
            public void afterTextChanged(Editable s) {
                if (binding.mainInput.getText().toString().isEmpty()) {
                    translationVM.webEg.setValue("");
                    translationVM.alert.setValue("");
                    translationVM.UKPhonetic.setValue("");
                    translationVM.USPhonetic.setValue("");
                    translationVM.CPhonetic.setValue("");
                    translationVM.query.setValue("");
                    translationVM.to.setValue("");
                    translationVM.webShape.setValue("");
                    translationVM.webs.setValue("");
                    translationVM.explains.setValue("");
                    translationVM.labels.clear();
                    translationVM.img_src_collection.setValue(null);
                    binding.voicePlay.setVisibility(View.GONE);
                }
            }
        });
        translationVM.labels.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<String>>() {
            @Override
            public void onChanged(ObservableList<String> sender) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<String> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<String> sender, int positionStart, int itemCount) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeMoved(ObservableList<String> sender, int fromPosition, int toPosition, int itemCount) {
            }

            @Override
            public void onItemRangeRemoved(ObservableList<String> sender, int positionStart, int itemCount) {
                adapter.notifyDataSetChanged();
            }
        });

        binding.webShape.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.webShape.getText().toString().isEmpty()) {
                    binding.webShape.setVisibility(View.GONE);
                } else {
                    binding.webShape.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.voicePlay.setOnClickListener(v -> {
            if (!UtilMethod.isFastClick()) {
                translationVM.PLayVoice();
            } else {
                translationVM.ShowToast("不要频繁点击~");
            }
        });

        translationVM.SpeakUrl.observe(getViewLifecycleOwner(), s -> {
            if (s.isEmpty()) {
                binding.voicePlay.setVisibility(View.GONE);
            } else {
                binding.voicePlay.setVisibility(View.VISIBLE);

            }
        });
        translationVM.VoicePlaying.observe(getViewLifecycleOwner(), b -> {
            if (b) {
                binding.voicePlay.setImageResource(R.drawable.voice_playing);
            } else {
                binding.voicePlay.setImageResource(R.drawable.voice_stop);
            }
        });
    }
}