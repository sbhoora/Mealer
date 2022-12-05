package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewCookHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cook_home);

        // Show as default Home fragment on app open
        replaceFragment(new CookHomeFragment());

        // Navigation Bar
        BottomNavigationView navigationBar = findViewById(R.id.cookNavigationBar);

        // Retrieve cook email from sign in
        Intent bundleFromCallingActivity = getIntent();
        String email = bundleFromCallingActivity.getStringExtra("email");

        navigationBar.setOnItemSelectedListener( item -> {
            switch (item.getItemId()) {
                case R.id.menuIcon:
                    replaceFragment(CookMenuFragment.newInstance(email));
                    break;
                case R.id.cookHomeIcon:
                    replaceFragment(new CookHomeFragment());
                    break;
                case R.id.cookProfileIcon:
                    replaceFragment(CookProfileFragment.newInstance(email));
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.cookContentFrame, fragment);
        fragmentTransaction.commit();
    }
}