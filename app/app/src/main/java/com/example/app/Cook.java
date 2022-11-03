package com.example.app;

public class Cook extends Account {
    
    Address address;
    String description;
    Boolean suspended = false;

    public Cook(String first, String last, String mail, String pass, Address address, String description) 
    {
        super(first, last, mail, pass);
        this.address = address;
        this.description = description;
        
    }


    public String getAccountType(){
        return "Cook";
    }

    public Address getAddress(){
        return address;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isSuspended() { return suspended;};

}
