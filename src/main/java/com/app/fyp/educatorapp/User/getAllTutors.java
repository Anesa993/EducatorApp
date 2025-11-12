package com.app.fyp.educatorapp.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.fyp.educatorapp.Adpater.AlllistRecyclerView;
import com.app.fyp.educatorapp.Model.ServiceModel;
import com.app.fyp.educatorapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class getAllTutors extends AppCompatActivity implements AlllistRecyclerView.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private AlllistRecyclerView listAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<ServiceModel> mservice;
    private List<ServiceModel> filteredService;
    private EditText categoryEditText, tuitionTypeEditText;
    private Button searchButton;

    private static final String TAG = "getAllTutors";
    String catagorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_tutors);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        categoryEditText = findViewById(R.id.categoryEditText);
        tuitionTypeEditText = findViewById(R.id.tuitionTypeEditText);
        searchButton = findViewById(R.id.searchButton);

        mservice = new ArrayList<>();
        filteredService = new ArrayList<>();
        listAdapter = new AlllistRecyclerView(getAllTutors.this, filteredService);
        mRecyclerView.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(getAllTutors.this);

        Intent intent = getIntent();
        catagorie = intent.getStringExtra("cat");
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("service_uploads").child(catagorie);

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mservice.clear();

                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    ServiceModel upload = teacherSnapshot.getValue(ServiceModel.class);
                    if (upload != null) {
                        upload.setKey(teacherSnapshot.getKey());
                        mservice.add(upload);
                        Log.d(TAG, "Added service: " + upload.toString());
                    } else {
                        Log.d(TAG, "Service is null");
                    }
                }
                filterServices();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getAllTutors.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterServices();
            }
        });
    }

    private void filterServices() {
        String categoryFilter = categoryEditText.getText().toString().trim();
        String tuitionTypeFilter = tuitionTypeEditText.getText().toString().trim();

        filteredService.clear();

        for (ServiceModel service : mservice) {
            boolean matchesCategory = categoryFilter.isEmpty() || service.getCategory().toLowerCase().contains(categoryFilter.toLowerCase());
            boolean matchesTuitionType = tuitionTypeFilter.isEmpty() || service.getTuitionType().toLowerCase().contains(tuitionTypeFilter.toLowerCase());

            if (matchesCategory && matchesTuitionType) {
                filteredService.add(service);
            }
        }
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        ServiceModel clickedTeacher = filteredService.get(position);
        String[] teacherData = {
                clickedTeacher.getName(),
                clickedTeacher.getAddress(),
                clickedTeacher.getImageUrl(),
                clickedTeacher.getMobile(),
                clickedTeacher.getKey(),
                clickedTeacher.getCategory(),
                clickedTeacher.getTuitionType(),
                clickedTeacher.getDescription()
        };
        openDetailActivity(teacherData);
    }

    @Override
    public void onShowItemClick(int position) {
        // Implement if needed
    }

    @Override
    public void onDeleteItemClick(int position) {
        // Implement if needed
    }

    @Override
    public void onEditItemClick(int position) {
        // Implement if needed
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    private void openDetailActivity(String[] data) {
        Intent intent = new Intent(this, TutorDetailActivity.class); // Assuming you have a detail activity
        intent.putExtra("name", data[0]);
        intent.putExtra("address", data[1]);
        intent.putExtra("image", data[2]);
        intent.putExtra("mobile", data[3]);
        intent.putExtra("key", data[4]);
        intent.putExtra("category", data[5]);
        intent.putExtra("tuitionType", data[6]);
        intent.putExtra("des", data[7]);
        startActivity(intent);
    }
}
