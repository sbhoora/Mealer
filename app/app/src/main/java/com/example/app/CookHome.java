package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.app.AlertDialog;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class CookHome extends AppCompatActivity {

    private ListView mealListView;
    private DatabaseReference database;
    private Cook cook;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_home);

        // Buttons
        MaterialButton signOutButton = (MaterialButton) findViewById(R.id.signOutButton);
        Button addMealButton = (Button) findViewById(R.id.addMealButton);

        // Value retrieved from SignIn for the cook that just signed in
        email = getIntent().getStringExtra("email");

        cook = new Cook(email);

        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to start activity
                Intent cookToMealCreation = new Intent(Intent.ACTION_SENDTO);
                cookToMealCreation.setClass(CookHome.this, MealCreation.class);
                // Bundle of elements to be passed to MealCreation
                Bundle info = new Bundle();
                info.putString("email", email);
                cookToMealCreation.putExtras(info);
                // Start Meal Creation
                startActivity(cookToMealCreation);
                update();
            }
        });

        mealListView = (ListView) findViewById(R.id.mealListView);

        update();

        // Signs out user
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CookHome.this.finish();
            }
        });
    }
    private void update(){
        Context cntx = this;
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

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(cntx, R.layout.complaint_list_item, R.id.textView, mealTitles);
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
                                    Toast.makeText(CookHome.this,"This meal has been moved to Offered Meals.", Toast.LENGTH_SHORT).show();
                                }else if(input.isChecked()&&input.getText().equals("Not Offered")){
                                    menu.removeFromOfferedMeals(item);
                                    cook.save(menu);
                                    Toast.makeText(CookHome.this,"This meal has been moved to Not Offered Meals.", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(CookHome.this,"This meal cannot be deleted.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(CookHome.this,"This meal has been deleted.", Toast.LENGTH_SHORT).show();
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