package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

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

        navigationBar.setOnItemSelectedListener( item -> {
            switch (item.getItemId()) {
                case R.id.menuIcon:
                    replaceFragment(new CookMenuFragment());
                    break;
                case R.id.cookHomeIcon:
                    replaceFragment(new CookHomeFragment());
                    break;
                case R.id.cookProfileIcon:
                    replaceFragment(new CookProfileFragment());
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