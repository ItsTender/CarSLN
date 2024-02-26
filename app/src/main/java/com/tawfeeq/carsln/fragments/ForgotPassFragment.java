package com.tawfeeq.carsln.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgotPassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgotPassFragment extends Fragment {

    FireBaseServices fbs;
    Button btnReset, btnSignup;
    TextView tvLog;
    ImageView Back, Close;
    EditText etEmail;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ForgotPassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForgotPassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForgotPassFragment newInstance(String param1, String param2) {
        ForgotPassFragment fragment = new ForgotPassFragment();
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
        return inflater.inflate(R.layout.fragment_forgot_pass, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();


        fbs=FireBaseServices.getInstance();
        btnReset =getView().findViewById(R.id.btnResetPass);
        btnSignup = getView().findViewById(R.id.tvSignUp);
        tvLog=getView().findViewById(R.id.tvLoginForgot);
        etEmail=getView().findViewById(R.id.etEmailForgotPass);
        Back = getView().findViewById(R.id.ForgotPassGoBack);
        Close = getView().findViewById(R.id.ForgotPassClose);


        if(!fbs.getCurrentFragment().equals("Forgot")) fbs.setCurrentFragment("Forgot");


        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToLoginFragment();
            }
        });

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNavigationBarCarsMarket();
                GoToNoUserHome();
                setNavigationBarVisible();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToSignup();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    // Data Validation ( The Given Info is Correct )
                    String Mail = etEmail.getText().toString();
                    if (Mail.trim().isEmpty()) {
                        Toast.makeText(getActivity(), "Email Field is Missing", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                fbs.getAuth().sendPasswordResetEmail(etEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {

                            Toast.makeText(getActivity(), "Password Reset Email has been Sent, Check Your Inbox", Toast.LENGTH_LONG).show();

                        }
                        else
                        {

                            Toast.makeText(getActivity(), "Sending Failed, Check the Email Address You Entered", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });

        tvLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToLoginFragment();
            }
        });

    }

    private void GoToLoginFragment() {
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new LogInFragment());
        ft.commit();
    }

    private void GoToSignup() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SignUpFragment());
        ft.commit();
    }

    private void GoToNoUserHome() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.FrameLayoutMain, new NoUserHomeFragment());
        ft.commit();
    }

    private void setNavigationBarVisible() {
        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }

    private void setNavigationBarGone() {
        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.GONE);
    }

    private void setNavigationBarCarsMarket() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.market);
    }

}