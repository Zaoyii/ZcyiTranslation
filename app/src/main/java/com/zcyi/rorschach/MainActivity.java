package com.zcyi.rorschach;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.zcyi.rorschach.Util.BaseActivity;
import com.zcyi.rorschach.Util.UtilMethod;
import com.zcyi.rorschach.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {
    private static final int MIN_DELAY_TIME = 2000;  // 两次点击间隔
    private static long lastClickTime;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.home_fragment_container);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.homeBottomNavigation, navController);
        UtilMethod.changeStatusBarFrontColor(true, this);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentClickTime = System.currentTimeMillis();
            if ((currentClickTime - lastClickTime) <= MIN_DELAY_TIME) {
                return super.onKeyUp(keyCode, event);
            } else {
                Toast.makeText(getApplication(), "再按一次返回键退出", Toast.LENGTH_SHORT).show();
            }
            lastClickTime = currentClickTime;
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }
}
