package com.tawfeeq.carsln.objects;

import java.util.ArrayList;

public class Users {

    private String userPhoto;
    private String Username;
    private String Phone;
    private String Location;
    private ArrayList<String> SavedCars;
    private String docID;

    public Users() {
    }

    public Users(String userPhoto, String username, String phone, String Location, ArrayList<String> SavedCars) {
        this.userPhoto = userPhoto;
        Username = username;
        Phone = phone;
        this.Location = Location;
        this.SavedCars = SavedCars;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
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
