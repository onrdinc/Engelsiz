package com.example.engelsiz;

public class Users {

    private String userEmail;
    private String userPassword;
    private String name;
    private String userId;

    public Users() {
    }

    public Users(String userEmail, String userPassword, String name, String userId) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
