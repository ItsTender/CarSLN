package com.tawfeeq.carsln.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.objects.UserProfile;

public class SplashScreenActivity extends AppCompatActivity {

    FireBaseServices fbs;
    UserProfile usr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // To Hide the Top Action Bar.
        getSupportActionBar().hide();

        // To Make the App not Flip.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        fbs = FireBaseServices.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(fbs.getAuth().getCurrentUser()!=null) {

                    fbs.setCarList(null);
                    fbs.setSearchList(null);
                    fbs.setLastSearch(null);
                    fbs.setLastFilter("null");

                    String str = fbs.getAuth().getCurrentUser().getEmail();
                    int n = str.indexOf("@");
                    String user = str.substring(0, n);
                    fbs.getStore().collection("Users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            usr = documentSnapshot.toObject(UserProfile.class);
                            fbs.setUser(usr);
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(SplashScreenActivity.this, "Couldn't Retrieve User Info, Please Try Again Later!", Toast.LENGTH_SHORT).show();
                            fbs.setUser(null);
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                }else {

                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                }

            }
        } ,1600);

    }
}