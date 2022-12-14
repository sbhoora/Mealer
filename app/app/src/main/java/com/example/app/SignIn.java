package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {

    private DatabaseReference reference;
    private DatabaseReference cu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cu = FirebaseDatabase.getInstance().getReference("Accounts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        System.out.println("New Activity: SignIn");

        // Text Views
        TextView email = (TextView) findViewById(R.id.email);
        TextView password = (TextView) findViewById(R.id.password);

        // Buttons
        MaterialButton signInButton = (MaterialButton) findViewById(R.id.signinbutton);
        MaterialButton goToSignUpButton = (MaterialButton) findViewById(R.id.goToSignUpButton);

        // Authenticates user
        // If input info is valid, the home activity is started and the user is sent there.
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                        isItUser(email.getText().toString().replace(".",""),password.getText().toString());
                        Log.i("onClick", "pass");
                    } else if (email.getText().toString().isEmpty() && password.getText().toString().isEmpty()){
                        email.setError("Please fill in this field.");
                        password.setError("Please fill in this field.");
                        Toast.makeText(SignIn.this,"Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                    } else if (email.getText().toString().isEmpty()){
                        email.setError("Please fill in this field.");
                        Toast.makeText(SignIn.this,"Please enter your email.", Toast.LENGTH_SHORT).show();
                    } else if (password.getText().toString().isEmpty()){
                        password.setError("Please fill in this field.");
                        Toast.makeText(SignIn.this,"Please enter your password.", Toast.LENGTH_SHORT).show();
                    }
                    email.setText("");
                    password.setText("");
                }

        });

        // Listens for sign up button click
        // If clicked, sends user to the sign up activity.
        goToSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this,SignUp.class));
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("Resumed Activity: SignIn");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("Paused Activity: SignIn");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Ended Activity: SignIn");
    }

    private void signIn(Bundle info, Class destination){
        // Sign In Intent action
        final String ACTION_SIGN_IN = "com.example.action.LOG_IN";

        // Passing client info to Home activity on activity start
        Intent signIn = new Intent(ACTION_SIGN_IN);
        signIn.setClass(SignIn.this, destination);
        signIn.putExtras(info);
        startActivity(signIn);

    }

    private void isItUser(String email, String pw){
        reference = FirebaseDatabase.getInstance().getReference("Accounts"); //ref for clients

        Log.i("PASS","Complete Listener");
        reference.child("admin").child("password").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                String password = String.valueOf(dataSnapshot.getValue());
                if(email.equals("admin")){
                    Log.i("FIREBASE", "ADMIN");
                    if(password.equals(pw)){
                        Toast.makeText(SignIn.this,"Login Successful", Toast.LENGTH_SHORT).show();

                        Bundle info = new Bundle();
                        info.putString("accountType", "Administrator");
                        signIn(info, AdminHome.class);
                    } else {
                        Toast.makeText(SignIn.this,"Wrong password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    reference.child("Clients").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.getResult().exists()){
                                //Toast.makeText(MainActivity.this,"SUCCESS", Toast.LENGTH_SHORT).show();
                                DataSnapshot dataSnapshot = task.getResult();
                                String password = String.valueOf(dataSnapshot.child("AccountInfo").child("password").getValue());
                                Log.d("FIREBASE", password);

                                if(!pw.equals(password)){
                                    Toast.makeText(SignIn.this,"Wrong password", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignIn.this,"Login Successful", Toast.LENGTH_SHORT).show();

                                    // Passing client info to Home activity on activity start
                                    Bundle info = new Bundle();
                                    info.putString("email", email);
                                    info.putString("accountType", "Client");
                                    signIn(info, NewClientHome.class);
                                }

                            } else {
                                Log.i("FIREBASE", "FAIL0");
                                reference.child("Cooks").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(task.getResult().exists()){
                                            Log.i("FIREBASE", "PASSSSSSS");
                                            DataSnapshot dataSnapshot = task.getResult();
                                            String password = String.valueOf(dataSnapshot.child("AccountInfo").child("password").getValue());
                                            Boolean suspended = (Boolean) dataSnapshot.child("AccountInfo").child("suspended").getValue();
                                            Boolean banned = (Boolean) dataSnapshot.child("AccountInfo").child("banned").getValue();
                                            Log.d("FIREBASE", password);

                                            if(!pw.equals(password)){
                                                Toast.makeText(SignIn.this,"Wrong password", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Cook cook = new Cook(email, pw, suspended, banned);
                                                if (cook.isSuspended()) {
                                                    String suspendedUntil = (String) dataSnapshot.child("AccountInfo").child("suspendedUntil").getValue();
                                                    suspendedUntil = new String(new char[] {suspendedUntil.charAt(0), suspendedUntil.charAt(1), '/',suspendedUntil.charAt(2), suspendedUntil.charAt(3), '/',suspendedUntil.charAt(4), suspendedUntil.charAt(5),suspendedUntil.charAt(6), suspendedUntil.charAt(7)});
                                                    Toast.makeText(SignIn.this,String.format("This account is currently suspended until %s.", suspendedUntil), Toast.LENGTH_SHORT).show();
                                                } else if (cook.isBanned()){
                                                    Toast.makeText(SignIn.this,"Permanent suspension, therefore you can no longer use" +
                                                            " this application", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(SignIn.this,"Login Successful", Toast.LENGTH_SHORT).show();

                                                    // Passing cook info to Home activity on activity start
                                                    Bundle info = new Bundle();
                                                    info.putString("email", cook.getEmail());
                                                    info.putString("accountType", "Cook");
                                                    signIn(info, NewCookHome.class);
                                                }

                                            }

                                        } else {
                                            Log.i("FIREBASE", "FAIL1");
                                            Toast.makeText(SignIn.this,"This user doesn't exist", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            };
        });
    };
}