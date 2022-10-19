package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.material.button.MaterialButton;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference databaseAccounts;
        databaseAccounts = FirebaseDatabase.getInstance("https://mealer-2f04c-default-rtdb.firebaseio.com/").getReference().child("Accounts");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // User Type
        Spinner userType = (Spinner) findViewById(R.id.userType);

        // Information
        EditText firstName = (EditText) findViewById(R.id.firstName);
        EditText lastName = (EditText) findViewById(R.id.lastName);
        EditText address = (EditText) findViewById(R.id.address);
        EditText description = (EditText) findViewById(R.id.description);

        // Login
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        EditText passwordConfirm = (EditText) findViewById(R.id.passwordConfirm);

        // Payment
        EditText creditCardNumber = (EditText) findViewById(R.id.creditCardNumber);
        LinearLayout creditCardInfoLinearLayout = (LinearLayout) findViewById(R.id.creditCardInfoLinearLayout);
        EditText creditCardExpirationDate = (EditText) findViewById(R.id.creditCardExpirationDate);
        EditText creditCardCVV = (EditText) findViewById(R.id.creditCardCVV);
        Button uploadVoidChequeButton = (Button) findViewById(R.id.uploadVoidChequeButton);

        // Sign Up
        Button signUpButton = (Button) findViewById(R.id.signUpButton);

        userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                System.out.println(position);
                if (position == 0) {
                    System.out.println("Switching to client mode");
                    description.setVisibility(View.GONE);
                    uploadVoidChequeButton.setVisibility(View.GONE);

                    creditCardNumber.setVisibility(View.VISIBLE);
                    creditCardInfoLinearLayout.setVisibility(View.VISIBLE);
                }
                else {
                    System.out.println("Switching to cook mode");
                    creditCardNumber.setVisibility(View.GONE);
                    creditCardInfoLinearLayout.setVisibility(View.GONE);

                    description.setVisibility(View.VISIBLE);
                    uploadVoidChequeButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        MaterialButton signInButton = (MaterialButton) findViewById(R.id.signUpButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if all fields are filled in (NOT DONE)

                if(userType.getSelectedItem().toString().equals("Client")){
                    System.out.println("onClick Client");
                    Client client = new Client(firstName.getText().toString(),lastName.getText().toString(),email.getText().toString(),
                            password.getText().toString(),address.getText().toString(),123);
                    databaseAccounts.child("Clients").setValue(client);
                } else {
                    System.out.println("onClick Cook");
                    Cook cook = new Cook(firstName.getText().toString(),lastName.getText().toString(),email.getText().toString(),
                            password.getText().toString(),description.getText().toString());
                    databaseAccounts.child("Cooks").setValue(cook);
                }

                /*
                if(userType.getSelectedItem().toString().equals("Client")){
                    Client client = new Client(firstName.getText().toString(),lastName.getText().toString(),email.getText().toString(),
                            password.getText().toString(),address.getText().toString(),123);
                    Map<String, Client> users = new HashMap<>();
                    users.put(email.toString(),client);
                    //Client a = users.get(email.toString());
                    //String b = a.getFirstName();
                    //Log.i("OUTPUT!!",b);
                } else {
                    Cook cook = new Cook(firstName.getText().toString(),lastName.getText().toString(),email.getText().toString(),
                            password.getText().toString(),description.getText().toString());
                    Map<String, Cook> users = new HashMap<>();
                    users.put(email.toString(),cook);
                }
                 */

                Toast.makeText(SignUp.this,"Created Account", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void goHome(View v) {
        startActivity(new Intent(SignUp.this,MainActivity.class));
    }
}