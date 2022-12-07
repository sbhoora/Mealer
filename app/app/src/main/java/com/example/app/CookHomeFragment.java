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
    ListView requestListView;
    ArrayAdapter<String> arrayAdapter;

    private DatabaseReference database;
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
        update();

        return view;
    }

    private void update(){
        CookHomeFragment cntx = this;
        database = FirebaseDatabase.getInstance().getReference("Cooks");

        database.child(cook.getEmail()).child("Requests").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                String requestEmail[] = new String[Math.toIntExact(dataSnapshot.getChildrenCount())];
                String requestMeal[] = new String[Math.toIntExact(dataSnapshot.getChildrenCount())];
                Log.i("SIZE",Integer.toString(requestEmail.length));

                Map<String,Object> reqMap =  (Map<String,Object>) dataSnapshot.getValue();
                if(reqMap!=null) {
                    int i = 0;
                    //creating a list of complaints that exist on the DB
                    for (Map.Entry<String, Object> entry : reqMap.entrySet()) {
                        Map req = (Map) entry.getValue();
                        requestEmail[i] = entry.getKey();
                        requestMeal[i] = req.get("Meal").toString();
                        i++;
                    }
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(cntx, R.layout.request_list_item, R.id.textView, requestEmail);
                requestListView.setAdapter(arrayAdapter);
                //CREATE LISTVIEW FOR REQUESTS

                requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String title, message, cookEmail;
                        title = requestEmail[i] + ": " +requestMeal[i];

                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertDialogTheme));

                        builder.setTitle(title);

                        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Add request to "accepted section"
                                //Change status of meal to "accepted" for cook purchase history
                                //Dismiss request from "pending requests" section
                            }
                        });

                        builder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Delete Request
                                //Change status of meal to "rejected" for cook purchase history
                                //Dismiss request from "pending requests" section
                            }
                        });

                    }
                });

            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Fragment", getClass().getName() + " Destroyed");
    }
}