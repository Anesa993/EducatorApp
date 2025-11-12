package com.app.fyp.educatorapp.Teacher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.fyp.educatorapp.Model.ProfileModel;
import com.app.fyp.educatorapp.Model.RequestObject;
import com.app.fyp.educatorapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AllRequest extends AppCompatActivity {
    private DatabaseReference mRequestsRef;
    private DatabaseReference mUsersRef;
    private LinearLayout requestsLayout;
    private String tutorMobile,tutorname; // To store the tutor's mobile number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_request);
        mRequestsRef = FirebaseDatabase.getInstance().getReference().child("BookingRequest");
        requestsLayout = findViewById(R.id.requestsLayout);

        // Initialize Firebase references
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String tutorId = firebaseAuth.getCurrentUser().getUid();
        mUsersRef = FirebaseDatabase.getInstance().getReference().child("users").child(tutorId);

        // Fetch tutor's profile
        fetchTutorProfile();

        // Fetch requests and populate UI
        fetchRequests();
    }

    private void fetchTutorProfile() {
        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ProfileModel profile = dataSnapshot.getValue(ProfileModel.class);
                    if (profile != null) {
                        tutorMobile = profile.getMobile();
                        tutorname = profile.getName();
                        Toast.makeText(AllRequest.this, "Mobile: " + tutorMobile, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AllRequest.this, "Profile data is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AllRequest.this, "Data snapshot does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AllRequest.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchRequests() {
        mRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestsLayout.removeAllViews(); // Clear existing views

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RequestObject requestObject = snapshot.getValue(RequestObject.class);
                    if (requestObject != null) {
                        addRequestView(requestObject, snapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void addRequestView(RequestObject requestObject, String requestId) {
        View requestView = LayoutInflater.from(this).inflate(R.layout.request_layout, null);

        // Find views in request_layout.xml and set details from requestObject
        TextView nameTextView = requestView.findViewById(R.id.nameTextView);
        TextView phoneTextView = requestView.findViewById(R.id.phoneTextView);
        TextView addressTextView = requestView.findViewById(R.id.addressTextView);
        TextView timeTextView = requestView.findViewById(R.id.timeTextView);
        TextView typeTextView = requestView.findViewById(R.id.desTextView);

        nameTextView.setText("Name: " + requestObject.getUserName());
        phoneTextView.setText("Phone: " + requestObject.getUserPhone());
        addressTextView.setText("Address: " + requestObject.getUserAddress());
        timeTextView.setText("Time: " + requestObject.getUsertime());
        typeTextView.setText("Type: " + requestObject.getUsertype());

        Button rejectButton = requestView.findViewById(R.id.rejectButton);
        Button acceptButton = requestView.findViewById(R.id.acceptButton);

        if (requestObject.getStatus().equals("Pending")) {
            acceptButton.setVisibility(View.VISIBLE);
            rejectButton.setVisibility(View.VISIBLE);
            /*acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRequestsRef.child(requestId).child("status").setValue("Accepted")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (tutorMobile != null) {
                                        // Update the request with the tutor's mobile number
                                        mRequestsRef.child(requestId).child("tutorMobile").setValue(tutorMobile)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(AllRequest.this, "Request accepted and tutor's mobile added!", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(AllRequest.this, "Failed to update request with tutor's mobile!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AllRequest.this, "Failed to accept request!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });*/
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRequestsRef.child(requestId).child("status").setValue("Accepted")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (tutorMobile != null) {
                                        // Update the request with the tutor's mobile number
                                        mRequestsRef.child(requestId).child("tutorMobile").setValue(tutorMobile)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // After successfully adding tutorMobile, add tutorname
                                                        if (tutorname != null) {
                                                            mRequestsRef.child(requestId).child("tutorName").setValue(tutorname)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toast.makeText(AllRequest.this, "Request accepted and tutor's details added!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(AllRequest.this, "Failed to update request with tutor's name!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(AllRequest.this, "Failed to update request with tutor's mobile!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AllRequest.this, "Failed to accept request!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });


            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRequestsRef.child(requestId).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AllRequest.this, "Request rejected and deleted!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AllRequest.this, "Failed to reject request!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        } else {
            // If the request is not pending, hide the buttons
            acceptButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
        }
        requestsLayout.addView(requestView);
    }
}
