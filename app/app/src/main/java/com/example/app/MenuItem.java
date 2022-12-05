package com.example.app;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;

/**
 * MenuItem is a abstraction of any item you could find on a menu.
 * It is to be used only inside the {@link Menu} class.
 */
public class MenuItem {

    private String name;
    private String cookEmail;
    private Type type;
    private CuisineType cuisineType;
    private ArrayList<String> ingredients;
    private ArrayList<String> allergens;
    private double price;
    private String description;

    /**
     * Default MenuItem constructor.
     */
    public MenuItem() {}

    public MenuItem(String name, String cookEmail, Type type, CuisineType cuisineType, ArrayList<String> ingredients,
                    ArrayList<String> allergens, Double price, String description) {
        this.name = name;
        this.cookEmail = cookEmail;
        this.type = type;
        this.cuisineType = cuisineType;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.price = price;
        this.description = description;
        
    }

    public static void updateOfferedMealsOnDatabase(Cook cook, Menu menu) {
        // Firebase database reference
        DatabaseReference mealsBranch = FirebaseDatabase.getInstance().getReference("Meals");
        mealsBranch.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    // All offered items that have been added by all cooks
                    ArrayList<MenuItem> allOfferedMeals = new ArrayList<MenuItem>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        allOfferedMeals.add(ds.getValue(MenuItem.class));
                    }

                    // updating the list of offered meals
                    if (allOfferedMeals != null) {
                        // Removing all meals of a cook from the Meals branch if they are in it
                        ArrayList<MenuItem> tempMealsToRemove = new ArrayList<MenuItem>();
                        for (MenuItem meal : allOfferedMeals) {
                            if (meal.getCookEmail().equals(cook.getEmail())) {
                                tempMealsToRemove.add(meal);
                            }
                        }
                        allOfferedMeals.removeAll(tempMealsToRemove);
                        // Adding all of the cook's new meals
                        allOfferedMeals.addAll(menu.getOfferedMeals().values());
                        // This remove-add is done because it is not known which meals are new and which
                        // are old. So, to be sure. Remove all the ones with the cook's email first.
                        // This guarantees any old item is removed. Then, add only the meals the cook
                        // either added as new, or that were kept.

                    }
                    mealsBranch.setValue(allOfferedMeals);
                    Log.i("Firebase", "Offered meals update successful.");
                } else {
                    Log.e("Firebase", "Offered meals update failed.");
                }
            }
        });
    }

    // Menu Item interaction
    public void addAllergen(String allergen) {
        if (!allergens.contains(allergen)) {
            allergens.add(allergen);
        }
    }

    public void removeAllergen(String allergen) {
        if (allergens.contains(allergen)) {
            allergens.remove(allergen);
        }
    }

    public void addIngredient(String ingredient) {
        if (!ingredients.contains(ingredient)) {
            ingredients.add(ingredient);
        }
    }

    public void removeIngredient(String ingredient) {
        if (ingredients.contains(ingredient)) {
            ingredients.remove(ingredient);
        }
    }

    //Getters
    public String getName() {
        return name;
    }

    public String getCookEmail() {
        return cookEmail;
    }

    /**
     * Returns the type of meal. Type can be things like drinks, entrée, or appetizer etc.
     * @return String
     */
    public String getType() {
        return type.name();
    }

        /**
     * Returns the cuisine type of meal. Type can be things like Italian, Chinese, Greek etc.
     * @return String
     */
    public String getCuisineType() {
        return cuisineType.name();
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public ArrayList<String> getAllergens() {
        return allergens;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    //Setters
    public void setName(String newName) {
        this.name = newName;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setCuisineType(CuisineType cuisineType) { this.cuisineType = cuisineType; }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setAllergens(ArrayList<String> allergens) {
        this.allergens = allergens;
    }

    public void setPrice(Double newPrice) {
        this.price = newPrice;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "name='" + name + '\'' +
                ", types=" + type.name() +
                ", cuisineTypes=" + cuisineType.name() +
                ", type='" + type + '\'' +
                ", cuisineType='" + cuisineType + '\'' +
                ", ingredients=" + ingredients +
                ", allergens=" + allergens +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}
