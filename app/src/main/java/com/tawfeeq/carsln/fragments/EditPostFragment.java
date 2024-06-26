package com.tawfeeq.carsln.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.objects.Cars;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.File;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPostFragment extends Fragment {

    FireBaseServices fbs = FireBaseServices.getInstance();
    CarID currentCar;
    Cars newCar;
    EditText Price,BHP,Users,Kilometre,Engine,etNotes;
    Spinner SpinnerGear, SpinnerYear, SpinnerSellLend, SpinnerMan, SpinnerMod, SpinnerColor, SpinnerLocation, SpinnerTestMonth, SpinnerTestYear, SpinnerOwnership;
    ImageView ivFirstPhoto, ivSecondPhoto, ivThirdPhoto, ivFourthPhoto, ivFifthPhoto, Back;
    ImageView dFirst, dSecond, dThird, dFourth, dFifth;
    String FirstPhoto = fbs.getSelectedCar().getPhoto(), SecondPhoto = fbs.getSelectedCar().getSecondphoto(), ThirdPhoto = fbs.getSelectedCar().getThirdPhoto(), FourthPhoto = fbs.getSelectedCar().getFourthPhoto(), FifthPhoto = fbs.getSelectedCar().getFifthPhoto();
    int PressedPhoto;
    Button Apply;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditPostFragment newInstance(String param1, String param2) {
        EditPostFragment fragment = new EditPostFragment();
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
        return inflater.inflate(R.layout.fragment_edit_post, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        if(fbs.getSelectedCar()!=null) currentCar = fbs.getSelectedCar();
        else ((MainActivity) getActivity()).setSavedGoToMarket();
        Apply = getView().findViewById(R.id.btnApply);
        Back = getView().findViewById(R.id.EditListingGoBack);
        ivFirstPhoto = getView().findViewById(R.id.ivFirstCarPhoto);
        ivSecondPhoto = getView().findViewById(R.id.ivSecondCarPhoto);
        ivThirdPhoto =getView().findViewById(R.id.ivThirdCarPhoto);
        ivFourthPhoto = getView().findViewById(R.id.ivFourthCarPhoto);
        ivFifthPhoto = getView().findViewById(R.id.ivFifthCarPhoto);
        dFirst = getView().findViewById(R.id.DeleteFirstPhoto);
        dSecond = getView().findViewById(R.id.DeleteSecondPhoto);
        dThird = getView().findViewById(R.id.DeleteThirdPhoto);
        dFourth = getView().findViewById(R.id.DeleteFourthPhoto);
        dFifth = getView().findViewById(R.id.DeleteFifthPhoto);
        BHP = getView().findViewById(R.id.etBHP);
        Price = getView().findViewById(R.id.etPrice);
        etNotes = getView().findViewById(R.id.etMultiLineNotes);
        Users=getView().findViewById(R.id.etUsers);
        Engine = getView().findViewById(R.id.etEngine);
        Kilometre=getView().findViewById(R.id.etKM);
        SpinnerGear = getView().findViewById(R.id.SpinnerGear);
        SpinnerYear = getView().findViewById(R.id.SpinnerYear);
        SpinnerSellLend= getView().findViewById(R.id.SpinnerSellLend);
        SpinnerMan = getView().findViewById(R.id.SpinnerManufacturer);
        SpinnerMod = getView().findViewById(R.id.SpinnerModel);
        SpinnerTestYear = getView().findViewById(R.id.SpinnerNextTestYear);
        SpinnerTestMonth = getView().findViewById(R.id.SpinnerNextTestMonth);
        SpinnerColor = getView().findViewById(R.id.SpinnerCarColor);
        SpinnerLocation = getView().findViewById(R.id.SpinnerLocationArea);
        SpinnerOwnership = getView().findViewById(R.id.SpinnerOwnership);


        if(!fbs.getCurrentFragment().equals("EditPost")) fbs.setCurrentFragment("EditPost");


        if (FirstPhoto == null || FirstPhoto.isEmpty()) {
            ivFirstPhoto.setImageResource(R.drawable.photo_iv_null);
            dFirst.setVisibility(View.INVISIBLE);
        } else {
            Glide.with(getActivity()).load(FirstPhoto).into(ivFirstPhoto);
        }

        if (SecondPhoto == null || SecondPhoto.isEmpty()) {
            ivSecondPhoto.setImageResource(R.drawable.photo_iv_null);
            dSecond.setVisibility(View.INVISIBLE);
        } else {
            Glide.with(getActivity()).load(SecondPhoto).into(ivSecondPhoto);
        }

        if (ThirdPhoto == null || ThirdPhoto.isEmpty()) {
            ivThirdPhoto.setImageResource(R.drawable.photo_iv_null);
            dThird.setVisibility(View.INVISIBLE);
        } else {
            Glide.with(getActivity()).load(ThirdPhoto).into(ivThirdPhoto);
        }

        if (FourthPhoto == null || FourthPhoto.isEmpty()) {
            ivFourthPhoto.setImageResource(R.drawable.photo_iv_null);
            dFourth.setVisibility(View.INVISIBLE);
        } else {
            Glide.with(getActivity()).load(FourthPhoto).into(ivFourthPhoto);
        }

        if (FifthPhoto == null || FifthPhoto.isEmpty()) {
            ivFifthPhoto.setImageResource(R.drawable.photo_iv_null);
            dFifth.setVisibility(View.INVISIBLE);
        } else {
            Glide.with(getActivity()).load(FifthPhoto).into(ivFifthPhoto);
        }


        BHP.setText(Integer.toString(currentCar.getBHP()));
        Price.setText(Integer.toString(currentCar.getPrice()));
        etNotes.setText(currentCar.getNotes());
        Users.setText(Integer.toString(currentCar.getUsers()));
        Engine.setText(currentCar.getEngine());
        Kilometre.setText(Integer.toString(currentCar.getKilometre()));


        if(SpinnerGear.getSelectedItem()==null) {
            String[] GearList = {"Select Gear Type", "Automatic", "Manual", "PDK", "DCT", "CVT", "SAT", "iManual"};
            ArrayAdapter<String> GearAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, GearList);
            GearAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
            SpinnerGear.setAdapter(GearAdapter);

            List<String> select = Arrays.asList(GearList);
            int n = select.indexOf(currentCar.getTransmission());
            SpinnerGear.setSelection(n);
        }


        int CurrentYear = Year.now().getValue();
        String year = String.valueOf(CurrentYear);
        if(SpinnerYear.getSelectedItem()==null) {
            String[] Years = {"Select Model Year", year, String.valueOf(CurrentYear-1), String.valueOf(CurrentYear-2), String.valueOf(CurrentYear-3),
                    String.valueOf(CurrentYear-4), String.valueOf(CurrentYear-5), String.valueOf(CurrentYear-6), String.valueOf(CurrentYear-7), String.valueOf(CurrentYear-8), String.valueOf(CurrentYear-9), String.valueOf(CurrentYear-10), String.valueOf(CurrentYear-11), String.valueOf(CurrentYear-12), String.valueOf(CurrentYear-13),
                    String.valueOf(CurrentYear-14), String.valueOf(CurrentYear-15), String.valueOf(CurrentYear-16), String.valueOf(CurrentYear-17), String.valueOf(CurrentYear-18), String.valueOf(CurrentYear-19), String.valueOf(CurrentYear-20), String.valueOf(CurrentYear-21), String.valueOf(CurrentYear-22), String.valueOf(CurrentYear-23), String.valueOf(CurrentYear-24),
                    String.valueOf(CurrentYear-25), String.valueOf(CurrentYear-26), String.valueOf(CurrentYear-27), String.valueOf(CurrentYear-28), String.valueOf(CurrentYear-29), String.valueOf(CurrentYear-30), String.valueOf(CurrentYear-31), String.valueOf(CurrentYear-32), String.valueOf(CurrentYear-33), String.valueOf(CurrentYear-34), String.valueOf(CurrentYear-35), String.valueOf(CurrentYear-36)};
            ArrayAdapter<String> YearsAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, Years);
            YearsAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
            SpinnerYear.setAdapter(YearsAdapter);

            List<String> select = Arrays.asList(Years);
            int n = select.indexOf(String.valueOf(currentCar.getYear()));
            SpinnerYear.setSelection(n);
        }


        if(SpinnerTestYear.getSelectedItem()==null) {
            String[] TestYear = {"Test Year Until", year, String.valueOf(CurrentYear+1), String.valueOf(CurrentYear+2), String.valueOf(CurrentYear+3), String.valueOf(CurrentYear+4), String.valueOf(CurrentYear+5)};
            ArrayAdapter<String> TestYearAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, TestYear);
            TestYearAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
            SpinnerTestYear.setAdapter(TestYearAdapter);

            List<String> select = Arrays.asList(TestYear);
            int n = select.indexOf(currentCar.getNextTest().split("/")[1]);
            SpinnerTestYear.setSelection(n);
        }


        if(SpinnerTestMonth.getSelectedItem()==null) {
            String[] TestMonth = {"Test Month Until", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
            ArrayAdapter<String> TestMonthAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, TestMonth);
            TestMonthAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
            SpinnerTestMonth.setAdapter(TestMonthAdapter);

            List<String> select = Arrays.asList(TestMonth);
            int n = select.indexOf(currentCar.getNextTest().split("/")[0]);
            SpinnerTestMonth.setSelection(n);
        }


        if(SpinnerColor.getSelectedItem()==null) {
            String[] CarColor = {"Select Car Color", "White", "Black", "Dark Grey", "Grey", "Light Grey", "Silver", "Dark Red", "Red", "Light Red", "Dark Green", "Green", "Metallic Green", "Light Green", "Dark Blue", "Blue", "Vibrant Blue", "Light Blue", "Orange", "Yellow", "Bright Yellow", "Brown", "Purple"};
            ArrayAdapter<String> ColorAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, CarColor);
            ColorAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
            SpinnerColor.setAdapter(ColorAdapter);

            List<String> select = Arrays.asList(CarColor);
            int n = select.indexOf(currentCar.getColor());
            SpinnerColor.setSelection(n);
        }


        if(SpinnerLocation.getSelectedItem()==null) {
            String[] Location = {"Select Location District", "Golan", "Galil", "Haifa", "Central", "Tel Aviv", "Jerusalem", "Be'er Sheva", "Central Southern", "Eilat"};
            ArrayAdapter<String> LocationAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, Location);
            LocationAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
            SpinnerLocation.setAdapter(LocationAdapter);

            List<String> select = Arrays.asList(Location);
            int n = select.indexOf(currentCar.getLocation());
            SpinnerLocation.setSelection(n);
        }


        if(SpinnerOwnership.getSelectedItem()==null) {
            String[] Ownership = {"Select Ownership Type", "New Car", "Private", "Rental", "Private Leasing", "Company Leasing", "Taxi"};
            ArrayAdapter<String> OwnershipAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, Ownership);
            OwnershipAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
            SpinnerOwnership.setAdapter(OwnershipAdapter);

            List<String> select = Arrays.asList(Ownership);
            int n = select.indexOf(currentCar.getOwnership());
            SpinnerOwnership.setSelection(n);
        }


        if(SpinnerSellLend.getSelectedItem()==null) {
            String[] HowSellLend = {"Select Offer Type", "Sell the Car", "Lend the Car"};
            ArrayAdapter<String> SellLendAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, HowSellLend);
            SellLendAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
            SpinnerSellLend.setAdapter(SellLendAdapter);

            int n = 0;
            if(currentCar.getSellLend()==true) n=1;
            else n=2;
            SpinnerSellLend.setSelection(n);
        }


        if(SpinnerMan.getSelectedItem()==null) {
            String[] ManList = {"Choose Car Manufacturer", "Audi", "Abarth", "Alfa Romeo", "Aston Martin", "BMW", "Bentley", "Citroen", "Cadillac", "Cupra", "Chevrolet",
                    "Dacia", "Dodge", "Fiat", "Ford", "Ferrari", "Genesis", "GMC", "Honda", "Hyundai", "Infiniti", "Isuzu", "Jeep", "Jaguar", "Kia", "Lamborghini", "Land Rover", "Lexus",
                    "Maserati", "Mazda", "Mini", "Mitsubishi", "Mercedes", "Nissan", "Opel", "Porsche", "Peugeot", "Renault", "Subaru", "Suzuki", "Seat", "Skoda", "Toyota", "Tesla", "Volkswagen", "Volvo"};
            //45 Manufacturers!!
            ArrayAdapter<String> ManAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ManList);
            ManAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
            SpinnerMan.setAdapter(ManAdapter);

            List<String> select = Arrays.asList(ManList);
            int n = select.indexOf(currentCar.getManufacturer());
            SpinnerMan.setSelection(n);
        }


        // Here is the Whole list of Models For Each Car Manufacturer (45 lists and the No Manufacturer List)


        String [] ModelNon = {"Choose Car Model"};

        String [] ModelAudi = { "100", "80", "A1", "A3", "A4", "A5", "A6", "A7", "A8", "E-tron", "E-tron GT", "E-tron Q4", "Q2", "Q3", "Q4", "Q5",
                "Q6", "Q7", "Q8", "R8", "RS3 Sedan", "RS3 Hatchback", "RS4", "RS4 Avant", "RS5", "RS6", "RS6 Avant", "RS7", "RSQ3", "RSQ8", "S3", "S4", "S5", "S6", "S7", "S8", "SQ5", "SQ7", "SQ8", "TT", "TT RS"};

        String [] ModelAbarth = { "124 Spider", "500", "500c", "595", "595c"};

        String [] ModelAlfa = { "145", "146", "147", "156", "159", "164", "166", "33", "75", "90", "4C", "4C Spider", "8C", "8C Spider", "GT", "GTV", "Giulia", "Giulia Quadrifoglio",
                "Giulietta", "brera", "Tonale", "MiTO", "Stelvio", "Spider"};

        String [] ModelAston = { "DB12", "DB11", "DB9", "DBS", "DBX", "Vantage", "Vanquish", "Rapide"};

        String [] ModelBMW = { "1 Series",  "2 Series", "3 Series", "3 Series Convertible", "4 Series", "5 Series",  "6 Series", "7 Series", "8 Series", "i3", "iX3", "i4", "i7", "i8", "iX", "1 Series M", "M2", "M3", "M4", "M5", "M6", "M8",
                "M2 Competition", "M3 Competition", "M4 Competition", "M5 Competition", "M6 Competition", "M8 Competition", "X1", "X2", "X3", "X4", "X5", "X6", "X6M", "X7", "Z3", "Z4"};

        String [] ModelBentley = { "BE53", "Bentayga", "Continental GT", "Flying Spur"};

        String [] ModelCitroen = { "AX", "C Elysee", "C Zero", "C1", "C2", "C3", "C3 Aircross", "C4", "C4 X", "C5", "C6", "C8", "CX", "DS3", "DS3 Convertible", "DS4", "DS5", "XM", "ZX",
                "BX", "C15", "Berlingo", "Berlingo Electric", "Jumper"};

        String [] ModelCadillac = { "ATS", "CT4", "CT5", "CT6", "CTS", "DTS", "SRX", "STS", "XLR", "XT4", "XT5", "XT6", "XTS", "Escalade"};

        String [] ModelCupra = { "Ateca", "Leon", "Formentor", "Born"};

        String [] ModelChevrolet = { "Optra", "Orlando", "Impala", "Uplander", "Equinox", "Bolt", "Blaze", "Volt", "Tahoe", "Traverse", "Trax", "Trailblazer", "Malibu", "Suburban",
                "Sonic", "Spark", "Camaro", "Camaro ZL1", "Camaro ZL1 1LE", "Corvette", "Corvette ZO6", "Corvette ZL1", "Cruze", "Nevada", "Savana", "Silverado 1500", "Silverado 2500", "Silverado 3500",
                "Colorado", "Colorado ZR2"};

        String [] ModelDacia = { "Duster", "Jogger", "Logan MCV", "Lodgy", "Sandero", "Sandero Stepway", "Spring", "Dokker"};

        String [] ModelDodge = { "Avenger", "Journey", "Durango", "Durango SRT", "Nitro", "Challenger", "Challenger Hellcat", "Challenger Demon", "Charger", "Charger Hellcat", "Caliber",
                "Dart", "Viper", "SRT Viper", "SRT Viper GTS", "Ram", "Ram TRX", "Ram 1500", "Ram 2500", "Ram 3500"};

        String [] ModelFiat = { "500", "500C", "500L", "500X", "Bravo", "Tipo", "Croma", "Linea", "Multipla", "Ponto", "Panda", "Doblo", "Ducato", "Scudo", "QUBO"};

        String [] ModelFord = { "Edge", "S Max", "Escort", "Escape", "Expedition", "Explorer", "Bronco", "Bronco R", "Galaxy", "Taurus", "Lincoln MKC", "Lincoln MKS", "Lincoln MKT", "Lincoln MKZ",
                "Mondeo", "Mustang", "Mustang GT", "Mustang SVT Cobra", "Mustang Mach E", "Mustang Shelby GT500", "Mustang Shelby GT350", "Puma", "Focus", "Fiesta", "Fusion", "Kuga", "Excrusion", "F-150", "F-150 Lightning",
                "F-250", "F-350", "GT", "Transit", "Maverick", "Connect", "Ranger"};

        String [] ModelFerrari = { "296 GTB", "Roma", "F8 Tributo", "SF90 Stradale", "488 Pista", "488 GTB", "Portofino", "F12tdf", "GTC4Lusso", "812 Superfast", "458 Speciale", "458 Spider", "458 Italia",
                "LaFerrari", "F12berlinetta", "599 GTO", "599 GTB", "F430", "360 Modena"};

        String [] ModelGenesis = { "G70", "G80", "G90", "GLS", "GV60", "GV70", "GV80"};

        String [] ModelGMC = { "Acadia", "Jimmy", "Yukon", "Yukon Denali", "Suburban", "Hummer EV", "Envoy", "Vandura", "Savana", "Sonoma", "Sierra", "Sierra 4x4", "Safari", "Rally"};

        String [] ModelHonda = { "Acura Integra", "Acura NSX", "Acura MDX", "Shuttle", "CR-V", "CR-Z", "FR-V", "HR-V", "S2000", "Odyssey", "Accord", "Insight","Jazz", "Jazz Hybrid", "Legend", "Stream",
                "Civic Type-R", "Civic Hybrid", "Civic Hatchback", "Civic Sedan", "Civic Station", "Pilot", "Prelude"};

        String [] ModelHyundai = { "i10", "i20", "i20N", "i25", "i30", "i30CW", "i30N", "i35", "i40", "iX20", "iX35", "iX55", "Ioniq",  "Ioniq 5", "Ioniq 6", "Elantra", "Accent", "Bayon", "Getz", "Veloster", "Veloster N",
                "Venue", "Terracan", "Tucson", "Sonata", "Sonata Hybrid", "Sonata N", "Santa Fe", "Palisade", "Kona", "Kona EV", "Coupe", "H1", "H100", "H100 Truck", "i800", "Staria"};

        String [] ModelInfiniti = { "FX30", "FX35", "FX50", "G37", "Q30", "Q50", "Q60", "Q70", "QX30", "QX50", "QX55", "QX56", "QX60", "QX70"};

        String [] ModelIsuzu = { "D-Max 2x4", "D-Max 4x4", "Sumo"};

        String [] ModelJeep = { "Gladiator", "Wagoneer", "Grand Wagoneer", "Cherokee", "Grand Cherokee", "Commander", "Compass", "Renegade", "Wrangler", "Wrangler Rubicon"};

        String [] ModelJaguar = { "E-Pace", "F-Pace", "F-Type", "I-Pace", "S-Pace", "X-Pace", "XE", "XF", "XJ", "XJ6", "XJ8", "XJR", "XK", "XKR"};

        String [] ModelKia = { "EV6", "K900", "XCEED", "Optima", "Telluride", "Magentis", "Niro", "Niro EV", "Niro PHEV", "Niro Hybrid", "Niro Plus", "Soul", "Sorento", "Stonic", "Stinger", "CEED", "ProCeed", "Seltos", "Sportage", "Cerato", "Forte", "Picanto",
                "Carens", "Cadenza", "Carnival", "Rio", "K2500", "Borrego", "Creta"};

        String [] ModelLamborghini = { "Diablo", "Diablo SV", "Gallardo", "Gallardo Superleggera", "Gallardo Spyder", "Murcielago", "Murcielago SV", "Urus", "Aventador", "Aventador Roadster", "Aventador SV", "Aventador SVJ", "Huracan", "Huracan Spyder", "Huracan Performante", "Huracan Evo", "Huracan STO", "Huracan Tecnica", "Sian"};

        String [] ModelLandRover = { "Discovery 1", "Discovery 2", "Discovery 3", "Discovery 4", "Discovery 5", "Discovery Sport", "Defender", "Freelander 1", "Freelander 2", "Range Rover", "Evoque", "Velar", "Sport", "Sport SVR"};

        String [] ModelLexus = { "CS", "CT200H", "ES300H", "GS250", "GS300", "GS300H", "GS450H", "GX460", "IS250", "IS300", "IS300H", "LC500", "LS400", "LS430", "LS460", "LS500", "LS600HL", "LX570", "LX600", "NX", "RC", "RCF", "LFA", "RX200t", "RX300", "RX330", "RX350", "RX400H", "RX450H", "RX500H", "RZ450e", "SC430", "UX", "GX470"};

        String [] ModelMaserati = { "MC20", "Ghibli", "Granturismo", "Grancabrio", "Gransport", "Grecale", "Levante", "Quattroporte"};

        String [] ModelMazda = { "Mazda2", "Mazda2 Demio", "Mazda3", "323", "Mazda5", "Mazda6", "626", "CX-3", "CX-30", "CX-5", "CX-50", "CX-60", "CX-07", "CX-9", "CX-90", "MPV", "MX-3", "MX-30", "MX-5", "MX-6", "RX-7", "RX-8", "Demio", "Tribute", "Premacy", "B2500 2x4", "B2500 4x4", "BT50 2x4", "BT50 4x4"};

        String [] ModelMini = { "JCW", "ONE", "Paceman", "Countryman", "Coupe", "Cooper", "Cooper Cabriolet", "Clubman", "Roadster"};

        String [] ModelMitsubishi = { "ASX", "GT3000", "I-MIEV", "Outlander", "Outlander PHEV", "Attrage", "Eclipse", "Eclipse Cross", "Galant", "Grandis", "Lancer", "Lancer EVO", "Lancer Sportback", "Super Lancer", "Montero Sport", "Space Star", "Pajero", "Colt", "Hunter 2x4", "Hunter 4x4",
                "L300", "L400", "Triton 2x4", "Triton 4x4", "Magnum 2x4", "Magnum 4x4", "Space Gear", "Canter"};

        String [] ModelMercedes = { "A-Class", "A45 AMG", "B-Class", "C-Class", "CE", "CL", "CLA", "CLC", "CLK", "CLS", "E-Class", "E63 AMG", "EQA", "EQA250 Plus", "EQB", "EQC", "EQE", "EQS", "EQV", "G-Class", "GL-Class", "GLA", "GLB", "GLC", "GLC Coupe", "GLE", "GLE-Coupe", "GLK-Class", "GLS-Class", "AMG GT Sedan", "AMG GT Coupe", "AMG GT S", "AMG GT R", "ML-Class", "R-Class",
                "S-Class", "S-Class Coupe", "S-Class Cabrio", "SE", "SL", "SLC", "SLK", "SLK 55 AMG", "SLS", "CL65 AMG", "C63 AMG", "V-Class", "Metris", "313", "316", "319", "413", "X-Class", "Atego", "Viano", "Vito", "Vito Electric", "Unimog", "Citan", "Sprinter"};

        String [] ModelNissan = { "370Z", "Z350", "GT-R", "SX200", "Altima", "Almera", "X Trail", "Armada", "Juke", "Tiida", "Terrano", "Leaf", "Murano", "Micra" , "Maxima", "Note", "Sunny", "Sentra", "Pathfinder", "Pulsar", "Patrol", "Primera", "Kavak", "Quest", "Qashqai", "Qashqai +2", "NV200", "Xterra", "Winner 2x4", "Winner 4x4",
                "Navara", "Frontier", "Largo", "Serena", "Cabstar"};

        String [] ModelOpel = { "Adam", "Omega", "Insignia", "Astra", "Grandland", "Vectra", "Zafira", "Tigra", "Mokka", "Mokka X", "Meriva", "Frontera", "Corsa", "Cascada", "Crossland X", "Vivaro", "Movano", "Combo"};

        String [] ModelPorsche = { "718 Boxster", "718 Cayman", "Cayman GT4", "Cayman GT4 RS", "Carrera GT", "918 Spyder", "911 Carrera", "911 Carrera 4", "911 Carrera T", "911 Carrera S", "911 Carrera 4S", "911 Carrera GTS", "911 Carrera 4 GTS", "911 Cabriolet", "911 Speedster", "911 Sport Classic", "911 S/T", "911 Turbo", "911 Turbo S", "911 Targa", "911 Dakar", "911 GT2 RS", "911 GT3", "911 GT3 RS",
                "Taycan", "Macan", "Macan GTS", "Panamera", "Panamera Turbo", "Cayenne", "Cayenne Turbo"};

        String [] ModelPeugeot = { "106", "107", "108", "2008", "205", "206", "206+", "206CC", "207", "208", "208 GTI", "3008", "301", "305", "306", "307", "307CC", "308", "308CC", "309", "405", "406", "407", "408", "5008",
                "504", "505", "508", "605", "607", "E-Rifter", "RCZ", "E-2008", "E-208", "iON", "Traveller", "204", "J5", "J9", "Expert", "Boxer", "Partner", "Landtrek", "Bipper"};

        String [] ModelRenault = { "Austral", "Espace", "Arkana", "Arkana Hybrid", "Grand Scenic", "Zoe", "Twingo", "Laguna", "Logan", "Latitude", "Megane", "Megane Grand Coupe", "Megane E-Tech", "Megane R.S.", "Symbol", "Sandero", "Scenic", "Fluence", "Fluence Z.E.", "Kadjar", "Koleos", "Clio", "Clio R.S.", "Captur", "Expert", "Trafic",
                "Master", "Kangoo"};

        String [] ModelSubaru = { "B3 Sedan", "B3 Hatchback", "B4", "B9", "BRZ", "SVX", "XV", "Outback", "Impreza", "Impreza WRX", "Impreza WRX STI", "Leone", "Levorg", "Legacy", "Forester", "Crosstrek", "E10", "E12", "GLF", "Baja", "Rex"};

        String [] ModelSuzuki = { "SX4", "SX4 Crossover", "XL7", "Ignis", "Alto", "Baleno", "Jimny", "Grand Vitara", "Wagon", "Vitara", "Liana", "Maruti", "Swift", "Celerio", "Samurai", "Splash", "X90", "Equator", "Carry"};

        String [] ModelSeat = { "Ateca", "Ibiza", "Ibiza FR", "Alhambra", "Altea", "Altea XL", "Arona", "Arona FR", "Toledo", "Tarraco", "Leon", "Leon FR", "Mii", "Cordoba"};

        String [] ModelSkoda = { "Octavia", "Octavia RS", "Octavia Space", "Enyaq", "Yeti", "Superb", "Citigo", "Scala", "Fabia", "Fabia Space", "Kamiq", "Karoq", "kodiaq", "Rapid", "Rapid Spaceback", "Roomster", "Enyaq IV"};

        String [] ModelToyota = { "4Runner", "BZ4X", "C-HR", "FJ Cruiser", "GR86", "GT86", "IQ", "MR2", "RAV4", "RAV4 Hybrid", "Avalon", "Avalon Hybrid", "Avensis", "Auris", "Auris Hybrid", "AYGO", "AYGO X", "Grand Highlander", "Highlander", "Venza", "Avanza", "Verso",
                "Yaris", "Yaris Hybrid", "GR Yaris", "Land Cruiser", "Supra", "Starlet", "Sienna", "Sienna Hybrid", "Celica", "Space Verso", "Sequoia", "Previa", "Prius","Prius Hybrid", "Camry", "Corolla", "Corolla Hybrid", "Corolla Cross", "Hiace", "Hilux 2x4", "Hilux 4x4", "Tacoma", "Tundra", "Proace", "Sette"};

        String [] ModelTesla = { "Model S", "Model 3", "Model X", "Model Y", "Roadster"};

        String [] ModelVolkswagen = { "ID.5", "ID3", "ID4", "ID6 CROZZ", "T-Roc", "T-Cross", "Atlas", "Eos", "UP", "Arteon", "Bora", "Jetta", "Golf", "Golf GTE", "Golf GTI", "Golf R", "Golf Sportsvan", "Vento","Beetle", "Touran", "Tiguan", "Passat", "Passat Estate",
                "Polo", "Polo GTI", "Phaeton", "Corrado", "Scirocco", "ID.Buzz", "LT", "LT35", "Amarok", "Transporter", "Multivan", "Caddy", "Caravelle", "Crafter", "Shuttle"};

        String [] ModelVolvo = { "240", "244", "740", "744", "745", "760", "850", "854", "855", "940", "944", "945", "960", "C30", "C40", "C70", "CX11", "S40", "S60", "S70", "S80", "S90", "V40", "V40 Cross Country", "V50",
                "V60", "V70", "XC40", "XC60", "XC70", "XC90", "245", "264", "340", "345"};


        // Ends .......


        SpinnerMan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                String item = adapterView.getSelectedItem().toString();
                String[] selectedModel = null;

                // For Each Manufacturer Selected, The Models get a new String Array of Sold/Available Models of that Manufacturer!
                if(item.equals("Choose Car Manufacturer")){

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelNon);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelNon;
                }
                if(item.equals("Audi")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelAudi);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelAudi;
                }
                if(item.equals("Abarth")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelAbarth);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelAbarth;
                }
                if(item.equals("Alfa Romeo")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelAlfa);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelAlfa;
                }
                if(item.equals("Aston Martin")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelAston);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelAston;
                }
                if(item.equals("BMW")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelBMW);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelBMW;
                }
                if(item.equals("Bentley")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelBentley);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelBentley;
                }
                if(item.equals("Citroen")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelCitroen);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelCitroen;
                }
                if(item.equals("Cadillac")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelCadillac);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelCadillac;
                }
                if(item.equals("Cupra")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelCupra);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelCupra;
                }
                if(item.equals("Chevrolet")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelChevrolet);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelChevrolet;
                }
                if(item.equals("Dacia")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelDacia);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelDacia;
                }
                if(item.equals("Dodge")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelDodge);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelDodge;
                }
                if(item.equals("Fiat")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelFiat);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelFiat;
                }
                if(item.equals("Ford")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelFord);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelFord;
                }
                if(item.equals("Ferrari")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelFerrari);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelFerrari;
                }
                if(item.equals("Genesis")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelGenesis);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelGenesis;
                }
                if(item.equals("GMC")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelGMC);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelGMC;
                }
                if(item.equals("Honda")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelHonda);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelHonda;
                }
                if(item.equals("Hyundai")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelHyundai);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelHyundai;
                }
                if(item.equals("Infiniti")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelInfiniti);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelInfiniti;
                }
                if(item.equals("Isuzu")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelIsuzu);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelIsuzu;
                }
                if(item.equals("Jeep")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelJeep);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelJeep;
                }
                if(item.equals("Jaguar")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelJaguar);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelJaguar;
                }
                if(item.equals("Kia")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelKia);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelKia;
                }
                if(item.equals("Lamborghini")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelLamborghini);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelLamborghini;
                }
                if(item.equals("Land Rover")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelLandRover);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelLandRover;
                }
                if(item.equals("Lexus")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelLexus);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelLexus;
                }
                if(item.equals("Maserati")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelMaserati);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelMaserati;
                }
                if(item.equals("Mazda")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelMazda);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelMazda;
                }
                if(item.equals("Mini")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelMini);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelMini;
                }
                if(item.equals("Mitsubishi")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelMitsubishi);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelMitsubishi;
                }
                if(item.equals("Mercedes")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelMercedes);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelMercedes;
                }
                if(item.equals("Nissan")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelNissan);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelNissan;
                }
                if(item.equals("Opel")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelOpel);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelOpel;
                }
                if(item.equals("Porsche")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelPorsche);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelPorsche;
                }
                if(item.equals("Peugeot")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelPeugeot);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelPeugeot;
                }
                if(item.equals("Renault")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelRenault);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelRenault;
                }
                if(item.equals("Subaru")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelSubaru);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelSubaru;
                }
                if(item.equals("Suzuki")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelSuzuki);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelSuzuki;
                }
                if(item.equals("Seat")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelSeat);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelSeat;
                }
                if(item.equals("Skoda")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelSkoda);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelSkoda;
                }
                if(item.equals("Toyota")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelToyota);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelToyota;
                }
                if(item.equals("Tesla")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelTesla);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelTesla;
                }
                if(item.equals("Volkswagen")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelVolkswagen);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelVolkswagen;
                }
                if(item.equals("Volvo")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, ModelVolvo);
                    ModAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);

                    selectedModel = ModelVolvo;
                }

                List<String> select = Arrays.asList(selectedModel);
                int n = select.indexOf(currentCar.getModel());
                SpinnerMod.setSelection(n);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Does Nothing As Nothing is Selected
            }
        });


        ivFirstPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PressedPhoto = 1;
                ImageChooser();
            }
        });

        ivSecondPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(FirstPhoto == null || FirstPhoto.isEmpty())) {

                    PressedPhoto = 2;
                    ImageChooser();
                }
            }
        });

        ivThirdPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(SecondPhoto == null || SecondPhoto.isEmpty())) {

                    PressedPhoto = 3;
                    ImageChooser();
                }
            }
        });

        ivFourthPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(ThirdPhoto == null || ThirdPhoto.isEmpty())) {

                    PressedPhoto = 4;
                    ImageChooser();
                }
            }
        });

        ivFifthPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(FourthPhoto == null || FourthPhoto.isEmpty())) {

                    PressedPhoto = 5;
                    ImageChooser();
                }
            }
        });

        dFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SecondPhoto == null || SecondPhoto.isEmpty()) {

                    FirstPhoto = "";
                    ivFirstPhoto.setImageResource(R.drawable.photo_iv_null);
                    dFirst.setVisibility(View.INVISIBLE);

                } else {

                    Toast.makeText(getActivity(), "Delete Previous Photos Firstly", Toast.LENGTH_SHORT).show();

                }
            }
        });

        dSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ThirdPhoto == null || ThirdPhoto.isEmpty()) {

                    SecondPhoto = "";
                    ivSecondPhoto.setImageResource(R.drawable.photo_iv_null);
                    dSecond.setVisibility(View.INVISIBLE);

                } else {

                    Toast.makeText(getActivity(), "Delete Previous Photos Firstly", Toast.LENGTH_SHORT).show();

                }
            }
        });

        dThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(FourthPhoto == null || FourthPhoto.isEmpty()) {

                    ThirdPhoto = "";
                    ivThirdPhoto.setImageResource(R.drawable.photo_iv_null);
                    dThird.setVisibility(View.INVISIBLE);

                } else {

                    Toast.makeText(getActivity(), "Delete Previous Photos Firstly", Toast.LENGTH_SHORT).show();

                }
            }
        });

        dFourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(FifthPhoto == null || FifthPhoto.isEmpty()) {

                    FourthPhoto = "";
                    ivFourthPhoto.setImageResource(R.drawable.photo_iv_null);
                    dFourth.setVisibility(View.INVISIBLE);

                } else {

                    Toast.makeText(getActivity(), "Delete Previous Photos Firstly", Toast.LENGTH_SHORT).show();

                }
            }
        });

        dFifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FifthPhoto = "";
                ivFifthPhoto.setImageResource(R.drawable.photo_iv_null);
                dFifth.setVisibility(View.INVISIBLE);

            }
        });



        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.post_alert);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog.setCancelable(true);

        Button btnClose = dialog.findViewById(R.id.btnConfirmClose);
        Button btnCancel = dialog.findViewById(R.id.btnCancelClose);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayoutMain, new DetailedFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });



        Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // checks the User's Internet Connection
                if(!isConnected()) {
                    // No Connection Dialog!
                    Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_no_network);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
                    dialog.setCancelable(true);
                    dialog.show();

                    Button btn = dialog.findViewById(R.id.btnOK);

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    return;
                }

                Dialog loading = new Dialog(getActivity());
                loading.setContentView(R.layout.loading_dialog);
                loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loading.setCancelable(false);
                loading.show();

                String Man= SpinnerMan.getSelectedItem().toString();
                String Mod= SpinnerMod.getSelectedItem().toString();
                String HP= BHP.getText().toString();
                String prc= Price.getText().toString();
                String User = Users.getText().toString();
                String engine = Engine.getText().toString();
                String Kilo =Kilometre.getText().toString();
                String notes = etNotes.getText().toString();
                String transmission = SpinnerGear.getSelectedItem().toString();
                String year = SpinnerYear.getSelectedItem().toString();
                String SellLend = SpinnerSellLend.getSelectedItem().toString();
                String Color = SpinnerColor.getSelectedItem().toString();
                String area = SpinnerLocation.getSelectedItem().toString();
                String testyear = SpinnerTestYear.getSelectedItem().toString();
                String testmonth = SpinnerTestMonth.getSelectedItem().toString();
                String ownership = SpinnerOwnership.getSelectedItem().toString();
                boolean selllend;
                if(SellLend.equals("Sell the Car")) selllend=true;
                else selllend=false;


                if(SellLend.equals("Select Offer Type")||Man.equals("Choose Car Manufacturer")||Mod.equals("Choose Car Model")||HP.trim().isEmpty()||prc.trim().isEmpty()||notes.trim().isEmpty()||year.equals("Select Model Year")|| transmission.equals("Select Gear Type") ||User.trim().isEmpty()||engine.trim().isEmpty()||Kilo.trim().isEmpty()||Color.equals("Select Car Color")||area.equals("Select Location District")||testyear.equals("Test Year Until")||testmonth.equals("Test Month Until")||ownership.equals("Select Ownership Type")) {
                    Toast.makeText(getActivity(), "Some Fields Are Missing", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                    return;
                }
                if(HP.length()>4){
                    Toast.makeText(getActivity(), "Horse Power Field is too Much", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                    return;
                }
                if(User.length()>3){
                    Toast.makeText(getActivity(), "Owners Field is too Much", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                    return;
                }
                if(engine.length()>33){
                    Toast.makeText(getActivity(), "Engine Field Characters have Surpassed their limit", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                    return;
                }
                if(Kilo.length()>7){
                    Toast.makeText(getActivity(), "Kilometre Field is too Much", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                    return;
                }
                if(notes.length()>300){
                    Toast.makeText(getActivity(), "Notes Field Characters are over 300", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                    return;
                }

                String test = testmonth + "/" + testyear;


                int power= Integer.parseInt(HP);
                int price=Integer.parseInt(prc);
                int Yahr= Integer.parseInt(year);
                int userhands= Integer.parseInt(User);
                int KM= Integer.parseInt(Kilo);

                newCar = new Cars(selllend,fbs.getAuth().getCurrentUser().getEmail(),Man,Mod,power,price,Yahr,transmission,engine,KM,userhands,Color,area,test,FirstPhoto,SecondPhoto,ThirdPhoto,FourthPhoto,FifthPhoto,notes,ownership);
                newCar.setTimestamp(currentCar.getTimestamp());

                fbs.getStore().collection("MarketPlace").document(currentCar.getId()).set(newCar).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getActivity(), "Changes have been Applied to Your Car Listing", Toast.LENGTH_SHORT).show();
                        fbs.setMarketList(null);
                        setNavigationCarsMarket();
                        setNavigationBarVisible();
                        loading.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to Apply Changes", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });

            }
        });

    }

    private void setNavigationCarsMarket() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.market);
    }

    private void setNavigationBarVisible(){
        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }

    private boolean isConnected(){
        return ((MainActivity) getActivity()).isNetworkAvailable();
    }

    public void ImageChooser() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 123);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = null;

            if(data!=null) resultUri = UCrop.getOutput(data);

            if(resultUri!=null) {

                if(FirstPhoto == null || PressedPhoto==1){

                    ivFirstPhoto.setImageURI(resultUri);
                    UploadFirstPhoto(resultUri);

                }else if (SecondPhoto == null || PressedPhoto==2){

                    ivSecondPhoto.setImageURI(resultUri);
                    UploadSecondPhoto(resultUri);

                }else if(ThirdPhoto == null || PressedPhoto==3){

                    ivThirdPhoto.setImageURI(resultUri);
                    UploadThirdPhoto(resultUri);

                }else if(FourthPhoto == null || PressedPhoto==4){

                    ivFourthPhoto.setImageURI(resultUri);
                    UploadFourthPhoto(resultUri);

                }else if(FifthPhoto == null || PressedPhoto==5){

                    ivFifthPhoto.setImageURI(resultUri);
                    UploadFifthPhoto(resultUri);

                }

            }

        } else if(resultCode == UCrop.RESULT_ERROR) {
            // Close the UCrop.
        }
        if (requestCode == 123 && resultCode == getActivity().RESULT_OK) {

            Uri selectedImageUri = data.getData();
            startCropActivity(selectedImageUri);

        }

    }

    private void startCropActivity(Uri sourceUri) {

        Uri destinationUri = Uri.fromFile(new File(getActivity().getCacheDir(), UUID.randomUUID().toString()));

        // Multiple Aspect Ratios for each Selected Pictures!! (Fixed the Photos Flipping)....
        UCrop.Options options = new UCrop.Options();
        AspectRatio defaultaspectRatio = new AspectRatio("4/3",4,3);
        AspectRatio secondaryaspectRation = new AspectRatio("16/9",16,9);
        AspectRatio fifthaspectRation = new AspectRatio("3/4", 3, 4);
        AspectRatio FourthaspectRation = new AspectRatio("1/1", 1, 1);
        options.setAspectRatioOptions(0, defaultaspectRatio, fifthaspectRation, secondaryaspectRation, FourthaspectRation);

        UCrop uCrop = UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(2000, 2000);
        uCrop.withOptions(options);
        uCrop.start(getContext(), this, UCrop.REQUEST_CROP);

    }

    private void UploadFirstPhoto(Uri selectedImageUri) {

        if (selectedImageUri != null) {

            Dialog loading = new Dialog(getActivity());
            loading.setContentView(R.layout.loading_dialog);
            loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loading.setCancelable(false);
            loading.show();


            String imageStr = "images/" + UUID.randomUUID() + ".jpg"; //+ selectedImageUri.getLastPathSegment();
            StorageReference imageRef = fbs.getStorage().getReference().child("images/" + selectedImageUri.getLastPathSegment());

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            FirstPhoto = uri.toString();
                            dFirst.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Utils: uploadImage: ", e.getMessage());
                        }
                    });

                    loading.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading.dismiss();
                    Toast.makeText(getActivity(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Choose an Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void UploadSecondPhoto(Uri selectedImageUri) {

        if (selectedImageUri != null) {

            Dialog loading = new Dialog(getActivity());
            loading.setContentView(R.layout.loading_dialog);
            loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loading.setCancelable(false);
            loading.show();


            String imageStr = "images/" + UUID.randomUUID() + ".jpg"; //+ selectedImageUri.getLastPathSegment();
            StorageReference imageRef = fbs.getStorage().getReference().child("images/" + selectedImageUri.getLastPathSegment());

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            SecondPhoto = uri.toString();
                            dSecond.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Utils: uploadImage: ", e.getMessage());
                        }
                    });

                    loading.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading.dismiss();
                    Toast.makeText(getActivity(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Choose an Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void UploadThirdPhoto(Uri selectedImageUri) {

        if (selectedImageUri != null) {

            Dialog loading = new Dialog(getActivity());
            loading.setContentView(R.layout.loading_dialog);
            loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loading.setCancelable(false);
            loading.show();


            String imageStr = "images/" + UUID.randomUUID() + ".jpg"; //+ selectedImageUri.getLastPathSegment();
            StorageReference imageRef = fbs.getStorage().getReference().child("images/" + selectedImageUri.getLastPathSegment());

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            ThirdPhoto = uri.toString();
                            dThird.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Utils: uploadImage: ", e.getMessage());
                        }
                    });

                    loading.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading.dismiss();
                    Toast.makeText(getActivity(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Choose an Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void UploadFourthPhoto(Uri selectedImageUri) {

        if (selectedImageUri != null) {

            Dialog loading = new Dialog(getActivity());
            loading.setContentView(R.layout.loading_dialog);
            loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loading.setCancelable(false);
            loading.show();


            String imageStr = "images/" + UUID.randomUUID() + ".jpg"; //+ selectedImageUri.getLastPathSegment();
            StorageReference imageRef = fbs.getStorage().getReference().child("images/" + selectedImageUri.getLastPathSegment());

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            FourthPhoto = uri.toString();
                            dFourth.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Utils: uploadImage: ", e.getMessage());
                        }
                    });

                    loading.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading.dismiss();
                    Toast.makeText(getActivity(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Choose an Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void UploadFifthPhoto(Uri selectedImageUri) {

        if (selectedImageUri != null) {

            Dialog loading = new Dialog(getActivity());
            loading.setContentView(R.layout.loading_dialog);
            loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loading.setCancelable(false);
            loading.show();


            String imageStr = "images/" + UUID.randomUUID() + ".jpg"; //+ selectedImageUri.getLastPathSegment();
            StorageReference imageRef = fbs.getStorage().getReference().child("images/" + selectedImageUri.getLastPathSegment());

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            FifthPhoto = uri.toString();
                            dFifth.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Utils: uploadImage: ", e.getMessage());
                        }
                    });

                    loading.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading.dismiss();
                    Toast.makeText(getActivity(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Choose an Image", Toast.LENGTH_SHORT).show();
        }

    }

}