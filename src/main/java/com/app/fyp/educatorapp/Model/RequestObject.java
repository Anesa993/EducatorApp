package com.app.fyp.educatorapp.Model;

public class RequestObject {
    private String userId;
    private String serviceId;
    private String status;
    private String userName;
    private String userPhone;
    private String userAddress;
    private String usertime;
    private String tutorMobile;

    public String getUsertime() {
        return usertime;
    }

    public void setUsertime(String usertime) {
        this.usertime = usertime;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    private String usertype;


    public RequestObject() {
        // Default constructor
    }


    public RequestObject(String userId, String serviceId, String status, String userName, String userPhone, String userAddress, String usertime, String usertype) {
        this.userId = userId;
        this.serviceId = serviceId;
        this.status = status;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.usertime = usertime;
        this.usertype = usertype;
    }

    public String getUserId() {
        return userId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }
}
