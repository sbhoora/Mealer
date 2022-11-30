package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MealCreation extends AppCompatActivity {
    private Cook cook;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_creation);
        Button createMealButton = (Button) findViewById(R.id.createMealButton);

        // Getting cook object from CookHome from Intent activity call
        Intent fromCookHome = getIntent();
        String emailFromCookHome = fromCookHome.getStringExtra("email");
        cook = new Cook(emailFromCookHome);

        createMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MEALCREATION","HERE");
                String name = ((EditText) findViewById(R.id.nameText)).getText().toString();
                int type = ((Spinner) findViewById(R.id.typeSpinner)).getSelectedItemPosition();
                int cuisineType = ((Spinner) findViewById(R.id.cuisineTypeSpinner)).getSelectedItemPosition();
                String[] ingredients = ((EditText) findViewById(R.id.ingredientsText)).getText().toString().split(",");

                // DATA VALIDATION STUFF
                EditText[] texts = { (EditText) findViewById(R.id.nameText), (EditText) findViewById(R.id.ingredientsText),
                        (EditText) findViewById(R.id.allergensText), (EditText) findViewById(R.id.priceText),
                        (EditText) findViewById(R.id.descriptionText)};

                boolean empty = false;
                for (EditText text : texts) {
                    if (TextUtils.isEmpty(text.getText().toString())) {
                        empty = true;
                        text.setError("Please fill in this field");
                    }
                }

                if(empty==false) {
                    //GET INFO FROM FIELDS STUFF
                    for (int i = 0; i < ingredients.length; i++) {
                        ingredients[i] = ingredients[i].trim();
                    }
                    String[] allergens = ((EditText) findViewById(R.id.allergensText)).getText().toString().split(",");
                    for (int i = 0; i < allergens.length; i++) {
                        allergens[i] = allergens[i].trim();
                    }
                    double price = Double.parseDouble(((EditText) findViewById(R.id.priceText)).getText().toString());
                    String description = ((EditText) findViewById(R.id.descriptionText)).getText().toString();

                    // DATABASE STUFF
                    cook.getMenu(new Cook.FirebaseMenuCallback() {
                        @Override
                        public void onCallBack(Menu menu) {
                            item = new MenuItem(name, Type.values()[type], CuisineType.values()[cuisineType],
                                    new ArrayList<>(Arrays.asList(ingredients)),
                                    new ArrayList<>(Arrays.asList(allergens)), price, description);
                            if (menu == null) {
                                menu = new Menu();
                            }
                            menu.addAsNotOfferedMeal(item);
                            cook.save(menu);
                            Toast.makeText(MealCreation.this,"New Meal Added", Toast.LENGTH_SHORT).show();
                            MealCreation.this.finish();
                        }
                    });

                }
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MealCreation.this.finish();
            }
        });

    }
}