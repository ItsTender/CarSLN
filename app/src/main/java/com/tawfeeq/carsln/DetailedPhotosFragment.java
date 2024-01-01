package com.tawfeeq.carsln;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedPhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedPhotosFragment extends Fragment {

    boolean sell_lend;
    String Email,Man, Mod, Photo,Transmission,Engine,ID,Color,Location,NextTest,SecondPhoto,ThirdPhoto;
    Integer Price,Power,Year,Users,Kilometre;
    ImageView ivFirst, ivSecond,ivThird, Back;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailedPhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailedPhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailedPhotosFragment newInstance(String param1, String param2) {
        DetailedPhotosFragment fragment = new DetailedPhotosFragment();
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
        View view = inflater.inflate(R.layout.fragment_detailed_photos, container, false);


        Bundle bundle =this.getArguments();


        ID=bundle.getString("ID");
        sell_lend=bundle.getBoolean("SellorLend");
        Email=bundle.getString("Email");
        Man=bundle.getString("Man");
        Mod=bundle.getString("Mod");
        Price=bundle.getInt("Price");
        Photo=bundle.getString("Photo");
        SecondPhoto=bundle.getString("Second");
        ThirdPhoto=bundle.getString("Third");
        Power =bundle.getInt("HP");
        Engine =bundle.getString("Engine");
        Year =bundle.getInt("Year");
        Users =bundle.getInt("Users");
        Kilometre=bundle.getInt("Kilo");
        Transmission=bundle.getString("Transmission");
        Color=bundle.getString("Color");
        Location=bundle.getString("Area");
        NextTest=bundle.getString("Test");


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ivFirst = getView().findViewById(R.id.DetailedPhotosFirstCar);
        ivSecond =getView().findViewById(R.id.DetailedPhotosSecondCar);
        ivThird =getView().findViewById(R.id.DetailedPhotosThirdCar);
        Back =getView().findViewById(R.id.DetailedPhotosGoBack);


        if (Photo == null || Photo.isEmpty()) {
            Picasso.get().load(R.drawable.photo_iv).into(ivFirst);
        } else {
            Picasso.get().load(Photo).into(ivFirst);
        }

        if (SecondPhoto == null || SecondPhoto.isEmpty()) {
            ivSecond.setImageResource(R.color.white);
            ivSecond.setVisibility(View.VISIBLE);
            ivSecond.setMaxHeight(0);
        } else {
            Picasso.get().load(SecondPhoto).into(ivSecond);
        }

        if (ThirdPhoto == null || ThirdPhoto.isEmpty()) {
            ivThird.setImageResource(R.color.white);
            ivThird.setVisibility(View.INVISIBLE);
            ivThird.setMaxHeight(0);
        } else {
            Picasso.get().load(ThirdPhoto).into(ivThird);
        }

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment gtn= new DetailedFragment();
                Bundle bundle= new Bundle();


                bundle.putString("ID", ID);
                bundle.putBoolean("SellorLend", sell_lend);
                bundle.putString("Email", Email);
                bundle.putString("Man", Man);
                bundle.putString("Mod", Mod);
                bundle.putInt("HP", Power);
                bundle.putInt("Price", Price);
                bundle.putString("Photo", Photo);
                bundle.putString("Second", SecondPhoto);
                bundle.putString("Third", ThirdPhoto);
                bundle.putString("Engine", Engine);
                bundle.putString("Transmission", Transmission);
                bundle.putInt("Year", Year);
                bundle.putInt("Kilo", Kilometre);
                bundle.putInt("Users", Users);
                bundle.putString("Color", Color);
                bundle.putString("Area", Location);
                bundle.putString("Test", NextTest);


                gtn.setArguments(bundle);
                FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayoutMain, gtn);
                ft.commit();

            }
        });

    }
}