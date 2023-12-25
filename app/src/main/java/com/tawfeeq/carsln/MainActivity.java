package com.tawfeeq.carsln;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView bnv;
    private FireBaseServices fbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // To Hide The Top Bar For App Name.
        getSupportActionBar().hide();


        // To Make the App not Flippable
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        bnv= findViewById(R.id.bottomNavigationView);
        fbs = FireBaseServices.getInstance();

            bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    if (item.getItemId() == R.id.market) {
                        GoToFragmentCars();
                    }
                    else if (item.getItemId() == R.id.searchcar){
                        GoToFragmentSearch();
                    }
                    else if (item.getItemId() == R.id.addcar) {
                        GoToFragmentAdd();
                    }
                    else if (item.getItemId() == R.id.profile){
                        GoToFragmentProfile();
                    }

                    return true;
                }
            });

            if(fbs.getAuth().getCurrentUser()!=null) {

                bnv.setVisibility(View.VISIBLE);
                GoToFragmentCars();
            }
            else {

                bnv.setVisibility(View.GONE);
                GoToStarterScreen();
            }

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

    private void GoToFragmentAdd() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AddCarFragment());
        ft.commit();
    }

    private void GoToFragmentProfile() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
        ft.commit();
    }

    private void GoToStarterScreen() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new StartFragment());
        ft.commit();
    }

    public BottomNavigationView getBottomNavigationView() {
        return bnv;
    }

}