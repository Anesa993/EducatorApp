package com.app.fyp.educatorapp.Teacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.fyp.educatorapp.Model.ServiceModel;
import com.app.fyp.educatorapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddServices extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    Button chooseImageBtn;
    Button uploadBtn;
    EditText nameEditText, desedt, addressedt, mobilenoedt;
    ImageView chosenImageView;
    ProgressBar uploadProgressBar;
    private DatabaseReference databaseRef;
    private StorageReference storageRef;
    private StorageTask mUploadTask;
    private Uri imageUri;

    // Spinners for Service Category and Tuition Type
    private Spinner serviceCategorySpinner;
    private Spinner tuitionTypeSpinner;

    private String selectedCategory = "Tutor"; // Set default value here
    private String selectedTuitionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);

        chooseImageBtn = findViewById(R.id.button_choose_image);
        uploadBtn = findViewById(R.id.uploadBtn);
        nameEditText = findViewById(R.id.nameEditText);
        desedt = findViewById(R.id.desText);
        addressedt = findViewById(R.id.adress);
        mobilenoedt = findViewById(R.id.phoneno);
        chosenImageView = findViewById(R.id.chosenImageView);
        uploadProgressBar = findViewById(R.id.progress_bar);

        // Initialize Spinners
        serviceCategorySpinner = findViewById(R.id.serviceCategorySpinner);
        tuitionTypeSpinner = findViewById(R.id.tuitionTypeSpinner);

        // Setup Spinner for Service Categories
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.service_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceCategorySpinner.setAdapter(categoryAdapter);
        serviceCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Set default category to "Tutor" if nothing is selected
                selectedCategory = "Tutor";
            }
        });

        // Setup Spinner for Tuition Types
        ArrayAdapter<CharSequence> tuitionAdapter = ArrayAdapter.createFromResource(this,
                R.array.tuition_types, android.R.layout.simple_spinner_item);
        tuitionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tuitionTypeSpinner.setAdapter(tuitionAdapter);
        tuitionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTuitionType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTuitionType = null;
            }
        });

        storageRef = FirebaseStorage.getInstance().getReference("service_uploads");

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getApplicationContext(), "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImage();
                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                chosenImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            String name = nameEditText.getText().toString().trim();
            String des = desedt.getText().toString().trim();
            String address = addressedt.getText().toString().trim();
            String mobileNo = mobilenoedt.getText().toString().trim();

            if (!name.isEmpty() && selectedCategory != null && selectedTuitionType != null) {
                uploadImageToStorage(imageUri, name, des, address, mobileNo, selectedCategory, selectedTuitionType);
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToStorage(Uri imageUri, final String name, final String des, final String address, final String mobileNo, final String category, final String tuitionType) {
        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    uploadProgressBar.setVisibility(View.VISIBLE);
                    uploadProgressBar.setIndeterminate(false);
                    uploadProgressBar.setProgress(0);
                }
            }, 500);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();

            final String imageFileName = System.currentTimeMillis() + ".jpg";
            final StorageReference imageRef = storageRef.child(imageFileName);

            UploadTask uploadTask = imageRef.putBytes(imageData);

            uploadTask.addOnCompleteListener(this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String downloadUrl = downloadUri.toString();
                                    saveImageAndTitleToDatabase(downloadUrl, name, des, address, mobileNo, category, tuitionType);
                                } else {
                                    Toast.makeText(AddServices.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(AddServices.this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveImageAndTitleToDatabase(String downloadUrl, String name, String des, String address, String mobile, String category, String tuitionType) {
        databaseRef = FirebaseDatabase.getInstance().getReference("service_uploads").child(selectedCategory);
        String imageKey = databaseRef.push().getKey();
        ServiceModel imageData = new ServiceModel(downloadUrl, name, des, address, mobile, category, tuitionType);
        databaseRef.child(imageKey).setValue(imageData);

        chosenImageView.setImageResource(android.R.color.transparent);
        nameEditText.setText("");
        desedt.setText("");
        addressedt.setText("");
        mobilenoedt.setText("");
        imageUri = null;
        uploadProgressBar.setVisibility(View.INVISIBLE);
        onBackPressed();
        Toast.makeText(this, "Image uploaded and data saved successfully.", Toast.LENGTH_SHORT).show();
    }
}
