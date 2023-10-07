package com.tawfeeq.carsln;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCarFragment extends Fragment {

    FireBaseServices fbs;
    Cars AddCar;
    TextView Model,Manufacturer,Engine,BHP;
    Button Add;
    ImageView IV;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddCarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCarFragment newInstance(String param1, String param2) {
        AddCarFragment fragment = new AddCarFragment();
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
        return inflater.inflate(R.layout.fragment_add_car, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs=FireBaseServices.getInstance();
        Manufacturer = getView().findViewById(R.id.etMan);
        Model = getView().findViewById(R.id.etMod);
        Engine = getView().findViewById(R.id.etEng);
        BHP = getView().findViewById(R.id.etBHP);
        IV = getView().findViewById(R.id.imageView);
        Add = getView().findViewById(R.id.btnAdd);

        IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageChooser();
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check info (....)
                String Man= Manufacturer.getText().toString();
                String Mod= Model.getText().toString();
                String Eng= Engine.getText().toString();
                String HP= BHP.getText().toString();
                if(Man.trim().isEmpty()||Mod.trim().isEmpty()||HP.trim().isEmpty()||Eng.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Some Fields Are Missing!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Adding the Car
                Integer power= Integer.parseInt(HP);
                String cp="carPhoto.png";
                AddCar= new Cars(Man,Mod,Eng,power,cp);
                FirebaseFirestore db= fbs.getStore();
                db.collection("Cars").document().set(AddCar)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Car Added to FireBase", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Failed to Add Car to FireBase", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    public void ImageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i,"SELECT PICTURE"),200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        Uri SIuri = data.getData();
        IV.setImageURI(SIuri);
    }

}