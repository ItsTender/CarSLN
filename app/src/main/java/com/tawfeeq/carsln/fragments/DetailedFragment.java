package com.tawfeeq.carsln.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tawfeeq.carsln.activities.SplashScreenActivity;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.objects.UserProfile;
import com.tawfeeq.carsln.objects.Users;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedFragment extends Fragment {

    FireBaseServices fbs;
    TextView tvMan, tvPrice, tvPower, tvYear, tvUsers, tvKilometre, tvTransmission, tvEngine, tvLocation, tvTest, tvColor, tvNotes, tvOwnership, tvDate;
    ImageView  ivSaved, ivBack, ivDelete, ivEdit;
    Button btnSeller, btnSLN;
    boolean isFound;
    String pfp;
    CarID currentCar;
    UserProfile usr;
    ImageSlider imageSlider;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailedFragment newInstance(String param1, String param2) {
        DetailedFragment fragment = new DetailedFragment();
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
        View view = inflater.inflate(R.layout.fragment_detailed, container, false);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        imageSlider = getView().findViewById(R.id.image_slider);
        btnSeller = getView().findViewById(R.id.btnShowSeller);
        btnSLN = getView().findViewById(R.id.btnSLNMessage);
        tvMan=getView().findViewById(R.id.DetailedMan);
        tvPrice =getView().findViewById(R.id.DetailedPrice);
        tvPower =getView().findViewById(R.id.DetailedHP);
        tvYear = getView().findViewById(R.id.DetailedYear);
        tvKilometre =getView().findViewById(R.id.DetailedKilometre);
        tvUsers= getView().findViewById(R.id.DetailedUsers);
        tvTransmission =getView().findViewById(R.id.DetailedGear);
        tvLocation = getView().findViewById(R.id.DetailedLocationArea);
        tvTest = getView().findViewById(R.id.DetailedTestUntil);
        tvColor = getView().findViewById(R.id.DetailedColor);
        tvNotes = getView().findViewById(R.id.DetailedNotes);
        tvOwnership = getView().findViewById(R.id.DetailedOwnership);
        tvDate = getView().findViewById(R.id.DateCreated);
        tvEngine = getView().findViewById(R.id.DetailedEngine);
        ivSaved = getView().findViewById(R.id.ivSavedCar); // the Saved Icon......
        ivBack =getView().findViewById(R.id.DetailedGoBack); // Goes Back To Wherever the User Was.
        ivDelete =getView().findViewById(R.id.DetailedDeleteListing); // Delete Listing if you Posted it......
        ivEdit = getView().findViewById(R.id.DetailedEditListing); // Edit Listing Info if you Posted it......

        // Keep the Navigation Bar Invisible while looking at the Car's Details!!!!
        getNavigationBar().setVisibility(View.GONE);

        if(!fbs.getCurrentFragment().equals("Detailed")) fbs.setCurrentFragment("Detailed");

        if(fbs.getSelectedCar()!=null) currentCar = fbs.getSelectedCar();
        else {
            startActivity(new Intent(getContext(), MainActivity.class));
        }


        usr = new UserProfile();

        String str = fbs.getSelectedCar().getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0,n);

        final String[] sellerinfo = new String[3];

        // Get User Profile Photo.....

        if(fbs.getAuth().getCurrentUser()!=null) {

            if (fbs.getUser() != null && str.equals(fbs.getAuth().getCurrentUser().getEmail())) {

                // Show Control Buttons.
                ivDelete.setVisibility(View.VISIBLE);
                ivEdit.setVisibility(View.VISIBLE);


                // Custom Delete Dialog!
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_delete_listing);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
                dialog.setCancelable(true);

                Button btnDelete = dialog.findViewById(R.id.btnConfirmDelete);
                Button btnCancel = dialog.findViewById(R.id.btnCancelDelete);

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        fbs.getStore().collection("MarketPlace").document(currentCar.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Successfully Deleted Your Car Listing", Toast.LENGTH_SHORT).show();
                                BottomNavigationView bnv = getNavigationBar();


                                if (bnv.getSelectedItemId() == R.id.market) {
                                    if (fbs.getFrom().equals("Near") || fbs.getFrom().equals("New") || fbs.getFrom().equals("Used"))
                                        GoToForYouList();
                                    else {
                                        bnv.setVisibility(View.VISIBLE);
                                        GoToFragmentCars();
                                    }
                                } else if (bnv.getSelectedItemId() == R.id.searchcar) {
                                    bnv.setVisibility(View.VISIBLE);
                                    GoToFragmentSearch();
                                } else if (bnv.getSelectedItemId() == R.id.savedcars) {
                                    bnv.setVisibility(View.VISIBLE);
                                    GoToFragmentSaved();
                                } else if (bnv.getSelectedItemId() == R.id.profile) {
                                    getNavigationBar().setVisibility(View.VISIBLE);
                                    GoToUserListings();
                                }

                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Couldn't Deleted Your Car Listing, Try Again Later", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                    }
                });


                btnSeller.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        GoToProfile();
                        setNavigationBarProfile();
                        getNavigationBar().setVisibility(View.VISIBLE);

                    }
                });

                btnSLN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        GoToProfile();
                        setNavigationBarProfile();
                        getNavigationBar().setVisibility(View.VISIBLE);
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.show();
                    }
                });

                ivEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        GoToEditPostFragment();
                    }
                });

            }
            else {

                // Make Control Buttons not shown........
                ivDelete.setVisibility(View.INVISIBLE);
                ivEdit.setVisibility(View.INVISIBLE);


                // Custom Seller Page Dialog!
                BottomSheetDialog dialogSeller = new BottomSheetDialog(getActivity());
                dialogSeller.setContentView(R.layout.seller_page);
                dialogSeller.setCancelable(true);

                TextView username = dialogSeller.findViewById(R.id.tvSellerUsername);
                TextView tvnum = dialogSeller.findViewById(R.id.tvtxtcall);
                ImageView iv = dialogSeller.findViewById(R.id.imageViewSellerPage);
                CardView btnCall = dialogSeller.findViewById(R.id.cardViewCall);
                CardView btnSMS = dialogSeller.findViewById(R.id.cardViewSMS);
                ImageView back = dialogSeller.findViewById(R.id.imageViewDialogClose);


                fbs.getStore().collection("Users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        usr = documentSnapshot.toObject(UserProfile.class);

                        username.setText(usr.getUsername());

                        String phone = usr.getPhone();
                        tvnum.setText(phone.substring(0,3) + "-" + phone.substring(3));

                        pfp = documentSnapshot.getString("userPhoto");
                        if (pfp == null || pfp.isEmpty())
                        {
                            iv.setImageResource(R.drawable.slnpfp);            }
                        else {
                            Glide.with(getActivity()).load(pfp).into(iv);
                        }

                        btnSLN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(usr!=null) {

                                    Fragment gtn = new ChatFragment();
                                    Users users = usr.toUsers();
                                    users.setDocID(user);

                                    fbs.setSelectedUser(users);

                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    ft.replace(R.id.FrameLayoutMain, gtn);
                                    ft.commit();
                                }
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Couldn't Retrieve User Profile Info", Toast.LENGTH_SHORT).show();
                    }
                });


                btnSeller.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogSeller.show();

                    }
                });

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogSeller.dismiss();
                    }
                });

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

                        if(usr!=null) {

                            String CarName = currentCar.getYear() + " " + currentCar.getManufacturer() + " " + currentCar.getModel();
                            String phoneNumber = usr.getPhone();
                            String message = "Hello, I Saw Your " + CarName + " Listed On CarSLN and I'm Interested in it";

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("sms:" + phoneNumber));
                            intent.putExtra("sms_body",message);
                            startActivity(intent);

                        }
                    }
                });


            }

        } else {

            // Make Control Buttons not shown........
            ivDelete.setVisibility(View.INVISIBLE);
            ivEdit.setVisibility(View.INVISIBLE);


            // Custom Seller Page Dialog!
            BottomSheetDialog dialogSeller = new BottomSheetDialog(getActivity());
            dialogSeller.setContentView(R.layout.seller_page);
            dialogSeller.setCancelable(true);

            TextView username = dialogSeller.findViewById(R.id.tvSellerUsername);
            TextView tvnum = dialogSeller.findViewById(R.id.tvtxtcall);
            ImageView iv = dialogSeller.findViewById(R.id.imageViewSellerPage);
            CardView btnCall = dialogSeller.findViewById(R.id.cardViewCall);
            CardView btnSMS = dialogSeller.findViewById(R.id.cardViewSMS);
            ImageView back = dialogSeller.findViewById(R.id.imageViewDialogClose);


            fbs.getStore().collection("Users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    usr = documentSnapshot.toObject(UserProfile.class);

                    username.setText(usr.getUsername());

                    String phone = usr.getPhone();
                    tvnum.setText(phone.substring(0,3) + "-" + phone.substring(3));

                    pfp = documentSnapshot.getString("userPhoto");
                    if (pfp == null || pfp.isEmpty())
                    {
                        iv.setImageResource(R.drawable.slnpfp);            }
                    else {
                        Glide.with(getActivity()).load(pfp).into(iv);
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Couldn't Retrieve User Profile Info", Toast.LENGTH_SHORT).show();
                }
            });


            btnSeller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogSeller.show();

                }
            });

            btnSLN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: login to unlock this feature!
                    Toast.makeText(getActivity(), "Login or Create a new account to unlock this feature!", Toast.LENGTH_SHORT).show();
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialogSeller.dismiss();
                }
            });

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

                    if(usr!=null) {

                        String CarName = currentCar.getYear() + " " + currentCar.getManufacturer() + " " + currentCar.getModel();
                        String phoneNumber = usr.getPhone();
                        String message = "Hello, I Saw Your " + CarName + " Listed On CarSLN and I'm Interested in it";

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("sms:" + phoneNumber));
                        intent.putExtra("sms_body",message);
                        startActivity(intent);

                    }
                }
            });

        }


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomNavigationView bnv = getNavigationBar();

                if (bnv.getSelectedItemId() == R.id.market) {
                    if(fbs.getAuth().getCurrentUser()!=null) {

                        if (fbs.getFrom().equals("Near") || fbs.getFrom().equals("New") || fbs.getFrom().equals("Used"))
                            GoToForYouList();
                        else {
                            getNavigationBar().setVisibility(View.VISIBLE);
                            GoToFragmentCars();
                        }

                    } else {

                        if (fbs.getFrom().equals("Near") || fbs.getFrom().equals("New") || fbs.getFrom().equals("Used"))
                            GoToForYouList();
                        else {
                            getNavigationBar().setVisibility(View.VISIBLE);
                            GoToNoUserHome();
                        }

                    }
                }
                else if (bnv.getSelectedItemId() == R.id.searchcar){
                    getNavigationBar().setVisibility(View.VISIBLE);
                    GoToFragmentSearch();
                }
                else if (bnv.getSelectedItemId() == R.id.savedcars) {
                    getNavigationBar().setVisibility(View.VISIBLE);
                    GoToFragmentSaved();
                }
                else if (bnv.getSelectedItemId() == R.id.profile){
                    getNavigationBar().setVisibility(View.VISIBLE);
                    GoToUserListings();
                }
            }
        });


        // Bind the Saved Icon *and* Save/Remove the Car

        if(fbs.getAuth().getCurrentUser()!=null) {

            String str1 = fbs.getAuth().getCurrentUser().getEmail();
            int n1 = str1.indexOf("@");
            String user1 = str1.substring(0, n1);
            ArrayList<String> Saved;

            if (fbs.getUser() != null) Saved = fbs.getUser().getSavedCars();
            else Saved = new ArrayList<String>();

            if (Saved.contains(currentCar.getId())) {
                ivSaved.setImageResource(R.drawable.saved_logo);
                isFound = true;
            } else {
                ivSaved.setImageResource(R.drawable.saved_logo_unfilled);
                isFound = false;
            }

            ivSaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (fbs.getUser() != null) {
                        if (isFound) {
                            Saved.remove(currentCar.getId());
                            ivSaved.setImageResource(R.drawable.saved_logo_unfilled);
                        }
                        if (!isFound) {
                            Saved.add(currentCar.getId());
                            ivSaved.setImageResource(R.drawable.saved_logo);
                        }

                        fbs.getStore().collection("Users").document(user1).update("savedCars", Saved).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if (isFound) {
                                    ivSaved.setImageResource(R.drawable.saved_logo_unfilled);
                                    fbs.getUser().setSavedCars(Saved);
                                    isFound = false;
                                } else {
                                    ivSaved.setImageResource(R.drawable.saved_logo);
                                    fbs.getUser().setSavedCars(Saved);
                                    isFound = true;
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Couldn't Add/Remove The Car, Try Again Later", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });

        } else ivSaved.setVisibility(View.INVISIBLE);


        // Ends........................................


        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);


        tvMan.setText(currentCar.getManufacturer() + " " + currentCar.getModel());

        if(currentCar.getSellLend()) {
            tvPrice.setText(formatter.format(currentCar.getPrice()) + "₪");
        }
        else{
            tvPrice.setText(formatter.format(currentCar.getPrice()) + "₪" + " Monthly");
        }

        tvPower.setText(formatter.format(currentCar.getBHP()));
        tvKilometre.setText(formatter.format(currentCar.getKilometre()) + " km");
        String year=String.valueOf(currentCar.getYear()); tvYear.setText(year);
        tvTransmission.setText(currentCar.getTransmission());
        tvLocation.setText(currentCar.getLocation());
        tvTest.setText(currentCar.getNextTest());
        tvColor.setText(currentCar.getColor());
        tvOwnership.setText(currentCar.getOwnership());
        tvDate.setText(String.valueOf(currentCar.getTimestamp().toDate().toLocaleString()));

        if(currentCar.getUsers()==1){
            String Owners= String.valueOf(currentCar.getUsers()); tvUsers.setText(Owners + " Owner");
        }
        else {
            String Owners= String.valueOf(currentCar.getUsers()); tvUsers.setText(Owners + " Owners");
        }

        tvEngine.setText(currentCar.getEngine());

        tvNotes.setText(currentCar.getNotes());


        int i;
        ArrayList<SlideModel> imageList = new ArrayList<SlideModel>();

        if ( currentCar.getPhoto() == null || currentCar.getPhoto().isEmpty()) imageList.add(new SlideModel(R.drawable.photo_iv_null,  ScaleTypes.CENTER_CROP));
        else imageList.add(new SlideModel(currentCar.getPhoto(),  ScaleTypes.CENTER_CROP));

        if ( currentCar.getSecondphoto() == null || currentCar.getSecondphoto().isEmpty()) i =2;
        else imageList.add(new SlideModel(currentCar.getSecondphoto(),  ScaleTypes.CENTER_CROP));

        if ( currentCar.getThirdPhoto() == null || currentCar.getThirdPhoto().isEmpty()) i=3;
        else imageList.add(new SlideModel(currentCar.getThirdPhoto(),  ScaleTypes.CENTER_CROP));

        if ( currentCar.getFourthPhoto() == null || currentCar.getFourthPhoto().isEmpty()) i=4;
        else imageList.add(new SlideModel(currentCar.getFourthPhoto(),  ScaleTypes.CENTER_CROP));

        if ( currentCar.getFifthPhoto() == null || currentCar.getFifthPhoto().isEmpty()) i=5;
        else imageList.add(new SlideModel(currentCar.getFifthPhoto(),  ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(imageList);

        imageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                if(currentCar.getSecondphoto() == null || currentCar.getSecondphoto().isEmpty() || currentCar.getSecondphoto().equals("")){

                    if(currentCar.getPhoto() == null || currentCar.getPhoto().isEmpty() || currentCar.getPhoto().equals("")) i =1;
                    else GoToMoreDetailedPhoto();
                }
                else GoToDetailedPhotos();
            }

            @Override
            public void doubleClick(int i) {
                // nothing....
            }
        });
    }

    private void GoToDetailedPhotos() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new DetailedPhotosFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void GoToEditPostFragment() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new EditPostFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void GoToFragmentCars() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }

    private void GoToNoUserHome() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new NoUserHomeFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }

    private void GoToForYouList(){

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ForYouListFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }

    private void GoToFragmentSearch() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new CarSearchListFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }

    private void GoToFragmentAdd() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AddCarFragment());
        ft.commit();
    }

    private void GoToFragmentSaved() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SavedCarsFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }

    private void GoToProfile() {
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void GoToUserListings() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new UserListingsFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }

    private void GoToMoreDetailedPhoto(){

        Fragment gtn = new MoreDetailedPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ImageNum", 1);
        gtn.setArguments(bundle);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.FrameLayoutMain, gtn);
        ft.commit();
    }

    private void setNavigationBarProfile() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.profile);
    }

    private BottomNavigationView getNavigationBar(){
        return ((MainActivity) getActivity()).getBottomNavigationView();
    }

}