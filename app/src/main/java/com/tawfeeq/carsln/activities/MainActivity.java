package com.tawfeeq.carsln.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.fragments.AddCarFragment;
import com.tawfeeq.carsln.fragments.AllCarsFragment;
import com.tawfeeq.carsln.fragments.CarSearchListFragment;
import com.tawfeeq.carsln.fragments.DetailedFragment;
import com.tawfeeq.carsln.fragments.DetailedPhotosFragment;
import com.tawfeeq.carsln.fragments.ForYouListFragment;
import com.tawfeeq.carsln.fragments.LogInFragment;
import com.tawfeeq.carsln.fragments.NoUserHomeFragment;
import com.tawfeeq.carsln.fragments.NoUserProfileFragment;
import com.tawfeeq.carsln.fragments.NoUserSavedFragment;
import com.tawfeeq.carsln.fragments.ProfileFragment;
import com.tawfeeq.carsln.fragments.SavedCarsFragment;
import com.tawfeeq.carsln.fragments.SearchFragment;
import com.tawfeeq.carsln.fragments.SettingsFragment;
import com.tawfeeq.carsln.fragments.UserListingsFragment;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.objects.UserProfile;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bnv;
    private FireBaseServices fbs;
    private UserProfile usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // To Hide the Top Action Bar.
        getSupportActionBar().hide();

        // To Make the App not Flip.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        fbs = FireBaseServices.getInstance();
        bnv= findViewById(R.id.bottomNavigationView);


        fbs.setCarList(null);
        fbs.setSearchList(null);
        fbs.setLastSearch(null);
        fbs.setLastFilter("null");

        fbs.setFrom("");
        fbs.setRcSearch(null);
        fbs.setRcSaved(null);
        fbs.setRcForYou(null);
        fbs.setRcListings(null);
        fbs.setCurrentFragment("");

        if(fbs.getAuth().getCurrentUser()!=null) {

            bnv.setSelectedItemId(R.id.market);
            bnv.setVisibility(View.VISIBLE);
            setSavedGoToMarket();
        }
        else {

            bnv.setSelectedItemId(R.id.market);
            bnv.setVisibility(View.VISIBLE);
            setNoAccount();
        }

        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.market) {
                    if(fbs.getAuth().getCurrentUser()!=null) GoToFragmentCars();
                    else GoToNoUserHomePage();
                }
                else if (item.getItemId() == R.id.searchcar){
                    GoToFragmentCarSearchList();
                }
                else if (item.getItemId() == R.id.addcar) {
                    if(fbs.getAuth().getCurrentUser()!=null) GoToFragmentAdd();
                    else GoToLogin();
                }
                else if (item.getItemId() == R.id.savedcars) {
                    if(fbs.getAuth().getCurrentUser()!=null) GoToFragmentSaved();
                    else GoToNoUserSaved();
                }
                else if (item.getItemId() == R.id.profile){
                    if(fbs.getAuth().getCurrentUser()!=null) GoToFragmentProfile();
                    else GoToNoUserProfile();
                }

                return true;
            }
        });

    }

    public void setNoAccount() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new NoUserHomeFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }

    public void setSavedGoToMarket() {

        if(fbs.getUser() == null) {

            Dialog loading = new Dialog(MainActivity.this);
            loading.setContentView(R.layout.loading_dialog);
            loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loading.setCancelable(false);
            loading.show();


            String str = fbs.getAuth().getCurrentUser().getEmail();
            int n = str.indexOf("@");
            String user = str.substring(0, n);
            fbs.getStore().collection("Users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    usr = documentSnapshot.toObject(UserProfile.class);
                    fbs.setUser(usr);

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();

                    loading.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(MainActivity.this, "Couldn't Retrieve User Info, Please Try Again Later!", Toast.LENGTH_SHORT).show();
                    fbs.setUser(null);

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
                    ft.commit();

                    loading.dismiss();

                }
            });

        }else {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
            ft.commit();

        }
    }

    public void setSavedProfileLogin() {

        Dialog loading = new Dialog(MainActivity.this);
        loading.setContentView(R.layout.loading_dialog);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setCancelable(false);
        loading.show();


        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0, n);
        fbs.getStore().collection("Users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                usr = documentSnapshot.toObject(UserProfile.class);
                fbs.setUser(usr);

                bnv.setSelectedItemId(R.id.profile);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();

                loading.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MainActivity.this, "Couldn't Retrieve User Info, Please Try Again Later!", Toast.LENGTH_SHORT).show();
                fbs.setUser(null);

                bnv.setSelectedItemId(R.id.profile);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
                ft.commit();

                loading.dismiss();

            }
        });

    }

    public boolean isNetworkAvailable(){

        try {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if(manager!=null){

                networkInfo = manager.getActiveNetworkInfo();

            }
            return networkInfo != null && networkInfo.isConnected();
        }
        catch (NullPointerException e){
            return false;
        }

    }







    // Back Button........................................
    @Override
    public void onBackPressed() {

        String fragment = fbs.getCurrentFragment();
        if(!fragment.equals("")){

            if(fragment.equals("AllCars") || fragment.equals("NoUserHome")){

                // Keep App Open!!!!!!!!!!

            } else if(fragment.equals("Login")){

                GoToNoUserHomePage();
                bnv.setVisibility(View.VISIBLE);
                bnv.setSelectedItemId(R.id.market);

            } else if(fragment.equals("AddCar") || fragment.equals("AddPhotos")){

                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.post_alert);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
                dialog.setCancelable(true);
                dialog.show();

                Button btnClose = dialog.findViewById(R.id.btnConfirmClose);
                Button btnCancel = dialog.findViewById(R.id.btnCancelClose);

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bnv.setSelectedItemId(R.id.market); // Simple, Might turn this into a Stack.....
                        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        ft.commit();

                        bnv.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }else if(fragment.equals("Detailed")) {

                if (bnv.getSelectedItemId() == R.id.market) {
                    if(fbs.getAuth().getCurrentUser()!=null) {

                        if (fbs.getFrom().equals("Near") || fbs.getFrom().equals("New") || fbs.getFrom().equals("Used"))
                            GoToForYouList();
                        else {

                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                            ft.commit();
                            bnv.setVisibility(View.VISIBLE);
                        }

                    } else {

                        if (fbs.getFrom().equals("Near") || fbs.getFrom().equals("New") || fbs.getFrom().equals("Used"))
                            GoToForYouList();
                        else {

                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.FrameLayoutMain, new NoUserHomeFragment());
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                            ft.commit();
                            bnv.setVisibility(View.VISIBLE);

                        }

                    }
                }
                else if (bnv.getSelectedItemId() == R.id.searchcar){

                    FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FrameLayoutMain, new CarSearchListFragment());
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();
                    bnv.setVisibility(View.VISIBLE);
                }
                else if (bnv.getSelectedItemId() == R.id.savedcars) {

                    FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FrameLayoutMain, new SavedCarsFragment());
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();
                    bnv.setVisibility(View.VISIBLE);
                }
                else if (bnv.getSelectedItemId() == R.id.profile){

                    FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FrameLayoutMain, new UserListingsFragment());
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();
                    bnv.setVisibility(View.VISIBLE);
                }


            }else if(fragment.equals("DetailedPhotos") || fragment.equals("EditPost")){

                GoToDetailed();

            }else if(fragment.equals("Forgot") || fragment.equals("Signup")){

                GoToFragmentLogin();

            }else if(fragment.equals("Search")){

                FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayoutMain, new CarSearchListFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.commit();
                // Simple, Might turn this into a Stack.....
                bnv.setVisibility(View.VISIBLE);

            }
            else if(fragment.equals("Settings") || fragment.equals("UserListings")){

                FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.commit();

                fbs.setFrom("");

            }else if(fragment.equals("ViewPhoto")){

                if(fbs.getFrom().equals("Profile")) {
                    bnv.setSelectedItemId(R.id.profile);
                    bnv.setVisibility(View.VISIBLE);
                }
                else if(fbs.getFrom().equals("Settings")) {
                    GoToSettings();
                    bnv.setVisibility(View.VISIBLE);
                }
                fbs.setFrom("");

            }else if(fragment.equals("ForYou")){

                bnv.setSelectedItemId(R.id.market);
                bnv.setVisibility(View.VISIBLE);
                fbs.setFrom("");

            }else if(fragment.equals("MoreDetailedPhoto")){

                CarID currentCar = fbs.getSelectedCar();
                if(currentCar.getSecondphoto() == null || currentCar.getSecondphoto().isEmpty() || currentCar.getSecondphoto().equals("")) GoToDetailed(); // Car Listing only has one Photo.
                else GoToDetailedPhotos(); // Car Listing more than one Photo.

            }else if(fragment.equals("ContactUs")){

                if(fbs.getAuth().getCurrentUser()!=null){

                    FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();

                } else {

                    FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FrameLayoutMain, new NoUserProfileFragment());
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();

                }

            }else {

                bnv.setSelectedItemId(R.id.market); // Simple, Might turn this into a Stack.....

            }

        }

    }
    // Ends................................................................






    private void GoToDetailedPhotos() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new DetailedPhotosFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void GoToFragmentCars() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
        ft.commit();
    }

    private void GoToNoUserHomePage() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new NoUserHomeFragment());
        ft.commit();
    }

    private void GoToFragmentSearch() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SearchFragment());
        ft.commit();
    }

    private void GoToFragmentCarSearchList() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new CarSearchListFragment());
        ft.commit();
    }

    private void GoToFragmentAdd() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AddCarFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void GoToFragmentSaved() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SavedCarsFragment());
        ft.commit();
    }

    private void GoToNoUserSaved() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new NoUserSavedFragment());
        ft.commit();
    }

    private void GoToFragmentProfile() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
        ft.commit();
    }

    private void GoToNoUserProfile() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new NoUserProfileFragment());
        ft.commit();
    }

    private void GoToSettings() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SettingsFragment());
        ft.commit();
    }

    private void GoToDetailed() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new DetailedFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void GoToForYouList(){

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ForYouListFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }

    private void GoToUserListings() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new UserListingsFragment());
        ft.commit();
    }

    private void GoToLogin() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new LogInFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void GoToFragmentLogin(){

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new LogInFragment());
        ft.commit();
    }

    public BottomNavigationView getBottomNavigationView() {
        return bnv;
    }

}