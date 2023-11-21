package com.tawfeeq.carsln;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    FireBaseServices fbs;
    Button btnSign;
    EditText etEmail, etPassword;
    TextView etLog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
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
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs =FireBaseServices.getInstance();
        etEmail = getView().findViewById(R.id.etEmailSignup);
        etPassword = getView().findViewById(R.id.etPasswordSignup);
        btnSign = getView().findViewById(R.id.btnLogOut);
        etLog = getView().findViewById(R.id.tvSignin);


        etLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToLogIn();
            }
        });

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etEmail.getText().toString();
                String pass = etPassword.getText().toString();
                if (username.trim().isEmpty() || pass.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Some Field Are Missing!", Toast.LENGTH_SHORT).show();
                    return;
                }

                fbs.getAuth().createUserWithEmailAndPassword(username, pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            // To DO
                            Toast.makeText(getActivity(), "Sign Up Successful", Toast.LENGTH_LONG).show();
                            GoToLogIn();
                        }
                        else
                        {
                            // To DO
                            Toast.makeText(getActivity(), "Sign Up Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    private void GoToLogIn() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new LogInFragment());
        ft.commit();
    }

}