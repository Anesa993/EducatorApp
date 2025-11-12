package com.app.fyp.educatorapp.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.app.fyp.educatorapp.AUth.Signin_activity;
import com.app.fyp.educatorapp.OptionScreen;
import com.app.fyp.educatorapp.R;



public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), OptionScreen.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}