package com.tawfeeq.carsln.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.objects.FireBaseServices;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactUsFragment extends Fragment {

    FireBaseServices fbs;
    LinearLayout cvEmail, cvPhone;
    ImageView ivBack;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();
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
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        cvPhone = getView().findViewById(R.id.LayoutPhoneContact);
        cvEmail = getView().findViewById(R.id.LayoutEmailContact);
        ivBack = getView().findViewById(R.id.ContactUsGoBack);


        if(!fbs.getCurrentFragment().equals("ContactUs")) fbs.setCurrentFragment("ContactUs");


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fbs.getAuth().getCurrentUser()!=null){

                    FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();

                } else {

                    FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FrameLayoutMain, new NoUserProfileFragment());
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();

                }

            }
        });

        cvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "+972 58-412-3696"));
                startActivity(intent);

            }
        });

        cvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + "tawfeeqshahoud@gmail.com"));
                startActivity(intent);

            }
        });

    }

}