package com.example.app;

public class Administrator extends Account {

    public Administrator(String f, String l, String e, String p) {
        super(f, l, e, p);
    }

    public String getAccountType() {
        return "Administrator";
    }

}