package com.example.engelsiz;

public class Posts {

    private String username;
    private String helpType;
    private String helpQuantity;
    private String helpAddress;
    private String helpPhone;
    private String userId;

    //bu olmadan no argument şeklinde bir constructor olmak zorunda diye hata veriyor.
    public Posts() {
    }
    public Posts(String name, String type, String quantity, String address, String phone, String id) {
        this.username = name;
        this.helpType = type;
        this.helpQuantity = quantity;
        this.helpAddress = address;
        this.helpPhone = phone;
        this.userId = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return username;
    }
    public void setUserName(String userName) {
        this.username = userName;
    }
    public String getHelpType() {
        return helpType;
    }
    public void setHelpType(String helpType) {
        this.helpType = helpType;
    }
    public String getHelpQuantity() {
        return helpQuantity;
    }
    public void setHelpQuantity(String helpQuantity) {
        this.helpQuantity = helpQuantity;
    }
    public String getHelpAddress() {
        return helpAddress;
    }
    public void setHelpAddress(String helpAddress) {
        this.helpAddress = helpAddress;
    }
    public String getHelpPhone() {
        return helpPhone;
    }
    public void setHelpPhone(String helpPhone) {
        this.helpPhone = helpPhone;
    }







}
