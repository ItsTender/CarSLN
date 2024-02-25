package com.tawfeeq.carsln.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.fragments.DetailedFragment;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.objects.FireBaseServices;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ForYouCarsAdapter extends RecyclerView.Adapter<ForYouCarsAdapter.ForYouCarsHolder> {


    private Context context;
    private  CarsAdapter.OnItemClickListener CarsListener;
    private CarsAdapter.OnItemClickListener SavedListener;
    private ArrayList<CarID> cars;
    private FireBaseServices fbs;
    private ArrayList<String> Saved;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(CarsAdapter.OnItemClickListener Listener) {
        this.CarsListener = Listener;
    }

    public boolean isConnected(){

        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if(manager!=null){

                networkInfo = manager.getActiveNetworkInfo();

            }
            return networkInfo != null && networkInfo.isConnected();

        }catch (NullPointerException e){
            return false;
        }
    }

    public ForYouCarsAdapter(Context context, ArrayList<CarID> cars, ArrayList<String> Saved) {
        this.context = context;
        this.cars = cars;
        this.Saved = Saved;

        this.CarsListener =new CarsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                fbs = FireBaseServices.getInstance();
                if(isConnected() && cars.get(position)!=null) {
                    Fragment gtn = new DetailedFragment();
                    //Bundle bundle = new Bundle();

                    fbs.setSelectedCar(cars.get(position));


                    //bundle.putString("ID", cars.get(position).getId());
                    //bundle.putBoolean("SellorLend", cars.get(position).getSellLend());
                    //bundle.putString("Email", cars.get(position).getEmail());
                    //bundle.putString("Man", cars.get(position).getManufacturer());
                    //bundle.putString("Mod", cars.get(position).getModel());
                    //bundle.putInt("HP", cars.get(position).getBHP());
                    //bundle.putInt("Price", cars.get(position).getPrice());
                    //bundle.putString("Photo", cars.get(position).getPhoto());
                    //bundle.putString("Second", cars.get(position).getSecondphoto());
                    //bundle.putString("Third", cars.get(position).getThirdPhoto());
                    //bundle.putString("Fourth", cars.get(position).getFourthPhoto());
                    //bundle.putString("Fifth", cars.get(position).getFifthPhoto());
                    //bundle.putString("Engine", cars.get(position).getEngine());
                    //bundle.putString("Transmission", cars.get(position).getTransmission());
                    //bundle.putInt("Year", cars.get(position).getYear());
                    //bundle.putInt("Kilo", cars.get(position).getKilometre());
                    //bundle.putInt("Users", cars.get(position).getUsers());
                    //bundle.putString("Color", cars.get(position).getColor());
                    //bundle.putString("Area", cars.get(position).getLocation());
                    //bundle.putString("Test", cars.get(position).getNextTest());
                    //bundle.putString("Notes", cars.get(position).getNotes());


                    //gtn.setArguments(bundle);
                    FragmentTransaction ft = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.FrameLayoutMain, gtn);
                    ft.commit();
                }
            }
        };

    }

    public ForYouCarsAdapter() {
    }

    @NonNull
    @Override
    public ForYouCarsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cars_layout,parent,false);

        return new ForYouCarsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForYouCarsHolder holder, @SuppressLint("RecyclerView") int position) {

        CarID car = cars.get(position);
        holder.SetDetails(car);

        holder.itemView.setOnClickListener(v -> {
            if (CarsListener != null) {
                CarsListener.onItemClick(position);
            }
        });

        holder.ivSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fbs= FireBaseServices.getInstance();

                String str1 = fbs.getAuth().getCurrentUser().getEmail();
                int n1 = str1.indexOf("@");
                String user1 = str1.substring(0,n1);

                final Boolean[] isFound = new Boolean[1];

                if (isConnected() && cars.get(position) != null) {

                    if (Saved.contains(cars.get(position).getId())) {
                        isFound[0] = true;
                    } else {
                        isFound[0] = false;
                    }

                    if (fbs.getUser() != null) {
                        if (isFound[0]) {
                            Saved.remove(cars.get(position).getId());
                            holder.ivSaved.setImageResource(R.drawable.cars_saved_unfilled);
                        }
                        if (!isFound[0]) {
                            Saved.add(cars.get(position).getId());
                            holder.ivSaved.setImageResource(R.drawable.cars_saved_filled);
                        }
                        fbs.getStore().collection("Users").document(user1).update("savedCars", Saved).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if (isFound[0]) {
                                    holder.ivSaved.setImageResource(R.drawable.cars_saved_unfilled);
                                    fbs.getUser().setSavedCars(Saved);
                                    isFound[0] = false;
                                } else {
                                    holder.ivSaved.setImageResource(R.drawable.cars_saved_filled);
                                    fbs.getUser().setSavedCars(Saved);
                                    isFound[0] = true;
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Couldn't Save/Remove The Car, Try Again Later", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cars.size();
    }


    class ForYouCarsHolder extends RecyclerView.ViewHolder {

        private TextView txtCar,txtKilo,txtYear,txtPrice,txtOwners;
        private ImageView ivCar;// shows the car photo from the firestore string url, if the photo url is "" then show the stock image (R.drawable.carplain.jpg)
        private ImageView ivSaved;
        public ForYouCarsHolder(@NonNull View itemView) {
            super(itemView);

            txtCar= itemView.findViewById(R.id.tvtxtCarName);
            txtKilo = itemView.findViewById(R.id.tvtxtKilo);
            txtYear= itemView.findViewById(R.id.tvtxtYear);
            txtPrice= itemView.findViewById(R.id.tvtxtPrice);
            txtOwners = itemView.findViewById(R.id.tvtxtOwners);
            ivCar= itemView.findViewById(R.id.CarRes);
            ivSaved = itemView.findViewById(R.id.ivSavedCarAdapter);

        }

        void SetDetails (CarID car){

            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
            symbols.setGroupingSeparator(',');
            formatter.setDecimalFormatSymbols(symbols);


            txtCar.setText(car.getManufacturer() + " " + car.getModel());
            txtKilo.setText(formatter.format(car.getKilometre()) + " km");
            String year = String.valueOf(car.getYear());
            txtYear.setText(year);


            if(car.getUsers()==1){
                String Owners= String.valueOf(car.getUsers()); txtOwners.setText(Owners + " Owner");
            }
            else {
                String Owners= String.valueOf(car.getUsers()); txtOwners.setText(Owners + " Owners");
            }


            if(car.getSellLend()==true){

                txtPrice.setText(formatter.format(car.getPrice()) + "₪");
            }
            else if(car.getSellLend()==false){

                txtPrice.setText(formatter.format(car.getPrice()) + "₪" + " Monthly");
            }

            if (car.getPhoto() == null || car.getPhoto().isEmpty())
            {
                ivCar.setImageResource(R.drawable.photo_iv_null);
            }
            else {
                Glide.with(context).load(car.getPhoto()).into(ivCar);
            }


            fbs = FireBaseServices.getInstance();

            if(fbs.getAuth().getCurrentUser()==null){

                ivSaved.setVisibility(View.INVISIBLE);

            } else {

                if (Saved.contains(car.getId())) {
                    ivSaved.setImageResource(R.drawable.cars_saved_filled);
                } else {
                    ivSaved.setImageResource(R.drawable.cars_saved_unfilled);
                }

            }

        }
    }
}
