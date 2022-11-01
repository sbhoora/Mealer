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

import java.util.HashMap;

public class SignIn extends AppCompatActivity {

    DatabaseReference reference1;
    DatabaseReference reference2;
    DatabaseReference cu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cu = FirebaseDatabase.getInstance().getReference("Accounts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        TextView email = (TextView) findViewById(R.id.email);
        TextView password = (TextView) findViewById(R.id.password);

        MaterialButton signInButton = (MaterialButton) findViewById(R.id.signinbutton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String a = email.getText().toString().substring(email.getText().toString().length()-4);
                if (email.getText().toString().equals(("admin")) && password.getText().toString().equals("admin")) {
                    // correct
                    Toast.makeText(SignIn.this,"Login Successful", Toast.LENGTH_SHORT).show();
                    cu.child("CurrentUser").setValue("Admin");
                    goHome(v);
                    return;
                } else {
                    if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                        isItUser(email.getText().toString().replace(".",""),password.getText().toString(), v);
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
                }
            }
        });
    }

    private void isItUser(String email, String pw, View v){
        reference1 = FirebaseDatabase.getInstance().getReference("Accounts");
        reference2 = FirebaseDatabase.getInstance().getReference("Accounts");
        cu = FirebaseDatabase.getInstance().getReference("Accounts");

        reference1.child("Clients").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    //Toast.makeText(MainActivity.this,"SUCCESS", Toast.LENGTH_SHORT).show();
                    DataSnapshot dataSnapshot = task.getResult();
                    String password = String.valueOf(dataSnapshot.child("password").getValue());
                    Log.d("FIREBASE", password);

                    if(!pw.equals(password)){
                        Toast.makeText(SignIn.this,"Wrong password.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignIn.this,"Login Successful", Toast.LENGTH_SHORT).show();
                        HashMap<String, String> currentUser = new HashMap<String, String>();
                        currentUser.put(email,"Client");
                        cu.child("CurrentUser").setValue(currentUser);
                        goHome(v);
                    }

                } else {
                    Log.i("FIREBASE", "FAIL0");
                    reference2.child("Cooks").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.getResult().exists()){
                                //Toast.makeText(MainActivity.this,"SUCCESS", Toast.LENGTH_SHORT).show();
                                DataSnapshot dataSnapshot = task.getResult();
                                String password = String.valueOf(dataSnapshot.child("password").getValue());
                                Log.d("FIREBASE", password);

                                if(!pw.equals(password)){
                                    Toast.makeText(SignIn.this,"Wrong password.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignIn.this,"Login Successful", Toast.LENGTH_SHORT).show();
                                    HashMap<String, String> currentUser = new HashMap<String, String>();
                                    currentUser.put(email,"Cook");
                                    cu.child("CurrentUser").setValue(currentUser);
                                    goHome(v);
                                }

                            } else {
                                Log.i("FIREBASE", "FAIL1");
                                Toast.makeText(SignIn.this,"This user doesn't exist.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void goToSignUp(View v) {
        startActivity(new Intent(SignIn.this,SignUp.class));
    }

    private void goHome(View v) {
        startActivity(new Intent(SignIn.this,Home.class));
    }
}