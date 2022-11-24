package com.example.app;

import java.io.Serializable;

public class Address implements Serializable {
    private String street;
    private String postal;
    private String country;
    private String province;
    private String city;

    public Address(String street, String postal, String country, String province, String city){
        this.street = street;
        this.postal = postal;
        this.country = country;
        this.province = province;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public String getPostal() {
        return postal;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String toString(){
        String a = (getStreet() + ", "  + getCity() + ", " + getProvince() + ", " + getPostal() + ", " + getCountry());
        return a;
    }

    public static boolean isValidPostal(String postal){
        return postal.length() == 6;
    }
}
