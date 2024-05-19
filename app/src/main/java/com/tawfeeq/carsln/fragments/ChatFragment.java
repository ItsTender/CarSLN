package com.tawfeeq.carsln.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.adapters.MessagesAdapter;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.objects.MessageModel;
import com.tawfeeq.carsln.objects.Users;

import java.util.ArrayList;

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
    //FirebaseDatabase db;
    CardView btnSend;
    EditText txtMessage;

    String senderRoom, receiverRoom;
    RecyclerView rc;
    ArrayList<MessageModel> messagesArrayList;
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
        user = fbs.getSelectedUser();
        //db = FirebaseDatabase.getInstance();
        username = getView().findViewById(R.id.tvUsernameMessaging); username.setText(user.getUsername());
        messagesArrayList = new ArrayList<>();
        btnSend = getView().findViewById(R.id.sendbtn);
        txtMessage = getView().findViewById(R.id.etMsg);
        rc = getView().findViewById(R.id.RecyclerMessages);
        back = getView().findViewById(R.id.MessageChatBack);


        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.GONE);


        pfp = getView().findViewById(R.id.ivUserChatPFP);
        if (user.getUserPhoto() == null || user.getUserPhoto().isEmpty())
        {
            pfp.setImageResource(R.drawable.slnpfp);
        }
        else {
            Glide.with(getActivity()).load(user.getUserPhoto()).into(pfp);
        }



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        rc.setLayoutManager(linearLayoutManager);
        //messagesAdapter = new MessagesAdapter(getActivity(),messagesArrayList);
        //rc.setAdapter(messagesAdapter);


        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String userID = str.substring(0, n);


        senderRoom = userID+user.getDocID();
        receiverRoom = user.getDocID()+userID;








    }

}