package com.app.fyp.educatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.fyp.educatorapp.AUth.Signin_activity;

public class OptionScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_screen);
    }

    public void student(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void educator(View view) {
        Intent intent = new Intent(getApplicationContext(), Signin_activity.class);
        startActivity(intent);
        finish();
    }
}