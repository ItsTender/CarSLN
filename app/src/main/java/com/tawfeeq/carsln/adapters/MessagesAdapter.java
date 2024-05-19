package com.tawfeeq.carsln.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.objects.MessageID;
import com.tawfeeq.carsln.objects.MessageModel;
import com.tawfeeq.carsln.objects.Users;

import java.util.ArrayList;


public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MessageID> messagesAdpterArrayList;
    FireBaseServices fbs;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;

    public MessagesAdapter(Context context, ArrayList<MessageID> messagesAdpterArrayList) {
        this.context = context;
        this.messagesAdpterArrayList = messagesAdpterArrayList;
        fbs = FireBaseServices.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.layout_sender, parent, false);
            return new senderVierwHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_receiver, parent, false);
            return new reciverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageID messages = messagesAdpterArrayList.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context).setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String str = fbs.getAuth().getCurrentUser().getEmail();
                                int n = str.indexOf("@");
                                String userID = str.substring(0, n);

                                Users user = fbs.getSelectedUser();

                                String senderRoom = userID + user.getDocID();
                                String receiverRoom = user.getDocID() + userID;


                                FirebaseDatabase db= FirebaseDatabase.getInstance();
                                db.getReference().child("chats")
                                        .child(senderRoom)
                                        .child("messages").child(messages.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                return true;
            }
        });

        if (holder.getClass()==senderVierwHolder.class){
            senderVierwHolder viewHolder = (senderVierwHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
        }else { reciverViewHolder viewHolder = (reciverViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messagesAdpterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageID messages = messagesAdpterArrayList.get(position);

        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String userID = str.substring(0, n);

        if (userID.equals(messages.getSenderID())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECIVE;
        }
    }

    class senderVierwHolder extends RecyclerView.ViewHolder {

        TextView msgtxt;

        public senderVierwHolder(@NonNull View itemView) {
            super(itemView);

            msgtxt = itemView.findViewById(R.id.msgsendertyp);
        }
    }
    class reciverViewHolder extends RecyclerView.ViewHolder {

        TextView msgtxt;

        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);

            msgtxt = itemView.findViewById(R.id.recivertextset);
        }
    }
}

