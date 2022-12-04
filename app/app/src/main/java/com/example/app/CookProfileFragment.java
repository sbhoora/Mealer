package com.example.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class CookProfileFragment extends Fragment {

    View view;

    // Firebase database reference
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    // the fragment initialization parameter
    private static final String EMAIL = "email";
    // the string instance variable storing the email
    private String email;

    public CookProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email Parameter 1.
     * @return A new instance of fragment CookProfileFragment.
     */
    public static CookProfileFragment newInstance(String email) {
        CookProfileFragment fragment = new CookProfileFragment();
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
        view = inflater.inflate(R.layout.fragment_cook_profile, container, false);

        // TextViews from XML file
        TextView cookProfileName = (TextView) view.findViewById(R.id.cookProfileName);
        TextView cookProfileEmail = (TextView) view.findViewById(R.id.cookProfileEmail);
        TextView cookProfileAddress = (TextView) view.findViewById(R.id.cookProfileAddress);
        TextView cookProfileDescription = (TextView) view.findViewById(R.id.cookProfileDescription);

        // Updating cook information on xml from database
        database.getReference("Accounts").child("Cooks").child(email).child("AccountInfo").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    Log.i("Firebase", "Cook Retrieval Successful for cook: " + email);
                    Map<String, String> accountInfo = (Map<String, String>) snapshot.getValue();
                    // Setting cook name
                    cookProfileName.setText(accountInfo.get("firstName") + " " + accountInfo.get("lastName"));
                    //Setting cook email
                    cookProfileEmail.setText(accountInfo.get("email"));
                    // Setting cook address
                    cookProfileAddress.setText(snapshot.child("address").getValue(Address.class).toString());
                    // Setting cook description
                    cookProfileDescription.setText(accountInfo.get("description"));
                } else {
                    Log.e("Firebase", "Cook Retrieval Failed for cook:" + email);
                }
            }
        });

        // Logout Button
        ImageButton logoutButton = view.findViewById(R.id.cookLogoutButton);

        // Logout functionality on button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });






        return view;
    }
}