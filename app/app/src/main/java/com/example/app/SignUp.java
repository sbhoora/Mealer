package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView email = (TextView) findViewById(R.id.email);
        TextView password = (TextView) findViewById(R.id.password);

        MaterialButton signInButton = (MaterialButton) findViewById(R.id.signupbutton);

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