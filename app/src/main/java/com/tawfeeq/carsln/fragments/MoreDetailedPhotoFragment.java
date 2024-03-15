package com.tawfeeq.carsln.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.objects.FireBaseServices;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreDetailedPhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreDetailedPhotoFragment extends Fragment {

    FireBaseServices fbs;
    ImageView Back, ivPhoto;
    CarID currentCar;
    int selectedImage;
    String Photo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoreDetailedPhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreDetailedPhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreDetailedPhotoFragment newInstance(String param1, String param2) {
        MoreDetailedPhotoFragment fragment = new MoreDetailedPhotoFragment();
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
        View view = inflater.inflate(R.layout.fragment_more_detailed_photo, container, false);

        Bundle bundle = getArguments();

        selectedImage = bundle.getInt("ImageNum");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        Back = getView().findViewById(R.id.MoreDetailedGoBack);
        ivPhoto = getView().findViewById(R.id.MoreDetailedPhoto);


        if(fbs.getSelectedCar()!=null) currentCar = fbs.getSelectedCar();
        else{
            startActivity(new Intent(getContext(), MainActivity.class));
        }

        if(!fbs.getCurrentFragment().equals("MoreDetailedPhoto")) fbs.setCurrentFragment("MoreDetailedPhoto");


        if(selectedImage==1){

            Photo = currentCar.getPhoto();
            if (Photo == null || Photo.isEmpty()) {
                ivPhoto.setImageResource(R.drawable.photo_iv_null);
            } else {
                Glide.with(getActivity()).load(Photo).into(ivPhoto);
            }

        }else if(selectedImage==2){

            Photo = currentCar.getSecondphoto();
            if (Photo == null || Photo.isEmpty()) {
                ivPhoto.setImageResource(R.drawable.photo_iv_null);
            } else {
                Glide.with(getActivity()).load(Photo).into(ivPhoto);
            }

        }else if(selectedImage==3){

            Photo = currentCar.getThirdPhoto();
            if (Photo == null || Photo.isEmpty()) {
                ivPhoto.setImageResource(R.drawable.photo_iv_null);
            } else {
                Glide.with(getActivity()).load(Photo).into(ivPhoto);
            }

        }else if(selectedImage==4){

            Photo = currentCar.getFourthPhoto();
            if (Photo == null || Photo.isEmpty()) {
                ivPhoto.setImageResource(R.drawable.photo_iv_null);
            } else {
                Glide.with(getActivity()).load(Photo).into(ivPhoto);
            }

        }else if(selectedImage==5){

            Photo = currentCar.getFifthPhoto();
            if (Photo == null || Photo.isEmpty()) {
                ivPhoto.setImageResource(R.drawable.photo_iv_null);
            } else {
                Glide.with(getActivity()).load(Photo).into(ivPhoto);
            }

        }

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentCar.getSecondphoto() == null || currentCar.getSecondphoto().isEmpty() || currentCar.getSecondphoto().equals("")) GoToDetailed(); // Car Listing only has one Photo.
                else GoToDetailedPhotos(); // Car Listing more than one Photo.
            }
        });

    }

    private void GoToDetailedPhotos() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new DetailedPhotosFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void GoToDetailed(){

        Fragment gtn= new DetailedFragment();
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.FrameLayoutMain, gtn);
        ft.commit();
    }


}