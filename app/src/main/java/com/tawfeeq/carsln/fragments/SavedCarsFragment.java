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
import com.tawfeeq.carsln.objects.UserProfile;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedCarsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedCarsFragment extends Fragment {

    RecyclerView rcListings;
    FireBaseServices fbs;
    ArrayList<CarID> lst;
    CarsAdapter Adapter;
    UserProfile usr;
    ArrayList<String> Saved;
    ArrayList<CarID> Market;
    SwipeRefreshLayout refreshSaved;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SavedCarsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedCarsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedCarsFragment newInstance(String param1, String param2) {
        SavedCarsFragment fragment = new SavedCarsFragment();
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
        return inflater.inflate(R.layout.fragment_saved_cars, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        fbs.setRcSaved(rcListings.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs=FireBaseServices.getInstance();
        rcListings= getView().findViewById(R.id.RecyclerSavedCars);
        refreshSaved = getView().findViewById(R.id.RefreshSaved);


        if(!fbs.getCurrentFragment().equals("Saved")) fbs.setCurrentFragment("Saved");


        lst=new ArrayList<CarID>();

        if(fbs.getUser()!=null) Saved = fbs.getUser().getSavedCars();
        else Saved = new ArrayList<String>();

        if(fbs.getMarketList()!=null) Market = fbs.getMarketList();
        else Market = new ArrayList<CarID>();


        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0,n);


        refreshSaved.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (isConnected()) {

                    fbs.getStore().collection("MarketPlace").orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            Market = new ArrayList<CarID>();
                            for (DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()) {

                                CarID car = dataSnapshot.toObject(CarID.class);
                                car.setCarPhoto(dataSnapshot.getString("photo"));
                                car.setId(dataSnapshot.getId());
                                car.setTimestamp(dataSnapshot.getTimestamp("timestamp"));
                                Market.add(car);

                            }

                            fbs.setMarketList(Market);


                            if(fbs.getUser()!=null) {

                                Saved = fbs.getUser().getSavedCars();
                                lst = new ArrayList<CarID>();
                                int i, j;
                                String ID;

                                for(i=Saved.size()-1 ; i>=0 ; i--){
                                    ID = Saved.get(i);
                                    for(j=0 ; j<Market.size() ; j++){

                                        CarID car = Market.get(j);
                                        if(ID.equals(car.getId())) {
                                            lst.add(car);
                                        }
                                    }
                                }

                                SettingFrame();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Retrieve MarketPlace Info, Please Try Again Later", Toast.LENGTH_SHORT).show();
                        }
                    });

                    refreshSaved.setRefreshing(false);

                } else {
                    refreshSaved.setRefreshing(false);
                }
            }
        });


        int i, j;
        String ID;

        for(i=Saved.size()-1 ; i>=0 ; i--){
            ID = Saved.get(i);
            for(j=0 ; j<Market.size() ; j++){

                CarID car = Market.get(j);
                if(ID.equals(car.getId())) {
                    lst.add(car);
                }
            }
        }
        SettingFrameOnPause();

    }

    private void SettingFrame() {

        Adapter = new CarsAdapter(getActivity(), lst, Saved);
        rcListings.setAdapter(Adapter);
    }

    private void SettingFrameOnPause() {

        rcListings.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new CarsAdapter(getActivity(), lst, Saved);
        rcListings.setAdapter(Adapter);

        if(fbs.getRcSaved()!=null){
            rcListings.getLayoutManager().onRestoreInstanceState(fbs.getRcSaved());
        }
    }

    private boolean isConnected(){
        return ((MainActivity) getActivity()).isNetworkAvailable();
    }

    private void GoToProfile() {
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
        ft.commit();
    }

    private void setNavigationBarProfile() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.profile);
    }

    private void setNavigationBarSearch() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.searchcar);
    }

    private void GoToSearch(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SearchFragment());
        ft.commit();
    }

}