package com.tawfeeq.carsln;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerPageFragment extends Fragment {

    String Email, CarName;
    Button btnSMS, btnCall, btnWhatsapp;
    TextView tvSellerName, tvintro;
    RecyclerView rc;
    FireBaseServices fbs;
    CarsAdapter Adapter;
    ArrayList<CarID> SellerCars;
    ImageView ivSeller;
    String pfp;
    UserProfile usr;
    private static final int PERMISSION_SEND_SMS = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SellerPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerPageFragment newInstance(String param1, String param2) {
        SellerPageFragment fragment = new SellerPageFragment();
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
        View view =  inflater.inflate(R.layout.fragment_seller_page, container, false);

        Bundle bundle =this.getArguments();

        Email=bundle.getString("Email");
        CarName =bundle.getString("CarName");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs= FireBaseServices.getInstance();
        tvSellerName = getView().findViewById(R.id.tvSellerUser);
        tvintro = getView().findViewById(R.id.tvsellerintro);
        btnCall = getView().findViewById(R.id.btnCallSeller);
        btnSMS = getView().findViewById(R.id.btnSMSContact);
        btnWhatsapp =getView().findViewById(R.id.btnWhatsapp);
        rc = getView().findViewById(R.id.RecyclerSellerCars);
        ivSeller = getView().findViewById(R.id.imageViewSellerPage);

        SellerCars =new ArrayList<CarID>();

        String str = Email;
        int n = str.indexOf("@");
        String user = str.substring(0,n);

        // Get User Profile.....

        fbs.getStore().collection("Users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                    usr = documentSnapshot.toObject(UserProfile.class);

                tvSellerName.setText(usr.getUsername() +"'s Page");
                tvintro.setText("Here You Can See his Car Listings and Contact him");

                pfp = usr.getUserPhoto();
                    if (pfp == null || pfp.isEmpty())
                    {
                        Picasso.get().load(R.drawable.slnpfp).into(ivSeller);
                    }
                    else {
                        Picasso.get().load(pfp).into(ivSeller);
                    }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Couldn't Retrieve User Profile Photo", Toast.LENGTH_SHORT).show();
                Picasso.get().load(R.drawable.slnpfp).into(ivSeller);
            }
        });

        // Get Profile Ends


        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // only fills in the Phone number in the Phone App....
                if(usr!=null) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + usr.getPhone()));
                    startActivity(intent);
                }
            }
        });

        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Sends a full on Message to the seller in the SMS App....
                if(usr!=null) PermissionSendSMS();
            }
        });

        btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(usr!=null) {

                    String phoneNumber = usr.getPhone();
                    String message = "Hello, I Saw Your " + CarName + " Listed for Sale On CarSLN and I'm Interested in it";

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + message));
                    startActivity(intent);
                }
            }
        });


        fbs.getStore().collection("MarketPlace").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){

                    CarID car = dataSnapshot.toObject(CarID.class);
                    car.setCarPhoto(dataSnapshot.getString("photo"));
                    car.setId(dataSnapshot.getId());

                    if(car.getEmail().equals(Email)) SellerCars.add(car);

                }
                SettingFrame();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Couldn't Retrieve Info, Please Try Again Later!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void SettingFrame() {

        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new CarsAdapter(getActivity(), SellerCars);
        rc.setAdapter(Adapter);

    }


//    private boolean IsWhatsappInstalled(){
//
//        boolean isInstalled;
//
//        try{
//
//            getActivity().getPackageManager().getPackageInfo("com.whatsapp", 0);
//            isInstalled = true;
//
//        }catch (PackageManager.NameNotFoundException e){
//
//            isInstalled = false;
//        }
//        return isInstalled;
//    }


    private void PermissionSendSMS() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
        } else {
            SendSMS();
        }
    }

    private void SendSMS() {

        String phoneNumber = usr.getPhone();
        String message = "Hello, I Saw Your " + CarName +" Listed for Sale On CarSLN and I'm Interested in it";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getActivity(), "Successfully Sent a Message to The Seller, Check The SMS App", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Failed to Send SMS to The Seller", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SendSMS();
        } else {
            Toast.makeText(requireContext(), "Permission Denied, Cannot Send SMS", Toast.LENGTH_SHORT).show();
        }
    }

}