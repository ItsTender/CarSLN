package com.tawfeeq.carsln;

public class CarID {
    private String id; // the ID of the Listing in the FireStore.
    private boolean SellLend; // if the User wants to Sell the Car the Var will be 'TRUE' else if the User only wants to Lend his Car the Var will be 'FALSE'
    private String Email; // The User's Email Address according to the Listing.
    private String Manufacturer;
    private String Model;
    private int BHP;
    private int Price;
    private int Year;
    private String Transmission;
    private String Engine;
    private int Kilometre;
    private int Users;
    private String Color;
    private String Location; // Only the Provinces of Palestine.
    private String NextTest;
    private String Photo; // First Photo of Three (Maximum).
    private String Secondphoto; // Second Photo of Three
    private String ThirdPhoto; // Third Photo of Three

    public CarID() {
    }

    public CarID(boolean sellLend, String email, String manufacturer, String model, int BHP, int price, int year, String transmission, String engine, int kilometre, int users, String color, String location, String nextTest, String photo, String secondphoto, String thirdPhoto) {

        SellLend = sellLend;
        Email = email;
        Manufacturer = manufacturer;
        Model = model;
        this.BHP = BHP;
        Price = price;
        Year = year;
        Transmission = transmission;
        Engine = engine;
        Kilometre = kilometre;
        Users = users;
        Color = color;
        Location = location;
        NextTest = nextTest;
        Photo = photo;
        Secondphoto =secondphoto;
        Transmission =thirdPhoto;

    }

    // protected Cars(Parcel in) {}

    // @Override public void writeToParcel(Parcel dest, int flags) {}

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getNextTest() {
        return NextTest;
    }

    public void setNextTest(String nextTest) {
        NextTest = nextTest;
    }

    public boolean getSellLend() {
        return SellLend;
    }

    public void setSellLend(boolean sellLend) {
        SellLend = sellLend;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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

    public String getEngine() {
        return Engine;
    }

    public void setEngine(String engine) {
        Engine = engine;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecondphoto() {
        return Secondphoto;
    }

    public void setSecondphoto(String secondphoto) {
        Secondphoto = secondphoto;
    }

    public String getThirdPhoto() {
        return ThirdPhoto;
    }

    public void setThirdPhoto(String thirdPhoto) {
        ThirdPhoto = thirdPhoto;
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
                ", Photo='" + Photo + '\'' +
                '}';
    }
}
