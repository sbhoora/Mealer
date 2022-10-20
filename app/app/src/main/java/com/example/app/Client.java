package com.example.app;

public class Client implements Account {
    String firstName;
    String lastName;
    String email;
    String password;
    Address address;
    CreditCard creditCard;

    public Client(String first, String last, String mail, String pass, Address address, CreditCard creditCard) {
        firstName = first;
        lastName = last;
        email = mail;
        password = pass;
        this.address = address;
        this.creditCard = creditCard;
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
        return "Client";
    }

    public String getAddress(){
        return adress.toString;
    }

    public int getCreditCard(){
        return paymentInfo();
    }

}
