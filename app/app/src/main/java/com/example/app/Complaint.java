package com.example.app;

public class Complaint {
    private String subject;
    private String cookEmail; // either the beginning of the email or the entire thing??
    private String description;

    public Complaint(String sub, String aUser , String comp) {
        this.subject = sub;
        this.cookEmail = aUser;
        this.description = comp;
    }

    public String getDescription() {
        return description;
    }

    public String getCookEmail() {
        return cookEmail;
    }

    public String getSubject() {
        return subject;
    }
}
