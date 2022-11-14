package com.example.app;

/**
 * MenuItem is a abstraction of any item you could find on a menu.
 * It is to be used only inside the {@link Menu} class.
 */
public class MenuItem {

    private String name;
    private String description;
    private String type;
    private Boolean offeredMeal = false;

    /**
     * Default MenuItem constructor.
     */
    public MenuItem() {}

    public MenuItem(String name, String description, String type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public void makeAsOfferedMeal() {
        offeredMeal = true;
    }

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
