package com.example.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientHistoryFragment extends Fragment {

    View view;

    // Firebase database reference
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    // Search
    ListView listView;
    MenuItem[] meals;
    String[] mealsNames;
    ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_client_history, container, false);

        // Logout Button
        ImageButton logoutButton = view.findViewById(R.id.clientLogoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        // SearchView from XML
        SearchView searchView = (SearchView) view.findViewById(R.id.clientHistorySearchView);
        // ListView from XML
        listView = view.findViewById(R.id.clientHistoryListView);

        // TODO: Need to change this to actually reference meals that were bought by the client
        // Currently, this is just a copy paste of clientSearch. Just to use as template to have
        // access to meals that can be clicked on. Technically, there's not need to make this a search
        // but this was the quickest, simplest way to make a Past Purchases page.
        database.getReference("Meals").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("Firebase", "Meals Retrieval Successful");

                // Getting all offered meals from database
                ArrayList<MenuItem> mealsFromDatabase = new ArrayList<MenuItem>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    mealsFromDatabase.add(ds.getValue(MenuItem.class));
                }

                // Saving offered meals in meals array
                meals = new MenuItem[mealsFromDatabase.size()];
                // All entries from mealsFromDatabase are saved to the meals array
                mealsFromDatabase.toArray(meals);

                // Saving meal names in array
                mealsNames = new String[mealsFromDatabase.size()];
                int i = 0;
                for (MenuItem meal : mealsFromDatabase) {
                    mealsNames[i] = meal.getName();
                    i++;
                }

                // Making arrayAdapter from meals array
                arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, mealsNames);

                // Setting listView to take values from arrayAdapter
                listView.setAdapter(arrayAdapter);

                // Search Related Methods
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // Performs the filtering of meals on user input in the search bar
                        arrayAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        replaceFragment(MealViewerFragment.newInstance(position, getClass().getName()));
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Meals Retrieval Failed");
            }
        });

        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Fragment", getClass().getName() + " Destroyed");
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.clientContentFrame, fragment);
        fragmentTransaction.commit();
    }
}