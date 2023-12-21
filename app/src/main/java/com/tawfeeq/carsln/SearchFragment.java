package com.tawfeeq.carsln;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    EditText etMan,etMod,etPriceFrom,etPriceTo;
    Button btnSearch, btnResetSearch;
    RecyclerView rc;
    FireBaseServices fbs;
    ArrayList<Cars> search, lstRst;
    CarsAdapter Adapter;


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
        etPriceFrom=getView().findViewById(R.id.etPriceFrom);
        etPriceTo=getView().findViewById(R.id.etPriceTo);
        rc=getView().findViewById(R.id.RecyclerSearch);

        lstRst=new ArrayList<Cars>();


        fbs.getStore().collection("MarketPlace").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){

                    Cars car = dataSnapshot.toObject(Cars.class);
                    car.setCarPhoto(dataSnapshot.getString("photo"));
                    lstRst.add(car);

                }
                SettingFullList();
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
                SettingFullList();

                etMan.setText("");
                etMod.setText("");
                etPriceFrom.setText("");
                etPriceTo.setText("");
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                search=new ArrayList<Cars>();

                String Man=etMan.getText().toString();
                String Mod=etMod.getText().toString();
                String PriceFrom=etPriceFrom.getText().toString();
                String PriceTo=etPriceTo.getText().toString();

                boolean none= PriceFrom.trim().isEmpty() && PriceTo.trim().isEmpty();
                boolean onlyfrom=!PriceFrom.trim().isEmpty() && PriceTo.trim().isEmpty();
                boolean onlyto=PriceFrom.trim().isEmpty() && !PriceTo.trim().isEmpty();


                if(!PriceFrom.trim().isEmpty() && !PriceTo.trim().isEmpty()) {
                    Integer From=Integer.parseInt(PriceFrom);
                    Integer To=Integer.parseInt(PriceTo);
                    if (From > To) {
                        Toast.makeText(getActivity(), "Price Values are Inaccurate", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                fbs.getStore().collection("MarketPlace").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){

                            Cars car = dataSnapshot.toObject(Cars.class);
                            car.setCarPhoto(dataSnapshot.getString("photo"));


                            if(Man.trim().isEmpty() && Mod.trim().isEmpty()) {
                                // both are not in
                                if(none)
                                { // none are in
                                    search = lstRst;
                                }
                                else
                                {
                                    if(onlyfrom)
                                    { // only From is in
                                        Integer From=Integer.parseInt(PriceFrom);
                                        if(car.getPrice()>From) search.add(car);
                                    }
                                    else if(onlyto)
                                    { // only To is in
                                        Integer To=Integer.parseInt(PriceTo);
                                        if(car.getPrice()<To) search.add(car);
                                    }
                                    else
                                    {
                                        Integer From=Integer.parseInt(PriceFrom);
                                        Integer To=Integer.parseInt(PriceTo);
                                        if(car.getPrice()>From && car.getPrice()<To) search.add(car);
                                    }
                                }
                            }
                            else
                            {
                                if(!Man.trim().isEmpty() && Mod.trim().isEmpty())
                                { // only man is in
                                    if(none)
                                    { // none are in
                                        if(car.getManufacturer().toLowerCase().contains(Man.toLowerCase())) search.add(car);
                                    }
                                    else
                                    {
                                        if(onlyfrom)
                                        { // only From is in
                                            Integer From=Integer.parseInt(PriceFrom);
                                            if(car.getManufacturer().toLowerCase().contains(Man.toLowerCase()) && car.getPrice()>From) search.add(car);
                                        }
                                        else if(onlyto)
                                        { // only To is in
                                            Integer To=Integer.parseInt(PriceTo);
                                            if(car.getManufacturer().toLowerCase().contains(Man.toLowerCase()) && car.getPrice()<To) search.add(car);
                                        }
                                        else
                                        {
                                            Integer From=Integer.parseInt(PriceFrom);
                                            Integer To=Integer.parseInt(PriceTo);
                                            if(car.getManufacturer().toLowerCase().contains(Man.toLowerCase()) && car.getPrice()>From && car.getPrice()<To) search.add(car);
                                        }
                                    }
                                } else
                                {
                                    if(Man.trim().isEmpty() && !Mod.trim().isEmpty())
                                    { // only mod is in
                                        if(none)
                                        { // none are in
                                            if(car.getModel().toLowerCase().contains(Mod.toLowerCase())) search.add(car);
                                        }
                                        else
                                        {
                                            if(onlyfrom)
                                            { // only From is in
                                                Integer From=Integer.parseInt(PriceFrom);
                                                if(car.getModel().toLowerCase().contains(Mod.toLowerCase()) && car.getPrice()>From) search.add(car);
                                            }
                                            else if(onlyto)
                                            { // only To is in
                                                Integer To=Integer.parseInt(PriceTo);
                                                if(car.getModel().toLowerCase().contains(Mod.toLowerCase()) && car.getPrice()<To) search.add(car);
                                            }
                                            else
                                            {
                                                Integer From=Integer.parseInt(PriceFrom);
                                                Integer To=Integer.parseInt(PriceTo);
                                                if(car.getModel().toLowerCase().contains(Mod.toLowerCase()) && car.getPrice()>From && car.getPrice()<To) search.add(car);
                                            }
                                        }
                                    }
                                    else
                                    { // Man and Mod are in
                                        if(none)
                                        { // none are in
                                            if(car.getManufacturer().toLowerCase().contains(Man.toLowerCase()) && car.getModel().toLowerCase().contains(Mod.toLowerCase())) search.add(car);
                                        }
                                        else
                                        {
                                            if(onlyfrom)
                                            { // only From is in
                                                Integer From=Integer.parseInt(PriceFrom);
                                                if(car.getManufacturer().toLowerCase().contains(Man.toLowerCase()) && car.getModel().toLowerCase().contains(Mod.toLowerCase()) && car.getPrice()>From) search.add(car);
                                            }
                                            else if(onlyto)
                                            { // only To is in
                                                Integer To=Integer.parseInt(PriceTo);
                                                if(car.getManufacturer().toLowerCase().contains(Man.toLowerCase()) && car.getModel().toLowerCase().contains(Mod.toLowerCase()) && car.getPrice()<To) search.add(car);
                                            }
                                            else
                                            {
                                                Integer From=Integer.parseInt(PriceFrom);
                                                Integer To=Integer.parseInt(PriceTo);
                                                if(car.getManufacturer().toLowerCase().contains(Man.toLowerCase()) && car.getModel().toLowerCase().contains(Mod.toLowerCase()) && car.getPrice()>From && car.getPrice()<To) search.add(car);
                                            }
                                        }
                                    }
                                }

                            }
                        }
                        SettingFrame();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Couldn't Complete Search, Please Try Again Later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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