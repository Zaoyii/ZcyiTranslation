package com.zcyi.rorschach.myFragment;

import static com.zcyi.rorschach.Util.UtilMethod.isEnglish;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.zcyi.rorschach.Dao.WordsDao;
import com.zcyi.rorschach.Mapper.BaseRequest;
import com.zcyi.rorschach.R;
import com.zcyi.rorschach.Util.BaseRoomDatabase;
import com.zcyi.rorschach.Util.Constant;
import com.zcyi.rorschach.Util.InstanceDatabase;
import com.zcyi.rorschach.Util.TranslationUtil;
import com.zcyi.rorschach.Util.UtilMethod;
import com.zcyi.rorschach.PagerLogin.LoginActivity;
import com.zcyi.rorschach.MyEntity.Words;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TranslationVM extends AndroidViewModel {

    MutableLiveData<String> inputInfo; //输入框
    MutableLiveData<String> alert; //提示
    MutableLiveData<String> CPhonetic; //拼音
    MutableLiveData<String> USPhonetic;//美式
    MutableLiveData<String> UKPhonetic; //英式
    MutableLiveData<String> to; //翻译后
    MutableLiveData<String> query;  //输入的参数
    MutableLiveData<String> webShape; //单词形式
    MutableLiveData<String> webEg; //小标题
    MutableLiveData<String> explains; //主要翻译
    MutableLiveData<String> webs; //网络
    MutableLiveData<Integer> img_src_lan;//切换语言图片
    MutableLiveData<Integer> img_src_collection;//切换语言图片
    MutableLiveData<String> SpeakUrl;//音频路径
    MutableLiveData<Boolean> VoicePlaying;//是否正在播放音频
    ObservableArrayList<String> labels;//标签
    ObservableArrayList<Words> words; //生词表数据
    MutableLiveData<Integer> wordsCount;//生词数量
    BaseRoomDatabase baseRoomDatabase;//数据库操作需要的工具类
    WordsDao wordsDao;
    static boolean isCollection;//是否为生词
    Retrofit retrofit;//网络请求
    BaseRequest baseRequest;
    SharedPreferences preferences;//存储简单数据的工具
    SharedPreferences.Editor editor;
    boolean flag;//true 英译汉  false 汉译英
    AlertDialog.Builder unCollectionDialog;//提示框
    AlertDialog.Builder CollectionDialog;
    int currentUserId;//当前登录用户的id，存储在SharedPreferences中

    public MutableLiveData<String> getSpeakUrl() {
        return SpeakUrl;
    }

    public MutableLiveData<Boolean> getVoicePlaying() {
        return VoicePlaying;
    }

    public MutableLiveData<Integer> getWordsCount() {
        return wordsCount;
    }

    public MutableLiveData<Integer> getImg_src_lan() {
        return img_src_lan;
    }

    public MutableLiveData<Integer> getImg_src_collection() {
        return img_src_collection;
    }

    public MutableLiveData<String> getTo() {
        return to;
    }

    public MutableLiveData<String> getQuery() {
        return query;
    }

    public MutableLiveData<String> getWebShape() {
        return webShape;
    }

    public MutableLiveData<String> getWebEg() {
        return webEg;
    }

    public MutableLiveData<String> getExplains() {
        return explains;
    }

    public MutableLiveData<String> getWebs() {
        return webs;
    }

    public MutableLiveData<String> getCPhonetic() {
        return CPhonetic;
    }

    public MutableLiveData<String> getUKPhonetic() {
        return UKPhonetic;
    }

    public MutableLiveData<String> getInputInfo() {
        return inputInfo;
    }

    public MutableLiveData<String> getUSPhonetic() {
        return USPhonetic;
    }

    public MutableLiveData<String> getAlert() {
        return alert;
    }

    public void setAlert(MutableLiveData<String> alert) {
        this.alert = alert;
    }

    public void setInputInfo(MutableLiveData<String> inputInfo) {
        this.inputInfo = inputInfo;
    }

    public void setImg_src_lan(MutableLiveData<Integer> img_src_lan) {
        this.img_src_lan = img_src_lan;
    }
    //构造方法，给变量初始化或赋初值
    public TranslationVM(@NonNull Application application) {
        super(application);

        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Constant.YouDaoUrl).build();
        baseRequest = retrofit.create(BaseRequest.class);
        //主页
        flag = true;
        explains = new MutableLiveData<>();
        webs = new MutableLiveData<>();
        labels = new ObservableArrayList<>();
        inputInfo = new MutableLiveData<>();
        alert = new MutableLiveData<>();
        CPhonetic = new MutableLiveData<>();
        USPhonetic = new MutableLiveData<>();
        UKPhonetic = new MutableLiveData<>();
        to = new MutableLiveData<>();
        query = new MutableLiveData<>();
        webShape = new MutableLiveData<>();
        webShape.setValue("");
        webEg = new MutableLiveData<>();
        img_src_lan = new MutableLiveData<>();
        img_src_lan.setValue(R.drawable.ic_arrow_right_24);
        img_src_collection = new MutableLiveData<>();
        SpeakUrl = new MutableLiveData<>();
        VoicePlaying = new MutableLiveData<>();
        baseRoomDatabase = InstanceDatabase.getInstance(getApplication());
        wordsDao = baseRoomDatabase.getWordsDao();
        preferences = UtilMethod.getPreferences(getApplication());
        editor = preferences.edit();
        currentUserId = preferences.getInt(Constant.currentId, -1);

        //生词页数据
        words = new ObservableArrayList<>();
        wordsCount = new MutableLiveData<>();
        selectAllWordsByUserId();
    }
    //关闭软键盘
    public void hideKeyBoard(View v) {
        UtilMethod.hideKeyboard((Activity) v.getContext());
        View currentFocus = ((Activity) v.getContext()).getCurrentFocus();

        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
    }
    //发送翻译请求
    public void translation() {
        if (!UtilMethod.isFastClick()) {
            String text = inputInfo.getValue();
            if (text == null || text.trim().isEmpty()) {
                alert.setValue("请输入正确的内容~");
                return;
            } else {
                alert.setValue("");
            }
            String toGO;
            if (!flag) {
                toGO = "en";
                if (isEnglish(text.trim())) {
                    alert.setValue("请输入汉字~");
                    return;
                }
            } else {
                toGO = "zh-CHS";
                if (!isEnglish(text.trim())) {
                    alert.setValue("请输入单词~");
                    return;
                }
            }
            if (flag) {
                Words words = wordsDao.selectWordByQueryUserId(text.trim(), currentUserId);
                Log.e(Constant.TAG, "本地加载-------------------------------------" + words);
                if (words != null) {
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
                        this.to.setValue(translation);

                    if (UtilMethod.checkString(explains))
                        this.explains.setValue(explains);

                    if (UtilMethod.checkString(query))
                        this.query.setValue(query);

                    if (UtilMethod.checkString(cnPhonetic))
                        this.CPhonetic.setValue(cnPhonetic);

                    if (UtilMethod.checkString(shape))
                        this.webShape.setValue(shape);

                    if (UtilMethod.checkString(usPhonetic))
                        this.USPhonetic.setValue(usPhonetic);

                    if (UtilMethod.checkString(ukPhonetic))
                        this.UKPhonetic.setValue(ukPhonetic);

                    if (UtilMethod.checkString(web)) {
                        this.webs.setValue(web);
                        webEg.setValue(getApplication().getResources().getString(R.string.web_eg));
                    }

                    if (UtilMethod.checkString(labels)) {
                        List<String> strings = UtilMethod.StringToList(labels);
                        this.labels.clear();
                        this.labels.addAll(strings);
                    }
                    this.isCollection = true;
                    img_src_collection.setValue(R.drawable.ic_collection_24);
                    return;
                }
            }
            Map<String, String> info = TranslationUtil.getInfo(text.trim(), "auto", toGO);
            Log.e(Constant.TAG, info.toString());
            Call<ResponseBody> translation = baseRequest.getTranslation(
                    info.get("q"),
                    info.get("from"),
                    info.get("to"),
                    info.get("salt"),
                    info.get("sign"),
                    info.get("curtime"),
                    info.get("signType"),
                    info.get("appKey"));

            translation.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    assert response.body() != null;
                    JSONObject jsonObject = null;
                    String isWord = "";
                    JSONObject basic = null;
                    boolean notBasic = false;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        Log.e(Constant.TAG, jsonObject.toString());
                        isWord = jsonObject.getString("errorCode");
                        basic = jsonObject.getJSONObject("basic");
                        notBasic = true;
                    } catch (Exception e) {
                        Log.e(Constant.TAG, "相应内容有误");
                        e.printStackTrace();
                    }

                    if (isWord.equals("0")) {
                        int i;
                        //上半部分的信息
                        String usPhonetic;
                        String ukPhonetic;
                        String Phonetic;
                        Log.e(Constant.TAG, jsonObject.toString());
                        //-------------------------------标签列 start
                        try {
                            if (flag && notBasic) {
                                JSONArray exam_type = basic.getJSONArray("exam_type");
                                if (exam_type.length() > 0) {
                                    labels.clear();
                                    for (i = 0; i < exam_type.length(); i++) {
                                        labels.add(exam_type.getString(i));
                                    }
                                    Log.e(Constant.TAG, labels.toString());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //-------------------------------标签列 end

                        //-------------------------------翻译 start
                        String translation;
                        try {
                            translation = jsonObject.getJSONArray("translation").getString(0);
                            String query = jsonObject.getString("query");
                            if (!translation.isEmpty()) {
                                to.setValue(translation);
                            }
                            Log.e(Constant.TAG, translation);
                            //q
                            if (!query.isEmpty()) {
                                TranslationVM.this.query.setValue(query);
                            }
                            Log.e(Constant.TAG, query);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //-------------------------------翻译 end

                        //-------------------------------发音 start
                        try {
                            if (notBasic) {
                                if (!flag) {
                                    Phonetic = basic.getString("phonetic");
                                    if (!Phonetic.isEmpty()) {
                                        CPhonetic.setValue("拼音:" + Phonetic);
                                    }
                                } else {
                                    usPhonetic = basic.getString("us-phonetic");
                                    ukPhonetic = basic.getString("uk-phonetic");
                                    Log.e(Constant.TAG, usPhonetic);
                                    Log.e(Constant.TAG, ukPhonetic);

                                    if (!usPhonetic.isEmpty()) {
                                        USPhonetic.setValue("美式发音:" + usPhonetic);
                                    }
                                    if (!ukPhonetic.isEmpty()) {
                                        UKPhonetic.setValue("英式发音:" + ukPhonetic);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //-------------------------------发音 end

                        //-------------------------------主要翻译内容 start
                        try {
                            if (notBasic) {
                                JSONArray explains = basic.getJSONArray("explains");

                                StringBuilder stringBuilder = new StringBuilder();
                                if (explains.length() > 0) {
                                    for (i = 0; i < explains.length(); i++) {
                                        stringBuilder.append(explains.getString(i));
                                        if (i != explains.length() - 1)
                                            stringBuilder.append("\n");
                                    }
                                    Log.e(Constant.TAG, stringBuilder.toString());
                                    TranslationVM.this.explains.setValue(stringBuilder.toString());
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //-------------------------------主要翻译内容 end

                        //-------------------------------单词的多种形式 start
                        try {
                            if (flag && notBasic) {
                                JSONArray wfs;

                                wfs = basic.getJSONArray("wfs");

                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("[");
                                Log.e(Constant.TAG, wfs.toString());
                                for (i = 0; i < wfs.length(); i++) {
                                    JSONObject wf = wfs.getJSONObject(i).getJSONObject("wf");
                                    String name = wf.getString("name");
                                    String value = wf.getString("value");
                                    stringBuilder.append(name).append(":").append(value).append(" ");
                                }
                                stringBuilder.append("]");
                                webShape.setValue(stringBuilder.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //-------------------------------单词的多种形式 end

                        //-------------------------------网络释义 start
                        try {
                            String web;

                            web = jsonObject.getJSONArray("web").toString();

                            Log.e(Constant.TAG, web);
                            JSONArray web1 = jsonObject.getJSONArray("web");
                            if (web1.length() > 0) {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (i = 0; i < web1.length(); i++) {
                                    stringBuilder.append("[")
                                            .append(web1.getJSONObject(i).getString("key"))
                                            .append("]\n")
                                            .append(web1.getJSONObject(i).getJSONArray("value"))
                                            .append("\n\n");
                                }
                                webs.setValue(stringBuilder.toString());
                                webEg.setValue(getApplication().getResources().getString(R.string.web_eg));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //-------------------------------网络释义 end

                        //-------------------------------显示收藏按钮 end
                        isCollection = false;
                        img_src_collection.setValue(R.drawable.ic_uncollection_24);
                        System.out.println("----------------------------");
                    } else {
                        alert.setValue("请输入正确的单词~");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                }
            });

        } else {
            ShowToast("不要频繁点击哦~");
        }
    }
    //通用显示提示
    public void ShowToast(String info) {
        Toast.makeText(getApplication(), info, Toast.LENGTH_SHORT).show();
    }
    //改变汉英翻译模式
    public void ChangeState(View v) {
        if (!UtilMethod.isFastClick()) {
            flag = !flag;
            if (flag) {
                img_src_lan.setValue(R.drawable.ic_arrow_right_24);
                Toast.makeText(v.getContext(), "切换成功,目前是英译汉模式", Toast.LENGTH_SHORT).show();
            } else {
                img_src_lan.setValue(R.drawable.ic_arrow_left_24);
                Toast.makeText(v.getContext(), "切换成功,目前是汉译英模式", Toast.LENGTH_SHORT).show();
            }
        } else {
            ShowToast("不要频繁点击哦~");
        }
    }
    //添加or取消生词
    public void Collection(View v) {
        if (!UtilMethod.isFastClick()) {
            if (query.getValue() != null && !query.getValue().isEmpty()) {
                if (!isCollection) {
                    //获取当前用户id
                    Words words = new Words();
                    //获取要存储的基本信息
                    String translation = this.to.getValue();
                    String query = this.query.getValue();
                    String explains = this.explains.getValue();
                    String shape = this.webShape.getValue();
                    String web = this.webs.getValue();
                    String CnPhonetic = this.CPhonetic.getValue();
                    String USPhonetic = this.USPhonetic.getValue();
                    String UKPhonetic = this.UKPhonetic.getValue();

                    if (currentUserId != -1) {
                        words.setUserId(currentUserId);
                    } else {
                        Activity context = (Activity) v.getContext();
                        ShowToast("用户信息保存有误请重新登录!");
                        editor.putInt(Constant.isLogin, 0);
                        editor.commit();
                        context.startActivity(new Intent(context, LoginActivity.class));
                        context.finish();
                    }
                    if (UtilMethod.checkString(translation)) {
                        words.setTranslation(translation);
                    }
                    if (UtilMethod.checkString(query)) {
                        words.setQuery(query);
                    }
                    if (UtilMethod.checkString(explains)) {
                        words.setExplains(explains);
                    }
                    if (UtilMethod.checkString(shape)) {
                        words.setShape(shape);
                    }
                    if (UtilMethod.checkString(web)) {
                        words.setWeb(web);
                    }
                    if (UtilMethod.checkString(CnPhonetic)) {
                        words.setCnPhonetic(CnPhonetic);
                    }
                    if (UtilMethod.checkString(USPhonetic)) {
                        words.setUSPhonetic(USPhonetic);
                    }
                    if (UtilMethod.checkString(UKPhonetic)) {
                        words.setUKPhonetic(UKPhonetic);
                    }
                    if (labels.size() > 0) {
                        words.setLabels(UtilMethod.ObjectToString(labels));

                    }
                    CollectionDialog = new AlertDialog.Builder(v.getContext());
                    CollectionDialog.setTitle("添加到生词表").setMessage("确定要添加吗?")
                            .setPositiveButton("确定", (dialog, which) -> {
                                wordsDao.insertWords(words);
                                img_src_collection.setValue(R.drawable.ic_collection_24);
                                System.out.println("---------------set1");
                                isCollection = true;
                                ShowToast("添加到生词表成功~");
                                selectAllWordsByUserId();
                            })
                            .setNegativeButton("取消", (dialog, which) -> {
                                ShowToast("取消");
                                dialog.cancel();
                            });
                    CollectionDialog.show();
                } else {
                    Words words = wordsDao.selectWordByQueryUserId(query.getValue(), currentUserId);
                    if (words != null) {
                        unCollectionDialog = new AlertDialog.Builder(v.getContext());
                        unCollectionDialog.setTitle("从生词表移除").setMessage("确定要移除吗?")
                                .setPositiveButton("确定", (dialog, which) -> {
                                    Log.e(Constant.TAG, "要删除的words:" + words);
                                    int i = wordsDao.DeleteWords(words);
                                    Log.e(Constant.TAG, "--------------------------------删除语句返回值：" + i);
                                    isCollection = false;
                                    this.img_src_collection.setValue(R.drawable.ic_uncollection_24);
                                    System.out.println("---------------set5");
                                    ShowToast("移除成功~");
                                    selectAllWordsByUserId();
                                })
                                .setNegativeButton("取消", (dialog, which) -> {
                                    ShowToast("取消");
                                    dialog.cancel();
                                });
                        unCollectionDialog.show();
                    } else {
                        isCollection = false;
                        img_src_collection.setValue(R.drawable.ic_uncollection_24);
                        ShowToast("该单词不在生词表中~");
                    }
                }
            }
        } else {
            if (img_src_collection.getValue() != null) {
                ShowToast("不要频繁点击~");
            }
        }
    }

    //播放单词音频
    public void PLayVoice() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(SpeakUrl.getValue());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                if (!Boolean.TRUE.equals(VoicePlaying.getValue())) {
                    VoicePlaying.setValue(true);
                    Log.e("zcyi", "音频文件已准备");
                    Log.e("zcyi", "音频文件时长:" + mediaPlayer.getDuration() / 1000);
                    mediaPlayer.start();
                }

            });
            mediaPlayer.setOnCompletionListener(mp -> {
                VoicePlaying.setValue(false);

                mediaPlayer.release();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //查询当前用户id的所有生词
    public void selectAllWordsByUserId() {
        if (currentUserId != -1) {
            List<Words> selectWords = wordsDao.selectAllWordByUserId(currentUserId);
            this.words.clear();
            words.addAll(selectWords);
            wordsCount.setValue(this.words.size());
            Log.e(Constant.TAG, "更新后:" + this.words);
        }

    }

}
