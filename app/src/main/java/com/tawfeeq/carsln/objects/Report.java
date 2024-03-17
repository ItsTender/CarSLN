package com.tawfeeq.carsln.objects;

public class Report {

    private String email;
    private String reason;
    private String content;
    // Report contents are (Sender Email for further comm, reason on report for easier evaluation, content for extra info on the matter)!!!!

    public Report() {
        // Empty Report.........
    }

    public Report(String email, String reason, String content) {
        this.email = email;
        this.reason = reason;
        this.content = content;
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
