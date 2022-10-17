package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                Toast.makeText(SignUp.this,"Created Account", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void goHome(View v) {
        startActivity(new Intent(SignUp.this,MainActivity.class));
    }
}