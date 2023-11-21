package com.tawfeeq.carsln;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.checkerframework.common.reflection.qual.GetClass;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView bnv;
    private FireBaseServices fbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        bnv= findViewById(R.id.bottomNavigationView);
        fbs = FireBaseServices.getInstance();

            bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    if (item.getItemId() == R.id.market) {
                        GoToFragmentCars();
                    } else if (item.getItemId() == R.id.addcar) {
                        GoToFragmentAdd();
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
                GoToLogIn();

            }

    }

    private void GoToFragmentAdd() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AddCarFragment());
        ft.commit();
    }
    private void GoToFragmentCars() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new AllCarsFragment());
        ft.commit();
    }

    private void GoToLogIn() {

        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new LogInFragment());
        ft.commit();

    }

    public BottomNavigationView getBottomNavigationView() {
        return bnv;
    }

}