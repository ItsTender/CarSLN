package com.tawfeeq.carsln;

import android.widget.ImageView;

public class Cars {

    private String Manufacturer;
    private String Model;
    private String Engine;
    private int BHP;
    private String CarPhoto;

    public Cars() {
    }

    public Cars(String manufacturer, String model, String engine, int BHP,String carPhoto) {
        Manufacturer = manufacturer;
        Model = model;
        Engine = engine;
        this.BHP = BHP;
        this.CarPhoto =carPhoto;
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

    @java.lang.Override
    public java.lang.String toString() {
        return "Car{" +
                "Manufacturer='" + Manufacturer + '\'' +
                ", Model='" + Model + '\'' +
                ", Engine='" + Engine + '\'' +
                ", BHP=" + BHP +
                '}';
    }
}
