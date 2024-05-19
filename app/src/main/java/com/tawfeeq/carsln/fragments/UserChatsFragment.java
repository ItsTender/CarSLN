package com.tawfeeq.carsln.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.adapters.CarsAdapter;
import com.tawfeeq.carsln.adapters.UsersAdapter;
import com.tawfeeq.carsln.objects.CarID;
import com.tawfeeq.carsln.objects.FireBaseServices;
import com.tawfeeq.carsln.objects.Users;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserChatsFragment extends Fragment {

    FireBaseServices fbs;
    ImageView Back;
    ArrayList<Users> users;
    RecyclerView rc;
    UsersAdapter Adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserChatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserChatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserChatsFragment newInstance(String param1, String param2) {
        UserChatsFragment fragment = new UserChatsFragment();
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
        return inflater.inflate(R.layout.fragment_user_chats, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        users = new ArrayList<>();
        Back = getView().findViewById(R.id.ChatsGoBack);
        rc = getView().findViewById(R.id.RecyclerUsers);


        if(!fbs.getCurrentFragment().equals("UserChats")) fbs.setCurrentFragment("UserChats");


        String str = fbs.getAuth().getCurrentUser().getEmail();
        int n = str.indexOf("@");
        String userID = str.substring(0, n);


        if(fbs.getUsers()==null){

            fbs.getStore().collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot dataSnapshot : queryDocumentSnapshots.getDocuments()) {

                        Users user = dataSnapshot.toObject(Users.class);
                        user.setDocID(dataSnapshot.getId());


                        if(!dataSnapshot.getId().equals(userID)) users.add(user);

                    }
                    fbs.setUsers(users);
                    SettingChats();

                }
            });

        }else {

            users = fbs.getUsers();
            SettingChats();
        }

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToProfile();
            }
        });

    }

    private void GoToProfile() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FrameLayoutMain, new ProfileFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }

    private void SettingChats() {

        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        Adapter = new UsersAdapter(getActivity(),users);
        rc.setAdapter(Adapter);

    }

}