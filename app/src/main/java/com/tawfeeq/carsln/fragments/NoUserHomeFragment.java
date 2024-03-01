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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.adapters.ForYouCarsAdapter;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.objects.FireBaseServices;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoUserHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoUserHomeFragment extends Fragment {

    ImageView ivPFP;
    RecyclerView rcNew, rcUsed;
    private ForYouCarsAdapter adapterNew, adapterUsed;
    private FireBaseServices fbs;
    private ArrayList<CarID> Market, lstNew, lstUsed;
    ArrayList<String> Saved;
    TextView tvShowNew, tvShowUsed;
    CardView mainSearch;
    SwipeRefreshLayout refreshMain;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NoUserHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoUserHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoUserHomeFragment newInstance(String param1, String param2) {
        NoUserHomeFragment fragment = new NoUserHomeFragment();
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
        return inflater.inflate(R.layout.fragment_no_user_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        ivPFP = getView().findViewById(R.id.ivNoAccountHomepfp);
        rcNew = getView().findViewById(R.id.RecyclerNewCars);
        rcUsed = getView().findViewById(R.id.RecyclerUsedCars);
        mainSearch =getView().findViewById(R.id.cardViewsearchNoAccountHome);
        refreshMain = getView().findViewById(R.id.RefreshNoAccountHome);
        tvShowNew = getView().findViewById(R.id.textViewMoreNewCars);
        tvShowUsed =getView().findViewById(R.id.textViewMoreUsedCars);


        if(!fbs.getCurrentFragment().equals("NoUserHome")) fbs.setCurrentFragment("NoUserHome");


        Market = new ArrayList<CarID>();
        Saved = new ArrayList<String>();


        if (fbs.getCarList() == null) {
            ArrayList<CarID> search = fbs.getMarketList();
            fbs.setCarList(search);
            fbs.setSearchList(search);
        }

        mainSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationBarSearch();
                GoToSearch();
            }
        });

        ivPFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationBarProfile();
            }
        });


        refreshMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(isConnected()) {

                    Market = new ArrayList<CarID>();
                    fbs.getStore().collection("MarketPlace").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()) {

                                CarID car = dataSnapshot.toObject(CarID.class);
                                car.setCarPhoto(dataSnapshot.getString("photo"));
                                car.setId(dataSnapshot.getId());
                                Market.add(car);

                            }

                            fbs.setMarketList(Market);


                            // New Cars Car List
                            lstNew = new ArrayList<CarID>();
                            int j;
                            if (Market != null) {
                                for (j = 0; j < Market.size(); j++) {
                                    CarID car = Market.get(j);
                                    if (lstNew.size()<8 && car.getUsers() < 2 && car.getYear() > 2019) lstNew.add(car);
                                }
                            }

                            SettingFrameNewCars();
                            // Ends.......................


                            // Used Cars Car List
                            lstUsed = new ArrayList<CarID>();
                            int k;
                            if (Market != null) {
                                for (k = 0; k < Market.size(); k++) {
                                    CarID car = Market.get(k);
                                    if (lstUsed.size()<8 && car.getUsers() >= 1 && car.getYear() < 2020 && car.getYear() > 2008 && car.getPrice() < 100000)
                                        lstUsed.add(car);
                                }
                            }

                            SettingFrameUsedCars();
                            // Ends.......................

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Retrieve MarketPlace Info, Please Try Again Later", Toast.LENGTH_SHORT).show();
                        }
                    });

                    refreshMain.setRefreshing(false);

                }else {
                    refreshMain.setRefreshing(false);
                }
            }
        });


        if (fbs.getMarketList() == null) {
            fbs.getStore().collection("MarketPlace").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()) {

                        CarID car = dataSnapshot.toObject(CarID.class);
                        car.setCarPhoto(dataSnapshot.getString("photo"));
                        car.setId(dataSnapshot.getId());
                        Market.add(car);

                    }

                    fbs.setMarketList(Market);


                    // New Cars Car List
                    lstNew = new ArrayList<CarID>();
                    int j;
                    if(Market!=null) {
                        for (j = 0; j < Market.size(); j++) {
                            CarID car = Market.get(j);
                            if (lstNew.size()<8 && car.getUsers()<2 && car.getYear()>2019) lstNew.add(car);
                        }
                    }

                    SettingFrameNewCars();
                    // Ends.......................


                    // Used Cars Car List
                    lstUsed = new ArrayList<CarID>();
                    int k;
                    if(Market!=null) {
                        for (k = 0; k < Market.size(); k++) {
                            CarID car = Market.get(k);
                            if (lstUsed.size()<8 && car.getUsers() >= 1 && car.getYear() < 2020 && car.getYear() > 2008 && car.getPrice() < 100000)
                                lstUsed.add(car);
                        }
                    }

                    SettingFrameUsedCars();
                    // Ends.......................

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Couldn't Retrieve MarketPlace Info, Please Try Again Later", Toast.LENGTH_SHORT).show();
                    fbs.setMarketList(new ArrayList<CarID>());

                }
            });

        } else {

            Market = fbs.getMarketList();


            // New Cars Car List
            lstNew = new ArrayList<CarID>();
            int j;
            if(Market!=null) {
                for (j = 0; j < Market.size(); j++) {
                    CarID car = Market.get(j);
                    if (lstNew.size()<8 && car.getUsers()<2 && car.getYear()>2019) lstNew.add(car);
                }
            }

            SettingFrameNewCars();
            // Ends.......................


            // Used Cars Car List
            lstUsed = new ArrayList<CarID>();
            int k;
            if(Market!=null) {
                for (k = 0; k < Market.size(); k++) {
                    CarID car = Market.get(k);
                    if (lstUsed.size()<8 && car.getUsers() >= 1 && car.getYear() < 2020 && car.getYear() > 2008 && car.getPrice() < 100000)
                        lstUsed.add(car);
                }
            }

            SettingFrameUsedCars();
            // Ends.......................

        }

        tvShowNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fbs.setFrom("New");
                GoToForYouList();
                setNavigationBarGone();
            }
        });

        tvShowUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fbs.setFrom("Used");
                GoToForYouList();
                setNavigationBarGone();
            }
        });

    }

    private void GoToSearch() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SearchFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void setNavigationBarSearch() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.searchcar);
    }

    private boolean isConnected(){
        return ((MainActivity) getActivity()).isNetworkAvailable();
    }


    private void SettingFrameNewCars() {

        // Horizontal RecyclerView for (Cars Near You, New Cars, Used Cars).
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rcNew.setLayoutManager(linearLayoutManager);
        rcNew.setHasFixedSize(true);
        adapterNew = new ForYouCarsAdapter(getActivity(), lstNew, Saved);
        rcNew.setAdapter(adapterNew);
    }

    private void SettingFrameUsedCars() {

        // Horizontal RecyclerView for (Cars Near You, New Cars, Used Cars).
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rcUsed.setLayoutManager(linearLayoutManager);
        rcUsed.setHasFixedSize(true);
        adapterUsed = new ForYouCarsAdapter(getActivity(), lstUsed, Saved);
        rcUsed.setAdapter(adapterUsed);
    }

    private void GoToProfile() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
        ft.commit();
    }

    private void setNavigationBarProfile() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.profile);
    }

    private void GoToForYouList(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ForYouListFragment());
        ft.commit();
    }

    private void setNavigationBarGone() {
        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.GONE);
    }
}