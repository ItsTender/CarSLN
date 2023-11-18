package com.tawfeeq.carsln;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {

    ImageView ivCar;
    int HP, price, Yahr, userhands, Phone, KM;
    String Car,CarPhoto, transm;
    TextView CarName,HorsePower,Price, transmission,users,phone, year, kilo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
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
        View view=  inflater.inflate(R.layout.fragment_info, container, false);



        Bundle bundle= this.getArguments();

        Car= bundle.getString("Car");
        HP =bundle.getInt("HP");
        price= bundle.getInt("Price");
        CarPhoto=bundle.getString("Photo");
        Yahr= bundle.getInt("Year");
        userhands= bundle.getInt("Users");
        Phone= bundle.getInt("Phone");
        KM = bundle.getInt("Kilo");
        transm=bundle.getString("Transmission");


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        CarName =getView().findViewById(R.id.tvCarName);
        HorsePower =getView().findViewById(R.id.tvHP);
        Price=getView().findViewById(R.id.tvPrice);
        ivCar=getView().findViewById(R.id.ivDetailedImage);
        users =getView().findViewById(R.id.tvtxtUsers);
        phone=getView().findViewById(R.id.tvtxtPhone);
        kilo=getView().findViewById(R.id.tvtxtKilo);
        year=getView().findViewById(R.id.tvtxtYear);
        transmission=getView().findViewById(R.id.tvtxtTransmisson);





        if (CarPhoto == null || CarPhoto.isEmpty())
        {
            Picasso.get().load(R.drawable.carplain).into(ivCar);

        }
        else {
            Picasso.get().load(CarPhoto).into(ivCar);

        }


        CarName.setText(Car);
        HorsePower.setText("Horse Power: "+ HP);
        Price.setText("Price: " + price);
        users.setText("Users: "+userhands);
        phone.setText(Phone);
        kilo.setText("Kilometre: "+KM);
        year.setText("Model Year: "+Yahr);
        transmission.setText("Shifting Type:"+transm);

    }
}