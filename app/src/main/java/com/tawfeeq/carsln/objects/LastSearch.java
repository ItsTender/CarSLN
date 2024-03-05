package com.tawfeeq.carsln.objects;

public class LastSearch {

    private String Manufacturer;
    private String Model;
    private Float YearFrom;
    private Float YearTo;
    private String Offer;
    private Float PriceFrom;
    private Float PriceTo;
    private String Location;
    private Float KiloFrom;
    private Float KiloTo;

    public LastSearch(String manufacturer, String model, Float yearFrom, Float yearTo, String offer, Float priceFrom, Float priceTo, String location, Float kiloFrom, Float kiloTo) {
        Manufacturer = manufacturer;
        Model = model;
        YearFrom = yearFrom;
        YearTo = yearTo;
        Offer = offer;
        PriceFrom = priceFrom;
        PriceTo = priceTo;
        Location = location;
        KiloFrom = kiloFrom;
        KiloTo = kiloTo;
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

    public Float getYearFrom() {
        return YearFrom;
    }

    public void setYearFrom(Float yearFrom) {
        YearFrom = yearFrom;
    }

    public Float getYearTo() {
        return YearTo;
    }

    public void setYearTo(Float yearTo) {
        YearTo = yearTo;
    }

    public String getOffer() {
        return Offer;
    }

    public void setOffer(String offer) {
        Offer = offer;
    }

    public Float getPriceFrom() {
        return PriceFrom;
    }

    public void setPriceFrom(Float priceFrom) {
        PriceFrom = priceFrom;
    }

    public Float getPriceTo() {
        return PriceTo;
    }

    public void setPriceTo(Float priceTo) {
        PriceTo = priceTo;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Float getKiloFrom() {
        return KiloFrom;
    }

    public void setKiloFrom(Float kiloFrom) {
        KiloFrom = kiloFrom;
    }

    public Float getKiloTo() {
        return KiloTo;
    }

    public void setKiloTo(Float kiloTo) {
        KiloTo = kiloTo;
    }

}
