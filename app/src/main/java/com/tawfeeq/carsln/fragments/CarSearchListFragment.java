package com.tawfeeq.carsln.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.adapters.SearchCarsAdapter;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.objects.LastSearch;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarSearchListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarSearchListFragment extends Fragment {

    FireBaseServices fbs;
    CardView GoSearch;
    ArrayList<CarID> search, filteredsearch, market;
    RecyclerView rc;
    SearchCarsAdapter Adapter;
    ImageButton btnSearch;
    TextView tvSearch, tvResults;
    ArrayList<String> Saved;
    Spinner Filter;
    SwipeRefreshLayout refreshSearch;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CarSearchListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarSearchListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarSearchListFragment newInstance(String param1, String param2) {
        CarSearchListFragment fragment = new CarSearchListFragment();
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
        View view = inflater.inflate(R.layout.fragment_car_search_list, container, false);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        fbs.setRcSearch(rc.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        GoSearch = getView().findViewById(R.id.cardViewsearch);
        rc = getView().findViewById(R.id.RecyclerSearch);
        refreshSearch = getView().findViewById(R.id.RefreshSearch);
        btnSearch = getView().findViewById(R.id.btngoSearch);
        tvSearch = getView().findViewById(R.id.textViewsearchcustom);
        tvResults = getView().findViewById(R.id.textViewcountresults);
        Filter =getView().findViewById(R.id.SpinnerFiltering);


        if(!fbs.getCurrentFragment().equals("CarSearchList")) fbs.setCurrentFragment("CarSearchList");


        search = new ArrayList<CarID>();


        if(fbs.getUser()!=null) Saved = fbs.getUser().getSavedCars();
        else Saved = new ArrayList<String>();


        if(Filter.getSelectedItem() == null) {
            String[] FilterList = {"Sort Search By", "Date Created", "Price - Ascending", "Price - Descending", "Kilometre - Ascending", "Year - Newest to Oldest"};
            ArrayAdapter<String> FilterAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, FilterList);
            FilterAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
            Filter.setAdapter(FilterAdapter);
        }



        refreshSearch.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                LastSearch lastSearch = fbs.getLastSearch();

                if(lastSearch != null) {

                    search = new ArrayList<CarID>();

                    String Man = lastSearch.getManufacturer();

                    String Mod = lastSearch.getModel();

                    String area = lastSearch.getLocation();
                    String[] location = new String[1];

                    Float PriceFrom = lastSearch.getPriceFrom();
                    Float[] PriceTo = new Float[1];
                    PriceTo[0] = lastSearch.getPriceTo();
                    if (PriceTo[0] == 200000) PriceTo[0] = Float.MAX_VALUE;

                    Float kiloFrom = lastSearch.getKiloFrom();
                    Float[] kiloTo = new Float[1];
                    kiloTo[0] = lastSearch.getKiloTo();
                    if (kiloTo[0] == 200000) kiloTo[0] = Float.MAX_VALUE;

                    Float yearFrom = lastSearch.getYearFrom();
                    Float[] yearTo = new Float[1];
                    yearTo[0] = lastSearch.getYearTo();

                    String type = lastSearch.getOffer();
                    Boolean[] selllend = new Boolean[1];


                    if (isConnected()) { // the User is Connected to the Internet and a Last Was Was Done Since the App was Opened!
                        fbs.getStore().collection("MarketPlace").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                for (DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()) {

                                    CarID car = dataSnapshot.toObject(CarID.class);
                                    car.setCarPhoto(dataSnapshot.getString("photo"));
                                    car.setId(dataSnapshot.getId());

                                    if (type.equals("Any Offer Type"))
                                        selllend[0] = car.getSellLend();
                                    if (type.equals("Purchase a Car")) selllend[0] = true;
                                    if (type.equals("Rent a Car")) selllend[0] = false;

                                    if (area.equals("Any Location District"))
                                        location[0] = car.getLocation();
                                    else location[0] = area;


                                    if (Man.equals("All Car Manufacturers") && Mod.equals("All Car Models")) { // All Car Manufacturers and Models!

                                        if (car.getPrice() >= PriceFrom && car.getPrice() <= PriceTo[0] & car.getKilometre() >= kiloFrom && car.getKilometre() <= kiloTo[0] & car.getYear() >= yearFrom && car.getYear() <= yearTo[0] & car.getSellLend() == selllend[0] && car.getLocation().equals(location[0]))
                                            search.add(car);

                                    } else {
                                        if (!Man.equals("All Car Manufacturers") && Mod.equals("All Models")) { // All Models of The Selected Manufacturer!

                                            if (car.getManufacturer().toLowerCase().contains(Man.toLowerCase()) && car.getPrice() >= PriceFrom && car.getPrice() <= PriceTo[0] & car.getKilometre() >= kiloFrom && car.getKilometre() <= kiloTo[0] & car.getYear() >= yearFrom && car.getYear() <= yearTo[0] & car.getSellLend() == selllend[0] && car.getLocation().equals(location[0]))
                                                search.add(car);

                                        } else { // Specific Car Manufacturer and Model!

                                            if (car.getManufacturer().toLowerCase().contains(Man.toLowerCase()) && car.getModel().toLowerCase().contains(Mod.toLowerCase()) && car.getPrice() >= PriceFrom && car.getPrice() <= PriceTo[0] & car.getKilometre() >= kiloFrom && car.getKilometre() <= kiloTo[0] & car.getYear() >= yearFrom && car.getYear() <= yearTo[0] & car.getSellLend() == selllend[0] && car.getLocation().equals(location[0]))
                                                search.add(car);

                                        }
                                    }

                                }

                                fbs.setCarList(search);
                                fbs.setSearchList(search);


                                String str = String.valueOf(search.size());
                                if(search.size()==1) tvResults.setText(str + " Result");
                                else tvResults.setText(str + " Results");


                                String item = fbs.getLastFilter();

                                if(item.equals("null") || item.equals("Date Created") || item.equals("Sort Search By")){

                                    search = fbs.getCarList();
                                    fbs.setSearchList(search);
                                    SettingFrame();

                                }
                                if(item.equals("Price - Ascending")){

                                    market = DupArray(search);
                                    filteredsearch = new ArrayList<CarID>();

                                    int rep;
                                    CarID car;

                                    while(!market.isEmpty()){
                                        car = null;
                                        for(rep=0 ; rep< market.size() ; rep++){
                                            if(car==null) car = market.get(rep);
                                            else if(car.getPrice()>market.get(rep).getPrice()) car = market.get(rep);
                                        }

                                        filteredsearch.add(car);
                                        market.remove(car);
                                    }

                                    search = filteredsearch;
                                    fbs.setSearchList(filteredsearch);
                                    SettingFrame();

                                }
                                if(item.equals("Price - Descending")){

                                    market = DupArray(search);
                                    filteredsearch = new ArrayList<CarID>();

                                    int rep;
                                    CarID car;

                                    while(!market.isEmpty()){
                                        car = null;
                                        for(rep=0 ; rep< market.size() ; rep++){
                                            if(car==null) car = market.get(rep);
                                            else if(car.getPrice()<market.get(rep).getPrice()) car = market.get(rep);
                                        }

                                        filteredsearch.add(car);
                                        market.remove(car);
                                    }

                                    search = filteredsearch;
                                    fbs.setSearchList(filteredsearch);
                                    SettingFrame();

                                }
                                if(item.equals("Kilometre - Ascending")){

                                    market = DupArray(search);
                                    filteredsearch = new ArrayList<CarID>();

                                    int rep;
                                    CarID car;

                                    while(!market.isEmpty()){
                                        car = null;
                                        for(rep=0 ; rep< market.size() ; rep++){
                                            if(car==null) car = market.get(rep);
                                            else if(car.getKilometre()>market.get(rep).getKilometre()) car = market.get(rep);
                                        }

                                        filteredsearch.add(car);
                                        market.remove(car);
                                    }

                                    search = filteredsearch;
                                    fbs.setSearchList(filteredsearch);
                                    SettingFrame();

                                }
                                if(item.equals("Year - Newest to Oldest")){

                                    market = DupArray(search);
                                    filteredsearch = new ArrayList<CarID>();

                                    int rep;
                                    CarID car;

                                    while(!market.isEmpty()){
                                        car = null;
                                        for(rep=0 ; rep< market.size() ; rep++){
                                            if(car==null) car = market.get(rep);
                                            else if(car.getYear()<market.get(rep).getYear()) car = market.get(rep);
                                        }

                                        filteredsearch.add(car);
                                        market.remove(car);
                                    }

                                    search = filteredsearch;
                                    fbs.setSearchList(filteredsearch);

                                    SettingFrame();

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Couldn't Complete Search, Please Try Again Later!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        refreshSearch.setRefreshing(false);

                    }
                    else { // The User Has no Internet Connection!
                        search = new ArrayList<CarID>();
                        SettingFrame();
                        refreshSearch.setRefreshing(false);
                    }

                }
                else { // Search Was Never Used Since the App Was Opened!

                    if(isConnected()) {
                        search = new ArrayList<CarID>();
                        fbs.getStore().collection("MarketPlace").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()) {

                                    CarID car = dataSnapshot.toObject(CarID.class);
                                    car.setCarPhoto(dataSnapshot.getString("photo"));
                                    car.setId(dataSnapshot.getId());
                                    search.add(car);

                                }

                                fbs.setCarList(search);
                                fbs.setSearchList(search);


                                String str = String.valueOf(search.size());
                                if(search.size()==1) tvResults.setText(str + " Result");
                                else tvResults.setText(str + " Results");


                                String item = fbs.getLastFilter();

                                if(item.equals("null") || item.equals("Date Created") || item.equals("Sort Search By")){

                                    search = fbs.getCarList();
                                    fbs.setSearchList(search);

                                    SettingFrame();
                                }
                                if(item.equals("Price - Ascending")){

                                    market = DupArray(search);
                                    filteredsearch = new ArrayList<CarID>();

                                    int rep;
                                    CarID car;

                                    while(!market.isEmpty()){
                                        car = null;
                                        for(rep=0 ; rep< market.size() ; rep++){
                                            if(car==null) car = market.get(rep);
                                            else if(car.getPrice()>market.get(rep).getPrice()) car = market.get(rep);
                                        }

                                        filteredsearch.add(car);
                                        market.remove(car);
                                    }

                                    search = filteredsearch;
                                    fbs.setSearchList(filteredsearch);

                                    SettingFrame();

                                }
                                if(item.equals("Price - Descending")){

                                    market = DupArray(search);
                                    filteredsearch = new ArrayList<CarID>();

                                    int rep;
                                    CarID car;

                                    while(!market.isEmpty()){
                                        car = null;
                                        for(rep=0 ; rep< market.size() ; rep++){
                                            if(car==null) car = market.get(rep);
                                            else if(car.getPrice()<market.get(rep).getPrice()) car = market.get(rep);
                                        }

                                        filteredsearch.add(car);
                                        market.remove(car);
                                    }

                                    search = filteredsearch;
                                    fbs.setSearchList(filteredsearch);

                                    SettingFrame();

                                }
                                if(item.equals("Kilometre - Ascending")){

                                    market = DupArray(search);
                                    filteredsearch = new ArrayList<CarID>();

                                    int rep;
                                    CarID car;

                                    while(!market.isEmpty()){
                                        car = null;
                                        for(rep=0 ; rep< market.size() ; rep++){
                                            if(car==null) car = market.get(rep);
                                            else if(car.getKilometre()>market.get(rep).getKilometre()) car = market.get(rep);
                                        }

                                        filteredsearch.add(car);
                                        market.remove(car);
                                    }

                                    search = filteredsearch;
                                    fbs.setSearchList(filteredsearch);

                                    SettingFrame();

                                }
                                if(item.equals("Year - Newest to Oldest")){

                                    market = DupArray(search);
                                    filteredsearch = new ArrayList<CarID>();

                                    int rep;
                                    CarID car;

                                    while(!market.isEmpty()){
                                        car = null;
                                        for(rep=0 ; rep< market.size() ; rep++){
                                            if(car==null) car = market.get(rep);
                                            else if(car.getYear()<market.get(rep).getYear()) car = market.get(rep);
                                        }

                                        filteredsearch.add(car);
                                        market.remove(car);
                                    }

                                    search = filteredsearch;
                                    fbs.setSearchList(filteredsearch);

                                    SettingFrame();

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Couldn't Retrieve MarketPlace Info, Please Try Again Later", Toast.LENGTH_SHORT).show();
                            }
                        });

                        refreshSearch.setRefreshing(false);

                    }else {// The User Has no Internet Connection!
                        search = new ArrayList<CarID>();
                        SettingFrame();
                        refreshSearch.setRefreshing(false);
                    }
                }
            }
        });



        if(fbs.getMarketList() != null) {
            if (fbs.getCarList() == null) {
                search = fbs.getMarketList();
                fbs.setCarList(search);
                fbs.setSearchList(search);
            }
            else search = fbs.getSearchList();
        } else search = new ArrayList<CarID>();
        SettingFrameOnPause();


        String str = String.valueOf(search.size());

        if(search.size()==1) tvResults.setText(str + " Result");
        else tvResults.setText(str + " Results");


        Filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String item = adapterView.getSelectedItem().toString();

                if(item.equals("Date Created")){

                    search = fbs.getCarList();
                    fbs.setSearchList(search);
                    fbs.setLastFilter("Name - Alphabetical");
                    SettingFrame();

                }
                if(item.equals("Price - Ascending")){

                    search = fbs.getCarList();
                    market = DupArray(search);
                    filteredsearch = new ArrayList<CarID>();

                    int rep;
                    CarID car;

                    while(!market.isEmpty()){
                        car = null;
                        for(rep=0 ; rep< market.size() ; rep++){
                            if(car==null) car = market.get(rep);
                            else if(car.getPrice()>market.get(rep).getPrice()) car = market.get(rep);
                        }

                        filteredsearch.add(car);
                        market.remove(car);
                    }

                    search = filteredsearch;
                    fbs.setSearchList(filteredsearch);
                    fbs.setLastFilter("Price - Ascending");
                    SettingFrame();

                }
                if(item.equals("Price - Descending")){

                    search = fbs.getCarList();
                    market = DupArray(search);
                    filteredsearch = new ArrayList<CarID>();

                    int rep;
                    CarID car;

                    while(!market.isEmpty()){
                        car = null;
                        for(rep=0 ; rep< market.size() ; rep++){
                            if(car==null) car = market.get(rep);
                            else if(car.getPrice()<market.get(rep).getPrice()) car = market.get(rep);
                        }

                        filteredsearch.add(car);
                        market.remove(car);
                    }

                    search = filteredsearch;
                    fbs.setSearchList(filteredsearch);
                    fbs.setLastFilter("Price - Descending");
                    SettingFrame();

                }
                if(item.equals("Kilometre - Ascending")){

                    search = fbs.getCarList();
                    market = DupArray(search);
                    filteredsearch = new ArrayList<CarID>();

                    int rep;
                    CarID car;

                    while(!market.isEmpty()){
                        car = null;
                        for(rep=0 ; rep< market.size() ; rep++){
                            if(car==null) car = market.get(rep);
                            else if(car.getKilometre()>market.get(rep).getKilometre()) car = market.get(rep);
                        }

                        filteredsearch.add(car);
                        market.remove(car);
                    }

                    search = filteredsearch;
                    fbs.setSearchList(filteredsearch);
                    fbs.setLastFilter("Kilometre - Ascending");
                    SettingFrame();

                }
                if(item.equals("Year - Newest to Oldest")){

                    search = fbs.getCarList();
                    market = DupArray(search);
                    filteredsearch = new ArrayList<CarID>();

                    int rep;
                    CarID car;

                    while(!market.isEmpty()){
                        car = null;
                        for(rep=0 ; rep< market.size() ; rep++){
                            if(car==null) car = market.get(rep);
                            else if(car.getYear()<market.get(rep).getYear()) car = market.get(rep);
                        }

                        filteredsearch.add(car);
                        market.remove(car);
                    }

                    search = filteredsearch;
                    fbs.setSearchList(filteredsearch);
                    fbs.setLastFilter("Year - Newest to Oldest");
                    SettingFrame();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Does Nothing!!!
            }
        });


        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoBackSearch();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoBackSearch();
            }
        });
        GoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(rc.getScrollState());
                GoBackSearch();
            }
        });

    }

    private boolean isConnected(){
        return ((MainActivity) getActivity()).isNetworkAvailable();
    }

    private ArrayList<CarID> DupArray(ArrayList<CarID> marketList) {

        ArrayList<CarID> market = new ArrayList<CarID>();

        for(int i=0 ; i< marketList.size() ; i++){
            market.add(marketList.get(i));
        }

        return market;
    }

    private void SettingFrame() {

        fbs.setRcSearch(null);

        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new SearchCarsAdapter(getActivity(), search, Saved);
        rc.setAdapter(Adapter);

    }

    private void SettingFrameOnPause() {

        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new SearchCarsAdapter(getActivity(), search, Saved);
        rc.setAdapter(Adapter);

        if(fbs.getRcSearch()!=null){
            rc.getLayoutManager().onRestoreInstanceState(fbs.getRcSearch());
        }
    }

    private void GoBackSearch(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SearchFragment());
        ft.commit();
    }

}