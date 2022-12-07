package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewClientHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client_home);

        // Retrieve client email from sign in
        Intent bundleFromCallingActivity = getIntent();
        String email = bundleFromCallingActivity.getStringExtra("email");

        // Show as default Home fragment on app open
        replaceFragment(new ClientHomeFragment());

        // Fragment Manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Navigation Bar
        BottomNavigationView navigationBar = findViewById(R.id.clientNavigationBar);

        navigationBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.searchIcon:
                    // Replaces frame with ClientSearchFragment
                    replaceFragment(new ClientSearchFragment());
                    // FragmentResultListener to open a MealViewerFragment from ClientSearchFragment
                    fragmentManager.setFragmentResultListener("openMealInClientSearchFragment", this, new FragmentResultListener() {
                        @Override
                        public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                            // Getting bundle info from Search specifying position of meal in ListView
                            int position = result.getInt("position");
                            // Getting class that wants to open a meal, and thus create a MealViewerFragment
                            String createdBy = result.getString("createdBy");
                            // Creating MealViewerFragment
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.clientContentFrame, MealViewerFragment.newInstance(position,createdBy,email), requestKey);
                            fragmentTransaction.addToBackStack("closeMealViewerFragment");
                            fragmentTransaction.commit();
                        }
                    });
                    break;
                case R.id.homeIcon:
                    // Replaces frame with ClientHomeFragment
                    replaceFragment(new ClientHomeFragment());
                    break;
                case R.id.historyIcon:
                    // Replaces frame with ClientHistoryFragment
                    replaceFragment(new ClientHistoryFragment());
                    // FragmentResultListener to open a MealViewerFragment from ClientHistoryFragment
                    fragmentManager.setFragmentResultListener("openMealInClientHistoryFragment", this, new FragmentResultListener() {
                        @Override
                        public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                            // Getting bundle info from Search specifying position of meal in ListView
                            int position = result.getInt("position");
                            // Getting class that wants to open a meal, and thus create a MealViewerFragment
                            String createdBy = result.getString("createdBy");
                            // Creating MealViewerFragment
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.clientContentFrame, MealViewerFragment.newInstance(position,createdBy,email), requestKey);
                            fragmentTransaction.addToBackStack("closeMealViewerFragment");
                            fragmentTransaction.commit();
                        }
                    });
                    break;
            }
            // FragmentResultListener to close a MealViewerFragment
            fragmentManager.setFragmentResultListener("closeMealViewerFragment", this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                    // Getting bundle info from Search specifying position of meal in ListView
                    Boolean finished = result.getBoolean("finished");
                    if (finished) {
                        // Removing MealViewerFragment from stack and going back to ClientSearchFragment
                        fragmentManager.popBackStack();
                    }
                }
            });
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.clientContentFrame, fragment);
        fragmentTransaction.commit();
    }
}