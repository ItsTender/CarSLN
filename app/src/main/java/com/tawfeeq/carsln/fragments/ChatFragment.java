package com.tawfeeq.carsln.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.adapters.MessagesAdapter;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.objects.MessageID;
import com.tawfeeq.carsln.objects.MessageModel;
import com.tawfeeq.carsln.objects.Users;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    FireBaseServices fbs;
    Users user;
    TextView username;
    ImageView pfp, back;
    FirebaseDatabase db;
    CardView btnSend;
    EditText txtMessage;
    ImageView call;

    String senderRoom, receiverRoom;
    RecyclerView rc;
    ArrayList<MessageID> messagesArrayList;
    MessagesAdapter messagesAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        db = FirebaseDatabase.getInstance();
        username = getView().findViewById(R.id.tvUsernameMessaging);
        pfp = getView().findViewById(R.id.ivUserChatPFP);
        call = getView().findViewById(R.id.ivCallMessaging);

        if(fbs.getSelectedUser()!=null) user = fbs.getSelectedUser();

        if(fbs.getSelectedUser()!=null) {
            username.setText(user.getUsername());
            if (user.getUserPhoto() == null || user.getUserPhoto().isEmpty())
            {
                pfp.setImageResource(R.drawable.slnpfp);
            }
            else {
                Glide.with(getActivity()).load(user.getUserPhoto()).into(pfp);
            }
        }

        messagesArrayList = new ArrayList<>();
        btnSend = getView().findViewById(R.id.sendbtn);
        txtMessage = getView().findViewById(R.id.etMsg);
        rc = getView().findViewById(R.id.RecyclerMessages);
        back = getView().findViewById(R.id.MessageChatBack);


        if(!fbs.getCurrentFragment().equals("Chat")) fbs.setCurrentFragment("Chat");


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((MainActivity) getActivity()).getBottomNavigationView().getSelectedItemId()==R.id.profile){
                    GoToChats();
                }else {
                    GoToDetailed();
                }

            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user!=null) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + user.getPhone()));
                    startActivity(intent);

                }
            }
        });


        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.GONE);


        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String userID = str.substring(0, n);


        if(user!=null) {

            senderRoom = userID + user.getDocID();
            receiverRoom = user.getDocID() + userID;


            // Set the RecyclerView!!
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setStackFromEnd(true);
            rc.setLayoutManager(linearLayoutManager);
            messagesAdapter = new MessagesAdapter(getActivity(), messagesArrayList);
            rc.setAdapter(messagesAdapter);


            DatabaseReference chatreference = db.getReference().child("chats").child(senderRoom).child("messages");


            // Refresh Chat!!
            chatreference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    messagesArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        MessageID messages = dataSnapshot.getValue(MessageID.class);
                        messages.setId(dataSnapshot.getKey());
                        messagesArrayList.add(messages);

                    }
                    messagesAdapter = new MessagesAdapter(getActivity(), messagesArrayList);
                    rc.setAdapter(messagesAdapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            // send new Message!!
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message = txtMessage.getText().toString();
                    if (message.isEmpty()) {
                        Toast.makeText(getActivity(), "Enter a Message First", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    txtMessage.setText("");
                    Date date = new Date();
                    MessageModel messagess = new MessageModel(message, userID, date.getTime());

                    db = FirebaseDatabase.getInstance();
                    db.getReference().child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    db.getReference().child("chats")
                                            .child(receiverRoom)
                                            .child("messages")
                                            .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            });
                }
            });
        }

    }

    private void GoToDetailed() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new DetailedFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();

        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }

    private void GoToChats() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new UserChatsFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();

        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }

}