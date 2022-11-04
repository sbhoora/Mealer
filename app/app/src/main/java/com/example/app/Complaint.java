package com.example.app;

public class Complaint {
    private String subject;
    private String complaintAbout; // either the beginning of the email or the entire thing??
    private String complaint;

    public Complaint(String sub, String aUser , String comp) {
        this.subject = sub;
        this.complaintAbout = aUser;
        this.complaint = comp;
    }

    public String getComplaint() {
        return complaint;
    }

    public String getComplaintAbout() {
        return complaintAbout;
    }

    public String getSubject() {
        return subject;
    }
}
