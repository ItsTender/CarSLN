package com.tawfeeq.carsln.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.MainActivity;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.objects.UserProfile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    FireBaseServices fbs;
    Button btnSignUp;
    EditText etUsername, etEmail, etPassword, etConfirm, etPhone;
    TextView etLog;
    Spinner SpinnerLocation;

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
        etUsername = getView().findViewById(R.id.etUsernameSignup);
        etEmail = getView().findViewById(R.id.etEmailSignup);
        etPassword = getView().findViewById(R.id.etPasswordSignup);
        etConfirm = getView().findViewById(R.id.etPasswordSignupConfirm);
        etPhone = getView().findViewById(R.id.etPhoneSignup);
        btnSignUp = getView().findViewById(R.id.btnSignUp);
        etLog = getView().findViewById(R.id.tvSignin);
        SpinnerLocation = getView().findViewById(R.id.SpinnerLocationAreaSignup);


        String [] Location = {"Select Your District","Golan","Galil","Haifa","Central","Tel Aviv","Jerusalem","Be'er Sheva","Central Southern","Eilat"};
        ArrayAdapter<String> LocationAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, Location);
        LocationAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        SpinnerLocation.setAdapter(LocationAdapter);


        etLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToLogIn();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etEmail.getText().toString();
                String Name = etUsername.getText().toString();
                String pass = etPassword.getText().toString();
                String confirm = etConfirm.getText().toString();
                String phone = etPhone.getText().toString();
                String location = SpinnerLocation.getSelectedItem().toString();
                if (username.trim().isEmpty() || Name.trim().isEmpty() || pass.trim().isEmpty() || confirm.trim().isEmpty()||phone.trim().isEmpty()||location.equals("Select Your District")) {
                    Toast.makeText(getActivity(), "Some Field Are Missing", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(pass.equals(confirm) && fbs.getAuth().getCurrentUser()==null) {
                    fbs.getAuth().createUserWithEmailAndPassword(username, pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                CreateUserProfile(username.toLowerCase(), Name ,phone, location);
                                LogIn(username, pass);
                            } else {
                                Toast.makeText(getActivity(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Password Credentials Do Not Match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CreateUserProfile(String name, String username ,String phone, String location) {

        String str = name;
        int n = str.indexOf("@");
        String user = str.substring(0,n);

        UserProfile userProfile = new UserProfile("",username,phone,location);
        fbs.getStore().collection("Users").document(user).set(userProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // creates a User with an Empty Profile Photo / Custom Username / Phone Number .......
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Something Goes Wrong
            }
        });
    }

    private void GoToLogIn() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new LogInFragment());
        ft.commit();
    }

    private void LogIn(String user, String password ) {

        fbs.getAuth().signInWithEmailAndPassword(user,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Welcome To CarSLN", Toast.LENGTH_LONG).show();
                    setNavigationBarVisible();
                    setNavigationBarCarsMarket();
                    setNewSavedandGoToMaketPlace();
                } else {
                    Toast.makeText(getActivity(), "Log In Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void GoToFragmentCars() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
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