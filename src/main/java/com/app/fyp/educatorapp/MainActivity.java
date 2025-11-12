package com.app.fyp.educatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;

import com.app.fyp.educatorapp.AUth.Signin_activity;
import com.app.fyp.educatorapp.Teacher.Acceptedrequest;
import com.app.fyp.educatorapp.Teacher.profile;
import com.app.fyp.educatorapp.User.booking;
import com.app.fyp.educatorapp.User.getAllTutors;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    DrawerLayout MainDrawerLayout;
    NavigationView MAINNavigationView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                if (item.getItemId() == R.id.profilee) {
                    if (mAuth.getCurrentUser() != null) {
                        // User is logged in, proceed to booking activity
                        Intent intent = new Intent(getApplicationContext(), profile.class);
                        startActivity(intent);
                    } else {
                        // User is not logged in, redirect to login activity
                        Intent intent = new Intent(getApplicationContext(), Signin_activity.class);
                        startActivity(intent);
                        finish();
                    }

                } else if (item.getItemId() == R.id.requesstt) {
                    if (mAuth.getCurrentUser() != null) {
                        // User is logged in, proceed to booking activity
                        Intent intent = new Intent(getApplicationContext(), Acceptedrequest.class);
                        startActivity(intent);
                    } else {
                        // User is not logged in, redirect to login activity
                        Intent intent = new Intent(getApplicationContext(), Signin_activity.class);
                        startActivity(intent);
                        finish();
                    }
                }else if (item.getItemId() == R.id.log) {
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

    public void islamic(View view) {
        Intent intent = new Intent(getApplicationContext(), getAllTutors.class);
        intent.putExtra("cat","Quran Teacher");
        startActivity(intent);
    }

    public void cooking(View view) {
        Intent intent = new Intent(getApplicationContext(), getAllTutors.class);
        intent.putExtra("cat","Cooking Teacher");
        startActivity(intent);
    }
    public void tutor(View view) {
        Intent intent = new Intent(getApplicationContext(), getAllTutors.class);
        intent.putExtra("cat","Tutor");
        startActivity(intent);
    }

    public void developer(View view) {
        Intent intent = new Intent(getApplicationContext(), getAllTutors.class);
        intent.putExtra("cat","Skills developers");
        startActivity(intent);
    }

    public void special(View view) {
        Intent intent = new Intent(getApplicationContext(), getAllTutors.class);
        intent.putExtra("cat","Special needs Educator");
        startActivity(intent);
    }

    public void language(View view) {
        Intent intent = new Intent(getApplicationContext(), getAllTutors.class);
        intent.putExtra("cat","Language instructors");
        startActivity(intent);
    }
}