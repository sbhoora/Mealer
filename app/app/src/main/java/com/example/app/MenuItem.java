package com.example.app;

/**
 * MenuItem is a abstraction of any item you could find on a menu.
 * It is to be used only inside the {@link Menu} class.
 */
public class MenuItem {

    private String name;
    private ArrayList<String> types;
    private ArrayList<String> cuisineTypes;
    private String type;
    private String cuisineType;
    private ArrayList<String> ingredients;
    private ArrayList<String> allergens;
    private double price;
    private String description;

    private Boolean offeredMeal = false;

    /**
     * Default MenuItem constructor.
     */
    public MenuItem() {}

    public MenuItem(String name, String type, String cuisineType, ArrayList<String> ingredients, 
    ArrayList<String> allergens, Double price, String description) {
        this.name = name;
        this.type = type;
        this.cuisineType = cuisineType;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.price = price;
        this.description = description;
        
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
        return type;
    }

        /**
     * Returns the cuisine type of meal. Type can be things like Italian, Chinese, Greek etc.
     * @return String
     */
    public String getCuisineType() {
        return cuisineType;
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

    public void setType(String type) {
        if (types.contains(type)) {
            this.type = type;
        }
        else {
            types.add(type);
            this.type = type;
        }
    }

    public void setCuisineType(String type) {
        if (cuisineTypes.contains(type)) {
            this.cuisineType = type;
        }
        else {
            cuisineTypes.add(type);
            this.cuisineType = type;
        }
    }

    public void addAllergens(String allergen) {
        if (!allergens.contain(allergen)) {
            allergens.add(allergen);
        }
    }

    public void removeAllergens(String allergen) {
        if (allergens.contain(allergen)) {
            allergens.remove(allergen);
        }
    }

    public void addIngredients(String ingredient) {
        if (!ingredients.contain(ingredient)) {
            ingredients.add(ingredient);
        }
    }

    public void removeIngredients(String ingredient) {
        if (ingredients.contain(ingredient)) {
            ingredients.remove(ingredient);
        }
    }

    public void setPrice(Double newPrice) {
        this.price = newPrice;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }


    /**
     * Adds a MenuItem to the offered meals list.
     */
    public void makeAsOfferedMeal() {
        offeredMeal = true;
    }

    /**
     * Removes a MenuItem to the offered meals list.
     */
    public void removeFromOfferedMeal() {
        offeredMeal = false;
    }

    public boolean isOfferedMeal() {
        return offeredMeal;
    }


    @Override
    public String toString() {
        return "MenuItem{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
