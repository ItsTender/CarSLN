package com.tawfeeq.carsln;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import io.grpc.internal.LogExceptionRunnable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class AddCarFragment extends Fragment {

    Utils utils;
    FireBaseServices fbs;
    Cars AddCar;
    EditText Price,BHP,Users,Kilometre,Engine;
    Spinner SpinnerGear, SpinnerYear, SpinnerSellLend, SpinnerMan, SpinnerMod, SpinnerColor, SpinnerLocation, SpinnerTestMonth, SpinnerTestYear;
    Button Add,Return;
    ImageView IV;
    String photo;

    //ProgressDialog pd = new ProgressDialog(getActivity());

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddCarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCarFragment newInstance(String param1, String param2) {
        AddCarFragment fragment = new AddCarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_car, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs=FireBaseServices.getInstance();
        utils=Utils.getInstance();
        BHP = getView().findViewById(R.id.etBHP);
        Price = getView().findViewById(R.id.etPrice);
        Users=getView().findViewById(R.id.etUsers);
        Engine = getView().findViewById(R.id.etEngine);
        Kilometre=getView().findViewById(R.id.etKM);
        IV = getView().findViewById(R.id.ivAddCar);
        Add = getView().findViewById(R.id.btnAdd);
        SpinnerGear = getView().findViewById(R.id.SpinnerGear);
        SpinnerYear = getView().findViewById(R.id.SpinnerYear);
        SpinnerSellLend= getView().findViewById(R.id.SpinnerSellLend);
        SpinnerMan = getView().findViewById(R.id.SpinnerManufacturer);
        SpinnerMod = getView().findViewById(R.id.SpinnerModel);
        SpinnerTestYear = getView().findViewById(R.id.SpinnerNextTestYear);
        SpinnerTestMonth = getView().findViewById(R.id.SpinnerNextTestMonth);
        SpinnerColor = getView().findViewById(R.id.SpinnerCarColor);
        SpinnerLocation = getView().findViewById(R.id.SpinnerLocationArea);


        String [] GearList = {"Gear Type", "Automatic", "Manual", "PDK", "DCT", "CVT", "SAT","iManual"};
        ArrayAdapter<String> GearAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, GearList);
        GearAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        SpinnerGear.setAdapter(GearAdapter);


        String [] Years = { "Model Year","2024","2023","2022","2021",
                "2020","2019","2018","2017","2016","2015","2014","2013","2012","2011",
                "2010","2009","2008","2007","2006","2005","2004","2003","2002","2001","2000","1999","1998","1997","1996","1995","1994","1993","1992","1991", "1990", "1989", "1988"};
        ArrayAdapter<String> YearsAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, Years);
        YearsAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        SpinnerYear.setAdapter(YearsAdapter);


        String [] TestYear = { "Test Year Until","2024","2025","2026"};
        ArrayAdapter<String> TestYearAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, TestYear);
        TestYearAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        SpinnerTestYear.setAdapter(TestYearAdapter);


        String [] TestMonth = { "Test Month Until","1","2","3","4","5","6","7","8","9","10","11","12"};
        ArrayAdapter<String> TestMonthAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, TestMonth);
        TestMonthAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        SpinnerTestMonth.setAdapter(TestMonthAdapter);


        String [] CarColor = { "Select Car Color","White","Dark Grey","Grey","Light Grey","Silver","Black","Dark Red","Red","Light Red","Dark Green","Green","Light Green","Dark Blue","Blue","Vibrant Blue","Light Blue","Orange","Yellow","Brown","Purple"};
        ArrayAdapter<String> ColorAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, CarColor);
        ColorAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        SpinnerColor.setAdapter(ColorAdapter);


        String [] Location = {"Select Location Area","Golan","Galil","Haifa","Central","Tel Aviv","Jerusalem","Be'er Sheva","Central Southern","Eilat"};
        ArrayAdapter<String> LocationAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, Location);
        LocationAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        SpinnerLocation.setAdapter(LocationAdapter);


        String [] HowSellLend = {"What do you want to do with the Car", "Sell the Car" , "Lend the Car" };
        ArrayAdapter<String> SellLendAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, HowSellLend);
        SellLendAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        SpinnerSellLend.setAdapter(SellLendAdapter);


        String [] ManList = {"Choose Car Manufacturer","Audi","Abarth", "Alfa Romeo", "Aston Martin","BMW", "Bentley", "Citroen", "Cadillac", "Cupra", "Chevrolet",
                "Dacia","Dodge","Fiat", "Ford", "Ferrari", "Genesis", "Honda", "Hyundai","Infiniti","Isuzu","Jeep", "Jaguar", "Kia", "Lamborghini","Land Rover", "Lexus",
                "Maserati", "Mini", "Mitsubishi", "Mercedes", "Nissan", "Opel", "Porsche", "Peugeot", "Renault", "Subaru", "Suzuki","Seat", "Skoda", "Toyota", "Tesla", "Volkswagen", "Volvo"};
        //43 Manufacturers!!
        ArrayAdapter<String> ManAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ManList);
        ManAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        SpinnerMan.setAdapter(ManAdapter);


        // Here is the Whole list of Models For Each Car Manufacturer (42 lists and the No Manufacturer List)


        String [] ModelNon = {"Choose the Car's Manufacturer"};

        String [] ModelAudi = {"Choose Audi Model", "100", "80", "A1", "A3", "A4", "A5", "A6", "A7", "A8", "E-tron", "E-tron GT", "E-tron Q4", "Q2", "Q3", "Q4", "Q5",
                "Q6", "Q7", "Q8", "R8", "RS3", "RS4", "RS5", "RS6", "RS7", "RSQ3", "RSQ8", "S3", "S4", "S5", "S6", "S7", "S8", "SQ5", "SQ7", "SQ8", "TT", "TT RS"};

        String [] ModelAbarth = {"Choose Abarth Model", "124 Spider", "500", "500c", "595", "595c"};

        String [] ModelAlfa = {"Choose Alfa Romeo Model", "145", "146", "147", "156", "159", "164", "166", "33", "75", "90", "4C", "4C Spider", "8C", "8C Spider", "GT", "GTV", "Giulia", "Giulia Quadrifoglio",
                "Giulietta", "brera", "Tonale", "MiTO", "Stelvio", "Spider"};

        String [] ModelAston = {"Choose Aston Martin Model", "DB11", "DB9", "DBS", "DBX", "Vantage", "Vanquish", "Rapide"};

        String [] ModelBMW = {"Choose BMW Model", "1 Series",  "2 Series", "3 Series", "3 Series Convertible", "4 Series", "5 Series",  "6 Series", "7 Series", "8 Series", "i3", "iX3", "i4", "i7", "i8", "iX", "M1", "M2", "M3", "M4", "M5", "M6", "M8",
                "M2 Competition", "M3 Competition", "M4 Competition", "M5 Competition", "M6 Competition", "M8 Competition", "X1", "X2", "X3", "X4", "X5", "X6", "X6M", "X7", "Z3", "Z4"};

        String [] ModelBentley = {"Choose Bentley Model", "BE53", "Bentayga", "Continental GT", "Flying Spur"};

        String [] ModelCitroen = {"Choose Citroen Model", "AX", "C Elysee", "C Zero", "C1", "C2", "C3", "C3 Aircross", "C4", "C4 X", "C5", "C6", "C8", "CX", "DS3", "DS3 Convertible", "DS4", "DS5", "XM", "ZX",
                "BX", "C15", "Berlingo", "Berlingo Electric", "Jumper"};

        String [] ModelCadillac = {"Choose Cadillac Model", "ATS", "CT4", "CT5", "CT4 V Blackwing", "CT5 V Blackwing", "CT6", "CTS", "DTS", "SRX", "STS", "XLR", "XT4", "XT5", "XT6", "XTS", "Escalade"};

        String [] ModelCupra = {"Choose Cupra Model", "Ateca", "Leon", "Formentor", "Born"};

        String [] ModelChevrolet = {"Choose Chevrolet Model", "Optra", "Orlando", "Impala", "Uplander", "Equinox", "Bolt", "Blaze", "Volt", "Tahoe", "Traverse", "Trax", "Trailblazer", "Malibu", "Suburban",
                "Sonic", "Spark", "Camaro", "Camaro ZL1", "Camaro ZL1 1LE", "Corvette", "Corvette ZO6", "Corvette ZL1", "Cruze", "Nevada", "Savana", "Silverado 1500", "Silverado 2500", "Silverado 3500",
                "Colorado", "Colorado ZR2"};

        String [] ModelDacia = {"Choose Dacia Model", "Duster", "Jogger", "Logan MCV", "Lodgy", "Sandero", "Sandero Stepway", "Spring", "Dokker"};

        String [] ModelDodge = {"Choose Dodge Model", "Avenger", "Journey", "Durango", "Durango SRT", "Nitro", "Challenger", "Challenger Hellcat", "Challenger Demon", "Charger", "Charger Hellcat", "Caliber",
                "Dart", "Viper", "SRT Viper", "SRT Viper GTS", "Ram", "Ram TRX", "Ram 1500", "Ram 2500", "Ram 3500"};

        String [] ModelFiat = {"Choose Fiat Model", "500", "500C", "500L", "500X", "Bravo", "Tipo", "Croma", "Linea", "Multipla", "Ponto", "Panda", "Doblo", "Ducato", "Scudo", "QUBO"};

        String [] ModelFord = {"Choose Ford Model", "Edge", "S Max", "Escort", "Escape", "Expedition", "Explorer", "Bronco", "Bronco R", "Galaxy", "Taurus", "Lincoln MKC", "Lincoln MKS", "Lincoln MKT", "Lincoln MKZ",
                "Mondeo", "Mustang", "Mustang GT", "Mustang SVT Cobra", "Mustang Mach E", "Mustang Shelby GT500", "Mustang Shelby GT350", "Puma", "Focus", "Fiesta", "Fusion", "Kuga", "Excrusion", "F-150", "F-150 Lightning",
                "F-250", "F-350", "GT", "Transit", "Maverick", "Connect", "Ranger"};

        String [] ModelFerrari = {"Choose Ferrari Model", "296 GTB", "Roma", "F8 Tributo", "SF90 Stradale", "488 Pista", "488 GTB", "Portofino", "F12tdf", "GTC4Lusso", "812 Superfast", "458 Speciale", "458 Spider", "458 Italia",
                "LaFerrari", "F12berlinetta", "599 GTO", "599 GTB", "F430", "360 Modena"};

        String [] ModelGenesis = {"Choose Genesis Model", "G70", "G80", "G90", "GLS", "GV60", "GV70", "GV80"};

        String [] ModelHonda = {"Choose Honda Model", "Acura Integra", "Acura NSX", "Acura MDX", "Shuttle", "CR-V", "CR-Z", "FR-V", "HR-V", "S2000", "Odyssey", "Accord", "Insight","Jazz", "Jazz Hybrid", "Legend", "Stream",
                "Civic Type-R", "Civic Hybrid", "Civic Hatchback", "Civic Sedan", "Civic Station", "Pilot", "Prelude"};

        String [] ModelHyundai = {"Choose Hyundai Model", "i10", "i20", "i20N", "i25", "i30", "i30CW", "i30N", "i35", "i40", "iX20", "iX35", "iX55", "Ioniq",  "Ioniq 5", "Ioniq 6", "Elantra", "Accent", "Bayon", "Getz", "Veloster", "Veloster N",
                "Venue", "Terracan", "Tucson", "Sonata", "Sonata Hybrid", "Sonata N", "Santa Fe", "Palisade", "Kona", "Kona EV", "Coupe", "H1", "H100", "H100 Truck", "i800", "Staria"};

        String [] ModelInfiniti = {"Choose Infiniti Model", ""};

        String [] ModelIsuzu = {"Choose Isuzu Model", ""};

        String [] ModelJeep = {"Choose Jeep Model", ""};

        String [] ModelJaguar = {"Choose Jaguar Model", ""};

        String [] ModelKia = {"Choose Kia Model", ""};

        String [] ModelLamborghini = {"Choose Lamborghini Model", ""};

        String [] ModelLandRover = {"Choose Land Rover Model", ""};

        String [] ModelLexus = {"Choose Lexus Model", ""};

        String [] ModelMaserati = {"Choose Maserati Model", ""};

        String [] ModelMini = {"Choose Mini Model", ""};

        String [] ModelMitsubishi = {"Choose Mitsubishi Model", ""};

        String [] ModelMercedes = {"Choose Mercedes Model", ""};

        String [] ModelNissan = {"Choose Nissan Model", ""};

        String [] ModelOpel = {"Choose Opel Model", ""};

        String [] ModelPorsche = {"Choose Porsche Model", "718 Boxster", "718 Cayman", "Cayman GT4", "Cayman GT4 RS", "Carrera GT", "918 Spyder", "911 Carrera", "911 Carrera S", "911 Carrera 4S", "911 Convertible", "911 Speedster", "911 Sports Classic", "911 S/T", "911 Turbo S", "911 Targa", "911 GT2 RS", "911 GT3", "911 GT3 RS", "Taycan", "Macan", "Macan GTS",
                "Panamera", "Panamera Turbo", "Cayenne", "Cayenne Turbo"};

        String [] ModelPeugeot = {"Choose Peugeot Model", ""};

        String [] ModelRenault = {"Choose Renault Model", ""};

        String [] ModelSubaru = {"Choose Subaru Model", ""};

        String [] ModelSuzuki = {"Choose Suzuki Model", ""};

        String [] ModelSeat = {"Choose Seat Model", ""};

        String [] ModelSkoda = {"Choose Skoda Model", ""};

        String [] ModelToyota = {"Choose Toyota Model", ""};

        String [] ModelTesla = {"Choose Tesla Model", ""};

        String [] ModelVolkswagen = {"Choose Volkswagen Model", ""};

        String [] ModelVolvo = {"Choose Volvo Model", ""};


        // Ends ........

        SpinnerMan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                String item = adapterView.getSelectedItem().toString();

                // For Each Manufacturer Selected, The Models get a new String Array of Sold/Available Models of that Manufacturer!
                if(item.equals("Choose Car Manufacturer")){

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelNon);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Audi")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelAudi);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Abarth")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelAbarth);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Alfa Romeo")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelAlfa);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Aston Martin")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelAston);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("BMW")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelBMW);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Bentley")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelBentley);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Citroen")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelCitroen);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Cadillac")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelCadillac);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Cupra")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelCupra);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Chevrolet")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelChevrolet);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Dacia")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelDacia);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Dodge")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelDodge);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Fiat")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelFiat);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Ford")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelFord);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Ferrari")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelFerrari);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Genesis")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelGenesis);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Honda")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelHonda);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Hyundai")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelHyundai);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Infiniti")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelInfiniti);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Isuzu")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelIsuzu);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Jeep")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelJeep);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Jaguar")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelJaguar);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Kia")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelKia);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Lamborghini")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelLamborghini);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Land Rover")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelLandRover);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Lexus")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelLexus);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Maserati")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelMaserati);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Mini")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelMini);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Mitsubishi")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelMitsubishi);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Mercedes")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelMercedes);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Nissan")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelNissan);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Opel")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelOpel);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Porsche")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelPorsche);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Peugeot")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelPeugeot);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Renault")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelRenault);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Subaru")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelSubaru);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Suzuki")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelSuzuki);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Seat")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelSeat);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Skoda")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelSkoda);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Toyota")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelToyota);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Tesla")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelTesla);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Volkswagen")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelVolkswagen);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Volvo")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelVolvo);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Does Nothing As Nothing is Selected
            }
        });


        IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageChooser();
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check Info (....)
                String Man= SpinnerMan.getSelectedItem().toString();
                String Mod= SpinnerMod.getSelectedItem().toString();
                String HP= BHP.getText().toString();
                String prc= Price.getText().toString();
                String User =Users.getText().toString();
                String engine = Engine.getText().toString();
                String Kilo =Kilometre.getText().toString();
                String transmission = SpinnerGear.getSelectedItem().toString();
                String year = SpinnerYear.getSelectedItem().toString();
                String SellLend = SpinnerSellLend.getSelectedItem().toString();
                String Color = SpinnerColor.getSelectedItem().toString();
                String area = SpinnerLocation.getSelectedItem().toString();
                String testyear = SpinnerTestYear.getSelectedItem().toString();
                String testmonth = SpinnerTestMonth.getSelectedItem().toString();
                if(SellLend.equals("What do you want to do with the Car")||Man.equals("Choose Car Manufacturer")||Mod.equals("Choose a Manufacturer First")||Mod.equals("Choose Car Model")||HP.trim().isEmpty()||prc.trim().isEmpty()||year.equals("Select Year")|| transmission.equals("Gear Type") ||User.trim().isEmpty()||engine.trim().isEmpty()||Kilo.trim().isEmpty()||Color.equals("Select Car Color")||area.equals("Select Location Area")||testyear.equals("Test Year Until")||testmonth.equals("Test Month Until")) {
                    Toast.makeText(getActivity(), "Some Fields Are Missing", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Adding the Car
                int power= Integer.parseInt(HP);
                int price=Integer.parseInt(prc);
                int Yahr= Integer.parseInt(year);
                int userhands= Integer.parseInt(User);
                int KM= Integer.parseInt(Kilo);
                boolean selllend = true;
                if(SellLend.equals("Sell the Car")) selllend=true;
                else if(SellLend.equals("Lend the Car")) selllend=false;

                String test = testmonth + "/" + testyear;

                if(fbs.getSelectedImageURL()==null) photo ="";
                else photo = fbs.getSelectedImageURL().toString()+".jpg";
                // Sell Lend; True=Sell the Car, False=Lend the Car.
                Cars Add = new Cars(selllend,fbs.getAuth().getCurrentUser().getEmail(),Man,Mod,power,price,Yahr,transmission,engine,KM,userhands,Color,area,test,photo);
                FirebaseFirestore db= fbs.getStore();
                db.collection("MarketPlace").add(Add).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Car Added To Market Place", Toast.LENGTH_LONG).show();
                        fbs.setSelectedImageURL(null);
                        //pd.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Couldn't Upload Car To Market Place, Try Again Later", Toast.LENGTH_LONG).show();
                        //pd.dismiss();

                    }
                });
            }
        });
    }

    public void ImageChooser() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 123);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 123 && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            IV.setImageURI(selectedImageUri);
            utils.uploadImage(getActivity(), selectedImageUri);

        }
    }
}