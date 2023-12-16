package com.tawfeeq.carsln;

public class UserProfile {
    private String UserPhoto;
    private String Email;

    public UserProfile(String userPhoto, String email) {
        UserPhoto = userPhoto;
        Email = email;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        UserPhoto = userPhoto;
    }
}
