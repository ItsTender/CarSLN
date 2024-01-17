package com.tawfeeq.carsln;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tawfeeq.carsln.fragments.AddCarFragment;
import com.tawfeeq.carsln.fragments.AddCarIntroFragment;
import com.tawfeeq.carsln.fragments.AllCarsFragment;
import com.tawfeeq.carsln.fragments.CarSearchListFragment;
import com.tawfeeq.carsln.fragments.LogInFragment;
import com.tawfeeq.carsln.fragments.ProfileFragment;
import com.tawfeeq.carsln.fragments.SavedCarsFragment;
import com.tawfeeq.carsln.fragments.SearchFragment;
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

        // To Hide The Top Bar For The App's Name.
        getSupportActionBar().hide();

        // To Make the App not Flip.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        bnv= findViewById(R.id.bottomNavigationView);
        fbs = FireBaseServices.getInstance();


        if(fbs.getAuth().getCurrentUser()!=null) {

            bnv.setVisibility(View.VISIBLE);
            bnv.setSelectedItemId(R.id.market);
            setSavedGoToMarket();
        }
        else {

            bnv.setVisibility(View.GONE);
            GoToLogin();
        }
            bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    if (item.getItemId() == R.id.market) {
                        GoToFragmentCars();
                    }
                    else if (item.getItemId() == R.id.searchcar){
                        GoToFragmentCarSearchList();
                    }
                    else if (item.getItemId() == R.id.addcar) {
                        GoToFragmentAdd();
                    }
                    else if (item.getItemId() == R.id.savedcars) {
                        GoToFragmentSaved();
                    }
                    else if (item.getItemId() == R.id.profile){
                        GoToFragmentProfile();
                    }

                    return true;
                }
            });
    }

    public void setCarsMarket() {
        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0,n);
        fbs.getStore().collection("Users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                usr = documentSnapshot.toObject(UserProfile.class);
                fbs.setUser(usr);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Couldn't Retrieve User Info, Please Try Again Later!", Toast.LENGTH_SHORT).show();
                fbs.setUser(null);
            }
        });
    }

    public void setSavedGoToMarket() {

        ProgressDialog progressDialog= new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Loading CarSLN");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIcon(R.drawable.slnround);
        progressDialog.setCancelable(false);
        progressDialog.show();

        fbs.setCarList(null);
        fbs.setSearchList(null);

        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String user = str.substring(0,n);
        fbs.getStore().collection("Users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                progressDialog.dismiss();

                usr = documentSnapshot.toObject(UserProfile.class);
                fbs.setUser(usr);

                bnv.setSelectedItemId(R.id.market);
                FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();

                Toast.makeText(MainActivity.this, "Couldn't Retrieve User Info, Please Try Again Later!", Toast.LENGTH_SHORT).show();
                fbs.setUser(null);

                FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
                ft.commit();

            }
        });

    }


    private void GoToFragmentCars() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
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
        ft.commit();
    }

    private void GoToFragmentSaved() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new SavedCarsFragment());
        ft.commit();
    }

    private void GoToFragmentProfile() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
        ft.commit();
    }

    private void GoToLogin() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new LogInFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public BottomNavigationView getBottomNavigationView() {
        return bnv;
    }

}