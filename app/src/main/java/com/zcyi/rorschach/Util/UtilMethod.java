package com.zcyi.rorschach.Util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zcyi.rorschach.MyEntity.Words;
import com.zcyi.rorschach.myFragment.TranslationVM;

import java.util.List;

public class UtilMethod {

    private static final int MIN_DELAY_TIME = 500;  // 两次点击间隔
    private static long lastClickTime;

    public static SharedPreferences preferences;
    public final static String TAG = "ZaoYi";

    //设置状态栏字体颜色
    public static void changeStatusBarFrontColor(boolean isBlack, Activity activity) {

        if (isBlack) {
            //设置状态栏黑色字体
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            //恢复状态栏白色字体
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

    }

    //隐藏软键盘
    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //限制短时间内多次点击
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    public static SharedPreferences getPreferences(Application application) {
        if (preferences == null) {
            preferences = application.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        }
        return preferences;
    }

    public static boolean isEnglish(String p) {
        byte[] bytes = p.getBytes();
        int i = bytes.length;//i为字节长度
        int j = p.length();//j为字符长度
        return i == j;
    }

    @BindingAdapter("img_src")
    public static void ImgSrc(ImageView imageView, int res) {
        imageView.setImageResource(res);
    }

    public static String ObjectToString(Object o) {
        return new Gson().toJson(o);
    }

    public static List<String> StringToList(String s) {
        return new Gson().fromJson(s, new TypeToken<List<String>>() {
        }.getType());
    }

    public static Words StringToWords(String s) {
        return new Gson().fromJson(s, new TypeToken<Words>() {
        }.getType());
    }

    public static TranslationVM StringToVm(String s) {
        return new Gson().fromJson(s, new TypeToken<TranslationVM>() {
        }.getType());
    }

    public static boolean checkString(String s) {

        return s != null && (!s.isEmpty());
    }

}
