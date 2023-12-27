package com.tawfeeq.carsln;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    Utils utils;
    FireBaseServices fbs;
    Button btnLogout, Changepfp;
    ImageView ivUser;
    String pfp;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        utils= Utils.getInstance();
        fbs= FireBaseServices.getInstance();
        btnLogout= getView().findViewById(R.id.btnLogout);
        Changepfp = getView().findViewById(R.id.btnChangePhoto);
        ivUser = getView().findViewById(R.id.imageViewProfilePhotoSettings);


        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0,n);

        if(fbs.getUser()!=null) {

            // Get User Profile Photo.....

            pfp = fbs.getUser().getUserPhoto();

            if (pfp == null || pfp.isEmpty()) {
                Picasso.get().load(R.drawable.generic_icon).into(ivUser);
            } else {
                Picasso.get().load(pfp).into(ivUser);
            }
        }


        // Get Profile Photo Ends


        // Set New Profile Photo.....

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });

        Changepfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePFP();
            }
        });


        // Set New Profile Photo Ends

        btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Hold the Button to Logout", Toast.LENGTH_SHORT).show();
                }
            });
        btnLogout.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View view) {
                   fbs.getAuth().signOut();
                   GoToLogIn();
                   setNavigationBarGone();
                  return true;
                }
            });
    }

    private void GoToLogIn() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new LogInFragment());
        ft.commit();
    }

    private void setNavigationBarGone() {
        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.GONE);
    }

    public void ImageChooser() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 123);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 123 && resultCode == getActivity().RESULT_OK && data != null) {

            Uri selectedImageUri = data.getData();
            utils.uploadImage(getActivity(), selectedImageUri);
        }
    }

    public void UpdatePFP(){

        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0,n);

        String photo;
        if(fbs.getSelectedImageURL()==null) photo ="";
        else photo = fbs.getSelectedImageURL().toString()+".jpg";

        if (photo == "")
        {
            // Does Nothing......
        }
        else {
            Picasso.get().load(photo).into(ivUser);
        }

        if(!photo.isEmpty()) {
            fbs.getStore().collection("Users").document(user).update("userPhoto", photo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    Toast.makeText(getActivity(), "Profile Photo Updated", Toast.LENGTH_LONG).show();
                    fbs.getUser().setUserPhoto(photo);
                    fbs.setSelectedImageURL(null);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Couldn't Update Your Profile Photo, Try Again Later", Toast.LENGTH_LONG).show();
                }
            });
        }
        else Toast.makeText(getActivity(), "Press Your Profile Picture to Insert a new Image Firstly", Toast.LENGTH_LONG).show();
    }
}