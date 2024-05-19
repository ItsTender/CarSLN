package com.tawfeeq.carsln.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.objects.FireBaseServices;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoUserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoUserProfileFragment extends Fragment {

    FireBaseServices fbs;
    Button btn;
    ImageView twitter, github, discord;
    LinearLayout userlistings, saved, chats, search, settings, logout, help, contactus;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NoUserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoUserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoUserProfileFragment newInstance(String param1, String param2) {
        NoUserProfileFragment fragment = new NoUserProfileFragment();
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
        return inflater.inflate(R.layout.fragment_no_user_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        btn = getView().findViewById(R.id.btnConnectLogin);
        userlistings = getView().findViewById(R.id.linearLayoutuserlinstings);
        saved = getView().findViewById(R.id.linearLayoutSavedCars);
        search = getView().findViewById(R.id.linearLayoutsearch);
        settings = getView().findViewById(R.id.linearLayoutsettings);
        contactus = getView().findViewById(R.id.linearLayoutcontactus);
        help = getView().findViewById(R.id.linearLayouthelp);
        chats = getView().findViewById(R.id.linearLayoutChats);


        if(!fbs.getCurrentFragment().equals("NoUserProfile")) fbs.setCurrentFragment("NoUserProfile");


        // Additional Links..............
        twitter = getView().findViewById(R.id.TwitterLogo);
        github = getView().findViewById(R.id.GithubLogo);

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String website = "https://twitter.com/TenderOn240HZ";
                Uri uri = Uri.parse(website);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String website = "https://github.com/ItsTender";
                Uri uri = Uri.parse(website);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        //Links End..................................................


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.GONE);
                GoToLogin();
            }
        });

        userlistings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToNoUserListings();
            }
        });

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationBarSaved();
                GoToNoUserSaved();
            }
        });

        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToNoUserChats();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToNoUserSettings();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationBarSearch();
                GoToSearch();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHelp();
            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToContactUs();
            }
        });

    }

    private void GoToContactUs() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ContactUsFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void GoToHelp() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new HelpFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void GoToNoUserListings() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new NoUserListingsFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void GoToNoUserChats() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new NoUserChatsFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void GoToNoUserSettings() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new NoUserSettingsFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void setNavigationBarSearch() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.searchcar);
    }

    private void GoToSearch(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SearchFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void setNavigationBarAddCar() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.addcar);
    }

    private void setNavigationBarSaved() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.savedcars);
    }

    private void GoToNoUserSaved(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new NoUserSavedFragment());
        ft.commit();
    }

    private void GoToLogin() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new LogInFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

}