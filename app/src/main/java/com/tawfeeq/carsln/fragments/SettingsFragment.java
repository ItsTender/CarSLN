package com.tawfeeq.carsln.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.MainActivity;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.objects.Utils;

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
    TextView tvPFP;
    Button logout, btnChangePhone, btnChangeUsername, btnChangeLocation;
    ImageView ivUser;
    String pfp;
    Spinner SpinnerLocation;

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
        logout= getView().findViewById(R.id.btnLogout);
        tvPFP = getView().findViewById(R.id.tvtxtPFPSettings);
        btnChangePhone = getView().findViewById(R.id.btnChangePhone);
        btnChangeUsername = getView().findViewById(R.id.btnChangeUsername);
        btnChangeLocation = getView().findViewById(R.id.btnChangeLocation);
        etChangePhone = getView().findViewById(R.id.etChangePhone);
        etChangeUsername =getView().findViewById(R.id.etChangeUsername);
        ivUser = getView().findViewById(R.id.imageViewProfilePhotoSettings);
        SpinnerLocation = getView().findViewById(R.id.SpinnerLocationAreaSettings);


        String [] Location = {"Select Your District","Golan","Galil","Haifa","Central","Tel Aviv","Jerusalem","Be'er Sheva","Central Southern","Eilat"};
        String[] Golan = {"Golan","Galil","Haifa","Central","Tel Aviv","Jerusalem","Be'er Sheva","Central Southern","Eilat"};
        String[] Galil = {"Galil","Golan","Haifa","Central","Tel Aviv","Jerusalem","Be'er Sheva","Central Southern","Eilat"};
        String[] Haifa = {"Haifa","Golan","Galil","Central","Tel Aviv","Jerusalem","Be'er Sheva","Central Southern","Eilat"};
        String[] Central = {"Central","Golan","Galil","Haifa","Tel Aviv","Jerusalem","Be'er Sheva","Central Southern","Eilat"};
        String[] TelAviv = {"Tel Aviv","Golan","Galil","Haifa","Central","Jerusalem","Be'er Sheva","Central Southern","Eilat"};
        String[] Jerusalem = {"Jerusalem","Golan","Galil","Haifa","Central","Tel Aviv","Be'er Sheva","Central Southern","Eilat"};
        String[] Ber = {"Be'er Sheva","Golan","Galil","Haifa","Central","Tel Aviv","Jerusalem","Central Southern","Eilat"};
        String[] south = {"Central Southern","Golan","Galil","Haifa","Central","Tel Aviv","Jerusalem","Be'er Sheva","Eilat"};
        String[] eilat = {"Eilat","Golan","Galil","Haifa","Central","Tel Aviv","Jerusalem","Be'er Sheva","Central Southern","Eilat"};


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

            String area = fbs.getUser().getLocation();
            if(area.equals("Golan")){
                ArrayAdapter<String> LocationAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, Golan);
                LocationAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                SpinnerLocation.setAdapter(LocationAdapter);
            }
            if(area.equals("Galil")){
                ArrayAdapter<String> LocationAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, Galil);
                LocationAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                SpinnerLocation.setAdapter(LocationAdapter);
            }
            if(area.equals("Haifa")){
                ArrayAdapter<String> LocationAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, Haifa);
                LocationAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                SpinnerLocation.setAdapter(LocationAdapter);
            }
            if(area.equals("Central")){
                ArrayAdapter<String> LocationAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, Central);
                LocationAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                SpinnerLocation.setAdapter(LocationAdapter);
            }
            if(area.equals("Tel Aviv")){
                ArrayAdapter<String> LocationAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, TelAviv);
                LocationAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                SpinnerLocation.setAdapter(LocationAdapter);
            }
            if(area.equals("Jerusalem")){
                ArrayAdapter<String> LocationAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, Jerusalem);
                LocationAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                SpinnerLocation.setAdapter(LocationAdapter);
            }
            if(area.equals("Be'er Sheva")){
                ArrayAdapter<String> LocationAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, Ber);
                LocationAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                SpinnerLocation.setAdapter(LocationAdapter);
            }
            if(area.equals("Central Southern")){
                ArrayAdapter<String> LocationAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, south);
                LocationAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                SpinnerLocation.setAdapter(LocationAdapter);
            }
            if(area.equals("Eilat")){
                ArrayAdapter<String> LocationAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, eilat);
                LocationAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
                SpinnerLocation.setAdapter(LocationAdapter);
            }

        }else {

            ArrayAdapter<String> LocationAdapter = new ArrayAdapter<>(requireContext(), R.layout.my_selected_item, Location);
            LocationAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
            SpinnerLocation.setAdapter(LocationAdapter);
        }

        // Get Profile Photo Ends


        // Set New Profile Photo.....


        ivUser.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if(fbs.getUser()!=null && !fbs.getUser().getUserPhoto().equals("")) GoToViewPhoto();

                return true;
            }
        });

        btnChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = SpinnerLocation.getSelectedItem().toString();

                if(location.equals("")) {
                    Toast.makeText(getActivity(), "Please Choose Your District", Toast.LENGTH_SHORT).show();
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
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Update Your Location, Try Again Later", Toast.LENGTH_LONG).show();
                        }

                    });
                }
            }
        });



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


        // Custom Bottom Dialog FOr Profile Photo Options!
        BottomSheetDialog sheetDialog = new BottomSheetDialog(getActivity());
        sheetDialog.setContentView(R.layout.bottom_profile_dialog);
        sheetDialog.setCancelable(true);

        CardView changepfp = sheetDialog.findViewById(R.id.cardViewChangePFP);
        CardView resetpfp = sheetDialog.findViewById(R.id.cardViewResetPFP);

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
                ivUser.setImageResource(R.drawable.slnpfp);

                if (fbs.getUser() != null) {
                    fbs.getStore().collection("Users").document(user).update("userPhoto", photo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(getActivity(), "Profile Photo Updated", Toast.LENGTH_LONG).show();
                            fbs.getUser().setUserPhoto(photo);
                            Picasso.get().load(R.drawable.slnpfp).into(ivUser);
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
        dialog.setCancelable(false);

        Button btnLogout = dialog.findViewById(R.id.btnConfirmLogout);
        Button btnCancel = dialog.findViewById(R.id.btnCancelLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbs.getAuth().signOut();
                fbs.setMarketList(null);
                setNavigationBarGone();
                GoToLogIn();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        
    }

    private void GoToLogIn() {

        FragmentManager ftm = getActivity().getSupportFragmentManager();
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
            progressDialog.setMessage("Uploading New Profile Picture Image, Please Wait");
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