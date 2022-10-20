package com.example.app;

public class Cook implements Account {
    String firstName;
    String lastName;
    String email;
    String password;
    Address Address;
    String description;

    public Cook(String first, String last, String mail, String pass, Address address, String description) 
    {
        firstName = first;
        lastName = last;
        email = mail;
        password = pass;
        this.address = address;
        this.description = description;
        
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

    public String getAccountType(){
        return "Cook";
    }

    public String getAddress(){
        return adress.toString;
    }

    public String getDescription() {
        return description;
    }

}
