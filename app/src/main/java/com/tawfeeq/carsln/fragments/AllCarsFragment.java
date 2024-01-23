package com.tawfeeq.carsln.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.tawfeeq.carsln.adapters.ForYouCarsAdapter;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.adapters.CarsAdapter;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.MainActivity;
import com.tawfeeq.carsln.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllCarsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllCarsFragment extends Fragment {

    ImageView ivPFP;
    RecyclerView rcNear, rcNew, rcUsed;
    private ForYouCarsAdapter adapterNear, adapterNew, adapterUsed;
    private FireBaseServices fbs;
    private ArrayList<CarID> Market, lstNear, lstNew, lstUsed;
    ArrayList<String> Saved;
    String pfp;
    TextView tvUsername, tvShowNear, tvShowNew, tvShowUsed;
    CardView mainSearch;
    SwipeRefreshLayout refreshMain;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllCarsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllCarsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllCarsFragment newInstance(String param1, String param2) {
        AllCarsFragment fragment = new AllCarsFragment();
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
        return inflater.inflate(R.layout.fragment_all_cars, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        FireBaseServices fbs = FireBaseServices.getInstance();
        ivPFP = getView().findViewById(R.id.imageViewProfilePhotoAllCars);
        tvUsername = getView().findViewById(R.id.tvUsernameMain);
        rcNear =getView().findViewById(R.id.RecyclerCarsNearYou);
        rcNew = getView().findViewById(R.id.RecyclerNewCars);
        rcUsed = getView().findViewById(R.id.RecyclerUsedCars);
        mainSearch =getView().findViewById(R.id.cardViewsearchMain);
        refreshMain = getView().findViewById(R.id.RefreshMain);
        tvShowNear = getView().findViewById(R.id.textViewMoreCarsNearYou);
        tvShowNew = getView().findViewById(R.id.textViewMoreNewCars);
        tvShowUsed =getView().findViewById(R.id.textViewMoreUsedCars);

        Market = new ArrayList<CarID>();


        if(fbs.getUser()!=null) {
            Saved = fbs.getUser().getSavedCars();
            tvUsername.setText(fbs.getUser().getUsername());
        }
        else Saved = new ArrayList<String>();


        if (fbs.getCarList() == null) {
            ArrayList<CarID> search = fbs.getMarketList();
            fbs.setCarList(search);
            fbs.setSearchList(search);
        }


        if(fbs.getUser()!=null) {

            pfp = fbs.getUser().getUserPhoto();
            if (pfp == null || pfp.isEmpty()) {
                ivPFP.setImageResource(R.drawable.slnpfp);
            } else {
                Glide.with(getActivity()).load(pfp).into(ivPFP);
            }
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
                    fbs.getStore().collection("MarketPlace").orderBy("manufacturer").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()) {

                                CarID car = dataSnapshot.toObject(CarID.class);
                                car.setCarPhoto(dataSnapshot.getString("photo"));
                                car.setId(dataSnapshot.getId());
                                Market.add(car);

                            }

                            fbs.setMarketList(Market);


                            // Cars Near You Car List
                            lstNear = new ArrayList<CarID>();
                            int i;
                            if (Market != null && fbs.getUser() != null) {
                                for (i = 0; i < Market.size(); i++) {
                                    CarID car = Market.get(i);
                                    if (car.getLocation().equals(fbs.getUser().getLocation()))
                                        lstNear.add(car);
                                }
                            }

                            SettingFrameNearYou();
                            // Ends.......................


                            // New Cars Car List
                            lstNew = new ArrayList<CarID>();
                            int j;
                            if (Market != null && fbs.getUser() != null) {
                                for (j = 0; j < Market.size(); j++) {
                                    CarID car = Market.get(j);
                                    if (car.getUsers() < 2 && car.getYear() > 2019) lstNew.add(car);
                                }
                            }

                            SettingFrameNewCars();
                            // Ends.......................


                            // Used Cars Car List
                            lstUsed = new ArrayList<CarID>();
                            int k;
                            if (Market != null && fbs.getUser() != null) {
                                for (k = 0; k < Market.size(); k++) {
                                    CarID car = Market.get(k);
                                    if (car.getUsers() >= 1 && car.getYear() < 2020)
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
                    lstNear = new ArrayList<CarID>();
                    lstNew = new ArrayList<CarID>();
                    lstUsed = new ArrayList<CarID>();
                    SettingFrameNearYou();
                    SettingFrameNewCars();
                    SettingFrameUsedCars();
                    refreshMain.setRefreshing(false);
                }
            }
        });




        if(isConnected()) {

            if (fbs.getMarketList() == null) {
                fbs.getStore().collection("MarketPlace").orderBy("manufacturer").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()) {

                            CarID car = dataSnapshot.toObject(CarID.class);
                            car.setCarPhoto(dataSnapshot.getString("photo"));
                            car.setId(dataSnapshot.getId());
                            Market.add(car);

                        }

                        fbs.setMarketList(Market);


                        // Cars Near You Car List
                        lstNear = new ArrayList<CarID>();
                        int i;
                        if(Market!=null && fbs.getUser()!=null) {
                            for (i = 0; i < Market.size(); i++) {
                                CarID car = Market.get(i);
                                if (car.getLocation().equals(fbs.getUser().getLocation())) lstNear.add(car);
                            }
                        }

                        SettingFrameNearYou();
                        // Ends.......................


                        // New Cars Car List
                        lstNew = new ArrayList<CarID>();
                        int j;
                        if(Market!=null && fbs.getUser()!=null) {
                            for (j = 0; j < Market.size(); j++) {
                                CarID car = Market.get(j);
                                if (car.getUsers()<2 && car.getYear()>2019) lstNew.add(car);
                            }
                        }

                        SettingFrameNewCars();
                        // Ends.......................


                        // Used Cars Car List
                        lstUsed = new ArrayList<CarID>();
                        int k;
                        if(Market!=null && fbs.getUser()!=null) {
                            for (k = 0; k < Market.size(); k++) {
                                CarID car = Market.get(k);
                                if (car.getUsers()>=1 && car.getYear()<2020) lstUsed.add(car);
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


                // Cars Near You Car List
                lstNear = new ArrayList<CarID>();
                int i;
                if(Market!=null && fbs.getUser()!=null) {
                    for (i = 0; i < Market.size(); i++) {
                        CarID car = Market.get(i);
                        if (car.getLocation().equals(fbs.getUser().getLocation())) lstNear.add(car);
                    }
                }

                SettingFrameNearYou();
                // Ends.......................


                // New Cars Car List
                lstNew = new ArrayList<CarID>();
                int j;
                if(Market!=null && fbs.getUser()!=null) {
                    for (j = 0; j < Market.size(); j++) {
                        CarID car = Market.get(j);
                        if (car.getUsers()<2 && car.getYear()>2019) lstNew.add(car);
                    }
                }

                SettingFrameNewCars();
                // Ends.......................


                // Used Cars Car List
                lstUsed = new ArrayList<CarID>();
                int k;
                if(Market!=null && fbs.getUser()!=null) {
                    for (k = 0; k < Market.size(); k++) {
                        CarID car = Market.get(k);
                        if (car.getUsers()>=1 && car.getYear()<2020) lstUsed.add(car);
                    }
                }

                SettingFrameUsedCars();
                // Ends.......................

            }
        } else {
            fbs.setMarketList(new ArrayList<CarID>());
            fbs.setCarList(new ArrayList<CarID>());
            fbs.setSearchList(new ArrayList<CarID>());
        }


        tvShowNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fbs.setFrom("Near");
                GoToForYouList();
                setNavigationBarGone();
            }
        });

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
        ft.commit();
    }

    private void setNavigationBarSearch() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.searchcar);
    }


    private boolean isConnected(){
        return ((MainActivity) getActivity()).isNetworkAvailable();
    }

    private void SettingFrameNearYou() {

        // Horizontal RecyclerView for (Cars Near You, New Cars, Used Cars).
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rcNear.setLayoutManager(linearLayoutManager);
        rcNear.setHasFixedSize(true);
        adapterNear = new ForYouCarsAdapter(getActivity(), lstNear, Saved);
        rcNear.setAdapter(adapterNear);
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


