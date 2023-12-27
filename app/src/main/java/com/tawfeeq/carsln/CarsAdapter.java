package com.tawfeeq.carsln;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarsHolder> {

// 1- Planet Adapter

    private Context context;
    private  CarsAdapter.OnItemClickListener CarsListener;
    private ArrayList<CarID> cars;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(CarsAdapter.OnItemClickListener Listener) {
        this.CarsListener = Listener;
    }
    public CarsAdapter(Context context, ArrayList<CarID> cars) {
        this.context = context;
        this.cars = cars;

        this.CarsListener =new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Fragment gtn= new DetailedFragment();
                Bundle bundle= new Bundle();


                bundle.putString("ID", cars.get(position).getId());
                bundle.putBoolean("SellorLend",cars.get(position).getSellLend());
                bundle.putString("Email", cars.get(position).getEmail());
                bundle.putString("Man", cars.get(position).getManufacturer());
                bundle.putString("Mod", cars.get(position).getModel());
                bundle.putInt("HP", cars.get(position).getBHP());
                bundle.putInt("Price", cars.get(position).getPrice());
                bundle.putString("Photo", cars.get(position).getPhoto());
                bundle.putString("Engine", cars.get(position).getEngine());
                bundle.putString("Transmission", cars.get(position).getTransmission());
                bundle.putInt("Year", cars.get(position).getYear());
                bundle.putInt("Kilo", cars.get(position).getKilometre());
                bundle.putInt("Users", cars.get(position).getUsers());
                bundle.putString("Color", cars.get(position).getColor());
                bundle.putString("Area", cars.get(position).getLocation());
                bundle.putString("Test", cars.get(position).getNextTest());


                gtn.setArguments(bundle);
                FragmentTransaction ft= ((MainActivity)context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.FrameLayoutMain, gtn);
                ft.commit();
            }
        };
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

        CarID car= cars.get(position);
        holder.SetDetails(car);

        holder.itemView.setOnClickListener(v -> {
            if (CarsListener != null) {
                CarsListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cars.size();
    }


    // 2- Cars Holder

    class CarsHolder extends RecyclerView.ViewHolder {

        private TextView txtMan,txtMod,txtHP,txtYear,txtPrice;
        private ImageView ivCar; // shows the car photo from the firestore string url, if the photo url is "" then show the stock image (R.drawable.carplain.jpg)
        public CarsHolder(@NonNull View itemView) {
            super(itemView);

            txtMan= itemView.findViewById(R.id.tvtxtMan);
            txtMod= itemView.findViewById(R.id.tvtxtMod);
            txtHP= itemView.findViewById(R.id.tvtxtHP);
            txtYear= itemView.findViewById(R.id.tvtxtYear);
            txtPrice= itemView.findViewById(R.id.tvtxtPrice);
            ivCar= itemView.findViewById(R.id.CarRes);

        }

        void SetDetails (CarID car){

            txtMan.setText(car.getManufacturer());
            txtMod.setText(car.getModel());
            txtHP.setText(car.getBHP() + " Horse Power");
            String year = String.valueOf(car.getYear());
            txtYear.setText(year);

            if(car.getSellLend()==true){

            txtPrice.setText(car.getPrice()+"$");
            }
            else if(car.getSellLend()==false){

                txtPrice.setText(car.getPrice()+ "$" +" Monthly");
            }

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
