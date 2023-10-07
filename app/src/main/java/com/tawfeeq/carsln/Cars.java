package com.tawfeeq.carsln;

import android.widget.ImageView;

public class Cars {

    private String Manufacturer;
    private String Model;
    private String Engine;
    private int BHP;
    private String carPhoto;

    public Cars() {
    }

    public Cars(String manufacturer, String model, String engine, int BHP,String carPhoto) {
        this.Manufacturer = manufacturer;
        this.Model = model;
        this.Engine = engine;
        this.BHP = BHP;
        this.carPhoto =carPhoto;
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

    public String getEngine() {
        return Engine;
    }

    public void setEngine(String engine) {
        Engine = engine;
    }

    public int getBHP() {
        return BHP;
    }

    public void setBHP(int BHP) {
        this.BHP = BHP;
    }

    public String getCarPhoto() {
        return carPhoto;
    }

    public void setCarPhoto(String carPhoto) {
        carPhoto = carPhoto;
    }

    @Override
    public String toString() {
        return "Cars{" +
                "Manufacturer='" + Manufacturer + '\'' +
                ", Model='" + Model + '\'' +
                ", Engine='" + Engine + '\'' +
                ", BHP=" + BHP +
                ", CarPhoto='" + carPhoto + '\'' +
                '}';
    }
}

