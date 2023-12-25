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
    Spinner SpinnerGear, SpinnerYear, SpinnerSellLend, SpinnerMan, SpinnerMod;
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


        String [] GearList = {"Gear Type", "Automatic", "Manual", "PDK", "DCT", "CVT", "SAT","iManual"};
        ArrayAdapter<String> GearAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, GearList);
        GearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerGear.setAdapter(GearAdapter);



        String [] Years = { "Select Year","2024","2023","2022","2021",
                "2020","2019","2018","2017","2016","2015","2014","2013","2012","2011",
                "2010","2009","2008","2007","2006","2005","2004","2003","2002","2001","2000","1999","1998","1997","1996","1995","1994","1993","1992","1991", "1990" };
        ArrayAdapter<String> YearsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, Years);
        YearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerYear.setAdapter(YearsAdapter);



        String [] HowSellLend = {"What do you want to do with the Car", "Sell the Car" , "Lend the Car" };
        ArrayAdapter<String> SellLendAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, HowSellLend);
        SellLendAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerSellLend.setAdapter(SellLendAdapter);



        String [] ManList = {"Choose Car Manufacturer","Audi","Abarth", "Alfa Romeo", "Aston Martin","BMW", "Bentley", "Citroen", "Cadillac", "Cupra", "Chevrolet",
                "Dacia","Dodge", "Daihatsu","Fiat", "Ford", "GMC", "Genesis", "Honda", "Hyundai","Infiniti","Isuzu","Jeep", "Jaguar", "Kia","Land Rover", "Lexus", "MG",
                "Maserati", "Mini", "Mitsubishi", "Mercedes", "Nissan", "Opel", "Porsche", "Peugeot", "Renault", "Subaru", "Suzuki","Seat", "Skoda", "Toyota", "Tesla", "Volvo"};
        ArrayAdapter<String> ManAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, ManList);
        ManAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerMan.setAdapter(ManAdapter);


        // Here is the Whole list of Models For Each Car Manufacturer

        String [] ModelNon = {"Choose a Manufacturer First"};

        String [] ModelAudi = {"Choose Car Model", "100", "80", "A1", "A3", "A4", "A5", "A6", "A7", "A8", "E-tron", "E-tron GT", "E-tron Q4", "Q2", "Q3", "Q4", "Q5",
                "Q6", "Q7", "Q8", "R8", "RS3", "RS4", "RS5", "RS6", "RS7", "RSQ3", "RSQ8", "S3", "S4", "S5", "S6", "S7", "S8", "SQ5", "SQ7", "SQ8", "TT", "TT RS"};

        String [] ModelAbarth = {"Choose Car Model", "124 Spider", "500", "500c", "595", "595c"};

        String [] ModelAlfa = {"Choose Car Model", "145", "146", "147", "156", "159", "164", "166", "33", "75", "90", "4C", "4C Spider", "8C", "8C Spider", "GT", "GTV", "Giulia", "Giulia Quadrifoglio",
                "Giulietta", "brera", "Tonale", "MiTO", "Stelvio", "Spider"};

        String [] ModelAston = {"Choose Car Model", "DB11", "DB9", "DBS", "DBX", "Vantage", "Vanquish", "Rapide"};


        // Ends ........

        SpinnerMan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                String item = adapterView.getSelectedItem().toString();

                // For Each Manufacturer Selected gets a new String Array of Sold/Available Models
                if(item.equals("Choose Car Manufacturer")){

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, ModelNon);
                    ModAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Audi")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, ModelAudi);
                    ModAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Abarth")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, ModelAbarth);
                    ModAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Alfa Romeo")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, ModelAlfa);
                    ModAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }
                if(item.equals("Aston Martin")) {

                    ArrayAdapter<String> ModAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, ModelAston);
                    ModAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    SpinnerMod.setAdapter(ModAdapter);
                }

                // Complete the Rest Of the Manufacturers (43 of them)....
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
                if(SellLend.equals("What do you want to do with the Car")||Man.equals("Choose Car Manufacturer")||Mod.equals("Choose a Manufacturer First")||Mod.equals("Choose Car Model")||HP.trim().isEmpty()||prc.trim().isEmpty()||year.equals("Select Year")|| transmission.equals("Gear Type") ||User.trim().isEmpty()||engine.trim().isEmpty()||Kilo.trim().isEmpty()) {
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

                if(fbs.getSelectedImageURL()==null) photo ="";
                else photo = fbs.getSelectedImageURL().toString()+".jpg";
                // Sell Lend; True=Sell the Car, False=Lend the Car.
                Cars Add = new Cars(selllend,fbs.getAuth().getCurrentUser().getEmail(),Man,Mod,power,price,Yahr,transmission,engine,KM,userhands,photo);
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