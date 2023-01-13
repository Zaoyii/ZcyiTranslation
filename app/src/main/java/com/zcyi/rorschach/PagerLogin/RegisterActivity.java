package com.zcyi.rorschach.PagerLogin;

import static com.zcyi.rorschach.Util.UtilMethod.hideKeyboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.zcyi.rorschach.Dao.UserDao;
import com.zcyi.rorschach.R;
import com.zcyi.rorschach.Util.BaseRoomDatabase;
import com.zcyi.rorschach.Util.InstanceDatabase;
import com.zcyi.rorschach.databinding.ActivityRegisterBinding;
import com.zcyi.rorschach.MyEntity.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    BaseRoomDatabase baseRoomDatabase;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @SuppressLint("SimpleDateFormat")
    private void init() {
        baseRoomDatabase = InstanceDatabase.getInstance(getApplication());
        userDao = baseRoomDatabase.getUserDao();

        binding = DataBindingUtil.setContentView(RegisterActivity.this, R.layout.activity_register);
        binding.back.setOnClickListener(v -> finish());
        binding.root.setOnClickListener(v -> {
            hideKeyboard((Activity) v.getContext());
            View currentFocus = getCurrentFocus();
            if (currentFocus != null) {
                currentFocus.clearFocus();
            }
        });

        binding.register.setOnClickListener(v -> {
            String username = binding.username.getText().toString().trim();
            String password = binding.password.getText().toString().trim();
            String passwordAgain = binding.passwordAgain.getText().toString().trim();
            String email = binding.email.getText().toString().trim();
            String phone = binding.phone.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty() && !passwordAgain.isEmpty() && !email.isEmpty() && !phone.isEmpty()) {
                if (binding.password.getText().toString().equals(binding.passwordAgain.getText().toString())) {
                    if (userDao.checkUsername(username) == null) {
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String data = simpleFormat.format(new Date());
                        userDao.addUser(new User(username, password, email, phone, data));
                        Toast.makeText(getApplication(), "注册成功~返回登录页面登录8", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        binding.alert.setText("账号已存在");
                    }
                } else {
                    binding.alert.setText("两次输入密码不一致");
                }
            } else {
                binding.alert.setText("请填写全部信息~");
            }
        });

    }
}