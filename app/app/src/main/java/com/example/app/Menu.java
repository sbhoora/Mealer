package com.example.app;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is an abstraction of a physical menu.
 * It holds menu items to be used by a {@link Cook}.
 */
public class Menu {

    private String title;
    private HashMap<String, MenuItem> items;

    /**
     * Default Menu constructor.
     */
    public Menu() {}

    /**
     * Constructs a Menu object.
     * @param title
     * @param items
     */
    public Menu(String title, HashMap<String, MenuItem> items) {
        this.title = title;
        this.items = items;
    }

    /**
     * Adds items to Menu.
     * Note that the items are not saved in to the database under the cook's field until
     * {@link Cook#save(Menu) save()} is called.
     * @see Cook
     * @param item
     */
    public void addItem(MenuItem item) {
        items.put(item.getName(), item);
    }

    /**
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return items
     */
    public HashMap<String, MenuItem> getItems() {
        return items;
    }

 /**
     * Adds a MenuItem to the offered meals list.
     */
    public void makeAsOfferedMeal(MenuItem meal) {
        items.get(meal.getName()).makeAsOfferedMeal();
    }

    /**
     * Removes a MenuItem to the offered meals list.
     */
    public void removeFromOfferedMeal(MenuItem meal) {
        items.get(meal.getName()).removeFromOfferedMeal();
    }


    @Override
    public String toString() {
        return "Menu{" +
                "title='" + title + '\'' +
                ", items=" + items +
                '}';
    }
}
