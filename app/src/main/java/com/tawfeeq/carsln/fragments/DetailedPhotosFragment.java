package com.tawfeeq.carsln.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedPhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedPhotosFragment extends Fragment {

    FireBaseServices fbs;
    String Photo,SecondPhoto,ThirdPhoto,FourthPhoto,FifthPhoto;
    ImageView ivFirst, ivSecond,ivThird, Back ,ivFourth, ivFifth;
    CarID currentCar;

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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs=FireBaseServices.getInstance();
        ivFirst = getView().findViewById(R.id.DetailedPhotosFirstCar);
        ivSecond =getView().findViewById(R.id.DetailedPhotosSecondCar);
        ivThird =getView().findViewById(R.id.DetailedPhotosThirdCar);
        ivFourth =getView().findViewById(R.id.DetailedPhotosFourthCar);
        ivFifth =getView().findViewById(R.id.DetailedPhotosFifthCar);
        Back =getView().findViewById(R.id.DetailedPhotosGoBack);


        if(!fbs.getCurrentFragment().equals("DetailedPhotos")) fbs.setCurrentFragment("DetailedPhotos");



        currentCar = fbs.getSelectedCar();


        Photo = currentCar.getPhoto();
        SecondPhoto = currentCar.getSecondphoto();
        ThirdPhoto = currentCar.getThirdPhoto();
        FourthPhoto = currentCar.getFourthPhoto();
        FifthPhoto = currentCar.getFifthPhoto();


        if (Photo == null || Photo.isEmpty()) {
            ivFirst.setImageResource(R.drawable.photo_iv);
        } else {
            Glide.with(getActivity()).load(Photo).into(ivFirst);
        }

        if (SecondPhoto == null || SecondPhoto.isEmpty()) {
            ivSecond.setImageResource(R.color.white);
            ivSecond.setVisibility(View.VISIBLE);
            ivSecond.setMaxHeight(0);
        } else {
            //Glide.with(getActivity()).load(SecondPhoto).into(ivSecond);
            Picasso.get().load(SecondPhoto).into(ivSecond);
        }

        if (ThirdPhoto == null || ThirdPhoto.isEmpty()) {
            ivThird.setImageResource(R.color.white);
            ivThird.setVisibility(View.INVISIBLE);
            ivThird.setMaxHeight(0);
        } else {
            //Glide.with(getActivity()).load(ThirdPhoto).into(ivThird);
            Picasso.get().load(ThirdPhoto).into(ivThird);
        }

        if (FourthPhoto == null || FourthPhoto.isEmpty()) {
            ivFourth.setImageResource(R.color.white);
            ivFourth.setVisibility(View.INVISIBLE);
            ivFourth.setMaxHeight(0);
        } else {
            //Glide.with(getActivity()).load(FourthPhoto).into(ivFourth);
            Picasso.get().load(FourthPhoto).into(ivFourth);
        }

        if (FifthPhoto == null || FifthPhoto.isEmpty()) {
            ivFifth.setImageResource(R.color.white);
            ivFifth.setVisibility(View.INVISIBLE);
            ivFifth.setMaxHeight(0);
        } else {
            //Glide.with(getActivity()).load(FifthPhoto).into(ivFifth);
            Picasso.get().load(FifthPhoto).into(ivFifth);
        }

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment gtn= new DetailedFragment();
                FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.FrameLayoutMain, gtn);
                ft.commit();

            }
        });

        ivFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment gtn = new MoreDetailedPhotoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("ImageNum", 1);
                gtn.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.FrameLayoutMain, gtn);
                ft.commit();
            }
        });

        ivSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment gtn = new MoreDetailedPhotoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("ImageNum", 2);
                gtn.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.FrameLayoutMain, gtn);
                ft.commit();
            }
        });

        ivThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment gtn = new MoreDetailedPhotoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("ImageNum", 3);
                gtn.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.FrameLayoutMain, gtn);
                ft.commit();
            }
        });

        ivFourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment gtn = new MoreDetailedPhotoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("ImageNum", 4);
                gtn.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.FrameLayoutMain, gtn);
                ft.commit();
            }
        });

        ivFifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment gtn = new MoreDetailedPhotoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("ImageNum", 5);
                gtn.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.FrameLayoutMain, gtn);
                ft.commit();
            }
        });

    }

    private BottomNavigationView getNavigationBar(){
        return ((MainActivity) getActivity()).getBottomNavigationView();
    }

    private void GoToMoreDetailedPhoto() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new MoreDetailedPhotoFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}