package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {
    DatabaseReference ref;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Text Views
        TextView welcome = (TextView) findViewById(R.id.welcomeMessage);

        // Console message
        System.out.println("New Activity: Home");

        // Buttons
        MaterialButton signOutButton = (MaterialButton) findViewById(R.id.signOutButton);

        // Getting account info passed from Sign In activity
        // Uses info to present proper account type on welcome
        Intent fromSignIn = getIntent();
        String type = fromSignIn.getStringExtra("accountType");
        // if condition seems redundant right now
        // but this is only until other accounts values are passed as well
        if (type != null && type.equals("Administrator")) {
            // ideally this is the only line necessary
            // whatever account type is passed will be added to the text view
            welcome.setText("Welcome! You are signed in as " + type + ".");
        } else {
            // temporary until can be implemented for all account types
            userType(welcome);
        }


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Ended Activity: Home");
    }

    private void userType(TextView tv){
        ref = FirebaseDatabase.getInstance().getReference("Accounts");
        userRef = FirebaseDatabase.getInstance().getReference("Accounts");
        ref.child("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String temp = "Welcome! You are signed in as ";
                DataSnapshot dataSnapshot = task.getResult();
                String currentUser = String.valueOf(dataSnapshot.getValue());
                Log.i("CU",currentUser);
                if(currentUser.contains("Client")){
                    tv.setText(temp + "Client.");
                } else if(currentUser.contains("Cook")){
                    tv.setText(temp + "Cook.");
                } else {
                    tv.setText(temp + "Administrator.");
                }
            }
        });
    }

}