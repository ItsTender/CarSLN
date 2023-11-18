package com.tawfeeq.carsln;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class Cars {

    private String Manufacturer;
    private String Model;
    private int BHP;
    private int Price;
    private int Year;
    private String Transmission;
    private int Kilometre;
    private int Users;
    private String Phone;
    private String Photo;

    public Cars() {
    }

    public Cars(String manufacturer, String model,int BHP,int Price, int Year, String Transmission, int Kilometre, int Users, String Phone, String carPhoto) {
        this.Manufacturer = manufacturer;
        this.Model = model;
        this.BHP = BHP;
        this.Price = Price;
        this.Year= Year;
        this.Transmission=Transmission;
        this.Kilometre=Kilometre;
        this.Users=Users;
        this.Phone=Phone;
        this.Photo =carPhoto;
    }

    // protected Cars(Parcel in) {}

   // @Override public void writeToParcel(Parcel dest, int flags) {}

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int Price) {
        this.Price = Price;
    }

    public int getBHP() {
        return BHP;
    }

    public void setBHP(int BHP) {
        this.BHP = BHP;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setCarPhoto(String carPhoto) {
        this.Photo = carPhoto;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public String getTransmission() {
        return Transmission;
    }

    public void setTransmission(String transmission) {
        Transmission = transmission;
    }

    public int getKilometre() {
        return Kilometre;
    }

    public void setKilometre(int kilometre) {
        Kilometre = kilometre;
    }

    public int getUsers() {
        return Users;
    }

    public void setUsers(int users) {
        Users = users;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    @Override
    public String toString() {
        return "Cars{" +
                "Manufacturer='" + Manufacturer + '\'' +
                ", Model='" + Model + '\'' +
                ", BHP=" + BHP +
                ", Price=" + Price +
                ", Year=" + Year +
                ", Transmission='" + Transmission + '\'' +
                ", Kilometre=" + Kilometre +
                ", Users=" + Users +
                ", Phone=" + Phone +
                ", Photo='" + Photo + '\'' +
                '}';
    }
}

