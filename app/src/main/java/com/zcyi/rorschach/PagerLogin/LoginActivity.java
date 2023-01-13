package com.zcyi.rorschach.PagerLogin;

import static com.zcyi.rorschach.Util.UtilMethod.hideKeyboard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.zcyi.rorschach.Dao.UserDao;
import com.zcyi.rorschach.MainActivity;
import com.zcyi.rorschach.R;
import com.zcyi.rorschach.Util.BaseActivity;
import com.zcyi.rorschach.Util.BaseRoomDatabase;
import com.zcyi.rorschach.Util.InstanceDatabase;
import com.zcyi.rorschach.Util.UtilMethod;
import com.zcyi.rorschach.databinding.ActivityLoginBinding;
import com.zcyi.rorschach.MyEntity.User;

public class LoginActivity extends BaseActivity {

    public ActivityLoginBinding binding;
    BaseRoomDatabase baseRoomDatabase;
    UserDao userDao;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = UtilMethod.getPreferences(getApplication());
        editor = preferences.edit();
        if (preferences.getInt("isLogin", 0) == 1) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        initView();
    }
    private void initView() {
        baseRoomDatabase = InstanceDatabase.getInstance(getApplication());
        userDao = baseRoomDatabase.getUserDao();

        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        UtilMethod.changeStatusBarFrontColor(true, this);
        //点击键盘外区域取消焦点并关闭软键盘
        binding.root.setOnClickListener(v -> {
            hideKeyboard((Activity) v.getContext());
            View currentFocus = getCurrentFocus();
            if (currentFocus != null) {
                currentFocus.clearFocus();
            }
        });
        binding.register.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
        binding.login.setOnClickListener(v -> {
            String username = binding.username.getText().toString().trim();
            String password = binding.password.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                User login = userDao.login(username, password);
                if (login != null) {
                    Toast.makeText(getApplication(), "登录成功", Toast.LENGTH_SHORT).show();
                    editor.putInt("isLogin", 1);
                    editor.putInt("currentId", login.getUserId());
                    editor.commit();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    binding.alert.setText("账号或密码错误");
                }
            }else {
                binding.alert.setText("账号或密码不能为空");
            }

        });
    }
}