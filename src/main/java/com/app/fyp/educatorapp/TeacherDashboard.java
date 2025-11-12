package com.app.fyp.educatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.app.fyp.educatorapp.AUth.Signin_activity;
import com.app.fyp.educatorapp.Teacher.Acceptedrequest;
import com.app.fyp.educatorapp.Teacher.AddServices;
import com.app.fyp.educatorapp.Teacher.AllRequest;
import com.app.fyp.educatorapp.Teacher.profile;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class TeacherDashboard extends AppCompatActivity {
    DrawerLayout MainDrawerLayout;
    NavigationView MAINNavigationView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        //////////////////navbar////////////////////////////////////////
        mAuth= FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        MainDrawerLayout = (DrawerLayout) findViewById(R.id.mandraw);
        MAINNavigationView = (NavigationView) findViewById(R.id.mainnav);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        MAINNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                MainDrawerLayout.closeDrawers();
                if (item.getItemId() == R.id.log) {
                    mAuth.signOut();
                    Intent intent = new Intent(getApplicationContext(), Signin_activity.class);
                    startActivity(intent);
                    finish();
                }

                return false;
            }
        });
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, MainDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        MainDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }



    public void request(View view) {
        Intent intent = new Intent(getApplicationContext(), AllRequest.class);
        startActivity(intent);
    }
    public void Addserrvice(View view) {
        Intent intent = new Intent(getApplicationContext(), AddServices.class);
        startActivity(intent);
    }

    public void profile(View view) {
        Intent intent = new Intent(getApplicationContext(), profile.class);
        startActivity(intent);
    }
}