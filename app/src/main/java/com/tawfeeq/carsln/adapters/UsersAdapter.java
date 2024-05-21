package com.tawfeeq.carsln.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.tawfeeq.carsln.fragments.ChatFragment;
import com.tawfeeq.carsln.fragments.DetailedFragment;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.objects.Users;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersHolder> {

    private Context context;
    private ArrayList<Users> users;
    private FireBaseServices fbs;

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

    public UsersAdapter(Context context, ArrayList<Users> users) {
        this.context = context;
        this.users = users;
    }

    public UsersAdapter() {
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);

        return new UsersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UsersHolder holder, @SuppressLint("RecyclerView") int position) {

        Users user = users.get(position);
        holder.SetDetails(user);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fbs = FireBaseServices.getInstance();
                if(isConnected() && users.get(position)!=null) {
                    Fragment gtn = new ChatFragment();
                    //Bundle bundle = new Bundle();

                    fbs.setSelectedUser(users.get(position));

                    FragmentTransaction ft = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.replace(R.id.FrameLayoutMain, gtn);
                    ft.commit();

                }
                else {

                    // No Connection Dialog!
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_no_network);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
                    dialog.setCancelable(true);
                    dialog.show();

                    Button btn = dialog.findViewById(R.id.btnOK);

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                }

            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + user.getPhone()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }



    class UsersHolder extends RecyclerView.ViewHolder {

        ImageView iv, call;
        TextView username, phone;

        public UsersHolder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.ivUserChat);
            call = itemView.findViewById(R.id.ivCallUser);
            username = itemView.findViewById(R.id.username);
            phone = itemView.findViewById(R.id.phone);

        }

        void SetDetails (Users user){

            username.setText(user.getUsername());
            phone.setText(String.valueOf(user.getPhone()));

            if (user.getUserPhoto() == null || user.getUserPhoto().isEmpty())
            {
                iv.setImageResource(R.drawable.slnpfp);
            }
            else {
                Glide.with(context).load(user.getUserPhoto()).into(iv);
            }

        }
    }
}
