package com.jeuxdevelopers.wakreadmin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.jeuxdevelopers.wakreadmin.databinding.ActivitySplashBinding;
import com.jeuxdevelopers.wakreadmin.utils.MyPreferences;


public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        new Handler(getMainLooper()).postDelayed(() -> {
            if (new MyPreferences(this).getCurrentUser() != null && new MyPreferences(this).getUId() != null) {
                navigateToMainScreen();
            } else {
                startActivity(new Intent(SplashActivity.this, AuthenticationActivity.class));
                finishAffinity();
            }

        }, 1000);
    }


    private void navigateToMainScreen() {
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();

    }
}