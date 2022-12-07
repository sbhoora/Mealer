package com.example.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookHomeFragment extends Fragment {
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

    public CookHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email Parameter 1.
     * @return A new instance of fragment CookProfileFragment.
     */
    public static CookHomeFragment newInstance(String email) {
        CookHomeFragment fragment = new CookHomeFragment();
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
        view = inflater.inflate(R.layout.fragment_cook_home, container, false);

        // ListView from XML
        listView = view.findViewById(R.id.clientSearchListView);

        //email IS NULL CHANGE THAT
        database.getReference("Accounts").child("Cooks").child("qw").child("Requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Firebase", "Requests Retrieval Successful");

                int i = 0;

                ArrayList<String> reqEmail = new ArrayList<String>();
                ArrayList<String []> meal = new ArrayList<String []>();
                ArrayList<String> title = new ArrayList<String>();

                // Getting all requests from database
                for (DataSnapshot ds : snapshot.getChildren()) {
                    reqEmail.add(ds.getKey());
                    meal.add(ds.getValue().toString().replace("[","").replace("]","").split(","));
                    for(String m : meal.get(i)){
                        m = m.replace("}","").replace("{","").replace("=","");
                        title.add(ds.getKey() + ": " + m);
                    }
                    i++;
                }

                // Making arrayAdapter from email array
                arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, title);

                // Setting listView to take values from arrayAdapter
                listView.setAdapter(arrayAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String clientEmail = title.get(position).split(":")[0];
                        String mealName = title.get(position).split(":")[1];
                        DatabaseReference history = database.getReference("Accounts").child("Clients").child(clientEmail).child("History").child("qw").child(mealName);
                        DatabaseReference request = database.getReference("Accounts").child("Cooks").child("qw").child("Requests").child(clientEmail).child(mealName);

                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertDialogTheme));
                        builder.setTitle(title.get(position));

                        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                history.setValue("Accepted");
                                request.removeValue();
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                history.setValue("Rejected");
                                request.removeValue();
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Meals Retrieval Failed");
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
