package com.app.fyp.educatorapp.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fyp.educatorapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class profile extends AppCompatActivity {
    LinearLayout tutorTypee,qualification,subjectt,area,workimghour,type,tspiinner;
    private EditText etQualification, etSubject, etWorkingHours, etArea, etType;
    private TextView tvQualification, tvSubject, tvWorkingHours, tvArea, tvType, tvEmail, tvPhone,tvgender,tvuttorType;
    private Button btnSave, btnEdit;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private boolean isEditing = true;
    Spinner spinnerTutorType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        etQualification = findViewById(R.id.etQualification);
        etSubject = findViewById(R.id.etSubject);
        etWorkingHours = findViewById(R.id.etWorkingHours);
        etArea = findViewById(R.id.etArea);
        etType = findViewById(R.id.etType);
        tvgender = findViewById(R.id.tvgender);
        tvQualification = findViewById(R.id.tvQualification);
        tvSubject = findViewById(R.id.tvSubject);
        tvWorkingHours = findViewById(R.id.tvWorkingHours);
        tvArea = findViewById(R.id.tvArea);
        tvType = findViewById(R.id.tvType);
        tvuttorType = findViewById(R.id.tutorType);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        btnSave = findViewById(R.id.btnSave);
      //  btnEdit = findViewById(R.id.btnEdit);

        tutorTypee=findViewById(R.id.tutorTypee);
        tspiinner=findViewById(R.id.spiner);
        qualification=findViewById(R.id.qualification);
        subjectt=findViewById(R.id.subjecct);
        area=findViewById(R.id.areaa);
        workimghour=findViewById(R.id.workimghour);
        type=findViewById(R.id.Typee);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        spinnerTutorType = findViewById(R.id.spinnerTutorType);

        // Create an ArrayAdapter using a simple spinner item layout and the array of tutor types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tutor_types_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerTutorType.setAdapter(adapter);
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

            // Fetch user details from Firebase Authentication
            fetchAuthDetails();

            // Fetch additional profile data from Firebase Realtime Database
            fetchData();

            // Toggle between edit and view modes
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEditing) {
                        saveData();
                        switchToViewMode();
                    } else {
                        switchToEditMode();
                    }
                }
            });

            /*btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchToEditMode();
                }
            });*/
        } else {
            Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
        }
    }



    private void fetchAuthDetails() {
        if (currentUser != null) {
            String email = currentUser.getEmail();
            String phoneNumber = currentUser.getPhoneNumber();

            tvEmail.setText(email != null ? email : "Email not available");
           // tvPhone.setText(phoneNumber != null ? phoneNumber : "Phone number not available");

            // Access Realtime Database to get additional user details
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = database.child("users").child(currentUser.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Fetch additional details
                        String gender = dataSnapshot.child("gender").getValue(String.class);
                        String mobile = dataSnapshot.child("mobile").getValue(String.class);

                        // Update UI with additional details
                        tvgender.setText(gender != null ? gender : "Gender not available");
                        tvPhone.setText(mobile != null ? mobile : "Mobile number not available");
                    } else {
                        Log.d("fetchAuthDetails", "No such user in database");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("fetchAuthDetails", "Database error: " + databaseError.getMessage());
                }
            });
        }
    }

    private void fetchData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String qualification = snapshot.child("qualification").getValue(String.class);
                    String subject = snapshot.child("subject").getValue(String.class);
                    String workingHours = snapshot.child("workingHours").getValue(String.class);
                    String area = snapshot.child("area").getValue(String.class);
                    String type = snapshot.child("type").getValue(String.class);
                    String tutortype = snapshot.child("tutortype").getValue(String.class);

                    // Set text values to TextViews
                    tvQualification.setText(qualification);
                    tvSubject.setText(subject);
                    tvWorkingHours.setText(workingHours);
                    tvArea.setText(area);
                    tvType.setText(type);
                    tvuttorType.setText(tutortype);

                    // Set EditTexts with initial values
                    etQualification.setText(qualification);
                    etSubject.setText(subject);
                    etWorkingHours.setText(workingHours);
                    etArea.setText(area);
                    etType.setText(type);

                    // Set initial visibility
                    switchToViewMode();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profile.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveData() {
        String qualification = etQualification.getText().toString().trim();
        String subject = etSubject.getText().toString().trim();
        String workingHours = etWorkingHours.getText().toString().trim();
        String area = etArea.getText().toString().trim();
        String type = etType.getText().toString().trim();
        String tutortype = spinnerTutorType.getSelectedItem().toString();

        databaseReference.child("qualification").setValue(qualification);
        databaseReference.child("subject").setValue(subject);
        databaseReference.child("workingHours").setValue(workingHours);
        databaseReference.child("area").setValue(area);
        databaseReference.child("type").setValue(type);
        databaseReference.child("tutortype").setValue(tutortype);

        Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
    }

    private void switchToEditMode() {
        // Show EditTexts and hide TextViews
        etQualification.setVisibility(View.VISIBLE);
        etSubject.setVisibility(View.VISIBLE);
        etWorkingHours.setVisibility(View.VISIBLE);
        etArea.setVisibility(View.VISIBLE);
        etType.setVisibility(View.VISIBLE);
        tspiinner.setVisibility(View.VISIBLE);
        qualification.setVisibility(View.GONE);
        subjectt.setVisibility(View.GONE);
        workimghour.setVisibility(View.GONE);
        area.setVisibility(View.GONE);
        type.setVisibility(View.GONE);
        btnSave.setText("Save");
        isEditing = true;
    }

    private void switchToViewMode() {
        // Show TextViews and hide EditTexts
        etQualification.setVisibility(View.GONE);
        etSubject.setVisibility(View.GONE);
        etWorkingHours.setVisibility(View.GONE);
        etArea.setVisibility(View.GONE);
        etType.setVisibility(View.GONE);
        tspiinner.setVisibility(View.GONE);
        qualification.setVisibility(View.VISIBLE);
        subjectt.setVisibility(View.VISIBLE);
        workimghour.setVisibility(View.VISIBLE);
        area.setVisibility(View.VISIBLE);
        type.setVisibility(View.VISIBLE);
        tutorTypee.setVisibility(View.VISIBLE);
        btnSave.setText("Edit");
        isEditing = false;
    }
}