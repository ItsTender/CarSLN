package com.tawfeeq.carsln;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarSearchListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarSearchListFragment extends Fragment {

    FireBaseServices fbs;
    CardView GoSearch;
    ArrayList<CarID> search;
    RecyclerView rc;
    CarsAdapter Adapter;
    ImageButton btnSearch;
    TextView tvSearch;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CarSearchListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarSearchListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarSearchListFragment newInstance(String param1, String param2) {
        CarSearchListFragment fragment = new CarSearchListFragment();
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
        View view = inflater.inflate(R.layout.fragment_car_search_list, container, false);





        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        GoSearch = getView().findViewById(R.id.cardViewsearch);
        rc = getView().findViewById(R.id.RecyclerSearch);
        btnSearch = getView().findViewById(R.id.btngoSearch);
        tvSearch = getView().findViewById(R.id.textViewsearchcustom);



        search = fbs.getCarList();
        SettingFrame();



        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoBackSearch();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoBackSearch();
            }
        });
        GoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoBackSearch();
            }
        });

    }

    private void SettingFrame() {

        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new CarsAdapter(getActivity(), search);
        rc.setAdapter(Adapter);
    }

    private void GoBackSearch(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SearchFragment());
        ft.commit();
    }

}