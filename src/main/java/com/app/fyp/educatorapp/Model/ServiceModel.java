package com.app.fyp.educatorapp.Model;

import com.google.firebase.database.Exclude;

public class ServiceModel {
    private String imageUrl;
    private String name;
    private String description;
    private String address;
    private String mobile;
    private String category;
    private String tuitionType;
    private String key;
    private int position;

    // Constructor
    public ServiceModel(String imageUrl, String name, String description, String address, String mobile, String category, String tuitionType) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.address = address;
        this.mobile = mobile;
        this.category = category;
        this.tuitionType = tuitionType;
    }

    public ServiceModel(int position){
        this.position = position;
    }

    // Default constructor required for Firebase
    public ServiceModel() {
        // Default constructor required for calls to DataSnapshot.getValue(ServiceModel.class)
    }

    // Getters
    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCategory() {
        return category;
    }

    public String getTuitionType() {
        return tuitionType;
    }

    // Setters
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTuitionType(String tuitionType) {
        this.tuitionType = tuitionType;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
