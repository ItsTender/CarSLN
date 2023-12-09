package com.tawfeeq.carsln;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserListingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserListingsFragment extends Fragment {

    RecyclerView rcListings;
    FireBaseServices fbs;
    ArrayList<Cars> lst;
    CarsAdapter Adapter;

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
    public void onStart() {
        super.onStart();

        fbs=FireBaseServices.getInstance();
        rcListings= getView().findViewById(R.id.RCListings);


        lst=new ArrayList<Cars>();

        fbs.getStore().collection("MarketPlace").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){

                    Cars car = dataSnapshot.toObject(Cars.class);
                    car.setCarPhoto(dataSnapshot.getString("photo"));
                    car.setPhone(dataSnapshot.getString("phone"));
                    if(car.getEmail().equals(fbs.getAuth().getCurrentUser().getEmail())) {
                        lst.add(car);
                    }
                }
                SettingFrame();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Couldn't Retrieve Info, Please Try Again Later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SettingFrame() {

        rcListings.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new CarsAdapter(getActivity(), lst);
        rcListings.setAdapter(Adapter);

    }
}