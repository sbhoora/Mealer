package com.example.app;

public class Complaint {
    private String subject;
    private String complaintAbout; // either the beginning of the email or the entire thing??
    private String email;
    private String complaint;

    public Complaint(String sub, String aUser , String mail, String comp) {
        this.subject = sub;
        this.complaintAbout = aUser;
        this.email = mail;
        this.complaint = comp;
    }
}
