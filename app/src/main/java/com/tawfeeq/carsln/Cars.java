package com.tawfeeq.carsln;

import android.widget.ImageView;

public class Cars {

    private String Manufacturer;
    private String Model;
    private int BHP;
    private int Price;
    private String Photo;

    public Cars() {
    }

    public Cars(String manufacturer, String model,int BHP,int Price,String carPhoto) {
        this.Manufacturer = manufacturer;
        this.Model = model;
        this.BHP = BHP;
        this.Price = Price;
        this.Photo =carPhoto;
    }

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

    @Override
    public String toString() {
        return "Cars{" +
                "Manufacturer='" + Manufacturer + '\'' +
                ", Model='" + Model + '\'' +
                ", BHP=" + BHP + '\'' +
                ", price='" + Price + '\'' +
                ", CarPhoto='" + Photo + '\'' +
                '}';
    }
}

