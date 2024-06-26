package com.tawfeeq.carsln.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewPhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPhotoFragment extends Fragment {

    FireBaseServices fbs;
    String From;
    TextView tvUsername, Alert;
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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        tvUsername = getView().findViewById(R.id.tvUsernameViewPhoto);
        Alert = getView().findViewById(R.id.tvViewPhoto);
        ivUser = getView().findViewById(R.id.ivViewProfilePhoto);
        Back = getView().findViewById(R.id.ViewPhotoGoBack);
        ChangePFP = getView().findViewById(R.id.ViewPhotoChangePFP);
        From = fbs.getFrom();


        if(!fbs.getCurrentFragment().equals("ViewPhoto")) fbs.setCurrentFragment("ViewPhoto");


        ChangePFP.setVisibility(View.VISIBLE);


        // Custom Bottom Dialog FOr Profile Photo Options!
        BottomSheetDialog sheetDialog = new BottomSheetDialog(getActivity());
        sheetDialog.setContentView(R.layout.bottom_profile_dialog);
        sheetDialog.setCancelable(true);

        LinearLayout changepfp = sheetDialog.findViewById(R.id.linearLayoutChange);
        LinearLayout resetpfp = sheetDialog.findViewById(R.id.linearLayoutReset);

        changepfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.dismiss();
                ImageChooser();
            }
        });

        resetpfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String photo = "";

                String str = fbs.getAuth().getCurrentUser().getEmail();
                int n = str.indexOf("@");
                String user = str.substring(0,n);

                if (fbs.getUser() != null) {
                    fbs.getStore().collection("Users").document(user).update("userPhoto", photo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(getActivity(), "Profile Photo Updated", Toast.LENGTH_LONG).show();
                            fbs.getUser().setUserPhoto(photo);

                            ivUser.setImageResource(R.color.black);
                            Alert.setVisibility(View.VISIBLE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Update Your Profile Photo, Try Again Later", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                sheetDialog.dismiss();
            }
        });

        ChangePFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    sheetDialog.show();
                }
        });



        if(fbs.getUser()!=null) {

            String username = fbs.getUser().getUsername();
            String pfp = fbs.getUser().getUserPhoto();

            tvUsername.setText(username);

            if (pfp == null || pfp.isEmpty()) {
                Alert.setVisibility(View.VISIBLE);
            }
            else {
                Alert.setVisibility(View.INVISIBLE);
                Glide.with(getActivity()).load(pfp).into(ivUser);
            }

        }

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(From.equals("Profile")) {
                    GoToProfile();
                    setNavVisible();
                }
                else if(From.equals("Settings")) {
                    GoToSettings();
                    setNavVisible();
                }

                fbs.setFrom("");
            }
        });

    }

    private void setNavVisible() {
        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }

    private void GoToSettings() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.FrameLayoutMain, new SettingsFragment());
        ft.commit();
    }

    private void GoToProfile() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
        ft.commit();
    }

    public void ImageChooser() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 123);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = null;

            if(data!=null) resultUri = UCrop.getOutput(data);

            if(resultUri!=null) {
                Alert.setVisibility(View.INVISIBLE);
                ivUser.setImageURI(resultUri);
                uploadImageandSave(getActivity(), resultUri);
            }

        } else if(resultCode == UCrop.RESULT_ERROR) {
            // Close the UCrop.
        }
        if (requestCode == 123 && resultCode == getActivity().RESULT_OK && data!=null) {

            Uri selectedImageUri = data.getData();
            startCropActivity(selectedImageUri);

        }

    }

    private void startCropActivity(Uri sourceUri) {

        Uri destinationUri = Uri.fromFile(new File(getActivity().getCacheDir(), UUID.randomUUID().toString()));

        UCrop.Options options = new UCrop.Options();

        UCrop uCrop = UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1,1)
                .withMaxResultSize(2000, 2000);
        uCrop.withOptions(options);
        uCrop.start(getContext(), this, UCrop.REQUEST_CROP);

    }

    public void uploadImageandSave(Context context, Uri selectedImageUri) {
        if (selectedImageUri != null) {

            Dialog loading = new Dialog(getActivity());
            loading.setContentView(R.layout.loading_dialog);
            loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loading.setCancelable(false);
            loading.show();


            String imageStr = "pfps/" + UUID.randomUUID() + ".jpg"; //+ selectedImageUri.getLastPathSegment();
            StorageReference imageRef = fbs.getStorage().getReference().child("pfps/" + selectedImageUri.getLastPathSegment());

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            fbs.setSelectedImageURL(uri);
                            if(fbs.getUser() != null) UpdatePFP();
                            else {

                                if(fbs.getUser()!=null) {
                                    String pfp = fbs.getUser().getUserPhoto();
                                    if (pfp == null || pfp.isEmpty()) {
                                        Alert.setVisibility(View.VISIBLE);
                                    } else {
                                        Alert.setVisibility(View.INVISIBLE);
                                        Glide.with(getActivity()).load(pfp).into(ivUser);
                                    }
                                } else {
                                    Alert.setVisibility(View.VISIBLE);
                                }

                                Toast.makeText(context, "Couldn't Update Your Profile Photo, Try Again Later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Utils: uploadImage: ", e.getMessage());
                        }
                    });

                    loading.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading.dismiss();

                    if(fbs.getUser()!=null) {
                        String pfp = fbs.getUser().getUserPhoto();
                        if (pfp == null || pfp.isEmpty()) {
                            Alert.setVisibility(View.VISIBLE);
                        } else {
                            Alert.setVisibility(View.INVISIBLE);
                            Glide.with(getActivity()).load(pfp).into(ivUser);
                        }
                    } else {
                        Alert.setVisibility(View.VISIBLE);
                    }

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
            Glide.with(getActivity()).load(photo).into(ivUser);
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

                    if(fbs.getUser()!=null) {
                        String pfp = fbs.getUser().getUserPhoto();
                        if (pfp == null || pfp.isEmpty()) {
                            Alert.setVisibility(View.VISIBLE);
                        } else {
                            Alert.setVisibility(View.INVISIBLE);
                            Glide.with(getActivity()).load(pfp).into(ivUser);
                        }
                    } else {
                        Alert.setVisibility(View.VISIBLE);
                    }

                    Toast.makeText(getActivity(), "Couldn't Update Your Profile Photo, Try Again Later", Toast.LENGTH_LONG).show();
                }
            });
        }
        else Toast.makeText(getActivity(), "Press Your Profile Picture to Insert a new Image Firstly", Toast.LENGTH_LONG).show();
    }
}