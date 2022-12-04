package com.example.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewClientHome extends AppCompatActivity {

    //private ActivityNewClientHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client_home);
        //setContentView(binding.getRoot());

        // Show as default Home fragment on app open
        replaceFragment(new ClientHomeFragment());

        // Navigation Bar
        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);

        navigationBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.searchIcon:
                    replaceFragment(new ClientSearchFragment());
                    break;
                case R.id.homeIcon:
                    replaceFragment(new ClientHomeFragment());
                    break;
                case R.id.historyIcon:
                    replaceFragment(new ClientHistoryFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }
}