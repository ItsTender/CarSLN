package com.tawfeeq.carsln;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    FireBaseServices fbs;
    TextView tvUser;
    ImageView ivSettings, ivPFP, ivSaved, ivSearch;
    String pfp;
    RecyclerView rcListings;
    ArrayList<CarID> lst;
    CarsAdapter Adapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs= FireBaseServices.getInstance();
        tvUser =getView().findViewById(R.id.tvUsername);
        ivSettings = getView().findViewById(R.id.imageViewSettings);
        ivPFP = getView().findViewById(R.id.imageViewProfilePhoto);
        ivSaved = getView().findViewById(R.id.imageViewSavedProfile);
        ivSearch = getView().findViewById(R.id.imageViewSearchProfile);
        rcListings= getView().findViewById(R.id.RecyclerListingsProfile);
        lst=new ArrayList<CarID>();


        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0,n);

        // Get User Profile Photo.....

        if(fbs.getUser()!=null) {

            tvUser.setText(fbs.getUser().getUsername());

            pfp = fbs.getUser().getUserPhoto();
            if (pfp == null || pfp.isEmpty()) {
                Picasso.get().load(R.drawable.slnpfp).into(ivPFP);
            } else {
                Picasso.get().load(pfp).into(ivPFP);
            }
        }

        // Get Profile Photo Ends


        fbs.getStore().collection("MarketPlace").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){

                    CarID car = dataSnapshot.toObject(CarID.class);
                    car.setCarPhoto(dataSnapshot.getString("photo"));
                    car.setId(dataSnapshot.getId());

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


        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToSettings();
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationBarSearch();
                GoToSearch();
            }
        });

        ivSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationBarSaved();
                GoToSaved();
            }
        });

        ivPFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GoToViewPhoto();
            }
        });

    }

    private void GoToViewPhoto() {

        if(fbs.getUser()!=null) {

            Fragment gtn= new ViewPhotoFragment();
            Bundle bundle = new Bundle();

            bundle.putString("Email", fbs.getAuth().getCurrentUser().getEmail());
            bundle.putString("Username", fbs.getUser().getUsername());
            bundle.putString("PFP", fbs.getUser().getUserPhoto());
            bundle.putString("From", "Profile");

            gtn.setArguments(bundle);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.FrameLayoutMain, gtn);
            ft.commit();
        }
    }

    private void GoToSettings() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SettingsFragment());
        ft.commit();
    }

    private void SettingFrame() {

        rcListings.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new CarsAdapter(getActivity(), lst);
        rcListings.setAdapter(Adapter);

    }

    private void setNavigationBarSearch() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.searchcar);
    }

    private void GoToSearch(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SearchFragment());
        ft.commit();
    }

    private void setNavigationBarSaved() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.savedcars);
    }

    private void GoToSaved(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SavedCarsFragment());
        ft.commit();
    }

}