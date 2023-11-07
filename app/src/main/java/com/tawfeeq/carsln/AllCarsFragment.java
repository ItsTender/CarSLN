package com.tawfeeq.carsln;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllCarsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllCarsFragment extends Fragment {

    ImageView ivSell;
    RecyclerView rc;
    private CarsAdapter adapter;
    private FireBaseServices fbs;
    private ArrayList<Cars> Market;


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
        rc= getView().findViewById(R.id.RecyclerCars);
        Market = new ArrayList<Cars>();

        ivSell= getView().findViewById(R.id.ivFireStore);
        ivSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayoutMain, new AddCarFragment());
                ft.commit();

            }
        });

        // checking accessibility to FireStore Info
        fbs.getStore().collection("MarketPlace").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){

                    Cars car = dataSnapshot.toObject(Cars.class);
                    car.setCarPhoto(dataSnapshot.getString("photo"));
                    Market.add(car);

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

        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CarsAdapter(getActivity(), Market);
        rc.setAdapter(adapter);
        rc.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

    }
}


