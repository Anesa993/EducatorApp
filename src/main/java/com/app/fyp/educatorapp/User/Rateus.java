package com.app.fyp.educatorapp.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.app.fyp.educatorapp.Model.ProfileModel;
import com.app.fyp.educatorapp.Model.Rating;
import com.app.fyp.educatorapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Rateus extends AppCompatActivity {
    Button submit;
    RatingBar ratingBar;
    private FirebaseAuth mAuth;
    String requestId;
    private DatabaseReference mRequestsRef;
    String name,email,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateus);
        ratingBar = findViewById(R.id.ratingBar);
        submit = findViewById(R.id.btnSubmit);
        mAuth = FirebaseAuth.getInstance();
        Intent i=this.getIntent();
        String key=i.getExtras().getString("key");
        String category=i.getExtras().getString("catagory");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = database.getReference().child("users").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ProfileModel userProfile = dataSnapshot.getValue(ProfileModel.class);
                    if (userProfile != null) {
                        name = userProfile.getName();
                        email = userProfile.getEmail();
                        mobile = userProfile.getMobile();

                    }
                } else {
                    // Document does not exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

        mRequestsRef = FirebaseDatabase.getInstance().getReference().child("Rating");// "data" is the reference node in the database


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                Toast.makeText(getApplicationContext(), "Rating: " + rating, Toast.LENGTH_SHORT).show();
                String userId = mAuth.getCurrentUser().getUid();
                String username = name;
                String rate = String.valueOf(rating);
                Map<String, Object> data = new HashMap<>();
                data.put("Username", username);
                data.put("Rating", rate);
                Rating rates = new Rating(userId,rate, key,username);

                // Push the request to the database
                requestId = mRequestsRef.push().getKey();
                mRequestsRef.child(requestId).setValue(rates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Rateus.this, "Rate Add Succesfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })

                        .addOnFailureListener(e ->
                                Toast.makeText(Rateus.this, "Failed to Add Rate!", Toast.LENGTH_SHORT).show());

            }
        });


    }

}