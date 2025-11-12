package com.app.fyp.educatorapp.Teacher;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fyp.educatorapp.Adpater.AcceptedRequestsAdapter;
import com.app.fyp.educatorapp.Model.RequestObject;
import com.app.fyp.educatorapp.Model.RequestObjectAccept;
import com.app.fyp.educatorapp.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Acceptedrequest extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference mRequestsRef;
    private List<RequestObjectAccept> acceptedRequestsList;
    private AcceptedRequestsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptedrequest);  // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.acceptedRequestsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        acceptedRequestsList = new ArrayList<>();
        // adapter = new AcceptedRequestsAdapter(this, acceptedRequestsList);
        //  recyclerView.setAdapter(adapter);

        // Firebase database reference for accepted requests of the user

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRequestsRef = FirebaseDatabase.getInstance().getReference().child("BookingRequest");
        mRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // acceptedRequestsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RequestObjectAccept request = snapshot.getValue(RequestObjectAccept.class);
                    if (request != null && request.getUserId().equals(userId) && request.getStatus().equals("Accepted")) {
                        acceptedRequestsList.add(request);
                    }
                }
                updateAdapter();
                // Now, you have filtered the accepted requests locally
                // Update your UI or perform operations with acceptedRequestsList
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event
                Log.e("TAG", "Firebase Database Error: " + databaseError.getMessage());

            }
        });

    }

    private void updateAdapter() {
        if (adapter == null) {
            adapter = new AcceptedRequestsAdapter(Acceptedrequest.this, acceptedRequestsList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}