package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import com.google.android.material.button.MaterialButton;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Error Message
        TextView errorMessage = (TextView) findViewById(R.id.errorMessage);

        DatabaseReference databaseAccounts;
        DatabaseReference cu;
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

        // Buttons
        MaterialButton signUpButton = (MaterialButton) findViewById(R.id.signUpButton);
        Button uploadVoidChequeButton = (Button) findViewById(R.id.uploadVoidChequeButton);
        MaterialButton goToSignInButton = (MaterialButton) findViewById(R.id.goToSignInButton);

        // Groups
        EditText[] commonFields = {firstName, lastName, address, email, password, passwordConfirm};
        EditText[] cookFields = {firstName, lastName, address, email, password, passwordConfirm, description};
        EditText[] clientFields = {firstName, lastName, address, email, password, passwordConfirm, creditCardNumber,creditCardExpirationDate,creditCardCVV};

        System.out.println("New Activity: SignUp");

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
                    clearErrors(clientFields);
                }
                else {
                    System.out.println("Switching to cook mode");
                    creditCardNumber.setVisibility(View.GONE);
                    creditCardInfoLinearLayout.setVisibility(View.GONE);

                    description.setVisibility(View.VISIBLE);
                    uploadVoidChequeButton.setVisibility(View.VISIBLE);
                    clearErrors(cookFields);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

            private void clearErrors(EditText[] texts) {
                for (EditText text : texts) {
                    text.setError(null);
                }
            }
        });

        DatabaseReference clientDB = databaseAccounts.child("Clients");
        DatabaseReference cookDB = databaseAccounts.child("Cooks");
        cu = FirebaseDatabase.getInstance().getReference("Accounts");

        // Sign up Button Listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
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

                // Intent Action Sign In
                final String ACTION_SIGN_IN = "com.example.action.LOG_IN";

                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String eMail = email.getText().toString();
                String pWord = password.getText().toString();
                Address add = new Address(address.getText().toString(),"K1N 2S6","Canada","ON","Ottawa");

                if (userType.getSelectedItemPosition() == 0 && !areEmpty(clientFields) && isMatching()) {
                    Log.i("CLIENT","onClick Client");
                    String ccNumber = creditCardNumber.getText().toString();
                    String cvv = creditCardCVV.getText().toString();
                    String exp = creditCardExpirationDate.getText().toString();
                    String cardName = fName + " " + lName;

                    CreditCard card = new CreditCard(cardName, ccNumber, cvv, exp);
                    Client client = new Client(fName, lName, eMail, pWord, add, card);
                    String temp = client.getEmail().replace(".", "");
                    clientDB.child(temp).setValue(client);
                    Toast.makeText(SignUp.this,"Created Account", Toast.LENGTH_SHORT).show();

                    // After signing up, automatically signs in user
                    Bundle info = new Bundle();
                    info.putString("email", eMail);
                    info.putString("accountType", "Client");
                    signIn(info, Home.class);

                } else if (userType.getSelectedItemPosition() == 1 && !areEmpty(cookFields) && isMatching()){
                    Log.i("COOK","onClick Cook");
                    String desc = description.getText().toString();

                    Cook cook = new Cook(fName, lName, eMail, pWord, add, desc);
                    String temp = cook.getEmail().replace(".", "");
                    cookDB.child(temp).setValue(cook);
                    Toast.makeText(SignUp.this,"Created Account", Toast.LENGTH_SHORT).show();

                    // After signing up, automatically signs in user
                    Bundle info = new Bundle();
                    info.putString("email", eMail);
                    info.putString("accountType", "Cook");
                    signIn(info, Home.class);
                } else {
                    Toast.makeText(SignUp.this,"Some fields are empty.", Toast.LENGTH_SHORT).show();
                };
            }
        });

        // Upload Cheque Button Listener
        uploadVoidChequeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, VoidCheque.class));
            }
        });

        // Sign In Button Listener
        // Ends current Sign Up activity and sends user back to Sign In
        goToSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp.this.finish();
            }
        });

    }

    private void signIn(Bundle info, Class destination){
        // Sign In Intent action
        final String ACTION_SIGN_IN = "com.example.action.LOG_IN";

        // Passing client info to Home activity on activity start
        Intent signIn = new Intent(ACTION_SIGN_IN);
        signIn.setClass(SignUp.this, destination);
        signIn.putExtras(info);
        startActivity(signIn);

        // End this activity
        SignUp.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Ended Activity: SignUp");
    }

    private void goHome(View v) {
        startActivity(new Intent(SignUp.this,Home.class));
    }

}