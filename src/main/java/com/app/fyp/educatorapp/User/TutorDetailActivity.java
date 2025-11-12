package com.app.fyp.educatorapp.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.app.fyp.educatorapp.AUth.Signin_activity;
import com.app.fyp.educatorapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TutorDetailActivity extends AppCompatActivity {
    TextView nameTextView,addressTextView,tutiontype,desTextView,mobileTextView,cattext;
    ImageView teacherDetailImageView;
    ImageView rate,phone,whatsapp,msg;
    Button bookng;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_detail);

        initializeWidgets();
        //RECEIVE DATA FROM ITEMSACTIVITY VIA INTENT
        Intent i=this.getIntent();
        String name=i.getExtras().getString("name");
        String address=i.getExtras().getString("address");
        String imageURL=i.getExtras().getString("image");
        String mobile=i.getExtras().getString("mobile");
        String key=i.getExtras().getString("key");
        String category=i.getExtras().getString("category");
        String tutiontypee=i.getExtras().getString("tuitionType");
        String des=i.getExtras().getString("des");
        mAuth = FirebaseAuth.getInstance();


        //SET RECEIVED DATA TO TEXTVIEWS AND IMAGEVIEWS
        nameTextView.setText(name);
        addressTextView.setText(address);
        mobileTextView.setText(mobile);
        desTextView.setText(des);
        tutiontype.setText(tutiontypee);
        cattext.setText(category);
        Picasso.with(this)
                .load(imageURL)
                .placeholder(R.drawable.ic_launcher_background)
                .fit()
                .centerCrop()
                .into(teacherDetailImageView);


        ////////////////////////////////////////////////////////////////////



        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    // User is logged in, proceed to booking activity
                    Intent intent = new Intent(getApplicationContext(), Rateus.class);
                    intent.putExtra("key", key);
                    startActivity(intent);
                } else {
                    // User is not logged in, redirect to login activity
                    Intent intent = new Intent(getApplicationContext(), Signin_activity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
        bookng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the user is logged in
                if (mAuth.getCurrentUser() != null) {
                    // User is logged in, proceed to booking activity
                    Intent intent = new Intent(getApplicationContext(), booking.class);
                    intent.putExtra("key", key);
                    startActivity(intent);
                } else {
                    // User is not logged in, redirect to login activity
                    Intent intent = new Intent(getApplicationContext(), Signin_activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }


    private void initializeWidgets(){
        nameTextView= findViewById(R.id.nameDetailTextView);
        addressTextView= findViewById(R.id.addresDetailTextView);
        tutiontype= findViewById(R.id.datDetailTextView);
        desTextView= findViewById(R.id.priceDetailTextView);
        mobileTextView= findViewById(R.id.MobileDetailTextView);
        teacherDetailImageView=findViewById(R.id.teacherDetailImageView);
        cattext=findViewById(R.id.catDetailTextView);

        rate=findViewById(R.id.rateservice);
        bookng=findViewById(R.id.booking);
    }
    private String getDateToday(){
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String today= dateFormat.format(date);
        return today;
    }
}