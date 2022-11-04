package com.example.app;

public class Client extends Account {

    Address address;
    CreditCard creditCard;

    public Client(String first, String last, String mail, String pass, Address address, CreditCard creditCard) {
        super(first, last, mail, pass);
        this.address = address;
        this.creditCard = creditCard;
    }

    public static String getAccountType(){
        return "Client";
    }

    public Address getAddress(){
        return address;
    }

    public String getCreditCard(){
        return creditCard.getCardNumber();
    }

}
