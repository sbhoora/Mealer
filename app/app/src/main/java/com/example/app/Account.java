package com.example.app;

import java.io.Serializable;

public class Account implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    /**
     * Constructor that only uses email of user.
     * This is so the user password isn't passed around everywhere.
     * Besides, only the email is used as a key in the database.
     * @param email
     */
    public Account(String email) {
        this.email = email;
    }

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