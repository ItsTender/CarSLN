package com.tawfeeq.carsln.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {

    FireBaseServices fbs;
    Button btnLog, btnSignup;
    EditText etEmail, etPassword;
    TextView tvForgot;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
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
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }




    @Override
    public void onStart() {
        super.onStart();

        fbs=FireBaseServices.getInstance();
        btnLog=getView().findViewById(R.id.btnLogIn);
        etEmail =getView().findViewById(R.id.etEmailSignIn);
        etPassword =getView().findViewById(R.id.etPasswordSignIn);
        btnSignup =getView().findViewById(R.id.tvSignUp);
        tvForgot =getView().findViewById(R.id.tvForgot);


        if(!fbs.getCurrentFragment().equals("Login")) fbs.setCurrentFragment("Login");


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToSignup();
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToForgotPass();
            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etEmail.getText().toString();
                String pass = etPassword.getText().toString();
                if (username.trim().isEmpty() || pass.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Email Or Password Field is Missing", Toast.LENGTH_SHORT).show();
                    return;
                }


                Dialog loading = new Dialog(getActivity());
                loading.setContentView(R.layout.loading_dialog);
                loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loading.setCancelable(false);
                loading.show();

                if (fbs.getAuth().getCurrentUser() == null) {
                    fbs.getAuth().signInWithEmailAndPassword(username, pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // To DO
                                Toast.makeText(getActivity(), "Welcome Back", Toast.LENGTH_LONG).show();
                                fbs.setUser(null);
                                setNewSavedandGoToMaketPlace();
                                setNavigationBarCarsMarket();
                                setNavigationBarVisible();
                            } else {
                                // To DO
                                Toast.makeText(getActivity(), "Log In Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                loading.dismiss();
            }
        });
    }



    private void GoToFragmentCars() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
        ft.commit();
    }

    private void GoToSignup() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SignUpFragment());
        ft.commit();
    }

    private void GoToForgotPass() {
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ForgotPassFragment());
        ft.commit();
    }

    private void setNavigationBarVisible() {
        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }

    private void setNavigationBarCarsMarket() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.market);
    }

    private void setNewSavedandGoToMaketPlace(){
        ((MainActivity) getActivity()).setSavedGoToMarket();
    }

}
