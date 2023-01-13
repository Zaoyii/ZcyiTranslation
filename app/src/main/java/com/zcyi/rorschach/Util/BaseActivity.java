package com.zcyi.rorschach.Util;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(Constant.TAG, "onCreate:调用 ");
    }

    @Override
    protected void onStart() {
        Log.e(Constant.TAG, "onStart:调用 ");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.e(Constant.TAG, "onStop:调用 ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e(Constant.TAG, "onDestroy:调用 ");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.e(Constant.TAG, "onResume:调用 ");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e(Constant.TAG, "onPause:调用 ");
        super.onPause();
    }


}
