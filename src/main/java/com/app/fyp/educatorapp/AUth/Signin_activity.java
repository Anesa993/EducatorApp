package com.app.fyp.educatorapp.AUth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fyp.educatorapp.MainActivity;
import com.app.fyp.educatorapp.R;
import com.app.fyp.educatorapp.TeacherDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signin_activity extends AppCompatActivity {

    Button signin, signup;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    EditText email, pass;
    ProgressBar progressbar;
    TextView loadingText, donthaveaccount;
    DatabaseReference myRef, databaseReference, refadmin, refemployee;
    String role = "", id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        runtime_permissions();

        signin = findViewById(R.id.signinBtn_1);
        progressbar = findViewById(R.id.progressbar);
        loadingText = findViewById(R.id.loadingText);
        donthaveaccount = findViewById(R.id.have_an_account);

        progressbar.setVisibility(View.INVISIBLE);
        loadingText.setVisibility(View.INVISIBLE);

        email = findViewById(R.id.email_1);
        pass = findViewById(R.id.pass_1);

        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Get the right drawable
                    Drawable drawable = pass.getCompoundDrawablesRelative()[DRAWABLE_RIGHT];

                    if (drawable != null) {
                        if (event.getRawX() >= (pass.getRight() - drawable.getBounds().width())) {
                            // Toggle password visibility
                            if (pass.getTransformationMethod() instanceof PasswordTransformationMethod) {
                                pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_24, 0);
                            } else {
                                pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                pass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_remove_red_eye_24, 0);
                            }
                            pass.setSelection(pass.length()); // Maintain cursor position
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference().child("users");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    if (user.isEmailVerified()) {
                        id = user.getUid();
                        databaseReference = myRef.child(id);
                        refadmin = FirebaseDatabase.getInstance().getReference().child("Student");
                        refemployee = FirebaseDatabase.getInstance().getReference().child("Educator");

                        // Check user role and redirect accordingly
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("role")) {
                                    role = dataSnapshot.child("role").getValue().toString();
                                    if (role.equals("Student")) {
                                        Intent intent = new Intent(Signin_activity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else if (role.equals("Educator")) {
                                        Intent intent = new Intent(Signin_activity.this, TeacherDashboard.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                        refadmin.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(id)) {
                                    Toast.makeText(Signin_activity.this, "Student exists", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Signin_activity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                        refemployee.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(id)) {
                                    Toast.makeText(Signin_activity.this, "Educator exists", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Signin_activity.this, TeacherDashboard.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                    }
                }
            }
        };


        donthaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signin_activity.this, Signup.class);
                startActivity(intent);
            }
        });

/*        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email_Text = email.getText().toString().trim();
                final String pass_Text = pass.getText().toString().trim();

                if (validateInput(email_Text, pass_Text)) {
                    email.setEnabled(false);
                    pass.setEnabled(false);
                    signin.setEnabled(false);
                    progressbar.setVisibility(View.VISIBLE);
                    loadingText.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email_Text, pass_Text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    if (user.isEmailVerified()) {
                                        id = user.getUid();
                                        databaseReference = myRef.child(id);
                                        refadmin = FirebaseDatabase.getInstance().getReference().child("Student");
                                        refemployee = FirebaseDatabase.getInstance().getReference().child("Educator");

                                        databaseReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild("role")) {
                                                    role = dataSnapshot.child("role").getValue().toString();
                                                    if (role.equals("Student")) {
                                                        Intent intent = new Intent(Signin_activity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else if (role.equals("Educator")) {
                                                        Intent intent = new Intent(Signin_activity.this, TeacherDashboard.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });

                                        refadmin.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild(id)) {
                                                    Toast.makeText(Signin_activity.this, "Student exists", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(Signin_activity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });

                                        refemployee.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild(id)) {
                                                    Toast.makeText(Signin_activity.this, "Educator exists", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(Signin_activity.this, TeacherDashboard.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    } else {
                                        progressbar.setVisibility(View.INVISIBLE);
                                        loadingText.setVisibility(View.INVISIBLE);
                                        signin.setEnabled(true);
                                        email.setEnabled(true);
                                        pass.setEnabled(true);
                                        email.setClickable(true);
                                        pass.setClickable(true);
                                        Toast.makeText(Signin_activity.this, "Please verify your email address", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                progressbar.setVisibility(View.INVISIBLE);
                                loadingText.setVisibility(View.INVISIBLE);
                                signin.setEnabled(true);
                                email.setEnabled(true);
                                pass.setEnabled(true);
                                email.setClickable(true);
                                pass.setClickable(true);
                                Toast.makeText(Signin_activity.this, "Sign In Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });*/
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email_Text = email.getText().toString().trim();
                final String pass_Text = pass.getText().toString().trim();

                if (validateInput(email, pass)){
                    progressbar.setVisibility(View.VISIBLE);
                    loadingText.setVisibility(View.VISIBLE);
                    signin.setEnabled(false);

                    mAuth.signInWithEmailAndPassword(email_Text, pass_Text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                id = mAuth.getCurrentUser().getUid();
                                databaseReference = myRef.child(id);
                                refadmin = FirebaseDatabase.getInstance().getReference().child("Student");
                                refemployee = FirebaseDatabase.getInstance().getReference().child("Educator");

                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild("role")) {
                                            role = dataSnapshot.child("role").getValue().toString();
                                            if (role.equals("Student")) {
                                                Intent intent = new Intent(Signin_activity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else if (role.equals("Educator")) {
                                                Intent intent = new Intent(Signin_activity.this, TeacherDashboard.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle potential errors here
                                    }
                                });

                                refadmin.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(id)) {
                                            Toast.makeText(Signin_activity.this, "Student exists", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Signin_activity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle potential errors here
                                    }
                                });

                                refemployee.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(id)) {
                                            Toast.makeText(Signin_activity.this, "Educator exists", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Signin_activity.this, TeacherDashboard.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle potential errors here
                                    }
                                });
                            } else {
                                // Sign In Error
                                progressbar.setVisibility(View.INVISIBLE);
                                loadingText.setVisibility(View.INVISIBLE);
                                signin.setEnabled(true);
                                Toast.makeText(Signin_activity.this, "Sign In Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean validateInput(EditText emailEditText, EditText passwordEditText) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email cannot be empty");
            emailEditText.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid Email");
            emailEditText.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password cannot be empty");
            passwordEditText.requestFocus();
            return false;
        } else if (password.length() < 8) {
            passwordEditText.setError("Password must be at least 8 characters");
            passwordEditText.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, "Permission not Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "version is greater than 23", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }

   /* public void Guest(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }*/
}
