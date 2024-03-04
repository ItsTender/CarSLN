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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.objects.Utils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    Utils utils;
    FireBaseServices fbs;
    RelativeLayout Username, Phone, Location, Password, Logout;
    TextView tvPFP, tvUsername, tvPhone, tvLocation;
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
        tvPFP = getView().findViewById(R.id.tvtxtPFPSettings);
        tvUsername = getView().findViewById(R.id.tvCurrentUsername);
        tvPhone = getView().findViewById(R.id.tvCurrentPhone);
        tvLocation = getView().findViewById(R.id.tvCurrentLocation);
        Username = getView().findViewById(R.id.Layoutusername);
        Phone = getView().findViewById(R.id.LayoutPhone);
        Location = getView().findViewById(R.id.LayoutLocation);
        Password = getView().findViewById(R.id.LayoutPassword);
        Logout = getView().findViewById(R.id.Layoutlogout);


        ivUser = getView().findViewById(R.id.imageViewProfilePhotoSettings);


        if(!fbs.getCurrentFragment().equals("Settings")) fbs.setCurrentFragment("Settings");


        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0,n);


        // Bind Current Account Info!!!!!!

        if(fbs.getUser()!=null) {

            // Get User Profile Photo.....

            pfp = fbs.getUser().getUserPhoto();

            if (pfp == null || pfp.isEmpty()) {
                ivUser.setImageResource(R.drawable.slnpfp);
            } else {
                Glide.with(getActivity()).load(pfp).into(ivUser);
            }

            tvUsername.setText(fbs.getUser().getUsername());
            tvPhone.setText(fbs.getUser().getPhone());
            tvLocation.setText(fbs.getUser().getLocation());

        }

        // Ends................................................


        ivUser.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                setNavigationBarGone();
                GoToViewPhoto();

                return true;
            }
        });










        // Custom Change Username Dialog!
        Dialog dialogUsername = new Dialog(getActivity());
        dialogUsername.setContentView(R.layout.change_username_dialog);
        dialogUsername.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialogUsername.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialogUsername.setCancelable(true);

        Button ChangeUsername = dialogUsername.findViewById(R.id.btnChangeUsername);
        EditText etUsername = dialogUsername.findViewById(R.id.etChangeUsername);
        ImageView ivBack = dialogUsername.findViewById(R.id.UsernameGoBack);

        if(fbs.getUser()!=null) etUsername.setText(fbs.getUser().getUsername());

        ChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newusername = etUsername.getText().toString();

                if (newusername.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter a Valid Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fbs.getUser() != null) {

                    if (newusername.equals(fbs.getUser().getUsername())) {
                        return;
                    }
                    if (newusername.length() < 3) {
                        Toast.makeText(getActivity(), "The Username is too Short", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (newusername.length() > 20) {
                        Toast.makeText(getActivity(), "The Username is too Long", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    fbs.getStore().collection("Users").document(user).update("username", newusername).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(getActivity(), "Username Updated", Toast.LENGTH_LONG).show();
                            fbs.getUser().setUsername(newusername);
                            tvUsername.setText(newusername);
                            dialogUsername.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Update Your Username, Try Again Later", Toast.LENGTH_LONG).show();
                            dialogUsername.dismiss();
                        }
                    });

                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUsername.dismiss();
            }
        });

        Username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUsername.show();
            }
        });









        // Custom Change Phone Dialog!
        Dialog dialogPhone = new Dialog(getActivity());
        dialogPhone.setContentView(R.layout.change_phone_dialog);
        dialogPhone.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialogPhone.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialogPhone.setCancelable(true);

        Button ChangePhone = dialogPhone.findViewById(R.id.btnChangePhone);
        EditText etPhone = dialogPhone.findViewById(R.id.etChangePhone);
        ImageView ivBackPhone = dialogPhone.findViewById(R.id.PhoneGoBack);

        if(fbs.getUser()!=null) etPhone.setText(fbs.getUser().getPhone());

        ChangePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newphone = etPhone.getText().toString();

                if(newphone.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Phone Number Field is Missing", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newphone.length()!=10) {
                    Toast.makeText(getActivity(), "a Valid Phone Number Must Contain 10 Digits", Toast.LENGTH_SHORT).show();
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
                            tvPhone.setText(newphone);
                            dialogPhone.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Update Your Phone Number, Try Again Later", Toast.LENGTH_LONG).show();
                            dialogPhone.dismiss();
                        }
                    });

                }
            }
        });

        ivBackPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPhone.dismiss();
            }
        });

        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPhone.show();
            }
        });









        // Custom Change Location Dialog!
        Dialog dialogLocation = new Dialog(getActivity());
        dialogLocation.setContentView(R.layout.change_location_dialog);
        dialogLocation.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialogLocation.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialogLocation.setCancelable(true);

        Button ChangeLocation = dialogLocation.findViewById(R.id.btnChangeLocation);
        Spinner SpinnerLocation = dialogLocation.findViewById(R.id.SpinnerChangeLocation);
        ImageView ivBackLocation = dialogLocation.findViewById(R.id.LocationGoBack);

        if(SpinnerLocation.getSelectedItem()==null) {
            String[] LocationList = {"New Location District", "Golan", "Galil", "Haifa", "Central", "Tel Aviv", "Jerusalem", "Be'er Sheva", "Central Southern", "Eilat"};
            ArrayAdapter<String> LocationAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, LocationList);
            LocationAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
            SpinnerLocation.setAdapter(LocationAdapter);
        }

        ChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String location = SpinnerLocation.getSelectedItem().toString();

                if(location.equals("New Location District")) {
                    Toast.makeText(getActivity(), "Please Choose a New Location District", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(fbs.getUser()!=null) {

                    if (location.equals(fbs.getUser().getLocation())) {
                        return;
                    }

                    fbs.getStore().collection("Users").document(user).update("location", location).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(getActivity(), "Location District Updated", Toast.LENGTH_LONG).show();
                            fbs.getUser().setLocation(location);
                            tvLocation.setText(location);
                            dialogLocation.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Update Your Location, Try Again Later", Toast.LENGTH_LONG).show();
                            dialogLocation.dismiss();
                        }

                    });

                }
            }
        });

        ivBackLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLocation.dismiss();
            }
        });

        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLocation.show();
            }
        });










        // Custom Change Location Dialog!
        Dialog dialogPass = new Dialog(getActivity());
        dialogPass.setContentView(R.layout.change_password_dialog);
        dialogPass.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialogPass.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialogPass.setCancelable(true);

        Button ChangePass = dialogPass.findViewById(R.id.btnChangePass);
        EditText etPass = dialogPass.findViewById(R.id.etChangePass);
        EditText etConf = dialogPass.findViewById(R.id.etConfirmPass);
        ImageView ivBackPass = dialogPass.findViewById(R.id.PasswordGoBack);

        ChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pass = etPass.getText().toString();
                String confirm = etConf.getText().toString();

                if (pass.trim().isEmpty() || confirm.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Password Fields Are Missing", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pass.equals(confirm)) {
                    Toast.makeText(getActivity(), "Password Credentials Do Not Match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(fbs.getUser()!=null) {

                    fbs.getAuth().getCurrentUser().updatePassword(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Password Updated", Toast.LENGTH_LONG).show();
                            dialogPass.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Update Your Password, Try Again Later", Toast.LENGTH_LONG).show();
                            dialogPass.dismiss();
                        }
                    });

                }
            }
        });

        ivBackPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPass.dismiss();
            }
        });

        Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etPass.setText("");
                etConf.setText("");
                dialogPass.show();
            }
        });









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

                if (fbs.getUser() != null) {
                    fbs.getStore().collection("Users").document(user).update("userPhoto", photo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(getActivity(), "Profile Photo Updated", Toast.LENGTH_LONG).show();
                            fbs.getUser().setUserPhoto(photo);
                            ivUser.setImageResource(R.drawable.slnpfp);

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

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.show();
            }
        });

        tvPFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sheetDialog.show();
            }
        });


        // Custom Logout Dialog!
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_logout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog.setCancelable(true);

        Button btnLogout = dialog.findViewById(R.id.btnConfirmLogout);
        Button btnCancel = dialog.findViewById(R.id.btnCancelLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbs.getAuth().signOut();
                GoToNoUserProfile();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

    }

    private void GoToNoUserProfile() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new NoUserProfileFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void setNavigationBarMarket() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.market);
    }

    private void setNavigationBarGone() {
        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.GONE);
    }

    private void GoToViewPhoto() {

        if(fbs.getUser()!=null) {

            Fragment gtn= new ViewPhotoFragment();

            fbs.setFrom("Settings");

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


        if (requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = null;

            if(data!=null) resultUri = UCrop.getOutput(data);

            if(resultUri!=null) {
                ivUser.setImageURI(resultUri);
                uploadImageandSave(getActivity(), resultUri);
            }

        } else if(resultCode == UCrop.RESULT_ERROR) {
            // Close the UCrop.
        }
        if (requestCode == 123 && resultCode == getActivity().RESULT_OK) {

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

                    loading.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading.dismiss();
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