package com.example.app;

public class Account {

    String firstName;
    String lastName;
    String email;
    String password;

    public Account(String first, String last, String mail, String pass) {
        String firstName = first;
        String lastName = last;
        String email = mail;
        String password = pass; 
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