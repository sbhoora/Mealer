package com.example.app;

import java.io.Serializable;

public class Address implements Serializable {
    private String street;
    private String postal;
    private String country;
    private String province;
    private String city;

    public Address() {
        // Default Constructor for database retrieval
    }

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

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String toString(){
        String a = (getStreet() + ", "  + getCity() + ", " + getProvince() + ", " + getPostal() + ", " + getCountry());
        return a;
    }

    public static boolean isValidPostal(String postal){
        return postal.length() == 6;
    }
}
