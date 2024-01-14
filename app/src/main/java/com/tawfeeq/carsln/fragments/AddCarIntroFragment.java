package com.tawfeeq.carsln.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tawfeeq.carsln.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCarIntroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCarIntroFragment extends Fragment {

    Button btnAddCar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddCarIntroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCarIntroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCarIntroFragment newInstance(String param1, String param2) {
        AddCarIntroFragment fragment = new AddCarIntroFragment();
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
        return inflater.inflate(R.layout.fragment_add_car_intro, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        btnAddCar = getView().findViewById(R.id.btnAddCar);

        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayoutMain, new AddCarFragment());
                ft.commit();
            }
        });


    }

}