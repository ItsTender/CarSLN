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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.adapters.CarsAdapter;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserListingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserListingsFragment extends Fragment {

    RecyclerView rcListings;
    FireBaseServices fbs;
    ArrayList<CarID> lst, Market;
    CarsAdapter Adapter;
    ArrayList<String> Saved;
    ImageView Back;
    SwipeRefreshLayout refreshUserListings;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserListingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserListingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserListingsFragment newInstance(String param1, String param2) {
        UserListingsFragment fragment = new UserListingsFragment();
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
        return inflater.inflate(R.layout.fragment_user_listings, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        fbs.setRcListings(rcListings.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs=FireBaseServices.getInstance();
        rcListings= getView().findViewById(R.id.RecyclerListings);
        refreshUserListings = getView().findViewById(R.id.RefreshUserListings);
        Back = getView().findViewById(R.id.UserListingsGoBack);


        if(!fbs.getCurrentFragment().equals("UserListings")) fbs.setCurrentFragment("UserListings");


        lst=new ArrayList<CarID>();

        if(fbs.getUser()!=null) Saved = fbs.getUser().getSavedCars();
        else Saved = new ArrayList<String>();


        if(fbs.getMarketList()!=null) Market = fbs.getMarketList();
        else Market = new ArrayList<CarID>();



        refreshUserListings.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                                car.setTimestamp(dataSnapshot.getTimestamp("timestamp"));
                                Market.add(car);

                            }

                            fbs.setMarketList(Market);


                            lst = new ArrayList<CarID>();
                            int i;
                            for (i = 0; i < Market.size(); i++) {
                                CarID car = Market.get(i);
                                if (car.getEmail().equals(fbs.getAuth().getCurrentUser().getEmail())) lst.add(car);
                            }

                            SettingFrame();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Retrieve MarketPlace Info, Please Try Again Later", Toast.LENGTH_SHORT).show();
                        }
                    });

                    refreshUserListings.setRefreshing(false);

                } else {
                    refreshUserListings.setRefreshing(false);
                }
            }
        });


        int i;
        if(Market!=null) {
            for (i = 0; i < Market.size(); i++) {
                CarID car = Market.get(i);
                if (car.getEmail().equals(fbs.getAuth().getCurrentUser().getEmail())) lst.add(car);
            }
        }
        SettingFrameOnPause();

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToProfile();
                fbs.setFrom("");
            }
        });

    }

    private void setNavigationBarVisible() {
        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }

    private boolean isConnected(){
        return ((MainActivity) getActivity()).isNetworkAvailable();
    }

    private void GoToProfile() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }

    private void SettingFrame() {

        fbs.setRcListings(null);

        rcListings.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new CarsAdapter(getActivity(), lst, Saved);
        rcListings.setAdapter(Adapter);

    }

    private void SettingFrameOnPause() {

        rcListings.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new CarsAdapter(getActivity(), lst, Saved);
        rcListings.setAdapter(Adapter);

        if(fbs.getRcListings()!=null){
            rcListings.getLayoutManager().onRestoreInstanceState(fbs.getRcListings());
        }
    }

}