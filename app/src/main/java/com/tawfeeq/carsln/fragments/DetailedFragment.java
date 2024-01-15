package com.tawfeeq.carsln.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.MainActivity;
import com.tawfeeq.carsln.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedFragment extends Fragment {

    FireBaseServices fbs;
    TextView tvMan, tvPrice, tvPower, tvYear, tvUsers, tvKilometre, tvTransmission, tvSeller, tvEngine, tvLocation, tvTest, tvColor, tvNotes;
    ImageView ivCar, ivSeller, ivSaved, ivBack, ivDelete;
    boolean isFound;
    String pfp;
    CarID currentCar;

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
        ivCar=getView().findViewById(R.id.DetailedCar);
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
        tvSeller = getView().findViewById(R.id.DetailedUserMail);
        ivSeller = getView().findViewById(R.id.imageViewSeller);
        tvEngine = getView().findViewById(R.id.DetailedEngine);
        ivSaved = getView().findViewById(R.id.ivSavedCar); //the Saved Icon......
        ivBack =getView().findViewById(R.id.DetailedGoBack); // Goes Back To Where ever the User Was.
        ivDelete =getView().findViewById(R.id.DetailedDeleteListing);

        currentCar = fbs.getSelectedCar();

        String str = fbs.getSelectedCar().getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0,n);

        // Get User Profile Photo.....

        if(fbs.getUser()!=null && str.equals(fbs.getAuth().getCurrentUser().getEmail())){

            tvSeller.setText(fbs.getUser().getUsername());

            pfp = fbs.getUser().getUserPhoto();
            if (pfp == null || pfp.isEmpty()) {
                ivSeller.setImageResource(R.drawable.slnpfp);
            } else {
                Glide.with(getActivity()).load(pfp).into(ivSeller);
            }

            ivDelete.setVisibility(View.VISIBLE);


            // Custom Delete Dialog!
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_delete_listing);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
            dialog.setCancelable(false);

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

                            ArrayList<CarID> Market = fbs.getMarketList();
                            Market.remove(currentCar);
                            fbs.setMarketList(Market);

                            if (bnv.getSelectedItemId() == R.id.market) {
                                GoToFragmentCars(); // Maybe One of the Many Show All Options.
                            }
                            else if (bnv.getSelectedItemId() == R.id.searchcar){
                                GoToFragmentSearch();
                            }
                            else if (bnv.getSelectedItemId() == R.id.savedcars) {
                                GoToFragmentSaved();
                            }
                            else if (bnv.getSelectedItemId() == R.id.profile){
                                GoToProfile(); // Profile/ User Listings.
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Couldn't Deleted Your Car Listing, Try Again Later", Toast.LENGTH_SHORT).show();
                        }
                    });

                    dialog.dismiss();
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

        }
        else {

            ivDelete.setVisibility(View.INVISIBLE);
            fbs.getStore().collection("Users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    tvSeller.setText(documentSnapshot.getString("username"));

                    pfp = documentSnapshot.getString("userPhoto");

                    if (pfp == null || pfp.isEmpty()) {
                        ivSeller.setImageResource(R.drawable.slnpfp);
                    } else {
                        Glide.with(getActivity()).load(pfp).into(ivSeller);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Couldn't Retrieve User Profile Info", Toast.LENGTH_SHORT).show();
                    ivSeller.setImageResource(R.drawable.slnpfp);
                }
            });
        }

        // Get Profile Photo Ends


        if(!currentCar.getPhoto().equals("")) {
            ivCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BottomNavigationView bnv = getNavigationBar();

                    bnv.setVisibility(View.GONE);

                    Fragment gtn = new DetailedPhotosFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.replace(R.id.FrameLayoutMain, gtn);
                    ft.commit();
                }
            });
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomNavigationView bnv = getNavigationBar();

                if (bnv.getSelectedItemId() == R.id.market) {
                    GoToFragmentCars();// Maybe One of the Many Show All Options.
                }
                else if (bnv.getSelectedItemId() == R.id.searchcar){
                    GoToFragmentSearch();
                }
                else if (bnv.getSelectedItemId() == R.id.savedcars) {
                    GoToFragmentSaved();
                }
                else if (bnv.getSelectedItemId() == R.id.profile){
                    GoToProfile();// Maybe One of the Many Show All Options.
                }
            }
        });

        tvSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = fbs.getAuth().getCurrentUser().getEmail();

                if(str.equals(currentCar.getEmail())){
                    // Always will be like that!
                    GoToProfile();
                    setNavigationBarProfile();
                }
                else {

                    Fragment gtn = new SellerPageFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.replace(R.id.FrameLayoutMain, gtn);
                    ft.commit();
                }
            }
        });

        ivSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = fbs.getAuth().getCurrentUser().getEmail();

                if(str.equals(currentCar.getEmail())){

                    GoToProfile();
                    setNavigationBarProfile();
                }
                else {

                    Fragment gtn = new SellerPageFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.replace(R.id.FrameLayoutMain, gtn);
                    ft.commit();
                }
            }
        });


        // Bind the Saved Icon *and* Save/Remove the Car

        String str1 = fbs.getAuth().getCurrentUser().getEmail();
        int n1 = str1.indexOf("@");
        String user1 = str1.substring(0,n1);
        ArrayList<String> Saved;

        if(fbs.getUser()!=null) Saved = fbs.getUser().getSavedCars();
        else Saved = new ArrayList<String>();

        if(Saved.contains(currentCar.getId())) {
            ivSaved.setImageResource(R.drawable.bookmark_filled);
            isFound = true;
        }
        else{
            ivSaved.setImageResource(R.drawable.bookmark_unfilled);
            isFound = false;
        }

        ivSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fbs.getUser()!=null) {
                    if (isFound) Saved.remove(currentCar.getId());
                    if (!isFound) Saved.add(currentCar.getId());

                    fbs.getStore().collection("Users").document(user1).update("savedCars", Saved).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if (isFound) {
                                ivSaved.setImageResource(R.drawable.bookmark_unfilled);
                                fbs.getUser().setSavedCars(Saved);
                                isFound = false;
                            } else {
                                ivSaved.setImageResource(R.drawable.bookmark_filled);
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


        // Functions Ends......


        tvMan.setText(currentCar.getManufacturer() + " " + currentCar.getModel());

        String prc= String.valueOf(currentCar.getPrice());
        if(currentCar.getSellLend()) {
            tvPrice.setText(prc + "₪");
        }
        else{
            tvPrice.setText(prc + "₪" + " Monthly");
        }

        String power=String.valueOf(currentCar.getBHP()); tvPower.setText(power);
        String year=String.valueOf(currentCar.getYear()); tvYear.setText(year);
        String Kilo =String.valueOf(currentCar.getKilometre()); tvKilometre.setText(Kilo+" km");
        tvTransmission.setText(currentCar.getTransmission());
        tvLocation.setText(currentCar.getLocation());
        tvTest.setText(currentCar.getNextTest());
        tvColor.setText(currentCar.getColor());

        if(currentCar.getUsers()==1){
            String Owners= String.valueOf(currentCar.getUsers()); tvUsers.setText(Owners + " Owner");
        }
        else {
            String Owners= String.valueOf(currentCar.getUsers()); tvUsers.setText(Owners + " Owners");
        }

        tvEngine.setText(currentCar.getEngine());

        tvNotes.setText(currentCar.getNotes());

        if ( currentCar.getPhoto() == null || currentCar.getPhoto().isEmpty())
        {
            ivCar.setImageResource(R.drawable.photo_iv_null);

        }
        else {
            Glide.with(getActivity()).load(currentCar.getPhoto()).into(ivCar);

        }

    }

    private void GoToFragmentCars() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void GoToFragmentSearch() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new CarSearchListFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void GoToProfile() {
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void setNavigationBarProfile() {
        ((MainActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.profile);
    }

    private BottomNavigationView getNavigationBar(){
        return ((MainActivity) getActivity()).getBottomNavigationView();
    }



}