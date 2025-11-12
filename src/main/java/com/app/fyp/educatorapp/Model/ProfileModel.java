package com.app.fyp.educatorapp.Model;

public class ProfileModel {
    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String mobile;


    // Default constructor (required for Firestore)
    public ProfileModel() {
        // Default constructor required for calls to DataSnapshot.getValue(UserProfile.class)
    }

    // Constructor
    public ProfileModel(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    // Getters and setters (required for Firestore serialization)
    // Implement getters and setters for 'name', 'email', and 'age' fields
}
