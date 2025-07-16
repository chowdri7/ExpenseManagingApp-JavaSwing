package com.expenseapp.model;

public class User {
    private int id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private String otherField1;
    private String otherField2;
    private String password;

    // Constructors
    public User() {}

    public User(int id, String name, String address, String email, String phone, String otherField1, String otherField2, String password) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.otherField1 = otherField1;
        this.otherField2 = otherField2;
        this.password = password;
    }

    // Getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getOtherField1() { return otherField1; }
    public void setOtherField1(String otherField1) { this.otherField1 = otherField1; }

    public String getOtherField2() { return otherField2; }
    public void setOtherField2(String otherField2) { this.otherField2 = otherField2; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
