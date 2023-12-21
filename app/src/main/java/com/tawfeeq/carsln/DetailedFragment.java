package com.tawfeeq.carsln;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.protobuf.StringValue;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedFragment extends Fragment {

    FireBaseServices fbs;
    TextView tvName, tvPrice, tvPhone, tvPower, tvYear, tvUsers, tvKilometre, tvTransmission, tvSeller;
    ImageView ivCar, ivSeller;
    boolean sell_lend;
    String Email,Name,Phone,Photo,Transmission;
    Integer Price,Power,Year,Users,Kilometre;
    String pfp;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailedFragment newInstance(String param1, String param2) {
        DetailedFragment fragment = new DetailedFragment();
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
        View view = inflater.inflate(R.layout.fragment_detailed, container, false);

        Bundle bundle =this.getArguments();

        sell_lend=bundle.getBoolean("SellorLend");
        Email=bundle.getString("Email");
        Name=bundle.getString("Car");
        Phone=bundle.getString("Phone");
        Price=bundle.getInt("Price");
        Photo=bundle.getString("Photo");
        Power =bundle.getInt("HP");
        Year =bundle.getInt("Year");
        Users =bundle.getInt("Users");
        Kilometre=bundle.getInt("Kilo");
        Transmission=bundle.getString("Transmission");


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        ivCar=getView().findViewById(R.id.DetailedCar);
        tvName=getView().findViewById(R.id.DetailedMan);
        tvPrice =getView().findViewById(R.id.DetailedPrice);
        tvPower =getView().findViewById(R.id.DetailedHP);
        tvYear = getView().findViewById(R.id.DetailedYear);
        tvKilometre =getView().findViewById(R.id.DetailedKilometre);
        tvUsers= getView().findViewById(R.id.DetailedUsers);
        tvTransmission =getView().findViewById(R.id.DetailedGear);
        tvSeller = getView().findViewById(R.id.DetailedUserMail);
        ivSeller = getView().findViewById(R.id.imageViewSeller);


        String str = Email;
        int n = str.indexOf("@");
        String user = str.substring(0,n);
        tvSeller.setText(user);


        // Get User Profile Photo.....

        fbs.getStore().collection("ProfilePFP").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.getString("Email").equals(str)) {
                    pfp = documentSnapshot.getString("userPhoto");

                    if (pfp == null || pfp.isEmpty())
                    {
                        Picasso.get().load(R.drawable.generic_icon).into(ivSeller);
                    }
                    else {
                        Picasso.get().load(pfp).into(ivSeller);
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Couldn't Retrieve User Profile Photo", Toast.LENGTH_SHORT).show();
                Picasso.get().load(R.drawable.generic_icon).into(ivSeller);
            }
        });

        // Get Profile Photo Ends


        tvSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment gtn= new SellerPageFragment();
                Bundle bundle= new Bundle();


                bundle.putString("Email", Email);
                bundle.putString("Phone", Phone);


                gtn.setArguments(bundle);
                FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayoutMain, gtn);
                ft.commit();
            }
        });

        ivSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment gtn= new SellerPageFragment();
                Bundle bundle= new Bundle();


                bundle.putString("Email", Email);
                bundle.putString("Phone", Phone);


                gtn.setArguments(bundle);
                FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayoutMain, gtn);
                ft.commit();

            }
        });


        tvName.setText(Name);

        String prc= Price.toString();
        if(sell_lend) {
            tvPrice.setText("Selling Price: " + prc + "$");
        }
        else{
            tvPrice.setText("Monthly Payment: " + prc + "$");
        }

        String power=Power.toString(); tvPower.setText("Horse Power: " + power);
        String year=Year.toString(); tvYear.setText("Model Year: " + year);
        String Kilo =Kilometre.toString(); tvKilometre.setText("Kilometre: " + Kilo);
        String Owners= Users.toString(); tvUsers.setText("Owners: " + Owners);
        tvTransmission.setText("Transmission: " + Transmission);


        if ( Photo == null || Photo.isEmpty())
        {
            Picasso.get().load(R.drawable.carplain).into(ivCar);

        }
        else {
            Picasso.get().load(Photo).into(ivCar);

        }

    }
}