package com.example.app;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientHomeFragment extends Fragment {
    //purchase requests

    View view;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;

    //private DatabaseReference database;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private Cook cook;

    // the fragment initialization parameter
    private static final String EMAIL = "email";
    // the string instance variable storing the email
    private String email;

    public ClientHomeFragment() {
        // Required empty public constructor
    }

    public static ClientHomeFragment newInstance(String email) {
        ClientHomeFragment fragment = new ClientHomeFragment();
        Bundle args = new Bundle();
        args.putString(EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_client_home, container, false);
        listView = view.findViewById(R.id.clientHomeListView);

        Context cntx = container.getContext();

        database.getReference("Accounts").child("Clients").child("a").child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;

                ArrayList<String []> order = new ArrayList<String []>();
                ArrayList<String> cookEmail = new ArrayList<String>();
                ArrayList<String> title = new ArrayList<String>();

                // Getting all requests from database
                for (DataSnapshot ds : snapshot.getChildren()) {
                    cookEmail.add(ds.getKey());
                    order.add(ds.getValue().toString().replace("[","").replace("]","").split(","));
                    for(String m : order.get(i)){
                        m = m.replace("}","").replace("{","").replace("=","");
                        if(m.contains("Pending")){
                            title.add(m.replace("Pending","")+" ("+ds.getKey()+"): "+ "Pending");
                        }else if(m.contains("Accepted")){
                            title.add(m.replace("Accepted","")+" ("+ds.getKey()+"): "+ "Accepted");
                        }else if(m.contains("Rejected")){
                            title.add(m.replace("Rejected","")+" ("+ds.getKey()+"): "+ "Rejected");
                        }

                        Log.i("STATUS",ds.getKey() + ": " + m);
                    }
                    i++;
                }

                arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, title);

                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Fragment", getClass().getName() + " Destroyed");
    }
}