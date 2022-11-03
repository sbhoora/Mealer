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
        if (type != null) {
            // whatever account type is passed will be added to the text view
            welcome.setText("Welcome! You are signed in as " + type + ".");
        }

        // To get signed in user email
        String email = fromSignIn.getStringExtra("email");

        // i.e.
            // retrieve account info from db with email as key
            // display user email on screen
            // do what you want :)

        // Essentially, to pass any info from SignIn, where user info is retrieved from db
        // on SignIn do
            // Bundle b = new Bundle() /// Essentially a Java Map but preferred for Android Studio
            // b.putString("giveitaname", value) //// value can be any primitive data type
            // yourIntent.putExtras(b)
            // ....
            // startActivity(yourIntent)
        // on Home do
            // retrievedValue = intent.getStringExtra("nameyougaveit")
        // tada!

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
}