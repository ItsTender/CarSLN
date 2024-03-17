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
    LinearLayout ContactUs, Help;

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
        ContactUs = getView().findViewById(R.id.linearLayoutcontactus);
        Help = getView().findViewById(R.id.linearLayouthelp);


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

        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHelp();
            }
        });

        ContactUs.setOnClickListener(new View.OnClickListener() {
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

    private void GoToLogin() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new LogInFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

}