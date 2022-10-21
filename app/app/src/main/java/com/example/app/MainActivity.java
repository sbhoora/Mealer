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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    DatabaseReference reference1;
    DatabaseReference reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView email = (TextView) findViewById(R.id.email);
        TextView password = (TextView) findViewById(R.id.password);

        MaterialButton signInButton = (MaterialButton) findViewById(R.id.signinbutton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String a = email.getText().toString().substring(email.getText().toString().length()-4);
                if (email.getText().toString().equals(("admin")) && password.getText().toString().equals("admin")) {
                    // correct
                    Toast.makeText(MainActivity.this,"Login Successful", Toast.LENGTH_SHORT).show();
                    goHome(v);
                }
                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                    isItUser(email.getText().toString().replace(".",""),password.getText().toString(), v);
                    Log.i("onClick", "pass");
                } else if (email.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter your email.", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter your password.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void isItUser(String email, String pw, View v){
        reference1 = FirebaseDatabase.getInstance().getReference("Accounts");
        reference2 = FirebaseDatabase.getInstance().getReference("Accounts");

        reference1.child("Clients").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    //Toast.makeText(MainActivity.this,"SUCCESS", Toast.LENGTH_SHORT).show();
                    DataSnapshot dataSnapshot = task.getResult();
                    String password = String.valueOf(dataSnapshot.child("password").getValue());
                    Log.d("FIREBASE", password);

                    if(!pw.equals(password)){
                        Toast.makeText(MainActivity.this,"Wrong password.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this,"Login Successful", Toast.LENGTH_SHORT).show();
                        goHome(v);
                    }

                } else {
                    Log.i("FIREBASE", "FAIL");
                    reference2.child("Cooks").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.getResult().exists()){
                                //Toast.makeText(MainActivity.this,"SUCCESS", Toast.LENGTH_SHORT).show();
                                DataSnapshot dataSnapshot = task.getResult();
                                String password = String.valueOf(dataSnapshot.child("password").getValue());
                                Log.d("FIREBASE", password);

                                if(!pw.equals(password)){
                                    Toast.makeText(MainActivity.this,"Wrong password.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this,"Login Successful", Toast.LENGTH_SHORT).show();
                                    goHome(v);
                                }

                            } else {
                                Log.i("FIREBASE", "FAIL");
                                Toast.makeText(MainActivity.this,"This user doesn't exist.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void isItCook(String email){
        DatabaseReference cookDB = FirebaseDatabase.getInstance().getReference("Cooks");
    }

    public void goToSignUp(View v) {
        startActivity(new Intent(MainActivity.this,SignUp.class));
    }

    public void goHome(View v) {
        startActivity(new Intent(MainActivity.this,Home.class));
    }
}