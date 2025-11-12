package com.app.fyp.educatorapp.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.fyp.educatorapp.Model.RequestObject;
import com.app.fyp.educatorapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class booking extends AppCompatActivity {
    private EditText nameEditText,phoneEditText,addressEditText,time,type;
    private ProgressBar uploadProgressBar;
    private DatabaseReference mRequestsRef;
    private StorageReference storageRef;
    Button submit;
    private FirebaseAuth mAuth;
    String requestId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        mAuth = FirebaseAuth.getInstance();
        Intent i=this.getIntent();
        String key=i.getExtras().getString("key");


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // DatabaseReference myRef = database.getReference("BookingRequest");
        mRequestsRef = FirebaseDatabase.getInstance().getReference().child("BookingRequest");// "data" is the reference node in the database

/////////////////////////////////////////////////////////////////////////////////
        submit=findViewById(R.id.uploadBtn);
        nameEditText=findViewById(R.id.nameEditText);
        phoneEditText=findViewById(R.id.phoneedt);
        addressEditText=findViewById(R.id.addressedt);
        time=findViewById(R.id.ettime);
        type=findViewById(R.id.tyoe);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = mAuth.getCurrentUser().getUid();
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String addrress = addressEditText.getText().toString();
                String datatype = type.getText().toString();
                String tutiontime = time.getText().toString();
                Map<String, Object> data = new HashMap<>();
                data.put("Name", name);
                data.put("phoneNumber", phone);
                data.put("address", addrress);
                data.put("Time", tutiontime);
                data.put("Tyoe", datatype);
                RequestObject requestObject = new RequestObject(userId, key, "Pending", name, phone, addrress,tutiontime,datatype);

                // Push the request to the database
                requestId = mRequestsRef.push().getKey();
                mRequestsRef.child(requestId).setValue(requestObject)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(booking.this, "Service requested!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })

                        .addOnFailureListener(e ->
                                Toast.makeText(booking.this, "Failed to request service!", Toast.LENGTH_SHORT).show());
            }
        });


    }




}