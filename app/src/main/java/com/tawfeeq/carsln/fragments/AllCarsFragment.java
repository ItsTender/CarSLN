package com.tawfeeq.carsln.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

    ImageView ivSell,ivPFP;
    RecyclerView rc;
    private CarsAdapter adapter;
    private FireBaseServices fbs;
    private ArrayList<CarID> Market;
    ArrayList<String> Saved;
    String pfp;


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
        rc= getView().findViewById(R.id.RecyclerCars);
        Market = new ArrayList<CarID>();




        if(fbs.getUser()!=null) Saved = fbs.getUser().getSavedCars();
        else Saved = new ArrayList<String>();


        if(fbs.getUser()!=null) {

            pfp = fbs.getUser().getUserPhoto();
            if (pfp == null || pfp.isEmpty()) {
                ivPFP.setImageResource(R.drawable.slnpfp);
            } else {
                Glide.with(getActivity()).load(pfp).into(ivPFP);
            }
        }


        ivPFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationBarProfile();
                GoToProfile();
            }
        });


        // checking accessibility to FireStore Info

        if(fbs.getMarketList()==null) {

            ProgressDialog progressDialog= new ProgressDialog(getActivity());
            progressDialog.setTitle("Loading...");
            progressDialog.setMessage("Loading CarSLN MarketPlace");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIcon(R.drawable.slnround);
            progressDialog.setCancelable(false);
            progressDialog.show();

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
                    SettingFrame();

                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Couldn't Retrieve MarketPlace Info, Please Try Again Later", Toast.LENGTH_SHORT).show();
                    fbs.setMarketList(new ArrayList<CarID>());

                    progressDialog.dismiss();
                }
            });
        } else {
            Market = fbs.getMarketList();
            SettingFrame();
        }
    }

    private void SettingFrame() {

        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CarsAdapter(getActivity(), Market, Saved);
        rc.setAdapter(adapter);
    }

    private void GoToProfile() {
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
        ft.commit();
    }

    private void setNavigationBarProfile() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.profile);
    }

}


