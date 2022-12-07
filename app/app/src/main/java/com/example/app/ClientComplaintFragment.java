package com.example.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientComplaintFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientComplaintFragment extends Fragment {

    View view;

    // Firebase database reference
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    // the fragment initialization parameter
    private static final String EMAIL = "email";
    // the string instance variable storing the email
    private String email;

    public ClientComplaintFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email Cook Email.
     * @return A new instance of fragment ClientComplaintFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientComplaintFragment newInstance(String email) {
        ClientComplaintFragment fragment = new ClientComplaintFragment();
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
        view = inflater.inflate(R.layout.fragment_client_complaint, container, false);

        // Distinguishing emails
        String cookEmailFromActivity = email;

        // TextView from XML
        TextView cookEmail = (TextView) view.findViewById(R.id.clientComplaintCookEmail);
        EditText subject = (EditText) view.findViewById(R.id.clientComplaintSubject);
        EditText description = (EditText) view.findViewById(R.id.clientComplaintDescriptionTextView);

        // Buttons from XML
        MaterialButton submitButton = (MaterialButton) view.findViewById(R.id.clientComplaintSubmitComplaintButton);
        MaterialButton cancelButton = (MaterialButton) view.findViewById(R.id.clientComplaintCancelComplaintButton);

        // Setting cook email
        cookEmail.setText(email);

        // TextViews as strings
        String subjectAsString = subject.getText().toString();
        String descriptionAsString = description.getText().toString();

        // Checking that the user has provided all information and nothing is blank
        if (!subjectAsString.isEmpty() && !descriptionAsString.isEmpty()) {
            // Listening for client to click on submit button
            submitButton.setOnClickListener(new View.OnClickListener() {
                Complaint complaint = new Complaint(subjectAsString, cookEmailFromActivity, descriptionAsString);
                @Override
                public void onClick(View v) {
                    database.getReference("Accounts").child("admin").child("Complaints").setValue(complaint)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.i("Firebase", "Complaint saved successfully against cook: " + cookEmailFromActivity);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Firebase", "Failed to save complaint against cook:" + cookEmailFromActivity);
                                }
                            });
                }
            });
        } else {
            // Put red flags
            subject.setError("Please fill in this field.");
            description.setError("Please fill in this field.");
            // Sending out toast message notifying client to fill in fields
            Toast.makeText(getContext(),"Please fill in all the fields.", Toast.LENGTH_SHORT).show();
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

}