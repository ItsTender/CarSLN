package com.tawfeeq.carsln;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    Utils utils;
    FireBaseServices fbs;
    EditText etChangeUsername, etChangePhone;
    Button btnLogout, Changepfp, Resetpfp, btnChangePhone, btnChangeUsername;
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
        btnChangePhone = getView().findViewById(R.id.btnChangePhone);
        btnChangeUsername = getView().findViewById(R.id.btnChangeUsername);
        etChangePhone = getView().findViewById(R.id.etChangePhone);
        etChangeUsername =getView().findViewById(R.id.etChangeUsername);
        Changepfp = getView().findViewById(R.id.btnChangePhoto);
        Resetpfp = getView().findViewById(R.id.btnResetPhoto);
        ivUser = getView().findViewById(R.id.imageViewProfilePhotoSettings);


        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0,n);

        if(fbs.getUser()!=null) {

            // Get User Profile Photo.....

            pfp = fbs.getUser().getUserPhoto();

            if (pfp == null || pfp.isEmpty()) {
                ivUser.setImageResource(R.drawable.slnpfp);
            } else {
                Glide.with(getActivity()).load(pfp).into(ivUser);
            }

            etChangeUsername.setText(fbs.getUser().getUsername());
            etChangePhone.setText(fbs.getUser().getPhone());

        }

        // Get Profile Photo Ends


        // Set New Profile Photo.....

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fbs.getUser()!=null && !fbs.getUser().getUserPhoto().equals("")) GoToViewPhoto();
            }
        });

        Changepfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {ImageChooser();}
        });

        Resetpfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Hold the Button to Confirm Resetting Your Profile Photo", Toast.LENGTH_LONG).show();
            }
        });

        Resetpfp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Uploading...");
                progressDialog.setMessage("Resetting Your Profile Picture, Please Wait");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIcon(R.drawable.slnround);
                progressDialog.show();

                String photo = "";
                Picasso.get().load(R.drawable.slnpfp).into(ivUser);

                if (fbs.getUser() != null) {
                    fbs.getStore().collection("Users").document(user).update("userPhoto", photo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(getActivity(), "Profile Photo Updated", Toast.LENGTH_LONG).show();
                            fbs.getUser().setUserPhoto(photo);
                            Picasso.get().load(R.drawable.slnpfp).into(ivUser);
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Update Your Profile Photo, Try Again Later", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
                }

                return true;
            }
        });



        // Set New Profile Photo Ends


        btnChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newusername = etChangeUsername.getText().toString();
                if(newusername.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter a Valid Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(fbs.getUser()!=null) {

                    if (newusername.equals(fbs.getUser().getUsername())) {
                        return;
                    }

                    fbs.getStore().collection("Users").document(user).update("username", newusername).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(getActivity(), "Username Updated", Toast.LENGTH_LONG).show();
                            fbs.getUser().setUsername(newusername);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Update Your Username, Try Again Later", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });


        btnChangePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newphone = etChangePhone.getText().toString();
                if(newphone.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(fbs.getUser()!=null) {

                    if (newphone.equals(fbs.getUser().getPhone())) {
                        return;
                    }

                    fbs.getStore().collection("Users").document(user).update("phone", newphone).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(getActivity(), "Phone Number Updated", Toast.LENGTH_LONG).show();
                            fbs.getUser().setPhone(newphone);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Update Your Phone Number, Try Again Later", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Hold the Button to Logout", Toast.LENGTH_LONG).show();
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
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void setNavigationBarGone() {
        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.GONE);
    }

    private void GoToViewPhoto() {

        if(fbs.getUser()!=null) {

            Fragment gtn= new ViewPhotoFragment();
            Bundle bundle = new Bundle();

            bundle.putString("Email", fbs.getAuth().getCurrentUser().getEmail());
            bundle.putString("Username", fbs.getUser().getUsername());
            bundle.putString("PFP", fbs.getUser().getUserPhoto());
            bundle.putString("From", "Settings");

            gtn.setArguments(bundle);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.FrameLayoutMain, gtn);
            ft.commit();
        }
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
                    Toast.makeText(getActivity(), "Couldn't Update Your Profile Photo, Try Again Later", Toast.LENGTH_LONG).show();
                }
            });
        }
        else Toast.makeText(getActivity(), "Press Your Profile Picture to Insert a new Image Firstly", Toast.LENGTH_LONG).show();
    }
}