package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientHome extends AppCompatActivity {

    private Client client;
    private ListView availableMealList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        // Console message
        System.out.println("New Activity: Home");

        // Buttons
        MaterialButton signOutButton = (MaterialButton) findViewById(R.id.signOutButton);

        // Getting account info passed from Sign In activity
        // Uses info to present proper account type on welcome
        Intent fromSignIn = getIntent();

        // To get signed in user email
        String email = fromSignIn.getStringExtra("email");

        // Buttons

        // Value retrieved from SignIn for the cook that just signed in
        email = getIntent().getStringExtra("email");

        // client = new Client(); // NEEDS EMAIL PASSED WHEN CLIENT WILL BE CODED

        availableMealList = (ListView) findViewById(R.id.availableMealListView);

        // update();

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientHome.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Ended Activity: Home");
    }
}