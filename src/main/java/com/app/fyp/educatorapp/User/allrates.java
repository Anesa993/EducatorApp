package com.app.fyp.educatorapp.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fyp.educatorapp.AUth.Signin_activity;
import com.app.fyp.educatorapp.Adpater.Allrateadapter;
import com.app.fyp.educatorapp.Model.Rating;
import com.app.fyp.educatorapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class allrates extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference mRequestsRef;
    private List<Rating> rateList;
    private Allrateadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allrates);

        recyclerView = findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rateList = new ArrayList<>();

        // Check if user is logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // User is logged in, continue to fetch ratings
            fetchRatings();
        } else {
            // User is not logged in, redirect to login activity
            Intent loginIntent = new Intent(allrates.this, Signin_activity.class); // Replace with your actual login activity
            startActivity(loginIntent);
            finish();  // Close current activity
        }
    }

    private void fetchRatings() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRequestsRef = FirebaseDatabase.getInstance().getReference().child("Rating");
        mRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rateList.clear(); // Clear the list to avoid duplication
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Rating rating = snapshot.getValue(Rating.class);
                    if (rating != null && rating.getUserId().equals(userId)) {
                        rateList.add(rating);
                    }
                }
                updateAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "Firebase Database Error: " + databaseError.getMessage());
            }
        });
    }

    private void updateAdapter() {
        if (adapter == null) {
            adapter = new Allrateadapter(allrates.this, rateList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
