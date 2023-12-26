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
    ImageView ivSettings, ivPFP;
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
        rcListings= getView().findViewById(R.id.RecyclerListingsProfile);
        lst=new ArrayList<CarID>();


        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0,n);

        // Get User Profile Photo.....

        fbs.getStore().collection("Users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                tvUser.setText("Welcome, " + documentSnapshot.getString("username"));

                pfp = documentSnapshot.getString("userPhoto");

                if (pfp == null || pfp.isEmpty())
                {
                    Picasso.get().load(R.drawable.generic_icon).into(ivPFP);
                }
                else {
                    Picasso.get().load(pfp).into(ivPFP);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Couldn't Retrieve User Profile Photo", Toast.LENGTH_SHORT).show();
                Picasso.get().load(R.drawable.generic_icon).into(ivPFP);
            }
        });

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

}