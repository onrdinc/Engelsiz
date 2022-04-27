package com.example.engelsiz;

public class User {

    private String userEmail;
    private String userPassword;
    private String FullName;
    private String userId;



    public User() {
        //no arg constructor
    }

    public User(String userEmail, String userPassword, String FullName, String userId) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.FullName = FullName;
        this.userId = userId;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String getFullName() {
        return FullName;
    }
    public void setFullName(String fullName) {
        FullName = fullName;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}

