package com.tawfeeq.carsln;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity {

    FireBaseServices fbs;
    RecyclerView rc;
    ArrayList<Cars> Market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        fbs = FireBaseServices.getInstance();
        rc= findViewById(R.id.rcView);
        Market = new ArrayList<Cars>();

        // checking accessibility to FireStore Info
        fbs.getStore().collection("MarketPlace").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){

                    Cars car = dataSnapshot.toObject(Cars.class);
                    Market.add(car);

                    rc.setLayoutManager(new LinearLayoutManager(this));
                    adapter=new CarsAdapter(this,Market);
                    rc.setAdapter(adapter);
                    rc.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(this, "Couldn't Retrieve Information, Try Again Later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}