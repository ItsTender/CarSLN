package com.tawfeeq.carsln.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.tawfeeq.carsln.MainActivity;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.adapters.CarsAdapter;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.R;

import org.checkerframework.checker.units.qual.C;

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
    CarsAdapter Adapter;
    ImageButton btnSearch;
    TextView tvSearch, tvResults;
    ArrayList<String> Saved;
    Spinner Filter;

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
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        GoSearch = getView().findViewById(R.id.cardViewsearch);
        rc = getView().findViewById(R.id.RecyclerSearch);
        btnSearch = getView().findViewById(R.id.btngoSearch);
        tvSearch = getView().findViewById(R.id.textViewsearchcustom);
        tvResults = getView().findViewById(R.id.textViewcountresults);
        Filter =getView().findViewById(R.id.SpinnerFiltering);


        search = new ArrayList<CarID>();


        if(fbs.getUser()!=null) Saved = fbs.getUser().getSavedCars();
        else Saved = new ArrayList<String>();


        String [] FilterList = {"Sort Search By","By Manufacturer", "Price - Ascending", "Price - Descending", "Kilometre - Ascending"};
        ArrayAdapter<String> FilterAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, FilterList);
        FilterAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        Filter.setAdapter(FilterAdapter);


        if(fbs.getMarketList() != null) {
            if (fbs.getCarList() == null) {
                search = fbs.getMarketList();
                fbs.setCarList(search);
                fbs.setSearchList(search);
            }
            else search = fbs.getSearchList();
        } else search = new ArrayList<CarID>();
        SettingFrame();


        String str = String.valueOf(search.size());

        if(search.size()==1) tvResults.setText(str + " Result");
        else tvResults.setText(str + " Results");


        Filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String item = adapterView.getSelectedItem().toString();

                if(item.equals("By Manufacturer")){

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
                GoBackSearch();
            }
        });

    }

    private ArrayList<CarID> DupArray(ArrayList<CarID> marketList) {

        ArrayList<CarID> market = new ArrayList<CarID>();

        for(int i=0 ; i< marketList.size() ; i++){
            market.add(marketList.get(i));
        }

        return market;
    }

    private void SettingFrame() {

        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new CarsAdapter(getActivity(), search, Saved);
        rc.setAdapter(Adapter);
    }

    private void GoBackSearch(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SearchFragment());
        ft.commit();
    }

}