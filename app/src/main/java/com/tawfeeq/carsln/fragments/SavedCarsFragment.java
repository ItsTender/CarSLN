package com.tawfeeq.carsln.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tawfeeq.carsln.MainActivity;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.adapters.CarsAdapter;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.objects.UserProfile;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedCarsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedCarsFragment extends Fragment {
    RecyclerView rcListings;
    FireBaseServices fbs;
    String pfp;
    ImageView ivPFP, ivSearch;
    ArrayList<CarID> lst;
    CarsAdapter Adapter;
    UserProfile usr;
    ArrayList<String> Saved;
    ArrayList<CarID> Market;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SavedCarsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedCarsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedCarsFragment newInstance(String param1, String param2) {
        SavedCarsFragment fragment = new SavedCarsFragment();
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
        return inflater.inflate(R.layout.fragment_saved_cars, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs=FireBaseServices.getInstance();
        rcListings= getView().findViewById(R.id.RecyclerSavedCars);
        ivPFP = getView().findViewById(R.id.imageViewProfilePhotoSaved);
        ivSearch = getView().findViewById(R.id.imageViewSearchSaved);


        lst=new ArrayList<CarID>();


        if(fbs.getUser()!=null) {

            pfp = fbs.getUser().getUserPhoto();
            if (pfp == null || pfp.isEmpty()) {
                ivPFP.setImageResource(R.drawable.slnpfp);
            } else {
                Glide.with(getActivity()).load(pfp).into(ivPFP);
            }
        }

        ivPFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationBarProfile();
                GoToProfile();
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationBarSearch();
                GoToSearch();
            }
        });

        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0,n);

        if(fbs.getUser()!=null) {

            Saved = fbs.getUser().getSavedCars();

            if(fbs.getMarketList()!=null) Market = fbs.getMarketList();
            else Market = new ArrayList<CarID>();
            int i;

            if(Saved!=null) {

                for (i = 0; i < Market.size(); i++) {
                    CarID car = Market.get(i);
                    String id = Market.get(i).getId();
                    if (Saved.contains(id)) lst.add(car);
                }

            }
            SettingFrame();

        }
    }

    private void SettingFrame() {

        rcListings.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new CarsAdapter(getActivity(), lst, Saved);
        rcListings.setAdapter(Adapter);

    }

    private void GoToProfile() {
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
        ft.commit();
    }

    private void setNavigationBarProfile() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.profile);
    }

    private void setNavigationBarSearch() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.searchcar);
    }

    private void GoToSearch(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SearchFragment());
        ft.commit();
    }

}