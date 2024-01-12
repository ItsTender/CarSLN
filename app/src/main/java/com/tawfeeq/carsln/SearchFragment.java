package com.tawfeeq.carsln;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    EditText etPriceFrom,etPriceTo, etMan, etMod;
    ImageButton btnSearch, btnResetSearch;
    RecyclerView rc;
    FireBaseServices fbs;
    ArrayList<CarID> search, lstRst;
    CarsAdapter Adapter;
    RangeSlider rngPrice, rngKilo, rngYear;
    Spinner SellLend;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs=FireBaseServices.getInstance();
        etMan=getView().findViewById(R.id.etSearchMan);
        etMod=getView().findViewById(R.id.etSearchMod);
        btnSearch=getView().findViewById(R.id.btnSearch);
        btnResetSearch=getView().findViewById(R.id.btnResetSearch);
        rngPrice =getView().findViewById(R.id.rngPrice);
        rngKilo = getView().findViewById(R.id.rngKilometre);
        rngYear = getView().findViewById(R.id.rngYears);
        SellLend = getView().findViewById(R.id.Spinnersellingtype);


        String [] HowSellLend = {"Any Offer Type", "Purchase a Car" , "Rent a Car"};
        ArrayAdapter<String> SellLendAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, HowSellLend);
        SellLendAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        SellLend.setAdapter(SellLendAdapter);


        lstRst=new ArrayList<CarID>();


        fbs.getStore().collection("MarketPlace").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){

                    CarID car = dataSnapshot.toObject(CarID.class);
                    car.setCarPhoto(dataSnapshot.getString("photo"));
                    car.setId(dataSnapshot.getId());
                    lstRst.add(car);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Couldn't Retrieve Info, Please Try Again Later!", Toast.LENGTH_SHORT).show();
            }
        });


        btnResetSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fbs.setCarList(lstRst);

                etMan.setText("");
                etMod.setText("");


                List<Float> price = rngPrice.getValues();
                price.set(0, rngPrice.getValueFrom());
                price.set(1, rngPrice.getValueTo());
                rngPrice.setValues(price);


                List<Float> kilo = rngKilo.getValues();
                kilo.set(0, rngKilo.getValueFrom());
                kilo.set(1, rngKilo.getValueTo());
                rngKilo.setValues(kilo);


                List<Float> years = rngYear.getValues();
                years.set(0, rngYear.getValueFrom());
                years.set(1, rngYear.getValueTo());
                rngYear.setValues(years);

                String [] HowSellLend = {"Any Offer Type", "Purchase a Car" , "Rent a Car"};
                ArrayAdapter<String> SellLendAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, HowSellLend);
                SellLendAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                SellLend.setAdapter(SellLendAdapter);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                search=new ArrayList<CarID>();

                String Man=etMan.getText().toString();
                String Mod=etMod.getText().toString();

                List<Float> price = rngPrice.getValues();
                Float PriceFrom=price.get(0);
                Float[] PriceTo = new Float[1];
                PriceTo[0]  = price.get(1);
                if(PriceTo[0] == 200000) PriceTo[0] = Float.MAX_VALUE;

                List<Float> kilo = rngKilo.getValues();
                Float kiloFrom=kilo.get(0);
                Float[] kiloTo = new Float[1];
                kiloTo[0]  = kilo.get(1);
                if(kiloTo[0] == 200000) kiloTo[0] = Float.MAX_VALUE;

                List<Float> years = rngYear.getValues();
                Float yearFrom=years.get(0);
                Float[] yearTo = new Float[1];
                yearTo[0]  = years.get(1);

                String type = SellLend.getSelectedItem().toString();
                Boolean[] selllend = new Boolean[1];

                ProgressDialog progressDialog= new ProgressDialog(getActivity());
                progressDialog.setTitle("Searching...");
                progressDialog.setMessage("Completing Search in the CarSLN MarketPlace, Please Wait");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIcon(R.drawable.slnround);
                progressDialog.show();

                fbs.getStore().collection("MarketPlace").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()) {

                            CarID car = dataSnapshot.toObject(CarID.class);
                            car.setCarPhoto(dataSnapshot.getString("photo"));
                            car.setId(dataSnapshot.getId());

                            if(type.equals("Any Offer Type")) selllend[0] = car.getSellLend();
                            if(type.equals("Purchase a Car")) selllend[0] = true;
                            if(type.equals("Rent a Car")) selllend[0] = false;


                            if (Man.trim().isEmpty() && Mod.trim().isEmpty()) {

                                if (car.getPrice() >= PriceFrom && car.getPrice() <= PriceTo[0] & car.getKilometre() >= kiloFrom && car.getKilometre() <= kiloTo[0] & car.getYear() >= yearFrom && car.getYear() <= yearTo[0] & car.getSellLend()==selllend[0])
                                    search.add(car);

                            } else {
                                if (!Man.trim().isEmpty() && Mod.trim().isEmpty()) { // only man is in

                                    if (car.getManufacturer().toLowerCase().contains(Man.toLowerCase()) && car.getPrice() >= PriceFrom && car.getPrice() <= PriceTo[0] & car.getKilometre() >= kiloFrom && car.getKilometre() <= kiloTo[0] & car.getYear() >= yearFrom && car.getYear() <= yearTo[0] & car.getSellLend()==selllend[0])
                                        search.add(car);

                                } else {
                                    if (Man.trim().isEmpty() && !Mod.trim().isEmpty()) {

                                        if (car.getModel().toLowerCase().contains(Mod.toLowerCase()) && car.getPrice() >= PriceFrom && car.getPrice() <= PriceTo[0] & car.getKilometre() >= kiloFrom && car.getKilometre() <= kiloTo[0] & car.getYear() >= yearFrom && car.getYear() <= yearTo[0] & car.getSellLend()==selllend[0])
                                            search.add(car);

                                    } else { // Man and Mod are in

                                        if (car.getManufacturer().toLowerCase().contains(Man.toLowerCase()) && car.getModel().toLowerCase().contains(Mod.toLowerCase()) && car.getPrice() >= PriceFrom && car.getPrice() <= PriceTo[0] & car.getKilometre() >= kiloFrom && car.getKilometre() <= kiloTo[0] & car.getYear() >= yearFrom && car.getYear() <= yearTo[0] & car.getSellLend()==selllend[0])
                                            search.add(car);

                                    }
                                }
                            }

                        }

                        fbs.setCarList(search);
                        GoToFragmentSearchCar();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Couldn't Complete Search, Please Try Again Later!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });

    }

    public void GoToFragmentSearchCar(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new CarSearchListFragment());
        ft.commit();
    }

    private void SettingFrame() {

        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new CarsAdapter(getActivity(), search);
        rc.setAdapter(Adapter);
    }

    private void SettingFullList() {

        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new CarsAdapter(getActivity(), lstRst);
        rc.setAdapter(Adapter);

    }

}