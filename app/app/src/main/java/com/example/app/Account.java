package com.example.app;

public class Account {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public Account(String first, String last, String mail, String pass) {
         this.firstName = first;
         this.lastName = last;
         this.email = mail;
         this.password = pass;
    }

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}