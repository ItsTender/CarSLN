package com.tawfeeq.carsln.objects;

import android.net.Uri;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class FireBaseServices {
    private static FireBaseServices instance;
    private FirebaseAuth auth;
    private FirebaseFirestore store;
    private FirebaseStorage storage;

    //--------------------------------------
    private Uri selectedImageURL;
    private UserProfile user;

    //--------------------------------------
    private CarID selectedCar;
    private ArrayList<CarID> SearchList;
    private ArrayList<CarID> CarList;
    private ArrayList<CarID> MarketList;

    //--------------------------------------
    private String From;
    private String currentFragment; // Checking the Last Fragment the User was in, for Back Pressed in the MainActivity!!
    private String LastFilter;
    private LastSearch lastSearch;

    //--------------------------------------
    // To Save the Scrolling Position for Each Recycler!!
    private Parcelable rcForYou;
    private Parcelable rcSearch;
    private Parcelable rcSaved;
    private Parcelable rcListings;

    //--------------------------------------


    public String getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(String currentFragment) {
        this.currentFragment = currentFragment;
    }

    public Parcelable getRcListings() {
        return rcListings;
    }

    public void setRcListings(Parcelable rcListings) {
        this.rcListings = rcListings;
    }

    public Parcelable getRcSaved() {
        return rcSaved;
    }

    public void setRcSaved(Parcelable rcSaved) {
        this.rcSaved = rcSaved;
    }

    public Parcelable getRcSearch() {
        return rcSearch;
    }

    public void setRcSearch(Parcelable rcSearch) {
        this.rcSearch = rcSearch;
    }

    public Parcelable getRcForYou() {
        return rcForYou;
    }

    public void setRcForYou(Parcelable rcForYou) {
        this.rcForYou = rcForYou;
    }

    public String getLastFilter() {
        return LastFilter;
    }

    public void setLastFilter(String lastFilter) {
        LastFilter = lastFilter;
    }

    public LastSearch getLastSearch() {
        return lastSearch;
    }

    public void setLastSearch(LastSearch lastSearch) {
        this.lastSearch = lastSearch;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public ArrayList<CarID> getMarketList() {
        return MarketList;
    }

    public void setMarketList(ArrayList<CarID> marketList) {
        MarketList = marketList;
    }

    public CarID getSelectedCar() {
        return selectedCar;
    }

    public void setSelectedCar(CarID selectedCar) {
        this.selectedCar = selectedCar;
    }

    public ArrayList<CarID> getCarList() {
        return CarList;
    }

    public void setCarList(ArrayList<CarID> carList) {
        CarList = carList;
    }

    public ArrayList<CarID> getSearchList() {
        return SearchList;
    }

    public void setSearchList(ArrayList<CarID> searchList) {
        SearchList = searchList;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public Uri getSelectedImageURL() {
        return selectedImageURL;
    }

    public void setSelectedImageURL(Uri selectedImageURL) {
        this.selectedImageURL = selectedImageURL;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseFirestore getStore() {
        return store;
    }

    public FirebaseStorage getStorage() {return storage;}

    private FireBaseServices()
    {
        this.auth= FirebaseAuth.getInstance();
        this.store= FirebaseFirestore.getInstance();
        this.storage=FirebaseStorage.getInstance();
    }
    public static FireBaseServices getInstance()
    {
        if(instance==null) instance = new FireBaseServices();

        return instance;
    }

}
