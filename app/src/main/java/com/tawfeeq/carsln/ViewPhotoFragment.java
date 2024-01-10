package com.tawfeeq.carsln;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewPhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPhotoFragment extends Fragment {

    boolean sell_lend;
    String Email,Man, Mod, Photo,Transmission,Engine,ID,Color,Location,NextTest,SecondPhoto,ThirdPhoto,FourthPhoto,FifthPhoto,Notes;
    Integer Price,Power,Year,Users,Kilometre;
    FireBaseServices fbs;
    String username, pfp, From;
    TextView tvUsername;
    ImageView ivUser, Back, ChangePFP;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewPhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPhotoFragment newInstance(String param1, String param2) {
        ViewPhotoFragment fragment = new ViewPhotoFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_photo, container, false);

        Bundle bundle =this.getArguments();


        ID=bundle.getString("ID");
        sell_lend=bundle.getBoolean("SellorLend");
        Man=bundle.getString("Man");
        Mod=bundle.getString("Mod");
        Price=bundle.getInt("Price");
        Photo=bundle.getString("Photo");
        SecondPhoto=bundle.getString("Second");
        ThirdPhoto=bundle.getString("Third");
        FourthPhoto= bundle.getString("Fourth");
        FifthPhoto=bundle.getString("Fifth");
        Power =bundle.getInt("HP");
        Engine =bundle.getString("Engine");
        Year =bundle.getInt("Year");
        Users =bundle.getInt("Users");
        Kilometre=bundle.getInt("Kilo");
        Transmission=bundle.getString("Transmission");
        Color=bundle.getString("Color");
        Location=bundle.getString("Area");
        NextTest=bundle.getString("Test");
        Notes=bundle.getString("Notes");
        Email = bundle.getString("Email");
        username = bundle.getString("Username");
        pfp = bundle.getString("PFP");
        From = bundle.getString("From");


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        tvUsername = getView().findViewById(R.id.tvUsernameViewPhoto);
        ivUser = getView().findViewById(R.id.ivViewProfilePhoto);
        Back = getView().findViewById(R.id.ViewPhotoGoBack);
        ChangePFP = getView().findViewById(R.id.ViewPhotoChangePFP);


        if(Email.equals(fbs.getAuth().getCurrentUser().getEmail())){

            ChangePFP.setVisibility(View.VISIBLE);

            ChangePFP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageChooser();
                }
            });


        } else ChangePFP.setVisibility(View.INVISIBLE);



        tvUsername.setText(username);

        if (pfp == null || pfp.isEmpty()) {
            Picasso.get().load(R.drawable.slnpfp).into(ivUser);
        } else {
            Picasso.get().load(pfp).into(ivUser);
        }

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(From.equals("Profile")) GoToProfile();
                else if(From.equals("Settings")) GoToSettings();
                else if(From.equals("Seller")) GoToSellerPage();
            }
        });

    }

    private void GoToSellerPage() {

        Fragment gtn = new SellerPageFragment();
        Bundle bundle = new Bundle();


        bundle.putString("ID", ID);
        bundle.putBoolean("SellorLend", sell_lend);
        bundle.putString("Email", Email);
        bundle.putString("Man", Man);
        bundle.putString("Mod", Mod);
        bundle.putInt("HP", Power);
        bundle.putInt("Price", Price);
        bundle.putString("Photo", Photo);
        bundle.putString("Second", SecondPhoto);
        bundle.putString("Third", ThirdPhoto);
        bundle.putString("Engine", Engine);
        bundle.putString("Transmission", Transmission);
        bundle.putInt("Year", Year);
        bundle.putInt("Kilo", Kilometre);
        bundle.putInt("Users", Users);
        bundle.putString("Color", Color);
        bundle.putString("Area", Location);
        bundle.putString("Test", NextTest);
        bundle.putString("Notes", Notes);
        bundle.putString("Fourth", FourthPhoto);
        bundle.putString("Fifth", FifthPhoto);


        gtn.setArguments(bundle);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, gtn);
        ft.commit();

    }

    private void GoToSettings() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SettingsFragment());
        ft.commit();
    }

    private void GoToProfile() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
        ft.commit();
    }

    public void ImageChooser() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 123);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 123 && resultCode == getActivity().RESULT_OK && data != null) {

            Uri selectedImageUri = data.getData();
            uploadImageandSave(getActivity(), selectedImageUri);
        }
    }

    public void uploadImageandSave(Context context, Uri selectedImageUri) {
        if (selectedImageUri != null) {

            ProgressDialog progressDialog= new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.setMessage("Uploading New Profile Picture Image, Please Wait!");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIcon(R.drawable.slnround);
            progressDialog.show();


            String imageStr = "images/" + UUID.randomUUID() + ".jpg"; //+ selectedImageUri.getLastPathSegment();
            StorageReference imageRef = fbs.getStorage().getReference().child("images/" + selectedImageUri.getLastPathSegment());

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            fbs.setSelectedImageURL(uri);
                            if(fbs.getUser() != null) UpdatePFP();
                            else Toast.makeText(context, "Couldn't Update Your Profile Photo, Try Again Later", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Utils: uploadImage: ", e.getMessage());
                        }
                    });

                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Choose an Image", Toast.LENGTH_SHORT).show();
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