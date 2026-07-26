package com.zs.assignment7.model;

public class Student {
    private  String id;
    private String firstName;
    private String lastName;
    private String mobile;

    public Student(String id, String firstName, String lastName, String mobile) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobile() {
        return mobile;
    }
}
