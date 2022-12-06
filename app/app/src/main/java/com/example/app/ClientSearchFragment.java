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
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientSearchFragment extends Fragment {

    View view;

    // Firebase database reference
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    // Search
    ListView listView;
    MenuItem[] meals;
    String[] mealsNames;
    ArrayAdapter<String> arrayAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientSearchFragment newInstance(String param1, String param2) {
        ClientSearchFragment fragment = new ClientSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_client_search, container, false);

        // SearchView from XML
        SearchView searchView = (SearchView) view.findViewById(R.id.clientSearchSearchView);
        // ListView from XML
        listView = view.findViewById(R.id.clientSearchListView);

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
                        replaceFragment(MealViewerFragment.newInstance(position));
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