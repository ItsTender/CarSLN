package com.tawfeeq.carsln.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.adapters.CarsAdapter;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.objects.InfoMessage;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    FireBaseServices fbs;
    InfoMessage infomsg;
    TextView tvUser, tvEmail;
    ImageView ivPFP;
    String pfp;
    ArrayList<CarID> lst;
    CarsAdapter Adapter;
    ArrayList<String> Saved;
    ArrayList<CarID> Market;
    Button addcar;
    LinearLayout userlistings, saved, search, settings, logout;


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
        infomsg = InfoMessage.getInstance();
        tvUser =getView().findViewById(R.id.tvUsername);
        tvEmail = getView().findViewById(R.id.textViewEmail);
        ivPFP = getView().findViewById(R.id.imageViewProfilePhoto);
        addcar = getView().findViewById(R.id.btnAddProfile);
        userlistings = getView().findViewById(R.id.linearLayoutuserlinstings);
        saved = getView().findViewById(R.id.linearLayoutSavedCars);
        search = getView().findViewById(R.id.linearLayoutsearch);
        settings = getView().findViewById(R.id.linearLayoutsettings);
        logout = getView().findViewById(R.id.linearLayoutlogout);


        if(!fbs.getCurrentFragment().equals("Profile")) fbs.setCurrentFragment("Profile");


        lst=new ArrayList<CarID>();


        if(fbs.getUser()!=null) Saved = fbs.getUser().getSavedCars();
        else Saved = new ArrayList<String>();


        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0,n);

        // Get User Profile Photo.....

        if(fbs.getUser()!=null) {

            tvUser.setText(fbs.getUser().getUsername());
            tvEmail.setText(fbs.getAuth().getCurrentUser().getEmail());

            pfp = fbs.getUser().getUserPhoto();
            if (pfp == null || pfp.isEmpty()) {
                ivPFP.setImageResource(R.drawable.slnpfp);
            } else {
                Glide.with(getActivity()).load(pfp).into(ivPFP);
            }
        }

        // Get Profile Photo Ends


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToSettings();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationBarSearch();
                GoToSearch();
            }
        });

        addcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationBarAddCar();
                GoToAddCar();
            }
        });

        userlistings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fbs.setFrom("UserListings");
                GoToUserListings();
            }
        });

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationBarSaved();
                GoToSaved();
            }
        });

        ivPFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fbs.getUser()!=null && !fbs.getUser().getUserPhoto().equals("")) {
                    setNavigationBarGone();
                    GoToViewPhoto();
                }
            }
        });

        // Custom Logout Dialog!
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_logout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog.setCancelable(true);

        Button btnLogout = dialog.findViewById(R.id.btnConfirmLogout);
        Button btnCancel = dialog.findViewById(R.id.btnCancelLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbs.getAuth().signOut();
                fbs.setMarketList(null);
                GoToLogIn();
                setNavigationBarGone();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

    }

    private void GoToLogIn() {

        FragmentManager ftm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new LogInFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void setNavigationBarGone() {
        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.GONE);
    }

    private void GoToUserListings() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new UserListingsFragment());
        ft.commit();
    }

    private void GoToViewPhoto() {

        if(fbs.getUser()!=null) {

            Fragment gtn= new ViewPhotoFragment();
            Bundle bundle = new Bundle();

            bundle.putString("Email", fbs.getAuth().getCurrentUser().getEmail());
            bundle.putString("Username", fbs.getUser().getUsername());
            bundle.putString("PFP", fbs.getUser().getUserPhoto());
            fbs.setFrom("Profile");

            gtn.setArguments(bundle);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.FrameLayoutMain, gtn);
            ft.commit();
        }
    }

    private void GoToSettings() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SettingsFragment());
        ft.commit();
    }

    private void setNavigationBarSearch() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.searchcar);
    }

    private void GoToSearch(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SearchFragment());
        ft.commit();
    }

    private void setNavigationBarAddCar() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.addcar);
    }

    private void GoToAddCar(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AddCarFragment());
        ft.commit();
    }

    private void setNavigationBarSaved() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.savedcars);
    }

    private void GoToSaved(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SavedCarsFragment());
        ft.commit();
    }

}