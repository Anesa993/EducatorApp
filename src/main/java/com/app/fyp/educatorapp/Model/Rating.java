package com.app.fyp.educatorapp.Model;

public class Rating {
    private String userId;
    private String serviceid;
    private String userrate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public Rating() {
        // Default constructor
    }

    public Rating(String userId, String userrate, String serviceid, String username) {
        this.userId = userId;
        this.userrate = userrate;
        this.serviceid = serviceid;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserrate() {
        return userrate;
    }

    public void setUserrate(String userrate) {
        this.userrate = userrate;
    }


}
