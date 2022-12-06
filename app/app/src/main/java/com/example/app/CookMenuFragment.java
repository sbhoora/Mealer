package com.example.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CookMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CookMenuFragment extends Fragment {

    View view;

    private ListView mealListView;
    private DatabaseReference database;
    private Cook cook;

    // the fragment initialization parameter
    private static final String EMAIL = "email";
    // the string instance variable storing the email
    private String email;

    public CookMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email Parameter 1.
     * @return A new instance of fragment CookProfileFragment.
     */
    public static CookMenuFragment newInstance(String email) {
        CookMenuFragment fragment = new CookMenuFragment();
        Bundle args = new Bundle();
        args.putString(EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cook_menu, container, false);

        Button addMealButton = (Button) view.findViewById(R.id.addMealButton);

        cook = new Cook(email);

        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to start activity
                Intent cookToMealCreation = new Intent(Intent.ACTION_SENDTO);
                cookToMealCreation.setClass(getActivity(), MealCreation.class);
                // Bundle of elements to be passed to MealCreation
                Bundle info = new Bundle();
                info.putString("email", email);
                cookToMealCreation.putExtras(info);
                // Start Meal Creation
                startActivity(cookToMealCreation);
                update();
            }
        });

        mealListView = (ListView) view.findViewById(R.id.mealListView);

        update();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Fragment", getClass().getName() + " Destroyed");
    }

    private void update(){
        cook.getMenu(new Cook.FirebaseMenuCallback() {
            @Override
            public void onCallBack(Menu menu) {
                ArrayList<String> tempMealTitles = new ArrayList<String>();
                ArrayList<MenuItem> tempItems = new ArrayList<MenuItem>();
                Menu myMenu = menu;
                if(menu != null) {
                    if (menu.getOfferedMeals() != null) {
                        tempItems.addAll(new ArrayList<>(Arrays.asList(menu.getOfferedMeals().values().toArray(new MenuItem[0]))));
                        tempMealTitles.addAll(new ArrayList<>(Arrays.asList(menu.getOfferedMeals().keySet().toArray(new String[0]))));
                    }
                    if (menu.getNotOfferedMeals() != null) {
                        tempItems.addAll(new ArrayList<>(Arrays.asList(menu.getNotOfferedMeals().values().toArray(new MenuItem[0]))));
                        tempMealTitles.addAll(new ArrayList<>(Arrays.asList(menu.getNotOfferedMeals().keySet().toArray(new String[0]))));
                    }
                }
                String[] mealTitles = tempMealTitles.toArray(new String[tempMealTitles.size()]);
                MenuItem[] items = tempItems.toArray(new MenuItem[tempItems.size()]);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.complaint_list_item, R.id.textView, mealTitles);
                mealListView.setAdapter(arrayAdapter);

                mealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertDialogTheme));
                        // USE THE ARRAY OF MEALS FROM ABOVE AT INDEX i TO GET THE DATA OF THIS MEAL
                        MenuItem item = items[i];
                        String name, type, cuisineType, description;
                        name = item.getName();
                        type = item.getType();
                        cuisineType = item.getCuisineType();
                        description = item.getDescription();

                        builder.setTitle(name);
                        builder.setMessage(String.format("Type: %s\nCuisine Type: %s\nDescription: %s\n", type, cuisineType, description));

                        final CheckBox input = new CheckBox(view.getContext());
                        if(menu.isInNotOffered(item)){
                            input.setText("Offered");
                        }else{
                            input.setText("Not Offered");
                        }
                        builder.setView(input);

                        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("APPLY","HERE");
                                boolean newOffered = input.isChecked();
                                // APPLY THE CHANGES TO THE DATABASE
                                if(input.isChecked()&&input.getText().equals("Offered")){
                                    menu.removeFromNotOfferedMeals(item);
                                    cook.save(menu);
                                    Toast.makeText(getActivity(),"This meal has been moved to Offered Meals.", Toast.LENGTH_SHORT).show();
                                }else if(input.isChecked()&&input.getText().equals("Not Offered")){
                                    menu.removeFromOfferedMeals(item);
                                    cook.save(menu);
                                    Toast.makeText(getActivity(),"This meal has been moved to Not Offered Meals.", Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();
                                update();
                            }
                        });
                        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("DISMISS","HERE");
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("DELETE","HERE");
                                // DELETE MEAL FROM DATABASE
                                if(!menu.deleteMeal(item)){
                                    Toast.makeText(getActivity(),"This meal cannot be deleted.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity(),"This meal has been deleted.", Toast.LENGTH_SHORT).show();
                                }
                                cook.save(menu);
                                dialog.cancel();
                                update();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });
    }
}