package com.tawfeeq.carsln.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.objects.Report;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactUsFragment extends Fragment {

    FireBaseServices fbs;
    LinearLayout cvEmail, cvPhone;
    Spinner SpinnerReason;
    EditText etContent, etEmail;
    Button btnSubmit;
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
        etEmail = getView().findViewById(R.id.etEmail);
        SpinnerReason = getView().findViewById(R.id.SpinnerReportReason);
        etContent = getView().findViewById(R.id.etMultiLineReportContent);
        btnSubmit = getView().findViewById(R.id.btnSubmitReport);


        if(!fbs.getCurrentFragment().equals("ContactUs")) fbs.setCurrentFragment("ContactUs");


        if(fbs.getAuth().getCurrentUser()!=null){

            etEmail.setText(fbs.getAuth().getCurrentUser().getEmail());
            etEmail.setEnabled(false);

        }


        if(SpinnerReason.getSelectedItem()==null) {
            String[] Reasons = {"Select Report Reason", "Missing Car information", "Problems with CarSLN Account", "Unable to Create a CarSLN Account", "Report a Bug or Crash", "Report a Car Listing", "Report a CarSLN User", "Report Spam", "I Don't see my Report Reasoning here"};
            ArrayAdapter<String> ReasonsAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, Reasons);
            ReasonsAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
            SpinnerReason.setAdapter(ReasonsAdapter);
        }


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


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, type;
                String reason = SpinnerReason.getSelectedItem().toString();
                String content = etContent.getText().toString();


                // checks the User's Internet Connection
                if(!isConnected()) {
                    // No Connection Dialog!
                    Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_no_network);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
                    dialog.setCancelable(true);
                    dialog.show();

                    Button btn = dialog.findViewById(R.id.btnOK);

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    return;
                }

                if(fbs.getAuth().getCurrentUser()==null) {

                    email = etEmail.getText().toString();
                    type = "Guest";

                    if (email.trim().isEmpty()) {
                        Toast.makeText(getActivity(), "Missing Email Address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!isEmailValid(email)){
                        Toast.makeText(getActivity(), "Email Address is not Valid", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                else {
                    email = fbs.getAuth().getCurrentUser().getEmail();
                    type = "Registered CarSLN User";
                }

                if(reason.equals("Select Report Reason")){
                    Toast.makeText(getActivity(), "Missing Report Reason", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(content.trim().isEmpty()){
                    Toast.makeText(getActivity(), "Missing Report Content", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Sending the Report..........................
                Report report = new Report(email,type,reason,content);

                fbs.getStore().collection("Reports").add(report).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(getActivity(), "Successfully Sent your Report", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(), "Successfully Sent your Report", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private boolean isConnected(){
        return ((MainActivity) getActivity()).isNetworkAvailable();
    }

    public boolean isEmailValid(String email)
    {
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}