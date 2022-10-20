package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

import org.w3c.dom.Text;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Error Message
        TextView errorMessage = (TextView) findViewById(R.id.errorMessage);

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

        // Groups
        EditText[] commonFields = {firstName, lastName, address, email, password, passwordConfirm};
        EditText[] cookFields = {description};
        EditText[] clientFields = {creditCardNumber,creditCardExpirationDate,creditCardCVV};

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
        DatabaseReference clientDB = databaseAccounts.child("Clients");
        DatabaseReference cookDB = databaseAccounts.child("Cooks");

        signInButton.setOnClickListener(new View.OnClickListener() {
            public void goHome(View v) {
                startActivity(new Intent(SignUp.this,MainActivity.class));
            }

            private boolean areEmpty(EditText[] texts){
                boolean atLeastOneEmpty = false;
                for (EditText text : texts) {
                    if (TextUtils.isEmpty(text.getText().toString())) {
                        errorMsg(text);
                        atLeastOneEmpty = true;
                    }
                }
                return atLeastOneEmpty;
            }

            private boolean isMatching(){
                boolean match = false;
                if (password.getText().toString().equals(passwordConfirm.getText().toString())){
                    match = true;
                } else {
                    passwordConfirm.setError("Passwords don't match!");
                }
                return match;
            }

            private void errorMsg(EditText v){
                v.setError("Please fill in this field.");
            }

            private boolean isEmpty(TextView view) {return view.getText().toString().isEmpty();}
            @Override
            public void onClick(View v) {

                if (!areEmpty(commonFields) && isMatching()) {
                    Address add = new Address(address.getText().toString(),"K1N 2S6","Canada","ON","Ottawa");
                    if(userType.getSelectedItemPosition() == 0){
                        Log.i("CLIENT","onClick Client");
                        areEmpty(cookFields);
                        //SEE IF ALL FIELDS ARE FILLED CORRECTLY FIRST
                        CreditCard card = new CreditCard(firstName.getText().toString(),creditCardNumber.getText().toString(),
                                creditCardCVV.getText().toString(),creditCardExpirationDate.getText().toString());

                        Client client = new Client(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(),
                                password.getText().toString(), add, card);

                        clientDB.child(client.getEmail().substring(0,client.getEmail().length()-4)).setValue(client);
                    } else {
                        Log.i("COOK","onClick Cook");
                        areEmpty(cookFields);
                        //SEE IF ALL FIELDS ARE FILLED CORRECTLY FIRST
                        Cook cook = new Cook(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(),
                                password.getText().toString(), add, description.getText().toString());

                        cookDB.child(cook.getEmail().substring(0,cook.getEmail().length()-4)).setValue(cook);
                    }
                    //SEE IF ALL FIELDS ARE FILLED CORRECTLY FIRST
                    Toast.makeText(SignUp.this,"Created Account", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUp.this,"Some fields are empty.", Toast.LENGTH_SHORT).show();
                };
            }
        });
    }


}