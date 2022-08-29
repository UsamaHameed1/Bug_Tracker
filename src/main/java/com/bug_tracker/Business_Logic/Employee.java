package com.bug_tracker.Business_Logic;

import org.bson.types.ObjectId;

public abstract class Employee {

    private ObjectId mongoId;
    private String name;
    private String cnic;
    private String contactNo;
    private String email;
    private String userName;
    private String userPassword;
    private String designation;


    public Employee( String name, String cnic, String contactNo, String email, String userName, String userPassword,String designation) {
        this.name = name;
        this.cnic = cnic;
        this.contactNo = contactNo;
        this.email = email;
        this.userName = userName;
        this.userPassword = userPassword;
        this.designation=designation;
    }

    //============ Getter and Setters =========================================
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public ObjectId getMongoId() {
        return mongoId;
    }

    public void setMongoId(ObjectId mongoId) {
        this.mongoId = mongoId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "mongoId=" + mongoId +
                ", name='" + name + '\'' +
                ", cnic='" + cnic + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", designation='" + designation + '\'' +
                '}';
    }
}
