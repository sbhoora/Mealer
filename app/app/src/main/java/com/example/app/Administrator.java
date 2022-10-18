package com.example.app;

public class Administrator implements Account {

    String firstName;
    String lastName;
    String email;
    String password;

    public Administrator(String f, String l, String e, String p) {
        firstName = f;
        lastName = l;
        email = e;
        password = p;
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

    public String getAccountType() {
        return "Administrator";
    }

}