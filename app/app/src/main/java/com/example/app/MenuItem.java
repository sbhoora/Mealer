package com.example.app;

import java.util.ArrayList;

/**
 * MenuItem is a abstraction of any item you could find on a menu.
 * It is to be used only inside the {@link Menu} class.
 */
public class MenuItem {

    private String name;
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

    public MenuItem(String name, Type type, CuisineType cuisineType, ArrayList<String> ingredients,
                    ArrayList<String> allergens, Double price, String description) {
        this.name = name;
        this.type = type;
        this.cuisineType = cuisineType;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.price = price;
        this.description = description;
        
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
