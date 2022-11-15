package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MealCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_creation);

        Button createMealButton = (Button) findViewById(R.id.createMealButton);

        createMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText) findViewById(R.id.nameText)).getText().toString();
                String type = ((EditText) findViewById(R.id.typeText)).getText().toString();
                String cuisineType = ((EditText) findViewById(R.id.cuisineTypeText)).getText().toString();
                String[] ingredients = ((EditText) findViewById(R.id.ingredientsText)).getText().toString().split(",");
                for (int i=0; i<ingredients.length; i++) {
                    ingredients[i] = ingredients[i].trim();
                }
                String[] alergens = ((EditText) findViewById(R.id.alergensText)).getText().toString().split(",");
                for (int i=0; i<alergens.length; i++) {
                    alergens[i] = alergens[i].trim();
                }
                double price = Double.parseDouble(((EditText) findViewById(R.id.priceText)).getText().toString());
                String description = ((EditText) findViewById(R.id.descriptionText)).getText().toString();
                // DATA VALIDATION

                // DATABASE STUFF

                MealCreation.this.finish();
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