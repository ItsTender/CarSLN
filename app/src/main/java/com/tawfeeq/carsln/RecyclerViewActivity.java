package com.tawfeeq.carsln;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity {

    FireBaseServices fbs;
    private RecyclerView rc;
    private CarsAdapter adapter;
    ArrayList<Cars> Market;
    TextView tvSell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        fbs = FireBaseServices.getInstance();
        rc= findViewById(R.id.rcView);
        tvSell=findViewById(R.id.SellURCar);
        Market = new ArrayList<Cars>();

        tvSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GoToAddMain();

            }
        });

        // checking accessibility to FireStore Info
        fbs.getStore().collection("MarketPlace").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){

                    Cars car = dataSnapshot.toObject(Cars.class);
                    Market.add(car);

                    SettingFrame();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RecyclerViewActivity.this, "Couldn't Retrieve Info, Please Try Again Later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GoToAddMain() {

        Intent i= new Intent(this, MainActivity2.class);
        startActivity(i);

    }

    private void SettingFrame() {

        rc.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CarsAdapter(this,Market);
        rc.setAdapter(adapter);
        rc.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

    }
}