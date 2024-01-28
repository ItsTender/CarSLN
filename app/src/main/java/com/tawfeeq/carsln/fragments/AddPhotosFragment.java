package com.tawfeeq.carsln.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.objects.Cars;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.MainActivity;
import com.tawfeeq.carsln.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPhotosFragment extends Fragment {

    FireBaseServices fbs;
    String Man, Mod,Transmission,Engine,color,Location,NextTest,Notes;
    Integer Price,Power,Year,Users,Kilometre;
    Boolean selllend;
    Button AddCar, Reset;
    ImageView ivFirstPhoto, ivSecondPhoto, ivThirdPhoto, ivFourthPhoto, ivFifthPhoto, Close;
    String FirstPhoto, SecondPhoto, ThirdPhoto, FourthPhoto, FifthPhoto;
    ArrayList<CarID> Market;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddPhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPhotosFragment newInstance(String param1, String param2) {
        AddPhotosFragment fragment = new AddPhotosFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_photos, container, false);

        Bundle bundle =this.getArguments();


        selllend=bundle.getBoolean("SellorLend");
        Man=bundle.getString("Man");
        Mod=bundle.getString("Mod");
        Price=bundle.getInt("Price");
        Power =bundle.getInt("HP");
        Engine =bundle.getString("Engine");
        Year =bundle.getInt("Year");
        Users =bundle.getInt("Users");
        Kilometre=bundle.getInt("Kilo");
        Transmission=bundle.getString("Transmission");
        color=bundle.getString("Color");
        Location=bundle.getString("Area");
        NextTest=bundle.getString("Test");
        Notes=bundle.getString("Notes");


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs= FireBaseServices.getInstance();
        Close = getView().findViewById(R.id.imageViewAddPhotosClose);
        ivFirstPhoto = getView().findViewById(R.id.ivFirstCarPhoto);
        ivSecondPhoto = getView().findViewById(R.id.ivSecondCarPhoto);
        ivThirdPhoto =getView().findViewById(R.id.ivThirdCarPhoto);
        ivFourthPhoto = getView().findViewById(R.id.ivFourthCarPhoto);
        ivFifthPhoto = getView().findViewById(R.id.ivFifthCarPhoto);
        AddCar = getView().findViewById(R.id.btnAddCarListing);


        if(!fbs.getCurrentFragment().equals("AddPhotos")) fbs.setCurrentFragment("AddPhotos");


        AddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(FirstPhoto == null) FirstPhoto ="";
                if(SecondPhoto == null) SecondPhoto="";
                if(ThirdPhoto == null ) ThirdPhoto ="";
                if(FourthPhoto == null ) FourthPhoto ="";
                if(FifthPhoto == null ) FifthPhoto ="";

                Cars Add = new Cars(selllend,fbs.getAuth().getCurrentUser().getEmail(),Man,Mod,Power,Price,Year,Transmission,Engine,Kilometre,Users,color,Location,NextTest,FirstPhoto,SecondPhoto,ThirdPhoto,FourthPhoto,FifthPhoto,Notes);

                Dialog loading = new Dialog(getActivity());
                loading.setContentView(R.layout.loading_dialog);
                loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                loading.setCancelable(false);
                loading.show();

                fbs.getStore().collection("MarketPlace").add(Add).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Car Added to MarketPlace", Toast.LENGTH_SHORT).show();
                        fbs.setMarketList(null);
                        setNavigationBarVisible();
                        setNavigationCarsMarket();
                        loading.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Couldn't Add Car to MarketPlace", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });

            }
        });

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToAddCar();
            }
        });

        ivFirstPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });

        ivSecondPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(FirstPhoto == null)) ImageChooser();
            }
        });

        ivThirdPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(SecondPhoto == null)) ImageChooser();
            }
        });

        ivFourthPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(ThirdPhoto == null)) ImageChooser();
            }
        });

        ivFifthPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(FourthPhoto == null)) ImageChooser();
            }
        });


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

                if(FirstPhoto == null){

                    ivFirstPhoto.setImageURI(resultUri);
                    UploadFirstPhoto(resultUri);

                }else if (SecondPhoto == null){

                    ivSecondPhoto.setImageURI(resultUri);
                    UploadSecondPhoto(resultUri);

                }else if(ThirdPhoto == null){

                    ivThirdPhoto.setImageURI(resultUri);
                    UploadThirdPhoto(resultUri);

                }else if(FourthPhoto == null){

                    ivFourthPhoto.setImageURI(resultUri);
                    UploadFourthPhoto(resultUri);

                }else if(FifthPhoto == null){

                    ivFifthPhoto.setImageURI(resultUri);
                    UploadFifthPhoto(resultUri);

                }

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

        // Multiple Aspect Ratios for each Selected Pictures!! (Fixed the Photos Flipping)....
        UCrop.Options options = new UCrop.Options();
        AspectRatio defaultaspectRatio = new AspectRatio("4/3",4,3);
        AspectRatio secondaryaspectRation = new AspectRatio("16/9",16,9);
        AspectRatio fifthaspectRation = new AspectRatio("3/4", 3, 4);
        AspectRatio FourthaspectRation = new AspectRatio("1/1", 1, 1);
        options.setAspectRatioOptions(0, defaultaspectRatio, fifthaspectRation, secondaryaspectRation, FourthaspectRation);

        UCrop uCrop = UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(2000, 2000);
        uCrop.withOptions(options);
        uCrop.start(getContext(), this, UCrop.REQUEST_CROP);

    }

    private void setNavigationBarVisible(){
        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }

    public void GoToAddCar(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AddCarFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public void GoToFragmentCars(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
        ft.commit();
    }

    private void setNavigationCarsMarket() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.market);
    }

    private void UploadFirstPhoto(Uri selectedImageUri) {

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

                            FirstPhoto = uri.toString();
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
                    Toast.makeText(getActivity(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Choose an Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void UploadSecondPhoto(Uri selectedImageUri) {

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

                            SecondPhoto = uri.toString();
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
                    Toast.makeText(getActivity(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Choose an Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void UploadThirdPhoto(Uri selectedImageUri) {

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

                            ThirdPhoto = uri.toString();
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
                    Toast.makeText(getActivity(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Choose an Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void UploadFourthPhoto(Uri selectedImageUri) {

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

                            FourthPhoto = uri.toString();
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
                    Toast.makeText(getActivity(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Choose an Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void UploadFifthPhoto(Uri selectedImageUri) {

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

                            FifthPhoto = uri.toString();
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
                    Toast.makeText(getActivity(), "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Choose an Image", Toast.LENGTH_SHORT).show();
        }

    }

}