package com.tawfeeq.carsln.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tawfeeq.carsln.MainActivity;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.adapters.CarsAdapter;
import com.tawfeeq.carsln.adapters.ForYouCarsAdapter;
import com.tawfeeq.carsln.adapters.SearchCarsAdapter;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.objects.Cars;
import com.tawfeeq.carsln.objects.FireBaseServices;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForYouListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForYouListFragment extends Fragment {

    RecyclerView rcForYou;
    FireBaseServices fbs;
    ArrayList<CarID> lst, Market;
    CarsAdapter Adapter;
    ArrayList<String> Saved;
    ImageView Back;
    TextView tvDesc;
    SwipeRefreshLayout refreshForYou;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ForYouListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForYouListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForYouListFragment newInstance(String param1, String param2) {
        ForYouListFragment fragment = new ForYouListFragment();
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
        return inflater.inflate(R.layout.fragment_for_you_list, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(fbs.getFrom()!="") fbs.setRcForYou(rcForYou.getLayoutManager().onSaveInstanceState());
        else fbs.setRcForYou(null);
    }


    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        rcForYou = getView().findViewById(R.id.RecyclerForYou);
        Back = getView().findViewById(R.id.ForYouGoBack);
        tvDesc = getView().findViewById(R.id.textViewForYou);
        refreshForYou = getView().findViewById(R.id.RefreshForYou);


        if(!fbs.getCurrentFragment().equals("ForYou")) fbs.setCurrentFragment("ForYou");


        lst=new ArrayList<CarID>();


        if(fbs.getUser()!=null) Saved = fbs.getUser().getSavedCars();
        else Saved = new ArrayList<String>();


        if(fbs.getMarketList()!=null) Market = fbs.getMarketList();
        else Market = new ArrayList<CarID>();


        refreshForYou.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (isConnected()) {

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


                            if (fbs.getFrom().equals("Near")) {

                                // Cars Near You Car List
                                lst = new ArrayList<CarID>();
                                int i;
                                if (Market != null && fbs.getUser() != null) {
                                    for (i = 0; i < Market.size(); i++) {
                                        CarID car = Market.get(i);
                                        if (car.getLocation().equals(fbs.getUser().getLocation()))
                                            lst.add(car);
                                    }
                                }
                                // Ends.......................

                            } else if (fbs.getFrom().equals("New")) {

                                // New Cars Car List
                                lst = new ArrayList<CarID>();
                                int j;
                                if (Market != null && fbs.getUser() != null) {
                                    for (j = 0; j < Market.size(); j++) {
                                        CarID car = Market.get(j);
                                        if (car.getUsers() < 2 && car.getYear() > 2019)
                                            lst.add(car);
                                    }
                                }
                                // Ends.......................

                            } else if (fbs.getFrom().equals("Used")) {

                                // Used Cars Car List
                                lst = new ArrayList<CarID>();
                                int k;
                                if (Market != null && fbs.getUser() != null) {
                                    for (k = 0; k < Market.size(); k++) {
                                        CarID car = Market.get(k);
                                        if (car.getUsers() >= 1 && car.getYear() < 2020)
                                            lst.add(car);
                                    }
                                }
                                // Ends.......................

                                SettingFrame();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Retrieve MarketPlace Info, Please Try Again Later", Toast.LENGTH_SHORT).show();
                        }
                    });

                    refreshForYou.setRefreshing(false);

                } else {
                    lst = new ArrayList<CarID>();
                    SettingFrame();
                    refreshForYou.setRefreshing(false);
                }
            }
        });



        if(fbs.getMarketList()!=null) {

            if (fbs.getFrom().equals("Near")) {

                tvDesc.setText("Cars Near You");
                // Cars Near You Car List
                lst = new ArrayList<CarID>();
                int i;
                if (Market != null && fbs.getUser() != null) {
                    for (i = 0; i < Market.size(); i++) {
                        CarID car = Market.get(i);
                        if (car.getLocation().equals(fbs.getUser().getLocation())) lst.add(car);
                    }
                }

                SettingFrameOnPause();
                // Ends.......................

            } else if (fbs.getFrom().equals("New")) {

                tvDesc.setText("New Cars");
                // New Cars Car List
                lst = new ArrayList<CarID>();
                int j;
                if (Market != null && fbs.getUser() != null) {
                    for (j = 0; j < Market.size(); j++) {
                        CarID car = Market.get(j);
                        if (car.getUsers() < 2 && car.getYear() > 2019) lst.add(car);
                    }
                }

                SettingFrameOnPause();
                // Ends.......................

            } else if (fbs.getFrom().equals("Used")) {

                tvDesc.setText("Used Cars");
                // Used Cars Car List
                lst = new ArrayList<CarID>();
                int k;
                if (Market != null && fbs.getUser() != null) {
                    for (k = 0; k < Market.size(); k++) {
                        CarID car = Market.get(k);
                        if (car.getUsers() >= 1 && car.getYear() < 2020) lst.add(car);
                    }
                }

                SettingFrameOnPause();
                // Ends.......................

            }
        }


        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GoToCarsMain();
                setNavigationBarVisible();
                fbs.setFrom("");

            }
        });

    }

    private boolean isConnected(){
        return ((MainActivity) getActivity()).isNetworkAvailable();
    }

    private void setNavigationBarVisible() {
        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }

    private void GoToCarsMain() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
        ft.commit();
    }

    private void SettingFrame() {

        fbs.setRcForYou(null);

        rcForYou.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new CarsAdapter(getActivity(), lst, Saved);
        rcForYou.setAdapter(Adapter);

    }

    private void SettingFrameOnPause() {

        rcForYou.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new CarsAdapter(getActivity(), lst, Saved);
        rcForYou.setAdapter(Adapter);

        if(fbs.getRcForYou()!=null){
            rcForYou.getLayoutManager().onRestoreInstanceState(fbs.getRcForYou());
        }
    }
}