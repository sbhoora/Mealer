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

    public Cook(String email, String password, Boolean suspended) {
        super(email, password);
        this.suspended = suspended;
    }

    // Getters
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

    // Setters
    public void suspend() {suspended = true;};

    public void removeSuspension() {suspended = false;};

}
