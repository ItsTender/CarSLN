package com.tawfeeq.carsln;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarsHolder> {

// 1- Planet Adapter

    private Context context;
    private ArrayList<Cars> cars;

    public CarsAdapter(Context context, ArrayList<Cars> cars) {
        this.context = context;
        this.cars = cars;
    }

    public CarsAdapter() {
    }

    @NonNull
    @Override
    public CarsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cars_test,parent,false);

        return new CarsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarsAdapter.CarsHolder holder, int position) {

        Cars car= cars.get(position);
        holder.SetDetails(car);

    }

    @Override
    public int getItemCount() {
        return cars.size();
    }


    // 2- Cars Holder

    class CarsHolder extends RecyclerView.ViewHolder {

        private TextView txtMan,txtMod,txtHP,txtPrice;
        private ImageView ivCar; // shows the car photo from the firestore string url, if the photo url is "" then show the stock image (R.drawable.carplain.jpg)
        public CarsHolder(@NonNull View itemView) {
            super(itemView);

            txtMan= itemView.findViewById(R.id.tvtxtMan);
            txtMod= itemView.findViewById(R.id.tvtxtMod);
            txtHP= itemView.findViewById(R.id.tvtxtHP);
            txtPrice= itemView.findViewById(R.id.tvtxtPrice);
            ivCar= itemView.findViewById(R.id.CarRes);

        }

        void SetDetails (Cars car){

            txtMan.setText(car.getManufacturer());
            txtMod.setText(car.getModel());
            txtHP.setText("Horse Power: "+car.getBHP());
            txtPrice.setText("Price: "+ car.getPrice()+"$");

            if (car.getPhoto() == null || car.getPhoto().isEmpty())
            {
                Picasso.get().load(R.drawable.carplain).into(ivCar);
            }
            else {
                Picasso.get().load(car.getPhoto()).into(ivCar);
            }

        }

    }




}
