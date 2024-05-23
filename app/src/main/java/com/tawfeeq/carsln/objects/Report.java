package com.tawfeeq.carsln.objects;

import com.google.firebase.Timestamp;

public class Report {

    private String email;
    private String userType;
    private String reason;
    private String content; // Report contents are (Sender Email for further comm, reason on report for easier evaluation, content for extra info on the matter)!!!!
    private com.google.firebase.Timestamp timestamp;


    public Report() {
    }

    public Report(String email, String userType, String reason, String content) {
        this.email = email;
        this.userType = userType;
        this.reason = reason;
        this.content = content;
        timestamp = new com.google.firebase.Timestamp(com.google.firebase.Timestamp.now().toDate());
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
