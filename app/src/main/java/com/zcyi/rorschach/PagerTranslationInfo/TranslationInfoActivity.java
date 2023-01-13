package com.zcyi.rorschach.PagerTranslationInfo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zcyi.rorschach.Adapter.LabelAdapter;
import com.zcyi.rorschach.Dao.WordsDao;
import com.zcyi.rorschach.MyEntity.Words;
import com.zcyi.rorschach.R;
import com.zcyi.rorschach.Util.BaseRoomDatabase;
import com.zcyi.rorschach.Util.Constant;
import com.zcyi.rorschach.Util.InstanceDatabase;
import com.zcyi.rorschach.Util.UtilMethod;
import com.zcyi.rorschach.databinding.ActivityTranslationInfoBinding;

import java.util.List;

public class TranslationInfoActivity extends AppCompatActivity {
    ActivityTranslationInfoBinding binding;
    Words words;
    LabelAdapter adapter;
    AlertDialog.Builder unCollectionDialog;

    BaseRoomDatabase baseRoomDatabase;
    WordsDao wordsDao;
    boolean isCollection = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_translation_info);
        setContentView(binding.getRoot());
        UtilMethod.changeStatusBarFrontColor(true, this);
        //intent接收参数
        Intent intent = getIntent();
        String s = intent.getStringExtra("words");
        if (!s.isEmpty()) {
            words = UtilMethod.StringToWords(s);
            if (words != null) {
                init();
            }
        }
    }

    public void init() {
        //数据库操作初始化
        baseRoomDatabase = InstanceDatabase.getInstance(getApplication());
        wordsDao = baseRoomDatabase.getWordsDao();

        String cnPhonetic = words.getCnPhonetic();
        String translation = words.getTranslation();
        String explains = words.getExplains();
        String query = words.getQuery();
        String labels = words.getLabels();
        String shape = words.getShape();
        String usPhonetic = words.getUSPhonetic();
        String ukPhonetic = words.getUKPhonetic();
        String web = words.getWeb();

        if (UtilMethod.checkString(translation))
            binding.toInput.setText(translation);

        if (UtilMethod.checkString(explains))
            binding.mainTranslation.setText(explains);

        if (UtilMethod.checkString(query))
            binding.fromInput.setText(query);

        if (UtilMethod.checkString(cnPhonetic))
            binding.phonetic.setText(cnPhonetic);

        if (UtilMethod.checkString(usPhonetic))
            binding.usPhonetic.setText(usPhonetic);

        if (UtilMethod.checkString(ukPhonetic))
            binding.ukPhonetic.setText(ukPhonetic);

        if (UtilMethod.checkString(shape)) {
            binding.webShape.setText(shape);
            binding.webShape.setVisibility(View.VISIBLE);
        }
        if (UtilMethod.checkString(web)) {
            binding.webTranslation.setText(web);
            binding.webEg.setText(getApplication().getResources().getString(R.string.web_eg));
        }

        if (UtilMethod.checkString(labels)) {
            List<String> strings = UtilMethod.StringToList(labels);
            adapter = new LabelAdapter(this, strings);
            binding.mainLabel.setAdapter(adapter);
            binding.mainLabel.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
        Log.e(Constant.TAG, "TranslationInfoActivity: " + words);
        binding.unCollection.setVisibility(View.VISIBLE);
        binding.unCollection.setOnClickListener(v -> {
            if (isCollection) {
                unCollectionDialog = new AlertDialog.Builder(v.getContext());
                unCollectionDialog.setTitle("从生词表移除").setMessage("确定要移除吗?")
                        .setPositiveButton("确定", (dialog, which) -> {
                            Log.e(Constant.TAG, "要删除的words:" + words);
                            wordsDao.DeleteWords(words);
                            binding.unCollection.setImageResource(R.drawable.ic_uncollection_24);
                            ShowToast("移除成功~");
                            finish();
                            isCollection = false;
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            ShowToast("取消");
                            dialog.cancel();
                        });
                unCollectionDialog.show();
            }
        });
        binding.back.setOnClickListener(v -> finish());
    }

    public void ShowToast(String info) {
        Toast.makeText(getApplication(), info, Toast.LENGTH_SHORT).show();
    }
}