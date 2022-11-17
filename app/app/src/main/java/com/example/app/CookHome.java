package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CookHome extends AppCompatActivity {

    private ListView mealListView;
    private DatabaseReference database;
    private Cook cook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_home);

        // Buttons
        MaterialButton signOutButton = (MaterialButton) findViewById(R.id.signOutButton);
        Button addMealButton = (Button) findViewById(R.id.addMealButton);

        // Value retrieved from SignIn for the cook that just signed in
        String email = getIntent().getStringExtra("email");
        cook = new Cook(email);

        HashMap<String, MenuItem> testMeals = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            ArrayList<String> ingredients = new ArrayList<>();
            ingredients.add("Salt");
            ingredients.add("Water");

            ArrayList<String> allergens = new ArrayList<>();
            allergens.add("Pollen");

            MenuItem item = new MenuItem("gross" + i, Types.DRINK, CuisineTypes.AMERICAN,
                    ingredients, allergens, 25.0, "overpriced salt water");

            testMeals.put(item.getName(), item);
        }
        Menu menu = new Menu(testMeals);
        //cook.save(menu);

        cook.getMenu();

        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CookHome.this, MealCreation.class));
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

        // ARRAY OF MEALS
        Menu elMenu = cook.getMenu();
        MenuItem offered[] = elMenu.getOfferedMeals().values().toArray(new MenuItem[0]);
        MenuItem notOffered[] = cook.getMenu().getNotOfferedMeals().values().toArray(new MenuItem[0]);
        // ARRAY OF MEAL TITLESa
        String offeredTitle[] = cook.getMenu().getOfferedMeals().keySet().toArray(new String[0]);
        String notOfferedTitle[] = cook.getMenu().getNotOfferedMeals().keySet().toArray(new String[0]);
//        // (IF YOU WANT TO SHOW THEIR OFFERED STATUS SIMPLY APPEND (OFFERED) TO THE END OF THE TITLE STRING)

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(cntx, R.layout.complaint_list_item, R.id.textView, offeredTitle);
        mealListView.setAdapter(arrayAdapter);

        mealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(view.getContext(), R.style.AlertDialogTheme));
                // USE THE ARRAY OF MEALS FROM ABOVE AT INDEX i TO GET THE DATA OF THIS MEAL
                builder.setTitle("Manage Meal");

                final CheckBox input = new CheckBox(view.getContext());
                input.setText("Offered");
                builder.setView(input);

                builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        boolean newOffered = input.isChecked();
                        // APPLY THE CHANGES TO THE DATABASE

                        dialog.cancel();
                        update();
                    }
                });
                builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // DELETE MEAL FROM DATABASE

                        dialog.cancel();
                        update();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}