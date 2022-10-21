package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Home extends AppCompatActivity {
    DatabaseReference ref;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView signup = (TextView) findViewById(R.id.signup);
        userType(signup);
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