package com.tawfeeq.carsln;

public class UserProfile {
    private String userPhoto;
    private String Email;

    public UserProfile() {
    }

    public UserProfile(String userPhoto, String email) {
        this.userPhoto = userPhoto;
        Email = email;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        userPhoto = userPhoto;
    }
}
