package com.tawfeeq.carsln.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.objects.FireBaseServices;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPostFragment extends Fragment {

    FireBaseServices fbs;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditPostFragment newInstance(String param1, String param2) {
        EditPostFragment fragment = new EditPostFragment();
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
        return inflater.inflate(R.layout.fragment_edit_post, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();

    }

    // TODO: add news photos or remove past set photos to the car listing. you can only remove photos from 5th to 1st so that the app assures that the Main photo isn't null and 2nd....... is not.
    // TODO: Save/Apply Changes button at the bottom of the Screen that sets the whole car listing in the FireBase to the New one with the user's desired adjustments.
    // NOTICE: set the fields with the already available data of the car, Set new Car in FireBaseCode is also available in Mazeyo.......
    // fbs.getStore().collection("MarketPlace").document(ID).set( New CarID() ).add......add); use the Loading Dialog Animation here too!
    // Edit Post Icon in Detailed Fragment (Animation: fade).

}