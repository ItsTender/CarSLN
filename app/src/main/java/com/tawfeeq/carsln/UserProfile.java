package com.tawfeeq.carsln;

import java.util.ArrayList;

public class UserProfile {
    private String userPhoto;
    private String Username;
    private String Phone;
    private ArrayList<String> SavedCars;

    public UserProfile() {
    }

    public UserProfile(String userPhoto, String username, String phone) {
        this.userPhoto = userPhoto;
        Username = username;
        Phone = phone;
        SavedCars = new ArrayList<String>();
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public ArrayList<String> getSavedCars() {
        return SavedCars;
    }
    public void setSavedCars(ArrayList<String> savedCars) {
        SavedCars = savedCars;
    }
}
