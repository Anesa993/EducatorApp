package com.app.fyp.educatorapp.AUth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.fyp.educatorapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    EditText nametext, emailtext, passwardtext, phonetext;
    Spinner genderSpinner;
    String namestr, mobilestr, emailStr, passStr, role, strgender;
    Button regist;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    RadioGroup radioGroup;
    RadioButton roleradioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailtext = findViewById(R.id.email);
        phonetext = findViewById(R.id.mobileNo);
        nametext = findViewById(R.id.name);
        passwardtext = findViewById(R.id.pass);
        genderSpinner = findViewById(R.id.genderSpinner);
        regist = findViewById(R.id.signupBtn);
        radioGroup = findViewById(R.id.radioGroup);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        setupGenderSpinner();

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                roleradioButton = findViewById(selectedId);

                if (selectedId == -1) {
                    Toast.makeText(Signup.this, "Please select a role", Toast.LENGTH_SHORT).show();
                    return;
                }

                namestr = nametext.getText().toString().trim();
                mobilestr = phonetext.getText().toString().trim();
                emailStr = emailtext.getText().toString().trim();
                strgender = genderSpinner.getSelectedItem().toString();
                passStr = passwardtext.getText().toString().trim();
                role = roleradioButton.getText().toString();

                if (TextUtils.isEmpty(namestr) || TextUtils.isEmpty(mobilestr) || TextUtils.isEmpty(emailStr) ||
                        TextUtils.isEmpty(passStr) || strgender.equals("Select Gender")) {
                    Toast.makeText(Signup.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                // Validate mobile number
                if (mobilestr.length() != 11) {
                    Toast.makeText(Signup.this, "Mobile number must be 11 digits long", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                // Validate email address
                if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                    Toast.makeText(Signup.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                // Validate password length
                if (passStr.length() < 8) {
                    Toast.makeText(Signup.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DatabaseReference current = databaseReference.child(user.getUid());
                                            current.child("name").setValue(namestr);
                                            current.child("email").setValue(emailStr);
                                            current.child("mobile").setValue(mobilestr);
                                            current.child("role").setValue(role);
                                            current.child("gender").setValue(strgender);
                                            current.child("id").setValue(user.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Intent intent = new Intent(Signup.this, Signin_activity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(Signup.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(Signup.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });

    }

    private void setupGenderSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        genderSpinner.setAdapter(adapter);
    }
}
